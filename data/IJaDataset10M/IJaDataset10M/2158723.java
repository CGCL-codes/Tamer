package org.rascalli.framework.net.tcp;

public interface Connection {

    public interface Listener {

        void messageReceived(byte[] data, Connection connection);

        void connectionClosed(Connection connection);
    }

    /**
     * <p>
     * Send the given message over this Connection.
     * </p>
     * 
     * <p>
     * Note: There is no guarantee that the message will really be sent (e.g.
     * the message might be put into the outgoing message queue and not be sent
     * before the Connection is closed).
     * </p>
     * 
     * @param data
     * @throws ClosedConnectionException
     *             If the Connection has been closed before this method was
     *             invoked.
     */
    public abstract void sendMessage(byte[] data) throws ClosedConnectionException;

    /**
     * <p>
     * Close this connection
     * </p>.
     * 
     * <p>
     * All remaining outgoing packets are discarded. Any further invocations of
     * {@link #sendMessage(byte[])} will throw a
     * {@link ClosedConnectionException}.
     * </p>
     * 
     * <p>
     * Note: The actual SocketChannel associated with this connection will not
     * be closed immediately. Instead, it will be closed asynchronously at a
     * later time.
     * </p>
     */
    public abstract void close();

    /**
     * <p>
     * Check whether this connection is still open (has not yet been closed).
     * </p>
     * 
     * @return {@code true} if this connection is open, {@code false} if it has
     *         been closed.
     */
    public abstract boolean isOpen();

    public abstract void setListener(Listener listener);
}
