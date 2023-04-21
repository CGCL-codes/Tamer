package org.apache.commons.math3.geometry.partitioning;

import org.apache.commons.math3.geometry.Space;

/** Visitor computing the boundary size.
 * @param <S> Type of the space.
 * @version $Id: BoundarySizeVisitor.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 3.0
 */
class BoundarySizeVisitor<S extends Space> implements BSPTreeVisitor<S> {

    /** Size of the boundary. */
    private double boundarySize;

    /** Simple constructor.
     */
    public BoundarySizeVisitor() {
        boundarySize = 0;
    }

    /** {@inheritDoc}*/
    public Order visitOrder(final BSPTree<S> node) {
        return Order.MINUS_SUB_PLUS;
    }

    /** {@inheritDoc}*/
    public void visitInternalNode(final BSPTree<S> node) {
        @SuppressWarnings("unchecked") final BoundaryAttribute<S> attribute = (BoundaryAttribute<S>) node.getAttribute();
        if (attribute.getPlusOutside() != null) {
            boundarySize += attribute.getPlusOutside().getSize();
        }
        if (attribute.getPlusInside() != null) {
            boundarySize += attribute.getPlusInside().getSize();
        }
    }

    /** {@inheritDoc}*/
    public void visitLeafNode(final BSPTree<S> node) {
    }

    /** Get the size of the boundary.
     * @return size of the boundary
     */
    public double getSize() {
        return boundarySize;
    }
}
