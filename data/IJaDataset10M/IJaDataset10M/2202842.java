package ru.bluesteel.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class URLEncoder {

    static int[] dontNeedEncoding;

    static final int caseDiff = ('a' - 'A');

    static String dfltEncName = null;

    public static final int MIN_RADIX = 2;

    public static final int MAX_RADIX = 36;

    public static final byte UPPERCASE_LETTER = 1;

    public static final byte LOWERCASE_LETTER = 2;

    public static final byte TITLECASE_LETTER = 3;

    static {
        dontNeedEncoding = new int[256];
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding[i] = 1;
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding[i] = 1;
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding[i] = 1;
        }
        dontNeedEncoding[' '] = 1;
        dontNeedEncoding['-'] = 1;
        dontNeedEncoding['_'] = 1;
        dontNeedEncoding['.'] = 1;
        dontNeedEncoding['*'] = 1;
    }

    private URLEncoder() {
    }

    public static String encode(String s) {
        int maxBytesPerChar = 10;
        StringBuffer out = new StringBuffer(s.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
        DataOutputStream writer = new DataOutputStream(buf);
        try {
            writer.writeUTF(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] ba = buf.toByteArray();
        for (int j = 2; j < ba.length; j++) {
            out.append('%');
            char ch = forDigit((ba[j] >> 4) & 0xF, 16);
            out.append(ch);
            ch = forDigit(ba[j] & 0xF, 16);
            out.append(ch);
        }
        buf.reset();
        return (out.toString());
    }

    private static char forDigit(int digit, int radix) {
        if ((digit >= radix) || (digit < 0)) {
            return '\0';
        }
        if ((radix < MIN_RADIX) || (radix > MAX_RADIX)) {
            return '\0';
        }
        if (digit < 10) {
            return (char) ('0' + digit);
        }
        return (char) ('a' - 10 + digit);
    }
}
