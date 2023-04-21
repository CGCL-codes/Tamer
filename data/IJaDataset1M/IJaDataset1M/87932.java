package com.parfumball;

import java.io.IOException;
import com.parfumball.analyzer.PacketInputStream;

/**
 * A general collection of utilities. 
 *  
 * @author prasanna
 */
public class Utils {

    private static char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * Returns a hex string corresponding to the
     * given byte.
     */
    public static String toHex(byte bite) {
        byte hi = (byte) ((bite >>> 4) & 0xF);
        byte lo = (byte) (bite & 0xF);
        return chars[hi & 0xF] + "" + chars[lo & 0xF];
    }

    /**
     * Returns a padded hex string corresponding to the
     * given byte.
     * 
     * @param bite The int to convert to hex.
     * @param width The total width desired. The hex version will be padded
     * with 0s in front.
     */
    public static String toHex(int bite, int width) {
        byte next = 0;
        String s = "";
        for (int i = 0; i < 4; i++) {
            next = (byte) ((bite >>> (i * 8)) & 0xFF);
            s = Utils.toHex(next) + s;
            if (s.length() >= width) {
                return s;
            }
        }
        return s;
    }

    /**
     * Returns a hex dump of the given byte array.
     * 
     * @param bytes The byte array to hex dump.
     * @param appendContent If true the original string corresponding to
     * the hex dump is appended.
     * @return
     */
    public static String toHex(byte[] bytes, boolean appendContent) {
        StringBuffer sb = new StringBuffer(256);
        int i = 0;
        while (i < bytes.length) {
            byte hi = (byte) ((bytes[i] >>> 4) & 0xF);
            byte lo = (byte) (bytes[i] & 0xF);
            sb.append(chars[hi & 0xF]).append(chars[lo & 0xF]);
            i++;
            if (i % 16 == 0) {
                if (appendContent) {
                    sb.append("        ").append(getPrintableString(bytes, i - 16, 16));
                }
                sb.append('\n');
            } else {
                sb.append(' ');
            }
        }
        if (appendContent) {
            if (i % 16 != 0) {
                for (int j = 0; j < 16 - i % 16; j++) {
                    sb.append("   ");
                }
                sb.append("        ");
                sb.append(getPrintableString(bytes, bytes.length - i % 16, i % 16));
            }
        }
        return sb.toString();
    }

    /**
     * Convert the given integer i into a binary string of length that is atleast 
     * the given width. If force is true, the resultant string is truncated of its
     * most significant bits (always from the left) so that a string of only
     * width characters is returned. If force is false, the resultant string is
     * returned even if it exceeds the given width. 
     * 
     * @param i
     * @param width
     * @param pad
     * @param force
     * @return
     */
    public static String toBinary(int i, int width, String pad, boolean force) {
        String s = Integer.toBinaryString(i);
        int len = s.length();
        if (len > width) {
            if (force) {
                s = s.substring(len - width, len);
                return s;
            }
            return s;
        }
        if (len < width) {
            if (pad == null) {
                pad = "0";
            }
            for (int j = 0; j < width - len; j++) {
                s = pad + s;
            }
        }
        return s;
    }

    /**
     * Returns the string version of the given byte array. Unprintable characters are
     * represented as a dot.
     * 
     * @param barray
     * @param start The starting position from which to convert 
     * @param count
     * @return
     */
    public static StringBuffer getPrintableString(byte[] barray, int start, int count) {
        StringBuffer sb = new StringBuffer(count);
        for (int i = 0; i < count; i++) {
            if (start + i >= barray.length) {
                return sb;
            }
            if (barray[start + i] <= 0x20 || barray[start + i] > 127) {
                sb.append('.');
            } else {
                sb.append((char) barray[start + i]);
            }
        }
        return sb;
    }

    public static String prettyPrint(byte[] barray, int start, int count, int width) {
        StringBuffer sb = new StringBuffer(count);
        int wrap = 0;
        for (int i = 0; i < count; i++) {
            if (start + i >= barray.length) {
                return sb.toString();
            }
            if (barray[start + i] <= 0x20 || barray[start + i] > 127) {
                sb.append('.');
            } else {
                sb.append((char) barray[start + i]);
            }
            if (++wrap == width) {
                wrap = 0;
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns the String version (dotted decimal notation) 
     * of an IPv4 address.
     * 
     * @param addr
     * @return
     */
    public static String toIPV4Address(byte[] addr) {
        if (addr == null || addr.length != 4) {
            throw new IllegalArgumentException("Invalid IPv4 address.");
        }
        String s = new String();
        for (int i = 0; i < 3; i++) {
            s += Integer.toString(addr[i] & 0xFF) + ".";
        }
        return s += Integer.toString(addr[3] & 0xFF);
    }

    /**
     * Returns the addresses of this Ethernet frame.
     */
    public static String getEthernetAddress(PacketInputStream input) throws IOException {
        byte[] addr = new byte[6];
        input.readFully(addr);
        return toEthernetAddress(addr);
    }

    /**
     * Reads and returns the String version of an IPV4 address.
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public static String getIPV4Address(PacketInputStream input) throws IOException {
        byte[] addr = new byte[4];
        input.readFully(addr);
        return toIPV4Address(addr);
    }

    /**
     * Given a byte array (the length must be 6 bytes), returns
     * a formatted string with the Ethernet address. The string
     * is of the form XX:XX:XX:XX:XX:XX where each X is a hexadecimal
     * digit.
     * 
     * @param addr
     * @return
     */
    public static String toEthernetAddress(byte[] addr) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            ret.append(Utils.toHex(addr[i]));
            if (i < 5) ret.append(":");
        }
        return ret.toString();
    }

    /**
     * Compares two byte arrays. Returns true if all the bytes in the given range compare
     * equal, false otherwise.
     * 
     * @param src The source byte array.
     * @param sOffset The offset in the source byte array from which to start the compare.
     * @param target The target byte array.
     * @param tOffset The offset in the target byte array from which to start the compare.
     * @param len The number of bytes to compare.
     * 
     * @return True if all the bytes compare equal. False otherwise.
     * 
     * @exception ArrayIndexOutOfBoundsException if the comparison results in a array index overrun.
     * @exception NullPointerException if either byte array is null.
     */
    public static boolean compareBytes(byte[] src, int sOffset, byte[] target, int tOffset, int len) {
        for (int i = 0; i < len; i++) {
            if (src[sOffset + i] != target[tOffset + i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert a given short in the little endian form to big endian
     * form.
     */
    public static short toBigEndian(short s) {
        short result = 0;
        result |= (byte) (s & 0xFF);
        result <<= 8;
        result |= (byte) ((s >>> 8) & 0xFF);
        return result;
    }

    /**
     * Convert a given int in the little endian form to the big endian
     * form.
     */
    public static int toBigEndian(int i) {
        int result = 0;
        int b0, b1, b2, b3;
        b3 = (i >>> 24) & 0xFF;
        b2 = (i >>> 16) & 0xFF;
        b1 = (i >>> 8) & 0xFF;
        b0 = i & 0xFF;
        result |= (b1 & 0xFF) << 24;
        result |= (b0 & 0xFF) << 16;
        result |= (b3 & 0xFF);
        result |= (b2 & 0xFF) << 8;
        return result;
    }

    /**
     * Convert a given short in the big endian form to the little
     * endian form.
     */
    public static short toLittleEndian(short s) {
        int result = 0;
        byte hi, lo;
        hi = (byte) ((s >>> 8) & 0xFF);
        lo = (byte) (s & 0xFF);
        result |= (hi & 0xFF);
        result |= (((int) lo) & 0xFF) << 8;
        return (short) result;
    }

    /**
     * Convert a given int in the big endian form to the little
     * endian form.
     */
    public static int toLittleEndian(int i) {
        int result = 0;
        int b0, b1, b2, b3;
        b0 = i & 0xFF;
        b1 = (i >>> 8) & 0xFF;
        b2 = (i >>> 16) & 0xFF;
        b3 = (i >>> 24) & 0xFF;
        result |= (b1 & 0xFF);
        result |= (b0 & 0xFF) << 8;
        result |= (b3 & 0xFF) << 16;
        result |= (b2 & 0xFF) << 24;
        return result;
    }

    public static void main(String[] args) throws Throwable {
        String str = new String("Hello World. \r\nHello Universe, Hello Simmi.");
        System.out.println(toHex(str.getBytes(), true));
        System.out.println(prettyPrint(str.getBytes(), 0, str.length(), 15));
    }
}
