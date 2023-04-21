package am.ik.protobuf.server;

import java.io.IOException;
import java.util.concurrent.Executors;
import am.ik.protobuf.TakServiceImpl;
import com.googlecode.protobuf.socketrpc.SocketRpcServer;

public class TakServer {

    public static final String DEFAULT_HOST = "192.168.11.4";

    public static final int DEFAULT_PORT = 1260;

    public static void main(String[] args) throws IOException {
        SocketRpcServer server = new SocketRpcServer(DEFAULT_PORT, Executors.newFixedThreadPool(3));
        server.registerService(new TakServiceImpl());
        server.run();
    }
}
