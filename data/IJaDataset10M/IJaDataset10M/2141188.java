package org.jskat.ai.nn.util;

import java.util.ArrayList;
import java.util.List;
import org.jskat.ai.nn.util.Neuron.ActivationFunction;

/**
 * Layer of Neurons for the NeuralNetwork
 */
abstract class Layer {

    /**
	 * All Neurons of the Layer
	 */
    protected List<Neuron> neurons = new ArrayList<Neuron>();

    /**
	 * Constructor
	 * 
	 * @param numberOfNeurons
	 *            Number of Neurons
	 */
    Layer(int numberOfNeurons) {
        for (int i = 0; i < numberOfNeurons; i++) {
            this.neurons.add(createNeuron(ActivationFunction.TANH));
        }
    }

    /**
	 * Creates a neuron for the layer
	 * 
	 * @param activFnct
	 *            Activation function
	 * @return Neuron
	 */
    abstract Neuron createNeuron(ActivationFunction activFnct);

    /**
	 * Gets all Neurons of the Layer
	 * 
	 * @return All Neurons of the Layer
	 */
    List<Neuron> getNeurons() {
        return this.neurons;
    }
}
