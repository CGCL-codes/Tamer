package jfreerails.network;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import jfreerails.world.common.FreerailsSerializable;
import org.apache.log4j.Logger;

/**
 * This class has the code that is shared by the client and server versions of
 * InetConnection.
 * 
 * @author Luke
 */
abstract class AbstractInetConnection implements Runnable {

    private static final Logger logger = Logger.getLogger(AbstractInetConnection.class.getName());

    private final SychronizedQueue inbound = new SychronizedQueue();

    private final InetConnection inetConnection;

    private final SynchronizedFlag readerThreadStatus = new SynchronizedFlag(false);

    private final SynchronizedFlag status = new SynchronizedFlag(true);

    private int timeout = 1000 * 5;

    public AbstractInetConnection(Socket s) throws IOException {
        inetConnection = new InetConnection(s);
        open();
    }

    public AbstractInetConnection(String ip, int port) throws IOException {
        inetConnection = new InetConnection(ip, port);
        open();
    }

    public void disconnect() throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug(this + "Initiating shutdown..");
        }
        shutdownOutput();
        long waitUntil = System.currentTimeMillis() + timeout;
        synchronized (readerThreadStatus) {
            while (readerThreadStatus.isOpen()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime >= waitUntil) {
                    shutDownInput();
                    throw new IOException("Time-out while trying to disconnect.");
                }
                try {
                    readerThreadStatus.wait(timeout);
                } catch (InterruptedException e) {
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this + "Finished shutdown!! --status=" + String.valueOf(status.isOpen()));
        }
    }

    public void flush() throws IOException {
        inetConnection.flush();
    }

    public synchronized boolean isOpen() {
        return status.isOpen();
    }

    public void run() {
        try {
            while (true) {
                FreerailsSerializable fs = inetConnection.receive();
                synchronized (inbound) {
                    inbound.write(fs);
                    inbound.notifyAll();
                }
            }
        } catch (EOFException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this + "Recipricating shutdown..");
        }
        shutDownInput();
        readerThreadStatus.close();
    }

    private synchronized void open() throws IOException {
        Thread t = new Thread(this);
        t.setName(getThreadName());
        inetConnection.open();
        t.start();
        readerThreadStatus.open();
    }

    private synchronized void shutDownInput() {
        try {
            inetConnection.shutdownInput();
            if (logger.isDebugEnabled()) {
                logger.debug(this + "Shut down input.");
            }
            if (status.isOpen()) {
                shutdownOutput();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private synchronized void shutdownOutput() throws IOException {
        if (!status.isOpen()) {
            throw new IllegalStateException();
        }
        status.close();
        inetConnection.shutdownOutput();
        if (logger.isDebugEnabled()) {
            logger.debug(this + "Shut down output.");
        }
    }

    abstract String getThreadName();

    FreerailsSerializable[] read() throws IOException {
        if (status.isOpen()) {
            return inbound.read();
        }
        throw new IOException();
    }

    void send(FreerailsSerializable object) throws IOException {
        inetConnection.send(object);
    }

    void setTimeOut(int i) {
        timeout = i;
    }

    FreerailsSerializable waitForObject() throws InterruptedException, IOException {
        if (status.isOpen()) {
            synchronized (inbound) {
                if (inbound.size() > 0) {
                    return inbound.getFirst();
                }
                inbound.wait();
                if (inbound.size() > 0) {
                    return inbound.getFirst();
                }
                throw new IllegalStateException();
            }
        }
        throw new IOException("The connection is close.");
    }
}
