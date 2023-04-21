package com.horstmann.violet.product.diagram.state;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import com.horstmann.violet.product.diagram.abstracts.node.EllipticalNode;

/**
 * An initial or final node (bull's eye) in a state or activity diagram.
 */
public class CircularInitialStateNode extends EllipticalNode {

    @Override
    public Rectangle2D getBounds() {
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = DEFAULT_DIAMETER;
        double h = DEFAULT_DIAMETER;
        Rectangle2D currentBounds = new Rectangle2D.Double(x, y, w, h);
        Rectangle2D snappedBounds = getGraph().getGrid().snap(currentBounds);
        return snappedBounds;
    }

    public void draw(Graphics2D g2) {
        super.draw(g2);
        Rectangle2D bounds = getBounds();
        Ellipse2D circle = new Ellipse2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        g2.fill(circle);
    }

    /**
     * Kept for compatibility with old versions
     * 
     * @param dummy
     */
    public void setFinal(boolean dummy) {
    }

    /** default node diameter */
    private static int DEFAULT_DIAMETER = 20;
}
