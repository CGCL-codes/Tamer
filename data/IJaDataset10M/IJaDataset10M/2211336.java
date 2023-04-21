package com.ibm.wala.util.graph.impl;

import java.util.HashSet;
import java.util.Iterator;
import com.ibm.wala.util.collections.HashSetFactory;
import com.ibm.wala.util.graph.NodeManager;

/**
 * Simple implementation of a {@link NodeManager}.
 */
public class BasicNodeManager<T> implements NodeManager<T> {

    private final HashSet<T> nodes = HashSetFactory.make();

    public Iterator<T> iterator() {
        return nodes.iterator();
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public void addNode(T n) {
        nodes.add(n);
    }

    public void removeNode(T n) {
        nodes.remove(n);
    }

    public boolean containsNode(T N) {
        return nodes.contains(N);
    }
}
