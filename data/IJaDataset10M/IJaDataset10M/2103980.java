package net.sf.saxon.tree.iter;

import net.sf.saxon.om.SequenceIterator;

/**
 * A SequenceIterator is used to iterate over a sequence. A LookaheadIterator
 * is one that supports a hasNext() method to determine if there are more nodes
 * after the current node.
 */
public interface LookaheadIterator extends SequenceIterator {

    /**
     * Determine whether there are more items to come. Note that this operation
     * is stateless and it is not necessary (or usual) to call it before calling
     * next(). It is used only when there is an explicit need to tell if we
     * are at the last element.
     * <p/>
     * This method must not be called unless the result of getProperties() on the iterator
     * includes the bit setting {@link SequenceIterator#LOOKAHEAD}
     *
     * @return true if there are more items in the sequence
     */
    public boolean hasNext();
}
