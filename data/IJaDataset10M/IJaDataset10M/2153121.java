package com.dukesoftware.utils.node.graph;

import java.util.HashMap;

public abstract class TGraphNodeDB<N extends TGraphNode<N>> {

    protected final HashMap<String, N> db = new HashMap<String, N>();

    public N add(N node) {
        return db.put(node.key, node);
    }

    public N get(String key) {
        return db.get(key);
    }

    public N remove(String key) {
        N node = db.remove(key);
        if (node != null) {
            GraphNodeOperator.disconnectAllConnectedNodes(node);
        }
        return node;
    }

    public abstract N[] updateArray();

    public abstract N[] array();

    public void clear() {
        db.clear();
    }
}
