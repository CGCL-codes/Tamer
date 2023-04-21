package org.jul.warp;

import java.nio.ByteBuffer;

public interface IdFactory {

    public AgentId getNullAgentId();

    public NodeId getNullNodeId();

    public AgentId createRandomAgentId();

    public AgentId decodeAgentId(ByteBuffer buffer);

    public NodeId decodeNodeId(ByteBuffer buffer);
}
