package de.oklemenz.meta.ann.helper.random;

import de.oklemenz.meta.ann.ArtificialNeuralNetwork;

public class DoubleDoubleDoubleDoubleRandom extends DoubleDoubleDoubleARandom<Double> {

    /**
     * Default constructor
     * @param artificialNeuralNetwork ArtificialNeuralNetwork
     * 
     * @since MetaANN 1.0 
     */
    public DoubleDoubleDoubleDoubleRandom(ArtificialNeuralNetwork<Double, Double, Double, Double> artificialNeuralNetwork) {
        super(artificialNeuralNetwork);
    }

    private static final long serialVersionUID = 3258125851903209525L;

    public Double nextA() {
        return nextDouble();
    }

    public Double nextA(Double max) {
        return nextDouble(max);
    }

    public Double nextA(Double min, Double max) {
        return nextDouble(min, max);
    }

    public String toString() {
        return "doubleDoubleDoubleDoubleRandom:";
    }
}
