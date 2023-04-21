package sasc.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.BitSet;
import java.util.StringTokenizer;

/**
 *
 * @author sasc
 */
public class Util {

    public static String getSpaces(int length) {
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            buf.append(" ");
        }
        return buf.toString();
    }

    public static String prettyPrintHex(String in, int indent, boolean wrapLines) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            buf.append(c);
            int nextPos = i + 1;
            if (wrapLines && nextPos % 32 == 0 && nextPos != in.length()) {
                buf.append("\n").append(getSpaces(indent));
            } else if (nextPos % 2 == 0 && nextPos != in.length()) {
                buf.append(" ");
            }
        }
        return buf.toString();
    }

    public static String prettyPrintHex(String in, int indent) {
        return prettyPrintHex(in, indent, true);
    }

    public static String prettyPrintHex(byte[] data) {
        return Util.prettyPrintHex(Util.byteArrayToHexString(data), 0, true);
    }

    public static String prettyPrintHexNoWrap(byte[] data) {
        return Util.prettyPrintHex(Util.byteArrayToHexString(data), 0, false);
    }

    public static String prettyPrintHexNoWrap(String in) {
        return Util.prettyPrintHex(in, 0, false);
    }

    public static String prettyPrintHex(String in) {
        return prettyPrintHex(in, 0, true);
    }

    public static String prettyPrintHex(BigInteger bi) {
        byte[] data = bi.toByteArray();
        if (data[0] == (byte) 0x00) {
            byte[] tmp = new byte[data.length - 1];
            System.arraycopy(data, 1, tmp, 0, data.length - 1);
            data = tmp;
        }
        return prettyPrintHex(data);
    }

    public static byte[] performRSA(byte[] dataBytes, byte[] expBytes, byte[] modBytes) {
        int inBytesLength = dataBytes.length;
        if (expBytes[0] >= (byte) 0x80) {
            byte[] tmp = new byte[expBytes.length + 1];
            tmp[0] = (byte) 0x00;
            System.arraycopy(expBytes, 0, tmp, 1, expBytes.length);
            expBytes = tmp;
        }
        if (modBytes[0] >= (byte) 0x80) {
            byte[] tmp = new byte[modBytes.length + 1];
            tmp[0] = (byte) 0x00;
            System.arraycopy(modBytes, 0, tmp, 1, modBytes.length);
            modBytes = tmp;
        }
        if (dataBytes[0] >= (byte) 0x80) {
            byte[] tmp = new byte[dataBytes.length + 1];
            tmp[0] = (byte) 0x00;
            System.arraycopy(dataBytes, 0, tmp, 1, dataBytes.length);
            dataBytes = tmp;
        }
        BigInteger exp = new BigInteger(expBytes);
        BigInteger mod = new BigInteger(modBytes);
        BigInteger data = new BigInteger(dataBytes);
        byte[] result = data.modPow(exp, mod).toByteArray();
        if (result.length == (inBytesLength + 1) && result[0] == (byte) 0x00) {
            byte[] tmp = new byte[inBytesLength];
            System.arraycopy(result, 1, tmp, 0, inBytesLength);
            result = tmp;
        }
        return result;
    }

    public static byte[] calculateSHA1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        return sha1.digest(data);
    }

    public static String byte2Hex(byte b) {
        String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        int nb = b & 0xFF;
        int i_1 = (nb >> 4) & 0xF;
        int i_2 = nb & 0xF;
        return HEX_DIGITS[i_1] + HEX_DIGITS[i_2];
    }

    public static String short2Hex(short s) {
        byte b1 = (byte) (s >> 8);
        byte b2 = (byte) (s & 0xFF);
        return byte2Hex(b1) + byte2Hex(b2);
    }

    public static int byteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static int byteToInt(byte first, byte second) {
        int value = (first & 0xFF) << 8;
        value += second & 0xFF;
        return value;
    }

    public static short byte2Short(byte b1, byte b2) {
        return (short) ((b1 << 8) | (b2 & 0xFF));
    }

    public static String getFormattedNanoTime(long nano) {
        StringBuilder buf = new StringBuilder();
        buf.append((int) (nano / 1000000));
        buf.append("ms ");
        buf.append(nano % 1000000);
        buf.append("ns");
        return buf.toString();
    }

    public static String getSafePrintChars(byte[] in) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < in.length; i++) {
            if (in[i] >= (byte) 0x20 && in[i] < (byte) 0x7F) {
                buf.append((char) in[i]);
            } else {
                buf.append(".");
            }
        }
        return buf.toString();
    }

    /**
     * Converts a byte array into a hex string.
     * @param byteArray the byte array source
     * @return a hex string representing the byte array
     */
    public static String byteArrayToHexString(final byte[] byteArray) {
        if (byteArray == null) {
            throw new IllegalArgumentException("Argument 'byteArray' cannot be null");
        }
        int readBytes = byteArray.length;
        StringBuilder hexData = new StringBuilder();
        int onebyte;
        for (int i = 0; i < readBytes; i++) {
            onebyte = ((0x000000ff & byteArray[i]) | 0xffffff00);
            hexData.append(Integer.toHexString(onebyte).substring(6));
        }
        return hexData.toString();
    }

    public static String int2Hex(int i) {
        String hex = Integer.toHexString(i);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * The length of the returned array depends on the size of the int
     * @param value
     * @return
     */
    public static byte[] intToByteArray(int value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte one = (byte) (value >>> 24);
        byte two = (byte) (value >>> 16);
        byte three = (byte) (value >>> 8);
        byte four = (byte) (value);
        boolean found = false;
        if (one > 0x00) {
            baos.write(one);
            found = true;
        }
        if (found || two > 0x00) {
            baos.write(two);
            found = true;
        }
        if (found || three > 0x00) {
            baos.write(three);
            found = true;
        }
        baos.write(four);
        return baos.toByteArray();
    }

    /**
     * Returns a byte array with length = 4
     * @param value
     * @return
     */
    public static byte[] intToByteArray4(int value) {
        return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
    }

    public static int byteArrayToInt(byte[] b) {
        if (b == null) {
            throw new IllegalArgumentException("Parameter cannot be null");
        }
        if (b.length == 0 || b.length > 4) {
            throw new IllegalArgumentException("Array length must be between 1 and 4. Length = " + b.length);
        }
        int value = 0;
        for (int i = 0; i < b.length; i++) {
            value += ((b[i] & 0xFF) << 8 * (b.length - i - 1));
        }
        return value;
    }

    public static byte[] fromHexString(String encoded) {
        encoded = removeSpaces(encoded);
        if ((encoded.length() % 2) != 0) {
            throw new IllegalArgumentException("Input string must contain an even number of characters");
        }
        final byte result[] = new byte[encoded.length() / 2];
        final char enc[] = encoded.toCharArray();
        for (int i = 0; i < enc.length; i += 2) {
            StringBuilder curr = new StringBuilder(2);
            curr.append(enc[i]).append(enc[i + 1]);
            result[i / 2] = (byte) Integer.parseInt(curr.toString(), 16);
        }
        return result;
    }

    public static String removeCRLFTab(String s) {
        StringTokenizer st = new StringTokenizer(s, "\r\n\t", false);
        StringBuilder buf = new StringBuilder();
        while (st.hasMoreElements()) {
            buf.append(st.nextElement());
        }
        return buf.toString();
    }

    public static String removeSpaces(String s) {
        StringTokenizer st = new StringTokenizer(s, " ", false);
        StringBuilder buf = new StringBuilder();
        while (st.hasMoreElements()) {
            buf.append(st.nextElement());
        }
        return buf.toString();
    }

    public static String readInputStreamToString(InputStream is, String encoding) throws IOException {
        InputStreamReader input = new InputStreamReader(is, encoding);
        final int CHARS_PER_PAGE = 5000;
        final char[] buffer = new char[CHARS_PER_PAGE];
        StringBuilder output = new StringBuilder(CHARS_PER_PAGE);
        for (int read = input.read(buffer, 0, buffer.length); read != -1; read = input.read(buffer, 0, buffer.length)) {
            output.append(buffer, 0, read);
        }
        String text = output.toString();
        return text;
    }

    public static void writeStringToFile(String string, String fileName, boolean append) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName, append));
        out.write(string);
        out.close();
    }

    /**
     * This method converts the literal hex representation of a byte to an int.
     * eg 0x70 = 70 (int)
     * @param b
     */
    public static int numericByteToInt(byte b) {
        String hex = Util.byte2Hex(b);
        try {
            return Integer.parseInt(hex);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("The hex representation of argument b must be digits", ex);
        }
    }

    /**
     * This method converts the literal hex representation of a byte to an int.
     * eg 0x70 = 70 (int)
     * @param hex
     */
    public static int numericHexToInt(String hex) {
        hex = Util.removeSpaces(hex);
        if (hex.length() > 8) {
            throw new IllegalArgumentException("There must be a maximum of 4 hex octets. hex=" + hex);
        }
        try {
            return Integer.parseInt(hex);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Argument hex must be all digits. hex=" + hex, ex);
        }
    }

    /**
     * This returns a String with length = 8
     * @param val
     * @return
     */
    public static String byte2BinaryLiteral(byte val) {
        String s = Integer.toBinaryString(Util.byteToInt(val));
        if (s.length() < 8) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8 - s.length(); i++) {
                sb.append('0');
            }
            sb.append(s);
            s = sb.toString();
        }
        return s;
    }

    /**
     * Returns a bitset containing the values in bytes.
     * The byte-ordering of bytes must be big-endian which means the most significant bit is in element 0.
     *
     * @param bytes
     * @return
     */
    public static BitSet byteArray2BitSet(byte[] bytes) {
        BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
                bits.set(i);
            }
        }
        return bits;
    }

    public static byte[] bitSet2ByteArray(BitSet bits) {
        byte[] bytes = new byte[bits.length() / 8 + 1];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }

    /**
     *
     * @param val
     * @param bitPos The leftmost bit is 8 (the most significant bit)
     * @return
     */
    public static boolean isBitSet(byte val, int bitPos) {
        if (bitPos < 1 || bitPos > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + bitPos);
        }
        if ((val >> (bitPos - 1) & 0x1) == 1) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param data
     * @param bitPos The leftmost bit is 8
     * @param on
     * @return
     */
    public static byte setBit(byte data, int bitPos, boolean on) {
        if (bitPos < 1 || bitPos > 8) {
            throw new IllegalArgumentException("parameter 'bitPos' must be between 1 and 8. bitPos=" + bitPos);
        }
        if (on) {
            return data |= 1 << (bitPos - 1);
        } else {
            return data &= ~(1 << (bitPos - 1));
        }
    }

    public static byte[] generateRandomBytes(int numBytes) {
        byte[] rndBytes = new byte[numBytes];
        SecureRandom random = new SecureRandom();
        random.nextBytes(rndBytes);
        return rndBytes;
    }

    public static void main(String[] args) {
        System.out.println(Util.isBitSet((byte) 0x5f, 2));
        System.out.println(Util.isBitSet((byte) 0x9f, 2));
        System.out.println(Util.byte2Short((byte) 0x6F, (byte) 0xEF));
        System.out.println(Util.short2Hex(Util.byte2Short((byte) 0x6F, (byte) 0xEF)));
        System.out.println(Util.byteArrayToInt(new byte[] { (byte) 0x6F, (byte) 0xEF }));
        System.out.println(Util.byteArrayToHexString(Util.intToByteArray(28655)));
        System.out.println(Util.byte2BinaryLiteral((byte) 0x00));
        System.out.println(Util.byte2BinaryLiteral((byte) 0x3F));
        System.out.println(Util.byte2BinaryLiteral((byte) 0x80));
        System.out.println(Util.byte2BinaryLiteral((byte) 0xAA));
        System.out.println(Util.byte2BinaryLiteral((byte) 0xFF));
        System.out.println(Util.byte2BinaryLiteral((byte) 0x8A));
        System.out.println(Util.byte2BinaryLiteral(Util.setBit((byte) 0x8A, 5, true)));
        System.out.println(Util.byte2BinaryLiteral(Util.setBit((byte) 0x8A, 8, false)));
    }
}
