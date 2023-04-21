package edu.ucla.sspace.clustering.seeding;

import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.BitSet;

/**
 * This {@link KMeansSeed} implementation selects data points at random from any
 * given data set to serve as the initial centroid seeds.
 *
 * @author Keith Stevens
 */
public class RandomSeed implements KMeansSeed {

    /**
     * {@inheritDoc}
     */
    public DoubleVector[] chooseSeeds(int numCentroids, Matrix dataPoints) {
        DoubleVector[] centers = new DoubleVector[numCentroids];
        if (numCentroids >= dataPoints.rows()) {
            for (int i = 0; i < dataPoints.rows(); ++i) centers[i] = dataPoints.getRowVector(i);
            for (int i = dataPoints.rows(); i < numCentroids; ++i) centers[i] = new DenseVector(dataPoints.columns());
            return centers;
        }
        BitSet selectedCentroids = Statistics.randomDistribution(numCentroids, dataPoints.rows());
        for (int c = 0, i = selectedCentroids.nextSetBit(0); i >= 0; c++, i = selectedCentroids.nextSetBit(i + 1)) centers[c] = dataPoints.getRowVector(i);
        return centers;
    }
}
