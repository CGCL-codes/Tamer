package vavi.net.im.protocol.oscar.flap;

import java.io.IOException;
import vavi.net.im.protocol.Connection;
import vavi.net.im.protocol.oscar.util.ByteUtils;

/**
 * @author mike
 */
public class FlapHeader {

    public static final int FLAP_HDR_LENGTH = 6;

    private byte channelId;

    private int sequenceNumber;

    private int dataLength;

    /**
     * Constructor
     * @param bytes byte array used to create FlapHeader
     */
    public FlapHeader(byte[] bytes) throws IllegalArgumentException {
        super();
        if ((bytes == null) || (bytes.length < FLAP_HDR_LENGTH)) {
            throw new IllegalArgumentException("FlapHeader constructor bytes param length must be " + FLAP_HDR_LENGTH);
        }
        if (bytes[0] != FlapConstants.FLAP_PREAMBLE) {
            throw new IllegalArgumentException("FlagHeader first byte=" + Integer.toHexString(bytes[0]) + " and not 0x2A");
        }
        channelId = bytes[1];
        sequenceNumber = ByteUtils.getUShort(bytes, 2);
        dataLength = ByteUtils.getUShort(bytes, 4);
    }

    /**
     * Constructor
     * @param channel channel number
     * @param seqNum sequence number
     */
    public FlapHeader(byte channel, int seqNum) {
        channelId = channel;
        sequenceNumber = seqNum;
    }

    /**
     * Get the channel id for the flap header
     * @return channel id of the flap header
     */
    public byte getChannelId() {
        return channelId;
    }

    /**
     * Gets the data length found in the flap header
     * @return data length found in the flap header
     */
    public int getDataLength() {
        return dataLength;
    }

    /**
     * Sets the data length within the flap header
     * @param length value to set the data length to
     */
    public void setDataLength(int length) {
        dataLength = length;
    }

    /**
     * Get the sequence number from the flap header
     * @return sequence number from the flap header
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Gets a byte array of the flap header
     * @return byte array representing the flap header
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[FLAP_HDR_LENGTH];
        bytes[0] = FlapConstants.FLAP_PREAMBLE;
        bytes[1] = channelId;
        bytes[2] = (byte) ((sequenceNumber >> 8) & 0xff);
        bytes[3] = (byte) (sequenceNumber & 0xff);
        bytes[4] = (byte) ((dataLength >> 8) & 0xff);
        bytes[5] = (byte) (dataLength & 0xff);
        return bytes;
    }

    /**
     * Gets string representation of the flap header
     * @return string representation of the flap header
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(30);
        sb.append("FlapHeader :").append("\t").append("preamble=").append("0x2A\t");
        sb.append("channelid=").append(channelId).append("\t").append("sequenceNumber=");
        sb.append(sequenceNumber).append("\tdataLength=").append(dataLength);
        return sb.toString();
    }

    /**
     * Reads a flap header from the network
     * @param conn Connection used to read the flap header
     * @return FlapHeader object
     * @throws IOException when error encountered reading from the connection
     * @throws IllegalStateException when invalid data read for the flap header,
     *         for example, if the preamable is not 0x2a.
     */
    public static FlapHeader getHeader(Connection conn) throws IOException {
        byte[] hdr = new byte[FlapHeader.FLAP_HDR_LENGTH];
        int pos = 0;
        while (pos < hdr.length) {
            final int count = conn.read(hdr, pos, hdr.length - pos);
            if (count == -1) {
                return null;
            }
            pos += count;
        }
        FlapHeader flapHeader = new FlapHeader(hdr);
        return flapHeader;
    }
}
