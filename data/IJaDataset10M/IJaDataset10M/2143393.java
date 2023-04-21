package net.sourceforge.jsispath.model;

import java.util.*;
import java.security.InvalidParameterException;

/**
 * Represents a directed graph.
 *
 * @author <a href="mailto:bahribayli@gmail.com">Mehdi Bahribayli</a>
 * Version: $Revision
 */
public class Graph {

    private Map<Node, Set<Node>> nodes;

    /**
     * Adds a new edge between <code>fromNode</code> and <code>toNode</code>.
     * @param fromNode is the node that edge departs from.
     * @param toNode is the node that edge enters to.
     */
    public void addEdge(Node fromNode, Node toNode) {
        if (!nodes.containsKey(fromNode)) throw new InvalidParameterException("error: from node is not included in this graph."); else if (!nodes.containsKey(fromNode)) {
            throw new InvalidParameterException("error: to node is not included in this graph.");
        }
        Set<Node> adjacentNodes = nodes.get(fromNode);
        if (adjacentNodes == null) {
            adjacentNodes = new HashSet<Node>();
            nodes.put(fromNode, adjacentNodes);
        }
        adjacentNodes.add(toNode);
    }

    /**
     * Adds a new node to the graph.
     * @param node is the node that is added to the graph.
     */
    public void addNode(Node node) {
        nodes.put(node, null);
    }

    /**
     * Returns set of adjacent nodes to the given node.
     * @param node is the node that ajdacent nodes of it is asked.
     * @return is a set of nodes that are adjacent to <node>node</node>.
     */
    public Set<Node> getAdjacentNodes(Node node) {
        return nodes.get(node);
    }

    public Set<Node> getNodes() {
        return nodes.keySet();
    }

    public Graph() {
        nodes = new HashMap<Node, Set<Node>>();
    }
}
