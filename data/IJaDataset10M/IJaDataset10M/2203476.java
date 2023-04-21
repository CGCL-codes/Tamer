package org.protune.net;

/**
 * Dummy implementation of the abstract class {@link org.protune.net.Service}, which echoes the received
 * messages.
 * @author jldecoi
 */
public class DummyService extends Service {

    DummyService(Pointer p) {
        otherPeer = p;
    }

    public NegotiationMessage eval(OngoingNegotiationMessage onm) {
        return onm;
    }
}
