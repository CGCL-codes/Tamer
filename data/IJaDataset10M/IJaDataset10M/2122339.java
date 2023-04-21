package org.susan.java.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class LoggingServer {

    private ServerSocket serverSocket = null;

    private Socket socket = null;

    public LoggingServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void acceptMessage() {
        try {
            InputStream inStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
            String str = null;
            while ((str = reader.readLine()) != null) {
                System.out.println(str);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String args[]) {
        LoggingServer server = new LoggingServer(2020);
        server.acceptMessage();
    }
}
