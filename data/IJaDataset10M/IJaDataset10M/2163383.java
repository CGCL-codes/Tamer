package org.apache.commons.math3.transform;

import org.apache.commons.math3.analysis.UnivariateFunction;

/**
 * <p>Interface for one-dimensional data sets transformations producing real
 * results.</p>
 * <p>Such transforms include {@link FastSineTransformer sine transform},
 * {@link FastCosineTransformer cosine transform} or {@link
 * FastHadamardTransformer Hadamard transform}. {@link FastFourierTransformer
 * Fourier transform} is of a different kind and does not implement this
 * interface since it produces {@link org.apache.commons.math3.complex.Complex}
 * results instead of real ones.</p>
 *
 * @version $Id: RealTransformer.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 2.0
 */
public interface RealTransformer {

    /**
     * Returns the (forward, inverse) transform of the specified real data set.
     *
     * @param f the real data array to be transformed (signal)
     * @param type the type of transform (forward, inverse) to be performed
     * @return the real transformed array (spectrum)
     */
    double[] transform(double[] f, TransformType type);

    /**
     * Returns the (forward, inverse) transform of the specified real function,
     * sampled on the specified interval.
     *
     * @param f the function to be sampled and transformed
     * @param min the (inclusive) lower bound for the interval
     * @param max the (exclusive) upper bound for the interval
     * @param n the number of sample points
     * @param type the type of transform (forward, inverse) to be performed
     * @return the real transformed array
     * @throws org.apache.commons.math3.exception.NonMonotonicSequenceException
     * if the lower bound is greater than, or equal to the upper bound
     * @throws org.apache.commons.math3.exception.NotStrictlyPositiveException
     * if the number of sample points is negative
     */
    double[] transform(UnivariateFunction f, double min, double max, int n, TransformType type);
}
