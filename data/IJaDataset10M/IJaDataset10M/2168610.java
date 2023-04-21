package se.sics.isl.inet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.sics.isl.util.ArrayQueue;
import se.sics.isl.util.ThreadPool;
import se.sics.isl.util.JobStatus;

/**
 */
public abstract class InetConnection {

    private static final Logger log = Logger.getLogger(InetConnection.class.getName());

    private static final Object CLOSE_MESSAGE = new Object();

    private String name;

    private String fullName;

    private String userName;

    private long connectTime;

    private Socket socket;

    private InputStream input;

    private OutputStream output;

    private String remoteHost;

    private int remotePort;

    private final boolean isServerConnection;

    private boolean isDeliveryBuffered = false;

    private boolean delivererRunning = false;

    private boolean isWriteBuffered = false;

    private boolean writerRunning = false;

    private boolean isOpen = false;

    private boolean isClosed = true;

    private ArrayQueue inBuffer;

    private ArrayQueue outBuffer;

    private ThreadPool threadPool;

    private MessageWriter messageWriter;

    private MessageDeliverer messageDeliverer;

    private MessageReader messageReader;

    public InetConnection(String name, Socket socket) {
        this.isServerConnection = true;
        this.name = name;
        this.fullName = name;
        this.socket = socket;
        this.connectTime = System.currentTimeMillis();
    }

    public InetConnection(String name, String host, int port) {
        this.isServerConnection = false;
        this.name = name;
        this.fullName = name;
        this.remoteHost = host;
        this.remotePort = port;
        this.connectTime = System.currentTimeMillis();
    }

    public String getName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if (userName == null) {
            throw new NullPointerException();
        }
        this.fullName = userName + '@' + this.name;
        this.userName = userName;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public InputStream getInputStream() {
        return input;
    }

    public OutputStream getOutputStream() {
        return output;
    }

    public boolean isServerConnection() {
        return isServerConnection;
    }

    public boolean isDeliveryBuffered() {
        return isDeliveryBuffered;
    }

    public void setDeliveryBuffered(boolean isDeliveryBuffered) {
        if (isDeliveryBuffered && inBuffer == null) {
            inBuffer = new ArrayQueue();
        }
        this.isDeliveryBuffered = isDeliveryBuffered;
    }

    public boolean isWriteBuffered() {
        return isWriteBuffered;
    }

    public void setWriteBuffered(boolean isWriteBuffered) {
        if (isWriteBuffered && outBuffer == null) {
            outBuffer = new ArrayQueue();
        }
        this.isWriteBuffered = isWriteBuffered;
    }

    public ThreadPool getThreadPool() {
        ThreadPool pool = this.threadPool;
        if (pool == null) {
            pool = this.threadPool = ThreadPool.getDefaultThreadPool();
        }
        return pool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public final void start() throws IOException {
        if (input != null) {
            return;
        }
        if (socket != null) {
            InetAddress remoteAddress = socket.getInetAddress();
            this.remoteHost = remoteAddress.getHostAddress();
            this.remotePort = socket.getPort();
        } else {
            this.socket = new Socket(remoteHost, remotePort);
        }
        this.input = socket.getInputStream();
        this.output = socket.getOutputStream();
        isClosed = false;
        isOpen = true;
        messageReader = new MessageReader(name, this);
        connectionOpened();
        messageReader.start();
    }

    public boolean isClosed() {
        return !isOpen;
    }

    public void close() {
        if (isOpen) {
            isOpen = false;
            sendMessage(CLOSE_MESSAGE);
        }
    }

    public void closeImmediately() {
        closeImmediately(true);
    }

    private void closeImmediately(boolean useThread) {
        if (!isClosed) {
            isOpen = false;
            isClosed = true;
            if (useThread) {
                getThreadPool().invokeLater(new ConnectionCloser(this));
            } else {
                doClose();
            }
        }
    }

    private void doClose() {
        log.finest(fullName + ": connection closed from " + remoteHost);
        messageReader.interrupt();
        try {
            connectionClosed();
        } catch (Exception e) {
            log.log(Level.WARNING, fullName + ": failed to close connection", e);
        }
        try {
            input.close();
            output.close();
            socket.close();
        } catch (Exception e) {
        }
    }

    public void sendMessage(Object message) {
        if (isWriteBuffered) {
            synchronized (outBuffer) {
                outBuffer.add(message);
                if (!writerRunning) {
                    if (messageWriter == null) {
                        messageWriter = new MessageWriter(this);
                    }
                    writerRunning = true;
                    getThreadPool().invokeLater(messageWriter);
                } else {
                    outBuffer.notify();
                }
            }
        } else if (message == CLOSE_MESSAGE) {
            closeImmediately();
        } else {
            try {
                doSendMessage(message);
            } catch (Throwable e) {
                log.log(Level.SEVERE, fullName + ": could not send " + message, e);
                closeImmediately();
                if (e instanceof ThreadDeath) {
                    throw (ThreadDeath) e;
                }
            }
        }
    }

    protected void deliverMessage(Object message) {
        if (isDeliveryBuffered) {
            synchronized (inBuffer) {
                inBuffer.add(message);
                if (!delivererRunning) {
                    if (messageDeliverer == null) {
                        messageDeliverer = new MessageDeliverer(this);
                    }
                    delivererRunning = true;
                    getThreadPool().invokeLater(messageDeliverer);
                } else {
                    inBuffer.notify();
                }
            }
        } else {
            doDeliverMessage(message);
        }
    }

    protected abstract void connectionOpened() throws IOException;

    protected abstract void connectionClosed() throws IOException;

    protected abstract void doReadMessages() throws IOException;

    protected abstract void doDeliverMessage(Object message);

    protected abstract void doSendMessage(Object message) throws IOException;

    private static class MessageWriter implements Runnable {

        private final InetConnection connection;

        MessageWriter(InetConnection connection) {
            this.connection = connection;
        }

        public void run() {
            Object message = null;
            boolean ok = false;
            JobStatus jobStatus = ThreadPool.getJobStatus();
            ArrayQueue outBuffer = connection.outBuffer;
            try {
                while (!connection.isClosed) {
                    synchronized (outBuffer) {
                        if (outBuffer.isEmpty()) {
                            try {
                                outBuffer.wait(800);
                            } catch (Exception e) {
                            }
                        }
                        if (!outBuffer.isEmpty()) {
                            message = outBuffer.remove(0);
                        } else {
                            connection.writerRunning = false;
                            ok = true;
                            return;
                        }
                    }
                    if (message == CLOSE_MESSAGE) {
                        connection.closeImmediately(false);
                        break;
                    }
                    if (jobStatus != null) {
                        jobStatus.stillAlive();
                    }
                    connection.doSendMessage(message);
                }
                ok = true;
            } catch (Throwable e) {
                log.log(Level.SEVERE, connection.fullName + ": could not send " + message, e);
                connection.closeImmediately(false);
                if (e instanceof ThreadDeath) {
                    throw (ThreadDeath) e;
                }
            } finally {
                if (!ok) {
                    synchronized (connection.outBuffer) {
                        if (!outBuffer.isEmpty() && !connection.isClosed) {
                            log.warning("reinvoking writer for " + connection.fullName);
                            connection.getThreadPool().invokeLater(this);
                        } else {
                            log.warning("writer for " + connection.fullName + " exiting");
                            connection.writerRunning = false;
                        }
                    }
                }
            }
        }

        public String toString() {
            return "MessageWriter[" + connection.fullName + ',' + connection.outBuffer.size() + ',' + connection.remoteHost + ']';
        }
    }

    private static class MessageReader extends Thread {

        private final InetConnection connection;

        MessageReader(String name, InetConnection connection) {
            super(name);
            this.connection = connection;
        }

        public void run() {
            try {
                connection.doReadMessages();
            } catch (Throwable e) {
                if (connection.isOpen) {
                    log.log(Level.SEVERE, connection.fullName + ": message reader error", e);
                }
            } finally {
                if (connection.isOpen) {
                    log.warning(connection.fullName + ": connection closed");
                    connection.closeImmediately(false);
                }
            }
        }
    }

    private static class MessageDeliverer implements Runnable {

        private final InetConnection connection;

        public MessageDeliverer(InetConnection connection) {
            this.connection = connection;
        }

        public void run() {
            Object message = null;
            boolean ok = false;
            JobStatus jobStatus = ThreadPool.getJobStatus();
            ArrayQueue inBuffer = connection.inBuffer;
            try {
                while (true) {
                    synchronized (inBuffer) {
                        if (inBuffer.isEmpty()) {
                            try {
                                inBuffer.wait(800);
                            } catch (Exception e) {
                            }
                        }
                        if (!inBuffer.isEmpty()) {
                            message = inBuffer.remove(0);
                        } else {
                            connection.delivererRunning = false;
                            ok = true;
                            return;
                        }
                    }
                    if (jobStatus != null) {
                        jobStatus.stillAlive();
                    }
                    connection.doDeliverMessage(message);
                }
            } catch (Throwable e) {
                log.log(Level.SEVERE, connection.fullName + ": could not deliver " + message, e);
            } finally {
                if (!ok) {
                    synchronized (inBuffer) {
                        if (!inBuffer.isEmpty()) {
                            log.warning("reinvoking deliverer for " + connection.fullName);
                            connection.getThreadPool().invokeLater(this);
                        } else {
                            log.warning("deliverer for " + connection.fullName + " exiting");
                            connection.delivererRunning = false;
                        }
                    }
                }
            }
        }

        public String toString() {
            return "MessageDeliverer[" + connection.fullName + ',' + connection.inBuffer.size() + ',' + connection.remoteHost + ']';
        }
    }

    private static class ConnectionCloser implements Runnable {

        private final InetConnection connection;

        public ConnectionCloser(InetConnection connection) {
            this.connection = connection;
        }

        public void run() {
            connection.doClose();
        }

        public String toString() {
            return "ConnectionCloser[" + connection.fullName + ',' + connection.remoteHost + ']';
        }
    }
}
