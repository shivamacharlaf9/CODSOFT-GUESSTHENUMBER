import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class GuessTheNumberGUI {
    private int numberToGuess;
    private int numberOfTries;
    private final Random random = new Random();
    private final JTextField guessField;
    private final JLabel resultLabel;
    private final JLabel triesLabel;
    private final JButton guessButton;
    private final JButton resetButton;
    private final Timer timer;
    private final JLabel timerLabel;
    private Timer countdownTimer;
    private int remainingTime = 60;
    private boolean isGameOver = false;

    public GuessTheNumberGUI() {
        // Setup JFrame
        JFrame frame = new JFrame("Guess The Number Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Panel for instructions and input
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(4, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        frame.add(topPanel, BorderLayout.NORTH);

        // Instructions
        JLabel instructions = new JLabel("I have chosen a number between 1 and 100. Try to guess it!", SwingConstants.CENTER);
        instructions.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(instructions);

        // Input field for guesses
        guessField = new JTextField();
        guessField.setFont(new Font("Arial", Font.PLAIN, 14));
        topPanel.add(guessField);

        // Button to submit guess
        guessButton = new JButton("Guess");
        guessButton.setBackground(new Color(0, 153, 255)); // Light Blue
        guessButton.setForeground(Color.WHITE);
        guessButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        guessButton.setIcon(new ImageIcon("path/to/guess/icon.png"));
        topPanel.add(guessButton);

        // Panel for result and tries
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        frame.add(centerPanel, BorderLayout.CENTER);

        // Label for result
        resultLabel = new JLabel("Enter your guess above!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(resultLabel);

        // Label to show number of tries
        triesLabel = new JLabel("Tries: 0", SwingConstants.CENTER);
        triesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(triesLabel);

        // Panel for timer and buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Timer label
        timerLabel = new JLabel("Time remaining: 60s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomPanel.add(timerLabel);

        // Reset button
        resetButton = new JButton("Reset");
        resetButton.setBackground(Color.RED);
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        resetButton.setIcon(new ImageIcon("path/to/reset/icon.png"));
        resetButton.setEnabled(false);
        bottomPanel.add(resetButton);

        // Generate a random number to guess
        resetGame();

        // Lambda for the guess button action
        guessButton.addActionListener(e -> handleGuess());

        // Reset button logic
        resetButton.addActionListener(e -> resetGame());

        // Set timer for the game (60 seconds)
        timer = new Timer(60000, e -> {
            if (!isGameOver) {
                resultLabel.setText("Time's up! The number was " + numberToGuess);
                guessButton.setEnabled(false);
                resetButton.setEnabled(true);
                isGameOver = true;
            }
        });
        timer.setRepeats(false);
        timer.start();

        // Initialize countdown timer
        countdownTimer = new Timer(1000, e -> {
            remainingTime--;
            timerLabel.setText("Time remaining: " + remainingTime + "s");
            if (remainingTime <= 0) {
                countdownTimer.stop();
                resultLabel.setText("Time's up! The number was " + numberToGuess);
                guessButton.setEnabled(false);
                resetButton.setEnabled(true);
                isGameOver = true;
            }
        });
        countdownTimer.start();

        frame.setVisible(true);
    }

    private void handleGuess() {
        if (isGameOver) return;

        try {
            int guess = Integer.parseInt(guessField.getText());
            numberOfTries++;

            if (guess < 1 || guess > 100) {
                resultLabel.setText("Please enter a number between 1 and 100.");
            } else if (guess < numberToGuess) {
                int difference = numberToGuess - guess;
                resultLabel.setText("Your guess is too low. You are off by " + difference + ".");
            } else if (guess > numberToGuess) {
                int difference = guess - numberToGuess;
                resultLabel.setText("Your guess is too high. You are off by " + difference + ".");
            } else {
                resultLabel.setText("Congratulations! You guessed the number!");
                guessButton.setEnabled(false);
                resetButton.setEnabled(true);
                timer.stop();  // Stop the timer when the game is won
                countdownTimer.stop();
                isGameOver = true;
            }

            triesLabel.setText("Tries: " + numberOfTries);

            // Provide hints after 5 and 10 tries
            if (numberOfTries == 5) {
                resultLabel.setText("Hint: The number is " + (numberToGuess % 2 == 0 ? "even." : "odd."));
            } else if (numberOfTries == 10) {
                resultLabel.setText("Hint: The number is " + (numberToGuess % 5 == 0 ? "divisible by 5." : "not divisible by 5."));
            }

            // Limit to 10 tries
            if (numberOfTries >= 10 && guess != numberToGuess) {
                resultLabel.setText("Game Over! You've reached the maximum number of tries. The number was " + numberToGuess);
                guessButton.setEnabled(false);
                resetButton.setEnabled(true);
                isGameOver = true;
            }

        } catch (NumberFormatException ex) {
            resultLabel.setText("Please enter a valid number.");
        }
    }

    private void resetGame() {
        numberToGuess = random.nextInt(100) + 1;
        numberOfTries = 0;
        guessField.setText("");
        resultLabel.setText("Enter your guess above!");
        triesLabel.setText("Tries: 0");
        guessButton.setEnabled(true);
        resetButton.setEnabled(false);
        isGameOver = false;
        remainingTime = 60;
        timerLabel.setText("Time remaining: 60s");

        // Reset the timers
        if (timer != null) {
            timer.restart();
        }
        if (countdownTimer != null) {
            countdownTimer.restart();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GuessTheNumberGUI::new);
    }
}
