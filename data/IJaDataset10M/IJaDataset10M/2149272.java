package org.das2.math;

import java.util.Random;

/**
 *
 * @author Jeremy
 */
public class PoissonDistribution {

    static Fac fac = new Fac();

    private static class Fac {

        static final int FAK_LEN = 1024;

        double[] fac_table;

        boolean initialized = false;

        final double C0 = 0.918938533204672722, C1 = 1. / 12., C3 = -1. / 360.;

        Fac() {
            fac_table = new double[FAK_LEN];
        }

        private void init() {
            double sum = fac_table[0] = 0.;
            for (int i = 1; i < FAK_LEN; i++) {
                sum += Math.log(i);
                fac_table[i] = sum;
            }
            initialized = true;
        }

        public double lnFac(int n) {
            if (n < FAK_LEN) {
                if (n <= 1) {
                    if (n < 0) throw new IllegalArgumentException("Parameter negative in LnFac function");
                    return 0;
                }
                if (!initialized) {
                    init();
                }
                return fac_table[n];
            } else {
                double n1, r;
                n1 = n;
                r = 1. / n1;
                return (n1 + 0.5) * Math.log(n1) - n1 + C0 + r * (C1 + r * r * C3);
            }
        }
    }

    static PoissonInver poissonInver = new PoissonInver();

    private static class PoissonInver {

        double p_L_last = -1;

        double p_f0;

        /**
         * This subfunction generates a random variate with the poisson
         * distribution using inversion by the chop down method (PIN).
         *
         * Execution time grows with L. Gives overflow for L > 80.
         *
         * The value of bound must be adjusted to the maximal value of L.
         */
        private int PoissonInver(double L, Random random) {
            final int bound = 130;
            double r;
            double f;
            int x;
            if (L != p_L_last) {
                p_L_last = L;
                p_f0 = Math.exp(-L);
            }
            while (true) {
                r = random.nextDouble();
                x = 0;
                f = p_f0;
                do {
                    r -= f;
                    if (r <= 0) return x;
                    x++;
                    f *= L;
                    r *= x;
                } while (x <= bound);
            }
        }
    }

    static PoissonRatioUniforms poissonRatioUniforms = new PoissonRatioUniforms();

    /**
     * This subfunction generates a random variate with the poisson
     * distribution using the ratio-of-uniforms rejection method (PRUAt).
     *
     * Execution time does not depend on L, except that it matters whether L
     * is within the range where ln(n!) is tabulated.
     *
     * Reference: E. Stadlober: "The ratio of uniforms approach for generating
     * discrete random variates". Journal of Computational and Applied Mathematics,
     * vol. 31, no. 1, 1990, pp. 181-189.
     */
    static class PoissonRatioUniforms {

        final double SHAT1 = 2.943035529371538573;

        final double SHAT2 = 0.8989161620588987408;

        double p_L_last = -1.0;

        double p_a;

        double p_h;

        double p_g;

        double p_q;

        int p_bound;

        int mode;

        private int PoissonRatioUniforms(double L, Random random) {
            double u;
            double lf;
            double x;
            int k;
            if (p_L_last != L) {
                p_L_last = L;
                p_a = L + 0.5;
                mode = (int) L;
                p_g = Math.log(L);
                p_q = mode * p_g - fac.lnFac(mode);
                p_h = Math.sqrt(SHAT1 * (L + 0.5)) + SHAT2;
                p_bound = (int) (p_a + 6.0 * p_h);
            }
            while (true) {
                u = random.nextDouble();
                if (u == 0) continue;
                x = p_a + p_h * (random.nextDouble() - 0.5) / u;
                if (x < 0 || x >= p_bound) continue;
                k = (int) (x);
                lf = k * p_g - fac.lnFac(k) - p_q;
                if (lf >= u * (4.0 - u) - 3.0) break;
                if (u * (u - lf) > 1.0) continue;
                if (2.0 * Math.log(u) <= lf) break;
            }
            return (k);
        }
    }

    /**
     * This function generates a random variate with the poisson distribution.
     *
     * Uses inversion by chop-down method for L < 17, and ratio-of-uniforms
     * method for L >= 17.
     *
     * For L < 1.E-6 numerical inaccuracy is avoided by direct calculation.
     * For L > 2E9 too big--throws IllegalArgumentException
     */
    public static int poisson(double L, Random random) {
        if (L < 17) {
            if (L < 1.E-6) {
                if (L == 0) return 0;
                if (L < 0) throw new IllegalArgumentException("Parameter negative in poisson function");
                return PoissonLow(L, random);
            } else {
                return poissonInver.PoissonInver(L, random);
            }
        } else {
            if (L > 2.E9) throw new IllegalArgumentException("Parameter too big in poisson function");
            return poissonRatioUniforms.PoissonRatioUniforms(L, random);
        }
    }

    /**
     * This subfunction generates a random variate with the poisson
     * distribution for extremely low values of L.
     *
     * The method is a simple calculation of the probabilities of x = 1
     * and x = 2. Higher values are ignored.
     *
     * The reason for using this method is to avoid the numerical inaccuracies
     * in other methods.
     */
    private static int PoissonLow(double L, Random random) {
        double d, r;
        d = Math.sqrt(L);
        if (random.nextDouble() >= d) return 0;
        r = random.nextDouble() * d;
        if (r > L * (1. - L)) return 0;
        if (r > 0.5 * L * L * (1. - L)) return 1;
        return 2;
    }
}
