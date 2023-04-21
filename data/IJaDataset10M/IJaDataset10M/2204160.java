package com.marmoush.jann.layer;

import org.jblas.DoubleMatrix;

/**
 * The Class CheckDimensionFn.
 */
public class CheckDimensionFn {

    /**
     * Checks if is equal.
     *
     * @param matrices the matrices
     * @return true, if is equal
     */
    public static boolean isEqual(final DoubleMatrix... matrices) {
        boolean result = true;
        int length = matrices[0].length;
        for (int i = 1; i < matrices.length; i++) {
            if (matrices[i].length != length) {
                result = false;
                break;
            }
            length = matrices[i].length;
        }
        return result;
    }
}
