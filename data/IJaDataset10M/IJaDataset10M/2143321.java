package net.sourceforge.jwap.wsp.header;

import java.util.Date;

/**
 * This class contains static methods used to encode primitive data types used
 * for encoding WSP data units.
 *
 * @author Michel Marti
 *
 */
public class Encoding {

    private static final byte[] EMPTY_VALUE = new byte[0];

    private static final byte[] EMPTY_STRING = new byte[] { 0 };

    private Encoding() {
    }

    public static byte[] textString(String value) {
        byte[] data = null;
        if (value == null) {
            data = EMPTY_VALUE;
        } else if ("".equals(value)) {
            data = EMPTY_STRING;
        } else {
            byte[] b = value.getBytes();
            if (b[0] > 127) {
                data = new byte[b.length + 2];
                data[0] = 127;
                System.arraycopy(b, 0, data, 1, b.length);
            } else {
                data = new byte[b.length + 1];
                System.arraycopy(b, 0, data, 0, b.length);
            }
        }
        return data;
    }

    public static byte[] extensionMedia(String value) {
        byte[] data = null;
        if (value == null) {
            data = EMPTY_VALUE;
        } else {
            byte[] b = value.getBytes();
            data = new byte[b.length + 1];
            System.arraycopy(b, 0, data, 0, b.length);
        }
        return data;
    }

    public static byte[] tokenText(String value) {
        int len = 1;
        byte[] data = null;
        if (value != null) {
            len += value.length();
            data = new byte[len];
            byte[] sb = value.getBytes();
            System.arraycopy(sb, 0, data, 0, len - 1);
        } else {
            data = EMPTY_STRING;
        }
        return data;
    }

    public static byte[] shortInteger(short value) {
        byte[] data = null;
        data = new byte[1];
        data[0] = (byte) ((Math.abs(value) & 0xff) | 0x80);
        return data;
    }

    public static byte[] longInteger(long value) {
        byte[] data = null;
        value = Math.abs(value);
        int octets = (int) Math.ceil(Long.toBinaryString(value).length() / 8.0);
        data = new byte[octets + 1];
        data[0] = (byte) octets;
        while (octets > 0) {
            data[octets] = (byte) (value & 0xff);
            value >>= 8;
            octets--;
        }
        return data;
    }

    public static byte[] uintVar(int v) {
        long value = v & 0xFFFFFFFFL;
        byte[] data = null;
        byte[] b = new byte[5];
        int i = 5;
        do {
            int x = (int) (value & 0x7f);
            value >>= 7;
            if (i != 5) {
                x |= 0x80;
            }
            b[--i] = (byte) (x & 0xff);
        } while ((value > 0) && (i > 0));
        data = new byte[5 - i];
        System.arraycopy(b, i, data, 0, data.length);
        return data;
    }

    public static byte[] quotedString(String value) {
        byte[] data = null;
        if (value == null) {
            data = new byte[] { 0x22, 0x00 };
        } else {
            byte[] b = value.getBytes();
            data = new byte[b.length + 2];
            data[0] = 0x22;
            System.arraycopy(b, 0, data, 1, b.length);
        }
        return data;
    }

    public static byte[] dateValue(Date date) {
        long secs = date.getTime() / 1000;
        return longInteger(secs);
    }

    public static byte[] qualityFactor(float value) {
        int qf = (int) (value * 1000) % 1000;
        if ((qf % 10) != 0) {
            qf += 100;
        } else {
            qf = (qf / 10) + 1;
        }
        return uintVar(qf);
    }

    public static byte[] versionValue(String version) {
        return textString(version);
    }

    public static byte[] versionValue(int major, int minor) {
        if ((major < 1) || (major > 7) || (minor < 0) || (minor > 14)) {
            StringBuffer sb = new StringBuffer(major).append('.').append(minor);
            return textString(sb.toString());
        }
        if (minor == 0) {
            minor = 15;
        }
        short i = (short) (((major & 0x0F) << 8) | (minor & 0x0f));
        return shortInteger(i);
    }

    public static byte[] valueLength(byte[] tt) {
        return valueLength((tt == null) ? 0 : tt.length);
    }

    public static byte[] valueLength(int i) {
        byte[] ret = null;
        if (i == 0) {
            ret = EMPTY_VALUE;
        } else if (i <= 30) {
            ret = new byte[] { (byte) i };
        } else {
            byte[] ui = uintVar(i);
            ret = new byte[ui.length + 1];
            ret[0] = 31;
            System.arraycopy(ui, 0, ret, 1, ui.length);
        }
        return ret;
    }

    public static byte[] encodeHeader(short wk, byte[] data) {
        byte[] kb = shortInteger(wk);
        byte[] ret = new byte[data.length + kb.length];
        System.arraycopy(kb, 0, ret, 0, kb.length);
        System.arraycopy(data, 0, ret, kb.length, data.length);
        return ret;
    }

    public static byte[] encodeHeader(String key, byte[] data) {
        byte[] kb = textString(key);
        byte[] ret = new byte[kb.length + data.length];
        System.arraycopy(kb, 0, ret, 0, kb.length);
        System.arraycopy(data, 0, ret, kb.length, data.length);
        return ret;
    }

    public static byte[] integerValue(long l) {
        return (l < 0x80) ? Encoding.shortInteger((short) l) : Encoding.longInteger(l);
    }

    public static byte[] uriValue(String value) {
        return textString((value == null) ? null : value.trim());
    }
}
