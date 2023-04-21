package com.ibm.wala.util.graph.impl;

import java.util.Iterator;
import com.ibm.wala.util.graph.NumberedNodeManager;
import com.ibm.wala.util.intset.IntSet;
import com.ibm.wala.util.intset.MutableMapping;

/**
 * An object which manages node numbers via a mapping.
 */
public class SlowNumberedNodeManager<T> implements NumberedNodeManager<T> {

    /**
   * A bijection between integer <-> node
   */
    private final MutableMapping<T> map = MutableMapping.make();

    public int getNumber(T obj) {
        return map.getMappedIndex(obj);
    }

    public T getNode(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("number must be >= 0");
        }
        T result = map.getMappedObject(number);
        return result;
    }

    public int getMaxNumber() {
        return map.getMaximumIndex();
    }

    public Iterator<T> iterator() {
        return map.iterator();
    }

    public int getNumberOfNodes() {
        return map.getSize();
    }

    public void addNode(T n) {
        if (n == null) {
            throw new IllegalArgumentException("n is null");
        }
        map.add(n);
    }

    public void removeNode(T n) {
        map.deleteMappedObject(n);
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("Nodes:\n");
        for (int i = 0; i <= getMaxNumber(); i++) {
            result.append(i).append("  ");
            result.append(map.getMappedObject(i));
            result.append("\n");
        }
        return result.toString();
    }

    public boolean containsNode(T N) {
        return getNumber(N) != -1;
    }

    public Iterator<T> iterateNodes(IntSet s) {
        return new NumberedNodeIterator<T>(s, this);
    }
}
