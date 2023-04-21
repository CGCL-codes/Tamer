package ch.sahits.math;

/**
 * The integration is realised with the fifth Gau�-Legendre-Formula.
 * The values C0, ..., C4 are the sampling points with the weights W0, ..., W2.<br>
 * The integral is aproximated by the wheighted mean of the function values at the sampling
 * points. If the function is sufficient differenciable the desiered double precicion is
 * reached with some function calls (maximum some hundred).
 * 
 * @author Andi Hotz (c) by Sahits.ch 2007
 * @version 1.0
 */
public class Integration {

    private static final double C0 = 1 - Math.sqrt((35 + 2 * Math.sqrt(70)) / 63);

    private static final double C1 = 1 - Math.sqrt((35 - 2 * Math.sqrt(70)) / 63);

    private static final double C3 = 1 + Math.sqrt((35 - 2 * Math.sqrt(70)) / 63);

    private static final double C4 = 1 + Math.sqrt((35 + 2 * Math.sqrt(70)) / 63);

    private static final double W0 = (322 - 13 * Math.sqrt(70)) / 900;

    private static final double W1 = (322 + 13 * Math.sqrt(70)) / 900;

    private static final double W2 = 128.0 / 225.0;

    /**
    * Tollerence for the desired double precision
    */
    public static final double TOL = 4e-15;

    /**
    * maximum of the iterations
    */
    public static final int MAXK = 65536;

    /**
    * function call
    * @param f function
    * @param a begin of the interval
    * @param b end of the intervall
    * @param k
    * @return approximation
    */
    public static double intk(IFunction f, double a, double b, int k) {
        double Q = 0;
        double h = (b - a) / k;
        double h2 = h / 2;
        double x0 = h2 * C0;
        double x1 = h2 * C1;
        double x2 = h2;
        double x3 = h2 * C3;
        double x4 = h2 * C4;
        double x = a;
        for (int i = 0; i < k; i++) {
            Q += W0 * (f.f(x0 + x) + f.f(x4 + x)) + W1 * (f.f(x1 + x) + f.f(x3 + x)) + W2 * f.f(x2 + x);
            x += h;
        }
        Q = Q * h2;
        return Q;
    }

    /**
    * Integrate the function <code>f</code> in the interval [a..b].
    * @param f function
    * @param a begin of the interval
    * @param b end of the intervall
    * @return size of the area under (over) the function f between a and b
    */
    public static double integral(IFunction f, double a, double b) {
        double intval1 = 0, intval2 = 0, intval = 0;
        int k = 1;
        intval1 = intk(f, a, b, k);
        while (k <= MAXK) {
            k *= 2;
            intval2 = intk(f, a, b, k);
            intval = (1024 * intval2 - intval1) / 1023;
            if (Math.abs(1 - intval / intval2) < TOL) return intval;
            intval1 = intval2;
        }
        return intval;
    }
}
