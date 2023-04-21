package com.wuala.applet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ConnectionChecker {

    private static final String[] SERVERS = ISettings.SERVER_ADDRESSES;

    public void check(TrustedApplet monitor) throws IOException {
        IOException ioex = null;
        int rand = Math.abs(((int) System.currentTimeMillis()) % SERVERS.length);
        for (int i = 0; i < SERVERS.length; i++) {
            try {
                String s = SERVERS[(i + rand) % SERVERS.length];
                serverCheck(s, monitor);
                return;
            } catch (IOException e) {
                if (ioex == null) {
                    ioex = e;
                }
            }
        }
        throw ioex;
    }

    private void serverCheck(String address, TrustedApplet monitor) throws IOException {
        try {
            connectionCheck(address, 443);
            monitor.note("Connecting to " + address + ":443 possible");
        } catch (IOException e) {
            connectionCheck(address, 80);
            monitor.note("Connecting to " + address + ":80 possible");
        }
    }

    private void connectionCheck(String address, int port) throws IOException {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(address, port));
        channel.close();
    }
}
