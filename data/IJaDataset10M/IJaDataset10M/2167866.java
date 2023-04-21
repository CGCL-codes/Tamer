package net.pesahov.remote.socket.direct;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import net.pesahov.remote.socket.RemoteChannel;
import net.pesahov.remote.socket.UnderlyingSocketProxy;

/**
 * @author Pesahov Dmitry
 * @since 2.0
 */
abstract class DirectUnderlyingSocketProxy implements UnderlyingSocketProxy {

    /**
     * Wrapper of Socket or ServerSocket.
     */
    protected DirectUnderlyingSocketWrapper wrapper;

    public void close() throws IOException {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        wrapper.close();
    }

    public RemoteChannel getChannel() {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.getChannel();
    }

    public InetAddress getInetAddress() {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.getInetAddress();
    }

    public int getLocalPort() {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.getLocalPort();
    }

    public SocketAddress getLocalSocketAddress() {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.getLocalSocketAddress();
    }

    public int getReceiveBufferSize() throws SocketException {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.getReceiveBufferSize();
    }

    public boolean getReuseAddress() throws SocketException {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.getReuseAddress();
    }

    public boolean isBound() {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.isBound();
    }

    public boolean isClosed() {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        return wrapper.isClosed();
    }

    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        wrapper.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    public void setReceiveBufferSize(int size) throws SocketException {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        wrapper.setReceiveBufferSize(size);
    }

    public void setReuseAddress(boolean on) throws SocketException {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        wrapper.setReuseAddress(on);
    }

    public void setSoTimeout(int timeout) throws SocketException {
        if (wrapper == null) throw new IllegalStateException("Socket instance not created yet!");
        wrapper.setSoTimeout(timeout);
    }

    @Override
    public String toString() {
        if (wrapper == null) return super.toString();
        return wrapper.toString();
    }
}
