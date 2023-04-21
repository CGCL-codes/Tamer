package gov.sns.tools.statistics;

import java.lang.reflect.Array;
import gov.sns.tools.ArrayTool;
import gov.sns.tools.ArrayMath;

/**
 * Manage statistics on a multi-dimensional array.
 * @author  tap
 */
public class ArrayStatistics {

    protected int[] dimensions;

    protected int population;

    protected Object mean, meanSquare;

    /** 
     * Creates a new instance of ArrayStatistics
     * @param dim    an array of sizes (one size for each dimension of the array)
     */
    public ArrayStatistics(int[] dim) {
        population = 0;
        dimensions = dim;
        mean = Array.newInstance(Double.TYPE, dimensions);
        meanSquare = Array.newInstance(Double.TYPE, dimensions);
    }

    /** Convenience constructor for handling a one dimensional vector. */
    public ArrayStatistics(int dim) {
        this(new int[] { dim });
    }

    /** Convenience constructor for handling a two dimensional Matrix. */
    public ArrayStatistics(int dim1, int dim2) {
        this(new int[] { dim1, dim2 });
    }

    /** Get the mean array */
    public Object getMean() {
        return mean;
    }

    /** Get the variance array */
    public Object variance() {
        return ArrayMath.subtract(meanSquare, ArrayMath.elementProduct(mean, mean, dimensions), dimensions);
    }

    /** Sample variance */
    public Object sampleVariance() {
        double scale = ((double) population) / (population - 1);
        return ArrayMath.multiply(variance(), scale, dimensions);
    }

    /** Get the variance of the mean */
    public Object varianceOfMean() {
        return ArrayMath.multiply(variance(), 1.0D / population, dimensions);
    }

    /** Sample variance of mean */
    public Object sampleVarianceOfMean() {
        return ArrayMath.multiply(sampleVariance(), 1.0D / population, dimensions);
    }

    /** Standard deviation */
    public Object standardDeviation() {
        return ArrayMath.elementSquareRootAbs(variance(), dimensions);
    }

    /** Sample standard deviation */
    public Object sampleStandardDeviation() {
        return ArrayMath.elementSquareRootAbs(sampleVariance(), dimensions);
    }

    /** Standard deviation of the mean */
    public Object standardDeviationOfMean() {
        return ArrayMath.elementSquareRootAbs(varianceOfMean(), dimensions);
    }

    /** Sample standard deviation of the mean */
    public Object sampleStandardDeviationOfMean() {
        return ArrayMath.elementSquareRootAbs(sampleVarianceOfMean(), dimensions);
    }

    /** 
     * Add a sample array to average into the population.
     * The dimensions of the sample array must match the specified dimensions.
     */
    public void addSample(Object sample) {
        double weight = 1.0D / ++population;
        int[] indices = (int[]) Array.newInstance(Integer.TYPE, dimensions.length);
        while (true) {
            double meanValue = ArrayTool.getDouble(mean, indices);
            double meanSquareValue = ArrayTool.getDouble(meanSquare, indices);
            double sampleValue = ArrayTool.getDouble(sample, indices);
            meanValue = weight * sampleValue + (1 - weight) * meanValue;
            meanSquareValue = weight * sampleValue * sampleValue + (1 - weight) * meanSquareValue;
            ArrayTool.setDouble(mean, indices, meanValue);
            ArrayTool.setDouble(meanSquare, indices, meanSquareValue);
            if (!ArrayTool.increment(indices, dimensions)) break;
        }
    }
}
