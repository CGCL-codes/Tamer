package pt.uc.dei.sdist.mytwitter.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * TCP Server.
 * 
 * @author Alexandre Vieira
 */
public class Server implements ClientHandlerListener {

    /** . */
    public static final String SIGNIN_STATE = "signin";

    /** The socket to listen for incoming connections. */
    private ServerSocket listener;

    /** The port to listen to. */
    private int port;

    /** This Server's thread. */
    private Thread thread;

    /** Wether this Server is running, or not. */
    private boolean running;

    /** . */
    private Set<ClientHandler> handlers;

    /** . */
    private Database database;

    /**
	 * Creates a Server.
	 */
    public Server(int port) {
        this.handlers = new HashSet<ClientHandler>();
        this.port = port;
        setRunning(false);
    }

    @Override
    public void onDone(ClientHandler handler) {
        handlers.remove(handler);
        System.out.printf("An handler is done.\n");
    }

    public User getUser(String name) {
        return database.getUsers().get(name);
    }

    public boolean isRegisteredUser(String name) {
        return database.getUsers().containsUser(name);
    }

    public User signUserIn(String name, String password) {
        if (isRegisteredUser(name) && getUser(name).getPassword().equals(password)) {
            getUser(name).setOnline(true);
            database.saveUsers();
            log("%s:%s is now signed in.\n", name, password);
            return getUser(name);
        }
        return null;
    }

    public User registerUser(String name, String password) {
        if (isRegisteredUser(name)) {
            return null;
        }
        database.getUsers().add(new User(name, password));
        database.getTweets().newUser(name);
        save();
        log("%s:%s is now registered.\n", name, password);
        return getUser(name);
    }

    public void submitTweet(Tweet tweet) {
        if (!isRegisteredUser(tweet.getAuthor())) {
            throw new IllegalArgumentException("Invalid tweet: the author is unknown.");
        }
        database.getTweets().add(tweet.getAuthor(), tweet);
        saveTweets();
    }

    public Tweet[] getAllTweetsFor(String username) {
        if (!isRegisteredUser(username)) {
            return null;
        }
        LinkedList<Tweet> allTweets = new LinkedList<Tweet>();
        List<String> following = getUser(username).getFollowing();
        Tweet[] tweets;
        tweets = getTweetsFor(username);
        for (Tweet tweet : tweets) {
            allTweets.add(tweet);
        }
        for (String name : following) {
            tweets = getTweetsFor(name);
            for (Tweet tweet : tweets) {
                allTweets.add(tweet);
            }
        }
        Collections.sort(allTweets);
        return allTweets.toArray(new Tweet[allTweets.size()]);
    }

    public Tweet[] getTweetsFor(String username) {
        if (!isRegisteredUser(username)) {
            return null;
        }
        TweetList list = database.getTweets().getTweets(username);
        Tweet[] tweets = new Tweet[list.size()];
        for (int i = 0; i < list.size(); i++) {
            tweets[i] = list.get(i);
        }
        return tweets;
    }

    public User[] searchUsers(String toSearch) {
        String[] names = database.getUsers().getUsernames();
        LinkedList<User> matches = new LinkedList<User>();
        for (String name : names) {
            if (name.toLowerCase().contains(toSearch.toLowerCase())) {
                matches.add(getUser(name));
            }
        }
        return matches.toArray(new User[matches.size()]);
    }

    public boolean follow(String username, String follower) {
        if (!isRegisteredUser(username) || !isRegisteredUser(follower)) {
            return false;
        }
        if (username.equals(follower)) {
            return false;
        }
        getUser(username).addFollower(follower);
        getUser(follower).follow(username);
        saveUsers();
        return true;
    }

    public User[] getFollowers(String name) {
        if (!isRegisteredUser(name)) {
            return null;
        }
        List<String> followers = getUser(name).getFollowers();
        User[] users = new User[followers.size()];
        for (int i = 0; i < followers.size(); i++) {
            users[i] = getUser(followers.get(i));
        }
        return users;
    }

    public User[] getFollowing(String name) {
        if (!isRegisteredUser(name)) {
            return null;
        }
        List<String> following = getUser(name).getFollowing();
        User[] users = new User[following.size()];
        for (int i = 0; i < following.size(); i++) {
            users[i] = getUser(following.get(i));
        }
        return users;
    }

    public User signUserOut(String name, String password) {
        if (isRegisteredUser(name) && getUser(name).getPassword().equals(password)) {
            getUser(name).setOnline(false);
            database.saveUsers();
            return getUser(name);
        }
        return null;
    }

    /**
	 * Launches the server. An IllegalStateException is thrown if this method is
	 * called while the Server is running.
	 * 
	 * @throws IOException
	 *             This exception is thrown if it isn't possible to intialize
	 *             the Server.
	 */
    public synchronized void launch() throws IOException {
        setRunning(true);
        try {
            init();
        } catch (IOException ioe) {
            setRunning(false);
            throw ioe;
        }
        branch();
        System.out.printf("Server launched at %d.\n", port);
    }

    /**
	 * Shuts down the server gracefully. The Server will wait for any
	 * ClientHandler to terminate.
	 */
    public synchronized void shutdown() {
        System.out.printf("Shuting down the server...\n");
        setRunning(false);
        terminateHandlers();
        if (!database.save()) {
            log("The database coudln't be stored...\n");
        }
        System.out.printf("Server shutted down.\n");
    }

    /**
	 * Initializes the server.
	 * 
	 * @throws IOException
	 *             This exception is thrown if the Server's socket can't be
	 *             opened.
	 */
    private void init() throws IOException {
        listener = new ServerSocket(port);
        thread = null;
        database = new Database();
        if (!database.load()) {
            log("Starting with a clean database...\n");
        }
    }

    /**
	 * Creates and starts the Server's thread.
	 */
    private void branch() {
        assert (thread == null);
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Server.this.run();
            }
        }, "MyTiwtter (Server)");
        thread.start();
    }

    /**
	 * TODO
	 */
    private void run() {
        while (running) {
            try {
                Client client = waitForConnection();
                ClientHandler handler = new ClientHandler(this, client);
                handlers.add(handler);
                handler.addListener(this);
                handler.handle();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        try {
            listener.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Waits for a connection.
	 * 
	 * @return A Client.
	 * @throws IOException
	 *             This exception is thrown if a problem is found when waiting
	 *             for a connection, or it wasn't possible to create the Client.
	 */
    private Client waitForConnection() throws IOException {
        Socket clientSocket = listener.accept();
        try {
            return new Client(clientSocket);
        } catch (IOException ioe) {
            clientSocket.close();
            throw new IOException("Couldn't create the client.", ioe);
        }
    }

    private void terminateHandlers() {
        for (ClientHandler handler : handlers) {
            try {
                handler.waitForMe();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public void saveUsers() {
        if (!database.saveUsers()) {
            log("Error while saving users...");
        }
    }

    public void saveTweets() {
        if (!database.saveTweets()) {
            log("Error while saving tweets...");
        }
    }

    public void save() {
        if (!database.save()) {
            log("Error while saving the database...");
        }
    }

    /**
	 * Enables the server to run. This method throws an IllegalStateException if
	 * the parameter equals TRUE and it's already running.
	 * 
	 * @param run
	 *            TRUE to enable the server, FALSE to disable it.
	 */
    private void setRunning(boolean run) {
        if (running && run) {
            throw new IllegalStateException("The server was already launched.");
        } else {
            running = run;
        }
    }

    public static void log(String format, Object... args) {
        System.out.printf(format, args);
    }
}
