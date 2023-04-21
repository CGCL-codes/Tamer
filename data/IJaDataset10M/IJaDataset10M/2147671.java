package org.jquantlib.processes;

import org.jquantlib.QL;
import org.jquantlib.math.Ops;
import org.jquantlib.math.integrals.GaussKronrodAdaptive;
import org.jquantlib.math.matrixutilities.Array;
import org.jquantlib.math.matrixutilities.Matrix;

public abstract class LfmCovarianceParameterization {

    protected int size_;

    private final int factors_;

    public LfmCovarianceParameterization(final int size, final int factors) {
        if (System.getProperty("EXPERIMENTAL") == null) throw new UnsupportedOperationException("Work in progress");
        this.size_ = size;
        this.factors_ = factors;
    }

    public int size() {
        return size_;
    }

    public int factors() {
        return factors_;
    }

    public abstract Matrix diffusion(double t, final Array x);

    public Matrix diffusion(final double t) {
        return diffusion(t, new Array(0));
    }

    public Matrix covariance(final double t, final Array x) {
        final Matrix sigma = this.diffusion(t, x);
        return sigma.mul(sigma.transpose());
    }

    public Matrix covariance(final double t) {
        return diffusion(t, new Array(0));
    }

    public Matrix integratedCovariance(final double t, final Array x) {
        QL.require(!x.empty(), "can not handle given x here");
        final Matrix tmp = new Matrix(size_, size_);
        for (int i = 0; i < size_; ++i) {
            for (int j = 0; j <= i; ++j) {
                final Var_Helper helper = new Var_Helper(this, i, j);
                final GaussKronrodAdaptive integrator = new GaussKronrodAdaptive(1e-10, 10000);
                for (int k = 0; k < 64; ++k) {
                    tmp.set(i, j, tmp.get(i, j) + integrator.op(helper, k * t / 64.0, (k + 1) * t / 64.0));
                }
                tmp.set(j, i, tmp.get(i, j));
            }
        }
        return tmp;
    }

    public Matrix integratedCovariance(final double t) {
        return integratedCovariance(t, new Array(0));
    }

    private static class Var_Helper implements Ops.DoubleOp {

        private final int i_, j_;

        private final LfmCovarianceParameterization param_;

        public Var_Helper(final LfmCovarianceParameterization param, final int i, final int j) {
            this.i_ = i;
            this.j_ = j;
            this.param_ = param;
        }

        public double op(final double t) {
            final Matrix m = param_.diffusion(t);
            return m.constRangeRow(i_).innerProduct(m.constRangeRow(j_));
        }
    }
}
