package clxnet.server;

import clxnet.server.net.ClientThread;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Scott Adams
 */
public class Server extends Thread {

    private ServerSocket serverSocket = null;

    private boolean listening = true;

    public int port;

    @Override
    public void run() {
        listening = true;
        try {
            port = 14134;
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Could not listen on port: " + port);
            System.exit(-1);
        }
        System.out.println("Server Started");
        try {
            while (listening) {
                new ClientThread(serverSocket.accept()).start();
            }
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
