package net.woodstock.nettool4j.socket;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public abstract class SocketUtils {

    private SocketUtils() {
    }

    public static String toString(final SocketAddress socketAddress) {
        return SocketUtils.toString((InetSocketAddress) socketAddress);
    }

    public static String toString(final InetSocketAddress socketAddress) {
        String str = socketAddress.getAddress().getHostAddress() + ":" + socketAddress.getPort();
        return str;
    }
}
