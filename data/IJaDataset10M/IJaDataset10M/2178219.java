package org.sourceforge.jemmrpc.server;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.apache.log4j.Logger;
import org.sourceforge.jemmrpc.shared.RPCHandler;
import org.sourceforge.jemmrpc.shared.RPCHandlerListener;

/**
 * ServerThread is used by RPCServer for handling individual client connections.
 *
 * @author Rory Graves
 */
public class ServerThread implements RPCHandlerListener {

    protected static final Logger LOG = Logger.getLogger(ServerThread.class);

    protected final CountDownLatch shutdownLatch = new CountDownLatch(1);

    protected final ClientId clientId;

    protected final RPCServer server;

    protected final RPCHandler rpcHandler;

    protected final Socket socket;

    protected volatile boolean connected;

    /**
     * Creates a server thread to handle a specific client connection.
     *
     * @param fServer The server instance this client belongs to
     * @param fSocket The client socket
     * @param fClientId The server assigned client id;
     * @param fOfferedIfs The interfaces offered to the client by the server
     * @param fThreadPool The threadpool to use for servicing requests
     */
    public ServerThread(RPCServer fServer, Socket fSocket, ClientId fClientId, Map<Class<?>, Object> fOfferedIfs, ExecutorService fThreadPool) {
        this.server = fServer;
        this.socket = fSocket;
        this.clientId = fClientId;
        rpcHandler = new RPCHandler(false, fSocket, fOfferedIfs, fThreadPool, fClientId);
        rpcHandler.setHandlerListener(this);
    }

    /**
     * Start the server thread, initialising the connection and listening to client requests.
     */
    public void start() {
        connected = true;
        server.notifyNewClient(clientId, socket.getInetAddress().toString(), socket.getPort());
        rpcHandler.start();
    }

    /**
     * Notification that the connection has been closed.
     */
    public void connectionTerminated() {
        if (connected) shutdown();
    }

    /**
     * Shutdown the thread by closing its socket connection.
     */
    public void shutdown() {
        if (connected) {
            connected = false;
            rpcHandler.close();
            server.clientDisconnected(clientId);
            shutdownLatch.countDown();
        }
    }

    /**
     * Utility method to allow a thread to wait for this thread to have completely terminated. This
     * method will return immediately if the thread is already stopped, otherwise it will wait until
     * all shutdown actions have completed before returning.
     */
    public void waitForShutdown() {
        try {
            shutdownLatch.await();
        } catch (final InterruptedException e) {
        }
    }

    /**
     * Gets the proxy for the given client interface.
     *
     * @param ifClass The requesting interface implementation.
     * @return A proxy that implements the interface 'ifClass'
     */
    public Object getClientIF(Class<?> ifClass) {
        return rpcHandler.getRemoteIF(ifClass);
    }
}
