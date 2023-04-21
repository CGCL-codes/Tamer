package com.dukesoftware.utils.math;

import static com.dukesoftware.utils.math.MathUtils.isOdd;

public class Bessel {

    private static double EPS = 1e-10;

    private static double EULER = 0.577215664901532861;

    public static double J(int n, double x) {
        if (x < 0) {
            if (isOdd(n)) return -J(n, -x);
            return J(n, -x);
        }
        if (n < 0) {
            if (isOdd(n)) return -J(-n, x);
            return J(-n, -x);
        }
        if (x == 0) return (n == 0) ? 1 : 0;
        double b = 1, x_2 = x / 2;
        int k = n;
        if (k < x) k = (int) x;
        do {
            k++;
        } while ((b *= x_2 / k) > EPS);
        if (isOdd(k)) k++;
        double a = 0, s = 0, r = Double.NaN;
        while (k > 0) {
            s += b;
            a = 2 * k * b / x - a;
            k--;
            if (n == k) r = a;
            b = 2 * k * a / x - b;
            k--;
            if (n == k) r = b;
        }
        return r / (2 * s + b);
    }

    public static double Y(int n, double x) {
        if (x <= 0) return Double.NaN;
        if (n < 0) {
            if (isOdd(n)) return -Y(-n, x);
            return Y(-n, x);
        }
        int k = (int) x;
        double b = 1, x_2 = x / 2, log_x_2 = Math.log(x_2);
        do {
            k++;
        } while ((b *= x_2 / k) > EPS);
        if (isOdd(k)) k++;
        double a = 0, s = 0, t = 0, u = 0;
        while (k > 0) {
            s += b;
            t = b / (k / 2) - t;
            a = 2 * k * b / x - a;
            k--;
            if (k > 2) u = (k * a) / ((k / 2) * (k / 2 + 1)) - u;
            b = 2 * k * a / x - b;
            k--;
        }
        s = 2 * s + b;
        a /= s;
        b /= s;
        t /= s;
        u /= s;
        t = (2 / Math.PI) * (2 * t + (log_x_2 + EULER) * b);
        if (n == 0) return t;
        u = (2 / Math.PI) * (u + ((EULER - 1) + log_x_2) * a - b / x);
        for (k = 1; k < n; k++) {
            s = (2 * k) * u / x - t;
            t = u;
            u = s;
        }
        return u;
    }
}
