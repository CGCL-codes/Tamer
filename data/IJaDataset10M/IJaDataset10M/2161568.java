package net.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import net.peer.Client;
import net.peer.Peer;

public class ConnectionReceptor implements Runnable {

    private ServerSocket ssocket;

    private ConnectionManager manager;

    public ConnectionReceptor(ServerSocket ssocket, ConnectionManager manager) {
        this.ssocket = ssocket;
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            Socket socket = ssocket.accept();
            Peer peer = new Client();
            peer.setIP(socket.getInetAddress().toString());
            manager.acceptConnection(manager.createConnection(socket), peer);
            manager.removeReceptor(this);
            ssocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
