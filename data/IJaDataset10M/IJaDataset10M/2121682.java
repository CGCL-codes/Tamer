package easyplay.history;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Toolkit;

class GUI extends JDialog {

    private Component parent;

    public GUI(Component p) {
        parent = p;
    }

    protected void setFrameDetails(JDialog window) {
        window.setTitle("easyPlay - History Game");
        window.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Dimension size = getScreenSize();
        window.setPreferredSize(new Dimension(300, 300));
        window.setLocation((size.width / 2) - 400, (size.height / 2) - 125);
        window.setPreferredSize(new Dimension(800, 250));
        window.setMinimumSize(new Dimension(800, 250));
        window.setMaximumSize(new Dimension(800, 250));
    }

    protected int dialogBox(String text) {
        int n = JOptionPane.showConfirmDialog(parent, "" + text + "Are you sure you want to quit?", "Game Over", JOptionPane.YES_NO_OPTION);
        return (n);
    }

    protected int startNew(int scr, int qs) {
        int n = JOptionPane.showConfirmDialog(parent, "Congratulations, you scored: \n" + scr + " / " + qs + "\nWould you like to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        return (n);
    }

    private Dimension getScreenSize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        return (tk.getScreenSize());
    }

    protected void displayHelpTheGame() {
        JOptionPane.showMessageDialog(parent, "This game tests your " + "knowledge of history \n" + "The screen will show a question\n" + "and you must click the correct " + "answer \n\n" + "The number of incorrect guesses you are allowed depends on the difficulty level\n" + "Easy - 3 Guesses\n" + "Standard - 2 Guesses\n" + "Hard - 1 Guess", "How To Play", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void displayHelpHowTo() {
        JOptionPane.showMessageDialog(parent, "To Start a new game select " + "File > New Game.\n" + "To change difficulty select " + "Difficulty > Easy/Standard/Hard.\n" + "To end the " + "current Game select File > Quit.", "Game Help", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void displayHelpAbout() {
        JOptionPane.showMessageDialog(parent, "Game by Simon Jackson.\n" + "EasyOS, December 2005", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void displaylose(String answer) {
        JOptionPane.showMessageDialog(parent, "Wrong!\n" + "The correct answer was: \n" + answer, "", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void displaywin() {
        JOptionPane.showMessageDialog(parent, "Correct!\n", "", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void displaytryagain(int remaining) {
        JOptionPane.showMessageDialog(parent, "Wrong!\n" + "You have " + remaining + " attempt(s) left\n" + "Please try again", "", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void createMenuBar(JDialog window, ActionListener listener, ActionListener rbLis, int diffi) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        menu.add(createMenuItem("New Game", listener, "new"));
        menu.add(createMenuItem("Quit", listener, "end"));
        JMenu level = new JMenu("Difficulty");
        menuBar.add(level);
        JRadioButtonMenuItem rb;
        ButtonGroup diff = new ButtonGroup();
        if (diffi == 0) {
            rb = createRButton("Easy", rbLis, "easy", true);
            diff.add(rb);
            level.add(rb);
        } else {
            rb = createRButton("Easy", rbLis, "easy", false);
            diff.add(rb);
            level.add(rb);
        }
        if (diffi == 1) {
            rb = createRButton("Standard", rbLis, "norm", true);
            diff.add(rb);
            level.add(rb);
        } else {
            rb = createRButton("Standard", rbLis, "norm", false);
            diff.add(rb);
            level.add(rb);
        }
        if (diffi == 2) {
            rb = createRButton("Hard", rbLis, "hard", true);
            diff.add(rb);
            level.add(rb);
        } else {
            rb = createRButton("Hard", rbLis, "hard", false);
            diff.add(rb);
            level.add(rb);
        }
        JMenu help = new JMenu("Help");
        menuBar.add(help);
        help.add(createMenuItem("The Game", listener, "info"));
        help.add(createMenuItem("How To Play", listener, "howTo"));
        help.add(createMenuItem("About", listener, "about"));
        window.setJMenuBar(menuBar);
    }

    protected JRadioButtonMenuItem createRButton(String title, ActionListener lis, String com, boolean select) {
        JRadioButtonMenuItem rb = new JRadioButtonMenuItem(title);
        rb.addActionListener(lis);
        rb.setActionCommand(com);
        rb.setSelected(select);
        return (rb);
    }

    protected JMenuItem createMenuItem(String title, ActionListener lis, String com) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(lis);
        menuItem.setActionCommand(com);
        return (menuItem);
    }
}
