package de.byteholder.geoclipse.gpsd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This GPS processor connects to a local instance of GPSD and sets it in raw mode, which outputs
 * plain NMEA data. It then delegates the parsing work to an instance of NMEAProcessor.
 * 
 * @author Michael Kanis
 */
public class GPSDaemon extends NMEAProcessor {

    private static final int DEFAULT_PORT = 2947;

    private static final String DEFAULT_HOST = null;

    private Socket socket;

    public GPSDaemon() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public GPSDaemon(String host, int port) {
        super();
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            OutputStream out = socket.getOutputStream();
            out.write(new byte[] { 'r', '\n' });
            out.flush();
            InputStream input = socket.getInputStream();
            setStream(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.run();
    }
}
