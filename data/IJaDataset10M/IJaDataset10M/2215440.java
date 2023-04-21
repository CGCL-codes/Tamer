package de.boardgamesonline.bgo2.shared.chat;

import java.rmi.RemoteException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import de.boardgamesonline.bgo2.webserver.axis.GameManager;
import de.boardgamesonline.bgo2.webserver.axis.GameManagerProxy;
import de.boardgamesonline.bgo2.webserver.axis.GameStatus;
import de.boardgamesonline.bgo2.webserver.axis.PlayerStatus;

/**
 * A pseudo-game for the BGO2 webserver, just providing a chat.
 * Based on {@link ChatDemoApp}. If you want to use it with the webserver,
 * just jar the .class files at the top of the directory hierarchy
 * representing the package names, put the .jar into the webserver's
 * web/java/ directory and put the configuration snippet found in the
 * source into the webserver's web/WEB-INF/games.properties.xml.  
 * 
 * @author Ulrich Rhein, Fabian Pietsch
 */
public class ChatGame extends JFrame {

    /** Our serialization UID... */
    private static final long serialVersionUID = 1L;

    /** The frame's basic title, to which status information may be prepended. */
    private static final String TITLE_BASE = "BGO2 Chat Pseudo-Game";

    /** The user's session ID. */
    private final String userid;

    /** The game's session ID. */
    private final String gameid;

    /** Our chat panel. */
    private final ChatPanel chatPanel;

    /** Our GameManager's proxy instance. */
    private final GameManager gameManager;

    /** Information about our user. */
    private final PlayerStatus user;

    /** The game status polling thread. */
    private final Thread poller;

    /** Whether first game status has been retrieved yet. */
    private boolean gameStatusInitialized;

    /**
	 * Creates an even-more-pseudo-game,
	 * assuming the User has already been added to the Game at the server. 
	 * 
	 * @param userid  The user's session ID.
	 * @param gameid  The game's session ID.
	 */
    public ChatGame(String userid, String gameid) {
        this(userid, gameid, false, "http://localhost:8080/bgowebserver/services/ChatManager", null);
    }

    /**
	 * Creates a pseudo-game that adds the User to the Game at the server,
	 * then only provides a chat.
	 *  
	 * @param userid  The user's session ID.
	 * @param gameid  The game's session ID.
	 * @param spectator
	 *     Whether the user will be a spectator.
	 * @param gameurl
	 *     In theory, the game's WebService's URL. We run stand-alone, though.
     * @param chaturl The URL corresponding to the ChatManager WebService.
	 */
    public ChatGame(String userid, String gameid, boolean spectator, String gameurl, String chaturl) {
        this.userid = userid;
        this.gameid = gameid;
        chatPanel = new ChatPanel(userid, gameid, chaturl);
        chatPanel.appendln("Chat client initialized.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setTitle(TITLE_BASE);
        add(chatPanel);
        setVisible(true);
        if (gameurl == null) {
            chatPanel.appendln("Running as chat only.");
            gameManager = null;
            user = null;
            poller = null;
            chatPanel.pollServer();
        } else {
            final GameManagerProxy proxy = new GameManagerProxy();
            proxy.setEndpoint(gameurl);
            gameManager = proxy;
            try {
                gameManager.addPlayer(userid, gameid, spectator);
                user = gameManager.authenticatePlayer(userid, gameid);
            } catch (RemoteException e) {
                e.printStackTrace();
                System.err.println("ChatGame: Caught a remote exception," + " see above for a stack trace");
                throw new RuntimeException("ChatGame got remote exception" + " while adding/authenticating user, aborting", e);
            }
            if (user == null) {
                int result = JOptionPane.showConfirmDialog(this, "Can't authenticate user.\nContinue anyhow?", "User authentication failed", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.CANCEL_OPTION) {
                    throw new RuntimeException("ChatGame couldn't authenticate user" + " remotely and local user decided for abortion.");
                }
                chatPanel.appendln("Couldn't authenticate user.");
            } else {
                chatPanel.appendln("Hello " + user.getName() + "!");
                chatPanel.appendln("You are " + (user.isMaster() ? "" : "NOT ") + "the game's master.");
            }
            poller = new Thread() {

                @Override
                public void run() {
                    boolean doGameStatus = true;
                    while (true) {
                        if (doGameStatus) {
                            if (!updateGameStatus()) {
                                doGameStatus = false;
                            }
                        }
                        chatPanel.pollServer();
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            chatPanel.appendln("ChatGame polling thread interrupted");
                            break;
                        }
                    }
                    chatPanel.appendln("ChatGame polling thread stopped polling.");
                }
            };
            poller.start();
        }
    }

    /**
     * Polls the GameManager for new status.
     * 
     * @return Whether it is worthwhile to poll ever again.
     */
    private synchronized boolean updateGameStatus() {
        if (gameManager == null) {
            return false;
        }
        try {
            final GameStatus status = gameManager.getGameStatus(gameid);
            if (status == null) {
                chatPanel.appendln("Can't get GameStatus...");
                return false;
            }
            setTitle(status.getName() + " - " + user.getName() + " - " + TITLE_BASE);
            if (!gameStatusInitialized) {
                final PlayerStatus creator = gameManager.authenticatePlayer(status.getCreator(), gameid);
                if (creator == null) {
                    chatPanel.appendln("Can't get creator's name...");
                    return false;
                }
                chatPanel.appendln("This is " + creator.getName() + "'s game, '" + status.getName().replace("\\", "\\\\").replace("'", "\\'") + "'. There are now " + status.getNumplayers() + " players.");
                gameStatusInitialized = true;
            }
            return true;
        } catch (RemoteException e) {
            chatPanel.appendln("Can't get GameStatus: " + e.toString());
            e.printStackTrace();
            System.err.println("ChatGame: Caught a remote exception," + " see above for a stack trace.");
            return false;
        }
    }

    /**
     * Entry point into the application. Parses invocation arguments.
     * 
     * @param args  The invocation arguments.
     */
    public static void main(String[] args) {
        switch(args.length) {
            case 2:
                new ChatGame(args[0], args[1]);
                break;
            case 5:
                new ChatGame(args[0], args[1], new Boolean(args[2]), args[3], args[4]);
                break;
            default:
                System.out.println("Usage: ChatGame USER_SID GAME_SID [SPECT GAME_URL CHAT_URL]");
                System.out.println("The four-argument form simply indicates to run as pseudo-game.");
                System.exit(1);
        }
    }
}
