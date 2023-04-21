package com.ibm.wala.util.graph.traverse;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import com.ibm.wala.util.collections.EmptyIterator;
import com.ibm.wala.util.collections.NonNullSingletonIterator;
import com.ibm.wala.util.debug.UnimplementedError;
import com.ibm.wala.util.graph.NumberedGraph;

/**
 * This class implements depth-first search over a {@link NumberedGraph}, return an enumeration of the nodes of the graph in order of
 * increasing discover time. This class follows the outNodes of the graph nodes to define the graph, but this behavior can be
 * changed by overriding the getConnected method.
 */
public abstract class DFSDiscoverTimeIterator<T> extends Stack<T> implements Iterator<T> {

    /**
   * an enumeration of all nodes to search from
   */
    private Iterator<? extends T> roots;

    /**
   * subclass constructors must call this!
   */
    protected void init(Iterator<? extends T> nodes) {
        roots = nodes;
        assert nodes != null;
        if (roots.hasNext()) {
            T n = roots.next();
            push(n);
            setPendingChildren(n, getConnected(n));
        }
    }

    /**
   * subclass constructors must call this!
   */
    protected void init(T N) {
        init(new NonNullSingletonIterator<T>(N));
    }

    /**
   * Return whether there are any more nodes left to enumerate.
   * 
   * @return true if there nodes left to enumerate.
   */
    public boolean hasNext() {
        return (!empty());
    }

    protected abstract Iterator<? extends T> getPendingChildren(T n);

    protected abstract void setPendingChildren(T v, Iterator<? extends T> iterator);

    /**
   * Find the next graph node in discover time order.
   * 
   * @return the next graph node in discover time order.
   */
    public T next() throws NoSuchElementException {
        if (empty()) {
            throw new NoSuchElementException();
        }
        T toReturn = peek();
        assert getPendingChildren(toReturn) != null;
        do {
            T stackTop = peek();
            for (Iterator<? extends T> it = getPendingChildren(stackTop); it.hasNext(); ) {
                T child = it.next();
                if (getPendingChildren(child) == null) {
                    visitEdge(stackTop, child);
                    setPendingChildren(child, getConnected(child));
                    push(child);
                    return toReturn;
                }
            }
            Iterator<T> empty = EmptyIterator.instance();
            setPendingChildren(stackTop, empty);
            pop();
        } while (!empty());
        while (roots.hasNext()) {
            T nextRoot = roots.next();
            if (getPendingChildren(nextRoot) == null) {
                push(nextRoot);
                setPendingChildren(nextRoot, getConnected(nextRoot));
                return toReturn;
            }
        }
        return toReturn;
    }

    /**
   * get the out edges of a given node
   * 
   * @param n the node of which to get the out edges
   * @return the out edges
   * 
   */
    protected abstract Iterator<? extends T> getConnected(T n);

    public void remove() throws UnimplementedError {
        throw new UnimplementedError();
    }

    /**
   * @param from source of the edge to visit
   * @param to target of the edge to visit
   */
    protected void visitEdge(T from, T to) {
    }
}
