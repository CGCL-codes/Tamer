package net.sf.jncu.cdil;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

/**
 * CD packet layer.
 * 
 * @author Moshe
 */
public abstract class CDPacketLayer<P extends CDPacket> extends Thread {

    protected final CDPipe<P> pipe;

    private boolean running;

    private int timeout;

    private final Timer timer = new Timer();

    private CDTimeout timeoutTask;

    /** List of packet listeners. */
    private final Collection<CDPacketListener<P>> listeners = new ArrayList<CDPacketListener<P>>();

    /**
	 * Constructs a new packet layer.
	 * 
	 * @param pipe
	 *            the pipe.
	 */
    public CDPacketLayer(CDPipe<P> pipe) {
        super();
        setName("CDPacketLayer-" + getId());
        this.pipe = pipe;
        setTimeout(30);
    }

    /**
	 * Get the output stream for packets.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    protected abstract OutputStream getOutput() throws IOException;

    /**
	 * Get the input stream for packets.
	 * 
	 * @return the stream.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    protected abstract InputStream getInput() throws IOException;

    /**
	 * Is finished?
	 * 
	 * @return finished.
	 */
    protected boolean isFinished() {
        return !running;
    }

    /**
	 * Add a packet listener.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
    public void addPacketListener(CDPacketListener<P> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
	 * Remove a packet listener.
	 * 
	 * @param listener
	 *            the listener to remove.
	 */
    public void removePacketListener(CDPacketListener<P> listener) {
        listeners.remove(listener);
    }

    /**
	 * Notify all the listeners that a packet has been received.
	 * 
	 * @param packet
	 *            the received packet.
	 */
    protected void firePacketReceived(P packet) {
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetReceived(packet);
        }
    }

    /**
	 * Notify all the listeners that a packet has been sent.
	 * 
	 * @param packet
	 *            the sent packet.
	 */
    protected void firePacketSent(P packet) {
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetSent(packet);
        }
    }

    /**
	 * Notify all the listeners that a packet has been acknowledged.
	 * 
	 * @param packet
	 *            the sent packet.
	 */
    protected void firePacketAcknowledged(P packet) {
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetAcknowledged(packet);
        }
    }

    /**
	 * Notify all the listeners that no more packets will be available.
	 */
    protected void firePacketEOF() {
        Collection<CDPacketListener<P>> listenersCopy = new ArrayList<CDPacketListener<P>>(listeners);
        for (CDPacketListener<P> listener : listenersCopy) {
            listener.packetEOF();
        }
    }

    /**
	 * Close the layer and release resources.
	 */
    public void close() {
        running = false;
        timer.cancel();
        listeners.clear();
    }

    /**
	 * Restart the timeout.
	 */
    protected void restartTimeout() {
        if (timeoutTask != null) timeoutTask.cancel();
        if (pipe == null) return;
        this.timeoutTask = new CDTimeout(pipe);
        timer.schedule(timeoutTask, timeout * 1000L);
    }

    /**
	 * Receive a packet.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @return the packet.
	 */
    public P receive() throws IOException {
        if (isFinished()) return null;
        P packet = null;
        byte[] payload;
        try {
            payload = read();
            packet = createPacket(payload);
            if (packet != null) {
                restartTimeout();
                firePacketReceived(packet);
            }
        } catch (EOFException eofe) {
            firePacketEOF();
        }
        return packet;
    }

    /**
	 * Receive a packet payload.
	 * 
	 * @return the payload - {@code null} otherwise.
	 * @throws EOFException
	 *             if end of stream is reached.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    protected abstract byte[] read() throws EOFException, IOException;

    /**
	 * Read a byte from the default input.
	 * 
	 * @return the byte value.
	 * @throws EOFException
	 *             if end of stream is reached.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    protected int readByte() throws IOException {
        return readByte(getInput());
    }

    /**
	 * Read a byte.
	 * 
	 * @param in
	 *            the input.
	 * @return the byte value.
	 * @throws EOFException
	 *             if end of stream is reached.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    protected int readByte(InputStream in) throws IOException {
        int b;
        try {
            b = in.read();
        } catch (IOException ioe) {
            if ((in.available() == 0) && (in instanceof PipedInputStream)) throw new EOFException(ioe.getMessage());
            throw ioe;
        }
        if (b == -1) throw new EOFException();
        return b;
    }

    /**
	 * Create a link packet, and decode.
	 * 
	 * @param payload
	 *            the payload.
	 * @return the packet.
	 */
    protected abstract P createPacket(byte[] payload);

    /**
	 * Listen for incoming packets until no more packets are available.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    public void listen() throws IOException {
        P packet;
        do {
            packet = receive();
            yield();
        } while (packet != null);
    }

    /**
	 * Send a packet.
	 * 
	 * @param packet
	 *            the packet.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws TimeoutException
	 *             if a timeout occurs.
	 */
    public void send(P packet) throws IOException, TimeoutException {
        if (isFinished()) return;
        byte[] payload = packet.serialize();
        try {
            write(payload);
            firePacketSent(packet);
        } catch (EOFException eof) {
            firePacketEOF();
        }
    }

    /**
	 * Send a packet.
	 * 
	 * @param payload
	 *            the payload.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    protected final void write(byte[] payload) throws IOException {
        write(payload, 0, payload.length);
    }

    /**
	 * Send a packet.
	 * 
	 * @param payload
	 *            the payload.
	 * @param offset
	 *            the frame offset.
	 * @param length
	 *            the frame length.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
    protected void write(byte[] payload, int offset, int length) throws IOException {
        OutputStream out = getOutput();
        if (out == null) return;
        out.write(payload, offset, length);
    }

    /**
	 * Sets the timeout period for CD_Read and CD_Write calls in a pipe.<br>
	 * <tt>DIL_Error CD_SetTimeout(CD_Handle pipe, long timeoutInSecs)</tt>
	 * <p>
	 * When the CDIL pipe is created, it is initialised with a default timeout
	 * period of 30 seconds. This timeout period is used to control
	 * <tt>CD_Read</tt> and <tt>CD_Write</tt> calls (and, indirectly, any
	 * flushing of outgoing data). Timeout values are specified on a per-pipe
	 * basis.<br>
	 * For <tt>CD_Read</tt>, if the requested number of bytes are not available
	 * after the timeout period, a <tt>kCD_Timeout</tt> error is returned and no
	 * bytes will be transferred. For <tt>CD_Write</tt>, if no data can be sent
	 * after the timeout period, a <tt>kCD_Timeout</tt> error is returned.<br>
	 * The timeout does not occur, if the data is presently being transferred.
	 * That is, a long operation does not fail due to a timeout. Note that an
	 * attempt is made to send data even if the timeout is set to zero seconds.
	 */
    public void setTimeout(int timeoutInSecs) {
        this.timeout = timeoutInSecs;
        restartTimeout();
    }

    /**
	 * Get the timeout period.
	 * 
	 * @return the timeout in seconds.
	 */
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void run() {
        running = true;
        do {
            try {
                listen();
            } catch (EOFException eofe) {
                firePacketEOF();
            } catch (IOException ioe) {
                if (isConnected()) {
                    ioe.printStackTrace();
                }
            }
        } while (running && isConnected());
        running = false;
    }

    /**
	 * Is the pipe connected?
	 * 
	 * @return {@code true} if connecting or connected.
	 */
    protected boolean isConnected() {
        if (pipe == null) return true;
        return pipe.isConnected();
    }
}
