package com.dm.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author lightning
 *
 */
public class UDPClient {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        DatagramSocket sock = new DatagramSocket();
        sock.connect(new InetSocketAddress("localhost", 1111));
        DatagramPacket dp = new DatagramPacket("Hello World!".getBytes(), "Hello World!".getBytes().length);
        for (; ; ) {
            sock.send(dp);
        }
    }
}
