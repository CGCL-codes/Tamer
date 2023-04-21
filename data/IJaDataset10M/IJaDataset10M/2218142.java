package org.encog.neural.flat.train.prop;

import org.encog.ml.data.MLDataSet;
import org.encog.neural.flat.FlatNetwork;
import org.encog.neural.networks.training.TrainingError;

/**
 * Train a flat network using RPROP.
 */
public class TrainFlatNetworkResilient extends TrainFlatNetworkProp {

    /**
	 * The update values, for the weights and thresholds.
	 */
    private final double[] updateValues;

    private final double[] lastDelta;

    /**
	 * The zero tolerance.
	 */
    private final double zeroTolerance;

    /**
	 * The maximum step value for rprop.
	 */
    private final double maxStep;

    private RPROPType rpropType = RPROPType.RPROPp;

    private double[] lastWeightChange;

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
    public TrainFlatNetworkResilient(final FlatNetwork network, final MLDataSet training, final double zeroTolerance, final double initialUpdate, final double maxStep) {
        super(network, training);
        this.updateValues = new double[network.getWeights().length];
        this.lastDelta = new double[network.getWeights().length];
        this.lastWeightChange = new double[network.getWeights().length];
        this.zeroTolerance = zeroTolerance;
        this.maxStep = maxStep;
        for (int i = 0; i < this.updateValues.length; i++) {
            this.updateValues[i] = initialUpdate;
            this.lastDelta[i] = 0;
        }
    }

    /**
	 * Tran a network using RPROP.
	 * @param flat
	 *            The network to train.
	 * @param trainingSet
	 *            The training data to use.
	 */
    public TrainFlatNetworkResilient(final FlatNetwork flat, final MLDataSet trainingSet) {
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
        double weightChange = 0;
        switch(this.rpropType) {
            case RPROPp:
                weightChange = updateWeightPlus(gradients, lastGradient, index);
                break;
            case RPROPm:
                weightChange = updateWeightMinus(gradients, lastGradient, index);
                break;
            case iRPROPp:
                weightChange = updateiWeightPlus(gradients, lastGradient, index);
                break;
            case iRPROPm:
                weightChange = updateiWeightMinus(gradients, lastGradient, index);
                break;
            default:
                throw new TrainingError("Unknown RPROP type: " + this.rpropType);
        }
        this.lastWeightChange[index] = weightChange;
        return weightChange;
    }

    public double updateWeightPlus(final double[] gradients, final double[] lastGradient, final int index) {
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
            weightChange = -this.lastWeightChange[index];
            lastGradient[index] = 0;
        } else if (change == 0) {
            final double delta = this.updateValues[index];
            weightChange = sign(gradients[index]) * delta;
            lastGradient[index] = gradients[index];
        }
        return weightChange;
    }

    public double updateWeightMinus(final double[] gradients, final double[] lastGradient, final int index) {
        final int change = sign(gradients[index] * lastGradient[index]);
        double weightChange = 0;
        double delta;
        if (change > 0) {
            delta = this.lastDelta[index] * RPROPConst.POSITIVE_ETA;
            delta = Math.min(delta, this.maxStep);
        } else {
            delta = this.lastDelta[index] * RPROPConst.NEGATIVE_ETA;
            delta = Math.max(delta, RPROPConst.DELTA_MIN);
        }
        lastGradient[index] = gradients[index];
        weightChange = sign(gradients[index]) * delta;
        this.lastDelta[index] = delta;
        return weightChange;
    }

    public double updateiWeightPlus(final double[] gradients, final double[] lastGradient, final int index) {
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
            if (this.currentError > this.lastError) {
                weightChange = -this.lastWeightChange[index];
            }
            lastGradient[index] = 0;
        } else if (change == 0) {
            final double delta = this.updateValues[index];
            weightChange = sign(gradients[index]) * delta;
            lastGradient[index] = gradients[index];
        }
        return weightChange;
    }

    public double updateiWeightMinus(final double[] gradients, final double[] lastGradient, final int index) {
        final int change = sign(gradients[index] * lastGradient[index]);
        double weightChange = 0;
        double delta;
        if (change > 0) {
            delta = this.lastDelta[index] * RPROPConst.POSITIVE_ETA;
            delta = Math.min(delta, this.maxStep);
        } else {
            delta = this.lastDelta[index] * RPROPConst.NEGATIVE_ETA;
            delta = Math.max(delta, RPROPConst.DELTA_MIN);
            lastGradient[index] = 0;
        }
        lastGradient[index] = gradients[index];
        weightChange = sign(gradients[index]) * delta;
        this.lastDelta[index] = delta;
        return weightChange;
    }

    /**
	 * @return The RPROP update values.
	 */
    public double[] getUpdateValues() {
        return updateValues;
    }

    /**
	 * @return the rpropType
	 */
    public RPROPType getRpropType() {
        return rpropType;
    }

    /**
	 * @param rpropType the rpropType to set
	 */
    public void setRpropType(RPROPType rpropType) {
        this.rpropType = rpropType;
    }

    /**
	 * Perform training method specific init.
	 */
    public void initOthers() {
    }
}
