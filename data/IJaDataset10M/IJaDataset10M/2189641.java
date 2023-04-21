package info.wisl.netmodules;

import info.wisl.*;
import java.net.*;

/**
 * @author Thomas Hildebrandt
 */
public class LandmarksPing {

    /** in milliseconds */
    static final int MAX_PING_MSEC = 3000;

    static final String LNPACKET = "LNPacket";

    int port;

    InetAddress fromIP;

    byte[] msg;

    public LandmarksPing(LandmarksNeighbor ln, String wsid) throws Exception {
        port = ln.getPort();
        fromIP = InetAddress.getByName(ln.getAddress());
        String data = "LNPacket\t" + wsid;
        msg = data.getBytes("US-ASCII");
    }

    public long doPing() throws Exception {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(MAX_PING_MSEC);
        DatagramPacket packet = new DatagramPacket(msg, msg.length, fromIP, port);
        byte[] repBuf = new byte[LNPACKET.length()];
        DatagramPacket reply = new DatagramPacket(repBuf, repBuf.length);
        long sendTime = System.currentTimeMillis();
        socket.send(packet);
        try {
            socket.receive(reply);
        } catch (SocketTimeoutException ste) {
            return MAX_PING_MSEC;
        }
        long receiveTime = System.currentTimeMillis();
        String rep = new String(repBuf, "US-ASCII");
        if (!rep.equals(LNPACKET)) {
            throw new Exception("Incorrect ping reply received: " + rep);
        }
        return (receiveTime - sendTime);
    }
}
