package com.eip.yost.relais;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Agent extends Thread {

    protected Socket sock;

    protected ConnectionsManager cManager;

    protected BufferedReader in;

    protected PrintWriter out;

    protected String identifier;

    protected Agent(ConnectionsManager manager, Socket socket, String id) throws IOException {
        sock = socket;
        cManager = manager;
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new PrintWriter(sock.getOutputStream());
        identifier = id;
    }

    protected void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    protected String receiveMessage() throws IOException {
        return in.readLine();
    }

    protected boolean isReadyForMessages() {
        return this.isAlive() && sock.isConnected();
    }

    protected void closeConnection() {
        try {
            in.close();
            out.flush();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String getIdentifier() {
        return identifier;
    }
}
