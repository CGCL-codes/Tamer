package edu.cmu.ece.agora.network;

import edu.cmu.ece.agora.pipeline.messages.RequestMessage;

public final class NetworkSendRequest<E extends Endpoint> extends RequestMessage implements NetworkLinkMessage<E> {

    private final byte[] data;

    public NetworkSendRequest(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
