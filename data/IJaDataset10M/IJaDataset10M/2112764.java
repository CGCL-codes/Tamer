package org.neuroph.nnet.comp;

import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;

/**
 * Provides behaviour for neurons with threshold.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class ThresholdNeuron extends Neuron {

    /**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Threshold value for this neuron
	 */
    protected double thresh = 0;

    /**
	 * Creates a neuron with threshold behaviour, and with the specified input
	 * and transfer functions.
	 * 
	 * @param inputFunction
	 *            input function for this neuron
	 * @param transferFunction
	 *            transfer function for this neuron
	 */
    public ThresholdNeuron(InputFunction inputFunction, TransferFunction transferFunction) {
        this.inputFunction = inputFunction;
        this.transferFunction = transferFunction;
        this.thresh = Math.random();
    }

    /**
	 * Calculates neuron's output
	 */
    @Override
    public void calculate() {
        if (this.hasInputConnections()) {
            this.netInput = this.inputFunction.getOutput(this.inputConnections);
        }
        this.output = this.transferFunction.getOutput(this.netInput - this.thresh);
    }

    /**
	 * Returns threshold value for this neuron
	 * @return threshold value for this neuron
	 */
    public double getThresh() {
        return thresh;
    }

    /**
	 * Sets threshold value for this neuron
	 * @param thresh threshold value for this neuron
	 */
    public void setThresh(double thresh) {
        this.thresh = thresh;
    }
}
