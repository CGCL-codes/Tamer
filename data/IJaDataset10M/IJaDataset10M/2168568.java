package org.vizzini.ai.neuralnetwork.layer;

import org.vizzini.ai.neuralnetwork.IActivationFunction;
import org.vizzini.ai.neuralnetwork.function.PassThroughFunction;
import java.util.logging.Logger;

/**
 * Provides an input layer for a neural network.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.3
 * @since    v0.3
 */
public class InputLayer extends AbstractFunctionLayer {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(InputLayer.class.getName());

    /**
     * Construct this object.
     *
     * @since  v0.3
     */
    public InputLayer() {
        super();
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  name       Name.
     * @param  nodeCount  Node count.
     *
     * @since  v0.3
     */
    public InputLayer(String name, int nodeCount) {
        this(name, nodeCount, true);
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  name            Name.
     * @param  nodeCount       Node count.
     * @param  isBiasNodeUsed  Flag indicating if a bias node is used.
     *
     * @since  v0.3
     */
    public InputLayer(String name, int nodeCount, boolean isBiasNodeUsed) {
        super(name, nodeCount, isBiasNodeUsed);
        setActivationFunction(new PassThroughFunction());
    }

    /**
     * @see  org.vizzini.ai.neuralnetwork.layer.AbstractFunctionLayer#setActivationFunction(org.vizzini.ai.neuralnetwork.IActivationFunction)
     */
    @Override
    public void setActivationFunction(IActivationFunction function) {
        if (function == null) {
            throw new IllegalArgumentException("function == null");
        }
        if (!(function instanceof PassThroughFunction)) {
            LOGGER.warning("Setting activation function not permitted: " + function.getClass().getName());
        }
        super.setActivationFunction(function);
    }

    /**
     * @see  org.vizzini.ai.neuralnetwork.layer.AbstractLayer#setInputs(double[])
     */
    @Override
    public void setInputs(double[] inputs) {
        super.setInputs(inputs);
        System.arraycopy(inputs, 0, getOutputs(), 0, inputs.length);
    }
}
