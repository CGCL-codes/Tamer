package org.mediavirus.graphl.layout;

import javax.swing.JComponent;
import org.mediavirus.graphl.graph.Node;
import org.mediavirus.graphl.layout.GraphlLayoutStrategy.GraphManager;
import org.mediavirus.graphl.layout.GraphlLayoutStrategy.NodeMovement;
import org.mediavirus.graphl.view.AbstractFacet;

/**
 * @author Flo Ledermann <ledermann@ims.tuwien.ac.at>
 * created: 06.07.2004 16:32:36
 */
public class UnconstrainedNodeLayouter extends AbstractFacet implements NodeLayouter {

    public void performLayoutStep(Node node, GraphManager graphManager) {
        if (!node.isDragging()) {
            NodeMovement movement = graphManager.getNodeMovement(node);
            double dx = movement.dx;
            double dy = movement.dy;
            movement.dx = dx / 2;
            movement.dy = dy / 2;
            double distanceMoved = Math.max(dx, dy);
            double x = node.getCenterX() + Math.max(-30, Math.min(30, dx));
            double y = node.getCenterY() + Math.max(-30, Math.min(30, dy));
            node.setCenter(x, y);
            graphManager.maxMotion = Math.max(distanceMoved, graphManager.maxMotion);
            if (movement.justChanged) {
                if (System.currentTimeMillis() > movement.timeWhenNodeBecomesNormal) movement.justChanged = false;
            }
        } else {
            NodeMovement movement = graphManager.getNodeMovement(node);
            movement.justChanged = true;
            movement.dx = 0;
            movement.dy = 0;
        }
    }

    public boolean hasVisualController() {
        return false;
    }

    public JComponent getVisualController() {
        return null;
    }

    public boolean isSameClass(Object o) {
        return (o instanceof UnconstrainedNodeLayouter);
    }

    public Object clone() {
        return new UnconstrainedNodeLayouter();
    }

    public String getName() {
        return "Unconstrained";
    }

    public String toString() {
        return getName();
    }

    public boolean isDraggable() {
        return true;
    }
}
