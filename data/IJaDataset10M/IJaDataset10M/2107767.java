package com.halozat2009osz;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketServer extends BaseSocketServer {

    private ServerSocket serverSocket;

    private Socket inSocket;

    private BufferedReader inReader;

    private int portNumber;

    private boolean initialized;

    /**
	 * Constructor
	 */
    public SimpleSocketServer() {
        this.initialized = false;
        this.portNumber = SOCKETSERVER_PORT;
    }

    /**
	 * Constructor
	 */
    public SimpleSocketServer(int portNumber) {
        this.initialized = false;
        this.portNumber = portNumber;
    }

    private boolean createObjects() {
        if (!initialized) {
            try {
                infoLog("Open server socket on port " + portNumber);
                serverSocket = new ServerSocket(portNumber);
                initialized = serverSocket != null;
            } catch (Exception ex) {
                errorLog(ex.getLocalizedMessage());
            }
        }
        return initialized;
    }

    public void RunListener() {
        infoLog("Listener has been started ...");
        if (!createObjects()) {
            return;
        }
        String line = null;
        try {
            while (true) {
                infoLog("Opening socket ...");
                inSocket = serverSocket.accept();
                infoLog("Accept incoming socket from " + inSocket.getInetAddress() + ":" + inSocket.getLocalPort());
                if (!serverSocket.isClosed()) {
                    infoLog("Connect input stream");
                    inReader = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
                    infoLog("Reading incoming buffer ...");
                    while ((line = inReader.readLine()) != null) {
                        inReader.read();
                        infoLog("Received message [" + line + "]");
                        Commands.execute(line, this);
                    }
                    inReader.close();
                    inReader = null;
                    inSocket.close();
                    inSocket = null;
                    serverSocket.close();
                }
            }
        } catch (Exception ex) {
            errorLog(ex.getLocalizedMessage());
        } finally {
            inReader = null;
            inSocket = null;
            serverSocket = null;
            infoLog("Listener has been stopped");
        }
    }
}
