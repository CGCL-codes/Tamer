package org.is.aisdag;

import java.util.Hashtable;

public class GraphModel extends Hashtable {

    public GraphModel() {
        super();
    }

    public GraphModel(int capacity) {
        super(capacity);
    }

    public GraphModel(int capacity, float factor) {
        super(capacity, factor);
    }

    public Node getNode(String key) {
        return (Node) get(key);
    }

    public void addNode(String key, Node node) {
        put(key, node);
    }

    public Node removeNode(String key) {
        return (Node) remove(key);
    }
}
