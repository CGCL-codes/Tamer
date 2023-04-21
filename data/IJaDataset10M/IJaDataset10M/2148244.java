package net.sf.katta.node;

import net.sf.katta.protocol.InteractionProtocol;

public class NodeContext {

    private final Node _node;

    private final ShardManager _shardManager;

    private final InteractionProtocol _protocol;

    private final IContentServer _nodeManaged;

    public NodeContext(InteractionProtocol protocol, Node node, ShardManager shardManager, IContentServer nodeManaged) {
        _protocol = protocol;
        _node = node;
        _shardManager = shardManager;
        _nodeManaged = nodeManaged;
    }

    public Node getNode() {
        return _node;
    }

    public ShardManager getShardManager() {
        return _shardManager;
    }

    public InteractionProtocol getProtocol() {
        return _protocol;
    }

    public IContentServer getContentServer() {
        return _nodeManaged;
    }
}
