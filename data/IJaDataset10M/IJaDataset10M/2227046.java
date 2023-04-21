package org.rascalli.framework.net.tcp;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class CloseSocketChannel implements SelectorChangeRequest {

    private final SocketChannel socketChannel;

    public CloseSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void performChanges(TcpServiceImpl selectorService) throws IOException {
        socketChannel.close();
    }
}
