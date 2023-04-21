package net.walend.digraph;

/**
This class is an EdgeIterator for Immutables.

@author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
 */
public class ImmutableEdgeIterator implements EdgeIterator {

    private EdgeIterator it;

    public ImmutableEdgeIterator(EdgeIterator it) {
        this.it = it;
    }

    /**
       Returns true if there are more edges in this iterator.
    */
    public boolean hasNext() {
        return it.hasNext();
    }

    /**
       Advances to the next edge in the iterator.
       
       @throws NoSuchElementException if the iterator has nothing left.
    */
    public void next() {
        it.next();
    }

    /**
       Removes the current edge from the digraph.
       
       @throws UnsupportedOperationException if the digraph is immutable.
       @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
    */
    public void remove() {
        throw new UnsupportedOperationException("This CEDigrpah is immutable.");
    }

    /**
       Returns the node that his edge begins at.
       
       @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
    */
    public Object fromNode() {
        return it.fromNode();
    }

    /**
       Returns the node that this edge reaches.
       
       @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
    */
    public Object toNode() {
        return it.toNode();
    }

    /**
       Returns the current edge.
       
       @throws IllegalStateException if the next method has not yet been called, or the remove method has already been called after the last call to the next method.
        */
    public Object edge() {
        return it.edge();
    }
}
