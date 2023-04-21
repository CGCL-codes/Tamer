package org.ms150hams.trackem.network.ax25;

import java.util.Hashtable;
import java.util.logging.Logger;
import org.ms150hams.trackem.network.EventHandler;
import org.ms150hams.trackem.network.LayerOneProtocol;
import org.ms150hams.trackem.network.LayerThreeProtocol;
import org.ms150hams.trackem.network.LayerTwoProtocol;

public class AX25Protocol implements LayerTwoProtocol {

    private static final Logger logger = Logger.getLogger("org.ms150hams.trackem.network.ax25");

    public static final String SourceCallsignKey = "sourcecall";

    public static final String AX25PIDKey = "ax25pid";

    public static final byte ax25PID = 0x77;

    private LayerOneProtocol lowerLayer = null;

    private LayerThreeProtocol[] upperLayers = new LayerThreeProtocol[256];

    private char[] destCall = new char[] { 'M', 'S', '1', '5', '0', ' ' };

    private char[] myCall = new char[] { 'N', 'O', 'C', 'A', 'L', 'L' };

    private int mySSID = 0;

    public AX25Protocol(LayerOneProtocol lower, LayerThreeProtocol[] upper) {
        if (upper.length != 256) throw new IllegalArgumentException();
        lowerLayer = lower;
        upperLayers = upper;
    }

    public AX25Protocol() {
    }

    public synchronized void setLowerLayer(LayerOneProtocol layer) {
        lowerLayer = layer;
    }

    public LayerOneProtocol getLowerLayer() {
        return lowerLayer;
    }

    public synchronized void setUpperLayer(LayerThreeProtocol proto, int pid) {
        upperLayers[pid] = proto;
    }

    /**
	 * Sets the destination callsign for the AX.25 frames.  This does not need to
	 * be a valid callsign, any sort of alias is fine.
	 * @param call The destination callsign, 6 characters or less.
	 */
    public void setDestinationCall(String call) {
        if (call.length() > 6) throw new IllegalArgumentException("Call must be 6 characters or less");
        int i;
        call.getChars(0, call.length(), destCall, 0);
        for (i = call.length(); i < destCall.length; i++) {
            destCall[i] = ' ';
        }
    }

    public String getDestinationCall() {
        return new String(destCall);
    }

    /**
	 * Sets the source callsign for the AX.25 frames.
	 * @param call The source callsign, must be 6 characters or less.
	 * @throws IllegalArgumentException if the callsign is too long.
	 */
    public void setMyCall(String call) {
        if (call.length() > 6) throw new IllegalArgumentException("Call must be 6 characters or less");
        int i;
        call.getChars(0, call.length(), myCall, 0);
        for (i = call.length(); i < myCall.length; i++) {
            myCall[i] = ' ';
        }
    }

    public String getMyCall() {
        return new String(myCall);
    }

    /**
	 * Sets the SSID for the source callsign field.
	 * @param ssid The SSID, must be between 0 and 15, inclusive.
	 */
    public void setSSID(int ssid) {
        if (ssid > 15 || ssid < 0) throw new IllegalArgumentException("SSID must be between 0 and 15 inclusive");
        mySSID = ssid;
    }

    public int getSSID() {
        return mySSID;
    }

    public void receiveFrame(byte[] data, Hashtable protoInfo) {
        try {
            char[] srcCall = new char[6];
            int srcSSID;
            char[] dstCall = new char[6];
            int dstSSID;
            char[][] repeaterCalls = new char[8][6];
            int[] repeaterSSIDs = new int[8];
            boolean[] repeaterHeard = new boolean[8];
            if (data.length < 16) {
                System.out.println("Invalid ax25 frame, too short");
                return;
            }
            int pos = 0;
            dstCall[0] = (char) ((data[pos++] & 0xFF) >> 1);
            dstCall[1] = (char) ((data[pos++] & 0xFF) >> 1);
            dstCall[2] = (char) ((data[pos++] & 0xFF) >> 1);
            dstCall[3] = (char) ((data[pos++] & 0xFF) >> 1);
            dstCall[4] = (char) ((data[pos++] & 0xFF) >> 1);
            dstCall[5] = (char) ((data[pos++] & 0xFF) >> 1);
            dstSSID = ((data[pos++] & 0xFF) >> 1) & 0x0F;
            srcCall[0] = (char) ((data[pos++] & 0xFF) >> 1);
            srcCall[1] = (char) ((data[pos++] & 0xFF) >> 1);
            srcCall[2] = (char) ((data[pos++] & 0xFF) >> 1);
            srcCall[3] = (char) ((data[pos++] & 0xFF) >> 1);
            srcCall[4] = (char) ((data[pos++] & 0xFF) >> 1);
            srcCall[5] = (char) ((data[pos++] & 0xFF) >> 1);
            srcSSID = ((data[pos++] & 0xFF) >> 1) & 0x0F;
            logger.finest("ax.25 packet from " + new String(srcCall).trim() + (srcSSID != 0 ? "-" + srcSSID : "") + " to " + new String(dstCall).trim() + (dstSSID != 0 ? "-" + dstSSID : "") + " ");
            int repeatPos = 0;
            while (((data[pos - 1] & 0xFF) & 0x1) == 0) {
                repeaterCalls[repeatPos][0] = (char) ((data[pos++] & 0xFF) >> 1);
                repeaterCalls[repeatPos][1] = (char) ((data[pos++] & 0xFF) >> 1);
                repeaterCalls[repeatPos][2] = (char) ((data[pos++] & 0xFF) >> 1);
                repeaterCalls[repeatPos][3] = (char) ((data[pos++] & 0xFF) >> 1);
                repeaterCalls[repeatPos][4] = (char) ((data[pos++] & 0xFF) >> 1);
                repeaterCalls[repeatPos][5] = (char) ((data[pos++] & 0xFF) >> 1);
                repeaterSSIDs[repeatPos] = ((data[pos] & 0xFF) >> 1) & 0x0F;
                repeaterHeard[repeatPos] = (((data[pos++] & 0xFF) >> 7) & 0x01) == 0x01;
                repeatPos++;
            }
            int control1, control2 = 0;
            control1 = data[pos++] & 0xFF;
            if (control1 == 0xFF) control2 = data[pos++] & 0xFF;
            if (control1 != 0x3 && control2 == 0) {
                System.out.println("Not a UI Packet");
                return;
            }
            int pid = data[pos++] & 0xFF;
            protoInfo.put(LayerTwoProtocol.LayerTwoProtocolKey, this);
            protoInfo.put(SourceCallsignKey, new String(srcCall).trim() + (srcSSID > 0 ? "-" + srcSSID : ""));
            protoInfo.put(AX25PIDKey, new Integer(pid));
            if (pid == 0xF0 && upperLayers[pid] != null) {
                upperLayers[pid].receivePacket(data, protoInfo);
            } else if (upperLayers[pid] != null) {
                byte[] outData = new byte[data.length - pos];
                System.arraycopy(data, pos, outData, 0, outData.length);
                upperLayers[pid].receivePacket(outData, protoInfo);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void transmitPacket(byte[] inData, Hashtable protoInfo) {
        logger.finest("Transmitting " + inData.length + " bytes");
        byte[] data = new byte[inData.length + 16];
        int pos = 0;
        data[pos++] = (byte) (destCall[0] << 1);
        data[pos++] = (byte) (destCall[1] << 1);
        data[pos++] = (byte) (destCall[2] << 1);
        data[pos++] = (byte) (destCall[3] << 1);
        data[pos++] = (byte) (destCall[4] << 1);
        data[pos++] = (byte) (destCall[5] << 1);
        data[pos++] = (byte) (0 << 1);
        data[pos++] = (byte) (myCall[0] << 1);
        data[pos++] = (byte) (myCall[1] << 1);
        data[pos++] = (byte) (myCall[2] << 1);
        data[pos++] = (byte) (myCall[3] << 1);
        data[pos++] = (byte) (myCall[4] << 1);
        data[pos++] = (byte) (myCall[5] << 1);
        data[pos++] = (byte) ((mySSID << 1) | 1);
        data[pos++] = (byte) 0x3;
        int pid = ax25PID;
        try {
            Object context = protoInfo.get(AX25PIDKey);
            if (context != null) {
                Integer i = (Integer) context;
                pid = i.intValue();
            }
        } catch (ClassCastException e) {
        }
        data[pos++] = (byte) pid;
        for (int i = 0; i < inData.length; i++) {
            data[pos++] = inData[i];
        }
        protoInfo.put(LayerTwoProtocol.LayerTwoProtocolKey, this);
        logger.finest("Encapuslated " + inData.length + " byte packet into " + data.length + " byte frame.");
        if (lowerLayer != null) lowerLayer.transmitFrame(data, protoInfo);
    }
}
