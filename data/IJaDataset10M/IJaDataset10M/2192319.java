package org.xmlcml.cml.legacy2cml.molecule.chemdraw;

/**
 * 
 */
public class Util implements CDXConstants {

    /**
	 * @param b
	 * @return int
	 */
    public static int getUINT8(byte b) {
        return (b < 0) ? (int) b + 0xf00 : (int) b;
    }

    /**
	 * @param l
	 * @return byte
	 * @throws IllegalArgumentException
	 */
    public static byte setUINT8(int l) throws IllegalArgumentException {
        byte bb = (byte) ((l > 0x7F) ? l - 0x100 : l);
        return bb;
    }

    /**
	 * @param b
	 * @return int
	 */
    public static int getINT8(byte b) {
        return (int) b;
    }

    /**
	 * @param l
	 * @return byte
	 * @throws IllegalArgumentException
	 */
    public static byte setINT8(int l) throws IllegalArgumentException {
        byte bb = (byte) (l % 0x100);
        return bb;
    }

    /**
	 * @param b
	 * @return int
	 * @throws IllegalArgumentException
	 */
    public static int getUINT16(byte[] b) throws IllegalArgumentException {
        return getUINT16(b, 0);
    }

    /**
	 * @param b
	 * @param offset
	 * @return int
	 * @throws IllegalArgumentException
	 */
    public static int getUINT16(byte[] b, int offset) throws IllegalArgumentException {
        if (b.length == offset + 1) {
            return getUINT8(b[offset]);
        } else {
            int ii = 0;
            for (int i = 1 + offset; i >= offset; i--) {
                ii *= 256;
                int bb = (b[i] < 0x00) ? (int) b[i] + 0x100 : (int) b[i];
                ii += bb;
            }
            return ii;
        }
    }

    /**
	 * @param l
	 * @return byte
	 * @throws IllegalArgumentException
	 */
    public static byte[] setUINT16(int l) throws IllegalArgumentException {
        byte bb[] = new byte[2];
        int i = l / 0x100;
        bb[1] = (byte) ((i > 0x7F) ? i - 0x100 : i);
        l = l % 0x100;
        bb[0] = (byte) ((l > 0x7F) ? l - 0x100 : l);
        return bb;
    }

    static int getINT16(byte[] b) {
        return getINT16(b, 0);
    }

    static int getINT16(byte[] b, int offset) {
        int sh = getUINT16(b, offset);
        return (sh > 0x8000) ? sh - 0xffff - 1 : sh;
    }

    static int getUINT16(byte b0, byte b1) {
        byte[] bb = new byte[2];
        bb[0] = b0;
        bb[1] = b1;
        return getUINT16(bb);
    }

    static int getINT16(byte b0, byte b1) {
        byte[] bb = new byte[2];
        bb[0] = b0;
        bb[1] = b1;
        return getINT16(bb);
    }

    static long getUINT32(byte[] b, int offset) throws IllegalArgumentException {
        if (b.length == 1 + offset) {
            return getUINT8(b[offset]);
        } else if (b.length == 2 + offset) {
            return getUINT16(b, offset);
        } else if (b.length < 4 + offset) {
            throw new IllegalArgumentException("getUINT32 arg length: " + b.length + "/" + offset);
        } else {
            int[] bb = new int[4];
            for (int i = 0; i < 4; i++) {
                byte bbx = b[i + offset];
                bb[i] = (bbx < 0x00) ? (int) (bbx + (int) 0x100) : (int) bbx;
            }
            long ii = bb[0] + 0x100 * bb[1] + 0x10000 * bb[2] + 0x1000000 * bb[3];
            return ii;
        }
    }

    static byte[] setUINT32(long l) throws IllegalArgumentException {
        byte bb[] = new byte[4];
        long i = l / 0x1000000;
        bb[3] = (byte) ((i > 0x7F) ? i - 0x100 : i);
        l = l % 0x1000000;
        i = l / 0x10000;
        bb[2] = (byte) ((i > 0x7F) ? i - 0x100 : i);
        l = l % 0x10000;
        i = l / 0x100;
        bb[1] = (byte) ((i > 0x7F) ? i - 0x100 : i);
        l = l % 0x100;
        bb[0] = (byte) ((l > 0x7F) ? l - 0x100 : l);
        return bb;
    }

    static long getUINT32(byte[] b) throws IllegalArgumentException {
        return getUINT32(b, 0);
    }

    static int getINT32(byte[] b, int offset) {
        long sh = getUINT32(b, offset);
        return (sh > 0x80000000) ? (int) (sh - 0x100000000L) : (int) sh;
    }

    static int getINT32(byte[] b) {
        return getINT32(b, 0);
    }

    static int getINT32(byte b0, byte b1, byte b2, byte b3) {
        byte[] bb = new byte[4];
        bb[0] = b0;
        bb[1] = b1;
        bb[2] = b2;
        bb[3] = b3;
        return getINT32(bb);
    }

    static double getFLOAT64(byte[] bytes, int offset) {
        long accum = 0;
        for (int shiftBy = 0; shiftBy < 64; shiftBy += 8) {
            accum |= ((long) (bytes[offset++] & 0xff)) << shiftBy;
        }
        return Double.longBitsToDouble(accum);
    }

    static byte[] setFLOAT64(double d) {
        long l = Double.doubleToLongBits(d);
        byte[] b = new byte[8];
        for (int i = 0; i < 8; i++) {
            b[i] = (byte) (l % 0x100);
            l >>= 8;
        }
        return b;
    }

    static String getEscapedAsciiString(byte[] bytes, int offset) {
        StringBuffer sb = new StringBuffer();
        int ff00 = Integer.parseInt("ff00", 16);
        for (int i = offset; i < bytes.length; i++) {
            char ch = (char) bytes[i];
            if (Character.isWhitespace(ch) || (ch > 32 && ch < 127)) {
                sb.append(ch);
            } else if ((int) ch > ff00) {
                ch -= ff00;
                sb.append(LESCAPE + (int) ch + RESCAPE);
            } else {
                sb.append(LESCAPE + (int) ch + RESCAPE);
            }
        }
        return sb.toString();
    }

    static String getAsciiString(byte[] bytes, int offset) {
        StringBuffer sb = new StringBuffer();
        for (int i = offset; i < bytes.length; i++) {
            char ch = (char) bytes[i];
            if (ch < 0) ch += 0x100;
            if (Character.isWhitespace(ch) || (ch > 32 && ch < 127)) {
                sb.append(ch);
            } else {
            }
        }
        return sb.toString();
    }

    /** trims trailing zeros and points.
    */
    static String trimFloat(double d) {
        StringBuffer sb = new StringBuffer();
        sb.append(d);
        int l = sb.length();
        if (sb.indexOf(".") != -1) {
            while (true) {
                l = sb.length();
                if (sb.charAt(l - 1) == '0') {
                    sb.deleteCharAt(l - 1);
                } else {
                    break;
                }
            }
            l = sb.length();
            if (sb.charAt(l - 1) == '.') {
                sb.deleteCharAt(l - 1);
            }
        }
        return sb.toString();
    }

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        testFloat();
    }

    private static void testFloat() {
        double[] d = { 0.0, 10.0, 255.0, 256.0, 1000.0, 100000.0, 0.5, 0.001, -123.45 };
        for (int i = 0; i < d.length; i++) {
            byte[] b = Util.setFLOAT64(d[i]);
            String s = "" + d[i] + " => ";
            for (int j = 0; j < 8; j++) {
                String ss = Integer.toHexString(b[j]);
                s += " " + ((ss.length() == 8) ? ss.substring(6, 8) : ss);
            }
        }
    }
}

;
