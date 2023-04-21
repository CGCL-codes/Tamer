package com.hs.mail.container.server.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Default implementation of server socket factory
 * 
 * @author Won Chul Doh
 * @since Jul 29, 2010
 *
 */
public class DefaultServerSocketFactory implements ServerSocketFactory {

    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    public ServerSocket createServerSocket(int port, int backLog) throws IOException {
        return new ServerSocket(port, backLog);
    }

    public ServerSocket createServerSocket(int port, int backLog, InetAddress bindAddress) throws IOException {
        return new ServerSocket(port, backLog, bindAddress);
    }
}
