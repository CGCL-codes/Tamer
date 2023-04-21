package org.apache.mina.core.future;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.session.IoSession;

/**
 * A default implementation of {@link ConnectFuture}.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class DefaultConnectFuture extends DefaultIoFuture implements ConnectFuture {

    private static final Object CANCELED = new Object();

    /**
     * Returns a new {@link ConnectFuture} which is already marked as 'failed to connect'.
     */
    public static ConnectFuture newFailedFuture(Throwable exception) {
        DefaultConnectFuture failedFuture = new DefaultConnectFuture();
        failedFuture.setException(exception);
        return failedFuture;
    }

    /**
     * Creates a new instance.
     */
    public DefaultConnectFuture() {
        super(null);
    }

    @Override
    public IoSession getSession() {
        Object v = getValue();
        if (v instanceof RuntimeException) {
            throw (RuntimeException) v;
        } else if (v instanceof Error) {
            throw (Error) v;
        } else if (v instanceof Throwable) {
            throw (RuntimeIoException) new RuntimeIoException("Failed to get the session.").initCause((Throwable) v);
        } else if (v instanceof IoSession) {
            return (IoSession) v;
        } else {
            return null;
        }
    }

    public Throwable getException() {
        Object v = getValue();
        if (v instanceof Throwable) {
            return (Throwable) v;
        } else {
            return null;
        }
    }

    public boolean isConnected() {
        return getValue() instanceof IoSession;
    }

    public boolean isCanceled() {
        return getValue() == CANCELED;
    }

    public void setSession(IoSession session) {
        if (session == null) {
            throw new IllegalArgumentException("session");
        }
        setValue(session);
    }

    public void setException(Throwable exception) {
        if (exception == null) {
            throw new IllegalArgumentException("exception");
        }
        setValue(exception);
    }

    public void cancel() {
        setValue(CANCELED);
    }

    @Override
    public ConnectFuture await() throws InterruptedException {
        return (ConnectFuture) super.await();
    }

    @Override
    public ConnectFuture awaitUninterruptibly() {
        return (ConnectFuture) super.awaitUninterruptibly();
    }

    @Override
    public ConnectFuture addListener(IoFutureListener<?> listener) {
        return (ConnectFuture) super.addListener(listener);
    }

    @Override
    public ConnectFuture removeListener(IoFutureListener<?> listener) {
        return (ConnectFuture) super.removeListener(listener);
    }
}
