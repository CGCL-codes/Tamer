package org.posterita.core;

import java.util.Random;

/**
 * @author jane
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RandomStringGenerator {

    private static Random rn = new Random();

    private RandomStringGenerator() {
    }

    public static int rand(int lo, int hi) {
        int n = hi - lo + 1;
        int i = rn.nextInt() % n;
        if (i < 0) i = -i;
        return lo + i;
    }

    @SuppressWarnings("deprecation")
    public static String randomstring(int lo, int hi) {
        int n = rand(lo, hi);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) b[i] = (byte) rand('a', 'z');
        return new String(b, 0);
    }

    public static String randomstring() {
        return randomstring(5, 25);
    }
}
