package de.oklemenz.meta.ann.helper.network;

import de.oklemenz.meta.ann.ArtificialNeuralNetwork;
import de.oklemenz.meta.ann.api.BooleanParameter;

public class FeedbackNetwork<I extends Comparable<I>, E extends Comparable<E>, O extends Comparable<O>, A extends Comparable<A>> extends FeedForwardNetwork<I, E, O, A> {

    private static final long serialVersionUID = 3905524908292321849L;

    protected BooleanParameter<I, E, O, A> directFeedback;

    protected BooleanParameter<I, E, O, A> indirectFeedback;

    protected BooleanParameter<I, E, O, A> lateralFeedback;

    protected BooleanParameter<I, E, O, A> completeFeedback;

    /**
     * @param artificialNeuralNetwork
     */
    public FeedbackNetwork(ArtificialNeuralNetwork<I, E, O, A> artificialNeuralNetwork) {
        super(artificialNeuralNetwork);
        directFeedback = getArtificialNeuralNetwork().getBooleanParameter(ArtificialNeuralNetwork.ANN_NETWORK_DIRECT_FEEDBACK);
        indirectFeedback = getArtificialNeuralNetwork().getBooleanParameter(ArtificialNeuralNetwork.ANN_NETWORK_INDIRECT_FEEDBACK);
        lateralFeedback = getArtificialNeuralNetwork().getBooleanParameter(ArtificialNeuralNetwork.ANN_NETWORK_LATERAL_FEEDBACK);
        completeFeedback = getArtificialNeuralNetwork().getBooleanParameter(ArtificialNeuralNetwork.ANN_NETWORK_COMPLETE);
    }

    public String toString() {
        return "feedbackNetwork:";
    }
}
