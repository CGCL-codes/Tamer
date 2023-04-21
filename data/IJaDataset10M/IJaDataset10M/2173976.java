package org.jogre.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.Locale;
import org.jogre.common.GameList;
import org.jogre.common.IError;
import org.jogre.common.IJogre;
import org.jogre.common.JogreGlobals;
import org.jogre.common.util.JogreLabels;
import org.jogre.common.util.JogreLogger;
import org.jogre.server.controllers.ServerControllerList;
import org.jogre.server.data.IServerData;
import org.jogre.server.data.ServerDataException;
import org.jogre.server.data.ServerDataFactory;
import org.jogre.server.data.db.DBConnection;

/**
 * <p>This is the all important JogreServer class.</p>
 * 
 * <p>When a client connects to a <code>JogreServer</code> a new
 * <code>ServerConnectionThread</code> is created in its own Thread which will
 * handle all communciation between the server and the client. The connect
 * method returns the correct ServerConnectionThread to the server.</p>
 *
 * <p>If a server keeps state then the abstract <code>getJogreModel()</code> method should
 * return the correct implementation of the <code>JogreModel</code>. If a game should keep
 * state (this allows demanding games to run faster but means other users can't
 * join a table and watch a game) then this method should simply return null.</p>
 *
 * @author  Bob Marks
 * @version Beta 0.3
 */
public class JogreServer {

    /** Logging */
    JogreLogger logger = new JogreLogger(this.getClass());

    private static JogreServer instance;

    /** Default server port. */
    public static final int DEFAULT_SERVER_PORT = 1790;

    /** Server port we are listening on. */
    protected int serverPort;

    protected ServerSocket listenSocket;

    /** List of server connenction objects. */
    protected ConnectionList connections;

    /** Link to the various games, and tables within games. */
    protected GameList gameList;

    /** Link to the various parsers. */
    protected ServerControllerList serverControllerList;

    /** Game loader. */
    protected GameLoader gameLoader;

    /** Declare how a user connection. */
    protected IServerData dataConnection = null;

    private ServerLabels labels = ServerLabels.getInstance();

    private long startTime;

    /**
	 * Default server constructor.
	 */
    private JogreServer() {
        startTime = System.currentTimeMillis();
        System.out.println("------------------------------------------------------------------");
        System.out.println("                J O G R E   G A M E S   S E R V E R");
        System.out.println("------------------------------------------------------------------");
        System.out.println(labels.get("author") + ":\t\t\tBob Marks");
        System.out.println(labels.get("version") + ":\t\t" + IJogre.VERSION);
        serverPort = DEFAULT_SERVER_PORT;
        ServerProperties.setUpFromFile();
        System.out.println(labels.get("server.properties") + ":\t" + ServerProperties.getInstance().getServerFile().getAbsolutePath());
        setServerPort(ServerProperties.getInstance().getServerPort());
    }

    /**
	 * Return the single instance of this server.
	 *
	 * @return
	 */
    public static JogreServer getInstance() {
        if (instance == null) instance = new JogreServer();
        return instance;
    }

    /**
	 * Initialise the JOGRE server.
	 * 
	 * @throws ServerDataException 
	 */
    public void init() {
        this.connections = new ConnectionList();
        this.serverControllerList = new ServerControllerList();
        this.gameList = new GameList();
        this.gameLoader = new GameLoader(gameList, serverControllerList);
        this.dataConnection = ServerDataFactory.getInstance();
        try {
            dataConnection.resetSnapshot(gameList.getGameKeys());
            ShutDownHook shutDown = new ShutDownHook(this);
            Runtime.getRuntime().addShutdownHook(shutDown);
        } catch (ServerDataException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
	 * <p>This method parses the commands handed in from the command prompt.
	 * For example:</p>
	 *
	 * <code>-port=1234</code>
	 *
	 * <p>This sets the port for the server to listen on to 1234. More complex
	 * servers should over write this method and handle its specific commands
	 * here.</p>
	 *
	 * @param args     Additional arguments from the command line.
	 */
    public void parseCommandLineArguments(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String argument = args[i];
                if (argument.startsWith("-port=")) {
                    int pos = argument.indexOf("=");
                    try {
                        int portNum = Integer.parseInt(argument.substring(pos + 1));
                        setServerPort(portNum);
                    } catch (NumberFormatException nfEx) {
                        usage();
                    }
                } else if (argument.startsWith("-lang=")) {
                    int pos = argument.indexOf("=");
                    try {
                        String lang = argument.substring(pos + 1);
                        JogreGlobals.setLocale(lang);
                    } catch (Exception nfEx) {
                        usage();
                    }
                }
            }
        }
        Locale l = JogreGlobals.getLocale();
        System.out.println(labels.get("server.port") + ":\t\t" + getServerPort());
        System.out.println(labels.get("language") + ":\t\t" + l.getLanguage() + " (" + l.getDisplayLanguage() + ")");
    }

    /**
	 * Prints out the usage of the server.
	 */
    private void usage() {
        labels = ServerLabels.getInstance();
        System.out.println(labels.get("jogre.server.version") + ": " + IJogre.VERSION);
        System.out.println("\n" + labels.get("usage") + ":");
        System.out.println("\n\tjava org.jogre.server.JogreServer [" + labels.get("additional.arguments") + "]");
        System.out.println("\n" + labels.get("arguments") + ":");
        System.out.println("\t-port=x         x=" + labels.get("port.number.default.1790"));
        System.out.println("\t-lang=x         x=" + JogreGlobals.SUPPORTED_LANGS);
    }

    /**
	 * Run method which runs a new server and listens for clients on the
	 * specified port.
	 */
    public void run() {
        System.out.println("------------------------------------------------------------------");
        System.out.println(labels.get("games.being.served") + " (" + gameList.size() + "):");
        System.out.print(gameLoader);
        System.out.println("------------------------------------------------------------------");
        try {
            listenSocket = new ServerSocket(serverPort);
            System.out.println(labels.get("jogre.games.server.listening.on.port") + ": " + serverPort);
            new HeartBeatThread(this);
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(true);
            long timeStarted = System.currentTimeMillis() - startTime;
            System.out.println("\n" + labels.get("started.in", new String[] { nf.format(timeStarted) }));
            while (true) {
                Socket clientSocket = listenSocket.accept();
                ServerConnectionThread conn = new ServerConnectionThread(clientSocket);
                conn.start();
            }
        } catch (BindException bindEx) {
            System.out.println(labels.get("jogre.server.already.running.on.port") + ": " + serverPort);
        } catch (Exception genEx) {
            genEx.printStackTrace();
        } finally {
            try {
                if (listenSocket != null) listenSocket.close();
            } catch (IOException ioEx) {
            }
            System.exit(0);
        }
    }

    /**
	 * Return the current list of games on this server.
	 *
	 * @return
	 */
    public GameList getGameList() {
        return gameList;
    }

    /**
	 * Set the server port.
	 *
	 * @param serverPort
	 */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
	 * Return the server port.
	 *
	 * @return
	 */
    public int getServerPort() {
        return serverPort;
    }

    /**
	 * Return listen socket.
	 * 
	 * @return
	 */
    public ServerSocket getListenSocket() {
        return listenSocket;
    }

    /**
	 * Return the ConnectionList object of connections to the server.
	 *
	 * @return
	 */
    public ConnectionList getConnections() {
        return connections;
    }

    /**
	 * Return the server parser list which includes the standard base, game
	 * and table parser.  Also includes parsers for each specific game.
	 *
	 * @return   List of server parsers
	 */
    public ServerControllerList getControllers() {
        return serverControllerList;
    }

    /**
	 * Return the connection to the users.
	 * 
	 * @return
	 */
    public IServerData getServerData() {
        return dataConnection;
    }

    /**
	 * Return the game loader.
	 * 
	 * @return
	 */
    public GameLoader getGameLoader() {
        return gameLoader;
    }

    /**
	 * Perform some basic tests before server starts up properly e.g. check the server
	 * port 
	 */
    public void initialTests() {
        ServerProperties serverProps = ServerProperties.getInstance();
        String dataType = serverProps.getCurrentServerData();
        System.out.println(labels.get("persistent.data") + ":\t" + dataType);
        if (dataType.equals(IServerData.DATABASE)) {
            String curDatabaseConn = serverProps.getCurrentDatabaseConnection();
            System.out.println(labels.get("database.connection") + ":\t" + curDatabaseConn);
            System.out.println(labels.get("database.url") + ":\t\t" + serverProps.getConnectionURL(curDatabaseConn));
            System.out.println(labels.get("database.driver") + ":\t" + serverProps.getConnectionDriver(curDatabaseConn));
        }
        try {
            System.out.println("------------------------------------------------------------------");
            System.out.println(labels.get("inital.tests") + ":");
            ServerSocket testSocket = new ServerSocket(serverPort);
            System.out.println("\t" + labels.get("server.port.is.available"));
            testSocket.close();
        } catch (BindException bindEx) {
            System.out.println(labels.get("jogre.server.already.running.on.port") + ": " + serverPort);
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dataType.equals(IServerData.DATABASE)) {
            int error = IError.NO_ERROR;
            error = DBConnection.testConnection(serverProps.getDBDriver(), serverProps.getDBConnURL(), serverProps.getDBUsername(), serverProps.getDBPassword(), true);
            if (error != IError.NO_ERROR) {
                System.out.println(JogreLabels.getError(error) + " " + serverProps.getDBConnURL());
                System.exit(-1);
            }
        }
    }

    /**
	 * Main method which creates a single instance of the server,
	 * parses the commandline arguments and then runs the server.
	 *
	 * @param args     Additional arguments from command line.
	 */
    public static void main(String[] args) {
        JogreServer server = getInstance();
        server.parseCommandLineArguments(args);
        server.initialTests();
        server.init();
        server.run();
    }
}
