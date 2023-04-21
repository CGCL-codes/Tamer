package org.ode4j.ode.internal;

public class FastDot extends Misc {

    public static double dDot(final double[] a, final double[] b, int n) {
        return dDot(a, 0, b, n);
    }

    /**
	 * 
	 * @param a
	 * @param aOfs
	 * @param b
	 * @param n
	 * @return xxx
	 * @deprecated use other method
	 */
    public static double dDot(final double[] a, int aOfs, final double[] b, int n) {
        double p0, q0, m0, p1, q1, m1, sum;
        int aPos = aOfs, bPos = 0;
        sum = 0;
        n -= 2;
        while (n >= 0) {
            p0 = a[aPos + 0];
            q0 = b[bPos + 0];
            m0 = p0 * q0;
            p1 = a[aPos + 1];
            q1 = b[bPos + 1];
            m1 = p1 * q1;
            sum += m0;
            sum += m1;
            aPos += 2;
            bPos += 2;
            n -= 2;
        }
        n += 2;
        while (n > 0) {
            sum += a[aPos] * b[bPos];
            aPos++;
            bPos++;
            n--;
        }
        return sum;
    }

    public static double dDot(final double[] a, int aOfs, final double[] b, int bOfs, int n) {
        double p0, q0, m0, p1, q1, m1, sum;
        int aPos = aOfs, bPos = bOfs;
        sum = 0;
        n -= 2;
        while (n >= 0) {
            p0 = a[aPos + 0];
            q0 = b[bPos + 0];
            m0 = p0 * q0;
            p1 = a[aPos + 1];
            q1 = b[bPos + 1];
            m1 = p1 * q1;
            sum += m0;
            sum += m1;
            aPos += 2;
            bPos += 2;
            n -= 2;
        }
        n += 2;
        while (n > 0) {
            sum += a[aPos] * b[bPos];
            aPos++;
            bPos++;
            n--;
        }
        return sum;
    }
}
