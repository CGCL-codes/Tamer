package org.freelords.server.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import org.freelords.events.FreelordsEvent;
import org.freelords.player.PlayerId;

/**
 * A class for sending out network packets.</br >
 * Importantly buffers events per player (useful for when you have multiple players
 * on the one computer).<br />
 * flush() MUST be called for any data to be sent.
 * @author jamesandrews
 */
public class NetworkOut {

    /** The name for logging purposes
	 */
    private String name;

    /** Where NetworkPackets are actually sent
	 */
    private ObjectOutputStream outgoing;

    /** Has this been closed
	 */
    private volatile boolean closed;

    /**
	 * Creates a Freelords Packet Network outputstream
	 * @param name The name for logging purposes
	 * @param out  The outputstream to send data through
	 * @throws IOException
	 */
    public NetworkOut(String name, OutputStream out) throws IOException {
        if (out == null) {
            throw new NullPointerException("Output stream can not be null.");
        }
        this.name = name;
        this.outgoing = new ObjectOutputStream(out);
    }

    public synchronized void sendEvents(PlayerId playerId, FreelordsEvent... events) {
        if (events == null) {
            throw new NullPointerException("events can not be null");
        }
        try {
            for (FreelordsEvent fe : events) {
                NetworkPacket np = new NetworkPacket(playerId, fe);
                outgoing.writeObject(np);
            }
            outgoing.reset();
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    public synchronized void flush() throws IOException {
        outgoing.flush();
    }

    /**
	 * Closes the output, the object can not be re-opened
	 */
    public void close() {
        try {
            closed = true;
            outgoing.close();
        } catch (Exception e) {
        }
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public String toString() {
        return name + " - NetworkIn";
    }
}
