package com.mockturtlesolutions.snifflib.util;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;

/**
Series approximation to the Incomplete Gamma function.
*/
public class SeriesIncGamma extends AbstractIncGammaAlgorithm {

    private int maxiter;

    private DblMatrix eps;

    private DblMatrix normlogGamma;

    private LogGamma loggamma;

    public SeriesIncGamma() {
        this.loggamma = new LogGamma();
        this.maxiter = 500;
        this.eps = new DblMatrix(1.e-10);
    }

    public DblMatrix upperIncGamma(DblMatrix x, DblMatrix alpha) {
        DblMatrix out = this.logLowerIncGamma(x, alpha);
        this.normlogGamma = this.loggamma.logGamma(alpha);
        out = DblMatrix.exp(this.normlogGamma).minus(DblMatrix.exp(out));
        return (out);
    }

    public DblMatrix lowerIncGamma(DblMatrix x, DblMatrix alpha) {
        DblMatrix out = this.logLowerIncGamma(x, alpha);
        return (DblMatrix.exp(out));
    }

    public DblMatrix upperNormIncGamma(DblMatrix x, DblMatrix alpha) {
        return (DblMatrix.ONE.minus(this.lowerNormIncGamma(x, alpha)));
    }

    public DblMatrix lowerNormIncGamma(DblMatrix x, DblMatrix alpha) {
        DblMatrix out = this.logLowerIncGamma(x, alpha);
        this.normlogGamma = this.loggamma.logGamma(alpha);
        out = DblMatrix.exp(out.minus(this.normlogGamma));
        return (out);
    }

    public DblMatrix incGamma(DblMatrix x, DblMatrix alpha) {
        return (this.lowerNormIncGamma(x, alpha));
    }

    /**
        Returns the log of the (non-normalized) lower incomplete gamma function.
        */
    private DblMatrix logLowerIncGamma(DblMatrix x, DblMatrix alpha) {
        DblMatrix sum = new DblMatrix(0.0);
        DblMatrix term_last = new DblMatrix(-1.0);
        DblMatrix a = alpha;
        DblMatrix term;
        term = new DblMatrix(1.0);
        int iter = 0;
        while (iter < this.maxiter) {
            term = term.divideBy(a);
            sum = sum.plus(term);
            if (DblMatrix.test(DblMatrix.abs(term_last.minus(term)).lt(this.eps))) {
                break;
            } else {
                term_last = term;
                term = x.times(term);
                a = a.plus(1.0);
            }
            iter++;
        }
        if (iter == this.maxiter) {
            System.out.println("SeriesGamma failed to converge.");
        }
        DblMatrix out = alpha.times(DblMatrix.log(x)).minus(x).plus(DblMatrix.log(sum));
        return (out);
    }
}
