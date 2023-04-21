package us.conxio.hl7.hl7stream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import us.conxio.hl7.hl7message.HL7Message;
import us.conxio.hl7.hl7system.HL7Logger;

class HL7ServiceWorker implements Runnable {

    /**
    * A socket which contains the connection request accepted by the server.
    */
    protected Socket clientSocket = null;

    /**
     * The HL7MessageService specification to use in qualifying and routing HL7 transaction messages.
     */
    protected HL7MessageHandler msgHandler = null;

    /**
     * The HL7MessageStream to use for incoming HL7 transaction messages.
     * The socket passed to the HL7MessageServiceWorker is bound to this stream.
     */
    protected HL7MLLPStream inboundStream = null;

    /**
     * A flag to control use of SSL for network i/o.
     */
    private static Logger logger = HL7Logger.getHL7Logger();

    private int msgCount = 0;

    private String currentMsgID = null;

    private String threadID = null;

    /**
     * Creates an instance of the HL7MessageServiceWorker class.
     * @param clientSocket A socket containing the connection request accepted by the server.
     * @param svc The HL7MessageService which runs the server.
     * @param useSSL A flag to control use of SSL for network i/o.
     */
    public HL7ServiceWorker(Socket clientSocket, HL7MessageHandler svc) {
        this.clientSocket = clientSocket;
        this.msgHandler = svc;
    }

    /**
     * The run method for the thread pooled services under a HL7MessageServer.
     * <ul>This method
     * <li> assumes the connection which was accepted by the server, and passed in via the constructor.
     * <li> binds it to a stream.
     * <li> receives and acknowledges HL7 transaction messages.
     * <li> qualifies and routes them in accordance with the HL7MessageService passed to the constructor.
     * </ul>
     */
    public void run() {
        synchronized (this) {
            long threadIDNumber = Thread.currentThread().getId();
            threadID = Long.toHexString(threadIDNumber);
        }
        if (clientSocket == null) {
            logger.error("null client socket.");
            return;
        }
        try {
            inboundStream = new HL7MLLPStream(clientSocket, true);
            HL7Message inboundMsg = null;
            while ((inboundMsg = this.inboundStream.read()) != null) {
                currentMsgID = inboundMsg.idString();
                if (msgHandler.dispatch(inboundMsg) > 0) {
                    ++msgCount;
                }
            }
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }
}

/**
 * A thread pooled HL7 Server.
 * Hacked off of Jakob Jenkov's <a href="http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html"> thread pooled server example.
 * @author scott herman <scott.herman@unconxio.us>
 */
public class HL7Server implements Runnable, HL7Stream {

    /**
    * The port at which the server will listen for connection requests.
    */
    protected int serverPort;

    /**
    * The number of threads for the server to maintain in it's pool of available running threads.
    */
    protected int poolSize;

    /**
    * A flag to indicate the run status of the server.
    */
    protected boolean isStopped;

    /**
    * A reference to the currently running thread.
    */
    protected Thread runningThread;

    /**
    * A reference to the pool of available threads.
    */
    protected ExecutorService threadPool;

    /**
    * The class that handles the message.
    */
    protected HL7MessageHandler msgHandler;

    /**
    * The socket which the server uses to accept connection requests.
    */
    protected ServerSocket serverSock;

    private static Logger logger = HL7Logger.getHL7Logger();

    private int status;

    private HL7Server() {
    }

    /**
    * Create an instance of the thread pooled server, assuming that the host is localhost.
    * @param port The port no. for the server to listen on for connection requests.
    * @param poolSizeArg The desired thread pool size
    */
    public HL7Server(int port, int poolSizeArg) {
        this();
        serverPort = port;
        poolSize = (poolSizeArg < 2) ? 2 : poolSizeArg;
        threadPool = Executors.newFixedThreadPool(poolSize);
    }

    /**
    * Create a thread pooled server, with localhost as host, registering the argument handler.
    * @param port The port no. for the server to listen on for connection requests.
    * @param poolSize The desired thread pool size
    * @param handler The dispatch handler for incoming HL7Message objects.
    */
    public HL7Server(int port, int poolSize, HL7MessageHandler handler) {
        this(port, poolSize);
        msgHandler = handler;
    }

    /**
    * Create a HL7 server from the argument URI.
    * @param uri the argument URI.
    */
    public HL7Server(URI uri) {
        this();
        HL7StreamURI streamURI = new HL7StreamURI(uri);
        if (!streamURI.isServerURI()) {
            throw new IllegalArgumentException("HL7Server(" + uri.toString() + "):Not a valid HL7 server URI.");
        }
        serverPort = streamURI.getPortNo();
        int uriPoolSize = streamURI.uriServerPoolSize();
        poolSize = (uriPoolSize < 2) ? 2 : uriPoolSize;
        threadPool = Executors.newFixedThreadPool(poolSize);
    }

    /**
    * Set the message dispatch handler to the argument object.
    * @param handler
    */
    public void setHandler(HL7MessageHandler handler) {
        msgHandler = handler;
    }

    /**
    * The required run method for the server thread, which accepts connection requests, and hands them off
    * to HL7ServiceWorker instances in the thread pool.
    */
    public void run() {
        synchronized (this) {
            runningThread = Thread.currentThread();
        }
        String detailedIDString = detailedIDString() + ".run:";
        logger.info(detailedIDString + " Started.");
        while (!isStopped) {
            if (serverSock == null || serverSock.isClosed()) {
                openServerSocket();
            }
            status = HL7Stream.OPEN;
            Socket clientSocket = null;
            try {
                clientSocket = serverSock.accept();
            } catch (IOException ioEx) {
                if (isStopped()) {
                    logger.info(detailedIDString + " Stopped Listening.");
                    return;
                }
                if (!serverSock.isClosed()) {
                    throw new RuntimeException("Error accepting client connection", ioEx);
                }
            }
            if (clientSocket != null) {
                threadPool.execute(new HL7ServiceWorker(clientSocket, msgHandler));
            }
        }
        threadPool.shutdown();
        logger.info(detailedIDString + " Server Stopped.");
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    private void openServerSocket() {
        try {
            serverSock = new ServerSocket(serverPort);
        } catch (IOException ioEx) {
            throw new RuntimeException("Cannot open port:" + serverPort, ioEx);
        }
    }

    /**
     * A method for stopping the server.
     */
    public synchronized void stop() {
        isStopped = true;
        try {
            logger.info("Closing serverSock.");
            serverSock.close();
            status = HL7Stream.CLOSED;
        } catch (IOException ioEx) {
            throw new RuntimeException("Error closing server", ioEx);
        }
    }

    /**
     * Opens the server for operation, by starting the service thread.
     * @return true if the operation succeeded, otherwise false.
     * @throws us.conxio.HL7.HL7Stream.HL7IOException
     */
    public boolean open() throws HL7IOException {
        new Thread(this).start();
        while (true) {
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException ex) {
                HL7Server.logger.debug("InterruptedException caught.", ex);
            }
        }
    }

    private String statusString() {
        switch(status) {
            case HL7Stream.UNINITIALIZED:
                return "UNINITIALIZED";
            case HL7Stream.CLOSED:
                return "CLOSED";
            case HL7Stream.OPEN:
                return "OPEN";
            default:
                return "UNEXPECTED_STATUS";
        }
    }

    /**
     * Creates a status string for the context server instantiation.
     * @return the created string.
     */
    public String description() {
        return "HL7Server(port:" + serverPort + ", poolSize:" + poolSize + ", handler:" + msgHandler.getClass().getSimpleName() + "):" + statusString();
    }

    /**
     * Read access to the servers status value.
     * @return the status value as an integer.
     */
    public int status() {
        return status;
    }

    /**
    * Determines whether the context server is closed.
    * @return true if the context server is closed, otherwsie false;
    */
    public boolean isClosed() {
        return (status == HL7Stream.CLOSED);
    }

    /**
    * Determines whether the context server is open.
    * @return true if the context server is open, otherwsie false;
    */
    public boolean isOpen() {
        return (status == HL7Stream.OPEN);
    }

    /**
    * Disallowed operation.
    * @param msg
    * @return Always throws a Inappropriate operation HL7IOException.
    * @throws us.conxio.HL7.HL7Stream.HL7IOException
    */
    public boolean write(HL7Message msg) throws HL7IOException {
        throw new HL7IOException("HL7Server.write:Inappropriate operation.", HL7IOException.INAPPROPRIATE_OPERATION);
    }

    /**
    * Disallowed operation.
    * @return Always throws a Inappropriate operation HL7IOException.
    * @throws us.conxio.HL7.HL7Stream.HL7IOException
    */
    public HL7Message read() throws HL7IOException {
        throw new HL7IOException("HL7Server.read:Inappropriate operation.", HL7IOException.INAPPROPRIATE_OPERATION);
    }

    /**
    * Stops and closes the server.
    * @return true if the operation succeeded, otherwise false.
    * @throws us.conxio.HL7.HL7Stream.HL7IOException
    */
    public boolean close() throws HL7IOException {
        stop();
        return true;
    }

    /**
    * Exposes the server object.
    * @return the server object.
    */
    public HL7Server server() {
        return this;
    }

    /**
    * Determine whether the context stream is a server.
    * @return true (from here).
    */
    public boolean isServer() {
        return true;
    }

    public HL7MessageHandler dispatchHandler() {
        return null;
    }

    private String detailedIDString() {
        return new StringBuilder(getClass().getSimpleName()).append("(port:").append(Integer.toString(serverPort)).append(", poolSize:").append(Integer.toString(poolSize)).append(", handler:").append(msgHandler.getClass().getSimpleName()).append(")").toString();
    }
}
