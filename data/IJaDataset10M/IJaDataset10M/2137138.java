package de.robowars.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import de.robowars.ai.AIController;
import de.robowars.comm.transport.Acceptance;
import de.robowars.comm.transport.Application;
import de.robowars.comm.transport.FieldElementTypes;
import de.robowars.comm.transport.CardSelection;
import de.robowars.comm.transport.RobotEnumeration;
import de.robowars.ui.event.*;
import de.robowars.ui.event.EventController;
import de.robowars.util.RobowarsException;

/**
 * Main Controller for all standard Buttons and Menu-Items
 * 
 * This class controls the Client behaviour, manages the displayed screens and
 * starts the Server (Master) and the Server-Interaction (Client).
 * 
 * @author Stefan Henze
 */
public class DialogControl implements ActionListener {

    private final boolean demo = false;

    private static RobotEnumeration robotType = RobotEnumeration.FIRST;

    private static DialogControl _instance;

    private JFrame mainWindow;

    private org.apache.log4j.Category log;

    private String ip, name;

    private int port;

    private int boardX = 512, boardY = 384;

    private DialogControl() {
        log = org.apache.log4j.Category.getInstance(DialogControl.class);
        if (log.isInfoEnabled()) {
            log.info("Starting DialogControl");
        }
    }

    /**
	 * Method getInstance.
	 * invoke this method to get the Instance of DialogControl
	 * @return DialogControl
	 */
    public static DialogControl getInstance() {
        if (_instance == null) {
            _instance = new DialogControl();
        }
        return _instance;
    }

    /**
	 * called by Events from within Dialogs
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
    public void actionPerformed(ActionEvent ev) {
        String command = ev.getActionCommand();
        log.info("getting actionPerformed: " + command);
        int testrobo = 0;
        if (command == "control:server") {
            startAsServer();
        } else if (command == "control:client") {
            mainWindow.setContentPane(StartDialog.getInstance().createClientStart(_instance));
            mainWindow.validate();
        } else if (command == "control:startGame") {
            StartGameEvent ge = (StartGameEvent) ev;
            startGame();
            ServerControl.getInstance().selectServer(ge.getBoard(), ge.getSecTimeout());
        } else if (command == "control:startClient") {
            joinGameAsClient();
        } else if (command == "control:addBot") {
            AddBotEvent ae = (AddBotEvent) ev;
            log.info("Adding AI player: " + ae.getLevel() + ae.getName() + ae.getType());
            new AIController("localhost", port, ae.getLevel(), ae.getName(), ae.getType());
        } else if (command == "control:removePlayer") {
            try {
                ServerControl.getInstance().getMaster().withdrawal(((RemovePlayerEvent) ev).getPlayerId());
            } catch (RobowarsException e) {
                log.error("Error removing player.", e);
            }
        } else if (command == "exit") {
            System.exit(0);
        } else if (command == "game:exit") {
            System.exit(0);
        } else if (command == "game:powerdown") {
            CardSet.getInstance().togglePowerDown();
        } else if (command == "game:sendcards") {
            CardSet cardSet = CardSet.getInstance();
            if (cardSet.cardsSelected()) {
                CardSelection cards = CardSet.getInstance().getCardSelection();
                ServerControl.getInstance().cardsSelected(cards);
            } else {
                JOptionPane.showMessageDialog(MainFrame.getInstance(), "please select valid cards");
            }
        } else if (command == "view:rotateleft") {
            Board.getInstance().rotateBoardLeft();
        } else if (command == "view:rotateright") {
            Board.getInstance().rotateBoardRight();
        } else if (command == "view:up") {
            Board.getInstance().moveBoardUp();
        } else if (command == "view:down") {
            Board.getInstance().moveBoardDown();
        } else if (command == "view:left") {
            Board.getInstance().moveBoardLeft();
        } else if (command == "view:right") {
            Board.getInstance().moveBoardRight();
        } else if (command == "help:about") {
            JOptionPane.showMessageDialog(MainFrame.getInstance(), "to be implemented soon by ...");
        } else if (command == "test:robostart") {
            PlayerInfo.getInstance(0).showRobot();
            try {
                PlayerInfo.getInstance(0).getRobotLayer().startTurnToLeft();
            } catch (Exception e) {
                log.error(e, e);
                System.exit(1);
            }
        } else if (command == "test:robogo") {
            PlayerInfo.getInstance(0).getRobotLayer().turnFromLeft(++testrobo);
        } else if (command == "test:roboend") {
            try {
                PlayerInfo.getInstance(0).getRobotLayer().endTurnToLeft();
            } catch (Exception e) {
                log.error(e, e);
                System.exit(1);
            }
        } else {
            log.error("ActionCommand not found");
            System.exit(0);
        }
    }

    /**
	 * Brings up a dialog asking for player name and server port, then starts server, registers player,
	 * and shows game configuration dialog.
	 */
    private void startAsServer() {
        StartServerDialog startServerDlg = new StartServerDialog(mainWindow, null, true);
        startServerDlg.pack();
        startServerDlg.setVisible(true);
        if (!startServerDlg.wasCanceled()) {
            port = startServerDlg.getPortNr();
            name = startServerDlg.getPlayerName();
            ServerControl serverControl = ServerControl.getInstance();
            serverControl.startServer(port, new MasterClientImpl());
            serverControl.startClient("localhost", port);
            Acceptance ac = registerPlayer();
            if (ac != null) {
                mainWindow.setContentPane(StartDialog.getInstance().createServerStart(_instance));
                mainWindow.validate();
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Could not participate in game. Please try again.");
                try {
                    serverControl.getMaster().endGame();
                } catch (RobowarsException re) {
                    log.warn("Could not register Game Master at server. Closing server produced exception.", re);
                }
            }
        }
    }

    /** Registering the player is needed when player acts as Game Master as well
	 * as when he is acting as "normal" client. Therefore outsourced to this method.
	 */
    private Acceptance registerPlayer() {
        Application ap = new Application();
        ap.setPlayerName(name);
        ap.setRobotType(robotType);
        return ServerControl.getInstance().signUp(ap);
    }

    /** Makes an attempt at joining a game after starting communication first. Shows a message if it doesn't succeed.
	 * Needs further refinement. Now just showing standard game screen.
	 */
    private void joinGameAsClient() {
        if (!demo) {
            ServerControl.getInstance().startClient(ip, port);
            Acceptance ac = registerPlayer();
            if (ac != null) {
                startGame();
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Could not participate in game. Max. Players reached or server not available (wrong address?).");
            }
        } else {
            startGame();
        }
    }

    /**
	 * Method startGame.
	 */
    private void startGame() {
        mainWindow.setJMenuBar(null);
        GameDialog p = GameDialog.getInstance(this);
        p.startMainPane();
        mainWindow.setContentPane(p);
        mainWindow.validate();
    }

    /**
	 * Method getActionListener.
	 * Same as getInstance but returns type ActionListener
	 * @return ActionListener
	 */
    public ActionListener getActionListener() {
        return this;
    }

    /**
	 * Method setMainWindow.
	 * @param window
	 */
    public void setMainWindow(JFrame window) {
        mainWindow = window;
    }

    /**
	 * Method setClientInput.
	 * To be called by StartDialog to save Textbox-values
	 * @param ip
	 * @param port
	 * @param name
	 * @return boolean
	 */
    public boolean setClientInput(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        return true;
    }

    /**
	 * Method setServerInput.
	 * To be called by StartDialog to save Textbox-values
	 * @param port
	 * @param name
	 * @return boolean
	 */
    public boolean setServerInput(int port, String name) {
        this.port = port;
        this.name = name;
        return true;
    }

    /**
	 * Method should be called whenever this particular client acts as the game master and a new
	 * player has signed up.
	 * @param ac the acceptance which was send to the corresponding player
	 */
    public void playerRegistered(Acceptance ac) {
        Object o;
        log.debug("got player-Acceptance " + ac.getPlayerId());
        if ((o = mainWindow.getContentPane()) instanceof ServerStartPane) {
            ((ServerStartPane) o).playerRegistered(ac);
        }
    }

    /**
	 * Method should be called whenever this particular client acts as the game master and a player
	 * exited (=was kicked or retreated). This method will transmit this information to an object
	 * which displays this information.
	 * @param ac the acceptance which was send to the corresponding player
	 */
    public void playerQuit(int playerId) {
        Object o;
        log.debug("player with Id " + playerId + "has left the game.");
        if ((o = mainWindow.getContentPane()) instanceof ServerStartPane) {
            ((ServerStartPane) o).playerQuit(playerId);
        }
    }
}
