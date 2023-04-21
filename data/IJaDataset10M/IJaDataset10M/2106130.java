package nl.BobbinWork.diagram.model;

import java.awt.geom.Point2D;

/**
 *
 * @author J. Pol
 */
public class Point extends Point2D.Double {

    private static final long serialVersionUID = 1L;

    /** Creates a new instance of Point. 
     * 
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point2D point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    /** Creates a new instance of Point. 
     * 
     * @param p the point to clone.
     */
    public Point(Point p) {
        x = p.x;
        y = p.y;
    }
}
