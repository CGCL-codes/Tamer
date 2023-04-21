package pt.uc.dei.sdist.mytwitter.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import pt.uc.dei.sdist.mytwitter.common.FollowResponse;
import pt.uc.dei.sdist.mytwitter.common.FollowersFoundResponse;
import pt.uc.dei.sdist.mytwitter.common.FollowersMatchResponse;
import pt.uc.dei.sdist.mytwitter.common.FollowingFoundResponse;
import pt.uc.dei.sdist.mytwitter.common.FollowingMatchResponse;
import pt.uc.dei.sdist.mytwitter.common.Message;
import pt.uc.dei.sdist.mytwitter.common.MessageException;
import pt.uc.dei.sdist.mytwitter.common.Messages;
import pt.uc.dei.sdist.mytwitter.common.RegisterResponse;
import pt.uc.dei.sdist.mytwitter.common.SearchFoundResponse;
import pt.uc.dei.sdist.mytwitter.common.SearchMatchResponse;
import pt.uc.dei.sdist.mytwitter.common.SignInResponse;
import pt.uc.dei.sdist.mytwitter.common.TweetsResponse;

/**
 * @author Alexandre Vieira
 */
public class Client {

    /** . */
    private Server server;

    /** . */
    private String host;

    /** . */
    private int port;

    /** . */
    private User user;

    /** This Client's thread. */
    private Thread thread;

    /** Wether this Client is running, or not. */
    private boolean running;

    /** . */
    private List<ClientListener> listeners;

    public Client() {
        this.listeners = new LinkedList<ClientListener>();
        setRunning(false);
    }

    /**
	 * Startes the client. An IllegalStateException is thrown if this method is
	 * called while the Client is running.
	 * 
	 * @throws IOException
	 *             This exception is thrown if it isn't possible to intialize
	 *             the Client.
	 */
    public synchronized void start() throws IllegalArgumentException {
        setRunning(true);
        branch();
    }

    /**
	 * Shuts down the client gracefully.
	 */
    public synchronized void stop() {
        setRunning(false);
        disconnect();
    }

    /**
	 * Creates and starts the Client's thread.
	 */
    private void branch() {
        assert (thread == null);
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Client.this.run();
            }
        }, "MyTiwtter (Client)");
        thread.start();
    }

    private void run() {
        while (running) {
            if (isConnected()) {
                try {
                    String literal = server.receiveMessage();
                    try {
                        Message message = new Message(literal);
                        if (message.getType().equals(Messages.TYPE_REQUEST)) {
                            return;
                        }
                        if (message.getKey().equals(Messages.KEY_SIGNIN)) {
                            onSignInResponse(literal);
                        } else if (message.getKey().equals(Messages.KEY_REGISTER)) {
                            onRegisterResponse(literal);
                        } else if (message.getKey().equals(Messages.KEY_TWEETS)) {
                            onTweetsResponse(literal);
                        } else if (message.getKey().equals(Messages.KEY_SEARCH)) {
                            onSearchResponse(literal);
                        } else if (message.getKey().equals(Messages.KEY_FOLLOW)) {
                            onFollowResponse(literal);
                        } else if (message.getKey().equals(Messages.KEY_FOLLOWERS)) {
                            onFollowersResponse(literal);
                        } else if (message.getKey().equals(Messages.KEY_FOLLOWING)) {
                            onFollowingResponse(literal);
                        }
                    } catch (MessageException me) {
                    }
                } catch (IOException ioe) {
                    disconnect();
                    fireOnConnectionTemporarilyLost();
                }
            } else {
                stayIdleFor(100L);
            }
        }
        if (isConnected()) {
            disconnect();
        }
    }

    private void onSignInResponse(String message) {
        try {
            SignInResponse response = new SignInResponse(message);
            if (response.isSignedIn()) {
                user = new User(response.getName(), response.getPassword());
            } else {
                user = null;
            }
            fireOnUserSignedIn(response.isSignedIn());
        } catch (MessageException me) {
            me.printStackTrace();
        }
    }

    private void onRegisterResponse(String message) {
        try {
            RegisterResponse response = new RegisterResponse(message);
            fireOnUserRegistered(response.isRegistered());
        } catch (MessageException me) {
            me.printStackTrace();
        }
    }

    private void onTweetsResponse(String message) {
        try {
            TweetsResponse response = new TweetsResponse(message);
            boolean more = response.isMore();
            long timeStamp = response.getTime();
            String author = response.getAuthor();
            String content = response.getMessage();
            fireOnTweetReceived(new Tweet(timeStamp, author, content), more);
            String literal;
            while (more) {
                try {
                    literal = server.receiveMessage();
                    response = new TweetsResponse(literal);
                    more = response.isMore();
                    timeStamp = response.getTime();
                    author = response.getAuthor();
                    content = response.getMessage();
                    fireOnTweetReceived(new Tweet(timeStamp, author, content), more);
                } catch (IOException ioe) {
                    disconnect();
                    fireOnConnectionTemporarilyLost();
                    return;
                }
            }
        } catch (MessageException me) {
            me.printStackTrace();
        }
    }

    private void onSearchResponse(String message) {
        try {
            SearchFoundResponse response = new SearchFoundResponse(message);
            fireOnUsersFound(response.isFound(), response.getUsername());
        } catch (MessageException me) {
            try {
                SearchMatchResponse response = new SearchMatchResponse(message);
                boolean more = response.isMore();
                String match = response.getMatch();
                fireOnUserMatch(more, match);
                String literal;
                while (more) {
                    try {
                        literal = server.receiveMessage();
                        response = new SearchMatchResponse(literal);
                        more = response.isMore();
                        match = response.getMatch();
                        fireOnUserMatch(more, match);
                    } catch (IOException ioe) {
                        disconnect();
                        fireOnConnectionTemporarilyLost();
                        return;
                    }
                }
            } catch (MessageException me1) {
                me1.printStackTrace();
            }
        }
    }

    private void onFollowResponse(String message) {
        try {
            FollowResponse response = new FollowResponse(message);
            fireOnFollowingUser(response.isFollowing(), response.getUsername());
        } catch (MessageException me) {
            me.printStackTrace();
        }
    }

    private void onFollowersResponse(String message) {
        try {
            FollowersFoundResponse response = new FollowersFoundResponse(message);
            fireOnFollowersFound(response.isFound());
        } catch (MessageException me) {
            try {
                FollowersMatchResponse response = new FollowersMatchResponse(message);
                boolean more = response.isMore();
                String match = response.getMatch();
                fireOnFollowersMatch(more, match);
                String literal;
                while (more) {
                    try {
                        literal = server.receiveMessage();
                        response = new FollowersMatchResponse(literal);
                        more = response.isMore();
                        match = response.getMatch();
                        fireOnFollowersMatch(more, match);
                    } catch (IOException ioe) {
                        disconnect();
                        fireOnConnectionTemporarilyLost();
                        return;
                    }
                }
            } catch (MessageException me1) {
                me1.printStackTrace();
            }
        }
    }

    private void onFollowingResponse(String message) {
        try {
            FollowingFoundResponse response = new FollowingFoundResponse(message);
            fireOnFollowingFound(response.isFound());
        } catch (MessageException me) {
            try {
                FollowingMatchResponse response = new FollowingMatchResponse(message);
                boolean more = response.isMore();
                String match = response.getMatch();
                fireOnFollowingMatch(more, match);
                String literal;
                while (more) {
                    try {
                        literal = server.receiveMessage();
                        response = new FollowingMatchResponse(literal);
                        more = response.isMore();
                        match = response.getMatch();
                        fireOnFollowingMatch(more, match);
                    } catch (IOException ioe) {
                        disconnect();
                        fireOnConnectionTemporarilyLost();
                        return;
                    }
                }
            } catch (MessageException me1) {
                me1.printStackTrace();
            }
        }
    }

    private void stayIdleFor(long milliseconds) {
        if ((thread != null) && (Thread.currentThread() == thread)) {
            Thread.yield();
        }
    }

    public void signUserIn(String name, String password) {
        try {
            server.sendMessage(Messages.getSigninRequestFor(name, password));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void signUserOut() {
        try {
            server.sendMessage(Messages.getSignoutRequestFor(user.getName(), user.getPassword()));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void registerUser(String name, String password) {
        try {
            server.sendMessage(Messages.getRegisterRequestFor(name, password));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void sendTweet(String tweet) {
        try {
            server.sendMessage(Messages.getTweetRequestFor(tweet));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void requestAllTweets() {
        try {
            server.sendMessage(Messages.getTweetsRequestFor(user.getName()));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void searchUsers(String name) {
        try {
            server.sendMessage(Messages.getSearchRequestFor(name));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void followUser(String name) {
        try {
            server.sendMessage(Messages.getFollowRequestFor(name));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void requestFollowers() {
        try {
            server.sendMessage(Messages.getFollowersRequestFor(user.getName()));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public void requestFollowing() {
        try {
            server.sendMessage(Messages.getFollowingRequestFor(user.getName()));
        } catch (IOException ioe) {
            disconnect();
            fireOnConnectionTemporarilyLost();
        }
    }

    public synchronized boolean connect(String host, int port) {
        this.host = host;
        this.port = port;
        return connect();
    }

    private synchronized boolean connect() {
        if (server != null) {
            try {
                server.dispose();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        int counter = 0;
        while (!isConnected() && (counter < 4)) {
            try {
                server = new Server(host, port);
                if (user != null) {
                    signUserIn(user.getName(), user.getPassword());
                }
                fireOnConnectionEstablished();
                return true;
            } catch (UnknownHostException uhe) {
                server = null;
            } catch (IOException ioe) {
                server = null;
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ie) {
                }
                counter++;
            }
        }
        user = null;
        fireOnConnectionLost();
        return false;
    }

    public synchronized void disconnect() {
        if (server != null) {
            try {
                server.dispose();
            } catch (IOException ioe) {
            }
            server = null;
        }
        user = null;
    }

    public boolean isUserSignedIn() {
        return (user != null);
    }

    public boolean isConnected() {
        return (server != null);
    }

    protected Server getServer() {
        return server;
    }

    /**
	 * Enables the client to run. This method throws an IllegalStateException if
	 * the parameter equals TRUE and it's already running.
	 * 
	 * @param run
	 *            TRUE to enable the client, FALSE to disable it.
	 */
    private synchronized void setRunning(boolean run) {
        if (running && run) {
            throw new IllegalStateException("The client was already launched.");
        } else {
            running = run;
        }
    }

    public void addListener(ClientListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ClientListener listener) {
        listeners.remove(listener);
    }

    private void fireOnConnectionEstablished() {
        for (ClientListener listener : listeners) {
            listener.onConnectionEstablished();
        }
    }

    private void fireOnConnectionTemporarilyLost() {
        for (ClientListener listener : listeners) {
            listener.onConnectionTemporarilyLost();
        }
    }

    private void fireOnConnectionLost() {
        for (ClientListener listener : listeners) {
            listener.onConnectionLost();
        }
    }

    private void fireOnUserSignedIn(boolean signed) {
        for (ClientListener listener : listeners) {
            listener.onUserSignedIn(signed);
        }
    }

    private void fireOnUserRegistered(boolean registered) {
        for (ClientListener listener : listeners) {
            listener.onUserRegistered(registered);
        }
    }

    private void fireOnTweetReceived(Tweet tweet, boolean more) {
        for (ClientListener listener : listeners) {
            listener.onTweetReceived(tweet, more);
        }
    }

    private void fireOnUsersFound(boolean found, String name) {
        for (ClientListener listener : listeners) {
            listener.onUsersFound(found, name);
        }
    }

    private void fireOnUserMatch(boolean more, String name) {
        for (ClientListener listener : listeners) {
            listener.onUserMatch(more, name);
        }
    }

    private void fireOnFollowingUser(boolean following, String name) {
        for (ClientListener listener : listeners) {
            listener.onFollowingUser(following, name);
        }
    }

    private void fireOnFollowersFound(boolean found) {
        for (ClientListener listener : listeners) {
            listener.onFollowersFound(found);
        }
    }

    private void fireOnFollowersMatch(boolean more, String name) {
        for (ClientListener listener : listeners) {
            listener.onFollowersMatch(more, name);
        }
    }

    private void fireOnFollowingFound(boolean found) {
        for (ClientListener listener : listeners) {
            listener.onFollowingFound(found);
        }
    }

    private void fireOnFollowingMatch(boolean more, String name) {
        for (ClientListener listener : listeners) {
            listener.onFollowingMatch(more, name);
        }
    }
}
