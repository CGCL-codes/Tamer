package net.sf.saxon.tree.iter;

import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.Item;

/**
 * A SequenceIterator is used to iterate over a sequence. An UnfailingIterator
 * is a SequenceIterator that throws no checked exceptions.
 */
public interface UnfailingIterator extends SequenceIterator {

    /**
     * Get the next item in the sequence. <BR>
     * @return the next Item. If there are no more nodes, return null.
     */
    public Item next();

    /**
     * Get the current item in the sequence.
     *
     * @return the current item, that is, the item most recently returned by
     *     next()
     */
    public Item current();

    /**
     * Get the current position
     *
     * @return the position of the current item (the item most recently
     *     returned by next()), starting at 1 for the first node
     */
    public int position();

    /**
     * Get another iterator over the same sequence of items, positioned at the
     * start of the sequence. It must be possible to call this method at any time, whether
     * none, some, or all of the items in the original iterator have been read. The method
     * is non-destructive: it does not change the state of the original iterator.
     * @return a new iterator over the same sequence
     */
    public SequenceIterator getAnother();
}
