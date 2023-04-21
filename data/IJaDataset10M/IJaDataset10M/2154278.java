package officeserver.office.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import officeserver.log_error.Log;
import officeserver.main.MODULE;

public class NetworkConnectionManager implements Runnable {

    /** the port to listen to */
    private int port;

    /** the server thread pool */
    private ExecutorService pool;

    /** the maximum threads allowed */
    private static final int MAX_CONNECTIONS = 5;

    /**
     * Constructor for the class
     * 
     * @param portP
     *            the port to run on
     */
    public NetworkConnectionManager(final int portP) {
        port = portP;
        pool = Executors.newFixedThreadPool(MAX_CONNECTIONS);
    }

    /**
     * Run the server, accept connections, make a thread for each new
     * connection.
     */
    public void run() {
        ServerSocket theServer;
        Socket theClient = null;
        try {
            theServer = new ServerSocket(port);
            while (!Thread.interrupted()) {
                theClient = theServer.accept();
                NetworkConnection netCon = new NetworkConnection(theClient);
                pool.execute(netCon);
                Thread.yield();
            }
            theServer.close();
            System.out.println("Server Shut Down");
        } catch (IOException e) {
            System.err.println("Error in Server Socket accept");
            Log.writeToLog(MODULE.NETWORK, Level.SEVERE, "Error in Server Socket accept");
            e.printStackTrace();
        }
        pool.shutdownNow();
    }
}
