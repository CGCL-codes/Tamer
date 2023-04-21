package org.dcm4chee.arr.listeners.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.dcm4chee.arr.listeners.common.AbstractSyslogListener;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision$ $Date$
 * @since Jan 24, 2007
 */
public class UDPListener extends AbstractSyslogListener {

    private static final int MAX_PACKAGE_SIZE = 65507;

    private int maxMsgSize = MAX_PACKAGE_SIZE;

    private DatagramSocket socket;

    private Thread thread;

    public final int getMaxMessageSize() {
        return maxMsgSize;
    }

    public void setMaxMessageSize(int maxMessageSize) {
        if (maxMessageSize < 512 || maxMessageSize > MAX_PACKAGE_SIZE) {
            throw new IllegalArgumentException("maxMessageSize: " + maxMessageSize + " not in range 512..65507");
        }
        this.maxMsgSize = maxMessageSize;
    }

    protected void startServer() throws SocketException {
        if (socket != null) {
            stopServer();
        }
        socket = new DatagramSocket(port, laddr);
        int prevRcvBuf = socket.getReceiveBufferSize();
        if (rcvBuf == 0) {
            rcvBuf = prevRcvBuf;
        } else if (rcvBuf != prevRcvBuf) {
            socket.setReceiveBufferSize(rcvBuf);
            rcvBuf = socket.getReceiveBufferSize();
        }
        thread = new Thread(new Runnable() {

            public void run() {
                lastStartedAt = System.currentTimeMillis();
                SocketAddress lsa = socket.getLocalSocketAddress();
                log.info("Started UDP Server listening on " + lsa);
                byte[] data = new byte[maxMsgSize];
                DatagramPacket p = new DatagramPacket(data, data.length);
                boolean restart = false;
                while (socket != null && !socket.isClosed()) {
                    try {
                        socket.receive(p);
                    } catch (IOException e) {
                        if (!socket.isClosed()) {
                            log.warn("UDP Server throws i/o exception - restart", e);
                            restart = true;
                        }
                        break;
                    }
                    onMessage(p.getData(), p.getOffset(), p.getLength(), p.getAddress());
                    p.setLength(data.length);
                }
                socket = null;
                thread = null;
                lastStoppedAt = System.currentTimeMillis();
                log.info("Stopped UDP Server listening on " + lsa);
                if (restart) {
                    try {
                        startServer();
                    } catch (SocketException e) {
                        log.error("Failed to restart UDP Server", e);
                    }
                }
            }
        });
        thread.start();
    }

    protected void stopServer() {
        if (socket != null) {
            socket.close();
            try {
                thread.join();
            } catch (Exception ignore) {
            }
            socket = null;
        }
    }
}
