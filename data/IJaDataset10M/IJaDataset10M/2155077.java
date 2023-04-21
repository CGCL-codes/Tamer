package icm.unicore.uvisit.plugin;

import com.pallas.unicore.utility.UserMessages;
import icm.unicore.uvisit.util.LoggerSupport;
import icm.unicore.uvisit.util.seap.SeapException;
import icm.unicore.uvisit.util.seap.SeapProcessor;
import icm.unicore.uvisit.util.seap.SeapProtocol;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ListIterator;
import java.util.Vector;
import java.util.logging.Logger;

/**
 *
 * @author Aleksander Nowinski
 */
public class PluginSeapServer {

    private static Logger logger = LoggerSupport.getLogger();

    private static final int DEFAULT_TIMEOUT = 200;

    private static final int DEFAULT_PORT = 4711;

    /**
     * Holds value of property port.
     */
    private int port = DEFAULT_PORT;

    /**
     * Utility field used by bound properties.
     */
    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /**
     * Holds value of property enabled.
     */
    private boolean enabled = false;

    private ProxyManager manager = null;

    private ServerThread serverThread = null;

    /** Creates a new instance of PluginSeapServer */
    public PluginSeapServer(ProxyManager manager) {
        this.manager = manager;
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /**
     * Getter for property port.
     * @return Value of property port.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Setter for property port.
     * @param port New value of property port.
     */
    public void setPort(int port) {
        int oldPort = this.port;
        this.port = port;
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.halt();
            serverThread = null;
        }
        propertyChangeSupport.firePropertyChange("port", new Integer(oldPort), new Integer(port));
        if (isEnabled()) {
            runServer();
        }
    }

    private void runServer() {
        logger.info("Trying to init seap proxy on port " + port);
        try {
            serverThread = new ServerThread();
            serverThread.start();
        } catch (IOException ioe) {
            String msg = "Cannot init seap server: " + ioe;
            logger.severe(msg);
            UserMessages.error(msg);
            enabled = false;
        }
    }

    /**
     * Getter for property enabled.
     * @return Value of property enabled.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Setter for property enabled.
     * @param enabled New value of property enabled.
     */
    public void setEnabled(boolean enabled) {
        boolean oldEnabled = this.enabled;
        this.enabled = enabled;
        if (!enabled && serverThread != null && serverThread.isAlive()) {
            serverThread.halt();
            serverThread = null;
        }
        propertyChangeSupport.firePropertyChange("enabled", new Boolean(oldEnabled), new Boolean(enabled));
        if (enabled && (serverThread == null || !serverThread.isAlive())) {
            runServer();
        }
    }

    /** Class representing main server thread. Ths thread accepts connections 
     * on server socket, and then runs separate threads for each client.
     */
    private class ServerThread extends Thread {

        private boolean halt = false;

        private ServerSocket ssocket = null;

        private Vector threads = new Vector();

        public ServerThread() throws IOException {
            ssocket = new ServerSocket(getPort());
            ssocket.setSoTimeout(DEFAULT_TIMEOUT);
        }

        public void run() {
            try {
                int counter = 0;
                while (!halt) {
                    counter++;
                    if (counter >= 100) {
                        counter = 0;
                        purgeThreads();
                    }
                    try {
                        Socket s = ssocket.accept();
                        logger.info("Uvisit: seap client connected from " + s.getInetAddress());
                        SeapProtocolThread thread = new SeapProtocolThread(s, manager);
                        threads.add(thread);
                        thread.start();
                    } catch (SocketTimeoutException scte) {
                    }
                }
                ssocket.close();
            } catch (SocketException soe) {
            } catch (IOException ioe) {
                String msg = "Unexpected exception while running seap proxy: " + ioe;
                logger.warning(msg);
                ioe.printStackTrace();
            }
        }

        private void purgeThreads() {
            for (ListIterator iter = threads.listIterator(); iter.hasNext(); ) {
                SeapProtocolThread next = (SeapProtocolThread) iter.next();
                if (!next.isAlive()) {
                    iter.remove();
                }
            }
        }

        private void haltThreads() {
            for (ListIterator iter = threads.listIterator(); iter.hasNext(); ) {
                SeapProtocolThread next = (SeapProtocolThread) iter.next();
                if (!next.isAlive()) {
                    iter.remove();
                } else {
                    next.halt();
                }
            }
        }

        public void halt() {
            try {
                ssocket.close();
            } catch (IOException ioe) {
            }
            halt = true;
            while (this.isAlive()) {
                try {
                    sleep(DEFAULT_TIMEOUT);
                } catch (InterruptedException ie) {
                }
            }
            haltThreads();
        }
    }

    /** 
     * Class representing single communication thread of the server. 
     * This thread is responsible for processing SEAP protocol connections.
     */
    private class SeapProtocolThread extends Thread {

        private SeapProtocol protocol;

        private boolean halt = false;

        /** Creates a new instance of SeapProtocolThread */
        public SeapProtocolThread(Socket s, SeapProcessor processor, int timeout) throws IOException {
            s.setSoTimeout(timeout);
            protocol = new SeapProtocol(s, processor);
        }

        public SeapProtocolThread(Socket s, SeapProcessor processor) throws IOException {
            this(s, processor, DEFAULT_TIMEOUT);
        }

        public void run() {
            try {
                while (!halt && protocol.isActive()) {
                    try {
                        protocol.processRequest();
                    } catch (SocketTimeoutException scte) {
                    } catch (SeapException se) {
                        UserMessages.error("Unicore Visit proxy:\n" + "Received invalid SEAP client request:\n" + se.getMessage());
                    }
                }
            } catch (SocketException se) {
            } catch (IOException ioe) {
                UserMessages.error("Unicore Visit proxy:\n" + "Error while communicating with seap client: " + ioe.getMessage());
            }
        }

        public void halt() {
            this.halt = true;
            try {
                protocol.getSocket().close();
            } catch (IOException io) {
            }
        }
    }
}
