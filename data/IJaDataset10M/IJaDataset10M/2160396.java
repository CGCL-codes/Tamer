package neon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Graph<T> {

    private HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();

    /**
	 * Adds a node to the graph. Any existing node with the given index
	 * is overwritten. Connections to the node are kept.
	 * 
	 * @param node	the index of the new node
	 */
    public void addNode(int index, T content) {
        nodes.put(index, new Node(content));
    }

    /**
	 * Adds a connection between two nodes.
	 * 
	 * @param from			the start node
	 * @param to			the end node
	 * @param bidirectional	whether the connection is bidirectional
	 */
    public void addConnection(int from, int to, boolean bidirectional) {
        if (nodes.get(from) != null && nodes.get(to) != null) {
            nodes.get(from).addConnection(to);
            if (bidirectional) {
                nodes.get(to).addConnection(from);
            }
        }
    }

    /**
	 * @param index
	 * @return	the content of the node with the given index
	 */
    public T getNode(int index) {
        return nodes.get(index).content;
    }

    /**
	 * @param index
	 * @return	all connections leaving the node with the given index
	 */
    public Collection<Integer> getConnections(int index) {
        try {
            return nodes.get(index).connections;
        } catch (IndexOutOfBoundsException e) {
            return new ArrayList<Integer>();
        }
    }

    /**
	 * @return	a collection of all nodes in this graph
	 */
    public Collection<T> getNodes() {
        ArrayList<T> content = new ArrayList<T>();
        for (Node node : nodes.values()) {
            content.add(node.content);
        }
        return content;
    }

    private class Node {

        private T content;

        private ArrayList<Integer> connections = new ArrayList<Integer>();

        private Node(T content) {
            this.content = content;
        }

        private void addConnection(int to) {
            connections.add(to);
        }
    }
}
