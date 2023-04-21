package org.apache.mina.proxy.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferWrapper;

/**
 * ProxyHandshakeIoBuffer.java - {@link IoBuffer} wrapper to indicate handshake 
 * related messages which should not be passed upstream of the {@link ProxyFilter}.
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 * @since MINA 2.0.0-M3
 */
public class ProxyHandshakeIoBuffer extends IoBufferWrapper {

    public ProxyHandshakeIoBuffer(final IoBuffer buf) {
        super(buf);
    }
}
