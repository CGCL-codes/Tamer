package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import communication.Request;

/**
 * <h1>Class ServerMonitor.</h1>
 * 
 * The class ServerMonitor keeps track of all the sessions and conversations
 * opened in the system. For commodity and efficiency, they are stored in 
 * Hash tables in order to be retrieved very quickly.
 * It is implemented following the Monitor pattern in order to avoid "race
 * conditions" in the access to the objects from the different threads
 * which can access them.
 * It also follows the Singleton pattern to ensure just one instance of the
 * ServerMonitor.  
 * 
 * @author Tomas
 * @date March 4 2009
 */
public class ServerMonitor {

    /**
	 * Single instance of the ServerMonitor
	 */
    protected static ServerMonitor instance = null;

    /**
	 * Sessions currently active in the system
	 */
    protected HashMap<String, Session> sessions = null;

    /**
	 * Conversations currently opened in the system
	 */
    protected HashMap<String, Conversation> conversations = null;

    /**
	 * Buffer to store the requests made by the clients.
	 */
    protected RequestBuffer buffer = null;

    /**
	 * Collection of threads which are listening for client's requests.
	 * To be implemented as an ArrayList.
	 */
    protected List<InputListener> input = null;

    /**
	 * Collection of threads which are serving the requests.
	 * To be implemented as an ArrayList.
	 */
    protected List<RequestConsumer> consumers = null;

    /**
	 * Idle thread to poll if inactive sessions are still opened.
	 */
    protected IdleThread idle = null;

    /**
	 * Number of threads to be created when the server is setup
	 */
    protected final int NUMTHREADS = 5;

    /**
	 * Default constructor. Initializes an instance of the class ServerMonitor.
	 * It is private to enforce the Singleton pattern.
	 */
    private ServerMonitor() {
        sessions = new HashMap<String, Session>();
        conversations = new HashMap<String, Conversation>();
        buffer = new RequestBuffer();
        input = new ArrayList<InputListener>();
        consumers = new ArrayList<RequestConsumer>();
        idle = new IdleThread();
        for (int i = 0; i < NUMTHREADS; i++) {
            input.add(new InputListener());
            consumers.add(new RequestConsumer());
        }
    }

    /**
	 * Obtains the single instance of the class.
	 * 
	 * @return
	 * 		Single instance of the class.
	 */
    public static ServerMonitor getInstance() {
        if (instance == null) {
            instance = new ServerMonitor();
        }
        return instance;
    }

    /**
	 * Starts the threads.
	 */
    public synchronized void startServer() {
        Iterator<InputListener> itl = input.iterator();
        while (itl.hasNext()) {
            InputListener il = itl.next();
            il.start();
        }
        Iterator<RequestConsumer> it = consumers.iterator();
        while (it.hasNext()) {
            RequestConsumer rc = it.next();
            rc.start();
        }
        idle.start();
    }

    /**
	 * Stops the threads in a nicely way.
	 */
    public synchronized void stopServer() {
        Iterator<InputListener> itl = input.iterator();
        while (itl.hasNext()) {
            InputListener il = itl.next();
            il.finish();
            try {
                il.join(500);
            } catch (Exception e) {
                LogException.getInstance().logException(e);
            }
        }
        Iterator<RequestConsumer> it = consumers.iterator();
        while (it.hasNext()) {
            RequestConsumer rc = it.next();
            rc.finish();
            try {
                rc.join(500);
            } catch (InterruptedException e) {
                LogException.getInstance().logException(e);
            }
        }
        idle.finish();
        try {
            idle.join(500);
        } catch (Exception e) {
            LogException.getInstance().logException(e);
        }
    }

    /**
	 * Adds a new session to the Server.
	 * 
	 * @param s
	 * 		Session to be added.
	 */
    public synchronized void addSession(Session s) {
        if (sessions.containsKey(s.getOwner().getUsername())) {
            sessions.remove(s.getOwner().getUsername());
        }
        sessions.put(s.getOwner().getUsername(), s);
    }

    /**
	 * Adds a conversation to the Server.
	 * 
	 * @param c
	 * 		Conversation to be added to the server.
	 */
    public synchronized void addConversation(Conversation c) {
        conversations.put(c.getID(), c);
    }

    /**
	 * Looks for a session for a particular user.
	 * 
	 * @param username
	 * 		User whose session we are looking for.
	 * @return
	 * 		User's session, if exists; null, otherwise.
	 */
    public synchronized Session getSession(String username) {
        return sessions.get(username);
    }

    /**
	 * Looks for a conversation given the ID.
	 * 
	 * @param convID
	 * 		ID of the conversation we are looking for.
	 * @return
	 * 		Conversation with such ID, if exists; null, otherwise.
	 */
    public synchronized Conversation getConversation(String convID) {
        return conversations.get(convID);
    }

    /**
	 * Removes a Session for a user.
	 * 
	 * @param username
	 * 		User's name whose session must be removed.
	 */
    public synchronized void removeSession(String username) {
        sessions.remove(username);
    }

    /**
	 * Removes a conversation from the server given its ID.
	 * 
	 * @param convID
	 * 		Conversation ID to be removed.
	 */
    public synchronized void removeConversation(String convID) {
        sessions.remove(convID);
    }

    /**
	 * Stores a Request into the buffer.
	 * 
	 * @param r
	 * 		Request to be stored.
	 */
    public synchronized void storeRequest(Request r) {
        buffer.storeRequest(r);
    }

    /**
	 * Obtains a Request from the buffer.
	 * 
	 * @return
	 * 		Request from the buffer, if any; null, otherwise.
	 */
    public synchronized Request getRequest() {
        return buffer.getRequest();
    }

    /**
	 * Obtains the names of all the users online.
	 * 
	 * @return
	 * 		Iterator over the user's names who are online.
	 */
    public synchronized Iterator<String> getUsersOnline() {
        return sessions.keySet().iterator();
    }

    /**
	 * Obtains all the conversations IDs.
	 * 
	 * @return
	 * 		Iterator over the conversation IDs.
	 */
    public synchronized Iterator<String> getConversationsIDs() {
        return conversations.keySet().iterator();
    }

    /**
	 * Obtains the number of sessions.
	 * 
	 * @return
	 * 		Number of sessions.
	 */
    public synchronized long numSessions() {
        return sessions.keySet().size();
    }

    /**
	 * Obtains the number of conversations.
	 * 
	 * @return
	 * 		Number of conversations.
	 */
    public synchronized long numConversations() {
        return conversations.keySet().size();
    }
}
