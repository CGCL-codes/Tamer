package in.mantavyagajjar.screen.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import in.mantavyagajjar.screen.common.DataStream;
import in.mantavyagajjar.screen.common.ServerConfig;

/**
 * @author Mantavya Gajjar
 *
 */
public class Server implements Runnable {

    private Thread serverThread;

    private ServerConfig config;

    private ServerSocket socket;

    private boolean state = false;

    /**
	 * 
	 */
    public Server(ServerConfig config) {
        this.config = config;
    }

    public void run() {
        Socket s = null;
        DataStream ds = null;
        while (this.state) {
            try {
                s = this.socket.accept();
                ds = new DataStream(s.getInputStream(), new FileOutputStream("client.txt"), this.config.getBufferSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serverStart() {
        int backlog = config.getNetworkSize();
        InetAddress host = null;
        try {
            host = InetAddress.getByName(config.getHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            this.socket = new ServerSocket(config.getPort(), backlog, host);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.state = true;
        this.serverThread = new Thread(this, "ScreenServer");
        this.serverThread.start();
    }

    public void serverStop() {
        this.serverThread = null;
        this.socket = null;
        this.state = false;
    }
}
