package org.freeorion;

import org.freeorion.api.TurnCoordinator;
import org.freeorion.api.Controller;
import org.freeorion.api.ClientController;
import org.freeorion.api.Universe;
import org.freeorion.api.Player;
import org.freeorion.api.AlienType;
import org.freeorion.api.GameLayer;
import org.freeorion.gui.MainScreen;
import org.freeorion.gui.MainFrame;
import org.freeorion.config.AlienPreset;
import org.freeorion.util.Textbox;
import org.freeorion.util.Toolbox;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.JApplet;
import javax.swing.*;

/** Orion is the central class of the Project. 
 * It gathers some global Settings and Variables.
 */
public class Orion extends JApplet {

    public static final String NAME = "Free Orion";

    public static final String VERSION = "V0.0.4";

    public static final int DIFFICULTY_BEGINNER = 1;

    public static final int DIFFICULTY_EASY = 2;

    public static final int DIFFICULTY_NORMAL = 3;

    public static final int DIFFICULTY_HARD = 4;

    public static final int DIFFICULTY_IMPOSSIBLE = 5;

    /** The difficulty of the current game. */
    public static int DIFFICULTY = DIFFICULTY_NORMAL;

    /** This is the default number of players.
	 * Normally this number is irrelevant, because the Player should
	 * be forced to choose a number before starting the game.
	 */
    public static final int DEFAULT_NUMBER_OF_PLAYERS = 4;

    public static final int DEFAULT_SIZE_OF_UNIVERSE = Universe.SIZE_MEDIUM;

    public static GameLayer playerLayer;

    public static TurnCoordinator coordinator;

    public static final boolean DEBUG = true;

    public static final int DEBUG_FLAG_TURN_COMMANDS = (1 << 0);

    public static final int DEBUG_FLAG_DUMP_COMMANDS = (1 << 1);

    public static int DEBUG_FLAGS = 0;

    public static void DEBUG(String message) {
        if (Orion.DEBUG) {
            String line = "[" + Thread.currentThread().getName() + "]: " + message;
            if (runningAsApplet) {
                debugArea.append(line + "\n");
                System.out.println(line);
            } else {
                System.out.println(line);
            }
        }
    }

    public static void DEBUG(String message, int flag) {
        if (Orion.DEBUG && ((flag & DEBUG_FLAGS) != 0)) {
            String line = "[" + Thread.currentThread().getName() + "]: " + message;
            if (runningAsApplet) {
                debugArea.append(line + "\n");
                System.out.println(line);
            } else {
                System.out.println(line);
            }
        }
    }

    private static void initializeTextbox() {
        try {
            Textbox.initialize();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static void initialize(String arg[]) {
        Hashtable alienNames = new Hashtable();
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Klackon"), "Klackon");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Elerian"), "Elerian");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Bulrathi"), "Bulrathi");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Trilarian"), "Trilarian");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Mrrshan"), "Mrrshan");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Silicoid"), "Silicoid");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Alkari"), "Alkari");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Human"), "Human");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Gnolam"), "Gnolam");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Darlok"), "Darlok");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Sakkra"), "Sakkra");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Meklar"), "Meklar");
        alienNames.put(Textbox.getString(Textbox.AlienPresetText, "Psilon"), "Psilon");
        int size = DEFAULT_SIZE_OF_UNIVERSE;
        int numberOfPlayers = DEFAULT_NUMBER_OF_PLAYERS;
        int numberOfHumanPlayers = DEFAULT_NUMBER_OF_PLAYERS >> 1;
        String myAlienName = null;
        String myLeaderName = "John Malkovich";
        for (int argc = 0; argc < arg.length; argc++) {
            if (arg[argc].startsWith("-")) {
                if (arg[argc].equals("-small")) size = Universe.SIZE_SMALL; else if (arg[argc].equals("-medium")) size = Universe.SIZE_MEDIUM; else if (arg[argc].equals("-large")) size = Universe.SIZE_LARGE; else if (arg[argc].equals("-huge")) size = Universe.SIZE_HUGE; else if (arg[argc].startsWith("-debug=")) {
                    String debug = arg[argc].substring(7);
                    if (debug.equals("dump")) {
                        DEBUG_FLAGS |= DEBUG_FLAG_DUMP_COMMANDS;
                    } else if (debug.equals("turn")) {
                        DEBUG_FLAGS |= DEBUG_FLAG_TURN_COMMANDS;
                    }
                }
            } else if (alienNames.containsKey(arg[argc])) {
                myAlienName = (String) alienNames.get(arg[argc]);
            } else if (arg[argc].length() == 1 && "12345678".indexOf(arg[argc]) != 0) {
                numberOfPlayers = "12345678".indexOf(arg[argc]) + 1;
                numberOfHumanPlayers = "12345678".indexOf(arg[argc]) + 1;
            } else {
                System.err.println("AlienType '" + arg[argc] + "' unbekannt");
                System.exit(1);
            }
        }
        coordinator = new TurnCoordinator(size);
        if (myAlienName == null) {
            myAlienName = (String) Toolbox.getRandomElement(alienNames);
        }
        Vector colors = new Vector();
        colors.addElement(new Integer(Player.COLOR_WHITE));
        colors.addElement(new Integer(Player.COLOR_BROWN));
        colors.addElement(new Integer(Player.COLOR_RED));
        colors.addElement(new Integer(Player.COLOR_BLUE));
        colors.addElement(new Integer(Player.COLOR_GREEN));
        colors.addElement(new Integer(Player.COLOR_YELLOW));
        colors.addElement(new Integer(Player.COLOR_MAGENTA));
        colors.addElement(new Integer(Player.COLOR_ORANGE));
        String alienName;
        AlienType alienType;
        String alienLeaderName;
        Integer alienColor;
        Controller controller = coordinator.getMasterController();
        Orion.DEBUG("Game started with " + numberOfPlayers + " Players:");
        for (int i = 0; i < numberOfPlayers; i++) {
            if (myAlienName != null) {
                alienName = myAlienName;
                myAlienName = null;
            } else {
                alienName = (String) Toolbox.getRandomElement(alienNames);
            }
            alienType = AlienPreset.alienTypeForName(controller, alienName);
            alienLeaderName = alienType.getLeaderName();
            alienColor = (Integer) Toolbox.getRandomElement(colors);
            Player player = new Player(controller, alienType, alienLeaderName, alienColor.intValue(), i + 1);
            coordinator.addPlayer(player, (i < numberOfHumanPlayers) ? "HotSeat" : "org.freeorion.Stupid");
            alienNames.remove(Textbox.getString(Textbox.AlienPresetText, alienName));
            colors.removeElement(alienColor);
            Orion.DEBUG((i + 1) + ". opponent: " + alienName);
        }
        coordinator.finishInitialization();
        coordinator.start();
    }

    /** The is the main procedure of the Project.
	 * After initializing the global resources, the project window
	 * will be created and the game starts.
	 */
    public static void main(String arg[]) {
        initializeTextbox();
        initialize(arg);
    }

    /** The GUI Frame hosting the seats. */
    private static MainScreen hotSeatScreen;

    public static GameLayer registerHotSeatController(ClientController controller) {
        Orion.DEBUG("Orion.registerHotSeat()");
        if (hotSeatScreen == null) {
            Orion.DEBUG("new MainScreen(true)");
            hotSeatScreen = new MainScreen(true);
            Orion.DEBUG("new MainFrame(...)");
            MainFrame frame = new MainFrame(NAME + ", " + VERSION, hotSeatScreen);
            frame.setVisible(true);
        }
        hotSeatScreen.getHotSeatPanel().registerHotSeatController(controller);
        return hotSeatScreen;
    }

    public static JApplet OrionApplet = null;

    public static boolean runningAsApplet = false;

    static JTextArea debugArea;

    CardLayout cardLayout;

    JPanel mainPanel;

    JLabel label;

    /**
	 *
	 */
    public Orion() {
        getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
        runningAsApplet = true;
        OrionApplet = this;
        debugArea = new JTextArea();
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(new JScrollPane(debugArea), "debug");
        label = new JLabel(NAME + ", " + VERSION + " (Applet)");
        label.setHorizontalAlignment(JLabel.CENTER);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton button = new JButton("Orion Screen");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "orion");
            }
        });
        buttonPanel.add(button);
        button = new JButton("Debug Screen");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "debug");
            }
        });
        buttonPanel.add(button);
        getContentPane().add(label, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    /** Initialization of the Applet.
	 */
    public void init() {
        DEBUG("Applet:getCodeBase()=" + getCodeBase());
        DEBUG("Applet:getDocumentBase()=" + getDocumentBase());
        org.freeorion.gui.Settings test = new org.freeorion.gui.Settings();
        String[] arg = new String[0];
        initializeTextbox();
        try {
            Class guiSettings = Class.forName("org.freeorion.gui.Settings");
            label.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.green));
        } catch (ClassNotFoundException ex) {
            label.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.red));
        }
        hotSeatScreen = new MainScreen(true);
        mainPanel.add(hotSeatScreen, "orion");
        initialize(arg);
        cardLayout.show(mainPanel, "orion");
    }
}
