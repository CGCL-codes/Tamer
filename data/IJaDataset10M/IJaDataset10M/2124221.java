package com.kenmccrary.jtella;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import com.kenmccrary.jtella.util.Log;
import com.dan.jtella.GetHostsFromCache;
import com.dan.jtella.ConnectedHostsListener;

/**
 * EDITED BY: Daniel Meyers, 2003<br>
 * The GNUTellaConnection represents a connection to the GNUTella 
 * <b>network</b>. The connection consists of one or more socket 
 * connections to servant nodes on the network.<p>
 *
 */
public class GNUTellaConnection {

    public static final String LOGGER = "protocol.com.dan.jtella";

    public static Logger LOG = Logger.getLogger(LOGGER);

    private static ConnectionData connectionData;

    private HostCache hostCache;

    private ConnectionList connectionList;

    private Router router;

    private IncomingConnectionManager incomingConnectionManager;

    private OutgoingConnectionManager outgoingConnectionManager;

    private GetHostsFromCache getHostsFromCache;

    private KeepAliveThread keepAliveThread;

    /**
	 * Constructs an empty connection, the application must add a host cache or
	 * servant to generate activity
	 */
    public GNUTellaConnection() throws IOException {
        this(null);
    }

    /**
	 * EDITED BY: Daniel Meyers, 2003
	 * <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * Construct the connection specifying connection data. The connection will
	 * not have access to a host cache unless specified later.
	 *
	 * @param connData connection data
	 **/
    public GNUTellaConnection(ConnectionData connData) throws IOException {
        LOG.info("Network connection initializing");
        LOG.info(System.getProperties().toString());
        if (null != connData) {
            connectionData = connData;
        } else {
            connectionData = new ConnectionData();
        }
        hostCache = new HostCache();
        connectionList = new ConnectionList();
        router = initializeRouter();
        getHostsFromCache = new GetHostsFromCache(hostCache, connectionList, connData);
        incomingConnectionManager = new IncomingConnectionManager(router, connectionList, connectionData, hostCache);
        outgoingConnectionManager = new OutgoingConnectionManager(router, hostCache, connectionList, connectionData);
        keepAliveThread = new KeepAliveThread(connectionList);
    }

    /**
	 * Generates and returns the router thread for the Gnutella connection.
	 * @return The router thread for the Gnutella connection.
	 */
    protected Router initializeRouter() {
        return new Router(connectionList, connectionData, hostCache);
    }

    /**
	 * Fetch the router used by this Gnutella connection.
	 * @return the router used by this Gnutella connection
	 */
    public Router getRouter() {
        return router;
    }

    /** 
	 * EDITED BY: Daniel Meyers, 2003
	 * <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * Starts the connection
	 */
    public void start() {
        try {
            router.start();
            getHostsFromCache.start();
            incomingConnectionManager.start();
            outgoingConnectionManager.start();
            keepAliveThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * EDITED BY: Daniel Meyers, 2003
	 * <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * Stop the connection, after execution the <code>GNUTellaConnection</code>
	 * is unusable. A new connection must be created if needed. If a 
	 * temporary disconnect from NodeConnections is desired, the connection count
	 * requests can be set to 0
	 *
	 */
    public void stop() {
        keepAliveThread.shutdown();
        getHostsFromCache.shutdown();
        incomingConnectionManager.shutdown();
        outgoingConnectionManager.shutdown();
        connectionList.reduceActiveIncomingConnections(0);
        connectionList.reduceActiveOutgoingConnections(0);
        connectionList.stopOutgoingConnectionAttempts();
        router.shutdown();
    }

    /**
	 * Get the current <code>HostCache</code>. Using the <code>HostCache</code>
	 * an application can query the known hosts, and add and remove hosts
	 *
	 * @return host cache
	 */
    public HostCache getHostCache() {
        return hostCache;
    }

    /**
	 * Query if we are online with the network, with at least one active
	 * node connection
	 *
	 * @return true if online, false otherwise
	 */
    public boolean isOnline() {
        if (null == connectionList) {
            return false;
        }
        return !connectionList.getActiveIncomingConnections().isEmpty() || !connectionList.getActiveOutgoingConnections().isEmpty();
    }

    /**
	 * Get the <code>ConnectionData</code> settings
	 *
	 * @return connection data
	 */
    public ConnectionData getConnectionData() {
        return connectionData;
    }

    /** 
	 * Creates a session to conduct network searches
	 *
	 * @param query search query
	 * @param maxResults maximum result set size
	 * @param minSpeed minimum speed for responding servants
	 * @param receiver receiver for search responses
	 * @return session
	 */
    public SearchSession createSearchSession(String query, int queryType, int maxResults, int minSpeed, MessageReceiver receiver) {
        return new SearchSession(query, queryType, maxResults, minSpeed, this, router, receiver);
    }

    /**
	 * Get a search monitor session to monitor query requests
	 * flowing through this network connection. 
	 * 
	 * @param searchReceiver message receiver
	 */
    public SearchMonitorSession getSearchMonitorSession(MessageReceiver searchReceiver) {
        return new SearchMonitorSession(router, searchReceiver);
    }

    /**
	 * Creates a file serving session. <code>FileServerSession</code> can respond
	 * with a query hit
	 *
	 * @param receiver message receiver
	 */
    public FileServerSession createFileServerSession(MessageReceiver receiver) {
        return new FileServerSession(router, receiver);
    }

    /**
	 * Adds a listener to the connectionList
	 *
	 */
    public void addListener(ConnectedHostsListener chl) {
        outgoingConnectionManager.addListener(chl);
        incomingConnectionManager.addListener(chl);
    }

    /**
	 * Gets the current list of connections to GNUTella
	 *
	 * @return list of connections
	 */
    public List<NodeConnection> getConnectionList() {
        return connectionList.getList();
    }

    /**
	 * Get the connection list
	 */
    protected ConnectionList getConnections() {
        return connectionList;
    }

    /**
	 * Cleans dead connections from the connection list
	 */
    public void cleanDeadConnections() {
        connectionList.cleanDeadConnections(Connection.CONNECTION_OUTGOING);
    }

    /**
	 * Attempts an outgoing connection on the specified host
	 *
	 * @param ipAddress host IP
	 * @param port port number
	 */
    public void addConnection(String ipAddress, int port) {
        outgoingConnectionManager.addImmediateConnection(ipAddress, port);
    }

    /**
	 * Get the servant identifier the <code>GnutellaConnection</code> 
	 * is using. The servant identifier is used in connection with Push
	 * message processing
	 *
	 * @return servant identifier
	 */
    public short[] getServantIdentifier() {
        return Utilities.getClientIdentifier();
    }

    /**
	 * Sends a message to all connections
	 *
	 * @param m message to broadcast
	 * @param receiver message receiver
	 */
    void broadcast(Message m, MessageReceiver receiver) {
        List<NodeConnection> connections = connectionList.getActiveConnections();
        Log.getLog().logDebug("Broadcasting message, type: " + m.getType() + ", to " + connections.size() + " connections");
        ListIterator<NodeConnection> i = connections.listIterator();
        while (i.hasNext()) {
            NodeConnection connection = (NodeConnection) i.next();
            try {
                connection.sendAndReceive(m, receiver);
            } catch (IOException io) {
                Log.getLog().log(io);
            }
        }
    }
}
