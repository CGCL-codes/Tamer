package com.rwoar.moo.client.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection {

    private String host = null;

    private int port;

    private Socket mySocket = null;

    private BufferedReader inFromServer = null;

    private DataOutputStream outToServer = null;

    public Connection(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        this.openConnection();
    }

    public void openConnection() throws Exception {
        try {
            this.mySocket = new Socket(this.host, this.port);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        try {
            inFromServer = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            outToServer = new DataOutputStream(mySocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void closeConnection() throws IOException {
        mySocket.close();
        mySocket = null;
        inFromServer = null;
        outToServer = null;
    }

    public void writeLine(String line) {
        if (line.equals("")) return;
        try {
            this.outToServer.writeBytes(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() {
        String inLine = new String();
        try {
            inLine = inFromServer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inLine;
    }
}
