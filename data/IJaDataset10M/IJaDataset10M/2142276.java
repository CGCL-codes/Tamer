package net.hawk.digiextractor.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * A Helper class to decode Strings from descriptors.
 * 
 * @author Hawk
 */
public final class Helper {

    /**
	 * 
	 */
    private static final int DECIMAL_PLACE = 10;

    /**
	 * 
	 */
    private static final int NIBBLE_WIDTH = 4;

    /**
	 * 
	 */
    private static final int HIGH_NIBBLE_MASK = 0xf0;

    /**
	 * 
	 */
    private static final int LOW_NIBBLE_MASK = 0x0f;

    /**
	 * 
	 */
    private static final String RESERVED = "reserved";

    /**
	 * 
	 */
    private static final String ISO_8859_1 = "ISO-8859-1";

    /**
	 * 
	 */
    private static final int BYTE_MASK = 0xff;

    /**
	 * 
	 */
    private static final int ENCODING_LIMIT = 0x20;

    /** The Constant ENCODING. */
    private static final String[] ENCODING = { ISO_8859_1, "ISO-8859-5", "ISO-8859-6", "ISO-8859-7", "ISO-8859-8", "ISO-8859-9", ISO_8859_1, ISO_8859_1, ISO_8859_1, "ISO-8859-13", ISO_8859_1, "ISO-8859-15", RESERVED, RESERVED, RESERVED, RESERVED, ISO_8859_1, RESERVED, RESERVED, RESERVED, RESERVED, "UTF-8", RESERVED, RESERVED, RESERVED, RESERVED, RESERVED, RESERVED, RESERVED, RESERVED, RESERVED, RESERVED };

    /**
	 * Instantiates a new character helper.
	 */
    private Helper() {
    }

    /**
	 * Parses the character array.
	 * 
	 * @see EN 300 468 (Annex A)
	 * 
	 * @param chars the chars
	 * 
	 * @return the string
	 * 
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
    public static String parseCharacterArray(final byte[] chars) throws UnsupportedEncodingException {
        String result;
        if (chars.length == 0) {
            result = "";
        } else {
            final int marker = (int) (chars[0] & BYTE_MASK);
            if (marker < ENCODING_LIMIT) {
                result = new String(chars, 1, chars.length - 1, ENCODING[marker]);
            } else {
                result = new String(chars, 0, chars.length, ISO_8859_1);
            }
        }
        return result;
    }

    /**
	 * Gets a String (Null terminated) from the given Byte Buffer.
	 * @param b the Buffer containing the String.
	 * @return the string.
	 */
    public static String getStringFromBuffer(final ByteBuffer b) {
        StringBuffer sb = new StringBuffer();
        byte tmp;
        do {
            tmp = b.get();
            if ((tmp & BYTE_MASK) >= ENCODING_LIMIT) {
                sb.append((char) (tmp & BYTE_MASK));
            }
        } while (tmp != 0x00);
        return sb.toString();
    }

    /**
	 * Bcd to dec.
	 * 
	 * @param i the bcd
	 * 
	 * @return the byte
	 */
    public static byte bcdToDec(final int i) {
        byte tmp = (byte) (((i & HIGH_NIBBLE_MASK) >> NIBBLE_WIDTH) * DECIMAL_PLACE);
        tmp += (i & LOW_NIBBLE_MASK);
        return tmp;
    }

    /**
	 * Read the version String from the version file.
	 * 
	 * @return the Version string.
	 */
    public static String getVersionString() {
        try {
            InputStream is = Class.class.getResourceAsStream("/net/hawk/digiextractor/resources/version");
            byte[] buf = new byte[is.available()];
            is.read(buf);
            return new String(buf);
        } catch (IOException e) {
            return "99.99.99";
        }
    }
}
