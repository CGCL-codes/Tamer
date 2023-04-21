package verjinxer.util;

import static java.lang.Math.*;

/**
 * utility class with static mathematical routines
 * @author Sven Rahmann
 */
public final class MathUtils {

    private MathUtils() {
    }

    private static final double[] lngammacoeff = { 76.18009172947146, -86.50532032941677, 24.01409824083091, -1.231739572450155, 0.1208650973866179E-02, -0.5395239384953E-05 };

    /** Returns the logarithm of the Gamma function of argument xx.
   *  Throws an ArithmeticException of xx&lt;=0
   * @param xx the argument at which to compute the lngamma function
   * @return ln(gamma(xx))
   */
    public static final double lngamma(final double xx) {
        double x, y, tmp, ser;
        if (xx <= 0) throw new ArithmeticException("lngamma: Argument " + xx + " must be positive");
        y = x = xx;
        tmp = x + 5.5;
        tmp -= (x + 0.5) * log(tmp);
        ser = 1.000000000190015;
        for (int j = 0; j <= 5; j++) ser += lngammacoeff[j] / ++y;
        return (-tmp + log(2.5066282746310005 * ser / x));
    }

    /** returns the logarithm of x! (x factorial)
   * @param x  the argument
   * @return log(x!)
   */
    public static final double lnfactorial(final double x) {
        return (lngamma(x + 1));
    }

    /** returns (n choose k), the biomial coefficient
   * @param n  number of things from which we can choose
   * @param k  number of things we can choose
   * @return number of combinations (n choose k), as a double
   */
    public static final double nchoosek(final double n, final double k) {
        return exp((lngamma(n + 1) - lngamma(k + 1) - lngamma(n - k + 1)));
    }

    /** returns (n choose k), the biomial coefficient
   * @param n  number of things from which we can choose
   * @param k  number of things we can choose
   * @return number of combinations (n choose k), as a long integer
   */
    public static final long nchoosek(final long n, final long k) {
        return (long) xround(exp((lngamma(n + 1) - lngamma(k + 1) - lngamma(n - k + 1))));
    }

    /** rounds argument x to granularity eps
   * @param x   the argument
   * @param eps the rounding granularity
   * @return  x rounded to granularity eps
   */
    public static final double xround(final double x, final double eps) {
        double y = eps * floor(x / eps + 0.5);
        return (eps * floor(y / eps + 0.5));
    }

    /** rounds argument x to the nearest integer
   * @param x  the argument
   * @return   the argument rounded to the nearest integer
   */
    public static final double xround(final double x) {
        return xround(x, 1);
    }

    /** computes n-th Fibonacci number F(n), where F(0)=F(1)=1,
   * and F(n) = F(n-1)+F(n-2) for n>=2.
   *@param n
   *@return F(n)
   */
    public static final long fib(int n) {
        if (n < 0) throw new IllegalArgumentException("fib: parameter n must be nonnegative");
        if (n == 0 || n == 1) return 1L;
        long f2 = 1, f1 = 1, f = 1;
        for (int i = 2; i <= n; i++) {
            f = f1 + f2;
            f2 = f1;
            f1 = f;
        }
        return f;
    }

    /** finds largest n such that the n-th Fibonacci number F(n)
   * is smaller or equal to a given number fmax
   * @param fmax  the upper bound on F(n)
   * @return largest n such that F(n)<=fmax
   */
    public static final int fibFind(long fmax) {
        if (fmax <= 0) return -1;
        if (fmax == 1) return 1;
        long f2 = 1, f1 = 1, f = 2;
        int n;
        for (n = 2; f <= fmax; n++) {
            f = f1 + f2;
            f2 = f1;
            f1 = f;
        }
        return n - 2;
    }

    /** computes n-th Fibonacci string FS(n), where FS(0)="a",
   *  FS(1)="b", and FS(n) = FS(n-1)+FS(n-2) for n>=2.
   *@param n
   *@return FS(n)
   */
    public static final String fibString(int n) {
        if (n < 0) throw new IllegalArgumentException("fibString: parameter n must be nonnegative");
        if (n == 0) return "b";
        if (n == 1) return "a";
        String s2 = "b", s1 = "a", s = null;
        for (int i = 2; i <= n; i++) {
            s = s1 + s2;
            s2 = s1;
            s1 = s;
        }
        return s;
    }

    /**
    * @param value
    * @return Returns the base 2 logarithm of a double value.
    */
    public static double log2(double value) {
        return Math.log10(value) / Math.log10(2);
    }
}
