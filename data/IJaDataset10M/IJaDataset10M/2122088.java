package org.jeuron.jlightning.connection.protocol.factory;

import org.jeuron.jlightning.connection.protocol.bytebuffer.DefaultDatagramByteBufferProtocol;
import org.jeuron.jlightning.connection.protocol.Protocol;

/**
 * Creates a {@link DefaultDatagramByteBufferProtocol}.
 *
 * @author Mike Karrys
 * @since 1.0
 * @see ProtocolFactory
 * @see DefaultDatagramByteBufferProtocol
 */
public class DatagramByteBufferProtocolFactory implements ProtocolFactory {

    /**
     * Creates a protocol.
     * @param protocol
     */
    public Protocol createProtocol() {
        return new DefaultDatagramByteBufferProtocol();
    }
}
