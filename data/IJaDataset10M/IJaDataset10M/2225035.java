package com.dukesoftware.utils.random;

public class BinormalRandom {

    public static double[] rnd(double r) {
        double r1, r2, s;
        do {
            r1 = 2 * Math.random() - 1;
            r2 = 2 * Math.random() - 1;
            s = r1 * r1 + r2 * r2;
        } while (s > 1 || s == 0);
        s = -Math.log(s) / s;
        r1 = Math.sqrt((1 + r) * s) * r1;
        r2 = Math.sqrt((1 - r) * s) * r2;
        return new double[] { r1 + r2, r1 - r2 };
    }
}
