package be.lassi.artnet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import be.lassi.kernel.Constants;
import be.lassi.lanbox.ConnectionPreferences;
import be.lassi.lanbox.udp.Message;
import be.lassi.lanbox.udp.UdpPacket;
import be.lassi.lanbox.udp.UdpPacketParser;
import be.lassi.lanbox.udp.UdpReceiver;
import be.lassi.util.Util;

public class ArtNetUdpReceiver {

    /**
     * Destination for log messages.
     */
    private static final Logger LOGGER = LogManager.getLogger(UdpReceiver.class);

    private static final int WAIT_TIME = (1000 - 5) / Constants.UI_UPDATES_PER_SECOND;

    private long previousReadTime;

    private final ConnectionPreferences properties;

    private DatagramSocket socket;

    /**
     * Constructs a new receiver.
     *
     * @param properties
     *            connection configuration information
     */
    public ArtNetUdpReceiver(final ConnectionPreferences properties) {
        this.properties = properties;
    }

    /**
     * Closes the receiver network connection.
     */
    public void close() {
        if (socket != null) {
            socket.close();
        }
        socket = null;
        LOGGER.info("UDP receive socket closed");
    }

    /**
     * Reads a new packet from the UDP receive socket. This method gets called
     * right after the previous update of the user interface for the previous
     * packet was finished. We do not need to return a new packet immediately
     * but we can drop all packets that arrive before the wait time is finished.
     *
     * @return the new packet or null when no packet is received within the
     *         timeout period
     */
    public UdpPacket nextPacket() {
        assertOpen();
        UdpPacket result = null;
        if (socket != null) {
            long startTime = Util.now();
            long targetTime = startTime + WAIT_TIME;
            int timeout = WAIT_TIME;
            while (timeout > 15) {
                try {
                    socket.setSoTimeout(timeout);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                UdpPacket packet = read();
                if (packet != null) {
                    result = packet;
                }
                timeout = (int) (targetTime - Util.now());
            }
        }
        return result;
    }

    private void log(final UdpPacket packet) {
        if (LOGGER.isDebugEnabled()) {
            long now = Util.now();
            long timeSincePreviousRead = now - previousReadTime;
            previousReadTime = now;
            StringBuilder b = new StringBuilder();
            b.append("sequenceNumber=");
            b.append(packet.getSequenceNumber());
            b.append(", timeSincePreviousRead=");
            b.append(timeSincePreviousRead);
            b.append("ms");
            for (Message message : packet) {
                b.append("\n  ");
                message.append(b);
            }
            String string = b.toString();
            LOGGER.debug(string);
        }
    }

    private void assertOpen() {
        if (socket == null) {
            open();
        }
        if (socket == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("Unexpected sleep interrupt", e);
            }
        }
    }

    private void open() {
        int port = properties.getIntUdpPort();
        try {
            socket = new DatagramSocket(port);
            LOGGER.info("UDP receive socket opened");
            if (LOGGER.isDebugEnabled()) {
                StringBuilder b = new StringBuilder();
                b.append("InetAddress=");
                b.append(socket.getInetAddress());
                b.append(", localAddress=");
                b.append(socket.getLocalAddress());
                b.append(", localPort=");
                b.append(socket.getLocalPort());
                b.append(", port=");
                b.append(socket.getPort());
                b.append(", localSocketAddress");
                b.append(socket.getLocalSocketAddress());
                b.append(", removeSocketAddress");
                b.append(socket.getRemoteSocketAddress());
                b.append(", reuseAddress=");
                b.append(socket.getReuseAddress());
                b.append(", SendBufferSize=");
                b.append(socket.getSendBufferSize());
                b.append(", soTimeout=");
                b.append(socket.getSoTimeout());
                b.append(", trafficClass=");
                b.append(socket.getTrafficClass());
                b.append(", broadCast=");
                b.append(socket.getBroadcast());
                LOGGER.debug(b.toString());
            }
        } catch (SocketException e) {
            LOGGER.error("Cannot create UDP socket", e);
            socket = null;
        }
    }

    private UdpPacket read() {
        UdpPacket packet = null;
        byte[] bytes = new byte[12500];
        DatagramPacket p = new DatagramPacket(bytes, bytes.length);
        try {
            socket.receive(p);
            LOGGER.debug(Util.toString(bytes, p.getLength()));
            UdpPacketParser parser = new UdpPacketParser(LOGGER, bytes, p.getLength());
            packet = parser.getPacket();
            log(packet);
        } catch (SocketTimeoutException e) {
            LOGGER.debug("read() timeout");
        } catch (IOException e) {
            LOGGER.error("read()", e);
        }
        return packet;
    }
}
