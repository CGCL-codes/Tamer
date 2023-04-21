package gnu.java.nio;

import gnu.java.net.PlainDatagramSocketImpl;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.spi.SelectorProvider;

/**
 * @author Michael Koch
 */
public final class DatagramChannelImpl extends DatagramChannel {

    private NIODatagramSocket socket;

    /**
   * Indicates whether this channel initiated whatever operation
   * is being invoked on our datagram socket.
   */
    private boolean inChannelOperation;

    /**
   * Indicates whether our datagram socket should ignore whether
   * we are set to non-blocking mode. Certain operations on our
   * socket throw an <code>IllegalBlockingModeException</code> if
   * we are in non-blocking mode, <i>except</i> if the operation
   * is initiated by us.
   */
    public final boolean isInChannelOperation() {
        return inChannelOperation;
    }

    /**
   * Sets our indicator of whether we are initiating an I/O operation
   * on our socket.
   */
    public final void setInChannelOperation(boolean b) {
        inChannelOperation = b;
    }

    protected DatagramChannelImpl(SelectorProvider provider) throws IOException {
        super(provider);
        socket = new NIODatagramSocket(new PlainDatagramSocketImpl(), this);
        configureBlocking(true);
    }

    public DatagramSocket socket() {
        return socket;
    }

    protected void implCloseSelectableChannel() throws IOException {
        socket.close();
    }

    protected void implConfigureBlocking(boolean blocking) throws IOException {
        socket.setSoTimeout(blocking ? 0 : NIOConstants.DEFAULT_TIMEOUT);
    }

    public DatagramChannel connect(SocketAddress remote) throws IOException {
        if (!isOpen()) throw new ClosedChannelException();
        socket.connect(remote);
        return this;
    }

    public DatagramChannel disconnect() throws IOException {
        socket.disconnect();
        return this;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public int write(ByteBuffer src) throws IOException {
        if (!isConnected()) throw new NotYetConnectedException();
        return send(src, socket.getRemoteSocketAddress());
    }

    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (!isConnected()) throw new NotYetConnectedException();
        if ((offset < 0) || (offset > srcs.length) || (length < 0) || (length > (srcs.length - offset))) throw new IndexOutOfBoundsException();
        long result = 0;
        for (int index = offset; index < offset + length; index++) result += write(srcs[index]);
        return result;
    }

    public int read(ByteBuffer dst) throws IOException {
        if (!isConnected()) throw new NotYetConnectedException();
        int remaining = dst.remaining();
        receive(dst);
        return remaining - dst.remaining();
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        if (!isConnected()) throw new NotYetConnectedException();
        if ((offset < 0) || (offset > dsts.length) || (length < 0) || (length > (dsts.length - offset))) throw new IndexOutOfBoundsException();
        long result = 0;
        for (int index = offset; index < offset + length; index++) result += read(dsts[index]);
        return result;
    }

    public SocketAddress receive(ByteBuffer dst) throws IOException {
        if (!isOpen()) throw new ClosedChannelException();
        try {
            DatagramPacket packet;
            int len = dst.remaining();
            if (dst.hasArray()) {
                packet = new DatagramPacket(dst.array(), dst.arrayOffset() + dst.position(), len);
            } else {
                packet = new DatagramPacket(new byte[len], len);
            }
            boolean completed = false;
            try {
                begin();
                setInChannelOperation(true);
                socket.receive(packet);
                completed = true;
            } finally {
                end(completed);
                setInChannelOperation(false);
            }
            if (!dst.hasArray()) {
                dst.put(packet.getData(), packet.getOffset(), packet.getLength());
            } else {
                dst.position(dst.position() + packet.getLength());
            }
            return packet.getSocketAddress();
        } catch (SocketTimeoutException e) {
            return null;
        }
    }

    public int send(ByteBuffer src, SocketAddress target) throws IOException {
        if (!isOpen()) throw new ClosedChannelException();
        if (target instanceof InetSocketAddress && ((InetSocketAddress) target).isUnresolved()) throw new IOException("Target address not resolved");
        byte[] buffer;
        int offset = 0;
        int len = src.remaining();
        if (src.hasArray()) {
            buffer = src.array();
            offset = src.arrayOffset() + src.position();
        } else {
            buffer = new byte[len];
            src.get(buffer);
        }
        DatagramPacket packet = new DatagramPacket(buffer, offset, len, target);
        boolean completed = false;
        try {
            begin();
            setInChannelOperation(true);
            socket.send(packet);
            completed = true;
        } finally {
            end(completed);
            setInChannelOperation(false);
        }
        if (src.hasArray()) {
            src.position(src.position() + len);
        }
        return len;
    }
}
