package org.labrad.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Functions for storing basic data types in arrays of bytes, and
 * for converting arrays of bytes back into basic data types.
 * @author maffoo
 *
 */
class Bytes {

    static boolean getBool(ByteArrayView b) {
        return getBool(b.getBytes(), b.getOffset());
    }

    static boolean getBool(byte[] buf, int ofs) {
        return buf[ofs] != 0;
    }

    static void setBool(ByteArrayView b, boolean data) {
        setBool(b.getBytes(), b.getOffset(), data);
    }

    static void setBool(byte[] buf, int ofs, boolean data) {
        buf[ofs] = data ? (byte) 1 : (byte) 0;
    }

    static int getInt(ByteArrayView b) {
        return getInt(b.getBytes(), b.getOffset());
    }

    static int getInt(byte[] buf, int ofs) {
        return (int) ((0xFF & (int) buf[ofs + 0]) << 24 | (0xFF & (int) buf[ofs + 1]) << 16 | (0xFF & (int) buf[ofs + 2]) << 8 | (0xFF & (int) buf[ofs + 3]) << 0);
    }

    static void setInt(ByteArrayView b, int data) {
        setInt(b.getBytes(), b.getOffset(), data);
    }

    static void setInt(byte[] buf, int ofs, int data) {
        buf[ofs + 0] = (byte) ((data & 0xFF000000) >> 24);
        buf[ofs + 1] = (byte) ((data & 0x00FF0000) >> 16);
        buf[ofs + 2] = (byte) ((data & 0x0000FF00) >> 8);
        buf[ofs + 3] = (byte) ((data & 0x000000FF) >> 0);
    }

    /**
   * Read an int from a byte stream.
   * 
   * @param is
   * @return
   * @throws IOException
   */
    static int readInt(ByteArrayInputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes, 0, 4);
        return getInt(bytes, 0);
    }

    /**
   * Write an int to a byte stream.
   * 
   * @param os
   * @param data
   * @throws IOException
   */
    static void writeInt(ByteArrayOutputStream os, int data) throws IOException {
        byte[] bytes = new byte[4];
        setInt(bytes, 0, data);
        os.write(bytes);
    }

    static long getWord(ByteArrayView b) {
        return getWord(b.getBytes(), b.getOffset());
    }

    static long getWord(byte[] buf, int ofs) {
        return (long) (0xFF & (int) buf[ofs + 0]) << 24 | (long) (0xFF & (int) buf[ofs + 1]) << 16 | (long) (0xFF & (int) buf[ofs + 2]) << 8 | (long) (0xFF & (int) buf[ofs + 3]) << 0;
    }

    static void setWord(ByteArrayView b, long data) {
        setWord(b.getBytes(), b.getOffset(), data);
    }

    static void setWord(byte[] buf, int ofs, long data) {
        buf[ofs + 0] = (byte) ((data & 0xFF000000) >> 24);
        buf[ofs + 1] = (byte) ((data & 0x00FF0000) >> 16);
        buf[ofs + 2] = (byte) ((data & 0x0000FF00) >> 8);
        buf[ofs + 3] = (byte) ((data & 0x000000FF) >> 0);
    }

    static long getLong(ByteArrayView b) {
        return getLong(b.getBytes(), b.getOffset());
    }

    static long getLong(byte[] buf, int ofs) {
        return (long) (0xFF & (int) buf[ofs + 0]) << 56 | (long) (0xFF & (int) buf[ofs + 1]) << 48 | (long) (0xFF & (int) buf[ofs + 2]) << 40 | (long) (0xFF & (int) buf[ofs + 3]) << 32 | (long) (0xFF & (int) buf[ofs + 4]) << 24 | (long) (0xFF & (int) buf[ofs + 5]) << 16 | (long) (0xFF & (int) buf[ofs + 6]) << 8 | (long) (0xFF & (int) buf[ofs + 7]) << 0;
    }

    static void setLong(ByteArrayView b, long data) {
        setLong(b.getBytes(), b.getOffset(), data);
    }

    static void setLong(byte[] buf, int ofs, long data) {
        buf[ofs + 0] = (byte) ((data & 0xFF00000000000000L) >> 56);
        buf[ofs + 1] = (byte) ((data & 0x00FF000000000000L) >> 48);
        buf[ofs + 2] = (byte) ((data & 0x0000FF0000000000L) >> 40);
        buf[ofs + 3] = (byte) ((data & 0x000000FF00000000L) >> 32);
        buf[ofs + 4] = (byte) ((data & 0x00000000FF000000L) >> 24);
        buf[ofs + 5] = (byte) ((data & 0x0000000000FF0000L) >> 16);
        buf[ofs + 6] = (byte) ((data & 0x000000000000FF00L) >> 8);
        buf[ofs + 7] = (byte) ((data & 0x00000000000000FFL) >> 0);
    }

    static double getDouble(ByteArrayView b) {
        return getDouble(b.getBytes(), b.getOffset());
    }

    static double getDouble(byte[] buf, int ofs) {
        return Double.longBitsToDouble(getLong(buf, ofs));
    }

    static void setDouble(ByteArrayView b, double data) {
        setDouble(b.getBytes(), b.getOffset(), data);
    }

    static void setDouble(byte[] buf, int ofs, double data) {
        setLong(buf, ofs, Double.doubleToRawLongBits(data));
    }

    static Complex getComplex(ByteArrayView b) {
        return getComplex(b.getBytes(), b.getOffset());
    }

    static Complex getComplex(byte[] buf, int ofs) {
        return new Complex(getDouble(buf, ofs), getDouble(buf, ofs + 8));
    }

    static void setComplex(ByteArrayView b, Complex data) {
        setComplex(b.getBytes(), b.getOffset(), data);
    }

    static void setComplex(byte[] buf, int ofs, Complex data) {
        setDouble(buf, ofs, data.getReal());
        setDouble(buf, ofs + 8, data.getImag());
    }

    public static void main(String[] args) {
        byte[] bs = new byte[100];
        Random rand = new Random();
        int count;
        boolean b;
        for (count = 0; count < 1000; count++) {
            b = rand.nextBoolean();
            Bytes.setBool(bs, 0, b);
            assert b == Bytes.getBool(bs, 0);
        }
        System.out.println("Bool okay.");
        int i;
        for (count = 0; count < 1000000; count++) {
            i = rand.nextInt();
            Bytes.setInt(bs, 0, i);
            assert i == Bytes.getInt(bs, 0);
        }
        System.out.println("Int okay.");
        long l;
        for (count = 0; count < 1000000; count++) {
            l = Math.abs(rand.nextLong()) % 4294967296L;
            Bytes.setWord(bs, 0, l);
            assert l == Bytes.getWord(bs, 0);
        }
        System.out.println("Word okay.");
        for (count = 0; count < 1000000; count++) {
            l = rand.nextLong();
            Bytes.setLong(bs, 0, l);
            assert l == Bytes.getLong(bs, 0);
        }
        System.out.println("Long okay.");
        double d;
        for (count = 0; count < 100000; count++) {
            d = rand.nextGaussian();
            Bytes.setDouble(bs, 0, d);
            assert d == Bytes.getDouble(bs, 0);
        }
        System.out.println("Double okay.");
        double re, im;
        for (count = 0; count < 100000; count++) {
            re = rand.nextGaussian();
            im = rand.nextGaussian();
            Complex c1 = new Complex(re, im);
            Bytes.setComplex(bs, 0, c1);
            Complex c2 = Bytes.getComplex(bs, 0);
            assert (c1.getReal() == c2.getReal()) && (c1.getImag() == c2.getImag());
        }
        System.out.println("Complex okay.");
    }
}
