package org.kf.math;

/**
 * Some math utilities related to integer sums
 */
public abstract class Sums {

    /**
	 * Arithmetic sequel sum : computes the sum of a1 + ... + an
	 * where  ai+1=ai+d
	 * 
	 * @param a1 the first
	 * @param an the last
	 * @param n the n
	 * 
	 * @return the double
	 */
    public static double arithmeticSequelSum(double a1, double an, int n) {
        return n * (a1 + an) / 2.0;
    }

    /**
	 * Arithmetic sequel sum : computes the sum of a1 + ... + an
	 * where  ai+1=ai+d
	 * 
	 * @param a1 the first
	 * @param an the last
	 * @param n the n
	 * 
	 * @return the double
	 */
    public static int arithmeticSequelSum(int a1, int an, int n) {
        return n * (a1 + an) / 2;
    }
}
