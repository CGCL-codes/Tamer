package org.progeeks.graph;

import java.util.*;
import com.phoenixst.plexus.*;
import com.phoenixst.plexus.util.*;
import org.progeeks.util.CloseableIterator;

/**
 *  A subclass of EdgeIteratorTraverserAdapter that
 *  can provide pass-through close support to any contained
 *  ClosableIterator.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class CloseableIteratorTraverserAdapter extends EdgeIteratorTraverserAdapter implements CloseableIterator {

    private Iterator edgeIterator;

    /**
     *  Creates a new unmodifiable ClosableIteratorTraverserAdapter.
     */
    public CloseableIteratorTraverserAdapter(Object baseNode, Iterator edgeIter) {
        super(baseNode, edgeIter);
        this.edgeIterator = edgeIter;
    }

    /**
     *  Creates a new ClosableIteratorTraverserAdapter.
     */
    public CloseableIteratorTraverserAdapter(Graph graph, Object baseNode, Iterator edgeIter) {
        super(graph, baseNode, edgeIter);
        this.edgeIterator = edgeIter;
    }

    /**
     *  Closes the delegate iterator if it is a CloseableIterator.
     */
    public void close() {
        if (edgeIterator instanceof CloseableIterator) {
            ((CloseableIterator) edgeIterator).close();
        }
    }

    public String toString() {
        return (getClass().getName() + "[" + edgeIterator + "]");
    }
}
