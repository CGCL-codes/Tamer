package com.kni.util.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

/***
 * The DaytimeUDPClient class is a UDP implementation of a client for the
 * Daytime protocol described in RFC 867.  To use the class, merely
 * open a local datagram socket with
 * <a href="org.apache.commons.net.DatagramSocketClient.html#open"> open </a>
 * and call <a href="#getTime"> getTime </a> to retrieve the daytime
 * string, then
 * call <a href="org.apache.commons.net.DatagramSocketClient.html#close"> close </a>
 * to close the connection properly.  Unlike
 * <a href="org.apache.commons.net.DaytimeTCPClient.html"> DaytimeTCPClient </a>,
 * successive calls to <a href="#getTime"> getTime </a> are permitted
 * without re-establishing a connection.  That is because UDP is a
 * connectionless protocol and the Daytime protocol is stateless.
 * <p>
 * <p>
 * @author Daniel F. Savarese
 * @see DaytimeTCPClient
 ***/
public final class DaytimeUDPClient extends DatagramSocketClient {

    /*** The default daytime port.  It is set to 13 according to RFC 867. ***/
    public static final int DEFAULT_PORT = 13;

    private byte[] __dummyData = new byte[1];

    private byte[] __timeData = new byte[256];

    /***
     * Retrieves the time string from the specified server and port and
     * returns it.
     * <p>
     * @param host The address of the server.
     * @param port The port of the service.
     * @return The time string.
     * @exception IOException If an error occurs while retrieving the time.
     ***/
    public String getTime(InetAddress host, int port) throws IOException {
        DatagramPacket sendPacket, receivePacket;
        sendPacket = new DatagramPacket(this.__dummyData, this.__dummyData.length, host, port);
        receivePacket = new DatagramPacket(this.__timeData, this.__timeData.length);
        this._socket_.send(sendPacket);
        this._socket_.receive(receivePacket);
        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }

    /*** Same as <code>getTime(host, DaytimeUDPClient.DEFAULT_PORT);</code> ***/
    public String getTime(InetAddress host) throws IOException {
        return this.getTime(host, DaytimeUDPClient.DEFAULT_PORT);
    }
}
