package sourcekeeper.common;

import java.io.*;
import java.net.*;

/**
 * Connection is the base class of all connections
 * @author Christoph Permes
 */
public abstract class Connection extends Thread {

    private boolean connected = false;

    private boolean performedDisconnect = false;

    private Socket socket = null;

    private int port;

    private String host;

    private WatchThread watchThread = null;

    /** Creates a new instance of Connection with given port and hostname */
    public Connection(int port, String host) throws Exception {
        this.port = port;
        this.host = host;
    }

    /** Creates a new instance of Connection with the given socket */
    public Connection(Socket socket, ConnectionErrorHandler errorHandler) {
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new WatchThread(this, errorHandler);
        } catch (Exception e) {
            System.out.println("Connection::Connection(Socket)");
            System.out.println(e.toString());
        }
    }

    /** establish a connection */
    public void connect(ConnectionErrorHandler errorHandler) throws Exception {
        if (socket != null) return;
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        new WatchThread(this, errorHandler);
    }

    /** destroy the connection */
    public void disconnect() throws Exception {
        try {
            out.close();
            in.close();
            socket.close();
            socket = null;
            performedDisconnect = true;
        } catch (Exception e) {
            System.out.println("Connection::disconnect()");
            System.out.println(e.toString());
        }
    }

    /** @return if the connection is up */
    public boolean isConnected() {
        if (socket == null) return false;
        if (socket.isClosed()) return false;
        return true;
    }

    /** @return if a manual disconnect has been performed */
    public boolean disconnectPerformed() {
        return performedDisconnect;
    }

    /** send a string over the connection */
    public void send(String line) {
        try {
            out.write(line);
            out.newLine();
            out.flush();
        } catch (Exception e) {
            System.out.println("Connection::send()");
            System.out.println(e.toString());
        }
    }

    public void run() {
        while (isConnected()) {
            try {
                String request = in.readLine();
                processRequest(request);
            } catch (Exception e) {
                System.out.println("Connection::run()");
                System.out.println(e.toString());
            }
        }
    }

    protected abstract void processRequest(String request);

    protected BufferedReader in;

    protected BufferedWriter out;

    protected static final String separator = "";
}
