package math.linearalgebra;

import static math.linearalgebra.LinearMath.*;
import math.abstractalgebra.Ring;

/**
 * @author egoff
 *
 */
public class MatrixDivisionStrategyImpl implements MatrixDivisionStrategy {

    public <T> Matrix<T> divide(Ring<T> ring, Matrix<T> inv, Matrix<T> m1) throws SingularMatrixException {
        int om1 = m1.getColumnCount();
        if (m1.getRowCount() != om1) {
            throw new MatrixNotSquareException();
        }
        if (om1 != inv.getRowCount()) {
            throw new DimensionMismatchException();
        }
        int oinv = inv.getColumnCount();
        int numRows = 0;
        MatrixMonad<T> res = new MatrixMonad<T>(om1, oinv + om1, null);
        int[] colPerm = new int[om1];
        for (int i = 0; i < om1; i++) {
            colPerm[i] = i;
        }
        VectorMonad<T> rpivot = new VectorMonad<T>(om1, null);
        {
            for (int i = 0; i < om1; i++) {
                Vector<T> m1row = m1.getRow(i);
                for (int j = 0; j < om1; j++) {
                    res.set(i, j, m1row.get(j));
                }
                Vector<T> invrow = inv.getRow(i);
                for (int j = 0; j < oinv; j++) {
                    res.set(i, j + om1, invrow.get(j));
                }
            }
            int outerLen = oinv + om1;
            double dist = 0;
            for (int i = 0; i < om1; i++) {
                int maxj = i;
                int maxk = i;
                for (int j = i; j < om1; j++) {
                    for (int k = i; k < om1; k++) {
                        if (ring.abs(res.get(j, k)) > ring.abs(res.get(maxj, maxk))) {
                            maxj = j;
                            maxk = k;
                        }
                    }
                }
                T pivot = res.get(maxj, maxk);
                double abspivot = ring.abs(pivot);
                dist = Math.max(dist, abspivot);
                double rel = abspivot / dist;
                if (Double.isNaN(rel) || Math.abs(rel) < TOLERANCE) {
                    break;
                }
                if (i != maxj) {
                    for (int k = i; k < outerLen; k++) {
                        T tmp1 = res.get(i, k);
                        res.set(i, k, res.get(maxj, k));
                        res.set(maxj, k, tmp1);
                    }
                }
                if (i != maxk) {
                    for (int k = 0; k < om1; k++) {
                        T tmp1 = res.get(k, i);
                        res.set(k, i, res.get(k, maxk));
                        res.set(k, maxk, tmp1);
                    }
                    int tmp = colPerm[i];
                    colPerm[i] = colPerm[maxk];
                    colPerm[maxk] = tmp;
                }
                rpivot.set(i, ring.getMul().getInversion().perform(pivot));
                T negrpivot = ring.getAdd().getInversion().perform(rpivot.get(i));
                for (int j = i + 1; j < om1; j++) {
                    T d = res.get(j, i);
                    T konst = ring.getMul().getOperation().perform(d, negrpivot);
                    for (int k = i; k < outerLen; k++) {
                        res.set(j, k, ring.getAdd().getOperation().perform(res.get(j, k), ring.getMul().getOperation().perform(res.get(i, k), konst)));
                    }
                }
                numRows++;
            }
            for (int i = om1 - 1; i >= numRows; i--) {
                for (int k = om1; k < outerLen; k++) {
                    double rel = ring.abs(res.get(i, k)) / dist;
                    if (Double.isNaN(rel)) {
                    } else if (Math.abs(rel) >= TOLERANCE) {
                        throw new SingularMatrixException();
                    } else {
                        res.set(i, k, null);
                    }
                }
            }
            for (int i = numRows - 1; i >= 0; i--) {
                for (int k = i; k < outerLen; k++) {
                    res.set(i, k, ring.getMul().getOperation().perform(res.get(i, k), rpivot.get(i)));
                }
                for (int j = i - 1; j >= 0; j--) {
                    T d = res.get(j, i);
                    T negd = ring.getAdd().getInversion().perform(d);
                    for (int k = i; k < outerLen; k++) {
                        res.set(j, k, ring.getAdd().getOperation().perform(res.get(j, k), ring.getMul().getOperation().perform(res.get(i, k), negd)));
                    }
                }
            }
        }
        MatrixMonad<T> m2 = new MatrixMonad<T>(om1, oinv, null);
        for (int i = 0; i < om1; i++) {
            for (int j = 0; j < oinv; j++) {
                m2.set(colPerm[i], j, res.get(i, om1 + j));
            }
        }
        return m2.getMatrix();
    }
}
