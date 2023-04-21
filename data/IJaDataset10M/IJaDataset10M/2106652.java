package com.greentea.relaxation.algorithms.pnn;

import com.greentea.relaxation.jnmf.model.Neuron;
import com.greentea.relaxation.jnmf.model.Synapse;

public class NeuronWithWeight extends Neuron {

    private static final long serialVersionUID = 1L;

    double weight = 1;

    public NeuronWithWeight() {
    }

    protected void combine() {
        signalBeforeCombiningEvent();
        for (Synapse synapse : inputSynapses) {
            net += synapse.getSignal(this);
        }
        net *= weight;
        signalAfterCombiningEvent();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
