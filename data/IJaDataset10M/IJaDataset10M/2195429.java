package org.jboss.netty.example.http.upload;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author Andy Taylor (andy.taylor@jboss.org)
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * @author <a href="http://openr66.free.fr/">Frederic Bregier</a>
 *
 * @version $Rev: 612 $, $Date: 2010-11-11 13:35:43 -0500 (Thu, 11 Nov 2010) $
 */
public class HttpServer {

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new HttpServerPipelineFactory());
        bootstrap.bind(new InetSocketAddress(8080));
    }
}
