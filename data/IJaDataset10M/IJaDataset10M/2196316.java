package org.ultichat;

import java.io.IOException;
import org.ultichat.net.PacketHandler;
import org.ultichat.net.SocketHandler;
import org.ultichat.ui.Console;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

/**
 * Main class that starts up the chat server
 * 
 * @author Anthony
 */
public class Main {

    private static final Console c = Console.getInstance();

    public static void main(String[] args) {
        c.setVisible(true);
        c.appendText("white", "Ready and lisening on port " + Integer.parseInt(args[0]) + ".");
        PacketHandler.loadAllPackets();
        try {
            IServer s = new Server(Integer.parseInt(args[0]), new SocketHandler());
            s.setIdleTimeoutMillis(Constants.IDLE_TIMEOUT_DELAY);
            s.run();
        } catch (IOException e) {
            Console.getInstance().appendText("red", e.toString());
        }
    }
}
