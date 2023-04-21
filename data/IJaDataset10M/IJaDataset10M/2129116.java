package org.hydracache.server.harmony.core;

import java.util.Collection;
import org.hydracache.util.SimpleSet;

public class NodeSet extends SimpleSet<Node> {

    public NodeSet() {
        super();
    }

    @SuppressWarnings("unchecked")
    public NodeSet(Collection<? extends Node> collection) {
        super((Collection<Node>) collection);
    }
}
