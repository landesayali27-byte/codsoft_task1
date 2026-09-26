import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class NumberGuessingGame extends JFrame {

    // Game Constants
    private static final int MIN_VAL = 1;
    private static final int MAX_VAL = 100;
    private static final int MAX_ATTEMPTS = 7;

    // Palette Colors
    private static final Color BG_DARK = new Color(24, 26, 36);
    private static final Color CARD_BG = new Color(34, 38, 54);
    private static final Color ACCENT_BLUE = new Color(79, 110, 247);
    private static final Color TEXT_WHITE = new Color(240, 242, 248);
    private static final Color TEXT_MUTED = new Color(160, 166, 185);
    private static final Color COLOR_HIGH = new Color(255, 179, 71);
    private static final Color COLOR_LOW = new Color(74, 189, 255);
    private static final Color COLOR_WIN = new Color(72, 211, 153);
    private static final Color COLOR_LOSE = new Color(245, 101, 101);

    // State Variables
    private int targetNumber;
    private int attemptsLeft;
    private int roundsWon = 0;
    private int totalRounds = 0;
    private int totalScore = 0;
    private final Random random = new Random();

    // UI Components
    private JTextField guessInputField;
    private JButton submitButton;
    private JButton playAgainButton;
    private JLabel feedbackLabel;
    private JLabel attemptsBadge;
    private JLabel scoreBadge;
    private JLabel roundsBadge;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        // Header Panel (Stats & Scores)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Center Card Panel (Gameplay Area)
        add(createGameCard(), BorderLayout.CENTER);

        // Start initial round
        startNewRound();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new GridLayout(1, 3, 15, 0));
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(25, 30, 10, 30));

        roundsBadge = createStatCard("ROUNDS", "0");
        scoreBadge = createStatCard("SCORE", "0");
        attemptsBadge = createStatCard("ATTEMPTS", String.valueOf(MAX_ATTEMPTS));

        header.add(roundsBadge);
        header.add(scoreBadge);
        header.add(attemptsBadge);

        return header;
    }

    private JLabel createStatCard(String label, String value) {
        JLabel stat = new JLabel(formatStatText(label, value), SwingConstants.CENTER);
        stat.setOpaque(true);
        stat.setBackground(CARD_BG);
        stat.setForeground(TEXT_WHITE);
        stat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 56, 78), 1, true),
                new EmptyBorder(10, 5, 10, 5)
        ));
        return stat;
    }

    private String formatStatText(String label, String val) {
        return "<html><center><span style='font-size:9px; color:#A0A6B9;'>" + label + "</span><br>"
                + "<span style='font-size:16px; font-weight:bold; color:#F0F2F8;'>" + val + "</span></center></html>";
    }

    private JPanel createGameCard() {
        JPanel cardWrapper = new JPanel(new GridBagLayout());
        cardWrapper.setBackground(BG_DARK);
        cardWrapper.setBorder(new EmptyBorder(10, 30, 30, 30));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(48, 54, 76), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Subtitle instructions
        JLabel subtitle = new JLabel("Guess a number between " + MIN_VAL + " and " + MAX_VAL);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Field
        guessInputField = new JTextField();
        guessInputField.setMaximumSize(new Dimension(200, 48));
        guessInputField.setPreferredSize(new Dimension(200, 48));
        guessInputField.setFont(new Font("SansSerif", Font.BOLD, 22));
        guessInputField.setHorizontalAlignment(JTextField.CENTER);
        guessInputField.setBackground(new Color(24, 26, 36));
        guessInputField.setForeground(TEXT_WHITE);
        guessInputField.setCaretColor(TEXT_WHITE);
        guessInputField.setBorder(BorderFactory.createLineBorder(new Color(60, 68, 96), 1, true));

        // Enter key action
        guessInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && submitButton.isEnabled()) {
                    processGuess();
                }
            }
        });

        // Submit Button
        submitButton = new JButton("Submit Guess");
        styleButton(submitButton, ACCENT_BLUE, TEXT_WHITE);
        submitButton.addActionListener(e -> processGuess());

        // Dynamic Feedback Label
        feedbackLabel = new JLabel("Enter your guess to begin!", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        feedbackLabel.setForeground(TEXT_MUTED);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Play Again Button (Hidden by default)
        playAgainButton = new JButton("Play Next Round");
        styleButton(playAgainButton, COLOR_WIN, Color.BLACK);
        playAgainButton.setVisible(false);
        playAgainButton.addActionListener(e -> startNewRound());

        // Add elements to card with spacing
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));
        card.add(guessInputField);
        card.add(Box.createVerticalStrut(18));
        card.add(submitButton);
        card.add(Box.createVerticalStrut(22));
        card.add(feedbackLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(playAgainButton);
        card.add(Box.createVerticalGlue());

        cardWrapper.add(card);
        return cardWrapper;
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setPreferredSize(new Dimension(200, 42));
    }

    private void startNewRound() {
        totalRounds++;
        targetNumber = random.nextInt(MAX_VAL - MIN_VAL + 1) + MIN_VAL;
        attemptsLeft = MAX_ATTEMPTS;

        feedbackLabel.setText("Make your guess!");
        feedbackLabel.setForeground(TEXT_MUTED);

        guessInputField.setText("");
        guessInputField.setEnabled(true);
        guessInputField.requestFocusInWindow();

        submitButton.setEnabled(true);
        playAgainButton.setVisible(false);

        updateHeaderStats();
    }

    private void processGuess() {
        String text = guessInputField.getText().trim();
        if (text.isEmpty()) return;

        int guess;
        try {
            guess = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Please enter a valid number!");
            feedbackLabel.setForeground(COLOR_LOSE);
            return;
        }

        if (guess < MIN_VAL || guess > MAX_VAL) {
            feedbackLabel.setText("Enter a number between " + MIN_VAL + " and " + MAX_VAL + "!");
            feedbackLabel.setForeground(COLOR_HIGH);
            return;
        }

        attemptsLeft--;

        if (guess == targetNumber) {
            // Correct Guess
            roundsWon++;
            int points = (attemptsLeft + 1) * 15;
            totalScore += points;

            feedbackLabel.setText("Boom! Correct! (+" + points + " pts)");
            feedbackLabel.setForeground(COLOR_WIN);
            endRound();
        } else if (attemptsLeft <= 0) {
            // Out of attempts
            feedbackLabel.setText("Game Over! The number was " + targetNumber);
            feedbackLabel.setForeground(COLOR_LOSE);
            endRound();
        } else {
            // Hint Feedback
            if (guess > targetNumber) {
                feedbackLabel.setText("Too High! Try a smaller number.");
                feedbackLabel.setForeground(COLOR_HIGH);
            } else {
                feedbackLabel.setText("Too Low! Try a bigger number.");
                feedbackLabel.setForeground(COLOR_LOW);
            }
            guessInputField.selectAll();
            guessInputField.requestFocusInWindow();
        }

        updateHeaderStats();
    }

    private void endRound() {
        guessInputField.setEnabled(false);
        submitButton.setEnabled(false);
        playAgainButton.setVisible(true);
    }

    private void updateHeaderStats() {
        roundsBadge.setText(formatStatText("ROUNDS WON", roundsWon + "/" + totalRounds));
        scoreBadge.setText(formatStatText("SCORE", String.valueOf(totalScore)));
        attemptsBadge.setText(formatStatText("ATTEMPTS", String.valueOf(attemptsLeft)));
    }

    public static void main(String[] args) {
        // Run on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGame game = new NumberGuessingGame();
            game.setVisible(true);
        });
    }
}