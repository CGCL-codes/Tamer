package quamj.iiop_rsvpd.w2k_rsvp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InterruptedIOException;
import java.io.FileDescriptor;
import java.io.ByteArrayOutputStream;
import java.net.*;

/**
 *
 */
public class rsvpSocket extends SocketImpl {

    int timeout;

    private boolean shut_rd = false;

    private boolean shut_wr = false;

    private rsvpSocketInputStream rsvpsocketInputStream = null;

    private InetAddress myaddress;

    private static org.apache.log4j.Category logCategory = org.apache.log4j.Category.getInstance(rsvpSocket.class.getName());

    /**
     * Load JRSVP library into runtime.
     */
    static {
        try {
            System.loadLibrary("Jrsvp");
        } catch (UnsatisfiedLinkError x) {
            logCategory.debug("JRSVP native not found (JRSVP.DLL)");
        }
        initProto();
        logCategory.debug("[" + logCategory.getName() + "] created OK!");
    }

    /**
     * Creates a socket with a boolean that specifies whether this
     * is a stream socket (true) or an unconnected UDP socket (false).
     */
    protected synchronized void create(boolean stream) throws IOException {
        fd = new FileDescriptor();
        try {
            socketCreate(stream);
        } catch (IOException e) {
            logCategory.debug(e.getMessage());
            throw e;
        }
        logCategory.debug("Socket created");
    }

    /**
     * Creates a socket and connects it to the specified port on
     * the specified host.
     * @param host the specified host
     * @param port the specified port
     */
    public void connecttest(String host, int port) {
        logCategory.debug("Test connection");
        try {
            connect(host, port);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void connect(String host, int port) throws UnknownHostException, IOException {
        logCategory.debug("Connection with host & port");
        IOException pending = null;
        try {
            InetAddress address = InetAddress.getByName(host);
            try {
                connectToAddress(address, port);
                logCategory.debug("Socket connected");
                return;
            } catch (IOException e) {
                pending = e;
                logCategory.error(e.getMessage());
            }
        } catch (UnknownHostException e) {
            pending = e;
            logCategory.error(e.getMessage());
        }
        close();
        throw pending;
    }

    /**
     * Creates a socket and connects it to the specified address on
     * the specified port.
     * @param address the address
     * @param port the specified port
     */
    protected void connect(InetAddress address, int port) throws IOException {
        logCategory.debug("Connect with address [" + address.toString() + "] port [" + new Integer(port).toString() + "]");
        this.port = port;
        this.address = address;
        try {
            connectToAddress(address, port);
            logCategory.debug("Socket connected");
            return;
        } catch (IOException e) {
            close();
            logCategory.error(e.getMessage());
            throw e;
        }
    }

    private void connectToAddress(InetAddress address, int port) throws IOException {
        doConnect(address, port);
    }

    public void setOption(int opt, Object val) throws SocketException {
        boolean on = true;
        switch(opt) {
            case SO_LINGER:
                logCategory.debug("Set Option SO_LINGER");
                if (val == null || (!(val instanceof Integer) && !(val instanceof Boolean))) {
                    logCategory.error("Bad parameter for SO_LINGER");
                    throw new SocketException("Bad parameter for SO_LINGER");
                }
                if (val instanceof Boolean) on = false;
                break;
            case SO_TIMEOUT:
                logCategory.debug("Set Option SO_TIMEOUT");
                if (val == null || (!(val instanceof Integer))) {
                    logCategory.error("Bad parameter for SO_TIMEOUT");
                    throw new SocketException("Bad parameter for SO_TIMEOUT");
                }
                int tmp = ((Integer) val).intValue();
                if (tmp < 0) {
                    logCategory.error("Timeout < 0");
                    throw new IllegalArgumentException("timeout < 0");
                }
                timeout = tmp;
                return;
            case SO_BINDADDR:
                logCategory.error("Set Option SO_BINDADDR [Can't rebind socket]");
                throw new SocketException("Cannot re-bind socket");
            case TCP_NODELAY:
                logCategory.debug("Set Option TCP_NODELAY");
                if (val == null || !(val instanceof Boolean)) {
                    logCategory.error("Bad Parameter for TCP_NODELAY");
                    throw new SocketException("bad parameter for TCP_NODELAY");
                }
                on = ((Boolean) val).booleanValue();
                break;
            case SO_SNDBUF:
            case SO_RCVBUF:
                logCategory.debug("Set Option SO_SNDBUF or SO_RCVBUF");
                if (val == null || !(val instanceof Integer) || !(((Integer) val).intValue() > 0)) {
                    logCategory.error("Bad Parameter for SO_SNDBUF or SO_RCVBUF");
                    throw new SocketException("bad parameter for SO_SNDBUF or SO_RCVBUF");
                }
                break;
            case SO_REUSEADDR:
                logCategory.debug("Set Option SO_REUSEADDR");
                if (val == null || !(val instanceof Integer)) {
                    logCategory.error("Bad parameter for SO_REUSEADDR");
                    throw new SocketException("bad argument for SO_REUSEADDR");
                }
                break;
            case SO_KEEPALIVE:
                logCategory.debug("Set Option SO_KEEPALIVE");
                if (val == null || !(val instanceof Boolean)) {
                    logCategory.error("Bad Parameter for SO_KEEPALIVE");
                    throw new SocketException("bad parameter for SO_KEEPALIVE");
                }
                on = ((Boolean) val).booleanValue();
                break;
            default:
                logCategory.error("Unrecognized TCP Option [" + opt + "]");
                throw new SocketException("unrecognized TCP option: " + opt);
        }
        socketSetOption(opt, on, val);
    }

    public Object getOption(int opt) throws SocketException {
        if (opt == SO_TIMEOUT) return new Integer(timeout);
        int ret;
        try {
            ret = socketGetOption(opt);
        } catch (SocketException e) {
            logCategory.error(e.getMessage());
            throw e;
        }
        switch(opt) {
            case TCP_NODELAY:
                logCategory.debug("Get Option TCP_NODELAY");
                return (ret == -1) ? new Boolean(false) : new Boolean(true);
            case SO_LINGER:
                logCategory.debug("Get Option SO_LINGER");
                return (ret == -1) ? new Boolean(false) : (Object) (new Integer(ret));
            case SO_BINDADDR:
                logCategory.debug("Get Option SO_BINDADDR Return is " + new Integer(ret).toString());
                String addr = ((ret >>> 24) & 0xFF) + "." + ((ret >>> 16) & 0xFF) + "." + ((ret >>> 8) & 0xFF) + "." + ((ret >>> 0) & 0xFF);
                try {
                    myaddress = InetAddress.getByName(addr);
                } catch (UnknownHostException ex) {
                    logCategory.error("Unknown Host Exception");
                }
                return myaddress;
            case SO_SNDBUF:
            case SO_RCVBUF:
                logCategory.debug("Get Option SO_SNDBUF, SO_RCVBUF");
                return new Integer(ret);
            case SO_KEEPALIVE:
                logCategory.debug("Get Option SO_KEEPALIVE");
                return (ret == -1) ? new Boolean(false) : new Boolean(true);
            default:
                return null;
        }
    }

    private void doConnect(InetAddress address, int port) throws IOException {
        IOException pending = null;
        for (int i = 0; i < 3; i++) {
            try {
                socketConnect(address, port);
                logCategory.debug("Socket connected");
                return;
            } catch (ProtocolException e) {
                close();
                fd = new FileDescriptor();
                socketCreate(true);
                pending = e;
                logCategory.error(e.getMessage());
            } catch (IOException e) {
                logCategory.error(e.getMessage());
                close();
                throw e;
            }
        }
        close();
        throw pending;
    }

    protected synchronized void bind(InetAddress address, int lport) throws IOException {
        int post;
        byte[] addr = address.getAddress();
        logCategory.debug((addr[0] & 0xFF) + "." + (addr[1] & 0xFF) + "." + (addr[2] & 0xFF) + "." + (addr[3] & 0xFF) + ".");
        logCategory.debug("Socket bind Address[" + address.toString() + "] Port[" + new Integer(port).toString() + "]");
        try {
            socketBind(address, lport);
        } catch (IOException e) {
            logCategory.error(e.getMessage());
            throw e;
        }
    }

    protected synchronized void listen(int count) throws IOException {
        try {
            socketListen(count);
        } catch (IOException e) {
            logCategory.error(e.getMessage());
            throw e;
        }
        logCategory.debug("Socket listen");
    }

    protected synchronized void accept(SocketImpl s) throws IOException {
        try {
            socketAccept(s);
        } catch (IOException e) {
            logCategory.error(e.getMessage());
            throw e;
        }
        logCategory.debug("Socket is accepting");
    }

    protected synchronized InputStream getInputStream() throws IOException {
        if (fd == null) {
            logCategory.error("Socket is closed");
            throw new IOException("Socket Closed");
        }
        if (shut_rd) {
            logCategory.error("Socket input is down");
            throw new IOException("Socket input is shutdown");
        }
        if (rsvpsocketInputStream == null) {
            rsvpsocketInputStream = new rsvpSocketInputStream(this);
        }
        return rsvpsocketInputStream;
    }

    protected synchronized OutputStream getOutputStream() throws IOException {
        logCategory.debug(" ");
        if (fd == null) {
            logCategory.error("Socked is closed");
            throw new IOException("Socket Closed");
        }
        if (shut_wr) {
            logCategory.error("Socket output is down");
            throw new IOException("Socket output is shutdown");
        }
        return new rsvpSocketOutputStream(this);
    }

    protected synchronized int available() throws IOException {
        logCategory.debug(" ");
        if (fd == null) {
            logCategory.error("Stream closed");
            throw new IOException("Stream closed.");
        }
        return socketAvailable();
    }

    protected void close() throws IOException {
        logCategory.debug(" ");
        if (fd != null) {
            try {
                socketClose();
            } catch (IOException e) {
                logCategory.error(e.getMessage());
                throw e;
            }
            fd = null;
        }
    }

    protected FileDescriptor getFileDescriptor() {
        return fd;
    }

    protected void shutdownInput() throws IOException {
        if (fd != null) {
            try {
                socketShutdown(SHUT_RD);
            } catch (IOException e) {
                logCategory.error(e.getMessage());
                throw e;
            }
            if (rsvpsocketInputStream != null) {
                rsvpsocketInputStream.setEOF(true);
            }
            shut_rd = true;
        }
    }

    protected void shutdownOutput() throws IOException {
        logCategory.debug(" ");
        if (fd != null) {
            try {
                socketShutdown(SHUT_WR);
            } catch (IOException e) {
                logCategory.error(e.getMessage());
                throw e;
            }
            shut_wr = true;
        }
    }

    protected void finalize() throws IOException {
        try {
            close();
        } catch (IOException e) {
            logCategory.error(e.getMessage());
            throw e;
        }
    }

    private native void socketCreate(boolean isServer) throws IOException;

    private native void socketConnect(InetAddress address, int port) throws IOException;

    private native void socketBind(InetAddress address, int port) throws IOException;

    private native void socketListen(int count) throws IOException;

    private native void socketAccept(SocketImpl s) throws IOException;

    private native int socketAvailable() throws IOException;

    private native void socketClose() throws IOException;

    private native void socketShutdown(int howto) throws IOException;

    private static native void initProto();

    private native void socketSetOption(int cmd, boolean on, Object value) throws SocketException;

    private native int socketGetOption(int opt) throws SocketException;

    public static final int SHUT_RD = 0;

    public static final int SHUT_WR = 1;
}
