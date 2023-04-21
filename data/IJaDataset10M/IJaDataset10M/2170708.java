package ui;

import java.awt.*;
import javax.swing.*;
import sgf.SGFController;

/**
 * Panel for navigating a problem.
 * @author TKington
 *
 */
public class WidgetPanel extends javax.swing.JPanel {

    /** The SGFController. */
    private SGFController sgfController;

    /** Black to play icon. */
    private ImageIcon blackIcon;

    /** White to play icon. */
    private ImageIcon whiteIcon;

    /** 
     * Creates new form WidgetPanel
     * @param c the SGFController 
     */
    public WidgetPanel(SGFController c) {
        sgfController = c;
        blackIcon = new ImageIcon(GobanPanel.blacks[0]);
        whiteIcon = new ImageIcon(GobanPanel.whites[0][0]);
        initComponents();
        int width = navButton.getPreferredSize().width + 15;
        setPreferredSize(new Dimension(width, getPreferredSize().height));
    }

    /**
     * Sets the current player to black or white.
     * @param p 1 for black, -1 for white
     */
    public void setToPlay(int p) {
        if (p == 1) {
            toPlayLabel.setIcon(blackIcon);
            toPlayLabel.setText("Black to play");
        } else {
            toPlayLabel.setIcon(whiteIcon);
            toPlayLabel.setText("White to play");
        }
    }

    /**
     * Sets solved state to "Solved" or "Failed"
     * @param dispResult true for Solved
     */
    public void setSolved(boolean dispResult) {
        if (dispResult) {
            solvedLabel.setForeground(new Color(0xdb, 0xa3, 0));
            solvedLabel.setText("Solved");
        } else {
            solvedLabel.setForeground(Color.red);
            solvedLabel.setText("Failed");
        }
    }

    /**
     * Clears the solved state.
     *
     */
    public void clearSolved() {
        solvedLabel.setForeground(Color.black);
        solvedLabel.setText("Unsolved");
    }

    /** Creates UI components. */
    private void initComponents() {
        toPlayLabel = new javax.swing.JLabel();
        solvedLabel = new javax.swing.JLabel();
        JButton restartButton = new javax.swing.JButton();
        JButton backButton = new javax.swing.JButton();
        navButton = new javax.swing.JButton();
        setLayout(new FlowLayout());
        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(3, 3, 3, 3)));
        add(toPlayLabel);
        solvedLabel.setFont(new java.awt.Font("MS Sans Serif", 1, 14));
        solvedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(solvedLabel);
        restartButton.setText("Restart");
        restartButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRestart();
            }
        });
        add(restartButton);
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onBack();
            }
        });
        add(backButton);
        navButton.setText("Navigate Solution");
        navButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onNavigate();
            }
        });
        add(navButton);
    }

    /** Handler for Navigate Solution. */
    private void onNavigate() {
        sgfController.navigateSolution();
        navButton.setEnabled(false);
    }

    /** Handler for Back. */
    private void onBack() {
        sgfController.goBack();
    }

    /** Handler for Restart. */
    private void onRestart() {
        sgfController.restart();
        navButton.setEnabled(true);
    }

    /**
     * Enables/disables the navigate solution button.
     * @param e true to enable
     */
    public void enableNavButton(boolean e) {
        navButton.setEnabled(e);
    }

    /** Navigate Solution button. */
    private JButton navButton;

    /** Unsolved/Solved/Wrong label. */
    private javax.swing.JLabel solvedLabel;

    /** Black/white to play label. */
    private javax.swing.JLabel toPlayLabel;
}
