package org.szegedi.nioserver.protocols.http;

import java.nio.channels.SocketChannel;
import org.szegedi.nbpipe.ByteBufferPool;
import org.szegedi.nioserver.ProtocolHandler;
import org.szegedi.nioserver.ProtocolHandlerFactory;

public class HttpProtocolHandlerFactory implements ProtocolHandlerFactory {

    private final ByteBufferPool bufferPool;

    private final Adapter adapter;

    public HttpProtocolHandlerFactory(ByteBufferPool bufferPool, Adapter adapter) {
        this.bufferPool = bufferPool;
        this.adapter = adapter;
    }

    public ProtocolHandler createProtocolHandler(SocketChannel channel) {
        return new HttpProtocolHandler(channel, bufferPool, adapter);
    }
}
