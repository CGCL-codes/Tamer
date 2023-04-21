package easyplay.reaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.net.URLClassLoader;
import com.docuverse.swt.flash.FlashPlayer;
import tools.*;

/**
 * Main Reaction Game Class - By Rhys Godfrey
**/
public class ReactionGame extends JDialog {

    Random rand = new Random();

    Timer time = new Timer(900, new TimeListener());

    int current;

    int score = 0;

    int attempts = 0;

    int delay = 900;

    GameObject[] buttons = createButtons();

    JTextField statusText = new JTextField();

    static FlashPlayer mainPlayer;

    GUI graphics;

    public ReactionGame(JFrame parent, FlashPlayer player) {
        super(parent);
        mainPlayer = player;
        graphics = new GUI(this);
        Config conf = new Config("profiles//profile.temp");
        String diff = conf.read("REACTION");
        if (diff.equals("0")) delay = 1150; else if (diff.equals("1")) delay = 900; else delay = 650;
    }

    /**
     * Runs the game from the command line (for this implementation
     * the game must be run from the same directory as EasyOS.class
     * using <i>java easyplay\reaction\ReactionGame</i>
    **/
    public static void main(String args[]) {
        ReactionGame program = new ReactionGame(new JFrame(), mainPlayer);
        program.display();
    }

    public void bringToFront(String com) {
        if (com.equals("close")) {
            time.stop();
            this.dispose();
        } else if (com.equals("lock")) {
            this.setAlwaysOnTop(true);
        } else if (com.equals("unlock")) {
            this.setAlwaysOnTop(false);
        } else if (com.equals("front")) {
            this.toFront();
        }
    }

    /**
     * Sets up and then displays the game
    **/
    public void display() {
        this.addFocusListener(new EasyFocusListener("reaction", mainPlayer));
        this.setUndecorated(true);
        Container window = getContentPane();
        Box screen = new Box(BoxLayout.Y_AXIS);
        graphics.setFrameDetails(this);
        graphics.createMenuBar(this, new MenuListener(), new LevelListener());
        screen.add(graphics.createMainPane(buttons));
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(s.width / 3, s.height / 3);
        Box status = new Box(BoxLayout.X_AXIS);
        status.add(statusText);
        screen.add(status);
        statusText.setText("Select \"File > Start Game\" to Start");
        window.add(screen);
        pack();
        setVisible(true);
        toFront();
    }

    private GameObject[] createButtons() {
        GameObject[] buttons = new GameObject[9];
        ActionListener lis = new ButtonListener();
        for (int i = 0; i < 9; i++) {
            buttons[i] = new GameObject(new JButton(), lis);
        }
        return (buttons);
    }

    protected void replayOrExit(int choice) {
        if (choice == 1) {
            current = 0;
            score = 0;
            attempts = 0;
            statusText.setText("Select File > Start Game To Play Again");
        } else if (choice == 0) exitGame(false);
    }

    protected void exitGame(boolean showDialog) {
        int n = 0;
        if (showDialog) {
            n = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Game Over", JOptionPane.YES_NO_OPTION);
        }
        if (n == 0) {
            mainPlayer.setVariable("rtaskbar", "reaction");
            time.stop();
            dispose();
        }
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) exitGame(true);
    }

    protected class LevelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("easy")) {
                delay = 1150;
                newGame();
            } else if (e.getActionCommand().equals("norm")) {
                delay = 900;
                newGame();
            } else if (e.getActionCommand().equals("hard")) {
                delay = 650;
                newGame();
            }
            time.setDelay(delay);
        }
    }

    private void newGame() {
        score = 0;
        attempts = 0;
        if (time.isRunning()) time.stop();
        time = new Timer(delay, new TimeListener());
        time.start();
        statusText.setText("Score: 0, Attempts 0");
    }

    protected class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("new")) newGame(); else if (e.getActionCommand().equals("end")) {
                int choice;
                GameObject b = buttons[current];
                b.makeIcon(b.getButton(), "blank.gif");
                exitGame(true);
            } else if (e.getActionCommand().equals("howTo")) {
                graphics.displayHelpHowTo();
            } else if (e.getActionCommand().equals("about")) {
                graphics.displayHelpAbout();
            }
        }
    }

    protected class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            JButton b = (JButton) src;
            GameObject game = new GameObject();
            if ((current != -1) && (src == buttons[current].getButton())) {
                game.makeIcon(b, "blank.gif");
                score++;
                statusText.setText("Score " + score + ", Attempts " + attempts);
            }
        }
    }

    protected class TimeListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (attempts < 30) {
                int r = rand.nextInt(9);
                attempts++;
                GameObject b = buttons[current];
                b.makeIcon(b.getButton(), "blank.gif");
                buttons[r].makeIcon(buttons[r].getButton(), "select.gif");
                statusText.setText("Score " + score + ", Attempts " + attempts);
                current = r;
            } else {
                time.stop();
                GameObject b = buttons[current];
                b.makeIcon(b.getButton(), "blank.gif");
                int choice = graphics.dialogBox("You Scored " + score + " Out Of A Possible " + attempts);
                replayOrExit(choice);
            }
        }
    }
}
