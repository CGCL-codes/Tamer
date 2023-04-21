package org.ode4j.math;

import org.ode4j.ode.internal.Misc;

public class DMatrixN {

    private final double[] v;

    private final int MAX_I;

    private final int MAX_J;

    public DMatrixN(int max_i, int max_j) {
        v = new double[max_i * max_j];
        MAX_I = max_i;
        MAX_J = max_j;
    }

    public DMatrixN(DMatrixN matrix) {
        this(matrix.MAX_I, matrix.MAX_J);
        set(matrix);
    }

    /**
	 * Private to enforce usage of wrap().
	 * @param a
	 */
    private DMatrixN(double[] a, int i, int j) {
        v = a;
        MAX_I = i;
        MAX_J = j;
    }

    public static DMatrixN wrap(double[] a, int i, int j) {
        return new DMatrixN(a, i, j);
    }

    public void set(DMatrixN mat) {
        System.arraycopy(mat.v, 0, v, 0, v.length);
    }

    public void setOfs(int ofs, DVector3 v3) {
        v[ofs] = v3.get0();
        v[ofs + 1] = v3.get1();
        v[ofs + 2] = v3.get2();
    }

    public void setCol(int i, DVector3 v3) {
        int ofs = i * 4;
        v[ofs] = v3.get0();
        v[ofs + 1] = v3.get1();
        v[ofs + 2] = v3.get2();
    }

    public void set(double[] da, int da_ofs) {
        System.arraycopy(da, da_ofs, v, 0, v.length);
    }

    public void set(double a) {
        for (int i = 0; i < v.length; i++) v[i] = a;
    }

    public void set(double[] _data, int rowskip, int colskip) {
        for (int i = 0; i < MAX_I; i++) {
            for (int j = 0; j < MAX_J; j++) v[i * MAX_J + j] = _data[i * rowskip + j * colskip];
        }
    }

    @Override
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("dMatrixN=" + MAX_I + "*" + MAX_J + " \n");
        b.append("[[");
        for (int i = 0; i < MAX_I; i++) {
            for (int j = 0; j < MAX_J - 1; j++) {
                b.append(v[i * MAX_J + j]).append(", ");
            }
            b.append(v[i * MAX_J + MAX_J - 1]).append("],\n ");
        }
        b.append("]");
        return b.toString();
    }

    /**
	 * This returns a new Matrix containing a transposed of this matrix.
	 * Creates new matrix.
	 * @return New transposed matrix.
	 */
    public DMatrixN reTranspose() {
        DMatrixN r = new DMatrixN(MAX_I, MAX_J);
        for (int i = 0; i < MAX_I; i++) {
            for (int j = 0; j < MAX_J; j++) r.v[j * MAX_I + i] = v[i * MAX_J + j];
        }
        return r;
    }

    /**
	 * This returns a new Matrix containing a copy of the selected
	 * sub-matrix.
	 * @param np
	 * @param p
	 * @param nq
	 * @param q
	 * @return New sub-matrix.
	 */
    public DMatrixN newSubMatrix(int np, int[] p, int nq, int[] q) {
        if (np < 1 || nq < 1) dDebug(0, "Matrix select, bad index array sizes");
        DMatrixN r = new DMatrixN(np, nq);
        for (int i = 0; i < np; i++) {
            for (int j = 0; j < nq; j++) {
                if (p[i] < 0 || p[i] >= MAX_I || q[i] < 0 || q[i] >= MAX_J) dDebug(0, "Matrix select, bad index arrays");
                r.v[i * nq + j] = v[p[i] * MAX_J + q[j]];
            }
        }
        return r;
    }

    public DMatrixN mulNew(final DMatrixN a) {
        if (MAX_I != a.MAX_J) dDebug(0, "matrix *, mismatched sizes");
        DMatrixN r = new DMatrixN(MAX_I, a.MAX_J);
        for (int i = 0; i < MAX_I; i++) {
            for (int j = 0; j < a.MAX_J; j++) {
                double sum = 0;
                for (int k = 0; k < MAX_J; k++) sum += v[i * MAX_J + k] * a.v[k * a.MAX_J + j];
                r.v[i * a.MAX_I + j] = sum;
            }
        }
        return r;
    }

    void plusEq(final DMatrixN a) {
        if (MAX_I != a.MAX_I || MAX_J != a.MAX_J) dDebug(0, "matrix +=, mismatched sizes");
        for (int i = 0; i < MAX_I * MAX_J; i++) v[i] += a.v[i];
    }

    void subEq(final DMatrixN a) {
        if (MAX_I != a.MAX_I || MAX_J != a.MAX_J) dDebug(0, "matrix -=, mismatched sizes");
        for (int i = 0; i < MAX_I * MAX_J; i++) v[i] -= a.v[i];
    }

    public void clearUpperTriangle() {
        if (MAX_I != MAX_J) dDebug(0, "clearUpperTriangle() only works on square matrices");
        for (int i = 0; i < MAX_I; i++) {
            for (int j = i + 1; j < MAX_J; j++) v[i * MAX_J + j] = 0;
        }
    }

    public void clearLowerTriangle() {
        if (MAX_I != MAX_J) dDebug(0, "clearLowerTriangle() only works on square matrices");
        for (int i = 0; i < MAX_I; i++) {
            for (int j = 0; j < i; j++) v[i * MAX_J + j] = 0;
        }
    }

    DMatrixN makeRandom(double range) {
        for (int i = 0; i < MAX_I; i++) {
            for (int j = 0; j < MAX_J; j++) v[i * MAX_J + j] = (Misc.dRandReal() * (2.0) - (1.0)) * range;
        }
        return this;
    }

    public double maxDifference(final DMatrixN a) {
        if (MAX_J != a.MAX_J || MAX_I != a.MAX_I) dDebug(0, "maxDifference(), mismatched sizes");
        double max = 0;
        for (int i = 0; i < MAX_I; i++) {
            for (int j = 0; j < MAX_J; j++) {
                double diff = Math.abs(v[i * MAX_J + j] - a.v[i * MAX_J + j]);
                if (diff > max) max = diff;
            }
        }
        return max;
    }

    /**
	 * @param i row
	 * @param j column
	 * @param a value at (i,j)
	 */
    public void set(int i, int j, double a) {
        v[i * MAX_J + j] = a;
    }

    /**
	 * @deprecated
	 * @param n
	 * @param msg
	 */
    private void dDebug(int n, String msg) {
        throw new IllegalStateException(msg);
    }
}
