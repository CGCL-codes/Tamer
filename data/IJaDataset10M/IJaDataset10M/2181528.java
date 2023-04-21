package pl.edu.agh.iosr.ftpserverremote.serverdata;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.listener.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.iosr.ftpserverremote.data.ConnectionData;

/**
 * Class gather informations from current FtpServerContext about
 * connected users, packs this informations into ConnectionData objects and sends
 * to data listener object. It is Thread, because standard ConnectionObserver
 * doesn't work properly. It checks connections list, sends informations and after
 * that  object is waiting particular amount of time {@see NOTIFICATION_TIME}
 * @author Tomasz Jadczyk
 */
public class ServerSideConnectionManager extends Thread {

    /**
     * Amount of time between checks connections.
     */
    private static final int NOTIFICATION_TIME = 2000;

    /**
     * Max remote communication errors sequence length after which
     * running this thread will be broken.
     */
    private static final int MAX_ERROR_COUNTER = 3;

    private static Long idCounter = new Long(0);

    private ServerDataListener dataListener;

    private Map<Connection, ConnectionData> map;

    private Map<Long, Connection> connections;

    private List<Connection> observedConnections;

    private final Logger logger = LoggerFactory.getLogger(ServerSideConnectionManager.class);

    private FtpServer server;

    private int errorCounter = 0;

    private boolean isWorking = true;

    public ServerSideConnectionManager(FtpServer server, ServerDataListener dataListener) {
        map = new HashMap<Connection, ConnectionData>();
        connections = new HashMap<Long, Connection>();
        observedConnections = new LinkedList<Connection>();
        this.dataListener = dataListener;
        this.server = server;
    }

    /**
     * Thread is working permanently, until stopWorking() method will be called.
     * It fetched all connections from connection manager defined in server 
     * context, packed all in ConnectionData objects and try to send it to remote
     * data listener. If communication fails, errorCounter will be increment,
     * if it reached MAX_ERROR_COUNTER running of this thread will be stopped.
     */
    @Override
    public void run() {
        while (this.isWorking) {
            List<ConnectionData> dataList = null;
            try {
                List connectionsData = server.getServerContext().getConnectionManager().getAllConnections();
                dataList = new LinkedList<ConnectionData>();
                for (int i = 0; i < connectionsData.size(); i++) {
                    Connection con = (Connection) connectionsData.get(i);
                    ConnectionData data = createConnectionData(con);
                    map.put(con, data);
                    connections.put(data.getId(), con);
                    dataList.add(data);
                }
            } catch (Exception e) {
                logger.error("ServerSideConnectionManager - creating connections list", e);
            }
            if (dataList != null) {
                try {
                    dataListener.setAllConections(dataList);
                    errorCounter = 0;
                } catch (RemoteException e) {
                    logger.error("ServerSideConnectionManager - sending to DataListener", e);
                    errorCounter++;
                    if (errorCounter > MAX_ERROR_COUNTER) {
                        this.isWorking = false;
                    }
                }
            }
            try {
                Thread.sleep(NOTIFICATION_TIME);
            } catch (InterruptedException ex) {
                logger.error("ServerSideConnectionManager interrupted.", ex);
            }
        }
    }

    /**
     * Close connection described in passed parameter
     * @param connectionData Connection to close.
     */
    public synchronized void disconnect(ConnectionData connectionData) {
        Connection con = getConnection(connectionData);
        server.getServerContext().getConnectionManager().closeConnection(con);
    }

    /**
     * Create new observer for passed connection if it hasn't assigned yet any
     * observer.
     * @param connectionData Connection description to observe
     */
    public synchronized void spyUser(ConnectionData connectionData) {
        Connection con = getConnection(connectionData);
        if (!observedConnections.contains(con)) {
            ServerSideConnectionObserver observer = new ServerSideConnectionObserver(con, dataListener, this, connectionData.getId());
            con.setObserver(observer);
        }
    }

    /**
     * Remove observer from passed connection
     * @param connectionData Connection description to stop spying
     */
    public synchronized void stopSpyingUser(ConnectionData connectionData) {
        Connection con = getConnection(connectionData);
        if (con != null) {
            con.setObserver(null);
        }
    }

    /**
     * Turn off current workng thread
     */
    public synchronized void stopWorking() {
        this.isWorking = false;
    }

    /**
     * Pack Connection object into ConnectionData object. If it is new connection - 
     * new ConnectionData object is created, for other connections - ConnectionData
     * object is fetched from map. Sometimes Connection are not completely and
     * created ConnectionData has no full description.
     * @param connection Connection object to pack
     * @return ConnectionData
     */
    private synchronized ConnectionData createConnectionData(Connection connection) {
        ConnectionData connectionData = null;
        if (map.containsKey(connection)) {
            connectionData = map.get(connection);
        } else {
            connectionData = new ConnectionData();
            connectionData.setId(++idCounter);
        }
        try {
            FtpSession session = connection.getSession();
            connectionData.setIp(session.getClientAddress().getHostAddress());
            connectionData.setLastAccessTime(session.getLastAccessTime());
            connectionData.setLoginTime(session.getLoginTime());
            connectionData.setUserName(session.getUser().getName());
        } catch (Exception e) {
            logger.info("Cannot get full description from connection", e);
        }
        return connectionData;
    }

    /**
     * Return Connection object for passed ConnectionData, it is useful because
     * WebGui operates on ConnectionData objects and FtpServer operates on 
     * Connection objects
     * @param connectionData Object to map onto Connection
     * @return Connection for passed object.
     */
    private synchronized Connection getConnection(ConnectionData connectionData) {
        return connections.get(connectionData.getId());
    }
}
