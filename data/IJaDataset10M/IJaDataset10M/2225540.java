package org.opensourcephysics.numerics;

import java.util.ArrayList;

/**
 * Calculates Hermite polynomials.
 *
 * @author W. Christian
 * @version 1.0
 */
public class Hermite {

    static final ArrayList hermiteList;

    static final Polynomial twoX = new Polynomial(new double[] { 0, 2.0 });

    private Hermite() {
    }

    /**
   * Gets the n-th Hermite polynomial. If it has already been calculated
   * it just returns it from the list. If we have not calculated it uses
   * the recursion relationship to construct the polynomial based on the prior
   * polynomials.
    */
    public static synchronized Polynomial getPolynomial(int n) {
        if (n < hermiteList.size()) {
            return (Polynomial) hermiteList.get(n);
        }
        Polynomial p1 = getPolynomial(n - 1).multiply(twoX);
        Polynomial p2 = getPolynomial(n - 2).multiply(2 * (n - 1));
        Polynomial p = p1.subtract(p2);
        hermiteList.add(p);
        return p;
    }

    /**
   * Evaluates the n-th Hermite polynomial at x.
   *
   * @return the value of the function
   */
    public static double evaluate(int n, double x) {
        return getPolynomial(n).evaluate(x);
    }

    static {
        hermiteList = new ArrayList();
        Polynomial p = new Polynomial(new double[] { 1.0 });
        hermiteList.add(p);
        p = new Polynomial(new double[] { 0, 2.0 });
        hermiteList.add(p);
    }
}
