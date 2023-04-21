package self.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class UriEncoderUtils {

    private static final byte[] uri_char_validity = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    /**
     * tests whether a string contains any XML unsafe characters
     * that we need to encode 
     * @param testString string to test
     * @return true if the string is OK, false if encoding is necessary
     */
    public static boolean isSafe(String testString) {
        if (testString == null) {
            return true;
        }
        for (int i = 0; i < testString.length(); i++) {
            if (uri_char_validity[testString.charAt(i)] == 0) {
                return false;
            }
        }
        return true;
    }

    public static String encodeUri(String src) {
        StringBuffer sb = null;
        byte[] bytes;
        try {
            bytes = src.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            bytes = src.getBytes();
        }
        for (int i = 0; i < bytes.length; i++) {
            int index = bytes[i] & 0xFF;
            if (UriEncoderUtils.uri_char_validity[index] > 0) {
                if (sb != null) {
                    sb.append((char) bytes[i]);
                }
                continue;
            }
            if (sb == null) {
                sb = new StringBuffer();
                sb.append(new String(bytes, 0, i));
            }
            sb.append("%");
            sb.append(Character.toUpperCase(Character.forDigit((index & 0xF0) >> 4, 16)));
            sb.append(Character.toUpperCase(Character.forDigit(index & 0x0F, 16)));
        }
        return sb == null ? src : sb.toString();
    }

    public static String decodeUri(String src) {
        boolean query = false;
        boolean decoded = false;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(src.length());
        for (int i = 0; i < src.length(); i++) {
            byte ch = (byte) src.charAt(i);
            if (ch == '?') {
                query = true;
            } else if (ch == '+' && query) {
                ch = ' ';
            } else if (ch == '%' && i + 2 < src.length() && UriEncoderUtils.isHexDigit(src.charAt(i + 1)) && UriEncoderUtils.isHexDigit(src.charAt(i + 2))) {
                ch = (byte) (UriEncoderUtils.hexValue(src.charAt(i + 1)) * 0x10 + UriEncoderUtils.hexValue(src.charAt(i + 2)));
                decoded = true;
                i += 2;
            }
            bos.write(ch);
        }
        if (!decoded) {
            return src;
        }
        try {
            return new String(bos.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return src;
    }

    private static boolean isHexDigit(char ch) {
        return Character.isDigit(ch) || (Character.toUpperCase(ch) >= 'A' && Character.toUpperCase(ch) <= 'F');
    }

    private static int hexValue(char ch) {
        if (Character.isDigit(ch)) {
            return ch - '0';
        }
        ch = Character.toUpperCase(ch);
        return (ch - 'A') + 0x0A;
    }
}
