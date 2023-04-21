package net.cimd.packets.parameters;

import net.cimd.packets.InvalidContentException;

/**
 * @version $Revision: 1.1 $ $Date: 2007/03/14 14:17:06 $
 */
public class HexadecimalParameter extends CimdParameter {

    public HexadecimalParameter(String type, String value) throws InvalidContentException {
        this(type, value, new HexadecimalParamRestriction());
    }

    protected HexadecimalParameter(String type, String value, ParamRestriction restr) throws InvalidContentException {
        super(type, value, restr);
    }

    public HexadecimalParameter(String type, byte[] bytes) throws InvalidContentException {
        this(type, bytes, 0, bytes.length);
    }

    public HexadecimalParameter(String type, byte[] bytes, int offset, int length) throws InvalidContentException {
        this(type, convert(bytes, offset, length), new HexadecimalParamRestriction());
    }

    public HexadecimalParameter(String type, byte[] bytes, int offset, int length, ParamRestriction restr) throws InvalidContentException {
        super(type, convert(bytes, offset, length), restr);
    }

    private static String convert(byte[] bytes, int offset, int length) {
        StringBuffer buf = new StringBuffer(length);
        int last = offset + length;
        for (int i = offset; i < last; i++) {
            byte b = bytes[i];
            buf.append(getHexChar(b >> 4));
            buf.append(" ");
            buf.append(getHexChar(b & 0x0f));
            buf.append(" ");
        }
        return buf.toString();
    }

    private static char getHexChar(int b) {
        return Character.forDigit(b & 0x0f, 16);
    }

    public static byte[] parseString(String value) {
        if (value == null || (value.length() % 2 > 0)) {
            throw new NumberFormatException("Invalid length");
        }
        int arrayLength = value.length() / 2;
        byte[] res = new byte[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            byte hi = getHexValue(value.charAt(2 * i));
            byte low = getHexValue(value.charAt(2 * i + 1));
            res[i] = (byte) (((hi << 4) & 0x00f0) | low);
        }
        return res;
    }

    private static byte getHexValue(char ch) {
        int value = Character.digit(ch, 16);
        if (value < 0 || value >= 16) {
            throw new NumberFormatException("Invalid char: '" + ch + "'");
        }
        return (byte) (value & 0x0f);
    }
}
