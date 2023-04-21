package de.oklemenz.meta.ann.helper.activationfunction;

import de.oklemenz.meta.ann.ArtificialNeuralNetwork;
import de.oklemenz.meta.ann.api.AParameter;
import de.oklemenz.meta.ann.api.Activity;
import de.oklemenz.meta.ann.api.IParameter;
import de.oklemenz.meta.ann.api.Impulse;
import de.oklemenz.meta.ann.api.Neuron;
import de.oklemenz.meta.ann.helper.DefaultActivationFunction;

public class BipolarThresholdActivationFunction<I extends Comparable<I>, E extends Comparable<E>, O extends Comparable<O>, A extends Comparable<A>> extends DefaultActivationFunction<I, E, O, A> {

    private static final long serialVersionUID = 4121131437689811507L;

    protected IParameter<I, E, O, A> positiveImpulseThreshold;

    protected AParameter<I, E, O, A> positivePole;

    protected AParameter<I, E, O, A> negativePole;

    /**
     * @param artificialNeuralNetwork
     */
    public BipolarThresholdActivationFunction(ArtificialNeuralNetwork<I, E, O, A> artificialNeuralNetwork) {
        super(artificialNeuralNetwork);
        positiveImpulseThreshold = getArtificialNeuralNetwork().getIParameter(ArtificialNeuralNetwork.ANN_IMPULSE_POSITIVE_THRESHOLD);
        positivePole = getArtificialNeuralNetwork().getAParameter(ArtificialNeuralNetwork.ANN_ACTIVITY_POSITIVE_POLE);
        negativePole = getArtificialNeuralNetwork().getAParameter(ArtificialNeuralNetwork.ANN_ACTIVITY_NEGATIVE_POLE);
    }

    public Activity<I, E, O, A> evaluate(Neuron<I, E, O, A> neuron, Activity<I, E, O, A> oldActivity, Impulse<I, E, O, A> impulse) {
        if (impulse.getValue().compareTo(positiveImpulseThreshold.getIValue()) > 0) {
            return neuron.createActivity(positivePole.getAValue());
        }
        return neuron.createActivity(negativePole.getAValue());
    }

    public Activity<I, E, O, A> evaluateDerivative(Neuron<I, E, O, A> neuron, Impulse<I, E, O, A> impulse) {
        return null;
    }

    public String toString() {
        return "bipolarThresholdActivationFunction";
    }
}
