package com.grt192.networking.spot;

import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import com.grt192.networking.GRTSocket;
import com.grt192.prototyper.PrototyperUtils;

/**
 * An <code>InRadio</code> is a read-only socket to a corresponding
 * <code>OutboundConnection</code> on a remote write-only
 * <code>RadioServerClient</code>
 * 
 * @author ajc
 */
public class InRadio extends EventSocket implements GRTSocket {

    private final String host;

    private final int port;

    private DataInputStream in;

    private boolean open;

    /**
	 * Opens an inbound read only connection from a prototyped host with a
	 * provided prototype on provided port.
	 * 
	 * @param prototype
	 *            prototype of protyped host
	 * @param port
	 *            port to listen for
	 * @param debug
	 *            true to print debug messages
	 * @return
	 */
    public static InRadio toPrototype(int prototype, int port, boolean debug) {
        String host = PrototyperUtils.getAddress(prototype);
        return new InRadio(host, port, debug);
    }

    /**
	 * Opens an inbound read only connection to a provided host
	 * 
	 * @param host
	 *            address of host to listen to
	 * @param port
	 *            port to listen on
	 * @param debug
	 *            true to print debug messages
	 */
    public InRadio(String host, int port, boolean debug) {
        this.host = host;
        this.port = port;
        this.debug = debug;
        this.open = false;
    }

    /**
	 * Opens and inbound read only connection to a provided host
	 * 
	 * @param host
	 *            address of host to listen to
	 * @param port
	 *            port to listen on
	 */
    public InRadio(String host, int port) {
        this(host, port, false);
    }

    public void run() {
        running = true;
        while (running) {
            poll();
        }
    }

    /**
	 * Read data from the other host. This simply hangs and doesn't throw an
	 * exception when the remote host is down.
	 */
    public void poll() {
        if (!isConnected()) {
            connect();
        }
        try {
            debug("Listening for data...");
            String data = in.readUTF();
            if (data != null) {
                debug("Data in: " + data);
                notifyDataReceived(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
            connect();
        }
    }

    /** No action: this thing is only input **/
    public void sendData(String data) {
    }

    /** True if the RadioStream is open */
    public boolean isConnected() {
        return in != null && open;
    }

    /** Try to open the data output stream */
    public void connect() {
        String url = "radiostream://" + host + ":" + port;
        try {
            in = Connector.openDataInputStream(url);
            open = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Disconnects this instance, destroying it **/
    public void disconnect() {
        try {
            debug("disconnecting");
            open = false;
            in.close();
            notifyDisconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
