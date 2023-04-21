package br.usjt.smartzap.java.net;

import com.sun.javafx.runtime.Entry;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author vbonifacio
 */
public class SocketClient extends GenericSocket implements SocketListener {

    public static String DEFAULT_HOST = "localhost";

    public String host;

    private SocketListener fxListener;

    public SocketClient(SocketListener fxListener, String host, Integer port, Integer debugFlags) {
        super(port, debugFlags);
        this.host = host;
        this.fxListener = fxListener;
    }

    public SocketClient(SocketListener fxListener) {
        this(fxListener, DEFAULT_HOST, DEFAULT_PORT, DEBUG_NONE);
    }

    public SocketClient(SocketListener fxListener, String host, Integer port) {
        this(fxListener, host, port, DEBUG_NONE);
    }

    @Override
    public void onMessage(final String line) {
        Entry.deferAction(new Runnable() {

            @Override
            public void run() {
                fxListener.onMessage(line);
            }
        });
    }

    @Override
    public void onClosedStatus(final Boolean isClosed) {
        Entry.deferAction(new Runnable() {

            @Override
            public void run() {
                fxListener.onClosedStatus(isClosed);
            }
        });
    }

    @Override
    public void initSocketConnection() throws SocketException {
        try {
            socketConnection = new Socket();
            socketConnection.setReuseAddress(true);
            socketConnection.connect(new InetSocketAddress(host, port));
            if (debugFlagIsSet(DEBUG_STATUS)) {
                System.out.println("Conectado ao host -> " + host + " na porta -> " + port);
            }
        } catch (Exception e) {
            if (debugFlagIsSet(DEBUG_EXCEPTIONS)) {
                e.printStackTrace();
            }
            throw new SocketException();
        }
    }

    @Override
    public void closeAdditionalSockets() {
    }
}
