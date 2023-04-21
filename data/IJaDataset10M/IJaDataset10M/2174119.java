package com.hazelcast.cluster;

import com.hazelcast.impl.Node;

public interface NodeAware {

    Node getNode();

    void setNode(Node node);
}
