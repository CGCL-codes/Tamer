package com.vangent.hieos.xutil.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 * Send Messages over UDP (RFC5426) Protocol
 *
 * @author Adeola Odunlami / Bernie Thuman
 */
public class UDPSocketSupport {

    private static final Logger logger = Logger.getLogger(UDPSocketSupport.class);

    private InetAddress hostAddress = null;

    private DatagramSocket socket = null;

    private int port = 514;

    public UDPSocketSupport(String syslogHost, int syslogPort) {
        initialize(syslogHost, syslogPort);
    }

    /**
     * Initialize the class with the target host and port.
     *
     * @param syslogHost Target syslog host.
     * @param syslogPort Target syslog port.
     */
    private void initialize(String syslogHost, int syslogPort) {
        if (logger.isTraceEnabled()) {
            logger.trace("Initializing syslog using UDP protocol=");
        }
        this.port = syslogPort;
        try {
            this.hostAddress = InetAddress.getByName(syslogHost);
        } catch (UnknownHostException e) {
            logger.error("Could not find " + syslogHost + ". All ATNA UDP logging will fail!", e);
        }
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
            logger.error("Could not instantiate DatagramSocket. All ATNA UDP logging will fail!", e);
        }
    }

    /**
     * Write the message to the syslog.
     *
     * @param msg The message to write.
     */
    public void write(String msg) {
        if (this.socket != null) {
            byte[] bytes = msg.getBytes();
            int bytesLength = bytes.length;
            DatagramPacket packet = new DatagramPacket(bytes, bytesLength, this.hostAddress, this.port);
            try {
                this.socket.send(packet);
                this.socket.close();
            } catch (IOException e) {
                logger.error("Unable to send UDP message.", e);
            }
        }
    }
}
