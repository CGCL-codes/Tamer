package org.apache.mina.core.future;

import org.apache.mina.core.session.IoSession;

/**
 * An {@link IoFuture} for asynchronous connect requests.
 *
 * <h3>Example</h3>
 * <pre>
 * IoConnector connector = ...;
 * ConnectFuture future = connector.connect(...);
 * future.join(); // Wait until the connection attempt is finished.
 * IoSession session = future.getSession();
 * session.write(...);
 * </pre>
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public interface ConnectFuture extends IoFuture {

    /**
     * Returns {@link IoSession} which is the result of connect operation.
     *
     * @return <tt>null</tt> if the connect operation is not finished yet
     * @throws RuntimeException if connection attempt failed by an exception
     */
    IoSession getSession();

    /**
     * Returns the cause of the connection failure.
     *
     * @return <tt>null</tt> if the connect operation is not finished yet,
     *         or if the connection attempt is successful.
     */
    Throwable getException();

    /**
     * Returns <tt>true</tt> if the connect operation is finished successfully.
     */
    boolean isConnected();

    /**
     * Returns {@code true} if the connect operation has been canceled by
     * {@link #cancel()} method.
     */
    boolean isCanceled();

    /**
     * Sets the newly connected session and notifies all threads waiting for
     * this future.  This method is invoked by MINA internally.  Please do not
     * call this method directly.
     */
    void setSession(IoSession session);

    /**
     * Sets the exception caught due to connection failure and notifies all
     * threads waiting for this future.  This method is invoked by MINA
     * internally.  Please do not call this method directly.
     */
    void setException(Throwable exception);

    /**
     * Cancels the connection attempt and notifies all threads waiting for
     * this future.
     */
    void cancel();

    ConnectFuture await() throws InterruptedException;

    ConnectFuture awaitUninterruptibly();

    ConnectFuture addListener(IoFutureListener<?> listener);

    ConnectFuture removeListener(IoFutureListener<?> listener);
}
