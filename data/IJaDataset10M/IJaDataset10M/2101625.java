package spike.net;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketUtil {

    public static void close(ServerSocketChannel serverSocket) {
        if (serverSocket != null) {
            close(serverSocket.socket());
        }
    }

    private SocketUtil() {
    }

    public static void close(SocketChannel socket) {
        if (socket != null) {
            try {
                socket.socket().setReuseAddress(true);
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
            }
        }
    }

    public static boolean isPortInUse(int port) {
        ServerSocketChannel serverSocket = null;
        try {
            serverSocket = createServerSocket(port);
            return false;
        } catch (IllegalArgumentException err) {
            return true;
        } finally {
            close(serverSocket);
        }
    }

    public static ServerSocketChannel createServerSocket(int port) throws IllegalArgumentException {
        ServerSocketChannel serverSocket = null;
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            serverSocket.socket().bind(new InetSocketAddress(port));
        } catch (BindException err) {
            throw new IllegalArgumentException("Port appears to be already in use", err);
        } catch (IOException err) {
            err.printStackTrace();
        }
        return serverSocket;
    }

    public static void threadSafeClose(Selector selector) {
        if (selector != null) {
            synchronized (selector) {
                close(selector);
            }
        }
    }

    public static void close(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.setReuseAddress(true);
                serverSocket.close();
            } catch (IOException err) {
            }
        }
    }
}
