package ces.platform.infoplat.service.ftpserver;

import java.io.*;
import java.net.*;

/**
 * This is the starting point of all the servers. It invokes a new listener
 * thread. <code>BaseServer</code> object is used to serve the request.
 *
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public class ServerEngine implements Runnable {

    private Thread mRunner = null;

    private ServerSocket mServerSocket = null;

    private BaseServer mServer = null;

    /**
     * Constructor. Set the server object.
     */
    public ServerEngine(BaseServer server) {
        mServer = server;
    }

    /**
     * Start the server. Open a new listener thread.
     */
    public synchronized void startServer() throws IOException {
        if (mRunner == null) {
            InetAddress serverAddr = mServer.getServerAddress();
            if (serverAddr == null) {
                mServerSocket = new ServerSocket(mServer.getServerPort(), 100);
            } else {
                mServerSocket = new ServerSocket(mServer.getServerPort(), 100, serverAddr);
            }
            mRunner = new Thread(this);
            mRunner.start();
        }
    }

    /**
     * Stop the server. Stop the listener thread.
     */
    public synchronized void stopServer() {
        if (mRunner != null) {
            mRunner.interrupt();
            mRunner = null;
        }
        if (mServerSocket != null) {
            try {
                mServerSocket.close();
            } catch (IOException ex) {
            }
            mServerSocket = null;
        }
        if (mServer != null) {
            mServer.dispose();
            mServer = null;
        }
    }

    /**
     * Get the server status.
     */
    public boolean isStopped() {
        return mRunner == null;
    }

    /**
     * Listen for client requests.
     */
    public void run() {
        while (mRunner != null) {
            try {
                Socket datasocket = mServerSocket.accept();
                mServer.serveRequest(datasocket);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Get the server object.
     */
    public BaseServer getServer() {
        return mServer;
    }
}
