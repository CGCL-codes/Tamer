package org.jquantlib.math.matrixutilities;

import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.lang.exceptions.LibraryException;

@QualityAssurance(quality = Quality.Q1_TRANSLATION, version = Version.OTHER, reviewers = { "Richard Gomes" })
public class CholeskyDecomposition {

    private static final String MATRIX_IS_NOT_SIMMETRIC_POSITIVE = "Matrix is not symmetric positive definite.";

    /** Row and column dimension (square matrix). */
    private final int n;

    /** Matrix for internal storage of decomposition. */
    private final Matrix L;

    /** Symmetric and positive definite flag. */
    private boolean isspd;

    /**
     * Cholesky algorithm for symmetric and positive definite matrix.
     *
     * @param A is a square, symmetric matrix.
     * @return Structure to access L and isspd flag.
     */
    public CholeskyDecomposition(final Matrix A) {
        QL.require(A.rows() == A.cols(), Matrix.MATRIX_MUST_BE_SQUARE);
        this.n = A.rows();
        this.L = new Matrix(n, n);
        this.isspd = (A.rows() == A.cols());
        for (int j = 0; j < n; j++) {
            double d = 0.0;
            for (int k = 0; k < j; k++) {
                double s = 0.0;
                for (int i = 0; i < k; i++) {
                    s += L.$[L.addr.op(k, i)] * L.$[L.addr.op(j, i)];
                }
                L.$[L.addr.op(j, k)] = s = (A.$[A.addr.op(j, k)] - s) / L.$[L.addr.op(k, k)];
                d = d + s * s;
                isspd = isspd & (A.$[A.addr.op(k, j)] == A.$[A.addr.op(j, k)]);
            }
            d = A.$[A.addr.op(j, j)] - d;
            isspd = isspd && (d > 0.0);
            L.$[L.addr.op(j, j)] = Math.sqrt(Math.max(d, 0.0));
            for (int k = j + 1; k < n; k++) {
                L.$[L.addr.op(j, k)] = 0.0;
            }
        }
    }

    /**
     * Is the matrix symmetric and positive definite?
     *
     * @return true if A is symmetric and positive definite.
     */
    public boolean isSPD() {
        return isspd;
    }

    /**
     * Return triangular factor.
     *
     * @return L
     */
    public Matrix L() {
        return L.clone();
    }

    /**
     * Solve A*X = B
     *
     * @param m a Matrix with as many rows as A and any number of columns.
     * @return X so that L*L'*X = B
     * @exception IllegalArgumentException Matrix row dimensions must agree.
     * @exception LibraryException Matrix is not symmetric positive definite.
     */
    public Matrix solve(final Matrix B) {
        QL.require(B.rows() == this.n, Matrix.MATRIX_IS_INCOMPATIBLE);
        if (!this.isSPD()) throw new LibraryException(MATRIX_IS_NOT_SIMMETRIC_POSITIVE);
        final int nx = B.cols();
        final Matrix X = B.clone();
        for (int k = 0; k < n; k++) {
            for (int j = 0; j < nx; j++) {
                for (int i = 0; i < k; i++) {
                    X.$[X.addr.op(k, j)] -= X.$[X.addr.op(i, j)] * L.$[L.addr.op(k, i)];
                }
                X.$[X.addr.op(k, j)] /= L.$[L.addr.op(k, k)];
            }
        }
        for (int k = n - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                for (int i = k + 1; i < n; i++) {
                    X.$[X.addr.op(k, j)] -= X.$[X.addr.op(i, j)] * L.$[L.addr.op(i, k)];
                }
                X.$[X.addr.op(k, j)] /= L.$[L.addr.op(k, k)];
            }
        }
        return X;
    }
}
