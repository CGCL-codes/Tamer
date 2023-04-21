package org.jjsip.tools;

/**
 * Class Random collects some static methods for generating random numbers and
 * other stuff.
 */
public class Random {

    /** The random seed */
    static final long seed = System.currentTimeMillis();

    static java.util.Random rand = new java.util.Random(seed);

    /** Sets the seed of this random number generator using a single long seed */
    public static void setSeed(long seed) {
        rand.setSeed(seed);
    }

    /** Returns a random integer */
    public static int nextInt() {
        return rand.nextInt();
    }

    /** Returns a random integer between 0 and n-1 */
    public static int nextInt(int n) {
        return Math.abs(rand.nextInt()) % n;
    }

    /** Returns a random long */
    public static long nextLong() {
        return rand.nextLong();
    }

    /** Returns a random boolean */
    public static boolean nextBoolean() {
        return rand.nextInt(2) == 1;
    }

    /** Returns a random array of bytes */
    public static byte[] nextBytes(int len) {
        byte[] buff = new byte[len];
        for (int i = 0; i < len; i++) buff[i] = (byte) nextInt(256);
        return buff;
    }

    /** Returns a random String */
    public static String nextString(int len) {
        byte[] buff = new byte[len];
        for (int i = 0; i < len; i++) {
            int n = nextInt(62);
            buff[i] = (byte) ((n < 10) ? 48 + n : ((n < 36) ? 55 + n : 61 + n));
        }
        return new String(buff);
    }

    /** Returns a random numeric String */
    public static String nextNumString(int len) {
        byte[] buff = new byte[len];
        for (int i = 0; i < len; i++) buff[i] = (byte) (48 + nextInt(10));
        return new String(buff);
    }

    /** Returns a random hexadecimal String */
    public static String nextHexString(int len) {
        byte[] buff = new byte[len];
        for (int i = 0; i < len; i++) {
            int n = nextInt(16);
            buff[i] = (byte) ((n < 10) ? 48 + n : 87 + n);
        }
        return new String(buff);
    }
}
