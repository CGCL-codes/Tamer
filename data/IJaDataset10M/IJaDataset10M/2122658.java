package de.telekom.laboratories.multitouch.util;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * A ulilty class for Singular Value Decomposition (SVD).
 * The original source code is from <a href="http://www.idiom.com/~zilla/" target="_blank">http://www.idiom.com/~zilla/</a>, 
 * which is a translation of NR svdcmp.c from matlab.
 * @author Michael Nischt
 * @version 0.1
 */
public class SingularValueDecomposition {

    public static void svd(double[][] a, double[] w, double[][] v) {
        int i;
        int its;
        int j;
        int jj;
        int k;
        int l = 0;
        int nm = 0;
        boolean flag;
        int m = a.length;
        int n = a[0].length;
        double c;
        double f;
        double h;
        double s;
        double x;
        double y;
        double z;
        double anorm = 0.0;
        double g = 0.0;
        double scale = 0.0;
        assert (m >= n);
        double[] rv1 = new double[n];
        for (i = 0; i < n; i++) {
            l = i + 1;
            rv1[i] = scale * g;
            g = s = scale = 0.;
            if (i < m) {
                for (k = i; k < m; k++) {
                    scale += abs(a[k][i]);
                }
                if (scale != 0.0) {
                    for (k = i; k < m; k++) {
                        a[k][i] /= scale;
                        s += a[k][i] * a[k][i];
                    }
                    f = a[i][i];
                    g = -sign(sqrt(s), f);
                    h = f * g - s;
                    a[i][i] = f - g;
                    for (j = l; j < n; j++) {
                        for (s = 0, k = i; k < m; k++) {
                            s += a[k][i] * a[k][j];
                        }
                        f = s / h;
                        for (k = i; k < m; k++) {
                            a[k][j] += f * a[k][i];
                        }
                    }
                    for (k = i; k < m; k++) {
                        a[k][i] *= scale;
                    }
                }
            }
            w[i] = scale * g;
            g = s = scale = 0.0;
            if (i < m && i != n - 1) {
                for (k = l; k < n; k++) {
                    scale += abs(a[i][k]);
                }
                if (scale != 0.) {
                    for (k = l; k < n; k++) {
                        a[i][k] /= scale;
                        s += a[i][k] * a[i][k];
                    }
                    f = a[i][l];
                    g = -sign(sqrt(s), f);
                    h = f * g - s;
                    a[i][l] = f - g;
                    for (k = l; k < n; k++) {
                        rv1[k] = a[i][k] / h;
                    }
                    if (i != m - 1) {
                        for (j = l; j < m; j++) {
                            for (s = 0, k = l; k < n; k++) {
                                s += a[j][k] * a[i][k];
                            }
                            for (k = l; k < n; k++) {
                                a[j][k] += s * rv1[k];
                            }
                        }
                    }
                    for (k = l; k < n; k++) {
                        a[i][k] *= scale;
                    }
                }
            }
            anorm = max(anorm, abs(w[i]) + abs(rv1[i]));
        }
        for (i = n - 1; i >= 0; --i) {
            if (i < n - 1) {
                if (g != 0.) {
                    for (j = l; j < n; j++) {
                        v[j][i] = (a[i][j] / a[i][l]) / g;
                    }
                    for (j = l; j < n; j++) {
                        for (s = 0, k = l; k < n; k++) {
                            s += a[i][k] * v[k][j];
                        }
                        for (k = l; k < n; k++) {
                            v[k][j] += s * v[k][i];
                        }
                    }
                }
                for (j = l; j < n; j++) {
                    v[i][j] = v[j][i] = 0.0;
                }
            }
            v[i][i] = 1.0;
            g = rv1[i];
            l = i;
        }
        for (i = min(m - 1, n - 1); i >= 0; --i) {
            l = i + 1;
            g = w[i];
            if (i < n - 1) {
                for (j = l; j < n; j++) {
                    a[i][j] = 0.0;
                }
            }
            if (g != 0.0) {
                g = 1.0 / g;
                if (i != n - 1) {
                    for (j = l; j < n; j++) {
                        for (s = 0, k = l; k < m; k++) {
                            s += a[k][i] * a[k][j];
                        }
                        f = (s / a[i][i]) * g;
                        for (k = i; k < m; k++) {
                            a[k][j] += f * a[k][i];
                        }
                    }
                }
                for (j = i; j < m; j++) {
                    a[j][i] *= g;
                }
            } else {
                for (j = i; j < m; j++) {
                    a[j][i] = 0.0;
                }
            }
            a[i][i] += 1.0;
        }
        for (k = n - 1; k >= 0; --k) {
            for (its = 1; its <= 30; ++its) {
                flag = true;
                for (l = k; l >= 0; --l) {
                    nm = l - 1;
                    if ((abs(rv1[l]) + anorm) == anorm) {
                        flag = false;
                        break;
                    }
                    if ((abs(w[nm]) + anorm) == anorm) {
                        break;
                    }
                }
                if (flag) {
                    c = 0.0;
                    s = 1.0;
                    for (i = l; i <= k; i++) {
                        f = s * rv1[i];
                        rv1[i] = c * rv1[i];
                        if ((abs(f) + anorm) == anorm) {
                            break;
                        }
                        g = w[i];
                        h = pythag(f, g);
                        w[i] = h;
                        h = 1.0 / h;
                        c = g * h;
                        s = -f * h;
                        for (j = 0; j < m; j++) {
                            y = a[j][nm];
                            z = a[j][i];
                            a[j][nm] = y * c + z * s;
                            a[j][i] = z * c - y * s;
                        }
                    }
                }
                z = w[k];
                if (l == k) {
                    if (z < 0.0) {
                        w[k] = -z;
                        for (j = 0; j < n; j++) {
                            v[j][k] = -v[j][k];
                        }
                    }
                    break;
                }
                assert (its < 50);
                x = w[l];
                nm = k - 1;
                y = w[nm];
                g = rv1[nm];
                h = rv1[k];
                f = ((y - z) * (y + z) + (g - h) * (g + h)) / (2 * h * y);
                g = pythag(f, 1.0);
                f = ((x - z) * (x + z) + h * ((y / (f + sign(g, f))) - h)) / x;
                c = s = 1.0;
                for (j = l; j <= nm; j++) {
                    i = j + 1;
                    g = rv1[i];
                    y = w[i];
                    h = s * g;
                    g = c * g;
                    z = pythag(f, h);
                    rv1[j] = z;
                    c = f / z;
                    s = h / z;
                    f = x * c + g * s;
                    g = g * c - x * s;
                    h = y * s;
                    y *= c;
                    for (jj = 0; jj < n; jj++) {
                        x = v[jj][j];
                        z = v[jj][i];
                        v[jj][j] = x * c + z * s;
                        v[jj][i] = z * c - x * s;
                    }
                    z = pythag(f, h);
                    w[j] = z;
                    if (z != 0.0) {
                        z = 1.0 / z;
                        c = f * z;
                        s = h * z;
                    }
                    f = c * g + s * y;
                    x = c * y - s * g;
                    for (jj = 0; jj < m; ++jj) {
                        y = a[jj][j];
                        z = a[jj][i];
                        a[jj][j] = y * c + z * s;
                        a[jj][i] = z * c - y * s;
                    }
                }
                rv1[l] = 0.0;
                rv1[k] = f;
                w[k] = x;
            }
        }
    }

    static final double abs(double a) {
        return (a < 0.0) ? -a : a;
    }

    static final double pythag(double a, double b) {
        return sqrt(a * a + b * b);
    }

    static final double sign(double a, double b) {
        return (b) >= 0. ? abs(a) : -abs(a);
    }

    private static void main(String... args) {
        int nr = 6;
        int nc = 5;
        double[][] M = new double[nr][nc];
        for (int r = 0; r < nr; r++) {
            float p = (float) r / (nr - 1);
            for (int c = 0; c < nc; c++) {
                float frac = (float) c / (nc - 1);
                M[r][c] = pow(frac, p);
            }
        }
        double[] w = new double[nc];
        double[][] V = new double[nc][nc];
        svd(M, w, V);
        System.exit(0);
    }
}
