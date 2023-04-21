package com.google.devtools.depan.eclipse.visualization.ogl;

import com.google.devtools.depan.model.GraphNode;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Map;

public interface RendererChangeListener {

    /**
   * Notify the receiver that the locations for the indicated nodes should
   * be changed.
   */
    public void locationsChanged(Map<GraphNode, Point2D> changes);

    /**
   * Notify the receiver that the given set of nodes is the current selection.
   */
    public void selectionChanged(Collection<GraphNode> pickedNodes);

    /**
   * Notify the receiver that the given set of nodes should be added to
   * the current selection.
   */
    public void selectionExtended(Collection<GraphNode> pickedNodes);

    /**
   * Notify the receiver that the given set of nodes should be removed from
   * the current selection.
   */
    public void selectionReduced(Collection<GraphNode> pickedNodes);

    /**
   * Notify the receiver that the position all of currently selected nodes
   * should be adjusted by the relative amounts.
   */
    public void selectionMoved(double x, double y);
}
