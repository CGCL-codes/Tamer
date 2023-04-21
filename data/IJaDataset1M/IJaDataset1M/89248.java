package com.gcapmedia.dab.epg.binary;

import java.math.BigInteger;
import org.apache.commons.lang.StringUtils;

/**
 * Utility class for reading a set of bits with the ability to pull
 * data types out from various points.
 */
public class BitParser {

    /**
	 * Binary string
	 */
    private String binary;

    /**
	 * Create a new BitParser
	 * @param size
	 */
    public BitParser(byte[] bytes) {
        BigInteger big = new BigInteger(1, bytes);
        binary = StringUtils.leftPad(big.toString(2), bytes.length * 8, '0');
    }

    /**
	 * Returns the size of the construct
	 * @return the size of the construct
	 */
    public int size() {
        return binary.length();
    }

    /**
	 * 
	 */
    public int getInt(int index, int length) {
        String toParse = binary.substring(index, index + length);
        return Integer.parseInt(toParse, 2);
    }

    /**
	 * 
	 */
    public long getLong(int index, int length) {
        String toParse = binary.substring(index, index + length);
        return Long.parseLong(toParse, 2);
    }

    /**
	 * 
	 */
    public boolean getBoolean(int index) {
        return binary.charAt(index) == '1';
    }

    public byte getByte(int index) {
        byte[] bytes = getByteArray(index, 8);
        return bytes[0];
    }

    /**
	 * 
	 */
    public byte[] getByteArray(int index, int length) {
        if (index + length > size()) {
            throw new ArrayIndexOutOfBoundsException("Requested array (" + index + "->" + (index + length) + ") is beyond the bounds (" + size() + ")");
        }
        String tmp = binary.substring(index, index + length);
        byte[] bytes = new byte[(int) Math.ceil(((double) tmp.length()) / 8)];
        for (int i = 0; i < tmp.length(); i += 8) {
            int end = i + 8;
            if (end > tmp.length()) {
                end = tmp.length();
            }
            String chunk = tmp.substring(i, end);
            long lng = Long.parseLong(chunk, 2);
            bytes[i / 8] = (byte) lng;
        }
        return bytes;
    }

    /**
     * 
     */
    public static byte[] parseByteArray(String hex) {
        hex = hex.replaceAll(" ", "");
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            int num = Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[i / 2] = (byte) num;
        }
        return bytes;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "BitParser [" + size() + "]: " + binary;
    }
}
