package com.acworks.acroute.v2;

import java.util.Set;

/**
 * Graph node (aka vertex)
 * @author nikita
 */
public interface Node extends Comparable<Node> {

    NodeType getType();

    /**
     *
     * @return node id, unique within the graph
     */
    int getId();

    /**     
     * @return graph containing this node
     */
    Graph getGraph();

    /**
     *
     * @return collection of adjacent Nodes connected to this node by
     * either an undirected Arc or by a directed arc originating at this node.
     */
    Set<Node> getSuccessors();
}
