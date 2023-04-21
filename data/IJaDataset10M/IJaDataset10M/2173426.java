package org.protune.net;

/**
 * In the <i>Protune</i> system we are mostly interested in negotiations among (pairs of) peers. A
 * negotiation (i) starts, (ii) goes on and (iii) finishes. The class <tt>StartNegotiationMessage</tt>
 * (resp. {@link org.protune.net.OngoingNegotiationMessage},
 * {@link org.protune.net.EndNegotiationMessage}) is meant to represent a message the peers exchange
 * during (i) (resp. (ii), (iii)).<br />
 * Therefore a <tt>StartNegotiationMessage</tt> should wrap all information needed to start a
 * negotiation. According to the <i>Protune</i> protocol, so far this information is just a
 * {@link org.protune.net.Pointer} to the peer.
 * @author jldecoi
 */
public class StartNegotiationMessage implements NegotiationMessage {

    static final long serialVersionUID = 411;

    Pointer peerPointer;

    public StartNegotiationMessage(Pointer p) {
        peerPointer = p;
    }

    public Pointer getPeerPointer() {
        return peerPointer;
    }
}
