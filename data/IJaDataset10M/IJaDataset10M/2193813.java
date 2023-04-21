package com.rapidminer.operator.learner.functions.neuralnet;

import com.rapidminer.example.Example;

/**
 * This function represents a linear activation function by calculating the
 * identity function on the weighted sum. The linear activation function
 * is usually used for the output layer of regression problems.
 *
 * @author Ingo Mierswa
 */
public class LinearFunction extends ActivationFunction {

    private static final long serialVersionUID = 1L;

    @Override
    public String getTypeName() {
        return "Linear";
    }

    @Override
    public double calculateValue(InnerNode node, Example example) {
        Node[] inputs = node.getInputNodes();
        double[] weights = node.getWeights();
        double weightedSum = weights[0];
        for (int i = 0; i < inputs.length; i++) {
            weightedSum += inputs[i].calculateValue(true, example) * weights[i + 1];
        }
        return weightedSum;
    }

    @Override
    public double calculateError(InnerNode node, Example example) {
        Node[] outputs = node.getOutputNodes();
        int[] numberOfOutputs = node.getOutputNodeInputIndices();
        double errorSum = 0;
        for (int i = 0; i < outputs.length; i++) {
            errorSum += outputs[i].calculateError(true, example) * outputs[i].getWeight(numberOfOutputs[i]);
        }
        return errorSum;
    }
}
