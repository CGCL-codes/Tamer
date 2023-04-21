package backuper;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Sebastian
 */
public class CServerConnector implements Runnable {

    private static CServerConnector instance;

    private ServerSocket serverSocket;

    private boolean listening;

    private CServerConnector() throws IOException {
        serverSocket = null;
        listening = true;
        System.out.println("CServerConnector");
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public static synchronized CServerConnector getInstance() throws IOException {
        if (instance == null) {
            instance = new CServerConnector();
        }
        return instance;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(ServerConstraints.TCP_PORT);
            while (listening) new CServerConnectorThread(serverSocket.accept()).start();
            serverSocket.close();
        } catch (IOException ex) {
            System.err.println("IO Exception " + ex.getMessage());
        }
    }
}
