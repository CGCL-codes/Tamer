package gnu.inet.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.zip.InflaterInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class implements an FTP-style data connection server thread for GETing
 * files/data non-passively.
 * <P>
 * This class is used internally to the FtpClient class.
 */
public class ActiveGetter extends Getter {

    private static final Log log = LogFactory.getLog(ActiveGetter.class);

    private InetAddress address;

    private int port;

    private ServerSocket server;

    private int timeout;

    /**
     * Create a new ActiveGetter with the given OutputStream for data output.
     * 
     * @throws IOException
     *                 an IO error occurred with the ServerSocket
     */
    public ActiveGetter(OutputStream out) throws IOException {
        super();
        this.server = new ServerSocket(0);
        this.timeout = 30 * 1000;
        this.port = server.getLocalPort();
        this.address = this.server.getInetAddress();
        this.ostream = out;
    }

    /**
     * get the local port this ActiveGetter is listening on
     * 
     * @return port number
     */
    public synchronized int getPort() {
        return port;
    }

    /**
     * get the local IP address that this ActiveGetter is listening on
     * 
     * @return server socket IP address
     */
    public InetAddress getInetAddress() {
        return address;
    }

    /**
     * Set the connection timeout in milliseconds. This method must be called
     * before start()/run() for the value to take affect.
     * 
     * @param milliseconds
     *                the socket timeout value in milliseconds
     */
    public void setTimeout(int milliseconds) {
        timeout = milliseconds;
    }

    /**
     * get data from server using given parameters.
     */
    public void run() {
        boolean signalClosure = false;
        Socket sock = null;
        InputStream istream = null;
        long amount = 0;
        long buffer_size = 0;
        byte buffer[] = new byte[BUFFER_SIZE];
        try {
            server.setSoTimeout(timeout);
            if (cancelled) throw new InterruptedIOException("Transfer cancelled");
            sock = server.accept();
            signalConnectionOpened(new ConnectionEvent(sock.getInetAddress(), sock.getPort()));
            signalClosure = true;
            signalTransferStarted();
            try {
                switch(type) {
                    case FtpClientProtocol.TYPE_ASCII:
                        istream = new AsciiInputStream(sock.getInputStream());
                        break;
                    default:
                        istream = sock.getInputStream();
                        break;
                }
                switch(mode) {
                    case FtpClientProtocol.MODE_ZLIB:
                        istream = new InflaterInputStream(istream);
                        break;
                    case FtpClientProtocol.MODE_STREAM:
                    default:
                        break;
                }
                int len;
                while (!cancelled && ((len = istream.read(buffer)) > 0)) {
                    ostream.write(buffer, 0, len);
                    amount += len;
                    buffer_size += len;
                    if (buffer_size >= BUFFER_SIZE) {
                        buffer_size = buffer_size % BUFFER_SIZE;
                        signalTransfered(amount);
                    }
                    yield();
                }
                ostream.flush();
            } catch (InterruptedIOException iioe) {
                if (!cancelled) {
                    log.error(iioe.getMessage(), iioe);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                log.debug("Closing inputstream");
                if (istream != null) {
                    istream.close();
                }
                if (!sock.isClosed()) {
                    try {
                        log.debug("Setting socket to 0 lingering");
                        sock.setSoLinger(true, 0);
                        sock.close();
                    } catch (SocketException e) {
                    }
                }
                signalTransferCompleted();
            }
        } catch (InterruptedIOException eiioe) {
            signalConnectionFailed(eiioe);
            if (!cancelled) {
                log.error(eiioe.getMessage(), eiioe);
            }
        } catch (Exception ee) {
            signalConnectionFailed(ee);
            log.error(ee.getMessage(), ee);
        } finally {
            try {
                log.debug("Closing server socket");
                server.close();
            } catch (IOException ex) {
            }
        }
        if (signalClosure == true && sock != null) {
            signalConnectionClosed(new ConnectionEvent(sock.getInetAddress(), sock.getPort()));
        }
    }
}
