package server;

import java.net.*;
import java.io.*;
import java.util.logging.*;
import game.*;

public class ConnectionAcceptor implements Runnable {

    private ServerSocket cSocket;

    private Universe instance;

    private static Logger log = Logger.getLogger("ConnectionAcceptor");

    public ConnectionAcceptor(Universe instance) throws IOException {
        this.cSocket = new ServerSocket(15151);
        this.instance = instance;
    }

    public void run() {
        ConnectionHandler connected = null;
        try {
            Socket connection = this.cSocket.accept();
            connected = new ConnectionHandler(connection, this.instance);
        } catch (IOException e) {
            log.severe("IO Error while opening new connection to client");
        }
        if (connected != null) {
            Thread connThread = new Thread(connected);
            connThread.setDaemon(true);
            connThread.run();
        }
        try {
            ConnectionAcceptor internal = new ConnectionAcceptor(this.instance);
            Thread internalThread = new Thread(internal);
            internalThread.setDaemon(true);
            internalThread.run();
        } catch (IOException e) {
            log.severe("Connection Acceptor Failed to Respawn");
        }
    }
}
