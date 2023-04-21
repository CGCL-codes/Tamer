package spellcast.ui;

import java.io.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import javax.swing.table.*;
import org.log4j.*;
import spellcast.game.*;
import spellcast.message.*;

class ServerListUpdater implements Runnable {

    /**
     * The datagram packet size is 1024 bytes.  Any packet exceeding this length
     * will be truncated.
     */
    private static final int PACKET_SIZE = 1024;

    /**
     * The sleep for this many milliseconds before running the loop again.
     * On each cycle we check for any new messages from servers and update the
     * connection list.
     */
    private static final int SEND_REQUEST_SLEEP_TIMEOUT = 1 * 1000;

    /**
     * Once the list has been updated this many times a new request is sent
     * to query for new servers.  This gives us time to receive the responses
     * from servers during each cycle.
     */
    private static final int SEND_REQUEST_AFTER_UPDATE_NUMBER = 5;

    private DatagramSocket socket;

    private int numberOfUpdatesDone;

    private Thread serverListThread;

    private ServerListTableModel serverList;

    private boolean isRunning;

    private static final Category cat = Category.getInstance("client.connect");

    private static final Category cat_net = Category.getInstance("client.connect.net");

    ServerListUpdater(ServerListTableModel serverList) {
        this.serverList = serverList;
        try {
            InetAddress bindAddress = InetAddress.getLocalHost();
            socket = new DatagramSocket(0, bindAddress);
            socket.setSoTimeout(100);
        } catch (Exception ex) {
            cat.error("Could not create DatagramSocket for ServerListRequest/Responses", ex);
        }
    }

    public void run() {
        while (isRunning) {
            updateAvailableServers();
            try {
                Thread.sleep(SEND_REQUEST_SLEEP_TIMEOUT);
            } catch (InterruptedException ex) {
                cat.error("Unexepectedly Interrupted.", ex);
            }
        }
    }

    /**
     * Allocate resources, create and start the thread.
     * Resources are deallocated in <code>cleanup</code>.
     */
    public void start() {
        isRunning = true;
        numberOfUpdatesDone = 0;
        serverListThread = new Thread(this, "ServerListUpdater");
        serverListThread.start();
    }

    /**
     * Sets the isRunning flag to false which lets <code>run</code> exit the 
     * infite loop. This method will wait until the thread dies before 
     * calling <code>cleanup</code> where resources are deallocated.
     */
    public void stop() {
        isRunning = false;
        try {
            serverListThread.join();
        } catch (InterruptedException e) {
            cat.error("Unexepectedly Interrupted.", e);
        }
        cleanup();
    }

    /**
     * Dellocate resources allocated in <code>start</code>
     */
    private void cleanup() {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        serverListThread = null;
    }

    private void updateAvailableServers() {
        if ((numberOfUpdatesDone % SEND_REQUEST_AFTER_UPDATE_NUMBER) == 0) {
            numberOfUpdatesDone = 0;
            sendServerListRequest();
        }
        numberOfUpdatesDone++;
        ServerStatus ss = null;
        ArrayList tempList = new ArrayList(2);
        while ((ss = receiveServerListResponse()) != null) {
            cat_net.info("\nReceived response\n" + "Address: " + ss.getServerIPAddress() + "\n" + "Game Name: " + ss.getGameName() + "\n");
            tempList.add(ss);
        }
        if (tempList.size() != 0) {
            SwingUtilities.invokeLater(new DoUpdateOnSwingThread(serverList, tempList));
        }
    }

    private void sendServerListRequest() {
        try {
            InetAddress broadcast = InetAddress.getByName(GameConstants.SPELLCAST_NET_BROADCAST_INETADDRESS);
            DatagramPacket send = new DatagramPacket(new byte[0], 0, broadcast, GameConstants.SPELLCAST_NET_SERVERLIST_PORT);
            ServerListRequest request = new ServerListRequest();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            byte[] bytes = baos.toByteArray();
            send.setData(bytes);
            send.setLength(bytes.length);
            socket.send(send);
            cat_net.debug("\nLocal Address = " + socket.getLocalAddress() + "\n" + "Local Port = " + socket.getLocalPort() + "\n" + "Connected Address = " + socket.getInetAddress() + "\n" + "Connected Port = " + socket.getPort() + "\n");
            cat_net.debug("Sent ServerListRequest.size = " + bytes.length);
        } catch (Exception ex) {
            cat.error("Exception occurred.", ex);
        }
    }

    private ServerStatus receiveServerListResponse() {
        ServerStatus result = null;
        DatagramPacket response = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
        boolean messageReceived = false;
        try {
            socket.receive(response);
            cat_net.debug("Response Size = " + response.getLength());
            messageReceived = true;
        } catch (InterruptedIOException ix) {
            messageReceived = false;
        } catch (IOException ex) {
            cat.error("Failed on socket.receive.", ex);
        }
        if (messageReceived && response.getLength() != 0) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(response.getData()));
                Object obj = ois.readObject();
                if (!(obj instanceof ServerListResponse)) {
                    cat.error("ServerListResponse not received. Received " + obj.getClass().getName());
                } else {
                    ServerListResponse slr = (ServerListResponse) obj;
                    result = slr.getServerStatus();
                }
            } catch (Exception ex) {
                cat.error("Failed to decode ServerStatus from message.", ex);
            }
        }
        return result;
    }

    /**
     * This inner class is updates the server list on Swings Thread.
     * This class is runnable so that <code>SwingUtilities.invokeLater</code>
     * can run the necessary UI changes on Swing's thread.  
     * This class requires the server list table model that is to be updated
     * as well as a list of the servers to add to the model.  This class creates a copy
     * of the list of servers.
     */
    class DoUpdateOnSwingThread implements Runnable {

        private ServerListTableModel serverList;

        private ArrayList newServers;

        DoUpdateOnSwingThread(ServerListTableModel serverList, List newServers) {
            this.serverList = serverList;
            this.newServers = new ArrayList(newServers);
        }

        public void run() {
            Iterator i = newServers.iterator();
            while (i.hasNext()) {
                ServerStatus ss = (ServerStatus) i.next();
                serverList.add(ss);
            }
        }
    }
}
