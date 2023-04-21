package net.sf.maple.misc;

public class Timer {

    static String[] names = new String[200];

    static long[] ts = new long[200];

    static int pos = 0;

    public static void mark(String s) {
        names[pos] = s;
        ts[pos] = System.currentTimeMillis();
        ++pos;
    }

    public static void report() {
        for (int i = 1; i < pos; ++i) {
            long diff = ts[i] - ts[i - 1];
            long elap = ts[i] - ts[0];
            System.out.println("D=" + diff + "  E=" + elap + "   " + names[i - 1] + " -> " + names[i]);
        }
    }
}
