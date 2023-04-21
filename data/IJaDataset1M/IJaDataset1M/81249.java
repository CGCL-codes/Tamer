package net.java.sip.communicator.impl.media.transform.dummy;

import net.java.sip.communicator.impl.media.transform.*;

/**
 * DummyTransformEngine does nothing, its sole purpose is to test the
 * TransformConnector related classes.
 * 
 * @author Bing SU (nova.su@gmail.com)
 */
public class DummyTransformEngine implements TransformEngine, PacketTransformer {

    public PacketTransformer getRTCPTransformer() {
        return this;
    }

    public PacketTransformer getRTPTransformer() {
        return this;
    }

    public RawPacket transform(RawPacket pkt) {
        return pkt;
    }

    public RawPacket reverseTransform(RawPacket pkt) {
        return pkt;
    }
}
