package org.neuroph.core.input;

import java.io.Serializable;
import java.util.Vector;
import org.neuroph.core.Connection;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;

/**
 * Performs the vector difference operation on input and
 * weight vector.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class Diference extends WeightsFunction implements Serializable {

    /**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */
    private static final long serialVersionUID = 21L;

    public Vector<Double> getOutput(Vector<Connection> inputConnections) {
        Vector<Double> inputVector = new Vector<Double>();
        for (Connection connection : inputConnections) {
            Neuron neuron = connection.getConnectedNeuron();
            Weight weight = connection.getWeight();
            double input = neuron.getOutput() - weight.getValue();
            inputVector.addElement(input);
        }
        return inputVector;
    }
}
