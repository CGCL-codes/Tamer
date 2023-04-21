package org.renjin.primitives;

import org.renjin.primitives.annotations.Primitive;
import org.renjin.primitives.random.SignRank;
import org.renjin.sexp.DoubleVector;

public class PsiGamma {

    public static class dpsifn {

        static final double bvalues[] = { 1.00000000000000000e+00, -5.00000000000000000e-01, 1.66666666666666667e-01, -3.33333333333333333e-02, 2.38095238095238095e-02, -3.33333333333333333e-02, 7.57575757575757576e-02, -2.53113553113553114e-01, 1.16666666666666667e+00, -7.09215686274509804e+00, 5.49711779448621554e+01, -5.29124242424242424e+02, 6.19212318840579710e+03, -8.65802531135531136e+04, 1.42551716666666667e+06, -2.72982310678160920e+07, 6.01580873900642368e+08, -1.51163157670921569e+10, 4.29614643061166667e+11, -1.37116552050883328e+13, 4.88332318973593167e+14, -1.92965793419400681e+16 };

        static final int n_max = 100;

        static int i, j, k, mm, mx, nn, np, nx, fn;

        static double arg, den, elim, eps, fln, fx, rln, rxsq, r1m4, r1m5, s, slope, t, ta, tk, tol, tols, tss, tst, tt, t1, t2, wdtol, xdmln, xdmy, xinc, xln = 0.0, xm, xmin, xq, yint;

        static double[] trm = new double[23];

        static double[] trmr = new double[n_max + 1];

        static int[] ierr;

        static double x;

        static int kode;

        static double[] ans;

        static int[] nz;

        public static void dpsifn(double x, int n, int kode, int m, double[] ans, int[] nz, int[] ierr) {
            dpsifn.x = x;
            dpsifn.kode = kode;
            dpsifn.ans = ans;
            dpsifn.nz = nz;
            dpsifn.ierr = ierr;
            if (n < 0 || kode < 1 || kode > 2 || m < 1) {
                ierr[0] = 1;
                return;
            }
            if (x <= 0.) {
                if (x == (long) x) {
                    for (j = 0; j < m; j++) {
                        ans[j] = ((j + n) % 2) != 0 ? Double.POSITIVE_INFINITY : DoubleVector.NaN;
                    }
                    return;
                }
                dpsifn(1. - x, n, 1, m, ans, nz, ierr);
                if (m > 1 || n > 3) {
                    ierr[0] = 4;
                    return;
                }
                x *= Math.PI;
                if (n == 0) {
                    tt = Math.cos(x) / Math.sin(x);
                } else if (n == 1) {
                    tt = -1 / Math.pow(Math.sin(x), 2);
                } else if (n == 2) {
                    tt = 2 * Math.cos(x) / Math.pow(Math.sin(x), 3);
                } else if (n == 3) {
                    tt = -2 * (2 * Math.pow(Math.cos(x), 2) + 1) / Math.pow(Math.sin(x), 4);
                } else {
                    tt = DoubleVector.NaN;
                }
                s = (n % 2) != 0 ? -1. : 1.;
                t1 = t2 = s = 1.;
                for (k = 0, j = k - n; j < m; k++, j++, s = -s) {
                    t1 *= Math.PI;
                    if (k >= 2) {
                        t2 *= k;
                    }
                    if (j >= 0) {
                        ans[j] = s * (ans[j] + t1 / t2 * tt);
                    }
                }
                if (n == 0 && kode == 2) {
                    ans[0] += xln;
                }
                return;
            }
            nz[0] = 0;
            xln = Math.log(x);
            if (kode == 1 && m == 1) {
                double lrg = 1 / (2. * SignRank.DBL_EPSILON);
                if (n == 0 && x * xln > lrg) {
                    ans[0] = -xln;
                    return;
                } else if (n >= 1 && x > n * lrg) {
                    ans[0] = Math.exp(-n * xln) / n;
                    return;
                }
            }
            mm = m;
            nx = Math.min(-Double.MIN_EXPONENT, Double.MAX_EXPONENT);
            r1m5 = 8 * 4;
            r1m4 = 0 * 0.5;
            wdtol = Math.max(r1m4, 0.5e-18);
            elim = 2.302 * (nx * r1m5 - 3.0);
            for (; ; ) {
                nn = n + mm - 1;
                fn = nn;
                t = (fn + 1) * xln;
                if (Math.abs(t) > elim) {
                    if (t <= 0.0) {
                        nz[0] = 0;
                        ierr[0] = 2;
                        return;
                    }
                } else {
                    if (x < wdtol) {
                        ans[0] = Math.pow(x, -n - 1.0);
                        if (mm != 1) {
                            for (k = 1; k < mm; k++) {
                                ans[k] = ans[k - 1] / x;
                            }
                        }
                        if (n == 0 && kode == 2) {
                            ans[0] += xln;
                        }
                        return;
                    }
                    rln = r1m5 * 53;
                    rln = Math.min(rln, 18.06);
                    fln = Math.max(rln, 3.0) - 3.0;
                    yint = 3.50 + 0.40 * fln;
                    slope = 0.21 + fln * (0.0006038 * fln + 0.008677);
                    xm = yint + slope * fn;
                    mx = (int) xm + 1;
                    xmin = mx;
                    if (n != 0) {
                        xm = -2.302 * rln - Math.min(0.0, xln);
                        arg = xm / n;
                        arg = Math.min(0.0, arg);
                        eps = Math.exp(arg);
                        xm = 1.0 - eps;
                        if (Math.abs(arg) < 1.0e-3) {
                            xm = -arg;
                        }
                        fln = x * xm / eps;
                        xm = xmin - x;
                        if (xm > 7.0 && fln < 15.0) {
                            break;
                        }
                    }
                    xdmy = x;
                    xdmln = xln;
                    xinc = 0.0;
                    if (x < xmin) {
                        nx = (int) x;
                        xinc = xmin - nx;
                        xdmy = x + xinc;
                        xdmln = Math.log(xdmy);
                    }
                    t = fn * xdmln;
                    t1 = xdmln + xdmln;
                    t2 = t + xdmln;
                    tk = Math.max(Math.abs(t), Math.max(Math.abs(t1), Math.abs(t2)));
                    if (tk <= elim) {
                        L10();
                    }
                    return;
                }
                nz[0]++;
                mm--;
                ans[mm] = 0.;
                if (mm == 0) {
                    return;
                }
            }
            nn = (int) fln + 1;
            np = n + 1;
            t1 = (n + 1) * xln;
            t = Math.exp(-t1);
            s = t;
            den = x;
            for (i = 1; i <= nn; i++) {
                den += 1.;
                trm[i] = Math.pow(den, (double) -np);
                s += trm[i];
            }
            ans[0] = s;
            if (n == 0 && kode == 2) {
                ans[0] = s + xln;
            }
            if (mm != 1) {
                tol = wdtol / 5.0;
                for (j = 1; j < mm; j++) {
                    t /= x;
                    s = t;
                    tols = t * tol;
                    den = x;
                    for (i = 1; i <= nn; i++) {
                        den += 1.;
                        trm[i] /= den;
                        s += trm[i];
                        if (trm[i] < tols) {
                            break;
                        }
                    }
                    ans[j] = s;
                }
            }
            return;
        }

        private static void L20() {
            for (i = 1; i <= nx; i++) {
                s += 1. / (x + (nx - i));
            }
            L30();
        }

        public static void L30() {
            if (kode != 2) {
                ans[0] = s - xdmln;
            } else if (xdmy != x) {
                xq = xdmy / x;
                ans[0] = s - Math.log(xq);
            }
        }

        public static void L10() {
            tss = Math.exp(-t);
            tt = 0.5 / xdmy;
            t1 = tt;
            tst = wdtol * tt;
            if (nn != 0) {
                t1 = tt + 1.0 / fn;
            }
            rxsq = 1.0 / (xdmy * xdmy);
            ta = 0.5 * rxsq;
            t = (fn + 1) * ta;
            s = t * bvalues[2];
            if (Math.abs(s) >= tst) {
                tk = 2.0;
                for (k = 4; k <= 22; k++) {
                    t = t * ((tk + fn + 1) / (tk + 1.0)) * ((tk + fn) / (tk + 2.0)) * rxsq;
                    trm[k] = t * bvalues[k - 1];
                    if (Math.abs(trm[k]) < tst) {
                        break;
                    }
                    s += trm[k];
                    tk += 2.;
                }
            }
            s = (s + t1) * tss;
            if (xinc != 0.0) {
                nx = (int) xinc;
                np = nn + 1;
                if (nx > n_max) {
                    nz[0] = 0;
                    ierr[0] = 3;
                    return;
                } else {
                    if (nn == 0) {
                        L20();
                        L30();
                        return;
                    }
                    xm = xinc - 1.0;
                    fx = x + xm;
                    for (i = 1; i <= nx; i++) {
                        trmr[i] = Math.pow(fx, (double) -np);
                        s += trmr[i];
                        xm -= 1.;
                        fx = x + xm;
                    }
                }
            }
            ans[mm - 1] = s;
            if (fn == 0) {
                L30();
                return;
            }
            for (j = 2; j <= mm; j++) {
                fn--;
                tss *= xdmy;
                t1 = tt;
                if (fn != 0) {
                    t1 = tt + 1.0 / fn;
                }
                t = (fn + 1) * ta;
                s = t * bvalues[2];
                if (Math.abs(s) >= tst) {
                    tk = 4 + fn;
                    for (k = 4; k <= 22; k++) {
                        trm[k] = trm[k] * (fn + 1) / tk;
                        if (Math.abs(trm[k]) < tst) {
                            break;
                        }
                        s += trm[k];
                        tk += 2.;
                    }
                }
                s = (s + t1) * tss;
                if (xinc != 0.0) {
                    if (fn == 0) {
                        L20();
                        L30();
                        return;
                    }
                    xm = xinc - 1.0;
                    fx = x + xm;
                    for (i = 1; i <= nx; i++) {
                        trmr[i] = trmr[i] * fx;
                        s += trmr[i];
                        xm -= 1.;
                        fx = x + xm;
                    }
                }
                ans[mm - j] = s;
                if (fn == 0) {
                    L30();
                }
            }
        }
    }

    @Primitive("psigamma")
    public static double psigamma(double x, double deriv) {
        int k, n;
        if (DoubleVector.isNaN(x)) {
            return x;
        }
        deriv = Math.floor(deriv + 0.5);
        n = (int) deriv;
        double[] ans = new double[1];
        double[] xd = new double[] { x };
        int[] nz = new int[1];
        int[] ierr = new int[1];
        dpsifn.dpsifn(x, n, 1, 1, ans, nz, ierr);
        ans[0] = -ans[0];
        for (k = 1; k <= n; k++) {
            ans[0] *= (-k);
        }
        return ans[0];
    }
}
