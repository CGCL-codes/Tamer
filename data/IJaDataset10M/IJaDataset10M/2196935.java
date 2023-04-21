package network;

import java.io.*;
import java.net.*;

public class OpenPokerServerSocket extends ServerSocket implements Runnable {

    private int connectionCount = 0;

    private boolean interrupt = false;

    public OpenPokerServerSocket(int port) throws IOException {
        super(port);
    }

    public Socket accept() throws IOException {
        connectionCount++;
        return super.accept();
    }

    public void interrupt() {
        interrupt = true;
    }

    public void run() {
        while (!interrupt) try {
            ConnectionHandler.getConnectionHandler().newConnection(accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
