package de.oklemenz.meta.ann.helper;

import de.oklemenz.meta.ann.ArtificialNeuralNetwork;
import de.oklemenz.meta.ann.api.IntParameter;

public class DefaultIntParameter<I extends Comparable<I>, E extends Comparable<E>, O extends Comparable<O>, A extends Comparable<A>> extends DefaultParameter<I, E, O, A> implements IntParameter<I, E, O, A> {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -144084808229989030L;

    /**
     * Default constructor
     * 
     * @param artificialNeuralNetwork The artificial neural network
     * @param value The value of the <code>Parameter</code>
     * @since MetaANN 1.0 
     */
    public DefaultIntParameter(ArtificialNeuralNetwork<I, E, O, A> artificialNeuralNetwork, int value) {
        super(artificialNeuralNetwork, value);
    }

    public int getIntValue() {
        return ((Integer) getValue()).intValue();
    }

    public String toString() {
        return "intParameter: " + getValue();
    }
}
