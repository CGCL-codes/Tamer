package pl.edu.agh.iosr.ftpserverremote.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.apache.ftpserver.ConfigurableFtpServerContext;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.Configuration;
import org.apache.ftpserver.interfaces.FtpServerContext;
import org.apache.ftpserver.interfaces.ServerFtpStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.iosr.ftpserverremote.data.ConnectionData;
import pl.edu.agh.iosr.ftpserverremote.serverdata.ServerDataListener;
import pl.edu.agh.iosr.ftpserverremote.serverdata.ServerSideConnectionManager;
import pl.edu.agh.iosr.ftpserverremote.serverdata.ServerSideFileObserver;
import pl.edu.agh.iosr.ftpserverremote.serverdata.ServerSideLogListener;
import pl.edu.agh.iosr.ftpserverremote.serverdata.ServerSideStatisticsObserver;

/**
 * Implementation of ServerFacade interface.
 * It is currently the only implementation and is used by user (working on web interface) to access FTPServer possibly located on different host. 
 * Facade is accessed through RMI.
 * 
 * @author Agnieszka Janowska
 */
public class ServerSideFacadeImpl extends UnicastRemoteObject implements ServerFacade {

    private final Logger logger = LoggerFactory.getLogger(ServerSideFacadeImpl.class);

    private FtpServer server;

    private Configuration config;

    private ServerDataListener dataListener;

    private ServerSideConnectionManager connectionsManager;

    private ServerSideLogListener logListener;

    private ServerFtpStatistics statistics;

    private FtpServerContext serverContext;

    public ServerSideFacadeImpl(Configuration config) throws RemoteException, Exception {
        super();
        this.config = config;
        serverContext = new ConfigurableFtpServerContext(config);
        server = new FtpServer(serverContext);
    }

    /**
     * It is necessary to create new server and server context objects each time
     * when server should be started. When server starts all observers are created.
     * This method do nothing if server is not stopped
     * @see ServerSideFacadeImpl#isStopped
     * @throws java.rmi.RemoteException
     */
    public void startServer() throws RemoteException {
        if (isServerStopped()) {
            try {
                serverContext = new ConfigurableFtpServerContext(config);
                server = new FtpServer(serverContext);
                server.start();
                createListeners();
            } catch (Exception ex) {
                server = null;
                ex.printStackTrace();
                logger.error("ServerSideFacadeImpl.startServer()", ex);
            }
        }
    }

    /**
     * Stop server and clean up all observers.
     * @throws java.rmi.RemoteException
     */
    public void stopServer() throws RemoteException {
        if (server == null) {
            return;
        }
        server.stop();
        clearObservers();
        server = null;
    }

    public void suspendServer() throws RemoteException {
        if (server == null) {
            return;
        }
        server.suspend();
    }

    public void resumeServer() throws RemoteException {
        if (server == null) {
            return;
        }
        server.resume();
    }

    public boolean isServerStopped() throws RemoteException {
        if (server == null) {
            return true;
        }
        return server.isStopped();
    }

    public boolean isServerSuspended() throws RemoteException {
        if (server == null) {
            return false;
        }
        return server.isSuspended();
    }

    /**
     * Assign remote data listener and create all observers.
     * @param dataListener Remote data listener
     * @throws java.rmi.RemoteException
     */
    public void assignServerDataListener(ServerDataListener dataListener) throws RemoteException {
        this.dataListener = dataListener;
        createListeners();
    }

    public void closeConnection(ConnectionData connectionData) throws RemoteException {
        connectionsManager.disconnect(connectionData);
    }

    /**
     * Create new ConnectionsManager (which runs in background and checks current 
     * connections on server), StatisticsObserver, FileObserver and LogListener.
     * set up dataListener as remote data listener in all created objects.
     */
    private void createListeners() {
        if (this.dataListener != null) {
            if (server != null && !server.isStopped() && !server.isSuspended()) {
                if (connectionsManager != null) {
                    connectionsManager.stopWorking();
                }
                connectionsManager = new ServerSideConnectionManager(server, dataListener);
                connectionsManager.start();
                ServerSideFileObserver fileObserver = new ServerSideFileObserver(server, dataListener);
                statistics = (ServerFtpStatistics) server.getServerContext().getFtpStatistics();
                statistics.setFileObserver(fileObserver);
                ServerSideStatisticsObserver statsObserver = new ServerSideStatisticsObserver(server, dataListener);
                statistics.setObserver(statsObserver);
                logListener = new ServerSideLogListener(dataListener);
            }
        }
    }

    public void spyUser(ConnectionData connectionData) throws RemoteException {
        connectionsManager.spyUser(connectionData);
    }

    public void stopSpyingUser(ConnectionData connectionData) throws RemoteException {
        connectionsManager.stopSpyingUser(connectionData);
    }

    public void removeServerDataListener() throws RemoteException {
        clearObservers();
        this.dataListener = null;
    }

    public void setLoggingLevel(String level) throws RemoteException {
        if (logListener != null) {
            logListener.setLogLevel(level);
        }
    }

    /**
     * Removes all the registered observers or listeners
     */
    private void clearObservers() {
        if (statistics != null) {
            statistics.setFileObserver(null);
            statistics.setObserver(null);
        }
        if (connectionsManager != null) {
            connectionsManager.stopWorking();
            connectionsManager = null;
        }
        if (logListener != null) {
            logListener.stopLogging();
            logListener = null;
        }
    }
}
