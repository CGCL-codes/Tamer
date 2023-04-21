package fcp.sessions;

import java.io.*;
import java.net.*;
import java.util.logging.*;
import fcp.*;

/**
 * A base class representing a single transaction with a Freenet node over FCP.
 *
 * @author Adam Thomason
 **/
public abstract class FCPSession extends Thread {

    public static final int KW_UNKNOWN = -1;

    public static final int KW_NODE_HELLO = 1;

    public static final int KW_PROTOCOL = 2;

    public static final int KW_NODE = 3;

    public static final int KW_END_MESSAGE = 4;

    public static final int KW_URI_ERROR = 5;

    public static final int KW_RESTARTED = 6;

    public static final int KW_DATA_NOT_FOUND = 7;

    public static final int KW_ROUTE_NOT_FOUND = 8;

    public static final int KW_DATA_FOUND = 9;

    public static final int KW_DATA_LENGTH = 10;

    public static final int KW_METADATA_LENGTH = 11;

    public static final int KW_DATA_CHUNK = 12;

    public static final int KW_LENGTH = 13;

    public static final int KW_DATA = 14;

    public static final int KW_KEY_COLLISION = 15;

    public static final int KW_SIZE_ERROR = 16;

    public static final int KW_PENDING = 17;

    public static final int KW_URI = 18;

    public static final int KW_PUBLIC_KEY = 19;

    public static final int KW_PRIVATE_KEY = 20;

    public static final int KW_SUCCESS = 21;

    public static final int KW_FORMAT_ERROR = 22;

    public static final int KW_FAILED = 23;

    public static final int KW_REASON = 24;

    public static final int KW_TIMEOUT = 25;

    /**
     * Protected empty constructor.
     **/
    protected FCPSession() {
        attempts = 0;
    }

    /**
     * Creates a new FCP session for use in a single transaction.
     *
     * @param host the location of the Freenet host to connect to.
     * @param port the listening FCP port on the Freenet host.
     * @param tcpTimeout the number of milliseconds to wait on stalled
     *     connections.
     **/
    public FCPSession(InetAddress host, int port, int tcpTimeout) {
        this();
        this.host = host;
        this.port = port;
        this.tcpTimeout = tcpTimeout;
        log = Logger.getLogger("fcp.FCPSession");
        LogManager.getLogManager().addLogger(log);
        success = false;
        cause = null;
    }

    /**
     * Sets a listener on this session.  The listener will be notify'ed when
     * the request completes.
     *
     * @param listener the new listener.
     **/
    public void setListener(FCPListener listener) {
        this.listener = listener;
    }

    /**
     * Sets a passback for the listener on this session.  When the listener is
     * notify'ed, this passback object will be included.
     *
     * @param passback the new passback.
     **/
    public void setPassback(Object passback) {
        this.passback = passback;
    }

    /**
     * Sets up the FCP socket and I/O streams.  This session and presentation
     * identifier bytes are sent.  After this method is called, the client may
     * send the FCP instruction message.
     **/
    protected void init() throws Exception {
        attempts++;
        log.fine("Connecting to " + host + ":" + port + ".");
        socket = new Socket(host, port);
        socket.setSoTimeout(tcpTimeout);
        inStream = new BufferedInputStream(socket.getInputStream());
        outStream = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
        log.fine("Sending handshake sequence.");
        byte[] handshakeBytes = { 0x00, 0x00, 0x00, 0x02 };
        outStream.write(handshakeBytes, 0, handshakeBytes.length);
        outStream.flush();
        outWriter = new PrintWriter(new OutputStreamWriter(outStream));
    }

    /**
     * Performs cleanup and notification tasks.
     **/
    protected void finish() {
        try {
            if (outWriter != null) {
                outWriter.close();
            }
            if (outStream != null) {
                outStream.close();
            }
            if (inStream != null) {
                inStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
        if (listener != null) {
            listener.notify(this, passback);
        }
    }

    /**
     * Reads the next line from the input stream.
     *
     * @return a String containing all characters up to the next newline.
     *     The newline is not included.
     **/
    protected String nextLine() throws IOException {
        StringBuffer buf = new StringBuffer();
        int b;
        while ((b = inStream.read()) != '\n' && b != -1) {
            buf.append((char) b);
        }
        return buf.toString();
    }

    /**
     * Returns an integer constant representing the type of a reply keyword
     * received from a node.
     **/
    public static int getReplyKeywordType(String keyword) {
        if (keyword.equals("NodeHello")) {
            return KW_NODE_HELLO;
        } else if (keyword.equals("Protocol")) {
            return KW_PROTOCOL;
        } else if (keyword.equals("Node")) {
            return KW_NODE;
        } else if (keyword.equals("EndMessage")) {
            return KW_END_MESSAGE;
        } else if (keyword.equals("URIError")) {
            return KW_URI_ERROR;
        } else if (keyword.equals("Restarted")) {
            return KW_RESTARTED;
        } else if (keyword.equals("DataNotFound")) {
            return KW_DATA_NOT_FOUND;
        } else if (keyword.equals("RouteNotFound")) {
            return KW_ROUTE_NOT_FOUND;
        } else if (keyword.equals("DataFound")) {
            return KW_DATA_FOUND;
        } else if (keyword.equals("DataLength")) {
            return KW_DATA_LENGTH;
        } else if (keyword.equals("MetadataLength")) {
            return KW_METADATA_LENGTH;
        } else if (keyword.equals("DataChunk")) {
            return KW_DATA_CHUNK;
        } else if (keyword.equals("Length")) {
            return KW_LENGTH;
        } else if (keyword.equals("Data")) {
            return KW_DATA;
        } else if (keyword.equals("KeyCollision")) {
            return KW_KEY_COLLISION;
        } else if (keyword.equals("SizeError")) {
            return KW_SIZE_ERROR;
        } else if (keyword.equals("Pending")) {
            return KW_PENDING;
        } else if (keyword.equals("URI")) {
            return KW_URI;
        } else if (keyword.equals("PublicKey")) {
            return KW_PUBLIC_KEY;
        } else if (keyword.equals("PrivateKey")) {
            return KW_PRIVATE_KEY;
        } else if (keyword.equals("Success")) {
            return KW_SUCCESS;
        } else if (keyword.equals("FormatError")) {
            return KW_FORMAT_ERROR;
        } else if (keyword.equals("Failed")) {
            return KW_FAILED;
        } else if (keyword.equals("Reason")) {
            return KW_REASON;
        } else if (keyword.equals("Timeout")) {
            return KW_TIMEOUT;
        } else {
            return KW_UNKNOWN;
        }
    }

    public static String getReplyKeyword(int type) {
        switch(type) {
            case KW_UNKNOWN:
                return "Unknown";
            case KW_NODE_HELLO:
                return "NodeHello";
            case KW_PROTOCOL:
                return "Protocol";
            case KW_NODE:
                return "Node";
            case KW_END_MESSAGE:
                return "EndMessage";
            case KW_URI_ERROR:
                return "URIError";
            case KW_RESTARTED:
                return "Restarted";
            case KW_DATA_NOT_FOUND:
                return "DataNotFound";
            case KW_ROUTE_NOT_FOUND:
                return "RouteNotFound";
            case KW_DATA_FOUND:
                return "DataFound";
            case KW_DATA_LENGTH:
                return "DataLength";
            case KW_METADATA_LENGTH:
                return "MetadataLength";
            case KW_DATA_CHUNK:
                return "DataChunk";
            case KW_LENGTH:
                return "Length";
            case KW_DATA:
                return "Data";
            case KW_KEY_COLLISION:
                return "KeyCollision";
            case KW_SIZE_ERROR:
                return "SizeError";
            case KW_PENDING:
                return "Pending";
            case KW_URI:
                return "URI";
            case KW_PUBLIC_KEY:
                return "PublicKey";
            case KW_PRIVATE_KEY:
                return "PrivateKey";
            case KW_SUCCESS:
                return "Success";
            case KW_FORMAT_ERROR:
                return "FormatError";
            case KW_FAILED:
                return "Failed";
            case KW_REASON:
                return "Reason";
            case KW_TIMEOUT:
                return "Timeout";
        }
        return "Unknown";
    }

    /**
     * Sets the success status of the session.
     *
     * @param success the new status.
     **/
    protected void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return true if the session was concluded successfully.  Note that
     *     the definition of success depends on the type of session.  However,
     *     in general, a session is successful if it completes as desired
     *     <i>or</i> it is fairly certain that repeating exactly the same
     *     request would return the same error result.  As such, some
     *     "successes" may not necessarily indicated that the desired result
     *     was achieved.  See the documentation for the particular session
     *     types to see what defines success in each case.
     **/
    public boolean getSuccess() {
        return success;
    }

    /**
     * Sets the error explanation of the session.
     *
     * @param success the new status.
     **/
    protected void setCause(Throwable cause) {
        this.cause = cause;
    }

    /**
     * @return the reason for the session's failure.
     **/
    public Throwable getCause() {
        return cause;
    }

    /**
     * @return the number of times this session has been attempted.
     **/
    public int getAttempts() {
        return attempts;
    }

    /**
     * True if the session was concluded successfully.
     **/
    protected boolean success;

    /**
     * Reason for the session's failure.
     **/
    protected Throwable cause;

    /**
     * The TCP connection to the Freenet node.
     **/
    protected Socket socket;

    /**
     * Byte input from the FCP node.  Mixing use of inStream and inTokenizer
     * is permitted.
     **/
    protected InputStream inStream;

    /**
     * Byte output to the FCP node.  Mixing use of outStream and outWriter
     * is permitted.
     **/
    protected PrintStream outStream;

    /**
     * Character output to the FCP node.  Mixing use of outStream and outWriter
     * is permitted.
     **/
    protected PrintWriter outWriter;

    protected InetAddress host;

    protected int port;

    protected int tcpTimeout;

    protected Logger log;

    protected FCPListener listener;

    protected Object passback;

    /**
     * The number of times this session has been attempted.
     **/
    protected int attempts;
}
