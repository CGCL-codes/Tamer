package j2se.typestate.socketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketExample1 {

    public static SocketChannel createSocketChannel(String hostName, int port) throws IOException {
        SocketChannel sChannel = SocketChannel.open();
        sChannel.configureBlocking(false);
        sChannel.connect(new InetSocketAddress(hostName, port));
        return sChannel;
    }

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = createSocketChannel("hostname.com", 80);
            while (!socketChannel.finishConnect()) {
            }
        } catch (IOException e) {
        }
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        try {
            buf.clear();
            int numBytesRead = socketChannel.read(buf);
            if (numBytesRead == -1) {
                socketChannel.close();
            } else {
                buf.flip();
            }
        } catch (IOException e) {
        }
    }
}
