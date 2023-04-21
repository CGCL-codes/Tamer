package org.simbrain.network.gui.nodes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolox.handles.PHandle;
import edu.umd.cs.piccolox.util.PNodeLocator;

/**
 * Selection handle.
 *
 * <p>Usage:
 * <pre>
 * PNode node = ...;
 * SelectionHandle.addSelectionHandleTo(node)
 * </pre>
 * and
 * <pre>
 * PNode node = ...;
 * SelectionHandle.removeSelectionHandleFrom(node)
 * </pre>
 * </p>
 *
 * @see #addSelectionHandleTo(PNode)
 * @see #removeSelectionHandleFrom(PNode)
 */
public final class SourceHandle extends PHandle {

    /** Extend factor. */
    private static final double EXTEND_FACTOR = 0.2d;

    /** Color of selection boxes. */
    private static Color sourceColor = Color.RED;

    /**
     * Create a new selection handle.
     *
     * @param locator locator
     */
    private SourceHandle(final PNodeLocator locator) {
        super(locator);
        reset();
        setPickable(false);
        PNode parentNode = locator.getNode();
        parentNode.addChild(this);
        setPaint(null);
        setStrokePaint(sourceColor);
        updateBounds();
        relocateHandle();
    }

    /** @see PHandle */
    public void parentBoundsChanged() {
        updateBounds();
        super.parentBoundsChanged();
    }

    /**
     * Update the bounds of this selection handle based on the
     * size of its parent plus an extension factor.
     */
    private void updateBounds() {
        PNode parentNode = ((PNodeLocator) getLocator()).getNode();
        double x = 0.0d - (parentNode.getWidth() * EXTEND_FACTOR);
        double y = 0.0d - (parentNode.getHeight() * EXTEND_FACTOR);
        double width = parentNode.getWidth() + 2 * (parentNode.getWidth() * EXTEND_FACTOR);
        double height = parentNode.getHeight() + 2 * (parentNode.getHeight() * EXTEND_FACTOR);
        setPathToRectangle((float) x, (float) y, (float) width, (float) height);
    }

    /**
     * Return true if the specified node has a selection handle
     * as a child.
     *
     * @param node node
     * @return true if the specified node has a selection handle
     *    as a child
     */
    private static boolean hasSelectionHandle(final PNode node) {
        for (Iterator i = node.getChildrenIterator(); i.hasNext(); ) {
            PNode n = (PNode) i.next();
            if (n instanceof SourceHandle) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a selection handle to the specified node, if one does not
     * exist already.
     *
     * @param node node to add the selection handle to, must not be null
     */
    public static void addSourceHandleTo(final PNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }
        if (hasSelectionHandle(node)) {
            return;
        }
        PNodeLocator nodeLocator = new PNodeLocator(node);
        SourceHandle selectionHandle = new SourceHandle(nodeLocator);
    }

    /**
     * Remove the selection handle(s) from the specified node, if any exist.
     *
     * @param node node to remove the selection handle(s) from, must not be null
     */
    public static void removeSourceHandleFrom(final PNode node) {
        if (node == null) {
            throw new IllegalArgumentException("node must not be null");
        }
        Collection handlesToRemove = new ArrayList();
        for (Iterator i = node.getChildrenIterator(); i.hasNext(); ) {
            PNode n = (PNode) i.next();
            if (n instanceof SourceHandle) {
                handlesToRemove.add(n);
            }
        }
        node.removeChildren(handlesToRemove);
    }

    /**
     * @return Returns the sourceColor.
     */
    public static Color getSourceColor() {
        return sourceColor;
    }

    /**
     * @param sourceColor The sourceColor to set.
     */
    public static void setSourceColor(final Color selectionColor) {
        SourceHandle.sourceColor = selectionColor;
    }
}
