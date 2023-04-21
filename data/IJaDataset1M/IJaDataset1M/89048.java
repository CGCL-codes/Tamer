package net.kodeninja.http.service;

import java.net.InetAddress;
import net.kodeninja.scheduling.Scheduler;

public class HTTPUDPService extends HTTPService<HTTPUDPServerSocket> {

    public static final int DEFAULT_HTTP_PORT = 80;

    public static final int ALTERNATIVE_HTTP_PORT = 8080;

    public HTTPUDPService() {
        this(DEFAULT_HTTP_PORT);
    }

    public HTTPUDPService(int port) {
        super(new HTTPUDPServerSocket(), port);
    }

    public HTTPUDPService(InetAddress MulticastAddr, int port) {
        this(port);
        getTransport().joinMulticastGroup(MulticastAddr);
    }

    public HTTPUDPService(int Port, int MaxConnections) {
        super(new HTTPUDPServerSocket(), Port, MaxConnections);
    }

    public HTTPUDPService(Scheduler owner, int Port) {
        super(owner, new HTTPUDPServerSocket(), Port);
    }

    public HTTPUDPService(InetAddress MulticastAddr, int Port, int MaxConnections) {
        super(new HTTPUDPServerSocket(), Port, MaxConnections);
        getTransport().joinMulticastGroup(MulticastAddr);
    }

    public HTTPUDPService(Scheduler owner, InetAddress MulticastAddr, int Port) {
        super(owner, new HTTPUDPServerSocket(), Port);
        getTransport().joinMulticastGroup(MulticastAddr);
    }
}
