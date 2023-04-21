package org.dcopolis.util;

import java.util.*;

@SuppressWarnings("unchecked")
public class GraphVertex<V extends Vertex> implements Vertex<V> {

    public transient Set<V> neighbors;

    public GraphVertex() {
        this(new HashSet<V>());
    }

    public GraphVertex(Set<V> neighbors) {
        this.neighbors = new LinkedHashSet<V>();
    }

    protected void addNeighbor(GraphVertex<V> neighbor) {
        neighbors.add((V) neighbor);
        neighbor.neighbors.add((V) this);
    }

    public Set<V> getNeighbors() {
        return neighbors;
    }

    private static class DFSIterator<V extends Vertex> implements Iterator<V> {

        Stack<V> stack;

        HashSet<V> history;

        public DFSIterator(V v) {
            stack = new Stack<V>();
            stack.push(v);
            history = new HashSet<V>();
            history.add(v);
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public V next() {
            V v = stack.pop();
            for (Object nn : v.getNeighbors()) {
                V n = (V) nn;
                if (!history.contains(n)) {
                    stack.push(n);
                    history.add(n);
                }
            }
            return v;
        }

        public void remove() {
            throw new UnsupportedOperationException("You cannot remove vertices from a graph!");
        }
    }

    public Iterator<V> iterator() {
        return new DFSIterator<V>((V) this);
    }
}
