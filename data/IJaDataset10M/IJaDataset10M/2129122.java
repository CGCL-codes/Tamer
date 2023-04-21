package jimo.spi.im.msn.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import jimo.spi.im.api.exception.IllegalStateException;
import jimo.spi.im.api.net.Connection;
import jimo.spi.im.api.net.ProxyInfo;

/**
 * A server that connects to MSN and allows sending and receiving commands.
 */
abstract class MsnServer extends Thread {

    /**
	 * Connection used by this server.
	 */
    private Connection conn;

    /**
	 * The transaction ID to be used for next outgoing command.
	 */
    private Integer nextTransactionID;

    /**
	 * Mapping between an incoming command's transaction ID and the
	 * method to invoke, in case of asynchronous commands.
	 */
    private Hashtable callbackMap;

    /**
	 * The transaction ID used for last synchronous command.
	 */
    private Integer syncCallID;

    /**
	 * The reply received for a synchronous command.
	 */
    private AbstractCommand syncCallResult;

    /**
	 * Buffer that holds all outgoing commands.
	 */
    private Vector writeBuffer;

    /**
	 * Buffer that holds all incoming commands.
	 */
    private Vector readBuffer;

    /**
	 * Thread that reads from conn and fills readBuffer.
	 */
    private ReaderThread reader;

    /**
	 * Thread that writes commands stored in writeBuffer to conn.
	 */
    private WriterThread writer;

    /**
	 * Constructs a MsnServer by connecting to a host at the specified
	 * port using given information of the proxy server to be used.
	 *
	 * @param host the hostname of the server to connect to.
	 * @param port the TCP/IP port number to connect to.
	 * @param info the proxy server information.
	 * @throws UnknownHostException if host is not known.
	 * @throws IOException if an I/O error occurs while connecting to the host.
	 * @throws IllegalStateException if info is not initialized properly.
	 */
    protected MsnServer(String host, int port, ProxyInfo info) throws UnknownHostException, IOException, IllegalStateException {
        conn = info.getConnection(host, port);
        nextTransactionID = new Integer(1);
        callbackMap = new Hashtable();
        syncCallID = new Integer(0);
        readBuffer = new Vector();
        writeBuffer = new Vector();
        reader = new ReaderThread(this, conn, readBuffer);
        writer = new WriterThread(this, conn, writeBuffer);
        reader.setDaemon(true);
        writer.setDaemon(true);
        this.setDaemon(true);
        reader.start();
        writer.start();
        this.start();
    }

    /**
	 * Sends this command to server by writing it to the write buffer. This method
	 * does not wait for the reply from server. On the other hand, the method specified
	 * will be invoked when a reply arrives.
	 *
	 * @param cmd the command to be sent.
	 * @param method the name of the method that processes the reply for the command.
	 */
    protected synchronized void sendToServer(AbstractCommand cmd, String method) {
        cmd.setTransactionID(nextTransactionID);
        callbackMap.put(nextTransactionID, method);
        int tmp = nextTransactionID.intValue();
        tmp = tmp == 2147483647 ? 1 : tmp + 1;
        nextTransactionID = new Integer(tmp);
        synchronized (writeBuffer) {
            writeBuffer.add(cmd);
            writeBuffer.notify();
        }
    }

    /**
	 * Sends this command to server by writing it to the write buffer. This method
	 * waits till a reply is received from server.
	 *
	 * @param cmd the command to be sent.
	 * @return the reply received from the server, or null if the wait
	 *         was interrupted.
	 */
    protected synchronized AbstractCommand sendToServer(Command cmd) {
        cmd.setTransactionID(nextTransactionID);
        syncCallID = new Integer(nextTransactionID.intValue());
        int tmp = nextTransactionID.intValue();
        tmp = tmp == 2147483647 ? 1 : tmp + 1;
        nextTransactionID = new Integer(tmp);
        synchronized (writeBuffer) {
            writeBuffer.add(cmd);
            writeBuffer.notify();
        }
        synchronized (syncCallID) {
            try {
                syncCallID.wait();
            } catch (InterruptedException e) {
                return null;
            }
        }
        return syncCallResult;
    }

    /**
	 * Shutdown this server by closing all connections.
	 */
    protected void shutdown() {
        reader.stopThread();
        writer.stopThread();
        this.interrupt();
    }

    /**
	 * Processes messages received from MSN server.
	 */
    protected abstract void processMessage(MsnMessage msg);

    /**
	 * Process asynchronous commands received from MSN server.
	 */
    protected abstract void processAsyncCommand(AbstractCommand cmd);

    /**
	 * Invoked when the reader thread associtated with this server exits
	 * abnormally.
	 */
    protected abstract void readerExited();

    /**
	 * Invoked when the writer thread associtated with this server exits
	 * abnormally.
	 */
    protected abstract void writerExited();

    /**
	 * Runs the thread that polls for incoming commands.
	 */
    public void run() {
        GregorianCalendar prevPingTime = new GregorianCalendar();
        while (true) {
            if (this instanceof NotificationServer) pingIfRequired(prevPingTime);
            AbstractCommand[] commands = null;
            synchronized (readBuffer) {
                try {
                    readBuffer.wait(2000);
                    int size = readBuffer.size();
                    if (size <= 0) continue; else {
                        commands = (AbstractCommand[]) readBuffer.toArray(new AbstractCommand[0]);
                        readBuffer.removeAllElements();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            for (int i = 0; i < commands.length; i++) {
                AbstractCommand cmd = commands[i];
                Integer transactionID = cmd.getTransactionID();
                if (cmd instanceof MsnMessage) {
                    processMessage((MsnMessage) cmd);
                } else if (transactionID != null) {
                    synchronized (syncCallID) {
                        if (transactionID.equals(syncCallID)) {
                            syncCallResult = cmd;
                            syncCallID.notify();
                            syncCallID = new Integer(0);
                        }
                    }
                    String methodName = (String) callbackMap.get(transactionID);
                    if (methodName != null) invokeMethod(methodName, cmd);
                } else processAsyncCommand(cmd);
            }
        }
    }

    /**
	 * This method pings MSN server every two minutes. 
	 *
	 * @param prevPingTime the last time when ping command was sent
	 */
    private void pingIfRequired(GregorianCalendar prevPingTime) {
        GregorianCalendar now = new GregorianCalendar();
        prevPingTime.add(GregorianCalendar.SECOND, 119);
        if (now.after(prevPingTime)) {
            Command png = new Command("PNG");
            synchronized (writeBuffer) {
                writeBuffer.add(png);
                writeBuffer.notify();
            }
            prevPingTime = now;
        } else prevPingTime.add(GregorianCalendar.SECOND, -119);
    }

    /**
	 * Invokes a method defined in this instance (probably defined in a subclass),
	 * using reflection. The method is expected to accept a single argument of type
	 * AbstractCommand.
	 *
	 * @param name name of the method to be invoked.
	 * @param cmd an abstract command to be passed to that method.
	 */
    private void invokeMethod(String name, AbstractCommand cmd) {
        try {
            Class[] paramTypes = { AbstractCommand.class };
            Method method = this.getClass().getDeclaredMethod(name, paramTypes);
            Object[] args = { cmd };
            method.invoke(this, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
