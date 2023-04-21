package org.iosgi.outpost;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.iosgi.outpost.util.ipr.Announcer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sven Schulz
 */
public class Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private static final ExecutorService EXEC_SVC = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        final Announcer a = new Announcer();
        EXEC_SVC.submit(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                try {
                    a.announce(30, 1, TimeUnit.SECONDS);
                } catch (Exception e) {
                    LOGGER.error("IP announcement failed", e);
                }
                return null;
            }
        });
        Server s = new Server();
        s.listen();
        while (!Thread.interrupted()) {
            Thread.sleep(1000);
        }
    }

    private final ServerBootstrap bootstrap;

    private final int port;

    private Channel channel;

    public Server() {
        this(Constants.DEFAULT_PORT);
    }

    public Server(int port) {
        this.port = port;
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new ObjectEncoder(), new ObjectDecoder(Integer.MAX_VALUE), new ServerHandler());
            }
        });
    }

    public void listen() {
        channel = bootstrap.bind(new InetSocketAddress(port));
    }

    public void shutdown() {
        channel.disconnect();
    }
}
