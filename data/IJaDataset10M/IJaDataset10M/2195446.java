package ee.ttu.ann.learning;

import ee.ttu.ann.Neuron;
import ee.ttu.math.ActivationFunction;
import java.util.ArrayList;
import java.util.List;

public class BackPropagationLearningNeuron extends Neuron {

    float errorOutput;

    public BackPropagationLearningNeuron(ActivationFunction activationFunction) {
        super(activationFunction);
    }

    public void addInput(Neuron neuron) {
        if (inputNodes == null) inputNodes = new ArrayList<Neuron.Node>();
        inputNodes.add(new Node(neuron));
    }

    /**
	 * Backpropagate the error to the input neurons<br/>
	 * and adjust the input node weights.<br/>
	 * <p/>
	 * This method should be called when error neuron value is completely set.
	 */
    public void learn() {
        if (inputNodes == null) return;
        for (Neuron.Node node : inputNodes) {
            BackPropagationLearningNeuron nodeInputNeuron = (BackPropagationLearningNeuron) node.getInputNeuron();
            float tmp = activationFunction.calculateDerivative(cachedOutput);
            if (tmp < 0.1) tmp = 0.1f;
            float errorInput = errorOutput * tmp;
            nodeInputNeuron.increaseError(errorInput * node.getWeight());
            float errorWeight = errorInput * nodeInputNeuron.getOutput();
            ((Node) node).adjustWeight(errorWeight);
        }
        errorOutput = 0;
    }

    public void increaseError(float incError) {
        errorOutput += incError;
    }

    public List<Neuron.Node> getInputNodes() {
        return inputNodes;
    }

    public class Node extends Neuron.Node {

        public Node(Neuron inputNeuron) {
            super(inputNeuron);
        }

        public void adjustWeight(float errorWeight) {
            weight -= errorWeight;
        }
    }
}
