package com.ibm.wala.util.graph.impl;

import java.util.Iterator;
import com.ibm.wala.util.collections.EmptyIterator;
import com.ibm.wala.util.debug.Assertions;
import com.ibm.wala.util.debug.UnimplementedError;
import com.ibm.wala.util.graph.INodeWithNumberedEdges;
import com.ibm.wala.util.graph.NumberedEdgeManager;
import com.ibm.wala.util.intset.IntIterator;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.SparseIntSet;

/**
 * An object that delegates edge management to the nodes, {@link INodeWithNumberedEdges}
 */
public class DelegatingNumberedEdgeManager<T extends INodeWithNumberedEdges> implements NumberedEdgeManager<T> {

    private final DelegatingNumberedNodeManager<T> nodeManager;

    public DelegatingNumberedEdgeManager(DelegatingNumberedNodeManager<T> nodeManager) {
        if (nodeManager == null) {
            throw new IllegalArgumentException("nodeManager is null");
        }
        this.nodeManager = nodeManager;
    }

    private class IntSetNodeIterator implements Iterator<T> {

        private final IntIterator delegate;

        IntSetNodeIterator(IntIterator delegate) {
            this.delegate = delegate;
        }

        public boolean hasNext() {
            return delegate.hasNext();
        }

        public T next() {
            return nodeManager.getNode(delegate.next());
        }

        public void remove() {
            Assertions.UNREACHABLE();
        }
    }

    public Iterator<T> getPredNodes(T N) throws IllegalArgumentException {
        if (N == null) {
            throw new IllegalArgumentException("N cannot be null");
        }
        INodeWithNumberedEdges en = N;
        IntSet pred = en.getPredNumbers();
        Iterator<T> empty = EmptyIterator.instance();
        return (pred == null) ? empty : (Iterator<T>) new IntSetNodeIterator(pred.intIterator());
    }

    public IntSet getPredNodeNumbers(T node) {
        if (node == null) {
            throw new IllegalArgumentException("N cannot be null");
        }
        INodeWithNumberedEdges en = node;
        IntSet pred = en.getPredNumbers();
        return (pred == null) ? new SparseIntSet() : pred;
    }

    public int getPredNodeCount(T N) throws IllegalArgumentException {
        if (N == null) {
            throw new IllegalArgumentException("N cannot be null");
        }
        INodeWithNumberedEdges en = N;
        IntSet s = en.getPredNumbers();
        if (s == null) {
            return 0;
        } else {
            return s.size();
        }
    }

    public Iterator<T> getSuccNodes(T N) {
        if (N == null) {
            throw new IllegalArgumentException("N cannot be null");
        }
        INodeWithNumberedEdges en = N;
        IntSet succ = en.getSuccNumbers();
        Iterator<T> empty = EmptyIterator.instance();
        return (succ == null) ? empty : (Iterator<T>) new IntSetNodeIterator(succ.intIterator());
    }

    public int getSuccNodeCount(T N) {
        if (N == null) {
            throw new IllegalArgumentException("N is null");
        }
        INodeWithNumberedEdges en = N;
        IntSet s = en.getSuccNumbers();
        return s == null ? 0 : s.size();
    }

    public void addEdge(T src, T dst) {
        if (dst == null || src == null) {
            throw new IllegalArgumentException("parameter is null");
        }
        src.addSucc(dst.getGraphNodeId());
        dst.addPred(src.getGraphNodeId());
    }

    public void removeEdge(T src, T dst) throws UnimplementedError {
        Assertions.UNREACHABLE("Implement me");
    }

    public void removeAllIncidentEdges(T node) throws UnimplementedError {
        if (node == null) {
            throw new IllegalArgumentException("node is null");
        }
        INodeWithNumberedEdges n = node;
        n.removeAllIncidentEdges();
    }

    public void removeIncomingEdges(T node) throws UnimplementedError {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        INodeWithNumberedEdges n = node;
        n.removeIncomingEdges();
    }

    public void removeOutgoingEdges(T node) throws UnimplementedError {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        INodeWithNumberedEdges n = node;
        n.removeOutgoingEdges();
    }

    public boolean hasEdge(T src, T dst) throws IllegalArgumentException {
        if (dst == null) {
            throw new IllegalArgumentException("dst == null");
        }
        return getSuccNodeNumbers(src).contains(dst.getGraphNodeId());
    }

    public IntSet getSuccNodeNumbers(T node) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null");
        }
        INodeWithNumberedEdges en = node;
        IntSet succ = en.getSuccNumbers();
        return (succ == null) ? new SparseIntSet() : succ;
    }
}
