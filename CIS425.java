package cis425;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CIS425 extends Application {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cis425";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Conference System");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // User Type Dropdown
        Label userTypeLabel = new Label("User Type:");
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Author", "Reviewer", "Conference Chair");
        userTypeComboBox.setValue("Author");

        // Username
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        // Password
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(primaryStage, userTypeComboBox.getValue(), usernameField.getText(), passwordField.getText()));

        // Add elements to the grid
        grid.add(userTypeLabel, 0, 0);
        grid.add(userTypeComboBox, 1, 0);
        grid.add(usernameLabel, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(Stage primaryStage, String userType, String username, String password) {
        boolean isAuthenticated = authenticateUser(userType, username, password);

        if (isAuthenticated) {
            openDashboard(primaryStage, userType);
        } else {
            showErrorAlert("Login Error", "Invalid username or password.");
        }
    }

    private boolean authenticateUser(String userType, String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String query;
            switch (userType) {
                case "Author" -> query = "SELECT * FROM Author WHERE username = ? AND password = ?";
                case "Reviewer" -> query = "SELECT * FROM Reviewer WHERE username = ? AND password = ?";
                case "Conference Chair" -> query = "SELECT * FROM ConferenceChair WHERE username = ? AND password = ?";
                default -> {
                    return false;
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private void openDashboard(Stage primaryStage, String userType) {
        switch (userType) {
            case "Author" -> openAuthorDashboard(primaryStage);
            case "Reviewer" -> openReviewerDashboard(primaryStage);
            case "Conference Chair" -> openConferenceChairDashboard(primaryStage);
            default -> showErrorAlert("Error", "Invalid user type");
        }
    }

    private void openAuthorDashboard(Stage primaryStage) {
        primaryStage.setTitle("Author Dashboard");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label welcomeLabel = new Label("Welcome, Author!");
        grid.add(welcomeLabel, 0, 0);

        // Dropdown for selecting conferences
        ComboBox<String> conferenceDropdown = new ComboBox<>();
        // Placeholder conference names
        conferenceDropdown.getItems().addAll("Conference 1", "Conference 2", "Conference 3");
        conferenceDropdown.setPromptText("Select Conference");
        grid.add(conferenceDropdown, 0, 1);

        // Dummy button for uploading files
        Button uploadButton = new Button("Upload File");
        uploadButton.setOnAction(e -> handleUpload());
        grid.add(uploadButton, 0, 2);

        // Dummy button for submitting papers
        Button submitButton = new Button("Submit Paper");
        submitButton.setOnAction(e -> handleSubmit());
        grid.add(submitButton, 0, 3);

        // Dummy button for viewing papers
        Button viewButton = new Button("View Papers");
        viewButton.setOnAction(e -> handleView());
        grid.add(viewButton, 0, 4);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
    }

    private void handleUpload() {
            // Placeholder logic for file upload
            System.out.println("File upload button clicked. (Placeholder)");
        }

    private void handleSubmit() {
        try {
        // Assuming you have the necessary information (authorID, conferenceID) available
        int authorID = 1; // Replace with the actual authorID
        int conferenceID = 1; // Replace with the actual conferenceID

        // Get the title from the user (you may want to use JavaFX input dialog or some other method)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Submit Paper");
        dialog.setHeaderText("Enter the title for your paper:");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            String title = result.get();

            try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
                // Generate a random paperID between 1 and 30
                int paperID = generateRandomID();

                // Insert a new record into the Paper table
                String insertPaperSQL = "INSERT INTO Paper (paperID, title, status, conferenceID, authorID) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement insertPaperStatement = connection.prepareStatement(insertPaperSQL)) {
                insertPaperStatement.setInt(1, paperID);
                insertPaperStatement.setString(2, title);
                insertPaperStatement.setString(3, "pending");
                insertPaperStatement.setInt(4, conferenceID);
                insertPaperStatement.setInt(5, authorID);

                insertPaperStatement.executeUpdate();
            }

            System.out.println("Paper submitted successfully. Title: " + title + ", PaperID: " + paperID);
            } 
        catch (SQLException e) {
        }
        }
        else {
            System.out.println("Title is required.");
        }
    } 
        catch (Exception e) {
        }
    }

    private void handleView() {
        // Placeholder logic for viewing papers
        System.out.println("View papers button clicked. (Placeholder)");
    }


    private void openReviewerDashboard(Stage primaryStage) {
        primaryStage.setTitle("Reviewer Dashboard");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label welcomeLabel = new Label("Welcome, Reviewer!");
        grid.add(welcomeLabel, 0, 0);

            // Dropdown for selecting papers (papers will be pulled from the database)
        ComboBox<String> paperDropdown = new ComboBox<>();
        List<String> paperTitles = getPaperTitlesFromDatabase();
        paperDropdown.getItems().addAll(paperTitles);
        paperDropdown.setPromptText("Select Paper");
        grid.add(paperDropdown, 0, 1);


        // Dummy button for downloading selected paper
        Button downloadButton = new Button("Download Paper");
        downloadButton.setOnAction(e -> handleDownload(paperDropdown.getValue()));
        grid.add(downloadButton, 0, 2);

        // Dummy buttons for decision
        Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(e -> handleDecision(paperDropdown.getValue(), "Accept"));
        grid.add(acceptButton, 0, 3);

        Button neutralButton = new Button("Neutral");
        neutralButton.setOnAction(e -> handleDecision(paperDropdown.getValue(), "Neutral"));
        grid.add(neutralButton, 0, 4);

        Button rejectButton = new Button("Reject");
        rejectButton.setOnAction(e -> handleDecision(paperDropdown.getValue(), "Reject"));
        grid.add(rejectButton, 0, 5);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
    }
    private List<String> getPaperTitlesFromDatabase() {
        List<String> paperTitles = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String selectTitlesSQL = "SELECT title FROM Paper";
                try (PreparedStatement preparedStatement = connection.prepareStatement(selectTitlesSQL);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        paperTitles.add(resultSet.getString("title"));
                    }
                    }
        }  
        catch (SQLException e) {
        }
            return paperTitles;
    }

    private void handleDownload(String selectedPaper) {
            // Placeholder logic for downloading selected paper
        System.out.println("Download button clicked for paper: " + selectedPaper + " (Placeholder)");
        }

    private void handleDecision(String selectedPaper, String decision) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            // Get the paperID for the selected paper
            String selectPaperIDSQL = "SELECT paperID FROM Paper WHERE title = ?";
            int paperID;
            try (PreparedStatement paperIDStatement = connection.prepareStatement(selectPaperIDSQL)) {
                paperIDStatement.setString(1, selectedPaper);
            try (ResultSet paperIDResultSet = paperIDStatement.executeQuery()) {
                if (paperIDResultSet.next()) {
                    paperID = paperIDResultSet.getInt("paperID");

                    // Insert a new record into the Review table
                    String insertReviewSQL = "INSERT INTO Review (reviewID, recommendation, paperID, reviewerID) VALUES (?, ?, ?, ?)";
                    int reviewID = generateRandomID(); 
                    int reviewerID = 1; // Replace with the actual reviewerID

                    try (PreparedStatement insertReviewStatement = connection.prepareStatement(insertReviewSQL)) {
                        insertReviewStatement.setInt(1, reviewID);
                        insertReviewStatement.setString(2, decision);
                        insertReviewStatement.setInt(3, paperID);
                        insertReviewStatement.setInt(4, reviewerID);
                        insertReviewStatement.executeUpdate();
                    }

                    System.out.println("Decision made for paper: " + selectedPaper + " - " + decision);
                } else {
                    System.out.println("Paper not found.");
                }
            }
        }
    } catch (SQLException e) {
    }
}
    private int generateRandomID() {
        Random random = new Random();
        return random.nextInt(30) + 1;
}

    private void openConferenceChairDashboard(Stage primaryStage) {
        primaryStage.setTitle("Conference Chair Dashboard");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label welcomeLabel = new Label("Welcome, Conference Chair!");
        grid.add(welcomeLabel, 0, 0);

        // Button to go to the page for paper selection
        Button selectPaperButton = new Button("Select Paper");
        selectPaperButton.setOnAction(e -> openPaperSelectionPage(primaryStage));
        grid.add(selectPaperButton, 0, 1);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
}

    private void openPaperSelectionPage(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label selectPaperLabel = new Label("Select Paper:");
        ComboBox<String> paperDropdown = new ComboBox<>();
        List<String> paperTitles = getPaperTitlesFromDatabase();
        paperDropdown.getItems().addAll(paperTitles);
        paperDropdown.setPromptText("Select Paper");

        grid.add(selectPaperLabel, 0, 0);
        grid.add(paperDropdown, 1, 0);

        // Button to go to the page for reviewing
        Button viewReviewsButton = new Button("View Reviews");
        viewReviewsButton.setOnAction(e -> openReviewPage(primaryStage, paperDropdown.getValue()));
        grid.add(viewReviewsButton, 0, 1);

        Scene scene = new Scene(grid, 400, 150);
        primaryStage.setScene(scene);
}

    private void openReviewPage(Stage primaryStage, String selectedPaper) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label paperTitleLabel = new Label("Paper Title: " + selectedPaper);
        grid.add(paperTitleLabel, 0, 0);

        // Fetch recommendations from the Review table based on the paperID
        int paperID = getPaperIDFromTitle(selectedPaper);
        List<String> recommendations = getRecommendationsFromDatabase(paperID);

        int row = 1;
        for (int i = 0; i < recommendations.size(); i++) {
            TextArea reviewerTextArea = new TextArea();
            reviewerTextArea.setEditable(false);
            grid.add(new Label("Recommendation Reviewer " + (i + 1) + ":"), 0, row++);
            grid.add(reviewerTextArea, 0, row++);
            reviewerTextArea.setText("Reviewer " + (i + 1) + ": " + recommendations.get(i));
        }

        // Buttons for final decision
        Button publishButton = new Button("Publish");
        Button doNotPublishButton = new Button("Do Not Publish");

        publishButton.setOnAction(e -> {
            handleFinalDecision(selectedPaper, "Publish");
            showFeedback("Paper Published Successfully!");
        });
        doNotPublishButton.setOnAction(e -> {
            handleFinalDecision(selectedPaper, "Do Not Publish");
            showFeedback("Paper Not Published.");
        });

        grid.add(publishButton, 0, row);
        grid.add(doNotPublishButton, 1, row);

        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
    }

    private void showFeedback(String message) {
        Alert feedbackAlert = new Alert(Alert.AlertType.INFORMATION);
        feedbackAlert.setTitle("Feedback");
        feedbackAlert.setHeaderText(null);
        feedbackAlert.setContentText(message);
        feedbackAlert.showAndWait();
    }

    private int getPaperIDFromTitle(String title) {
        int paperID = 0;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String selectPaperIDSQL = "SELECT paperID FROM Paper WHERE title = ?";
            try (PreparedStatement paperIDStatement = connection.prepareStatement(selectPaperIDSQL)) {
                paperIDStatement.setString(1, title);
                try (ResultSet paperIDResultSet = paperIDStatement.executeQuery()) {
                    if (paperIDResultSet.next()) {
                        paperID = paperIDResultSet.getInt("paperID");
                    } else {
                        System.out.println("Paper not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paperID;
    }

    private List<String> getRecommendationsFromDatabase(int paperID) {
        List<String> recommendations = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String selectRecommendationsSQL = "SELECT recommendation FROM Review WHERE paperID = ?";
            try (PreparedStatement recommendationsStatement = connection.prepareStatement(selectRecommendationsSQL)) {
                recommendationsStatement.setInt(1, paperID);
                try (ResultSet recommendationsResultSet = recommendationsStatement.executeQuery()) {
                    while (recommendationsResultSet.next()) {
                        recommendations.add(recommendationsResultSet.getString("recommendation"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendations;
    }

private void handleFinalDecision(String selectedPaper, String finalDecision) {
    try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
        // Get the paperID for the selected paper
        String selectPaperIDSQL = "SELECT paperID FROM Paper WHERE title = ?";
        int paperID;
        try (PreparedStatement paperIDStatement = connection.prepareStatement(selectPaperIDSQL)) {
            paperIDStatement.setString(1, selectedPaper);
            try (ResultSet paperIDResultSet = paperIDStatement.executeQuery()) {
                if (paperIDResultSet.next()) {
                    paperID = paperIDResultSet.getInt("paperID");

                    // Update the status of the paper in the Paper table
                    String updatePaperStatusSQL = "UPDATE Paper SET status = ? WHERE paperID = ?";

                    try (PreparedStatement updatePaperStatusStatement = connection.prepareStatement(updatePaperStatusSQL)) {
                        updatePaperStatusStatement.setString(1, finalDecision);
                        updatePaperStatusStatement.setInt(2, paperID);

                        updatePaperStatusStatement.executeUpdate();
                    }

                    System.out.println("Final decision for paper " + selectedPaper + ": " + finalDecision);
                } else {
                    System.out.println("Paper not found.");
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
