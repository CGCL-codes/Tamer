package com.nipun.neural.objects.neurons;

import com.nipun.graph.objects.VertexImpl;
import com.nipun.neural.objects.Neuron;

public class InputNeuron extends VertexImpl implements Neuron {

    private double activation;

    public InputNeuron(String label) {
        super(label);
        activation = 0;
    }

    public void fire(double input) {
        System.out.println("why are we firing an input neuron??");
    }

    public double getActivation() {
        return activation;
    }

    public double getBias() {
        System.out.println("why was an input neuron asked for its bias?? returning 0");
        return 0;
    }

    public double getDelta() {
        System.out.println("why was an input neuron asked for its delta?? returning 0");
        return 0;
    }

    public double getDerivative() {
        System.out.println("why was an input neuron asked for getDerivative()?? returning 0");
        return 0;
    }

    public void setActivation(double act) {
        this.activation = act;
    }

    public void setBias(double bias) {
        System.out.println("why are we trying to setBias() an input neuron??");
    }

    public void setDelta(double delta) {
        System.out.println("why are we trying to setDelta() an input neuron??");
    }
}
