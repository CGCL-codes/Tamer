package org.octgame.utilities.noise;

import java.util.Random;

public class Simplex {

    int grad3[][] = { { 1, 1, 0 }, { -1, 1, 0 }, { 1, -1, 0 }, { -1, -1, 0 }, { 1, 0, 1 }, { -1, 0, 1 }, { 1, 0, -1 }, { -1, 0, -1 }, { 0, 1, 1 }, { 0, -1, 1 }, { 0, 1, -1 }, { 0, -1, -1 } };

    int[] p = new int[256];

    int[] p2 = new int[256];

    int perm[] = new int[512];

    Random r;

    public Simplex(long seed) {
        r = new Random(seed);
        for (int i = 0; i < 256; i++) {
            if (i % 2 == 0) {
                r = new Random(r.nextLong());
            }
            p2[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            if (i % 2 == 0) {
                r = new Random(r.nextLong());
            }
            int next = r.nextInt(256);
            do {
                next = r.nextInt(256);
            } while (p2[next] == -1);
            p[i] = p2[next];
            p2[next] = -1;
        }
        for (int i = 0; i < 512; i++) perm[i] = p[i & 255];
    }

    public double Simplex_Noise(double xin, double yin, boolean smooth) {
        double n0, n1, n2;
        final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
        double s = (xin + yin) * F2;
        int i = fastfloor(xin + s);
        int j = fastfloor(yin + s);
        final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
        double t = (i + j) * G2;
        double X0 = i - t;
        double Y0 = j - t;
        double x0 = xin - X0;
        double y0 = yin - Y0;
        int i1, j1;
        if (x0 > y0) {
            i1 = 1;
            j1 = 0;
        } else {
            i1 = 0;
            j1 = 1;
        }
        double x1 = x0 - i1 + G2;
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2;
        double y2 = y0 - 1.0 + 2.0 * G2;
        int ii = i & 255;
        int jj = j & 255;
        int gi0 = perm[ii + perm[jj]] % 12;
        int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
        int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0) n0 = 0.0; else {
            t0 *= t0;
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0);
        }
        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0) n1 = 0.0; else {
            t1 *= t1;
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
        }
        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0) n2 = 0.0; else {
            t2 *= t2;
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
        }
        if (smooth) {
            double corners = (Simplex_Noise(xin - 1, yin - 1, false) + Simplex_Noise(xin + 1, yin - 1, false) + Simplex_Noise(xin - 1, yin + 1, false) + Simplex_Noise(xin + 1, yin + 1, false)) / 16;
            double sides = (Simplex_Noise(xin - 1, yin, false) + Simplex_Noise(xin + 1, yin, false) + Simplex_Noise(xin, yin - 1, false) + Simplex_Noise(xin, yin + 1, false)) / 8;
            double center = (70.0 * (n0 + n1 + n2)) / 4;
            double pixel = center + sides + corners;
            return pixel;
        } else {
            return 70.0 * (n0 + n1 + n2);
        }
    }

    private static int fastfloor(double x) {
        return x > 0 ? (int) x : (int) x - 1;
    }

    private static double dot(int g[], double x, double y) {
        return g[0] * x + g[1] * y;
    }
}
