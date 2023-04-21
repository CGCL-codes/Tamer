package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

/**
 * A {@link ProtocolEncoder} implementation which decorates an existing encoder
 * to be thread-safe.  Please be careful if you're going to use this decorator
 * because it can be a root of performance degradation in a multi-thread
 * environment.  Please use this decorator only when you need to synchronize
 * on a per-encoder basis instead of on a per-session basis, which is not
 * common.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class SynchronizedProtocolEncoder implements ProtocolEncoder {

    private final ProtocolEncoder encoder;

    /**
     * Creates a new instance which decorates the specified <tt>encoder</tt>.
     */
    public SynchronizedProtocolEncoder(ProtocolEncoder encoder) {
        if (encoder == null) {
            throw new IllegalArgumentException("encoder");
        }
        this.encoder = encoder;
    }

    /**
     * Returns the encoder this encoder is decorating.
     */
    public ProtocolEncoder getEncoder() {
        return encoder;
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        synchronized (encoder) {
            encoder.encode(session, message, out);
        }
    }

    public void dispose(IoSession session) throws Exception {
        synchronized (encoder) {
            encoder.dispose(session);
        }
    }
}
