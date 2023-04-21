package com.cidero.bridge.slim;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 *  Slim UDP Discovery request (SliMP3 format) 
 * 
 *  18 byte message
 *
 *  Field    Value/Description
 *  ---------------------------------------------------
 *  0        'd' as in discovery  
 *  1        Reserved
 *  2        Device id (1 for SLIMP3, 2 for Squeezebox)
 *  3        Firmware rev e.g 0x11 for 1.1
 *  4..11    Reserved
 *  12..17   MAC address
 *
 */
public class SlimDiscoveryReq {

    private static Logger logger = Logger.getLogger("com.cidero.bridge.slim");

    InetAddress srcInetAddr;

    int srcPort;

    int deviceId = 0;

    String firmwareRev;

    byte[] macAddr = new byte[6];

    public SlimDiscoveryReq(DatagramPacket packet) {
        if (packet.getLength() != 18) {
            logger.warning("Bad discovery packet length");
            return;
        }
        byte[] data = packet.getData();
        if (data[0] != 'd') {
            logger.warning("Bad discovery packet header");
            return;
        }
        InetSocketAddress srcSocketAddr = (InetSocketAddress) packet.getSocketAddress();
        srcInetAddr = srcSocketAddr.getAddress();
        srcPort = srcSocketAddr.getPort();
        deviceId = data[2];
        int majorRev = (data[3] >> 4) & 0xf;
        int minorRev = data[3] & 0xf;
        firmwareRev = Integer.toString(majorRev) + "." + Integer.toString(minorRev);
        for (int n = 0; n < 6; n++) macAddr[n] = data[n + 12];
    }

    public InetAddress getSrcInetAddress() {
        return srcInetAddr;
    }

    public int getSrcPort() {
        return srcPort;
    }

    public String getMacAddrHexString() {
        StringBuffer buf = new StringBuffer();
        for (int n = 0; n < 6; n++) {
            int ival = (int) (macAddr[n] & 0xff);
            buf.append(Integer.toHexString(ival));
            if (n < 5) buf.append(":");
        }
        return buf.toString();
    }

    public String toString() {
        return "DiscoveryReq: DeviceId: " + deviceId + " FirmwareRev: " + firmwareRev + " MAC Addr: " + getMacAddrHexString() + " Port: " + srcPort;
    }
}
