package org.custommonkey.xmlunit;

import org.w3c.dom.Node;

/**
 * Listener for callbacks from a
 * {@link DifferenceEngine#compare DifferenceEngine comparison}.
 * <br />Examples and more at <a href="http://xmlunit.sourceforge.net"/>xmlunit.sourceforge.net</a>
 */
public interface DifferenceListener {

    /** 
     * Standard return value for the <code>differenceFound</code> method.
     * Indicates that the <code>Difference</code> is interpreted as defined 
     * in {@link DifferenceConstants DifferenceConstants}.
     */
    public final int RETURN_ACCEPT_DIFFERENCE = 0;

    /** 
     * Override return value for the <code>differenceFound</code> method.
     * Indicates that the nodes identified as being different should be 
     * interpreted as being identical.
     */
    public final int RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL = 1;

    /** 
     * Override return value for the <code>differenceFound</code> method.
     * Indicates that the nodes identified as being different should be 
     * interpreted as being similar.
     */
    public final int RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR = 2;

    /**
     * Receive notification that 2 nodes are different.
     * @param difference a Difference instance as defined in
     *  {@link DifferenceConstants DifferenceConstants} describing the
     *  cause of the difference and containing the detail of the nodes that differ
     * @return int one of the RETURN_... constants describing how this difference
     *  was interpreted
     */
    public int differenceFound(Difference difference);

    /**
     * Receive notification that a comparison between 2 nodes has been skipped
     *  because the node types are not comparable by the DifferenceEngine
     * @param control the control node being compared
     * @param test the test node being compared
     * @see DifferenceEngine
     */
    public void skippedComparison(Node control, Node test);
}
