package net.walend.digraph;

import java.util.Iterator;
import java.util.Set;
import net.walend.collection.HasState;
import net.walend.collection.Bag;

/**
CEDigraph methods that use indices.

@author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
*/
public interface IndexedCEDigraph extends IndexedDigraph, CEDigraph {

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndicies() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    public boolean contiansEdge(int fromIndex, int toIndex, Object edge);

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndicies() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    public Bag getInboundEdges(int index);

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndicies() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    public Bag getOutboundEdges(int index);

    /**
@throws ArrayIndexOutOfBoundsException if index does not have a node. Use nodeIndicies() or getNodeIndex() to avoid these. In your code, catch ArrayIndexOutOfBoundsException and throw a ConcurrentModificationException if you think that's the problem.
     */
    public Object getEdge(int fromIndex, int toIndex);

    public IndexedEdgeIterator indexedEdgeIterator();
}
