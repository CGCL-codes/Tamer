package common;

/**
 * Base 16 encoder.
 *
 * @author Marc Prud'hommeaux
 * @nojavadoc
 */
public class Base16Encoder {

    private static final char[] HEX = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * Convert bytes to a base16 string.
     */
    public static String encode(byte[] byteArray) {
        StringBuffer hexBuffer = new StringBuffer(byteArray.length * 2);
        for (int i = 0; i < byteArray.length; i++) for (int j = 1; j >= 0; j--) hexBuffer.append(HEX[(byteArray[i] >> (j * 4)) & 0xF]);
        return hexBuffer.toString();
    }

    /**
     * Convert a base16 string into a byte array.
     */
    public static byte[] decode(String s) {
        int len = s.length();
        byte[] r = new byte[len / 2];
        for (int i = 0; i < r.length; i++) {
            int digit1 = s.charAt(i * 2), digit2 = s.charAt(i * 2 + 1);
            if (digit1 >= '0' && digit1 <= '9') digit1 -= '0'; else if (digit1 >= 'A' && digit1 <= 'F') digit1 -= 'A' - 10;
            if (digit2 >= '0' && digit2 <= '9') digit2 -= '0'; else if (digit2 >= 'A' && digit2 <= 'F') digit2 -= 'A' - 10;
            r[i] = (byte) ((digit1 << 4) + digit2);
        }
        return r;
    }
}
