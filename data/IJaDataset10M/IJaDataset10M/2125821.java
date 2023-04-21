package com.juant.market.source.socket;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * Inits a socket that only prints out received information.
 */
public class TestNaiveSocketServer {

    private final SocketServer server;

    public TestNaiveSocketServer(final int port) {
        this.server = new SocketServer(port) {

            protected SimpleChannelUpstreamHandler createServerHandler() {
                return new NaiveHandler();
            }
        };
    }

    public void startListening() {
        System.out.println("Listening...");
        this.server.run();
    }

    public static void main(final String args[]) throws Exception {
        final TestNaiveSocketServer test = new TestNaiveSocketServer(13000);
        test.startListening();
    }

    private static class NaiveHandler extends SimpleChannelUpstreamHandler {

        @Override
        public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e) {
            final int messageSize = ((ChannelBuffer) e.getMessage()).readableBytes();
            final byte[] buffer = new byte[messageSize];
            ((ChannelBuffer) e.getMessage()).getBytes(0, buffer, 0, messageSize);
            System.out.print(new String(buffer));
        }

        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) {
            System.err.println(e.getCause());
            e.getChannel().close();
        }
    }
}
