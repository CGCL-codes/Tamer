package ludo.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.apache.log4j.Logger;
import ludo.domainmodel.Counter;
import ludo.domainmodel.GameBoard;
import ludo.domainmodel.Player;
import ludo.domainmodel.manager.PlayerManager;
import ludo.ui.controls.MenuItemListener;
import ludo.ui.controls.DiceListener;

/**
 *Stellt das Spielbrett grafisch dar
 * 
 */
public class GameBoardUI {

    private static Logger log = Logger.getLogger(GameBoardUI.class);

    private static GameBoardUI self = null;

    private JMenu gameMenu;

    private JMenuBar menuBar;

    private JButton dice;

    private JPanel background;

    private JFrame frame;

    private JLabel statusBar;

    public static GameBoardUI getInstance() {
        if (self == null) {
            self = new GameBoardUI();
        }
        return self;
    }

    public void dispplayGameBoard() {
        frame = new JFrame("Mensch ärgere dich nicht");
        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(680, 750));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        background = new ImagePanel("SpielbrettVorlage.jpg");
        background.setLayout(null);
        dice = new JButton("Würfel");
        dice.addActionListener(new DiceListener());
        dice.setBounds(292, 320, 75, 75);
        background.add(dice);
        menuBar = new JMenuBar();
        menuBar.setVisible(true);
        gameMenu = new JMenu("Menu");
        JMenuItem spielBeenden = new JMenuItem("Spiel beenden");
        spielBeenden.setActionCommand("Beenden");
        spielBeenden.addActionListener(new MenuItemListener());
        gameMenu.add(spielBeenden);
        menuBar.add(gameMenu);
        statusBar = new JLabel("Willkommen bei Mensch aergere dich nicht");
        statusBar.setBackground(Color.GRAY);
        statusBar.setBackground(Color.red);
        statusBar.setBounds(1, 1, 500, 25);
        background.add(statusBar);
        JButton skipButton = new JButton("Aussetzen");
        skipButton.setActionCommand(skipButton.getText());
        skipButton.addActionListener(new MenuItemListener());
        skipButton.setBounds(570, 1, 100, 30);
        background.add(skipButton);
        frame.getContentPane().add(background);
        frame.setJMenuBar(menuBar);
        refresh();
    }

    /**
	 * Updates the location of all {@link Counter}s and paints them accordingly
	 * on the graphical {@link GameBoard}.
	 */
    public void drawCounters() {
        for (Player player : PlayerManager.getInstance().getPlayerList()) {
            for (Counter counter : player.getCounters()) {
                background.remove(counter.getPlayerImage());
                counter.getPlayerImage().setBounds(((int) counter.getCurrentLocation().getX()), ((int) counter.getCurrentLocation().getY()), 50, 50);
                background.add(counter.getPlayerImage());
            }
        }
        refresh();
    }

    /**
	 * Displays a status message in the status bar.
	 */
    public void displayStatusMessage(String message) {
        this.statusBar.setText(message);
        refresh();
    }

    /**
	 * Refresh the current picture
	 */
    public void refresh() {
        background.repaint();
        frame.pack();
        frame.repaint();
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public String getDiceValue() {
        return dice.getText();
    }

    public void setDiceValue(String wert) {
        dice.setText(wert);
    }

    public JButton getDice() {
        return dice;
    }

    /**
	 * Exits the application.
	 */
    public void endGame() {
        self = null;
        frame.setVisible(false);
        frame.dispose();
    }

    public void dispose() {
        background.removeAll();
        refresh();
    }

    /**
	 * Draws the names for all currently existing players. This requires the
	 * coordinates for the name labels to be set in advance.
	 */
    public void drawPlayerNames(LinkedList<Player> players) {
        for (Player player : players) {
            JLabel spielerName = new JLabel(player.getPlayerName());
            spielerName.setBounds((int) player.getPlayerNameLocation().getX(), (int) player.getPlayerNameLocation().getY(), 150, 30);
            background.add(spielerName);
        }
    }

    public void drawMedals(Player player, ImageIcon icon) {
        JLabel medal = new JLabel(icon);
        medal.setBounds((int) player.getMedalLocation().getX(), (int) player.getMedalLocation().getY(), 100, 100);
        background.add(medal);
        refresh();
    }

    public void changeDiceColor(Color color) {
        getDice().setBorder(new LineBorder(color, 10));
    }
}
