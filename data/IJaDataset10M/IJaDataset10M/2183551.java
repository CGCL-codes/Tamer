package edu.emory.mathcs.csparsej.tdouble;

/**
 * Compute Householder reflection.
 * 
 * @author Piotr Wendykier (piotr.wendykier@gmail.com)
 * 
 */
public class Dcs_house {

    /**
     * Compute a Householder reflection, overwrite x with v, where
     * (I-beta*v*v')*x = s*e1. See Algo 5.1.1, Golub & Van Loan, 3rd ed.
     * 
     * @param x
     *            x on output, v on input
     * @param x_offset
     *            the index of the first element in array x
     * @param beta
     *            scalar beta
     * @param n
     *            the length of x
     * @return norm2(x), -1 on error
     */
    public static double cs_house(double[] x, int x_offset, double[] beta, int n) {
        double s, sigma = 0;
        int i;
        if (x == null || beta == null) return (-1);
        for (i = 1; i < n; i++) sigma += x[x_offset + i] * x[x_offset + i];
        if (sigma == 0) {
            s = Math.abs(x[x_offset + 0]);
            beta[0] = (x[x_offset + 0] <= 0) ? 2.0 : 0.0;
            x[x_offset + 0] = 1;
        } else {
            s = Math.sqrt(x[x_offset + 0] * x[x_offset + 0] + sigma);
            x[x_offset + 0] = (x[x_offset + 0] <= 0) ? (x[x_offset + 0] - s) : (-sigma / (x[x_offset + 0] + s));
            beta[0] = -1.0 / (s * x[x_offset + 0]);
        }
        return (s);
    }
}
