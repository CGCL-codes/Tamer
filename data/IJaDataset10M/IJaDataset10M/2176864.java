package communications;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import communications.parsers.Parser;

public class PlayerConnection implements Runnable {

    ServerSocket welcomeSocket;

    Socket socket;

    Parser parser;

    BufferedReader fromClient;

    PrintWriter toClient;

    public PlayerConnection(int port) {
        try {
            welcomeSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() throws IOException {
        String message = "";
        String line = null;
        do {
            line = fromClient.readLine();
            message += line + "\r\n";
        } while (!line.equals(""));
        return message;
    }

    public void sendMessage(String message) {
        toClient.println(message);
        toClient.flush();
    }

    protected void closeDown() {
        try {
            fromClient.close();
            toClient.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            socket = welcomeSocket.accept();
            fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toClient = new PrintWriter(new DataOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");
    }
}
