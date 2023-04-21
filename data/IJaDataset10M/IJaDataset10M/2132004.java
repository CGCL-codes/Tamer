package org.jbuzz.telnet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * 
 * @author Lung-Kai Cheng (lkcheng@users.sourceforge.net)
 */
public class TelnetServerSocket {

    ServerSocket serverSocket;

    /**
	 * Creates an unbound telnet server socket.
	 * 
	 * @exception IOException
	 *                IO error when opening the telnet server socket
	 */
    public TelnetServerSocket() throws IOException {
        this.serverSocket = new ServerSocket(23);
    }

    public TelnetServerSocket(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public TelnetServerSocket(int port, int backlog) throws IOException {
        this.serverSocket = new ServerSocket(port, backlog);
    }

    public TelnetServerSocket(int port, int backlog, InetAddress bindAddress) throws IOException {
        this.serverSocket = new ServerSocket(port, backlog, bindAddress);
    }

    public boolean isBound() {
        return this.serverSocket.isBound();
    }

    public void bind(SocketAddress endpoint) throws IOException {
        this.serverSocket.bind(endpoint);
    }

    public void bind(SocketAddress endpoint, int backlog) throws IOException {
        this.serverSocket.bind(endpoint, backlog);
    }

    public TelnetSocket accept() throws IOException {
        return new TelnetSocket(this.serverSocket.accept());
    }

    public boolean isClosed() {
        return this.serverSocket.isClosed();
    }

    public void close() throws IOException {
        this.serverSocket.close();
    }

    public InetAddress getInetAddress() {
        return this.serverSocket.getInetAddress();
    }

    public int getLocalPort() {
        return this.serverSocket.getLocalPort();
    }

    public SocketAddress getLocalSocketAddress() {
        return this.serverSocket.getLocalSocketAddress();
    }

    public int getReceiveBufferSize() throws SocketException {
        return this.serverSocket.getReceiveBufferSize();
    }

    public void setReceiveBufferSize(int size) throws SocketException {
        this.serverSocket.setReceiveBufferSize(size);
    }

    public boolean getReuseAddress() throws SocketException {
        return this.serverSocket.getReuseAddress();
    }

    public void setReuseAddress(boolean on) throws SocketException {
        this.serverSocket.setReuseAddress(on);
    }

    public int getSoTimeout() throws IOException {
        return this.serverSocket.getSoTimeout();
    }

    public void setSoTimeout(int timeout) throws SocketException {
        this.serverSocket.setSoTimeout(timeout);
    }
}
