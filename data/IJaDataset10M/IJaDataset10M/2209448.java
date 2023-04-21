package net.sf.saxon.expr;

import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.sort.NodeOrderComparer;
import net.sf.saxon.trans.XPathException;

/**
* An enumeration representing a nodeset that is teh difference of two other NodeSets.
* There is an "except" operator in XPath 2.0 to create such an expression.
*/
public class DifferenceEnumeration implements SequenceIterator {

    private SequenceIterator p1;

    private SequenceIterator p2;

    private NodeInfo nextNode1 = null;

    private NodeInfo nextNode2 = null;

    private NodeOrderComparer comparer;

    private NodeInfo current = null;

    private int position = 0;

    /**
    * Form an enumeration of the difference of two nodesets, that is, the nodes
    * that are in p1 and that are not in p2.
    * @param p1 the first operand, with nodes delivered in document order
    * @param p2 the second operand, with nodes delivered in document order
    * @param comparer the comparer
    */
    public DifferenceEnumeration(SequenceIterator p1, SequenceIterator p2, NodeOrderComparer comparer) throws XPathException {
        this.p1 = p1;
        this.p2 = p2;
        this.comparer = comparer;
        nextNode1 = next(p1);
        nextNode2 = next(p2);
    }

    /**
    * Get the next item from one of the input sequences,
    * checking that it is a node.
     * @param iter the iterator from which the next node is to be read
     * @return the node that was read, or null if the stream is exhausted
    */
    private NodeInfo next(SequenceIterator iter) throws XPathException {
        return (NodeInfo) iter.next();
    }

    public Item next() throws XPathException {
        while (true) {
            if (nextNode1 == null) {
                current = null;
                position = -1;
                return null;
            }
            if (nextNode2 == null) {
                return deliver();
            }
            int c = comparer.compare(nextNode1, nextNode2);
            if (c < 0) {
                return deliver();
            } else if (c > 0) {
                nextNode2 = next(p2);
                if (nextNode2 == null) {
                    return deliver();
                }
            } else {
                nextNode2 = next(p2);
                nextNode1 = next(p1);
            }
        }
    }

    /**
     * Deliver the next node from the first node-set, advancing the iterator to
     * look-ahead for the next item, and setting the current and position variables.
     * @return the next node from the first node-set
     * @throws XPathException
     */
    private NodeInfo deliver() throws XPathException {
        current = nextNode1;
        nextNode1 = next(p1);
        position++;
        return current;
    }

    public Item current() {
        return current;
    }

    public int position() {
        return position;
    }

    public void close() {
        p1.close();
        p2.close();
    }

    public SequenceIterator getAnother() throws XPathException {
        return new DifferenceEnumeration(p1.getAnother(), p2.getAnother(), comparer);
    }

    /**
     * Get properties of this iterator, as a bit-significant integer.
     *
     * @return the properties of this iterator. This will be some combination of
     *         properties such as {@link SequenceIterator#GROUNDED}, {@link SequenceIterator#LAST_POSITION_FINDER},
     *         and {@link SequenceIterator#LOOKAHEAD}. It is always
     *         acceptable to return the value zero, indicating that there are no known special properties.
     *         It is acceptable for the properties of the iterator to change depending on its state.
     */
    public int getProperties() {
        return 0;
    }
}
