package org.renjin.primitives.random;

import org.renjin.eval.Context;
import org.renjin.sexp.DoubleVector;

public class Geometric {

    public static double rgeom(Context.Globals context, double p) {
        if (p <= 0 || p > 1) {
            return (Double.NaN);
        }
        return Poisson.rpois(context, Exponantial.exp_rand(context) * ((1 - p) / p));
    }

    public static double pgeom(double x, double p, boolean lower_tail, boolean log_p) {
        if (DoubleVector.isNaN(x) || DoubleVector.isNaN(p)) {
            return x + p;
        }
        if (p <= 0 || p > 1) {
            return DoubleVector.NaN;
        }
        if (x < 0.) {
            return SignRank.R_DT_0(lower_tail, log_p);
        }
        if (!DoubleVector.isFinite(x)) {
            return SignRank.R_DT_1(lower_tail, log_p);
        }
        x = Math.floor(x + 1e-7);
        if (p == 1.) {
            x = lower_tail ? 1 : 0;
            return log_p ? Math.log(x) : x;
        }
        x = Math.log1p(-p) * (x + 1);
        if (log_p) {
            return SignRank.R_DT_Clog(x, lower_tail, log_p);
        } else {
            return lower_tail ? -Math.expm1(x) : Math.exp(x);
        }
    }

    public static double dgeom(double x, double p, boolean give_log) {
        double prob;
        if (DoubleVector.isNaN(x) || DoubleVector.isNaN(p)) {
            return x + p;
        }
        if (p <= 0 || p > 1) {
            return DoubleVector.NaN;
        }
        if (SignRank.R_D_nonint(x, true, give_log)) {
            return SignRank.R_D__0(true, give_log);
        }
        if (x < 0 || !DoubleVector.isFinite(x) || p == 0) {
            return SignRank.R_D__0(true, give_log);
        }
        x = SignRank.R_D_forceint(x);
        prob = Binom.dbinom_raw(0., x, p, 1 - p, give_log);
        return ((give_log) ? Math.log(p) + prob : p * prob);
    }

    public static double qgeom(double p, double prob, boolean lower_tail, boolean log_p) {
        if (prob <= 0 || prob > 1) {
            return DoubleVector.NaN;
        }
        if (log_p) {
            if (p > 0) {
                return DoubleVector.NaN;
            }
            if (p == 0) {
                return lower_tail ? Double.POSITIVE_INFINITY : 0;
            }
            if (p == Double.NEGATIVE_INFINITY) ;
            return lower_tail ? 0 : Double.POSITIVE_INFINITY;
        } else {
            if (p < 0 || p > 1) {
                return DoubleVector.NaN;
            }
            if (p == 0) {
                return lower_tail ? 0 : Double.POSITIVE_INFINITY;
            }
            if (p == 1) {
                return lower_tail ? Double.POSITIVE_INFINITY : 0;
            }
        }
        if (DoubleVector.isNaN(p) || DoubleVector.isNaN(prob)) {
            return p + prob;
        }
        if (prob == 1) {
            return (0);
        }
        return Math.ceil(SignRank.R_DT_Clog(p, lower_tail, log_p) / Math.log1p(-prob) - 1 - 1e-7);
    }
}
