package net.sf.jnclib.tp.ssh2;

import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.*;
import net.sf.jnclib.tp.ssh2.ber.BERInputStream;
import net.sf.jnclib.tp.ssh2.ber.BEROutputStream;
import net.sf.jnclib.tp.ssh2.crai.Crai;

/**
 * Dumb little utility functions.
 */
public class Util {

    private Util() {
    }

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    /**
     * Replace non-ASCII characters in a string with either a hex quote of the
     * form <code>\\xHH</code> or (for chars above 255) a unicode quote of the
     * form <code>\\uHHHH</code>.  Existing backslashes are quoted too.  The
     * resulting string will only contain chars between 32 and 126.
     * 
     * @param s the string to quote
     * @return a string safe to print or log
     */
    public static final String safeString(String s) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < 32) || ((c > 126) && (c < 256))) {
                String hex = Integer.toHexString(c);
                while (hex.length() < 2) {
                    hex = "0" + hex;
                }
                out.append("\\x" + hex);
            } else if (c >= 256) {
                String hex = Integer.toHexString(c);
                while (hex.length() < 4) {
                    hex = "0" + hex;
                }
                out.append("\\u" + hex);
            } else if (c == '\\') {
                out.append("\\\\");
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    public static final String[] splitString(String orig, String delim, int max) {
        List out = new ArrayList();
        while (true) {
            if (out.size() == max - 1) {
                break;
            }
            int n = orig.indexOf(delim);
            if (n < 0) {
                break;
            }
            out.add(orig.substring(0, n));
            orig = orig.substring(n + delim.length());
        }
        out.add(orig);
        return (String[]) out.toArray(new String[0]);
    }

    public static final String[] splitString(String orig, String delim) {
        return splitString(orig, delim, -1);
    }

    public static final String[] getStackTrace(Exception x) {
        StringWriter sw = new StringWriter();
        x.printStackTrace(new PrintWriter(sw));
        return splitString(sw.toString(), "\n");
    }

    public static int fuzzyInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException x) {
            return 0;
        }
    }

    /**
     * Decode a string of hex digits into a byte array.  For example, the
     * string <code>"FF3C"</code> would be decoded into the byte array
     * <code>new byte[] { -1, 0x3c }</code>.  This is mostly just used for
     * unit tests.
     * 
     * @param s the hex string to decode
     * @return the decoded byte array
     */
    public static final byte[] decodeHex(String s) {
        byte[] out = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            out[i / 2] = (byte) Integer.parseInt(s.substring(i, i + 2), 16);
        }
        return out;
    }

    public static final String encodeHex(byte[] x, int off, int len) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < len; i++) {
            String s = Integer.toHexString((int) x[off + i] & 0xff).toUpperCase();
            if (s.length() < 2) {
                out.append('0');
            }
            out.append(s);
        }
        return out.toString();
    }

    public static final String encodeHex(byte[] x) {
        return encodeHex(x, 0, x.length);
    }

    public static final String join(String[] list, String glue) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < list.length; i++) {
            if (i > 0) {
                out.append(glue);
            }
            out.append(list[i]);
        }
        return out.toString();
    }

    /**
     * Incredibly lazy method for decoding BER sequences from private key
     * files, knowing that they're always a single sequence of ints.
     * 
     * @param data a byte array to decode
     * @return an array of BigIntegers representing the key data
     */
    public static BigInteger[] decodeBERSequence(byte[] data) throws SSHException {
        Object obj;
        try {
            obj = BERInputStream.decode(data);
        } catch (IOException x) {
            throw new SSHException("BER decoding error: " + x);
        }
        if (!(obj instanceof List)) {
            throw new SSHException("Expected BER sequence");
        }
        List list = (List) obj;
        for (int i = 0; i < list.size(); i++) {
            if (!(list.get(i) instanceof BigInteger)) {
                throw new SSHException("Expected integer at BER sequence element " + i);
            }
        }
        BigInteger[] nums = new BigInteger[list.size()];
        list.toArray(nums);
        return nums;
    }

    public static byte[] encodeBERSequence(BigInteger[] nums) throws SSHException {
        List list = Arrays.asList(nums);
        try {
            return BEROutputStream.encode(list, false);
        } catch (IOException x) {
            throw new SSHException("BER encoding error: " + x);
        }
    }

    /**
     * Continue reading from a stream until EOF is reached, or the requested
     * number of bytes is read.
     * 
     * @param in the stream to read from
     * @param buffer the buffer to fill
     * @param off offset within the buffer to begin reading into
     * @param len number of bytes to read
     * @return number of bytes actually read (will only be less than the
     *     requested number at EOF)
     * @throws IOException if an IOException occurred while reading from
     *     the stream
     */
    public static int readAll(InputStream in, byte[] buffer, int off, int len) throws IOException {
        int soFar = 0;
        while (soFar < len) {
            int n = in.read(buffer, off + soFar, len - soFar);
            if (n < 0) {
                return soFar;
            }
            soFar += n;
        }
        return len;
    }

    public static int readAll(InputStream in, byte[] buffer) throws IOException {
        return readAll(in, buffer, 0, buffer.length);
    }

    public static BigInteger rollRandom(Crai crai, BigInteger max) {
        int bits = max.subtract(BigInteger.ONE).bitLength();
        int bytes = (bits + 7) / 8;
        int hbyte_mask = (1 << (bits % 8)) - 1;
        while (true) {
            byte[] x = new byte[bytes];
            crai.getPRNG().getBytes(x);
            if (hbyte_mask > 0) {
                x[0] = (byte) (x[0] & hbyte_mask);
            }
            BigInteger num = new BigInteger(1, x);
            if (num.compareTo(max) < 0) {
                return num;
            }
        }
    }

    public static String strip(String s) {
        int len = s.length();
        int start = 0;
        while ((start < len) && Character.isWhitespace(s.charAt(start))) {
            start++;
        }
        int end = len - 1;
        while ((end >= start) && Character.isWhitespace(s.charAt(end))) {
            end--;
        }
        if (end < start) {
            return "";
        }
        if ((end == len - 1) && (start == 0)) {
            return s;
        }
        return s.substring(start, end + 1);
    }

    public static byte[] toBytesFromString(String string) {
        return decodeHex(string);
    }

    /**
	    * <p>Returns a string of 8 hexadecimal digits (most significant digit first)
	    * corresponding to the unsigned integer <code>n</code>.</p>
	    *
	    * @param n the unsigned integer to convert.
	    * @return a hexadecimal string 8-character long.
	    */
    public static String toString(int n) {
        char[] buf = new char[8];
        for (int i = 7; i >= 0; i--) {
            buf[i] = HEX_DIGITS[n & 0x0F];
            n >>>= 4;
        }
        return new String(buf);
    }

    /**
	    * <p>Returns a string of hexadecimal digits from an integer array. Each int
	    * is converted to 4 hex symbols.</p>
	    */
    public static String toString(int[] ia) {
        int length = ia.length;
        char[] buf = new char[length * 8];
        for (int i = 0, j = 0, k; i < length; i++) {
            k = ia[i];
            buf[j++] = HEX_DIGITS[(k >>> 28) & 0x0F];
            buf[j++] = HEX_DIGITS[(k >>> 24) & 0x0F];
            buf[j++] = HEX_DIGITS[(k >>> 20) & 0x0F];
            buf[j++] = HEX_DIGITS[(k >>> 16) & 0x0F];
            buf[j++] = HEX_DIGITS[(k >>> 12) & 0x0F];
            buf[j++] = HEX_DIGITS[(k >>> 8) & 0x0F];
            buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
            buf[j++] = HEX_DIGITS[k & 0x0F];
        }
        return new String(buf);
    }

    /**
	    * <p>Returns a string of 16 hexadecimal digits (most significant digit first)
	    * corresponding to the unsigned long <code>n</code>.</p>
	    *
	    * @param n the unsigned long to convert.
	    * @return a hexadecimal string 16-character long.
	    */
    public static String toString(long n) {
        char[] b = new char[16];
        for (int i = 15; i >= 0; i--) {
            b[i] = HEX_DIGITS[(int) (n & 0x0FL)];
            n >>>= 4;
        }
        return new String(b);
    }

    /**
	    * <p>Returns a string of hexadecimal digits from a byte array. Each byte is
	    * converted to 2 hex symbols; zero(es) included.</p>
	    *
	    * <p>This method calls the method with same name and three arguments as:</p>
	    *
	    * <pre>
	    *    toString(ba, 0, ba.length);
	    * </pre>
	    *
	    * @param ba the byte array to convert.
	    * @return a string of hexadecimal characters (two for each byte)
	    * representing the designated input byte array.
	    */
    public static String toString(byte[] ba) {
        return toString(ba, 0, ba.length);
    }

    /**
	    * <p>Returns a string of hexadecimal digits from a byte array, starting at
	    * <code>offset</code> and consisting of <code>length</code> bytes. Each byte
	    * is converted to 2 hex symbols; zero(es) included.</p>
	    *
	    * @param ba the byte array to convert.
	    * @param offset the index from which to start considering the bytes to
	    * convert.
	    * @param length the count of bytes, starting from the designated offset to
	    * convert.
	    * @return a string of hexadecimal characters (two for each byte)
	    * representing the designated input byte sub-array.
	    */
    public static final String toString(byte[] ba, int offset, int length) {
        char[] buf = new char[length * 2];
        for (int i = 0, j = 0, k; i < length; ) {
            k = ba[offset + i++];
            buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
            buf[j++] = HEX_DIGITS[k & 0x0F];
        }
        return new String(buf);
    }
}
