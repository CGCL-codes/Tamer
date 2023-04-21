package pl.edu.pjwstk.mteam.jcsync.core.pubsub;

import pl.edu.pjwstk.mteam.p2p.P2PNode;
import pl.edu.pjwstk.mteam.pubsub.core.CoreAlgorithm;

/**
 * An extension of {@link CoreAlgorithm CoreAlgorithm}.
 * @author Piotr Bucior
 */
public class PubSubWrapper extends pl.edu.pjwstk.mteam.pubsub.core.CoreAlgorithm {

    private final MessageDeliveryObserver observer;

    public PubSubCustomisableAlgorithm pubSubCustomAlg;

    /**
     * Creates new instance with given arguments.
     * @param port communication port that will be used by pubsub layer.
     * @param n node associated with this layer.
     * @param observer an instance of jcsync core algorithm associated with this 
     * pubsub layer.
     */
    public PubSubWrapper(int port, P2PNode n, MessageDeliveryObserver observer) {
        super(port, n, new pl.edu.pjwstk.mteam.jcsync.core.pubsub.PubSubAlgorithmConfigurator());
        this.observer = observer;
    }

    /**
     * Initialises pubsub layer.
     * @throws Exception 
     */
    public void initialize() throws Exception {
        if (getNode().isConnected() == false) {
            throw new Exception("Node is not connected!");
        }
        super.init();
        this.pubSubCustomAlg = (PubSubCustomisableAlgorithm) super.getCustomizableAlgorithm();
        this.pubSubCustomAlg.setMessageDeliveryObserver(this.observer);
        super.getNode().addCallback(this.pubSubCustomAlg.getNCallback());
    }

    /**
     * Returns <tt>PubSubCustomisableAlgoritm</tt> associated with this pubsub 
     * layer. 
     */
    public PubSubCustomisableAlgorithm getCustomAlgorith() {
        return this.pubSubCustomAlg;
    }
}
