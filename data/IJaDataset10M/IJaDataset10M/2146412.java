package net.sf.solarnetwork.node.impl.sma;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Constants for SMA.
 *
 * @author matt
 * @version $Revision: 347 $ $Date: 2009-09-07 01:12:49 -0400 (Mon, 07 Sep 2009) $
 */
public final class SmaUtils {

    private static final Pattern STRING_TRIM_PAT = Pattern.compile("\\s+\\u0000?$");

    private SmaUtils() {
    }

    /**
	 * Encode the user data value for a {@code SmaCommand.GetData} request packet.
	 * 
	 * @param channel the channel to get the data for
	 * @return the user data value
	 */
    public static byte[] encodeGetDataRequestUserData(SmaChannel channel) {
        byte[] result = new byte[3];
        result[0] = (byte) channel.getType1().getCode();
        result[1] = (byte) channel.getType2();
        result[2] = (byte) channel.getIndex();
        return result;
    }

    /**
	 * Encode the user data value for a {@code SmaCommand.SetData} request packet
	 * for a specific channel and a integer value.
	 * 
	 * <p>For {@code Analog} channel types, the value will be encoded as a 2-byte
	 * value. Otherwise a 4-byte value will be used.</p>
	 * 
	 * @param channel the channel to set the data for
	 * @param value the data to encode
	 * @return the user data value (7 or 9 bytes long)
	 */
    public static byte[] encodeSetDataRequestUserData(SmaChannel channel, int value) {
        byte[] result = new byte[channel.getType1() == SmaChannelType.Analog ? 7 : 9];
        result[0] = (byte) channel.getType1().getCode();
        result[1] = (byte) channel.getType2();
        result[2] = (byte) channel.getIndex();
        result[3] = (byte) (channel.getType1() == SmaChannelType.Analog ? 1 : 2);
        result[4] = (byte) 0;
        result[5] = (byte) (0xFF & value);
        result[6] = (byte) (0xFF & (value >> 8));
        if (channel.getType1() != SmaChannelType.Analog) {
            result[7] = (byte) (0xFF & (value >> 16));
            result[8] = (byte) (0xFF & (value >> 24));
        }
        return result;
    }

    /**
	 * Parse an IEEE-754, little endian encoded float into a Float.
	 * 
	 * <p>This method expects to read 4 bytes starting at the provided {@code offset}.</p>
	 * 
	 * @param data the bytes
	 * @param offset the offset to read the float from
	 * @return a Float
	 */
    public static Float parseFloat(byte[] data, int offset) {
        int bits = (0xFF & data[offset]) | ((0xFF & data[offset + 1]) << 8) | ((0xFF & data[offset + 2]) << 16) | ((0xFF & data[offset + 3]) << 24);
        return Float.intBitsToFloat(bits);
    }

    /**
	 * Parse an ASCII encoded String from bytes, removing trailing spaces and 
	 * null character.
	 * 
	 * @param data the byte data
	 * @param offset the offset to start reading the String from
	 * @param length the length to read
	 * @return a new String
	 */
    public static String parseString(byte[] data, int offset, int length) {
        String s = null;
        try {
            s = new String(data, offset, length, "ASCII");
        } catch (UnsupportedEncodingException e) {
        }
        if (s != null) {
            Matcher m = STRING_TRIM_PAT.matcher(s);
            s = m.replaceFirst("");
        }
        return s;
    }

    /**
	 * Turn an integer into a little-endian encoded byte array.
	 * 
	 * @param value the integer to encode
	 * @return the byte array (4 bytes long)
	 */
    public static byte[] littleEndianBytes(int value) {
        byte[] result = new byte[4];
        result[0] = (byte) (0xFF & value);
        result[1] = (byte) (0xFF & (value >> 8));
        result[2] = (byte) (0xFF & (value >> 16));
        result[3] = (byte) (0xFF & (value >> 24));
        return result;
    }
}
