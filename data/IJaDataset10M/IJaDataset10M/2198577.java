package jacky.lanlan.song.util.codec;

/**
 * Encodes data to Base64 and decodes it.
 * 
 * @author Stefan Laubenberger
 * @version 20080810
 */
public abstract class Base64Codec {

    private static final String RES_ILLEGAL_CHARACTER = "ConverterBase64.illegal";

    private static final String RES_INVALID_STRING = "ConverterBase64.invalid";

    private static final char[] map1 = new char[64];

    private static final byte[] map2 = new byte[128];

    static {
        int ii = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            map1[ii++] = c;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            map1[ii++] = c;
        }
        for (char c = '0'; c <= '9'; c++) {
            map1[ii++] = c;
        }
        map1[ii++] = '+';
        map1[ii] = '/';
    }

    static {
        for (int ii = 0; ii < map2.length; ii++) {
            map2[ii] = (byte) -1;
        }
        for (int ii = 0; ii < 64; ii++) {
            map2[map1[ii]] = (byte) ii;
        }
    }

    /**
	 * Encodes a string into Base64 format. No blanks or line breaks are inserted.
	 * 
	 * @param string
	 *          String to be encoded.
	 * @return String with the Base64 encoded data.
	 */
    public static String encode(final String string) {
        final String str = new String(encode(string.getBytes()));
        return str;
    }

    /**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *          Array containing the data bytes to be encoded.
	 * @return Character array with the Base64 encoded data.
	 */
    public static char[] encode(final byte[] in) {
        final char[] result = encode(in, in.length);
        return result;
    }

    /**
	 * Decodes a byte array from Base64 format.
	 * 
	 * @param string
	 *          Base64 String to be decoded.
	 * @return Array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 */
    public static byte[] decode(final String string) {
        final byte[] result = decode(string.toCharArray());
        return result;
    }

    /**
	 * Decodes a byte array from Base64 format. No blanks or line breaks are
	 * allowed within the Base64 encoded data.
	 * 
	 * @param in
	 *          Character array containing the Base64 encoded data.
	 * @return Array containing the decoded data bytes.
	 * @throws IllegalArgumentException
	 */
    public static byte[] decode(final char[] in) {
        int iLen = in.length;
        if (iLen % 4 != 0) {
            throw new IllegalArgumentException(RES_INVALID_STRING);
        }
        while (iLen > 0 && in[iLen - 1] == '=') {
            iLen--;
        }
        final int oLen = (iLen * 3) / 4;
        final byte[] out = new byte[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
            final int i0 = in[ip++];
            final int i1 = in[ip++];
            final int i2 = (ip < iLen ? in[ip++] : 'A');
            final int i3 = (ip < iLen ? in[ip++] : 'A');
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
                throw new IllegalArgumentException(RES_ILLEGAL_CHARACTER);
            }
            final int b0 = map2[i0];
            final int b1 = map2[i1];
            final int b2 = map2[i2];
            final int b3 = map2[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
                throw new IllegalArgumentException(RES_ILLEGAL_CHARACTER);
            }
            final int o0 = b0 << 2 | b1 >>> 4;
            final int o1 = (b1 & 0xf) << 4 | b2 >>> 2;
            final int o2 = (b2 & 3) << 6 | b3;
            out[op++] = (byte) o0;
            if (op < oLen) {
                out[op++] = (byte) o1;
            }
            if (op < oLen) {
                out[op++] = (byte) o2;
            }
        }
        return out;
    }

    /**
	 * Encodes a byte array into Base64 format. No blanks or line breaks are
	 * inserted.
	 * 
	 * @param in
	 *          Array containing the data bytes to be encoded.
	 * @param iLen
	 *          Number of bytes to process in <code>in</code>.
	 * @return Character array with the Base64 encoded data.
	 */
    private static char[] encode(final byte[] in, final int iLen) {
        final int oDataLen = (iLen * 4 + 2) / 3;
        final int oLen = ((iLen + 2) / 3) * 4;
        final char[] out = new char[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
            final int i0 = in[ip++] & 0xff;
            final int i1 = ip < iLen ? in[ip++] & 0xff : 0;
            final int i2 = ip < iLen ? in[ip++] & 0xff : 0;
            final int o0 = i0 >>> 2;
            final int o1 = (i0 & 3) << 4 | i1 >>> 4;
            final int o2 = (i1 & 0xf) << 2 | i2 >>> 6;
            final int o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            op++;
            out[op] = op < oDataLen ? map1[o3] : '=';
            op++;
        }
        return out;
    }
}
