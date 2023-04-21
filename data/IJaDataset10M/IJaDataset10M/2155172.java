package com.dukesoftware.utils.sig.fft;

import com.dukesoftware.utils.common.ArrayUtils;
import com.dukesoftware.utils.math.MathUtils;

public class FFTCore implements FFT {

    public final void fft(double[] xr, double[] xi) {
        final int n = xr.length;
        final int nu = MathUtils.power2(n);
        int n2 = n;
        double w = MathUtils.PI2 / n;
        for (int l = 0; l < nu; l++) {
            System.out.println("---" + l + "---");
            int m = n2;
            n2 >>= 1;
            double arg = 0;
            for (int i = 0; i < n2; i++) {
                double c = Math.cos(arg);
                double s = -Math.sin(arg);
                arg = arg + w;
                int k = m;
                while (k < n) {
                    final int j1 = k - m + i;
                    final int j2 = j1 + n2;
                    System.out.println(j1 + "," + j2);
                    double t1 = xr[j1] - xr[j2];
                    double t2 = xi[j1] - xi[j2];
                    xr[j1] = xr[j1] + xr[j2];
                    xi[j1] = xi[j1] + xi[j2];
                    xr[j2] = c * t1 + s * t2;
                    xi[j2] = c * t2 - s * t1;
                    k = k + m;
                }
            }
            w = 2 * w;
        }
        correct(xr, xi);
    }

    @Override
    public void ifft(double[] x, double[] y) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static final void correct(double[] xr, double[] xi) {
        int n = xr.length;
        int r = 1;
        final int ndv2 = n >> 1;
        for (int k = 1; k < n; k++) {
            if (r > k) {
                ArrayUtils.exchange(xr, xi, k, r);
            }
            int t = ndv2;
            while (t < r) {
                r -= t;
                t >>= 1;
            }
            r += t;
        }
    }
}
