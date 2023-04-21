package org.apache.commons.math.stat.descriptive.moment;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math.DimensionMismatchException;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

/**
 * Returns the covariance matrix of the available vectors.
 * @since 1.2
 * @version $Revision: 728186 $ $Date: 2008-12-20 01:03:13 +0100 (Sa, 20 Dez 2008) $
 */
public class VectorialCovariance implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = 4118372414238930270L;

    /** Sums for each component. */
    private double[] sums;

    /** Sums of products for each component. */
    private double[] productsSums;

    /** Indicator for bias correction. */
    private boolean isBiasCorrected;

    /** Number of vectors in the sample. */
    private long n;

    /** Constructs a VectorialCovariance.
     * @param dimension vectors dimension
     * @param isBiasCorrected if true, computed the unbiased sample covariance,
     * otherwise computes the biased population covariance
     */
    public VectorialCovariance(int dimension, boolean isBiasCorrected) {
        sums = new double[dimension];
        productsSums = new double[dimension * (dimension + 1) / 2];
        n = 0;
        this.isBiasCorrected = isBiasCorrected;
    }

    /**
     * Add a new vector to the sample.
     * @param v vector to add
     * @exception DimensionMismatchException if the vector does not have the right dimension
     */
    public void increment(double[] v) throws DimensionMismatchException {
        if (v.length != sums.length) {
            throw new DimensionMismatchException(v.length, sums.length);
        }
        int k = 0;
        for (int i = 0; i < v.length; ++i) {
            sums[i] += v[i];
            for (int j = 0; j <= i; ++j) {
                productsSums[k++] += v[i] * v[j];
            }
        }
        n++;
    }

    /**
     * Get the covariance matrix.
     * @return covariance matrix
     */
    public RealMatrix getResult() {
        int dimension = sums.length;
        RealMatrix result = MatrixUtils.createRealMatrix(dimension, dimension);
        if (n > 1) {
            double c = 1.0 / (n * (isBiasCorrected ? (n - 1) : n));
            int k = 0;
            for (int i = 0; i < dimension; ++i) {
                for (int j = 0; j <= i; ++j) {
                    double e = c * (n * productsSums[k++] - sums[i] * sums[j]);
                    result.setEntry(i, j, e);
                    result.setEntry(j, i, e);
                }
            }
        }
        return result;
    }

    /**
     * Get the number of vectors in the sample.
     * @return number of vectors in the sample
     */
    public long getN() {
        return n;
    }

    /**
     * Clears the internal state of the Statistic
     */
    public void clear() {
        n = 0;
        Arrays.fill(sums, 0.0);
        Arrays.fill(productsSums, 0.0);
    }
}
