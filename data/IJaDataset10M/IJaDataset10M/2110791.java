package com.meatfreezer.jactid.pinger;

import com.meatfreezer.nms.IDevice;
import com.meatfreezer.nms.IPingResult;
import java.net.*;
import java.io.IOException;
import org.apache.log4j.Level;

/**
 * UdpPinger
 *
 * @author  Alex Shepard
 * @version 0.1
 */
public class UdpPinger extends Pinger {

    /**
     * This is the port we connect to when doing a UDP ping.  This is 
     * the same port that cmd.php and cactid use for UDP "ping."
     */
    public static final int UDPPINGPORT = 33439;

    /**
     *Constructor
     *
     * @param   device  The device to ping.
     * @param   retries The number of times to retry the UDP ping before
     * giving up.
     * @param   timeout The amount of time to wait before giving up on an
     * UDP ping.
     */
    public UdpPinger(IDevice device, int retries, int timeout) {
        super(device, retries, timeout);
    }

    /**
     * ping
     *
     * @param   device  The device to ping.
     * @return  A PingResult object, which is essentially a struct or tuple of
     * upDown (isUp()) and response time (getResponseTime().
     */
    public IPingResult ping() {
        PingResult pingResult = new PingResult(device.getId());
        long startTime = System.currentTimeMillis();
        InetAddress inet;
        DatagramSocket socket;
        try {
            inet = InetAddress.getByName(device.getHostname());
        } catch (UnknownHostException e) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn("Can't resolve " + device.getHostname() + ": " + e.getMessage());
            }
            pingResult.setUp(false);
            pingResult.setErrorMessage("Hostname resolution error.");
            return pingResult;
        }
        try {
            socket = new DatagramSocket();
            socket.connect(inet, UdpPinger.UDPPINGPORT);
        } catch (SocketException e) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn("Can't initialize UDP socket: " + e.getMessage());
            }
            pingResult.setUp(false);
            pingResult.setErrorMessage("UDP Socket error.");
            return pingResult;
        }
        String message = "cacti-monitoring-system";
        byte[] msgBuff = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msgBuff, msgBuff.length);
        try {
            socket.setSoTimeout(timeout);
        } catch (SocketException e) {
            if (logger.isEnabledFor(Level.WARN)) {
                logger.warn("Error setting socket timeout: " + e.getMessage());
            }
            pingResult.setUp(true);
            pingResult.setResponseTime(System.currentTimeMillis() - startTime);
            return pingResult;
        }
        for (int i = 0; i <= retries; i++) {
            try {
                socket.send(packet);
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                if (i == retries) {
                    pingResult.setUp(false);
                    pingResult.setErrorMessage("Host down.");
                    return pingResult;
                } else {
                    continue;
                }
            } catch (PortUnreachableException e) {
                pingResult.setUp(true);
                pingResult.setResponseTime(System.currentTimeMillis() - startTime);
                return pingResult;
            } catch (IOException e) {
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn("Can't send udp datagram: " + e.getMessage());
                }
                pingResult.setUp(false);
                pingResult.setErrorMessage("Can't send UDP datagram.");
                return pingResult;
            }
            pingResult.setUp(true);
            pingResult.setResponseTime(System.currentTimeMillis() - startTime);
            return pingResult;
        }
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error("lpt0 on fire.");
        }
        pingResult.setUp(false);
        pingResult.setErrorMessage("Bizarre fallthrough.");
        return pingResult;
    }
}
