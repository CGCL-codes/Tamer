package com.modp.cida;

/**
 * This converts raw bytes into an all-ascii base-64 representation <i>that is
 * also safe for puting into a URL</i>. This allows adding binary data into a
 * URL in a space efficient maner.
 * 
 * <P>
 * Compare:
 * <ul>
 * <li>Base 16 encodes one byte into two hex digits (ln 256/ln16)
 * <li>Base 64 encodes 3 bytes into 4 chars (ln 256/ln 64)
 * </ul>
 * 
 * <p>
 * The code is heavily modified from the original source of
 * <a href="ftp://ftp.oreilly.com/pub/examples/java/crypto/files/oreilly/jonathan/util/Base64.java">
 * ftp://ftp.oreilly.com/pub/examples/java/crypto/files/oreilly/jonathan/util/Base64.java
 * </a> That code has no copyright or license, so it is assumed to be public
 * domain.
 * 
 * <p>
 * The standard specification for base 64 is in RFC 1521, however this need to
 * be altered in ordered to be used in URL without character being mangled or
 * misinterpreted.
 * 
 * <p>
 * In to-spec Base64 encoding, the alphabet consists of a-z, A-Z, 0-9, '+', '/'
 * and '=' is used for padding
 * 
 * <p>
 * For URLs and form-posts the mime type of "www-form-urlencoded" is used and
 * defines certain transformations:
 * <ul>
 * <li>The "+" may be decoded as a space ' '
 * <li>The "/" will be encoded with "%2f"
 * <li>The "=" may be encoded with "%3d"
 * <li>While '.', '-', '*' and '_' are safe and not touched.
 * </ul>
 * 
 * <p>
 * While using '.' in the spec if safe, many proxies and servers may
 * misinterpret this as a filename reference. Likewise I've seen some
 * implementations use a tilde "~", but again this is a special character used
 * in user directory mappings and is not safe according to spec.
 * 
 * <p>
 * Therefore in this modified alphabet defined in this class:
 * <ul>
 * <li>"+" --> "-" (62)
 * <li>"/" --> "_" (63)
 * <li>"=" --> "*" padding
 * </ul>
 * makes the base64 encoding safe for inclusion into urls.
 * 
 * @author Nick Galbreath
 * @version 1.0.1
 *  
 */
public class Base64URLSafe {

    /**
	 * Character to use to pad blocks. The spec is '=', but this code is using
	 * a '*' to be url friendly
	 */
    protected static final char PAD_CHAR = '*';

    /**
	 * The letter 62. The spec is '+', but here it is a '-' to be url friendly
	 */
    protected static final char LETTER_62 = '-';

    /**
	 * The letter 63 The spec is '/', here it's '_' to be web friendly
	 */
    protected static final char LETTER_63 = '_';

    /**
	 * Map used in encoding into base 64
	 */
    protected static final char[] encodeMap = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', LETTER_62, LETTER_63 };

    /**
	 * Map used in decoding base64 back into raw bytes
	 */
    protected static byte[] decodeMap = new byte[256];

    static {
        for (int i = 0; i < 256; i++) decodeMap[i] = -1;
        for (int i = 'A'; i <= 'Z'; i++) decodeMap[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++) decodeMap[i] = (byte) (26 + i - 'a');
        for (int i = '0'; i <= '9'; i++) decodeMap[i] = (byte) (52 + i - '0');
        decodeMap[LETTER_62] = 62;
        decodeMap[LETTER_63] = 63;
    }

    /**
	 * Encode a raw binary array into a URL-safe format
	 * 
	 * @param raw
	 *            The raw binary data to encode
	 * @return String A String encoding of the the data in URL-safe format
	 */
    public static String encode(byte[] raw) {
        int end = raw.length;
        int slop = end % 3;
        char[] buf = new char[(slop == 0) ? (4 * (end / 3)) : (4 * (end / 3 + 1))];
        int i = 0, j = 0;
        end = end - slop;
        while (i < end) {
            int block = ((raw[i++] & 0xff) << 16) | ((raw[i++] & 0xff) << 8) | (raw[i++] & 0xff);
            buf[j++] = encodeMap[(block >>> 18) & 0x3f];
            buf[j++] = encodeMap[(block >>> 12) & 0x3f];
            buf[j++] = encodeMap[(block >>> 6) & 0x3f];
            buf[j++] = encodeMap[(block & 0x3f)];
        }
        if (slop == 2) {
            int block = ((raw[i++] & 0xff) << 16) | ((raw[i++] & 0xff) << 8);
            buf[j++] = encodeMap[(block >>> 18) & 0x3f];
            buf[j++] = encodeMap[(block >>> 12) & 0x3f];
            buf[j++] = encodeMap[(block >>> 6) & 0x3f];
            buf[j] = PAD_CHAR;
        } else if (slop == 1) {
            int block = (raw[i++] & 0xff) << 16;
            buf[j++] = encodeMap[(block >>> 18) & 0x3f];
            buf[j++] = encodeMap[(block >>> 12) & 0x3f];
            buf[j++] = PAD_CHAR;
            buf[j] = PAD_CHAR;
        }
        return new String(buf);
    }

    /**
	 * Decode a URL-safe encoded string back into it's raw binary format
	 * 
	 * @param base64String
	 *            The input string
	 * @return a byte[] array containing the original data
	 * @throws a
	 *             InvalidFormatException if the input String contains invalid
	 *             characters.
	 *  
	 */
    public static byte[] decode(String base64String) throws InvalidFormatException {
        char[] base64 = base64String.toCharArray();
        int pad = 0;
        int max = base64.length;
        if (max == 0) return new byte[0];
        for (int i = max - 1; base64[i] == PAD_CHAR; i--) pad++;
        byte[] r = new byte[max * 6 / 8 - pad];
        if (pad > 0) max = max - 4;
        int ri = 0, i = 0;
        while (i < max) {
            int block = (getValue(base64[i++]) << 18) | (getValue(base64[i++]) << 12) | (getValue(base64[i++]) << 6) | (getValue(base64[i++]));
            r[ri++] = (byte) ((block >> 16) & 0xff);
            r[ri++] = (byte) ((block >> 8) & 0xff);
            r[ri++] = (byte) (block & 0xff);
        }
        if (pad == 2) {
            int block = (getValue(base64[i++]) << 18) | (getValue(base64[i++]) << 12);
            r[ri++] = (byte) ((block >> 16) & 0xff);
        } else if (pad == 1) {
            int block = (getValue(base64[i++]) << 18) | (getValue(base64[i++]) << 12) | (getValue(base64[i++]) << 6);
            r[ri++] = (byte) ((block >> 16) & 0xff);
            r[ri++] = (byte) ((block >> 8) & 0xff);
        }
        return r;
    }

    /**
	 * This is the a small function to do the table look up during decoding.
	 * You will probably want to change the RuntimeException into a
	 * CheckedException of some type
	 */
    protected static int getValue(char c) throws InvalidFormatException {
        byte x = decodeMap[c];
        if (x == -1) {
            throw new InvalidFormatException("Bad base64 character of value " + (int) c + " found in decode");
        }
        return x;
    }
}
