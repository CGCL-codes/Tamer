package edu.hawaii.ics.ami.lib.io;

import java.io.*;
import java.net.*;

public class SingleServerSocketInput extends Input {

    public static final int DEFAULT_PORT = 80;

    private transient ServerSocket serverSocket;

    private transient Socket socket;

    private int port;

    private transient InputStream inputStream;

    public SingleServerSocketInput(int port) {
        this.port = port;
    }

    public SingleServerSocketInput() {
        this(DEFAULT_PORT);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Output createOutput() {
        return new SingleServerSocketOutput(this.port);
    }

    public String getName() {
        return "Single Server Socket Input";
    }

    public String getURI() {
        return "" + port;
    }

    public void setURI(String uri) throws IOException {
        try {
            this.port = Integer.parseInt(uri);
        } catch (Exception e) {
            throw new IOException("Please specify a port number.");
        }
    }

    public void open() throws IOException {
        serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        inputStream = socket.getInputStream();
    }

    public boolean isConnected() {
        return (inputStream != null);
    }

    public void close() {
        try {
            inputStream.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
        }
        inputStream = null;
        socket = null;
        serverSocket = null;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
