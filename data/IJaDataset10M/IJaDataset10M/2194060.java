package org.renjin.primitives.random;

import org.renjin.eval.Context;
import org.renjin.primitives.MathExt;

public class Wilcox {

    private static double[][][] w;

    private static int allocated_m, allocated_n;

    private static final int WILCOX_MAX = 50;

    public static double rwilcox(Context.Globals context, double m, double n) {
        int i, j, k;
        int[] x;
        double r;
        if (Double.isNaN(m) || Double.isNaN(n)) {
            return (m + n);
        }
        m = Math.floor(m + 0.5);
        n = Math.floor(n + 0.5);
        if ((m < 0) || (n < 0)) {
            return Double.NaN;
        }
        if ((m == 0) || (n == 0)) {
            return (0);
        }
        r = 0.0;
        k = (int) (m + n);
        x = new int[k];
        for (i = 0; i < k; i++) {
            x[i] = i;
        }
        for (i = 0; i < n; i++) {
            j = (int) (Math.floor(k * context.rng.unif_rand()));
            r += x[j];
            x[j] = x[--k];
        }
        x = null;
        return (r - n * (n - 1) / 2);
    }

    private static void w_init_maybe(int m, int n) {
        int i;
        if (m > n) {
            i = n;
            n = m;
            m = i;
        }
        if (w != null && (m > allocated_m || n > allocated_n)) {
            w = null;
        }
        if (w == null) {
            m = Math.max(m, WILCOX_MAX);
            n = Math.max(n, WILCOX_MAX);
            w = new double[m + 1][][];
            for (i = 0; i <= m; i++) {
                w[i] = new double[n + 1][];
            }
            allocated_m = m;
            allocated_n = n;
        }
    }

    private static double cwilcox(int k, int m, int n) {
        int c, u, i, j, l;
        u = m * n;
        if (k < 0 || k > u) {
            return (0);
        }
        c = (int) (u / 2);
        if (k > c) {
            k = u - k;
        }
        if (m < n) {
            i = m;
            j = n;
        } else {
            i = n;
            j = m;
        }
        if (j == 0) {
            return k == 0 ? 1.0 : 0.0;
        }
        if (j > 0 && k < j) {
            return cwilcox(k, i, k);
        }
        if (w[i][j] == null) {
            w[i][j] = new double[c + 1];
            for (l = 0; l <= c; l++) {
                w[i][j][l] = -1;
            }
        }
        if (w[i][j][k] < 0) {
            if (j == 0) {
                w[i][j][k] = (k == 0 ? 1 : 0);
            } else {
                w[i][j][k] = cwilcox(k - j, i - 1, j) + cwilcox(k, i, j - 1);
            }
        }
        return (w[i][j][k]);
    }

    public static double dwilcox(double x, double m, double n, boolean give_log) {
        double d;
        if (Double.isNaN(x) || Double.isNaN(m) || Double.isNaN(n)) {
            return (x + m + n);
        }
        m = Math.floor(m + 0.5);
        n = Math.floor(n + 0.5);
        if (m <= 0 || n <= 0) {
            return Double.NaN;
        }
        if (Math.abs(x - Math.floor(x + 0.5)) > 1e-7) {
            return (SignRank.R_D__0(true, give_log));
        }
        x = Math.floor(x + 0.5);
        if ((x < 0) || (x > m * n)) {
            return (SignRank.R_D__0(true, give_log));
        }
        w_init_maybe((int) m, (int) n);
        d = give_log ? Math.log(cwilcox((int) x, (int) m, (int) n)) - MathExt.lchoose(m + n, (int) n) : cwilcox((int) x, (int) m, (int) n) / MathExt.choose(m + n, (int) n);
        return (d);
    }

    public static double pwilcox(double q, double m, double n, boolean lower_tail, boolean log_p) {
        int i;
        double c, p;
        if (Double.isNaN(q) || Double.isNaN(m) || Double.isNaN(n)) {
            return (q + m + n);
        }
        if (Double.isInfinite(m) || Double.isInfinite(n)) {
            return Double.NaN;
        }
        m = Math.floor(m + 0.5);
        n = Math.floor(n + 0.5);
        if (m <= 0 || n <= 0) {
            return Double.NaN;
        }
        q = Math.floor(q + 1e-7);
        if (q < 0.0) {
            return SignRank.R_DT_0(lower_tail, log_p);
        }
        if (q >= m * n) {
            return SignRank.R_DT_1(lower_tail, log_p);
        }
        w_init_maybe((int) m, (int) n);
        c = MathExt.choose(m + n, (int) n);
        p = 0;
        if (q <= (m * n / 2)) {
            for (i = 0; i <= q; i++) {
                p += cwilcox(i, (int) m, (int) n) / c;
            }
        } else {
            q = m * n - q;
            for (i = 0; i < q; i++) {
                p += cwilcox(i, (int) m, (int) n) / c;
            }
            lower_tail = !lower_tail;
        }
        return SignRank.R_DT_val(p, lower_tail, log_p);
    }

    public static double qwilcox(double x, double m, double n, boolean lower_tail, boolean log_p) {
        double c, p, q;
        if (Double.isNaN(x) || Double.isNaN(m) || Double.isNaN(n)) {
            return (x + m + n);
        }
        if (Double.isInfinite(x) || Double.isInfinite(m) || Double.isInfinite(n)) {
            return Double.NaN;
        }
        if ((log_p && x > 0) || (!log_p && (x < 0 || x > 1))) {
            return (Double.NaN);
        }
        m = Math.floor(m + 0.5);
        n = Math.floor(n + 0.5);
        if (m <= 0 || n <= 0) {
            return (Double.NaN);
        }
        if (x == SignRank.R_DT_0(lower_tail, log_p)) {
            return (0);
        }
        if (x == SignRank.R_DT_1(lower_tail, log_p)) {
            return (m * n);
        }
        if (log_p || !lower_tail) {
            x = Normal.R_DT_qIv(x, log_p ? 1 : 0, lower_tail ? 1 : 0);
        }
        w_init_maybe((int) m, (int) n);
        c = MathExt.choose(m + n, (int) n);
        p = 0;
        q = 0;
        if (x <= 0.5) {
            x = x - 10 * SignRank.DBL_EPSILON;
            for (; ; ) {
                p += cwilcox((int) q, (int) m, (int) n) / c;
                if (p >= x) break;
                q++;
            }
        } else {
            x = 1 - x + 10 * SignRank.DBL_EPSILON;
            for (; ; ) {
                p += cwilcox((int) q, (int) m, (int) n) / c;
                if (p > x) {
                    q = m * n - q;
                    break;
                }
                q++;
            }
        }
        return (q);
    }
}
