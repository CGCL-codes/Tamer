package org.encog.engine.network.train.prop;

import org.encog.engine.data.EngineDataSet;
import org.encog.engine.network.flat.FlatNetwork;

/**
 * Train a flat network using RPROP.
 */
public class TrainFlatNetworkResilient extends TrainFlatNetworkProp {

    /**
	 * The update values, for the weights and thresholds.
	 */
    private final double[] updateValues;

    /**
	 * The zero tolerance.
	 */
    private final double zeroTolerance;

    /**
	 * The maximum step value for rprop.
	 */
    private final double maxStep;

    /**
	 * Construct a resilient trainer for flat networks.
	 * 
	 * @param network
	 *            The network to train.
	 * @param training
	 *            The training data to use.
	 * @param zeroTolerance
	 *            How close a number should be to zero to be counted as zero.
	 * @param initialUpdate
	 *            The initial update value.
	 * @param maxStep
	 *            The maximum step value.
	 */
    public TrainFlatNetworkResilient(final FlatNetwork network, final EngineDataSet training, final double zeroTolerance, final double initialUpdate, final double maxStep) {
        super(network, training);
        this.updateValues = new double[network.getWeights().length];
        this.zeroTolerance = zeroTolerance;
        this.maxStep = maxStep;
        for (int i = 0; i < this.updateValues.length; i++) {
            this.updateValues[i] = initialUpdate;
        }
    }

    /**
	 * Tran a network using RPROP.
	 * @param flat
	 *            The network to train.
	 * @param trainingSet
	 *            The training data to use.
	 */
    public TrainFlatNetworkResilient(final FlatNetwork flat, final EngineDataSet trainingSet) {
        this(flat, trainingSet, RPROPConst.DEFAULT_ZERO_TOLERANCE, RPROPConst.DEFAULT_INITIAL_UPDATE, RPROPConst.DEFAULT_MAX_STEP);
    }

    /**
	 * Determine the sign of the value.
	 * 
	 * @param value
	 *            The value to check.
	 * @return -1 if less than zero, 1 if greater, or 0 if zero.
	 */
    private int sign(final double value) {
        if (Math.abs(value) < this.zeroTolerance) {
            return 0;
        } else if (value > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
	 * Calculate the amount to change the weight by.
	 * 
	 * @param gradients
	 *            The gradients.
	 * @param lastGradient
	 *            The last gradients.
	 * @param index
	 *            The index to update.
	 * @return The amount to change the weight by.
	 */
    @Override
    public double updateWeight(final double[] gradients, final double[] lastGradient, final int index) {
        final int change = sign(gradients[index] * lastGradient[index]);
        double weightChange = 0;
        if (change > 0) {
            double delta = this.updateValues[index] * RPROPConst.POSITIVE_ETA;
            delta = Math.min(delta, this.maxStep);
            weightChange = sign(gradients[index]) * delta;
            this.updateValues[index] = delta;
            lastGradient[index] = gradients[index];
        } else if (change < 0) {
            double delta = this.updateValues[index] * RPROPConst.NEGATIVE_ETA;
            delta = Math.max(delta, RPROPConst.DELTA_MIN);
            this.updateValues[index] = delta;
            lastGradient[index] = 0;
        } else if (change == 0) {
            final double delta = this.updateValues[index];
            weightChange = sign(gradients[index]) * delta;
            lastGradient[index] = gradients[index];
        }
        return weightChange;
    }

    /**
	 * @return The RPROP update values.
	 */
    public double[] getUpdateValues() {
        return updateValues;
    }
}
