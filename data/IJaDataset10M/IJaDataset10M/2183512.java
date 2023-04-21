package org.bing.engine.common.multicast;

import java.net.InetAddress;

public interface MulticastHandler {

    public void handle(InetAddress addr, String msg);

    public void handle(InetAddress addr, int port, String msg);
}
