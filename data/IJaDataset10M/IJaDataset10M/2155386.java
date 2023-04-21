package org.astrogrid.cluster.cluster;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static java.lang.Math.*;

public class Objective {

    public static class result {

        public double getF() {
            return pf;
        }

        public Vector getDf() {
            return new DenseVector(pdf);
        }

        private double pf;

        private Vector pdf;

        public result(double rf, Vector rdf) {
            pf = rf;
            pdf = rdf;
        }
    }

    public static result objectiveSpherical(Object cvk, Vector q, Vector mu, Matrix data_er, Matrix S) {
        if (cvk instanceof Vector) {
            double cvkd = ((Vector) cvk).get(0);
            double cv = pow(cvkd, 2.0);
            int ndata = data_er.numRows();
            double f = 0.0;
            for (int n = 0; n < ndata; n++) {
                f = f + q.get(n) * (-0.5 * sum(log(add(cv, S.sliceRow(n)))) - 0.5 * sum(divide(pow(sub(data_er.sliceRow(n), mu), 2.0), (add(cv, S.sliceRow(n))))));
            }
            f = -f;
            Matrix df = zeros(1, 1);
            for (int n = 0; n < ndata; n++) {
                df = add(df, q.get(n) * (-sum(recip(cvkd, add(cv, S.sliceRow(n)))) + sum(divide(pow(sub(data_er.sliceRow(n), mu), 2.0).scale(cvkd), pow(add(cv, S.sliceRow(n)), 2.0)))));
            }
            df.scale(-1.0);
            return new result(f, df.asVector());
        } else {
            throw new IllegalArgumentException("cvk is not a double but a " + cvk.getClass().getCanonicalName());
        }
    }

    public static result objectiveFull(Object cvkm, Vector q, Vector mu, Matrix data_er, Matrix S) {
        if (cvkm instanceof Vector) {
            Matrix cvk = new AGDenseMatrix((Vector) cvkm);
            int ndata = data_er.numRows();
            int ndim = data_er.numColumns();
            cvk = reshape(cvk, ndim, ndim);
            Matrix cv = pow(cvk, 2.0);
            double f = 0.0;
            for (int n = 0; n < ndata; n++) {
                f = f + q.get(n) * (-0.5 * log(det(add(cv, diag(S.sliceRow(n))))) - 0.5 * multAt(sub(data_er.sliceRow(n), mu), mult(inv(add(cv, diag(S.sliceRow(n)))), (sub(data_er.sliceRow(n), mu)))).asScalar());
            }
            f = -f;
            Matrix df = zeros(ndim, ndim);
            for (int n = 0; n < ndata; n++) {
                Matrix tmp = sub(vprod(sub(data_er.sliceRow(n), mu)), add(cv, diag(S.sliceRow(n))));
                tmp = sub(tmp.scale(2.0), diag(diag(tmp)));
                df = add(df, (mult(tmp, mult(inv(add(cv, diag(S.sliceRow(n)))), mult(inv(add(cv, diag(S.sliceRow(n)))), cvk)))).scale(q.get(n)));
            }
            df.scale(-1.0);
            return new result(f, df.asVector());
        } else {
            throw new IllegalArgumentException("cvk is not a Matrix but a " + cvkm.getClass().getCanonicalName());
        }
    }

    public static result objectiveDiag(Object cvk, Vector q, Vector mu, Matrix data_er, Matrix S) {
        if (cvk instanceof Vector) {
            Vector cvkv = (Vector) cvk;
            Vector cv = pow(cvkv, 2.0);
            int ndata = data_er.numRows();
            int ndim = data_er.numColumns();
            double f = 0.0;
            for (int n = 0; n < ndata; n++) {
                f = f + q.get(n) * (-1 / 2.0 * sum(log(add(cv, S.sliceRow(n)))) - 1 / 2.0 * sum(divide(pow(sub(data_er.sliceRow(n), mu), 2.0), (add(cv, S.sliceRow(n))))));
            }
            f = -f;
            Vector df = new DenseVector(ndim).zero();
            for (int i = 0; i < ndim; i++) {
                Vector tmpt = sub(recip(add(cv.get(i), S.sliceCol(i))), divide(pow(sub(data_er.sliceCol(i), mu.get(i)), 2.0), (pow(add(cv.get(i), S.sliceCol(i)), 2.0))));
                df.set(i, sum(times(q, tmpt)) * cvkv.get(i));
            }
            return new result(f, df);
        } else {
            throw new IllegalArgumentException("cvk is not a vector but a " + cvk.getClass().getCanonicalName());
        }
    }
}
