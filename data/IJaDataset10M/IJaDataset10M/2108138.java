package jscorch;

import javax.swing.UIManager;
import java.awt.*;

/**
 * <P>The driver class for the JScorch application, the JScorch class creates
 * a new JFrame to serve as the game window, as well as initializing all
 * the Player data.</P>
 * <P>A typical window is created with a size of 640x480 pixels, and there are
 * currently 5 players in the roster.</P>
*/
public class JScorch {

    /**
	 * <P>A boolean variable which stores if the game window's frame should be packed or not.</P>
	 * <P>This validates frames which have preset sizes<BR>
	 * If a frame has useful preferred size info, perhaps from a layout, then pack</P>
	 */
    static boolean packFrame = false;

    /**
	 * A simple Dimension variable storing the size of the GameWindow JFrame to be created.
	 */
    static Dimension size = new Dimension(640, 480);

    /**
	 * A reference variable to the GameWindow of the game currently in progress,
	 * as there can only be one game happening at a time.
	 */
    static GameWindow game;

    /**
	 * Array of Player objects, which is passed to the GameWindow when a new game is started.
	 */
    static Player[] players;

    /**
	 * Creates a new JScorch object, which in fact just starts a new game.
	 */
    public JScorch() {
        newGame();
    }

    /**
	 * Controls creating and customizing a new game's window.
	 */
    public static void newGame() {
        initPlayers();
        JScorchSplash splash = new JScorchSplash();
        splash.setMessage("Loading... GameWindow");
        game = new GameWindow(size, players, splash.getMessage());
        game.setVisible(true);
        splash.setVisible(false);
    }

    /**
	 * Runs on application start. Creates a new JScorch object.
	 *
	 * @param args the command line arguments to the application.
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new JScorch();
    }

    /**
	 * Creates and initializes a new array of players.
	 */
    private static void initPlayers() {
        players = new Player[5];
        players[0] = new Player("Bob", TankTypes.BOX, Preferences.tankColors[0]);
        players[1] = new Player("Steve", TankTypes.OVAL, Preferences.tankColors[1]);
        players[2] = new Player("Jeff", TankTypes.TRI, Preferences.tankColors[5]);
        players[3] = new Player("Henry", TankTypes.OVAL, Preferences.tankColors[3]);
        players[4] = new Player("Chuck", TankTypes.BOX, Preferences.tankColors[4]);
    }
}
