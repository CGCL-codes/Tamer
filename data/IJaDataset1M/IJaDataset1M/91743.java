package net.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import net.peer.Peer;

public class SSLConnectionManager extends ConnectionManager {

    @Override
    protected SSLSocket createSocket(Peer peer, int port) {
        try {
            return (SSLSocket) SSLSocketFactory.getDefault().createSocket(peer.getIP(), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected SSLConnection createConnection(Peer peer, int port) {
        return new SSLConnection(false, createSocket(peer, port));
    }

    @Override
    protected ServerSocket createServerSocket(int port) {
        try {
            return (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
        return null;
    }

    @Override
    protected SSLConnection createConnection(Socket socket) {
        return new SSLConnection(true, (SSLSocket) socket);
    }
}
