package ntorrent.io.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ntorrent.NtorrentApplication;
import org.apache.log4j.Logger;

/**
 * @author  Kim Eik
 */
public class Server extends Thread {

    private static ServerSocket servSocket;

    /**
	 * Log4j logger
	 */
    private static final Logger log = Logger.getLogger(Server.class);

    public Server() throws IOException {
        servSocket = new ServerSocket(NtorrentApplication.SETTINGS.getIntSocketPort());
    }

    public void run() {
        do {
            Socket client;
            try {
                client = servSocket.accept();
                ThreadedClientHandler clienthandler = new ThreadedClientHandler(client);
                clienthandler.start();
            } catch (IOException e) {
                log.warn(e.getMessage(), e);
            }
        } while (true);
    }
}
