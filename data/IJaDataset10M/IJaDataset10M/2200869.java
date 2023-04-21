package org.codecover.instrumentation.xampil.visitor;

import org.codecover.instrumentation.xampil.syntaxtree.Node;
import org.codecover.instrumentation.xampil.syntaxtree.NodeToken;

/**
 * Gets the first {@link NodeToken#startOffset}
 */
public class StartOffset extends DepthFirstVisitor {

    private int foundStartOffset = -1;

    private StartOffset() {
    }

    /**
     * Assigns {@link #foundStartOffset} and throws an {@link RuntimeException}.
     */
    @Override
    public void visit(NodeToken n) {
        this.foundStartOffset = n.startOffset;
        throw new RuntimeException();
    }

    /**
     * This method seaches in a {@link Node} for the first
     * {@link NodeToken#startOffset}. <br>
     * This method calls:
     * 
     * <pre>
     * StartOffset startOffset = new StartOffset();
     * 
     * try {
     *     n.accept(startOffset);
     * } catch (RuntimeException e) {
     *     // expected
     * }
     * 
     * return startOffset.foundStartOffset;
     * </pre>
     * 
     * @param n
     *            The Node to start the search from.
     * 
     * @return The first {@link NodeToken#startOffset}. <code>-1</code> if
     *         not present.
     */
    public static int getStartOffset(Node n) {
        StartOffset startOffset = new StartOffset();
        try {
            n.accept(startOffset);
        } catch (RuntimeException e) {
        }
        return startOffset.foundStartOffset;
    }
}
