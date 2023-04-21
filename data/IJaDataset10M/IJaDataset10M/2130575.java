package org.jul.warp.messaging;

import static org.jul.warp.Encoder.*;
import java.nio.ByteBuffer;
import org.jul.warp.Environment;
import org.jul.warp.NodeId;

public class NodeMessage extends Message {

    private NodeId sender;

    @Override
    protected void decode(ByteBuffer buffer) {
        sender = environment.getIdFactory().decodeNodeId(buffer);
    }

    @Override
    protected void encode(ByteBuffer buffer) {
        sender.encode(buffer);
    }

    @Override
    protected int getSize() {
        return sender.getSize();
    }

    public NodeId getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return super.toString() + " { sender = " + sender + " }";
    }

    public NodeMessage(Environment environment, NodeId sender) {
        super(environment);
        this.sender = sender;
        if (sender == null) {
            throw new NullPointerException();
        }
    }

    protected NodeMessage(Environment environment) {
        super(environment);
    }
}
