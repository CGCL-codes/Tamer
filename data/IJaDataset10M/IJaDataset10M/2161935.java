package org.ibex.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/** General <tt>String</tt> and <tt>byte[]</tt> processing functions,
 *  including Base64 and a safe filename transform.
 *
 *  @author adam@ibex.org
 */
public final class Encode {

    public static class QuotedPrintable {

        public static String decode(String s, boolean lax) {
            return s;
        }
    }

    public static class RFC2047 {

        public static String decode(String s) {
            return s;
        }
    }

    public static long twoFloatsToLong(float a, float b) {
        return ((Float.floatToIntBits(a) & 0xffffffffL) << 32) | (Float.floatToIntBits(b) & 0xffffffffL);
    }

    public static float longToFloat1(long l) {
        return Float.intBitsToFloat((int) ((l >> 32) & 0xffffffff));
    }

    public static float longToFloat2(long l) {
        return Float.intBitsToFloat((int) (l & 0xffffffff));
    }

    private static final char[] fn = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String toFilename(String s) {
        StringBuffer sb = new StringBuffer();
        try {
            byte[] b = s.getBytes("UTF-8");
            for (int i = 0; i < b.length; i++) {
                char c = (char) (b[i] & 0xff);
                if (c == File.separatorChar || c < 32 || c > 126 || c == '%' || (i == 0 && c == '.')) sb.append("%" + fn[(b[i] & 0xf0) >> 8] + fn[b[i] & 0xf]); else sb.append(c);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new Error("this should never happen; Java spec mandates UTF-8 support");
        }
    }

    public static String fromFilename(String s) {
        byte[] b = new byte[s.length() * 2];
        int bytes = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '%') b[bytes++] = (byte) Integer.parseInt(("" + s.charAt(++i) + s.charAt(++i)), 16); else b[bytes++] = (byte) c;
        }
        try {
            return new String(b, 0, bytes, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            throw new Error("this should never happen; Java spec mandates UTF-8 support");
        }
    }

    public static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]).toUpperCase());
        }
        return (buf.toString());
    }

    public static String byteToHex(byte data) {
        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar((data >>> 4) & 0x0F));
        buf.append(toHexChar(data & 0x0F));
        return buf.toString();
    }

    public static char toHexChar(int i) {
        if ((0 <= i) && (i <= 9)) {
            return (char) ('0' + i);
        } else {
            return (char) ('a' + (i - 10));
        }
    }

    private static final byte[] encB64 = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/' };

    public static class Base64InputStream extends ByteArrayInputStream {

        public Base64InputStream(String s) {
            super(fromBase64(s.getBytes()));
        }
    }

    public static byte[] toBase64(String data) {
        return toBase64(data.getBytes());
    }

    /** Encode the input data producong a base 64 encoded byte array.
     *  @return A byte array containing the base 64 encoded data. */
    public static byte[] toBase64(byte[] data) {
        byte[] bytes;
        int modulus = data.length % 3;
        if (modulus == 0) {
            bytes = new byte[4 * data.length / 3];
        } else {
            bytes = new byte[4 * ((data.length / 3) + 1)];
        }
        int dataLength = (data.length - modulus);
        int a1, a2, a3;
        for (int i = 0, j = 0; i < dataLength; i += 3, j += 4) {
            a1 = data[i] & 0xff;
            a2 = data[i + 1] & 0xff;
            a3 = data[i + 2] & 0xff;
            bytes[j] = encB64[(a1 >>> 2) & 0x3f];
            bytes[j + 1] = encB64[((a1 << 4) | (a2 >>> 4)) & 0x3f];
            bytes[j + 2] = encB64[((a2 << 2) | (a3 >>> 6)) & 0x3f];
            bytes[j + 3] = encB64[a3 & 0x3f];
        }
        int b1, b2, b3;
        int d1, d2;
        switch(modulus) {
            case 0:
                break;
            case 1:
                d1 = data[data.length - 1] & 0xff;
                b1 = (d1 >>> 2) & 0x3f;
                b2 = (d1 << 4) & 0x3f;
                bytes[bytes.length - 4] = encB64[b1];
                bytes[bytes.length - 3] = encB64[b2];
                bytes[bytes.length - 2] = (byte) '=';
                bytes[bytes.length - 1] = (byte) '=';
                break;
            case 2:
                d1 = data[data.length - 2] & 0xff;
                d2 = data[data.length - 1] & 0xff;
                b1 = (d1 >>> 2) & 0x3f;
                b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
                b3 = (d2 << 2) & 0x3f;
                bytes[bytes.length - 4] = encB64[b1];
                bytes[bytes.length - 3] = encB64[b2];
                bytes[bytes.length - 2] = encB64[b3];
                bytes[bytes.length - 1] = (byte) '=';
                break;
        }
        return bytes;
    }

    private static final byte[] decB64 = new byte[128];

    static {
        for (int i = 'A'; i <= 'Z'; i++) decB64[i] = (byte) (i - 'A');
        for (int i = 'a'; i <= 'z'; i++) decB64[i] = (byte) (i - 'a' + 26);
        for (int i = '0'; i <= '9'; i++) decB64[i] = (byte) (i - '0' + 52);
        decB64['+'] = 62;
        decB64['/'] = 63;
    }

    /** Decode base 64 encoded input data.
     *  @return A byte array representing the decoded data. */
    public static byte[] fromBase64(byte[] data) {
        byte[] bytes;
        byte b1, b2, b3, b4;
        if (data[data.length - 2] == '=') bytes = new byte[(((data.length / 4) - 1) * 3) + 1]; else if (data[data.length - 1] == '=') bytes = new byte[(((data.length / 4) - 1) * 3) + 2]; else bytes = new byte[((data.length / 4) * 3)];
        for (int i = 0, j = 0; i < data.length - 4; i += 4, j += 3) {
            b1 = decB64[data[i]];
            b2 = decB64[data[i + 1]];
            b3 = decB64[data[i + 2]];
            b4 = decB64[data[i + 3]];
            bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[j + 2] = (byte) ((b3 << 6) | b4);
        }
        if (data[data.length - 2] == '=') {
            b1 = decB64[data[data.length - 4]];
            b2 = decB64[data[data.length - 3]];
            bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
        } else if (data[data.length - 1] == '=') {
            b1 = decB64[data[data.length - 4]];
            b2 = decB64[data[data.length - 3]];
            b3 = decB64[data[data.length - 2]];
            bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
        } else {
            b1 = decB64[data[data.length - 4]];
            b2 = decB64[data[data.length - 3]];
            b3 = decB64[data[data.length - 2]];
            b4 = decB64[data[data.length - 1]];
            bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
        }
        return bytes;
    }

    /** Decode a base 64 encoded String.
     *  @return A byte array representing the decoded data. */
    public static byte[] fromBase64(String data) {
        byte[] bytes;
        byte b1, b2, b3, b4;
        if (data.charAt(data.length() - 2) == '=') bytes = new byte[(((data.length() / 4) - 1) * 3) + 1]; else if (data.charAt(data.length() - 1) == '=') bytes = new byte[(((data.length() / 4) - 1) * 3) + 2]; else bytes = new byte[((data.length() / 4) * 3)];
        for (int i = 0, j = 0; i < data.length() - 4; i += 4, j += 3) {
            b1 = decB64[data.charAt(i)];
            b2 = decB64[data.charAt(i + 1)];
            b3 = decB64[data.charAt(i + 2)];
            b4 = decB64[data.charAt(i + 3)];
            bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[j + 2] = (byte) ((b3 << 6) | b4);
        }
        if (data.charAt(data.length() - 2) == '=') {
            b1 = decB64[data.charAt(data.length() - 4)];
            b2 = decB64[data.charAt(data.length() - 3)];
            bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
        } else if (data.charAt(data.length() - 1) == '=') {
            b1 = decB64[data.charAt(data.length() - 4)];
            b2 = decB64[data.charAt(data.length() - 3)];
            b3 = decB64[data.charAt(data.length() - 2)];
            bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
        } else {
            b1 = decB64[data.charAt(data.length() - 4)];
            b2 = decB64[data.charAt(data.length() - 3)];
            b3 = decB64[data.charAt(data.length() - 2)];
            b4 = decB64[data.charAt(data.length() - 1)];
            bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
            bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
            bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
        }
        return bytes;
    }

    /** Packs 8-bit bytes into a String of 7-bit chars.
     *  @throws IllegalArgumentException when <tt>len</tt> is not a multiple of 7.
     *  @return A String representing the processed bytes. */
    public static String toStringFrom8bit(byte[] b, int off, int len) throws IllegalArgumentException {
        if (len % 7 != 0) throw new IllegalArgumentException("len must be a multiple of 7");
        StringBuffer ret = new StringBuffer();
        for (int i = off; i < off + len; i += 7) {
            long l = 0;
            for (int j = 6; j >= 0; j--) {
                l <<= 8;
                l |= (b[i + j] & 0xff);
            }
            for (int j = 0; j < 8; j++) {
                ret.append((char) (l & 0x7f));
                l >>= 7;
            }
        }
        return ret.toString();
    }

    /** Packs a String of 7-bit chars into 8-bit bytes.
     *  @throws IllegalArgumentException when <tt>s.length()</tt> is not a multiple of 8.
     *  @return A byte array representing the processed String. */
    public static byte[] fromStringTo8bit(String s) throws IllegalArgumentException {
        if (s.length() % 8 != 0) throw new IllegalArgumentException("string length must be a multiple of 8");
        byte[] ret = new byte[(s.length() / 8) * 7];
        for (int i = 0; i < s.length(); i += 8) {
            long l = 0;
            for (int j = 7; j >= 0; j--) {
                l <<= 7;
                l |= (s.charAt(i + j) & 0x7fL);
            }
            for (int j = 0; j < 7; j++) {
                ret[(i / 8) * 7 + j] = (byte) (l & 0xff);
                l >>= 8;
            }
        }
        return ret;
    }

    public static class JavaSourceCode {

        public static final int LINE_LENGTH = 80 / 4;

        public static InputStream decode(String s) throws IOException {
            return new GZIPInputStream(new StringInputStream(s));
        }

        private static class StringInputStream extends InputStream {

            private final String s;

            private final int length;

            private int pos = 0;

            public StringInputStream(String s) {
                this.s = s;
                this.length = s.length();
            }

            public int read() {
                byte[] b = new byte[1];
                int numread = read(b, 0, 1);
                if (numread == -1) return -1;
                if (numread == 0) throw new Error();
                return b[0] & 0xff;
            }

            public int read(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    if (pos >= length) return i - off;
                    b[i] = (byte) s.charAt(pos++);
                }
                return len;
            }
        }

        public static void encode(String packageName, String className, InputStream is, Writer w) throws IOException {
            ByteArrayOutputStream baos;
            OutputStream os = new GZIPOutputStream(baos = new ByteArrayOutputStream());
            byte[] buf = new byte[1024];
            while (true) {
                int numread = is.read(buf, 0, buf.length);
                if (numread == -1) break;
                os.write(buf, 0, numread);
            }
            os.close();
            buf = baos.toByteArray();
            w.append("// generated by " + Encode.class.getName() + "\n\n");
            w.append("package " + packageName + ";\n\n");
            w.append("public class " + className + " {\n");
            w.append("    public static final String data = \n");
            for (int pos = 0; pos < buf.length; ) {
                w.append("        \"");
                for (int i = 0; i < LINE_LENGTH && pos < buf.length; i++) {
                    String cs = Integer.toOctalString(buf[pos++] & 0xff);
                    while (cs.length() < 3) cs = "0" + cs;
                    w.append("\\" + cs);
                }
                w.append("\" +\n");
            }
            w.append("    \"\";\n");
            w.append("}\n");
        }
    }
}
