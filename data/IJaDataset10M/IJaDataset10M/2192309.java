package GeneralGrizzlyConsensus;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 * 
 * @author Stephen Kent
 * 
 *         This handles listening for, accepting, and managing of multiple
 *         client connections.
 * 
 */
public enum GGCServer implements Runnable {

    INSTANCE;

    private boolean keepListening;

    private ServerSocket server;

    private Vector<GGCConnection> clients;

    private ActionListener messageSink;

    private String lastSentMessage;

    private ClientCleanUpTask clientCleaner;

    protected class ClientCleanUpTask implements Runnable {

        private Object lock = new Object();

        private boolean continueTask;

        ClientCleanUpTask() {
            continueTask = true;
        }

        public synchronized void stopCleanup() {
            continueTask = false;
        }

        @Override
        public void run() {
            while (continueTask) {
                synchronized (clients) {
                    for (int i = (clients.size() - 1); i >= 0; i--) {
                        if (clients.get(i).getRawSocket().isClosed()) clients.remove(i);
                    }
                }
                try {
                    synchronized (lock) {
                        lock.wait(50);
                    }
                } catch (InterruptedException e) {
                    GGCGlobals.INSTANCE.addExceptionToLog(e);
                }
            }
        }
    }

    /**
	 * The constructor instantiates the list of clients and starts the clean-up
	 * thread. It also initializes the last sent message and sets the connection
	 * acceptor condition to true.
	 */
    GGCServer() {
        keepListening = true;
        clients = new Vector<GGCConnection>();
        lastSentMessage = null;
        clientCleaner = new ClientCleanUpTask();
        Thread t = new Thread(clientCleaner);
        t.start();
    }

    /**
	 * Looks through the list of IPv4 addresses and attempts to locate the most
	 * likely one. NOTE: Now just a wrapper for getLikelyIpAddress(false)
	 * 
	 * @return Returns the first found likely IPv4 address.
	 */
    public static InetAddress getLikelyIpv4Address() {
        return getLikelyIpAddress(false);
    }

    /**
	 * Looks through the list of IPv6 addresses and attempts to locate the most
	 * likely one. NOTE: Now just a wrapper for getLikelyIpAddress(true)
	 * 
	 * @return Returns the first found likely IPv6 address.
	 */
    public static InetAddress getLikelyIpv6Address() {
        return getLikelyIpAddress(true);
    }

    /**
	 * Looks through the list of IP addresses and attempts to locate the most
	 * likely one which IP version depends on the value of the boolean parameter
	 * ipv6.
	 * 
	 * @param ipv6
	 * @return Returns the first found likely IP address.
	 */
    public static InetAddress getLikelyIpAddress(boolean ipv6) {
        InetAddress[] allIps = {};
        ArrayList<InetAddress> likelyAddresses = new ArrayList<InetAddress>();
        InetAddress fallbackAddress = null;
        try {
            allIps = InetAddress.getAllByName(InetAddress.getLocalHost().getCanonicalHostName());
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Unable to obtain this computer's IP addresses.");
        }
        if (allIps.length < 1) return null; else if (allIps.length == 1) return allIps[0]; else {
            for (int i = 0; i < allIps.length; i++) {
                if ((allIps[i].getHostAddress().contains(":")) == ipv6) {
                    if (allIps[i].isLinkLocalAddress()) {
                        fallbackAddress = allIps[i];
                    } else if (!allIps[i].isLoopbackAddress()) {
                        likelyAddresses.add(allIps[i]);
                    }
                }
            }
            if (likelyAddresses.size() < 1) return fallbackAddress; else {
                return likelyAddresses.get(likelyAddresses.size() - 1);
            }
        }
    }

    /**
	 * Returns the current amount of clients stored in the clients list.
	 * 
	 * @return Returns the size of the clients list.
	 */
    public int getNumberOfConnectedClients() {
        return clients.size();
    }

    /**
	 * Gets the server socket so that connections can be made.
	 * 
	 * @return Returns the reference to the server socket.
	 */
    public ServerSocket getRawServerSocket() {
        return server;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(GGCGlobals.INSTANCE.COMMUNICATION_PORT);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to create server port.");
        }
        while (keepListening) {
            GGCConnection conn;
            try {
                conn = new GGCConnection(server.accept(), messageSink);
                clients.add(conn);
                Thread t = new Thread(conn);
                t.start();
                if (lastSentMessage != null) conn.sendMessage(lastSentMessage);
            } catch (IOException e) {
                GGCGlobals.INSTANCE.addExceptionToLog(e);
            }
        }
        try {
            synchronized (clients) {
                for (GGCConnection client : clients) {
                    client.closeConnection();
                }
                clients.clear();
            }
            server.close();
        } catch (IOException e) {
            GGCGlobals.INSTANCE.addExceptionToLog(e);
        }
    }

    /**
	 * Sends the specified message to all connected clients, and also sets the
	 * last sent message to the message in case new client.
	 * 
	 * @param msg
	 */
    public void sendMessageToAll(String msg) {
        lastSentMessage = msg;
        synchronized (clients) {
            for (GGCConnection client : clients) {
                client.sendMessage(msg);
            }
        }
    }

    public synchronized void setMessageSink(ActionListener listener) {
        messageSink = listener;
    }

    /**
	 * This method will stop listening for new connections and gracefully exit
	 * remaining threads. This allows the main program to exit without leaving
	 * connections open.
	 */
    public synchronized void stopListening() {
        keepListening = false;
        clientCleaner.stopCleanup();
    }
}
