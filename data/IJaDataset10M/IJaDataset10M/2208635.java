package x10.eventServices.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;
import x10.event.X10Event;
import x10.eventServices.X10EventListener;
import x10.queues.X10EventQueue;

/**
 * @author Denny
 *
 */
public class TCPConnection implements Runnable, X10EventListener {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private TCPConnectionManager tcpConnectionManager;

    private Socket socket;

    private X10EventQueue queue = new X10EventQueue();

    boolean running = true;

    private OutputStream outputStream = null;

    private InputStream inputStream = null;

    private ObjectOutputStream objectOutputStream = null;

    private ObjectInputStream objectInputStream = null;

    static int connectionNumber = 1;

    private String eventListenerName;

    public TCPConnection(TCPConnectionManager tcpConnectionManager, Socket socket) {
        eventListenerName = "TCPConnection" + connectionNumber++;
        this.socket = socket;
        this.tcpConnectionManager = tcpConnectionManager;
    }

    public void run() {
        X10Event event;
        try {
            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = socket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            logger.fine("TCP client connection established");
        } catch (SecurityException se) {
            logger.fine(se.getMessage());
        } catch (SocketException se) {
            close();
        } catch (IOException ioe) {
            logger.fine(ioe.getMessage());
        }
        while (running) {
            if (socket.isBound()) {
            } else {
                close();
                break;
            }
            event = null;
            try {
                event = (X10Event) objectInputStream.readObject();
            } catch (NullPointerException npe) {
                if (objectInputStream == null) {
                    logger.fine("NullPointerException received - tcp client probably died - closing socket");
                    close();
                    break;
                } else {
                    logger.fine(npe.getMessage());
                }
            } catch (IOException ioe) {
                logger.fine(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                logger.fine(cnfe.getMessage());
            } catch (Exception e) {
                logger.fine(e.getMessage());
            }
            if (event != null) {
                this.transmitEvent(event);
            }
            while (queue.hasNext()) {
                event = queue.pop();
                try {
                    objectOutputStream.writeObject(event);
                } catch (SocketException se) {
                    logger.fine(se.getMessage());
                } catch (IOException ioe) {
                    logger.fine(ioe.getMessage());
                }
            }
        }
    }

    public void transmitEvent(X10Event event) {
        tcpConnectionManager.receiveEvent(event);
    }

    /**
     * getEventListenerName() - returns the name of this X10EventListener class
     */
    public String getEventListenerName() {
        return eventListenerName;
    }

    public void receiveEvent(X10Event event) {
        queue.add(event);
    }

    public void close() {
        logger.fine("Closing TCP Client connection with IP:" + socket.getInetAddress());
        running = false;
        tcpConnectionManager.dequeueTCPConnection(this);
    }
}
