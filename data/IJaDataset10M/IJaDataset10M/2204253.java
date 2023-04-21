package gcr.mmm2.util;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author sbfisher
 * This class is a point class that compares with 
 * distance from an origin point to see which are closer
 * and which are further.
 * 
 * Short for Comparable Distance Point
 * 
 * Also have many methods for getting the point back out -- whether
 * for the center of a box, top left corner of a box, or the top
 * left corner of a photo that should be contained in a box.
 */
public class CompDistPoint extends java.awt.Point implements Comparable {

    private Point origin;

    private Rectangle boxSize;

    private Rectangle photoSize;

    /**
     * creates a new point for placing a picture around a point.  The main
     * point is to compare the distance from an origin point so we can arrange
     * things by distance from origin from other class.
     * 
     * origin is the origin.
     * topLelftCornerPoint sets the top left corner
     * boxSize is the containing box that is placed
     * photoSize should be smaller than the box and fit inside the 
     * bounding rectangle.
     * 
     * @param origin
     * @param topLeftCornerPoint
     * @param boxSize
     * @param photoSize
     */
    public CompDistPoint(Point origin, Point topLeftCornerPoint, Rectangle boxSize, Rectangle photoSize) {
        this.origin = origin;
        this.boxSize = boxSize;
        int centerX = (int) Math.round(topLeftCornerPoint.getX() + (boxSize.getWidth() / 2));
        int centerY = (int) Math.round(topLeftCornerPoint.getY() + (boxSize.getHeight() / 2));
        this.setLocation(new Point(centerX, centerY));
        this.photoSize = photoSize;
    }

    /**
     * @return Returns the boxSize.
     */
    public Rectangle getBoxSize() {
        return boxSize;
    }

    /**
     * @return Returns the origin.
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * @return Returns the center point.
     */
    public Point getCenterPoint() {
        return this.getLocation();
    }

    /**
     * @return Returns the top/left corner point of the containing box.
     */
    public Point getBoxTopLeftCornerPoint() {
        int left = (int) Math.round(this.getLocation().getX() - (boxSize.getWidth() / 2));
        int top = (int) Math.round(this.getLocation().getY() - (boxSize.getHeight() / 2));
        return new Point(left, top);
    }

    /**
     * @return Returns the top/left corner point of the Photo.
     */
    public Point getPhotoTopLeftCornerPoint() {
        int left = (int) Math.round(this.getLocation().getX() - (photoSize.getWidth() / 2));
        int top = (int) Math.round(this.getLocation().getY() - (photoSize.getHeight() / 2));
        return new Point(left, top);
    }

    public int compareTo(Object o) {
        if (o.getClass() != this.getClass()) {
            IllegalArgumentException iae = new IllegalArgumentException();
            throw iae;
        }
        CompDistPoint otherCDP = (CompDistPoint) o;
        double otherDistance = otherCDP.distanceSq(otherCDP.getOrigin());
        double myDistance = this.distanceSq(this.getOrigin());
        if (myDistance > otherDistance) return 1; else if (myDistance < otherDistance) return -1; else return 0;
    }
}
