package com.hs.mail.container.server.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Interface for server socket factory classes
 * 
 * @author Won Chul Doh
 * @since Jul 29, 2010
 *
 */
public interface ServerSocketFactory {

    ServerSocket createServerSocket(int port) throws IOException;

    ServerSocket createServerSocket(int port, int backLog) throws IOException;

    ServerSocket createServerSocket(int port, int backLog, InetAddress bindAddress) throws IOException;
}
