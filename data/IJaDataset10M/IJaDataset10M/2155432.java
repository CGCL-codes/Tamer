package org.aiotrade.neuralnetwork.machine.mlp.neuron;

/**
 * 
 * @author Caoyuan Deng
 */
public class HsinSigmoidNeuron extends PerceptronNeuron {

    private static final double a = Math.PI;

    private static final double b = 2.0 / 3.0;

    public double f(double x) {
        return Math.sin(x / a);
    }

    public double df(double x) {
        return f(x) * (1.0 - f(x));
    }
}
