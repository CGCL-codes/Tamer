package org.eclipse.gef.util.layout.flexible;

import org.eclipse.draw2d.geometry.Point;

/**
 * The layout constraint used by FlexibleLayout. It specifies where the figure
 * is anchored and its degree of freedom.
 */
public class FlexibleConstraint<X extends Guide, Y extends Guide> {

    public enum Direction {

        FIXED, NEGATIVE, POSITIVE
    }

    /**
	 * The guides to which the figure is attached.
	 */
    protected CoordinatePair<X, Y> anchor;

    /**
	 * The figure's offset from the section point of its guides.
	 */
    protected Point offset;

    /**
	 * The direction in which the figure can move to avoid collisions. Negative
	 * direction means leftwards/upwards, a positive direction means
	 * rightwards/downwards.
	 */
    protected CoordinatePair<Direction, Direction> direction;

    /**
	 * The figure's layer. Only collisions of figures in the same layer are
	 * checked.
	 */
    protected String layer;

    public FlexibleConstraint() {
    }

    public FlexibleConstraint(CoordinatePair<X, Y> anchor, Point offset, CoordinatePair<Direction, Direction> direction, String layer) {
        this.anchor = anchor;
        this.offset = offset;
        this.direction = direction;
        this.layer = layer;
    }

    public CoordinatePair<X, Y> getAnchor() {
        return anchor;
    }

    public void setAnchor(CoordinatePair<X, Y> anchor) {
        this.anchor = anchor;
    }

    public Point getOffset() {
        return offset;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
    }

    public CoordinatePair<Direction, Direction> getDirection() {
        return direction;
    }

    public void setDirection(CoordinatePair<Direction, Direction> direction) {
        this.direction = direction;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }
}
