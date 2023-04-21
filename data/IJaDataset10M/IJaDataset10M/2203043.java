package de.oklemenz.meta.ann.helper;

import de.oklemenz.meta.ann.ArtificialNeuralNetwork;
import de.oklemenz.meta.ann.api.Activity;
import de.oklemenz.meta.ann.api.Neuron;
import de.oklemenz.meta.ann.api.Value;

public abstract class DefaultActivity<I extends Comparable<I>, E extends Comparable<E>, O extends Comparable<O>, A extends Comparable<A>> extends DefaultANNNeuronEntity<I, E, O, A> implements Activity<I, E, O, A> {

    protected A value;

    public DefaultActivity(ArtificialNeuralNetwork<I, E, O, A> artificialNeuralNetwork, Neuron<I, E, O, A> neuron) {
        super(artificialNeuralNetwork, neuron);
    }

    public DefaultActivity(ArtificialNeuralNetwork<I, E, O, A> artificialNeuralNetwork, Neuron<I, E, O, A> neuron, A value) {
        super(artificialNeuralNetwork, neuron);
        this.value = value;
    }

    public A getValue() {
        return value;
    }

    public Activity<I, E, O, A> toNew() {
        return getNeuron().createActivity(this);
    }

    public int compareToZero() {
        return this.compareTo(getZero());
    }

    public int compareToOne() {
        return this.compareTo(getOne());
    }

    public int compareTo(Value<I, E, O, A> value) {
        return getValue().compareTo(((Activity<I, E, O, A>) value).getValue());
    }

    public String toString() {
        return "activity";
    }
}
