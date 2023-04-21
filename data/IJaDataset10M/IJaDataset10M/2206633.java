package databaseserver;

import java.net.ServerSocket;
import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(50691);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 50691.");
            System.exit(-1);
        }
        while (listening) {
            new ServerThread(serverSocket.accept()).start();
        }
        serverSocket.close();
    }
}
