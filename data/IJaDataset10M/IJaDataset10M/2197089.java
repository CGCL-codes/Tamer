package org.xsocket.stream;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import javax.annotation.Resource;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.stream.IConnection.FlushMode;

/**
*
* @author grro@xsocket.org
*/
public final class Proxy implements Closeable {

    private IServer server = null;

    Proxy(int listenPort, String forwardHost, int forwardPort) throws IOException {
        server = new Server(listenPort, new ClientToProxyHandler(InetAddress.getByName(forwardHost), forwardPort));
        StreamUtils.start(server);
    }

    public static void main(String... args) throws Exception {
        if (args.length != 3) {
            System.out.println("usage org.xsocket.stream.Proxy <listenport> <forwardhost> <forwardport>");
            System.exit(-1);
        }
        new Proxy(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
    }

    public void close() throws IOException {
        if (server != null) {
            server.close();
        }
    }

    private static class ProxyHandler implements IConnectHandler, IDataHandler, IDisconnectHandler {

        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            connection.setFlushmode(FlushMode.ASYNC);
            return true;
        }

        public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
            INonBlockingConnection reverseConnection = (INonBlockingConnection) connection.getAttachment();
            if (reverseConnection != null) {
                connection.setAttachment(null);
                reverseConnection.close();
            }
            return true;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            INonBlockingConnection forwardConnection = (INonBlockingConnection) connection.getAttachment();
            ByteBuffer[] data = connection.readAvailable();
            forwardConnection.write(data);
            return true;
        }
    }

    private static final class ClientToProxyHandler extends ProxyHandler implements IConnectHandler {

        private InetAddress forwardHost = null;

        private int forwardPort = 0;

        @Resource
        private IServerContext ctx = null;

        public ClientToProxyHandler(InetAddress forwardHost, int forwardPort) {
            this.forwardHost = forwardHost;
            this.forwardPort = forwardPort;
        }

        public boolean onConnect(INonBlockingConnection clientToProxyConnection) throws IOException {
            super.onConnect(clientToProxyConnection);
            Executor workerPool = ctx.getWorkerpool();
            INonBlockingConnection proxyToServerConnection = new NonBlockingConnection(forwardHost, forwardPort, new ProxyHandler(), workerPool);
            clientToProxyConnection.setAttachment(proxyToServerConnection);
            proxyToServerConnection.setAttachment(clientToProxyConnection);
            return true;
        }
    }
}
