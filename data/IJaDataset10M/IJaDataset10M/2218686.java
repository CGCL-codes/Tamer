package uk.co.upco.iopproxy;

import java.net.*;
import java.io.*;

/**
 * Server polling class. Runs on separate thread and waits for
 * new client connections.
 */
public class ServerListen implements Runnable {

    /** Reference to server's IP Socket */
    private ServerSocket serverSocket = null;

    /** Determines whether server is listening for new input */
    private boolean isListening = true;

    /** The port the server is listening on */
    private int localPort = 2006;

    /**
	 * Creates the server socket and starts the proxy server
	 * listening on either the port read from the configuration
	 * file, or 2001 if none was specified.
	 * @param listenPort The port to listen on.
	 */
    public ServerListen(int listenPort) {
        localPort = listenPort;
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                ServerListen.this.stop();
                if (Proxy.logging) {
                    Proxy.logInfo(this, "Received ServerListen shutdown from VM hook");
                }
                try {
                    Utils.closeLog();
                } catch (Exception e) {
                    if (Proxy.logging) Proxy.logException(this, e);
                }
            }
        });
    }

    /** Listen process itself. Starts listening and doesn't stop until
	* isListening becomes false. When isListening does become false, 
	* destroys the server socket and itself.
	*/
    public void run() {
        try {
            Proxy.logInfo(this, "Initialising IOP Proxy server on " + getInternetIP() + ":" + Integer.toString(localPort));
            serverSocket = new ServerSocket(localPort, Proxy.getServerBackLog());
        } catch (IOException e) {
            Proxy.logError(this, "Could not initialise server on port: " + Integer.toString(localPort) + "\n" + e.getMessage());
            Proxy.logException(this, e);
            return;
        }
        Proxy.logInfo(this, "Proxy initialisation complete.");
        while (isListening) {
            try {
                Socket socket = serverSocket.accept();
                Proxy.logInfo(this, "Received client connection, allocating streams.");
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                DataInputStream in = new DataInputStream(socket.getInputStream());
                IOPServerEntry iopServer = Proxy.getServer();
                if (!(iopServer == null)) {
                    if (Proxy.logging) {
                        Proxy.logInfo(this, "Allocated server: " + iopServer.getHost() + ":" + Integer.toString(iopServer.getPort()));
                    }
                    ManagedThread thread = Proxy.getManagedThread();
                    if (thread.getIsDormant()) {
                        thread.setIsDormant(false);
                        thread.start();
                    }
                    ServerListener sl = new ServerListener(in, out, socket, thread);
                    setUpClientListener(iopServer, sl, thread);
                } else {
                    if (Proxy.logging) {
                        Proxy.logError(this, "No servers are available for client - sending error.");
                    }
                    out.print("99 No IOP Servers available&;");
                    out.flush();
                    in.close();
                    out.close();
                    socket.close();
                }
                try {
                    Thread.sleep(25);
                } catch (Exception ex) {
                    if (Proxy.logging) Proxy.logException(this, ex);
                }
            } catch (SocketException e) {
                if (e.getMessage().toLowerCase().indexOf("socket closed") == -1) {
                    Proxy.logError(this, "Unexpected socket error: " + e.getMessage());
                    Proxy.logException(this, e);
                }
            } catch (Exception e) {
                if (Proxy.logging) {
                    Proxy.logError(this, "Error with socket streams: " + e.getMessage());
                    Proxy.logException(this, e);
                }
            }
        }
        try {
            serverSocket.close();
        } catch (IOException exx) {
            Proxy.logError(this, "Error closing server socket - " + exx.getMessage());
            Proxy.logException(this, exx);
        }
    }

    /** Stops the object listening by destroying the server socket.*/
    public void stop() {
        isListening = false;
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            Proxy.logError(this, "Unable to close server socket: " + e.getMessage());
            Proxy.logException(this, e);
        }
    }

    /**
	 *  Returns the Internet IP address for the current server
	 */
    public String getInternetIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            Proxy.logException(this, e);
            return "<unable to get local host>";
        }
    }

    /**
	 * Method getIOPServer.
	 * Returns null if no IOPServer
	 * @param in
	 * @param out
	 * @param socket
	 * @return IOPServerEntry
	 */
    private IOPServerEntry getIOPServer(DataInputStream in, PrintWriter out, Socket socket) {
        try {
            IOPServerEntry iopServer = Proxy.getServer();
            if (iopServer == null) {
                if (Proxy.logging) Proxy.logError(this, "No servers are available for client - sending error.");
                out.print("99 No IOP Servers available&;");
                out.flush();
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    Proxy.logException(this, e);
                }
            }
            return iopServer;
        } catch (Exception e) {
            if (Proxy.logging) Proxy.logException(this, e);
        }
        return null;
    }

    private void setUpClientListener(IOPServerEntry iopServer, ServerListener sl, ManagedThread managedThread) {
        try {
            ClientListener proxySocket = new ClientListener(iopServer, sl, managedThread);
            sl.setClientListner(proxySocket);
            managedThread.addProxyInputListener(proxySocket);
            managedThread.addProxyInputListener(sl);
        } catch (Exception e) {
            if (Proxy.logging) {
                Proxy.logError(this, "Error opening socket for IOP Server Proxy connection: " + e.getMessage());
                Proxy.logError(this, "Sending error to client.");
                Proxy.logError(this, "Marking server as dead.");
            }
            sl.send("99 IOP Server is dead&;");
            iopServer.setIsDead(true);
            sl.close();
        }
    }
}
