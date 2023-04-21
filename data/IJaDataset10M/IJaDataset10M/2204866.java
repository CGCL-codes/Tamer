package fr.itris.glips.svgeditor.shape.path.segments;

import java.awt.geom.*;

/**
 * the class describing a moveTo segment
 * @author Jordi SUC
 */
public class MoveToSeg extends ActionSeg {

    /**
	 * the point of the segment
	 */
    private Point2D point;

    /**
	 * the constructor of the class
	 * @param point the point of the segment
	 */
    public MoveToSeg(Point2D point) {
        this.point = point;
    }

    @Override
    public Point2D getEndPoint() {
        return point;
    }

    @Override
    public Point2D getControlPoint() {
        return null;
    }
}
