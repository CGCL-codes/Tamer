package mx4j.tools.remote.rmi;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * @version $Revision: 1.4 $
 */
public class SSLRMIServerSocketFactory implements RMIServerSocketFactory {

    private final SSLContext sslContext;

    private final int backlog;

    public SSLRMIServerSocketFactory(SSLContext sslContext) {
        this(sslContext, 50);
    }

    public SSLRMIServerSocketFactory(SSLContext sslContext, int backlog) {
        this.sslContext = sslContext;
        this.backlog = backlog;
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
        return factory.createServerSocket(port, backlog);
    }
}
