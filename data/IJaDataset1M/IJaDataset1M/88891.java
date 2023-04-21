package org.hopto.pentaj.jexin.node;

import java.io.*;
import java.net.*;
import java.util.Map;
import javax.net.SocketFactory;
import org.apache.log4j.Logger;
import org.hopto.pentaj.jexin.stacktrace.StackFrame;

/**
 * Client for the TraceServer
 */
public class TraceClientImpl implements TraceClient {

    private static final Logger log = Logger.getLogger(TraceClientImpl.class);

    public static final int DEFAULT_READ_TIMEOUT = 5000;

    static final int PROCEED_ACTION = Integer.MIN_VALUE;

    static final int STACK_FRAME_START_ACTION = PROCEED_ACTION + 1;

    static final int STACK_FRAME_END_ACTION = PROCEED_ACTION + 2;

    static final int STACK_FRAME_EXCEPTION_ACTION = PROCEED_ACTION + 3;

    private final SocketFactory socketFactory;

    private NodeAddress address;

    private TraceClientObserver traceClientObserver;

    private Socket socket;

    private int readTimeout = DEFAULT_READ_TIMEOUT;

    private ObjectOutputStream out;

    private ObjectInputStream in;

    private Thread socketMonitorThread;

    public TraceClientImpl() {
        socketFactory = SocketFactory.getDefault();
    }

    public TraceClientImpl(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    /**
	 * @param readTimeout
	 *            the readTimeout to set
	 */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
	 * @see org.hopto.pentaj.jexin.node.TraceClient#connect(org.hopto.pentaj.jexin.node.NodeAddress,
	 *      org.hopto.pentaj.jexin.node.TraceClientObserver)
	 */
    public synchronized void connect(NodeAddress address, TraceClientObserver traceClientObserver) throws UnknownHostException, IOException {
        if (address == null || traceClientObserver == null) {
            throw new IllegalArgumentException("Address and traceClientObserver must not be null");
        }
        disconnect();
        socket = socketFactory.createSocket(address.getHost(), address.getPort());
        socket.setSoTimeout(readTimeout);
        this.address = address;
        this.traceClientObserver = traceClientObserver;
        getNodeConfig();
        socketMonitorThread = new Thread(new Runnable() {

            public void run() {
                monitorSocket();
            }
        });
        socketMonitorThread.setDaemon(true);
        socketMonitorThread.start();
    }

    /**
	 * @see org.hopto.pentaj.jexin.node.TraceClient#disconnect()
	 */
    public synchronized void disconnect() {
        if (socket != null) {
            traceClientObserver.disconnected();
            address = null;
            traceClientObserver = null;
            try {
                socket.close();
            } catch (IOException e) {
            }
            socket = null;
        }
    }

    /**
	 * For unit testing
	 */
    void waitForServerThread() {
        try {
            socketMonitorThread.join(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void monitorSocket() {
        try {
            socket.setSoTimeout(0);
            for (; ; ) {
                int action = in.readInt();
                switch(action) {
                    case STACK_FRAME_START_ACTION:
                        InjectableException exception = traceClientObserver.stackFrameStart(in.readLong(), (String) in.readObject(), new StackFrame((String) in.readObject(), (int[]) in.readObject()));
                        out.writeInt(exception == null ? PROCEED_ACTION : exception.getId());
                        out.flush();
                        break;
                    case STACK_FRAME_END_ACTION:
                        traceClientObserver.stackFrameReturn(in.readLong());
                        break;
                    case STACK_FRAME_EXCEPTION_ACTION:
                        traceClientObserver.stackFrameException(in.readLong(), (String) in.readObject());
                        break;
                    default:
                        throw new IOException("Received invalid action from server: " + action);
                }
            }
        } catch (Exception e) {
            synchronized (this) {
                if (socket != null) {
                    log.error("Error communicating with node " + address, e);
                    disconnect();
                }
            }
        }
    }

    private void getNodeConfig() throws IOException {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            String nodeName = (String) in.readObject();
            Map<Integer, String> injectableExceptions = (Map<Integer, String>) in.readObject();
            traceClientObserver.connected(nodeName, injectableExceptions);
        } catch (IOException e) {
            synchronized (this) {
                if (socket != null) {
                    log.error("Error communicating with node " + address, e);
                    disconnect();
                }
            }
            throw e;
        } catch (ClassNotFoundException e) {
            log.fatal(e);
            throw new RuntimeException(e);
        }
    }
}
