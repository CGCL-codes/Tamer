package com.hoodcomputing.filetransfer.support;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * The Chunk class helps create chunks of data for transport over a network.
 * @author james_hood
 */
public class Chunk {

    private byte[] data;

    private long segment;

    /**
     * Constructs a new chunk using data received from the network.
     * @param payload The data that was received.
     * @throws ChecksumException This exception is thrown when the data is invalid.
     */
    public Chunk(byte[] payload) throws ChecksumException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
            System.exit(1);
        }
        md.update(payload, 16, payload.length - 16);
        byte[] md5 = md.digest();
        byte[] payloadMd5 = new byte[16];
        System.arraycopy(payload, 0, payloadMd5, 0, 16);
        if (!Arrays.equals(md5, payloadMd5)) {
            throw new ChecksumException();
        }
        segment = Conversion.bytesToLong(payload, 16);
        data = new byte[payload.length - 24];
        System.arraycopy(payload, 24, data, 0, payload.length - 24);
    }

    /**
     * Gets this chunk's data.
     * @return The data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Gets the segment number of this chunk.
     * @return The segment number.
     */
    public long getSegment() {
        return segment;
    }

    /**
     * This functions transforms a segment number and a data array into an
     * integrated unit suitable for transfer across a network to be used as an
     * instation parameter for the Chunk class.
     * @param segment The segment number.
     * @param data The data bytes.
     * @return The data for the network packet.
     */
    public static byte[] buildTransferPayload(long segment, byte[] data) {
        byte[] payload = new byte[data.length + 24];
        System.arraycopy(Conversion.longToBytes(segment), 0, payload, 16, 8);
        System.arraycopy(data, 0, payload, 24, data.length);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(ex);
            System.exit(1);
        }
        md.update(payload, 16, payload.length - 16);
        byte[] md5 = md.digest();
        System.arraycopy(md5, 0, payload, 0, 16);
        return payload;
    }
}
