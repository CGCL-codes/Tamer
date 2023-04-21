package ro.mosc.reco.glyphdesciption;

import ro.mosc.reco.algebra.RelationResolver;
import java.awt.*;

/**
 * Class determinatin characteristic predicate value of the relation
 * of a Point relative to a Position.
 */
public class PointRelativePositionRelationResolver implements RelationResolver {

    public enum Position {

        TOP_LEFT, TOP_MIDDLE, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_MIDDLE, MIDDLE_RIGHT, BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT
    }

    private Position position = null;

    private Rectangle glyphContainerRectangle = null;

    /**
     *
     * @param position
     * @param glyphContainerRectangle
     */
    public PointRelativePositionRelationResolver(Position position, Rectangle glyphContainerRectangle) {
        this.position = position;
        this.glyphContainerRectangle = glyphContainerRectangle;
    }

    /**
     *
     * @param elements
     * @return
     */
    public double getRelationValue(Object[] elements) {
        double maxDist = Math.sqrt(Math.pow(glyphContainerRectangle.getWidth(), 2) + Math.pow(glyphContainerRectangle.getHeight(), 2));
        int centroidPointX = -1;
        int centroidPointY = -1;
        if (position == Position.TOP_LEFT) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 1 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 1 / 6);
        }
        if (position == Position.TOP_MIDDLE) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 3 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 1 / 6);
        }
        if (position == Position.TOP_RIGHT) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 5 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 1 / 6);
        }
        if (position == Position.MIDDLE_LEFT) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 1 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 3 / 6);
        }
        if (position == Position.MIDDLE_MIDDLE) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 3 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 3 / 6);
        }
        if (position == Position.MIDDLE_RIGHT) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 5 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 3 / 6);
        }
        if (position == Position.BOTTOM_LEFT) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 1 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 5 / 6);
        }
        if (position == Position.BOTTOM_MIDDLE) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 3 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 5 / 6);
        }
        if (position == Position.BOTTOM_RIGHT) {
            centroidPointX = (int) (glyphContainerRectangle.getWidth() * 5 / 6);
            centroidPointY = (int) (glyphContainerRectangle.getHeight() * 5 / 6);
        }
        Point centroidPoint = new Point(centroidPointX, centroidPointY);
        return 1 - (((Point) elements[0]).distance(centroidPoint) / maxDist);
    }
}
