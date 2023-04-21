package de.oklemenz.meta.ann.helper.propagationfunction;

import de.oklemenz.meta.ann.ArtificialNeuralNetwork;
import de.oklemenz.meta.ann.api.Efficiency;
import de.oklemenz.meta.ann.api.Impulse;
import de.oklemenz.meta.ann.api.Neuron;
import de.oklemenz.meta.ann.api.Output;
import de.oklemenz.meta.ann.helper.DefaultPropagationFunction;

public class ProductPropagationFunction<I extends Comparable<I>, E extends Comparable<E>, O extends Comparable<O>, A extends Comparable<A>> extends DefaultPropagationFunction<I, E, O, A> {

    private static final long serialVersionUID = 3257284742704740402L;

    /**
     * @param artificialNeuralNetwork
     */
    public ProductPropagationFunction(ArtificialNeuralNetwork<I, E, O, A> artificialNeuralNetwork) {
        super(artificialNeuralNetwork);
    }

    public Impulse<I, E, O, A> evaluate(Neuron<I, E, O, A> neuron, Output<I, E, O, A> output, Efficiency<I, E, O, A> efficiency) {
        Impulse<I, E, O, A> impulse = neuron.createImpulse().getOne();
        impulse.multiply(output).multiply(efficiency);
        return neuron.getLastImpulse().multiply(impulse);
    }

    public String toString() {
        return "productPropagationFunction:";
    }
}
