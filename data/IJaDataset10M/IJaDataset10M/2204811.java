package org.ascape.util;

import java.io.Serializable;

/**
 * A one-dimensional function.
 *
 * @author Miles Parker
 * @version 1.0
 * @since 1.0
 */
public abstract class Function implements Serializable, Cloneable {

    /**
     * Tau "magic" number. ~1.61803
     */
    public static final double tau = (1 + Math.sqrt(5)) / 2;

    /**
     * Large end of golden section. ~.61803
     */
    public static final double sectLarge = tau - 1;

    /**
     * Small end of golden section. ~.38197
     */
    public static final double sectSmall = 1 - sectLarge;

    /**
     * Desired resolution of maximization.
     */
    public static final double resolution = 0.01;

    /**
     * The first measurement of the current interval.
     * For minor performance reasons, this and other measurements are not initialized in the
     * body of the maximization functions. This can easily be changed if desired.
     * Also, please note that this class is _not_ thread safe; in order to make it
     * so, simply initialize the following variables within a contructor (or in the method
     * body), make them non-static, and synchronize the methods as appropriate.
     */
    private static double x1 = 0.0;

    /**
     * The second measurement of the current interval.
     */
    private static double x2 = 0.0;

    /**
     * The third measurement of the current interval.
     */
    private static double x3 = 0.0;

    /**
     * The fourth measurement of the current interval.
     */
    private static double x4 = 0.0;

    /**
     * The result value for the second measurement.
     */
    private static double f2 = 0.0;

    /**
     * The result value for the third measurement.
     */
    private static double f3 = 0.0;

    /**
     * The X axis gap between the first and second measurements
     */
    private double gap1 = 0;

    /**
     * The X axis gap between the second and third measurements
     * (after one measurement has been dropped, leaving three total.)
     */
    private double gap2 = 0;

    /**
     * Maximize the output of this function, assuming function is unimodal,
     * using a golden section search strategy.
     * See Press, Flannery, Teukolsky, Vetterling  _Numerical Recipes in *_ 10.1
     * for general guidelines, but not for specific implementation.
     * @return the optimal input variable
     */
    public double maximize() {
        x1 = 0.0;
        x2 = sectSmall;
        x3 = sectLarge;
        x4 = 1.0;
        double gap = 1.0;
        while (gap > resolution) {
            f2 = solveFor(x2);
            f3 = solveFor(x3);
            if (f2 < f3) {
                gap1 = x3 - x2;
                gap2 = x4 - x3;
                x1 = x2;
                if (gap1 > gap2) {
                    x2 = x3 - gap1 * sectSmall;
                    f2 = solveFor(x2);
                    gap = gap1;
                } else {
                    x2 = x3;
                    x3 = x2 + gap2 * sectSmall;
                    f2 = f3;
                    f3 = solveFor(x2);
                    gap = gap2;
                }
            } else {
                gap1 = x2 - x1;
                gap2 = x3 - x2;
                x4 = x3;
                if (gap1 > gap2) {
                    x3 = x2;
                    x2 = x3 - gap1 * sectSmall;
                    f2 = solveFor(x2);
                    gap = gap1;
                } else {
                    x3 = x2 + gap2 * sectSmall;
                    f2 = f3;
                    f3 = solveFor(x2);
                    gap = gap2;
                }
            }
        }
        return f2 > f3 ? x2 : x3;
    }

    /**
     * Solve this (single-variable) function. Override to define your own function.
     * @param x the variable input parameter
     * @return the output value
     */
    public double solveFor(double x) {
        return x;
    }

    /**
     * Clones this function.
     */
    public Object clone() {
        try {
            Function clone = (Function) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
