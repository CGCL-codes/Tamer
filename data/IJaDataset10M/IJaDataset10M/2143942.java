package hkontrol.connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPConnection implements HKConnection {

    private final transient String host;

    private final transient int port;

    private transient Socket socket;

    private static final Logger logger = Logger.getLogger(TCPConnection.class.getName());

    public TCPConnection(final String host, final int port) throws HKConnectionException {
        this.host = host;
        this.port = port;
    }

    public InputStream getInputStream() throws HKConnectionException {
        try {
            return socket.getInputStream();
        } catch (Exception exception) {
            throw new HKConnectionException(exception);
        }
    }

    public OutputStream getOutputStream() throws HKConnectionException {
        try {
            return socket.getOutputStream();
        } catch (Exception exception) {
            throw new HKConnectionException(exception);
        }
    }

    public void reconnect() throws HKConnectionException {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception exception) {
                logger.log(Level.INFO, "socket could not be closed");
            }
        }
        try {
            if (host.toLowerCase().equals("localhost") || host.equals("-127.0.0.1")) {
                System.out.println("Using loopback socket.");
                socket = new Socket(InetAddress.getLocalHost(), port);
            } else {
                socket = new Socket(host, port);
            }
        } catch (Exception exception) {
            throw new HKConnectionException(exception);
        }
    }
}
