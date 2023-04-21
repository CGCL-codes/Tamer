package de.ibis.permoto.solver.sim.tech.simEngine1.math;

/** This class calculates the CDF and ICDF of the F(Fisher) distribution
 *
 * @author  Federico Granata
 */
public class FDistribution {

    private double num;

    private double den;

    /** Creates a new instance of FDistribution
	 *  @param num numerator degrees of freeedom
	 *  @param den denominator degrees of freedom
	 */
    public FDistribution(int num, int den) {
        if ((num <= 0) || (den <= 0)) throw new IllegalArgumentException("the degrees of freedom of a Fisher distribution must be > 0");
        this.num = (double) num;
        this.den = (double) den;
    }

    /** sets the degrees of freedom of the distribution
	 *  @param num numerator degrees of freeedom
	 *  @param den denominator degrees of freedom
	 */
    public void setParameters(int num, int den) {
        if ((num <= 0) || (den <= 0)) throw new IllegalArgumentException("the degrees of freedom of a Fisher distribution must be > 0");
        this.num = (double) num;
        this.den = (double) den;
    }

    /** CDF
	 *  ALGORITHM AS 63 APPL. STATIST. VOL.32, NO.1
	 *   Computes P(f>x)
	 *  @param f quantile
	 *  @param num numerator degrees of freeedom
	 *  @param den denominator degrees of freedom
	 */
    public static double CDF(double f, int num, int den) {
        if ((num <= 0) || (den <= 0)) throw new IllegalArgumentException("the degrees of freedom of a Fisher distribution must be > 0");
        double df1 = (double) num;
        double df2 = (double) den;
        return BetaFun.betaInv(df1 * f / (df1 * f + df2), 0.5 * df1, 0.5 * df2);
    }

    /** CDF
	 *  ALGORITHM AS 63 APPL. STATIST. VOL.32, NO.1
	 *   Computes P(f>x)
	 *  @param f quantile
	 */
    public double CDF(double f) {
        return BetaFun.betaInv(num * f / (num * f + den), 0.5 * num, 0.5 * den);
    }

    /** get the quantile of a probability f
	 * computes f, given P
	 * P(f>x)
	 * it uses the false position method for solving the problem
	 */
    public double ICDF(double p) {
        double precision = 0.0001;
        double max = p + precision;
        double min = p - precision;
        double z1 = 1;
        double z2 = 10;
        double zs;
        double sol;
        if (p <= 0 || p > 1) throw new IllegalArgumentException("probability p must be 0<p<1");
        while (CDF(z2) < p) z2 *= 2;
        while (CDF(z1) > p) z1 /= 2;
        zs = z2 / 2;
        sol = CDF(zs);
        while (sol > max || sol < min) {
            zs = z1 + (z2 - z1) * (-(p - CDF(z1))) / ((p - CDF(z2)) - (p - CDF(z1)));
            if ((p - CDF(zs)) * (p - CDF(z1)) > 0) z1 = zs; else z2 = zs;
            sol = CDF(zs);
        }
        return zs;
    }

    public static double ICDF(double p, int num, int den) {
        double precision = 0.0001;
        double max = p + precision;
        double min = p - precision;
        double z1 = 1;
        double z2 = 10;
        double zs;
        double sol;
        if (p <= 0 || p >= 1) throw new IllegalArgumentException("probability p must be 0<p<1");
        while (CDF(z2, num, den) < p) z2 *= 2;
        while (CDF(z1, num, den) > p) z1 /= 2;
        zs = z2 / 2;
        sol = CDF(zs, num, den);
        while (sol > max || sol < min) {
            zs = z1 + (z2 - z1) * (-(p - CDF(z1, num, den))) / ((p - CDF(z2, num, den)) - (p - CDF(z1, num, den)));
            if ((p - CDF(zs, num, den)) * (p - CDF(z1, num, den)) > 0) z1 = zs; else z2 = zs;
            sol = CDF(zs, num, den);
        }
        return zs;
    }

    /** test function */
    public static void test() {
        FDistribution fd = new FDistribution(4, 4);
        System.out.println(fd.ICDF(0.7347146269717488));
    }
}
