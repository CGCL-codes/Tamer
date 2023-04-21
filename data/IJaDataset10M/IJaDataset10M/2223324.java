package com.agentfactory.service.mts.http;

import com.agentfactory.platform.core.IPlatformServiceManager;
import com.agentfactory.platform.impl.MessageTransportService;
import java.net.*;
import java.io.*;

/**
 * Implements the HTTP server socket.  Received requests are handled by
 * instances of the HTTPRequestHandler class.
 *
 * @author  Rem Collier
 */
public class HTTPServer implements Runnable {

    private int port;

    private boolean active;

    private ServerSocket server;

    private IPlatformServiceManager manager;

    private MessageTransportService service;

    /** Creates a new instance of Server */
    public HTTPServer(int port, IPlatformServiceManager manager, MessageTransportService service) {
        this.port = port;
        this.manager = manager;
        this.service = service;
        this.active = true;
    }

    public void run() {
        try {
            server = new ServerSocket(port, 2000);
        } catch (IOException e) {
            System.out.println("Could not listen on port " + port);
            System.exit(-1);
        }
        while (isActive()) {
            try {
                Socket socket = server.accept();
                HTTPRequestHandler handler = new HTTPRequestHandler(socket, manager, service);
                new Thread(handler, "HTTP-MTS-Handler-" + socket.getRemoteSocketAddress().toString()).start();
            } catch (IOException e) {
                System.exit(-1);
            }
        }
        try {
            server.close();
        } catch (IOException e) {
            System.out.println("Could not close down server");
        }
    }

    public String getHost() {
        String host = null;
        try {
            host = (InetAddress.getLocalHost()).getHostAddress();
        } catch (UnknownHostException e) {
            host = "unknown";
        }
        return host;
    }

    public int getPort() {
        return port;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
