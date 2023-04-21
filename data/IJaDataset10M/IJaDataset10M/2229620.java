package com.marmoush.jann.layer;

import org.jblas.DoubleMatrix;

/**
 * The Class TeachFn.
 */
public abstract class TeachFn {

    /**
     * Calc error.
     *
     * @param output the output
     * @param target the target
     * @return the double matrix
     */
    public static DoubleMatrix calcError(final DoubleMatrix output, final DoubleMatrix target) {
        return target.sub(output);
    }

    /**
     * Delta rule.
     *
     * @param inputTransposed the input transposed
     * @param error the error
     * @param diffOutput the diff output
     * @param learnRate the learn rate
     * @return the double matrix
     */
    public static DoubleMatrix deltaRule(final DoubleMatrix inputTransposed, final DoubleMatrix error, final DoubleMatrix diffOutput, final double learnRate) {
        return error.mul(diffOutput).mmul(inputTransposed).mul(learnRate);
    }

    /**
     * Lyr backpropagate.
     *
     * @param weightTransposed the weight transposed
     * @param error the error
     * @return the double matrix
     */
    public static DoubleMatrix lyrBackpropagate(final DoubleMatrix weightTransposed, final DoubleMatrix error) {
        return weightTransposed.mmul(error);
    }

    /**
     * Simple delta rule.
     *
     * @param inputTransposed the input transposed
     * @param error the error
     * @param learnRate the learn rate
     * @return the double matrix
     */
    public static DoubleMatrix simpleDeltaRule(final DoubleMatrix inputTransposed, final DoubleMatrix error, final double learnRate) {
        return error.mmul(inputTransposed).mul(learnRate);
    }
}
