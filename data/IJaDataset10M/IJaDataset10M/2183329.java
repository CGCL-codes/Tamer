package org.jmol.util;

import java.util.Arrays;
import java.util.Comparator;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

/**
 * Eigenvalues and eigenvectors of a real matrix.
 * 
 * adapted by Bob Hanson from http://math.nist.gov/javanumerics/jama/ (public
 * domain)
 * 
 * 
 * output is as a set of double[n] columns, but for Jmol we use
 * getEigenvectorsFloatTransformed to return them as a set of rows for easier
 * use as A[0], A[1], etc.
 * 
 * <P>
 * If A is symmetric, then A = V*D*V' where the eigenvalue matrix D is diagonal
 * and the eigenvector matrix V is orthogonal. I.e. A =
 * V.times(D.times(V.transpose())) and V.times(V.transpose()) equals the
 * identity matrix.
 * <P>
 * If A is not symmetric, then the eigenvalue matrix D is block diagonal with
 * the real eigenvalues in 1-by-1 blocks and any complex eigenvalues, lambda +
 * i*mu, in 2-by-2 blocks, [lambda, mu; -mu, lambda]. The columns of V represent
 * the eigenvectors in the sense that A*V = V*D, i.e. A.times(V) equals
 * V.times(D). The matrix V may be badly conditioned, or even singular, so the
 * validity of the equation A = V*D*inverse(V) depends upon V.cond().
 **/
public class Eigen {

    public Eigen(int n) {
        this.n = n;
        V = new double[n][n];
        d = new double[n];
        e = new double[n];
    }

    public Eigen(double[][] A) {
        this(A.length);
        calc(A);
    }

    public Eigen(double[][] mat, Vector3f[] unitVectors, float[] lengths) {
        this(mat);
        set(unitVectors, lengths);
        sort(unitVectors, lengths);
    }

    private void set(Vector3f[] unitVectors, float[] lengths) {
        float[][] eigenVectors = getEigenvectorsFloatTransposed();
        double[] eigenValues = getRealEigenvalues();
        for (int i = 0; i < n; i++) {
            if (unitVectors[i] == null) unitVectors[i] = new Vector3f();
            unitVectors[i].set(eigenVectors[i]);
            lengths[i] = (float) Math.sqrt(Math.abs(eigenValues[i]));
        }
    }

    /**
   * Check for symmetry, then construct the eigenvalue decomposition
   * 
   * @param A
   *        Square matrix
   */
    public void calc(double[][] A) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                V[i][j] = A[i][j];
            }
        }
        tred2();
        tql2();
    }

    /**
   * Return the real parts of the eigenvalues
   * 
   * @return real(diag(D))
   */
    public double[] getRealEigenvalues() {
        return d;
    }

    /**
   * Return the imaginary parts of the eigenvalues
   * 
   * @return imag(diag(D))
   */
    public double[] getImagEigenvalues() {
        return e;
    }

    public double[] getEigenvalues() {
        return d;
    }

    /**
   * transpose V and turn into floats
   * 
   * @return ROWS of eigenvectors f[0], f[1], f[2], etc.
   */
    public float[][] getEigenvectorsFloatTransposed() {
        float[][] f = new float[n][n];
        for (int i = n; --i >= 0; ) for (int j = n; --j >= 0; ) f[j][i] = (float) V[i][j];
        return f;
    }

    public Vector3f[] getEigenVectors3() {
        Vector3f[] v = new Vector3f[3];
        for (int i = 0; i < 3; i++) {
            v[i] = new Vector3f((float) V[0][i], (float) V[1][i], (float) V[2][i]);
        }
        return v;
    }

    /**
   * Row and column dimension (square matrix).
   * 
   * @serial matrix dimension.
   */
    private int n;

    /**
   * Arrays for internal storage of eigenvalues.
   * 
   * @serial internal storage of eigenvalues.
   */
    private double[] d, e;

    /**
   * Array for internal storage of eigenvectors.
   * 
   * @serial internal storage of eigenvectors.
   */
    private double[][] V;

    private void tred2() {
        for (int j = 0; j < n; j++) {
            d[j] = V[n - 1][j];
        }
        for (int i = n - 1; i > 0; i--) {
            double scale = 0.0;
            double h = 0.0;
            for (int k = 0; k < i; k++) {
                scale = scale + Math.abs(d[k]);
            }
            if (scale == 0.0) {
                e[i] = d[i - 1];
                for (int j = 0; j < i; j++) {
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                    V[j][i] = 0.0;
                }
            } else {
                for (int k = 0; k < i; k++) {
                    d[k] /= scale;
                    h += d[k] * d[k];
                }
                double f = d[i - 1];
                double g = Math.sqrt(h);
                if (f > 0) {
                    g = -g;
                }
                e[i] = scale * g;
                h = h - f * g;
                d[i - 1] = f - g;
                for (int j = 0; j < i; j++) {
                    e[j] = 0.0;
                }
                for (int j = 0; j < i; j++) {
                    f = d[j];
                    V[j][i] = f;
                    g = e[j] + V[j][j] * f;
                    for (int k = j + 1; k <= i - 1; k++) {
                        g += V[k][j] * d[k];
                        e[k] += V[k][j] * f;
                    }
                    e[j] = g;
                }
                f = 0.0;
                for (int j = 0; j < i; j++) {
                    e[j] /= h;
                    f += e[j] * d[j];
                }
                double hh = f / (h + h);
                for (int j = 0; j < i; j++) {
                    e[j] -= hh * d[j];
                }
                for (int j = 0; j < i; j++) {
                    f = d[j];
                    g = e[j];
                    for (int k = j; k <= i - 1; k++) {
                        V[k][j] -= (f * e[k] + g * d[k]);
                    }
                    d[j] = V[i - 1][j];
                    V[i][j] = 0.0;
                }
            }
            d[i] = h;
        }
        for (int i = 0; i < n - 1; i++) {
            V[n - 1][i] = V[i][i];
            V[i][i] = 1.0;
            double h = d[i + 1];
            if (h != 0.0) {
                for (int k = 0; k <= i; k++) {
                    d[k] = V[k][i + 1] / h;
                }
                for (int j = 0; j <= i; j++) {
                    double g = 0.0;
                    for (int k = 0; k <= i; k++) {
                        g += V[k][i + 1] * V[k][j];
                    }
                    for (int k = 0; k <= i; k++) {
                        V[k][j] -= g * d[k];
                    }
                }
            }
            for (int k = 0; k <= i; k++) {
                V[k][i + 1] = 0.0;
            }
        }
        for (int j = 0; j < n; j++) {
            d[j] = V[n - 1][j];
            V[n - 1][j] = 0.0;
        }
        V[n - 1][n - 1] = 1.0;
        e[0] = 0.0;
    }

    private void tql2() {
        for (int i = 1; i < n; i++) {
            e[i - 1] = e[i];
        }
        e[n - 1] = 0.0;
        double f = 0.0;
        double tst1 = 0.0;
        double eps = Math.pow(2.0, -52.0);
        for (int l = 0; l < n; l++) {
            tst1 = Math.max(tst1, Math.abs(d[l]) + Math.abs(e[l]));
            int m = l;
            while (m < n) {
                if (Math.abs(e[m]) <= eps * tst1) {
                    break;
                }
                m++;
            }
            if (m > l) {
                int iter = 0;
                do {
                    iter = iter + 1;
                    double g = d[l];
                    double p = (d[l + 1] - g) / (2.0 * e[l]);
                    double r = hypot(p, 1.0);
                    if (p < 0) {
                        r = -r;
                    }
                    d[l] = e[l] / (p + r);
                    d[l + 1] = e[l] * (p + r);
                    double dl1 = d[l + 1];
                    double h = g - d[l];
                    for (int i = l + 2; i < n; i++) {
                        d[i] -= h;
                    }
                    f = f + h;
                    p = d[m];
                    double c = 1.0;
                    double c2 = c;
                    double c3 = c;
                    double el1 = e[l + 1];
                    double s = 0.0;
                    double s2 = 0.0;
                    for (int i = m - 1; i >= l; i--) {
                        c3 = c2;
                        c2 = c;
                        s2 = s;
                        g = c * e[i];
                        h = c * p;
                        r = hypot(p, e[i]);
                        e[i + 1] = s * r;
                        s = e[i] / r;
                        c = p / r;
                        p = c * d[i] - s * g;
                        d[i + 1] = h + s * (c * g + s * d[i]);
                        for (int k = 0; k < n; k++) {
                            h = V[k][i + 1];
                            V[k][i + 1] = s * V[k][i] + c * h;
                            V[k][i] = c * V[k][i] - s * h;
                        }
                    }
                    p = -s * s2 * c3 * el1 * e[l] / dl1;
                    e[l] = s * p;
                    d[l] = c * p;
                } while (Math.abs(e[l]) > eps * tst1);
            }
            d[l] = d[l] + f;
            e[l] = 0.0;
        }
        for (int i = 0; i < n - 1; i++) {
            int k = i;
            double p = d[i];
            for (int j = i + 1; j < n; j++) {
                if (d[j] < p) {
                    k = j;
                    p = d[j];
                }
            }
            if (k != i) {
                d[k] = d[i];
                d[i] = p;
                for (int j = 0; j < n; j++) {
                    p = V[j][i];
                    V[j][i] = V[j][k];
                    V[j][k] = p;
                }
            }
        }
    }

    private static double hypot(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        } else {
            r = 0.0;
        }
        return r;
    }

    public static Quadric getEllipsoid(double[][] a) {
        Eigen eigen = new Eigen(3);
        eigen.calc(a);
        Matrix3f m = new Matrix3f();
        float[] mm = new float[9];
        for (int i = 0, p = 0; i < 3; i++) for (int j = 0; j < 3; j++) mm[p++] = (float) a[i][j];
        m.set(mm);
        Vector3f[] evec = eigen.getEigenVectors3();
        Vector3f n = new Vector3f();
        Vector3f cross = new Vector3f();
        for (int i = 0; i < 3; i++) {
            n.set(evec[i]);
            m.transform(n);
            cross.cross(n, evec[i]);
            Logger.info("v[i], n, n x v[i]" + evec[i] + " " + n + " " + cross);
            n.set(evec[i]);
            n.normalize();
            cross.cross(evec[i], evec[(i + 1) % 3]);
            Logger.info("draw id eigv" + i + " " + Escape.escape(evec[i]) + " color " + (i == 0 ? "red" : i == 1 ? "green" : "blue") + " # " + n + " " + cross);
        }
        Logger.info("eigVl (" + eigen.d[0] + " + " + eigen.e[0] + "I) (" + eigen.d[1] + " + " + eigen.e[1] + "I) (" + eigen.d[2] + " + " + eigen.e[2] + "I)");
        Vector3f[] unitVectors = new Vector3f[3];
        float[] lengths = new float[3];
        eigen.set(unitVectors, lengths);
        sort(unitVectors, lengths);
        return new Quadric(unitVectors, lengths, false);
    }

    public static Quadric getEllipsoid(Vector3f[] vectors, float[] lengths, boolean isThermal) {
        Vector3f[] unitVectors = vectors.clone();
        sort(unitVectors, lengths);
        return new Quadric(unitVectors, lengths, isThermal);
    }

    /**
   * sorts vectors by absolute value and normalizes them
   * @param vectors
   * @param lengths
   */
    private static void sort(Vector3f[] vectors, float[] lengths) {
        Object[][] o = new Object[][] { new Object[] { vectors[0], Float.valueOf(Math.abs(lengths[0])) }, new Object[] { vectors[1], Float.valueOf(Math.abs(lengths[1])) }, new Object[] { vectors[2], Float.valueOf(Math.abs(lengths[2])) } };
        Arrays.sort(o, new EigenSort());
        for (int i = 0; i < 3; i++) {
            vectors[i] = new Vector3f((Vector3f) o[i][0]);
            vectors[i].normalize();
            lengths[i] = ((Float) o[i][1]).floatValue();
        }
    }

    /**
   * sort from smallest to largest
   * 
   */
    protected static class EigenSort implements Comparator<Object[]> {

        public int compare(Object[] o1, Object[] o2) {
            float a = ((Float) o1[1]).floatValue();
            float b = ((Float) o2[1]).floatValue();
            return (a < b ? -1 : a > b ? 1 : 0);
        }
    }
}
