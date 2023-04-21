package cc.slx.java.base64;

/**
 * Decodes from Base64 notation.
 *
 * @author Robert Harder
 * @author John Comeau
 * @author Thomas Pedley
 */
public class Base64 {

    /** 
	 * Translates a Base64 value to either its 6-bit reconstruction value
	 * or a negative number indicating some other meaning.
	 */
    private static final byte[] DECODE_ALPHABET = { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9 };

    /** String representation of the base64 alphabet. */
    public static String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    /**
	 * Private constructor to stop instantiation.
	 */
    private Base64() {
    }

    /**
	 * Decodes four bytes from the passed in source array and writes the resulting bytes 
	 * (up to three of them) to the passed in destination array.
	 *<p>
	 * The source and destination arrays can be manipulated anywhere along their length by
	 * specifying the relevant offsets.  This method does not check to make sure your arrays 
	 * are large enough to accomodate the specified offsets.
	 *<p>
	 * This method returns the actual number of bytes that were converted from the Base64 
	 * encoding.  This is the lowest level of the decoding methods with all possible 
	 * parameters.</p>
	 *
	 * @param src The array to convert.
	 * @param off The offset into the source where conversion begins.
	 * @param dest The array to hold the conversion.
	 * @param seek How many bytes to seek into the destination before writing.
	 * @return The number of decoded bytes converted.
	 */
    private static int decode4to3(byte[] src, int off, byte[] dest, int seek) {
        if (src[off + 2] == (byte) '=') {
            int outBuff = ((DECODE_ALPHABET[src[off]] & 0xFF) << 18) | ((DECODE_ALPHABET[src[off + 1]] & 0xFF) << 12);
            dest[seek] = (byte) (outBuff >>> 16);
            return 1;
        } else if (src[off + 3] == (byte) '=') {
            int outBuff = ((DECODE_ALPHABET[src[off]] & 0xFF) << 18) | ((DECODE_ALPHABET[src[off + 1]] & 0xFF) << 12) | ((DECODE_ALPHABET[src[off + 2]] & 0xFF) << 6);
            dest[seek] = (byte) (outBuff >>> 16);
            dest[seek + 1] = (byte) (outBuff >>> 8);
            return 2;
        } else {
            try {
                int outBuff = ((DECODE_ALPHABET[src[off]] & 0xFF) << 18) | ((DECODE_ALPHABET[src[off + 1]] & 0xFF) << 12) | ((DECODE_ALPHABET[src[off + 2]] & 0xFF) << 6) | ((DECODE_ALPHABET[src[off + 3]] & 0xFF));
                dest[seek] = (byte) (outBuff >> 16);
                dest[seek + 1] = (byte) (outBuff >> 8);
                dest[seek + 2] = (byte) (outBuff);
                return 3;
            } catch (Exception e) {
                return -1;
            }
        }
    }

    /**
	 * Very low-level access to decoding ASCII characters in the form of a byte array. 
	 *
	 * @param src The Base64 encoded data.
	 * @param off The offset of where to begin decoding.
	 * @param len The length of characters to decode.
	 * @return Decoded data.
	 */
    public static byte[] decode(byte[] src, int off, int len) {
        byte[] buf = new byte[len * 3 / 4];
        int bufPtr = 0;
        byte[] chunk = new byte[4];
        int chunkPtr = 0;
        byte chunkByte = 0;
        byte decodeByte = 0;
        for (int i = off; i < off + len; i++) {
            chunkByte = (byte) (src[i] & 0x7f);
            decodeByte = DECODE_ALPHABET[chunkByte];
            if (decodeByte >= (byte) -5) {
                if (decodeByte >= (byte) -1) {
                    chunk[chunkPtr++] = chunkByte;
                    if (chunkPtr > 3) {
                        bufPtr += decode4to3(chunk, 0, buf, bufPtr);
                        chunkPtr = 0;
                        if (chunkByte == (byte) '=') break;
                    }
                }
            } else {
                return null;
            }
        }
        byte[] out = new byte[bufPtr];
        System.arraycopy(buf, 0, out, 0, bufPtr);
        return out;
    }

    /**
	 * Decodes data from Base64 notation.
	 *
	 * @param s The string to decode.
	 * @return The decoded data.
	 */
    public static byte[] decode(String s) {
        byte[] bytes;
        try {
            bytes = s.getBytes("UTF-8");
        } catch (Exception e) {
            bytes = s.getBytes();
        }
        return decode(bytes, 0, bytes.length);
    }

    /**
	 * Encode bytes into base64.
	 * 
	 * @param data The bytes to encode.
	 * @return The encoded data.
	 */
    public static String encode(byte[] data) {
        StringBuilder encoded = new StringBuilder(data.length);
        int paddingCount = (3 - (data.length % 3)) % 3;
        byte[] padded = new byte[data.length + paddingCount];
        System.arraycopy(data, 0, padded, 0, data.length);
        data = padded;
        for (int i = 0; i < data.length; i += 3) {
            int j = ((data[i] & 0xff) << 16) + ((data[i + 1] & 0xff) << 8) + (data[i + 2] & 0xff);
            encoded.append(base64code.charAt((j >> 18) & 0x3f));
            encoded.append(base64code.charAt((j >> 12) & 0x3f));
            encoded.append(base64code.charAt((j >> 6) & 0x3f));
            encoded.append(base64code.charAt(j & 0x3f));
        }
        return encoded.substring(0, encoded.length() - paddingCount) + "==".substring(0, paddingCount);
    }
}
