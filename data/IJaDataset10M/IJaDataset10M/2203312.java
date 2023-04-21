package com.greentea.relaxation.jnmf.lib.synapses;

import com.greentea.relaxation.jnmf.model.Neuron;
import com.greentea.relaxation.jnmf.model.Synapse;
import org.apache.commons.lang.Validate;

public class SignalDiferenceSynapse extends Synapse {

    private static final long serialVersionUID = 1L;

    /**
    * Gets the signal from synapse.
    *
    * @param destinationNeuron the neuron which obtain the signal
    * @return the value of signal
    */
    public double getSignal(Neuron destinationNeuron) {
        Validate.isTrue(destinationNeuron == destination, "wrong destination neuron");
        return (w - signal);
    }
}
