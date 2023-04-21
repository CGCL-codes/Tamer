package cn.jsprun.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public final class Base64Decoder extends FilterInputStream {

    private static final char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    private static final int[] ints = new int[128];

    static {
        for (int i = 0; i < 64; i++) {
            ints[chars[i]] = i;
        }
    }

    private int charCount;

    private int carryOver;

    public Base64Decoder(InputStream in) {
        super(in);
    }

    public int read() throws IOException {
        int x;
        do {
            x = in.read();
            if (x == -1) {
                return -1;
            }
        } while (Character.isWhitespace((char) x));
        charCount++;
        if (x == '=') {
            return -1;
        }
        x = ints[x];
        int mode = (charCount - 1) % 4;
        if (mode == 0) {
            carryOver = x & 63;
            return read();
        } else if (mode == 1) {
            int decoded = ((carryOver << 2) + (x >> 4)) & 255;
            carryOver = x & 15;
            return decoded;
        } else if (mode == 2) {
            int decoded = ((carryOver << 4) + (x >> 2)) & 255;
            carryOver = x & 3;
            return decoded;
        } else if (mode == 3) {
            int decoded = ((carryOver << 6) + x) & 255;
            return decoded;
        }
        return -1;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int i;
        for (i = 0; i < len; i++) {
            int x = read();
            if (x == -1 && i == 0) {
                return -1;
            } else if (x == -1) {
                break;
            }
            b[off + i] = (byte) x;
        }
        return i;
    }

    public static String decode(String encoded, String charsetName) {
        byte[] bytes = null;
        try {
            bytes = encoded.getBytes(charsetName);
        } catch (UnsupportedEncodingException ignored) {
        }
        Base64Decoder in = new Base64Decoder(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) (bytes.length * 0.67));
        try {
            byte[] buf = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.close();
            return out.toString(charsetName);
        } catch (IOException ignored) {
            return null;
        }
    }
}
