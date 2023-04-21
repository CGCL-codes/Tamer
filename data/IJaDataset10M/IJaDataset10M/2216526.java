package org.jboss.netty.example.proxy;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
 * @version $Rev: 2080 $, $Date: 2010-01-26 18:04:19 +0900 (Tue, 26 Jan 2010) $
 */
public class HexDumpProxy {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: " + HexDumpProxy.class.getSimpleName() + " <local port> <remote host> <remote port>");
            return;
        }
        int localPort = Integer.parseInt(args[0]);
        String remoteHost = args[1];
        int remotePort = Integer.parseInt(args[2]);
        System.err.println("Proxying *:" + localPort + " to " + remoteHost + ':' + remotePort + " ...");
        Executor executor = Executors.newCachedThreadPool();
        ServerBootstrap sb = new ServerBootstrap(new NioServerSocketChannelFactory(executor, executor));
        ClientSocketChannelFactory cf = new NioClientSocketChannelFactory(executor, executor);
        sb.setPipelineFactory(new HexDumpProxyPipelineFactory(cf, remoteHost, remotePort));
        sb.bind(new InetSocketAddress(localPort));
    }
}
