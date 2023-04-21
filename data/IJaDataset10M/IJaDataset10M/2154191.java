package org.neuroph.contrib.dev;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.comp.InputOutputNeuron;
import org.neuroph.nnet.learning.BinaryHebbianLearning;
import org.neuroph.util.ConnectionFactory;
import org.neuroph.util.LayerFactory;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.NeuralNetworkType;
import org.neuroph.util.NeuronProperties;
import org.neuroph.util.TransferFunctionType;

/**
 *!!!! Learning rule not implemented!!!!
 * What criteria to use to stop unsupervse learning? Hopfield energy function?
 * Also needs simulated anealing
 * see http://www.cs.cf.ac.uk/Dave/JAVA/boltzman/Necker.html ; how to calculate goodnes? min of energy?
 * http://en.wikipedia.org/wiki/Boltzmann_machine#Training
 * Mozda koristiti supervised hebbian learning - pise u Jeffovpj knjizi
 * 
 * @author zoran
 */
public class BoltzmanNetwork extends NeuralNetwork {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates an instance of Boltzman network with specified number of neurons
         * in input, hidden and output layer.
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 */
    public BoltzmanNetwork(int inputNeuronsCount, int hiddenNeuronsCount, int outputNeuronsCount) {
        NeuronProperties neuronProperties = new NeuronProperties();
        neuronProperties.setProperty("neuronType", InputOutputNeuron.class);
        neuronProperties.setProperty("bias", new Double(0));
        neuronProperties.setProperty("transferFunction", TransferFunctionType.STEP);
        neuronProperties.setProperty("transferFunction.yHigh", new Double(1));
        neuronProperties.setProperty("transferFunction.yLow", new Double(0));
        this.createNetwork(inputNeuronsCount, hiddenNeuronsCount, outputNeuronsCount, neuronProperties);
    }

    /**
	 * Creates Boltzman network architecture
	 * 
	 * @param inputNeuronsCount
	 *            number of neurons in input layer
	 * @param outputNeuronsCount
	 *            number of neurons in output layer
	 * @param neuronProperties
	 *            neuron properties
	 */
    private void createNetwork(int inputNeuronsCount, int hiddenNeuronsCount, int outputNeuronsCount, NeuronProperties neuronProperties) {
        this.setNetworkType(NeuralNetworkType.BOLTZMAN);
        Layer inputLayer = LayerFactory.createLayer(inputNeuronsCount, neuronProperties);
        this.addLayer(inputLayer);
        Layer hiddenLayer = LayerFactory.createLayer(hiddenNeuronsCount, neuronProperties);
        this.addLayer(hiddenLayer);
        Layer outputLayer = LayerFactory.createLayer(outputNeuronsCount, neuronProperties);
        this.addLayer(outputLayer);
        ConnectionFactory.fullConnect(inputLayer);
        ConnectionFactory.fullConnect(hiddenLayer);
        ConnectionFactory.fullConnect(outputLayer);
        ConnectionFactory.fullConnect(inputLayer, outputLayer);
        ConnectionFactory.fullConnect(outputLayer, inputLayer);
        ConnectionFactory.fullConnect(inputLayer, hiddenLayer);
        ConnectionFactory.fullConnect(hiddenLayer, inputLayer);
        ConnectionFactory.fullConnect(outputLayer, hiddenLayer);
        ConnectionFactory.fullConnect(hiddenLayer, outputLayer);
        NeuralNetworkFactory.setDefaultIO(this);
        this.setLearningRule(new BinaryHebbianLearning());
    }
}
