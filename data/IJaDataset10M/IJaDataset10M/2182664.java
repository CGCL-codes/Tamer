package com.amethyst.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import com.amethyst.core.*;

/**
 * A TCPServer class that handles the collection of clients and raises events when clients connect, disconnect, and data is sent or received 
 * @author David
 */
public class TCPServer extends NetBase {

    /**
	 * Creates a cached ThreadPool object
	 */
    private ExecutorService mThreadPool = Executors.newCachedThreadPool();

    /**
	 * The socket that this server is bound on
	 */
    private ServerSocket mServerSocket = null;

    /**
	 * The list of clients that this server will be keeping alive
	 */
    private CopyOnWriteArrayList<TCPNetConnection> mClients = new CopyOnWriteArrayList<TCPNetConnection>();

    /**
	 * The maximum number of users that will be allowed to connect to this server
	 */
    private int mMaxNumOfUsers = 0;

    /**
	 * The current number of users on this server, currently connected
	 */
    private int mCurrentNumberOfUsers = 0;

    /**
	 * The local port number
	 */
    private int mLocalPort = 0;

    /**
	 * The name of this server
	 */
    private String mServerName;

    /**
	 * The socket for listening on
	 */
    private Socket mNewSocket = null;

    /**
	 * The control value for the listening thread
	 */
    private boolean exitListeningThread = false;

    /**
	 * The control value for the refresh thread
	 */
    private boolean exitRefreshThread = false;

    /**
	 * The control value for the worker thread
	 */
    private boolean exitWorkerThread = false;

    /**
	 * This object is the thread-safety object for accessing the client list
	 */
    private static Object clientThreadLock = new Object();

    /**
	 * The list of listeners who are waiting for a client connected event
	 */
    private List<ClientConnectedListener> mClientConnectedListeners = new ArrayList<ClientConnectedListener>();

    /**
	 * The list of listeners who are waiting for a client disconnected event
	 */
    private List<ClientDisconnectedListener> mClientDisconnectedListeners = new ArrayList<ClientDisconnectedListener>();

    /**
	 * The list of listeners who are waiting for a data received event
	 */
    private List<DataReceivedListener> mDataReceivedListeners = new ArrayList<DataReceivedListener>();

    /**
	 * The list of listeners who are waiting for a data sent event
	 */
    private List<ErrorRaisedListener> mErrorRaisedListeners = new ArrayList<ErrorRaisedListener>();

    /**
	 * The object that refreshes all the clients or drops them
	 */
    private final Runnable mThreadRefreshObject = new Runnable() {

        @Override
        public void run() {
            while (!exitRefreshThread) {
                List<TCPNetConnection> mClientsToRemove = new ArrayList<TCPNetConnection>();
                ListIterator<TCPNetConnection> iter;
                synchronized (clientThreadLock) {
                    iter = mClients.listIterator();
                }
                while (iter != null && iter.hasNext()) {
                    TCPNetConnection conn = iter.next();
                    try {
                        conn.SendData(new NetMessage("PING", NetMessageType.MESSAGE_SYSTEM));
                    } catch (IOException ioe) {
                        conn.Dispose();
                        mClientsToRemove.add(conn);
                        ClientDisconnectedEvent evt = new ClientDisconnectedEvent(this, conn);
                        Iterator<ClientDisconnectedListener> listeners = mClientDisconnectedListeners.iterator();
                        while (listeners.hasNext()) {
                            listeners.next().OnClientDisconnected(evt);
                        }
                    }
                }
                if (mClientsToRemove.size() > 0) {
                    synchronized (clientThreadLock) {
                        mClients.removeAll(mClientsToRemove);
                        mCurrentNumberOfUsers -= mClientsToRemove.size();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    raiseErrorEvent(this, null, new InterruptedException("Server cleanup thread interrupted. (Code: 41)"));
                }
            }
        }
    };

    /**
		 * The object that reads data from all the clients
		 */
    private final Runnable mThreadWorkObject = new Runnable() {

        @Override
        public void run() {
            while (!exitWorkerThread) {
                ListIterator<TCPNetConnection> iter;
                synchronized (clientThreadLock) {
                    iter = mClients.listIterator();
                }
                while (iter != null && iter.hasNext()) {
                    try {
                        TCPNetConnection conn = iter.next();
                        NetMessage message = null;
                        if (conn.isConnected()) {
                            message = conn.ReadData();
                        }
                        DataReceivedEvent evt = new DataReceivedEvent(this, conn, message);
                        Iterator<DataReceivedListener> listeners = mDataReceivedListeners.iterator();
                        while (listeners.hasNext()) {
                            listeners.next().OnDataReceived(evt);
                        }
                    } catch (IOException ioe) {
                        ioe.toString();
                    } catch (Exception ex) {
                        raiseErrorEvent(this, null, ex);
                    }
                }
                try {
                    int threadSleepAmount = (mCurrentNumberOfUsers >= 100) ? 1 : 100 - mCurrentNumberOfUsers;
                    Thread.sleep(threadSleepAmount);
                } catch (InterruptedException e) {
                    raiseErrorEvent(this, null, new InterruptedException("Server cleanup thread interrupted. (Code: 37)"));
                }
            }
        }
    };

    /**
	 * The object which listens for new clients
	 */
    private final Runnable mThreadListenObject = new Runnable() {

        @Override
        public void run() {
            try {
                while (!exitListeningThread) {
                    mNewSocket = mServerSocket.accept();
                    if (mCurrentNumberOfUsers + 1 > mMaxNumOfUsers) {
                        TCPNetConnection tempConn = new TCPNetConnection(mNewSocket);
                        tempConn.SendData("Server Full");
                        tempConn.Dispose();
                        raiseErrorEvent(this, null, new Exception("Rejecting client from " + tempConn.getHostname() + " due to the limit for maximum concurrent connections reached. (Code: 27)"));
                    } else {
                        TCPNetConnection newClient = new TCPNetConnection(mNewSocket);
                        synchronized (clientThreadLock) {
                            mClients.add(newClient);
                            mCurrentNumberOfUsers += 1;
                        }
                        ClientConnectedEvent evt = new ClientConnectedEvent(this, newClient);
                        Iterator<ClientConnectedListener> listeners = mClientConnectedListeners.iterator();
                        while (listeners.hasNext()) {
                            listeners.next().OnClientConnected(evt);
                        }
                    }
                }
            } catch (IOException e) {
                Stop();
                raiseErrorEvent(this, null, new InterruptedException("Error during server initialization or listening (Code: 25)" + e.getLocalizedMessage()));
            }
        }
    };

    /**
	 * Creates a basic TCP Server which is not bound to any addresses
	 */
    public TCPServer() {
        super();
    }

    /**
	 * Creates a TCP Server with a given name on a specified port
	 * @param ServerName The name of this server. This name will be shared with clients who attempt to connect.
	 * @param LocalPort The port that this server is bound on
	 * <br /><i>The server will be started with a maximum number of users set to 32</i>
	 */
    public TCPServer(String ServerName, int LocalPort) {
        this(ServerName, LocalPort, 32);
    }

    /**
	 * Creates a TCP server with a maximum number of users
	 * @param ServerName The name of this server
	 * @param LocalPort The port that this server is bound on
	 * @param MaxUsers The maximum number of users allowed to be connected to this server
	 */
    public TCPServer(String ServerName, int LocalPort, int MaxUsers) {
        this();
        mServerName = ServerName;
        mLocalPort = LocalPort;
        mMaxNumOfUsers = MaxUsers;
    }

    public TCPServer(String ServerName, int LocalPort, int MaxUsers, ConnectionListener eventListener) {
        this(ServerName, LocalPort, MaxUsers);
        if (eventListener instanceof ClientConnectedListener) AddClientConnectedListener(eventListener);
        if (eventListener instanceof ClientDisconnectedListener) AddClientDisconnectedListener(eventListener);
        if (eventListener instanceof ErrorRaisedListener) AddErrorRaisedListener(eventListener);
        if (eventListener instanceof DataReceivedListener) AddDataReceivedListener(eventListener);
    }

    /**
	 * Starts this server
	 * @throws IOException Thrown when the server cannot be bound on the given port when this object was created.
	 */
    public void Start() throws IOException {
        exitListeningThread = false;
        exitRefreshThread = false;
        exitWorkerThread = false;
        mServerSocket = new ServerSocket(mLocalPort);
        mThreadPool.execute(mThreadWorkObject);
        mThreadPool.execute(mThreadRefreshObject);
        mThreadPool.execute(mThreadListenObject);
    }

    /**
	 * Stops this server
	 */
    public void Stop() {
        exitListeningThread = true;
        exitRefreshThread = true;
        exitWorkerThread = true;
        synchronized (clientThreadLock) {
            mClients.clear();
            mServerSocket = null;
        }
    }

    /**
	 * Gets the name of this server
	 * @return The name of this server in string form
	 */
    public String getName() {
        return mServerName;
    }

    /**
	 * Gets the socket that this server is listening on
	 * @return The ServerSocket object on which this server is bound
	 */
    public ServerSocket getSocket() {
        return mServerSocket;
    }

    /**
	 * Gets the maximum amount of users allowed to be connected to this server at any given time
	 * @return The integer value describing the maximum number of users on this server
	 */
    public int getMaximumUsers() {
        return mMaxNumOfUsers;
    }

    /**
	 * Gets the list of all current users
	 * @return A list of TCPNetConnection objects of all currently connected users
	 */
    public List<TCPNetConnection> getCurrentUsers() {
        List<TCPNetConnection> currentClientList = null;
        synchronized (clientThreadLock) {
            currentClientList = new LinkedList<TCPNetConnection>(mClients);
        }
        return currentClientList;
    }

    /**
	 *	Gets the current number of users connected to this server
	 * @return The integer value describing the current number of users on this server
	 */
    public int getCurrentNumberOfUsers() {
        return mCurrentNumberOfUsers;
    }

    /**
	 * Adds a listener for client connected events
	 * @param eventListener The listener object
	 */
    public synchronized void AddClientConnectedListener(ConnectionListener eventListener) {
        if (mClientConnectedListeners != null && (eventListener instanceof ClientConnectedListener)) {
            mClientConnectedListeners.add((ClientConnectedListener) eventListener);
        }
    }

    /**
	 * Removes a listener for client connected events
	 * @param listener The listener object
	 */
    public synchronized void RemoveClientConnectedListener(ConnectionListener eventListener) {
        if (mClientConnectedListeners != null && (eventListener instanceof ClientConnectedListener)) {
            mClientConnectedListeners.remove(eventListener);
        }
    }

    /**
	 * Adds a listener for client disconnected events
	 * @param eventListener The listener object
	 */
    public synchronized void AddClientDisconnectedListener(ConnectionListener eventListener) {
        if (mClientDisconnectedListeners != null && (eventListener instanceof ClientConnectedListener)) {
            mClientDisconnectedListeners.add((ClientDisconnectedListener) eventListener);
        }
    }

    /**
	 * Removes a listener for client disconnected events
	 * @param listener The listener object
	 */
    public synchronized void RemoveClientDisconnectedListener(ConnectionListener eventListener) {
        if (mClientDisconnectedListeners != null && (eventListener instanceof ClientDisconnectedListener)) {
            mClientDisconnectedListeners.remove(eventListener);
        }
    }

    /**
	 * Adds a listener for error events
	 * @param listener The listener object
	 */
    public synchronized void AddErrorRaisedListener(ConnectionListener eventListener) {
        if (mErrorRaisedListeners != null && (eventListener instanceof ErrorRaisedListener)) {
            mErrorRaisedListeners.add((ErrorRaisedListener) eventListener);
        }
    }

    /**
	 * Removes a listener for error events
	 * @param listener The listener object
	 */
    public synchronized void RemoveErrorRaisedListener(ConnectionListener eventListener) {
        if (mErrorRaisedListeners != null && (eventListener instanceof ErrorRaisedListener)) {
            mErrorRaisedListeners.remove(eventListener);
        }
    }

    /**
	 * Adds a listener for data received events
	 * @param listener The listener object
	 */
    public synchronized void AddDataReceivedListener(ConnectionListener eventListener) {
        if (mDataReceivedListeners != null && (eventListener instanceof DataReceivedListener)) {
            mDataReceivedListeners.add((DataReceivedListener) eventListener);
        }
    }

    /**
	 * Removes a listener for data received events
	 * @param listener The listener object
	 */
    public synchronized void RemoveDataReceivedListener(ConnectionListener eventListener) {
        if (mDataReceivedListeners != null && (eventListener instanceof DataReceivedListener)) {
            mDataReceivedListeners.remove(eventListener);
        }
    }

    /**
	 * Raises an error event by using the OnErrorRaised event listener functionality
	 * @param source The source object that raised this event
	 * @param connection The {@link TCPNetConnection} that is relevant, or null for a non-connection oriented error
	 * @param errorException The exception that was incurred
	 */
    private void raiseErrorEvent(Object source, TCPNetConnection connection, Exception errorException) {
        ErrorRaisedEvent evt = new ErrorRaisedEvent(source, connection, errorException);
        Iterator<ErrorRaisedListener> listeners = mErrorRaisedListeners.iterator();
        while (listeners.hasNext()) {
            listeners.next().OnErrorRaised(evt);
        }
    }
}
