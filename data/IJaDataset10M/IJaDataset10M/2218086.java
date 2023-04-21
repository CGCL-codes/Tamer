package org.sinaxe.graph;

import java.awt.geom.Point2D;
import java.util.List;

public interface GraphEdgeModel extends GraphicObject {

    public int getIntersectingControlPoint(Point2D pt);

    public Point2D[] getControlPoints();

    public void setControlPoints(Point2D[] controlPoints);
}
