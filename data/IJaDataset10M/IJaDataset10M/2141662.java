package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * An abstract {@link MessageDecoder} implementation for those who don't need to
 * implement {@link MessageDecoder#finishDecode(IoSession, ProtocolDecoderOutput)}
 * method.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public abstract class MessageDecoderAdapter implements MessageDecoder {

    /**
     * Override this method to deal with the closed connection.
     * The default implementation does nothing.
     */
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
    }
}
