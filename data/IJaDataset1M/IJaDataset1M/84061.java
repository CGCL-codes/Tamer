package org.simpleframework.http.core;

import java.io.IOException;
import java.net.Socket;

public class PerformanceCreateSocketFactory implements PerformanceSocketFactory {

    private final String host;

    private final int port;

    public PerformanceCreateSocketFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void addSocket() throws Exception {
        return;
    }

    public Socket getSocket() throws Exception {
        for (int i = 0; i < 10; i++) {
            try {
                Socket socket = new Socket(host, port);
                socket.setReuseAddress(true);
                socket.setTcpNoDelay(true);
                return socket;
            } catch (IOException e) {
                continue;
            }
        }
        throw new IOException("Could not acquire socket");
    }

    public void recycle(Socket socket) throws Exception {
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (Exception e) {
            socket.close();
        }
    }
}
