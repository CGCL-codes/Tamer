package com.horstmann.violet.product.diagram.activity;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.EllipticalNode;

/**
 * A final node (bull's eye) in an activity diagram.
 */
public class ScenarioEndNode extends EllipticalNode {

    @Override
    public Rectangle2D getBounds() {
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = DEFAULT_DIAMETER + 2 * DEFAULT_GAP;
        double h = DEFAULT_DIAMETER + 2 * DEFAULT_GAP;
        Rectangle2D currentBounds = new Rectangle2D.Double(x, y, w, h);
        Rectangle2D snappedBounds = getGraph().getGrid().snap(currentBounds);
        return snappedBounds;
    }

    @Override
    public boolean addConnection(IEdge e) {
        return e.getEnd() != null && this != e.getEnd();
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        Ellipse2D circle = new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight());
        Rectangle2D bounds = getBounds();
        Ellipse2D inside = new Ellipse2D.Double(bounds.getX() + DEFAULT_GAP, bounds.getY() + DEFAULT_GAP, bounds.getWidth() - 2 * DEFAULT_GAP, bounds.getHeight() - 2 * DEFAULT_GAP);
        g2.fill(inside);
        g2.draw(circle);
    }

    @Override
    public ScenarioEndNode clone() {
        return (ScenarioEndNode) super.clone();
    }

    /** default node diameter */
    private static int DEFAULT_DIAMETER = 14;

    /** default gap between the main circle and the ring for a final node */
    private static int DEFAULT_GAP = 3;
}
