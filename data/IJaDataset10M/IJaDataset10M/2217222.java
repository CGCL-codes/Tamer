package network;

import main.GameData;
import network.Packet;
import gui.ChatPanel;
import gui.GUIConstants;
import gui.GameFrame;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * 
 * 
 * 
 */
public class ServerThread extends Thread {

    /** * */
    private int port;

    private String serverName = "DefaultName";

    /** * */
    private boolean waitForConnect = true;

    /**
    * fds
    * 
    * @param port a
    */
    public ServerThread(int port) {
        this.port = port;
    }

    /**
    * (non-Javadoc)
    * 
    * @see java.lang.Runnable#run()
    */
    public void run() {
        ServerSocket serverSocket = null;
        Listener listener;
        try {
            ChatPanel.getInstance().println("Server waiting...");
            serverSocket = new ServerSocket(port);
            while (waitForConnect) {
                Socket client = serverSocket.accept();
                if (waitForConnect == false) {
                    client.close();
                }
                listener = new Listener(client, this);
                listener.start();
                GameData.getInstance().getListenerList().add(listener);
            }
            serverSocket.close();
        } catch (IOException e) {
            if (Protocol.DEBUG) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(GameFrame.getInstance(), "Server connection problem, make sure no other servers" + "are running and try again.", "Omega Space", JOptionPane.INFORMATION_MESSAGE, GUIConstants.GLOBAL_ICON);
        }
    }

    /**
    * @param message a
    */
    public void send(Object message) {
        for (int i = 0; i < GameData.getInstance().getListenerList().size(); i++) {
            Listener listener = (Listener) GameData.getInstance().getListenerList().get(i);
            listener.send(message);
        }
    }

    /**
    * quit
    * 
    */
    public void quit() {
        waitForConnect = false;
        for (int i = 0; i < GameData.getInstance().getListenerList().size(); i++) {
            Listener listener = (Listener) GameData.getInstance().getListenerList().get(i);
            Packet packet = new Packet(Packet.TX_QUIT_MESSAGE);
            listener.send(packet);
            listener.flush();
            removeListener(listener);
        }
    }

    /**
     * 
     */
    public final void closeLobby() {
        this.waitForConnect = false;
    }

    /**
    * @param listener a
    */
    public void removeListener(Listener listener) {
        listener.quit();
        GameData.getInstance().getListenerList().remove(listener);
    }

    /**
    * @return Returns the port.
    */
    public int getPort() {
        return port;
    }

    /**
    * @param port The port to set.
    */
    public void setPort(int port) {
        this.port = port;
    }

    /**
    * @return Returns the serverName.
    */
    public String getServerName() {
        return serverName;
    }

    /**
    * @param serverName The serverName to set.
    */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
