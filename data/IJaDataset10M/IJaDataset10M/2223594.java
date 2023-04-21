package org.kabeja.dxf.helpers;

/**
 * @author <a href="mailto:simon.mieth@gmx.de>Simon Mieth</a>
 *
 */
public class Edge {

    protected Point startPoint = new Point();

    protected Point endPoint = new Point();

    /**
     * @return Returns the endPoint.
     */
    public Point getEndPoint() {
        return endPoint;
    }

    /**
     * @param endPoint The endPoint to set.
     */
    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * @return Returns the startPoint.
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * @param startPoint The startPoint to set.
     */
    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getIntersectionPoint(Edge e) {
        return null;
    }
}
