package com.eteks.sweethome3d.model;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

/**
 * Observer camera characteristics in home.
 * @author Emmanuel Puybaret
 */
public class ObserverCamera extends Camera implements Selectable {

    private static final long serialVersionUID = 1L;

    private transient Shape shapeCache;

    private transient Shape rectangleShapeCache;

    /**
   * Creates a camera at given location and angle.
   */
    public ObserverCamera(float x, float y, float z, float yaw, float pitch, float fieldOfView) {
        super(x, y, z, yaw, pitch, fieldOfView);
    }

    /**
   * Sets the yaw angle in radians of this camera.
   */
    public void setYaw(float yaw) {
        super.setYaw(yaw);
        this.shapeCache = null;
        this.rectangleShapeCache = null;
    }

    /**
   * Sets the abscissa of this camera.
   */
    public void setX(float x) {
        super.setX(x);
        this.shapeCache = null;
        this.rectangleShapeCache = null;
    }

    /**
   * Sets the ordinate of this camera.
   */
    public void setY(float y) {
        super.setY(y);
        this.shapeCache = null;
        this.rectangleShapeCache = null;
    }

    /**
   * Sets the elevation of this camera.
   */
    public void setZ(float z) {
        super.setZ(z);
        this.shapeCache = null;
        this.rectangleShapeCache = null;
    }

    /**
   * Returns the width of this observer camera according to
   * human proportions with an eyes elevation at z. 
   */
    public float getWidth() {
        float width = getZ() * 4 / 14;
        return Math.min(Math.max(width, 20), 62.5f);
    }

    /**
   * Returns the depth of this observer camera according to
   * human proportions with an eyes elevation at z. 
   */
    public float getDepth() {
        float depth = getZ() * 8 / 70;
        return Math.min(Math.max(depth, 8), 25);
    }

    /**
   * Returns the height of this observer camera according to
   * human proportions with an eyes elevation at z. 
   */
    public float getHeight() {
        return getZ() * 15 / 14;
    }

    /**
   * Returns the points of each corner of the rectangle surrounding this camera.
   * @return an array of the 4 (x,y) coordinates of the camera corners.
   */
    public float[][] getPoints() {
        float[][] piecePoints = new float[4][2];
        PathIterator it = getRectangleShape().getPathIterator(null);
        for (int i = 0; i < piecePoints.length; i++) {
            it.currentSegment(piecePoints[i]);
            it.next();
        }
        return piecePoints;
    }

    /**
   * Returns <code>true</code> if this camera intersects
   * with the horizontal rectangle which opposite corners are at points
   * (<code>x0</code>, <code>y0</code>) and (<code>x1</code>, <code>y1</code>).
   */
    public boolean intersectsRectangle(float x0, float y0, float x1, float y1) {
        Rectangle2D rectangle = new Rectangle2D.Float(x0, y0, 0, 0);
        rectangle.add(x1, y1);
        return getShape().intersects(rectangle);
    }

    /**
   * Returns <code>true</code> if this camera contains 
   * the point at (<code>x</code>, <code>y</code>)
   * with a given <code>margin</code>.
   */
    public boolean containsPoint(float x, float y, float margin) {
        if (margin == 0) {
            return getShape().contains(x, y);
        } else {
            return getShape().intersects(x - margin, y - margin, 2 * margin, 2 * margin);
        }
    }

    /**
   * Returns the ellipse shape matching this camera.
   */
    private Shape getShape() {
        if (this.shapeCache == null) {
            Ellipse2D cameraEllipse = new Ellipse2D.Float(getX() - getWidth() / 2, getY() - getDepth() / 2, getWidth(), getDepth());
            AffineTransform rotation = new AffineTransform();
            rotation.setToRotation(getYaw(), getX(), getY());
            PathIterator it = cameraEllipse.getPathIterator(rotation);
            GeneralPath pieceShape = new GeneralPath();
            pieceShape.append(it, false);
            this.shapeCache = pieceShape;
        }
        return this.shapeCache;
    }

    /**
   * Returns the rectangle shape matching this camera.
   */
    private Shape getRectangleShape() {
        if (this.rectangleShapeCache == null) {
            Rectangle2D cameraRectangle = new Rectangle2D.Float(getX() - getWidth() / 2, getY() - getDepth() / 2, getWidth(), getDepth());
            AffineTransform rotation = new AffineTransform();
            rotation.setToRotation(getYaw(), getX(), getY());
            PathIterator it = cameraRectangle.getPathIterator(rotation);
            GeneralPath cameraRectangleShape = new GeneralPath();
            cameraRectangleShape.append(it, false);
            this.rectangleShapeCache = cameraRectangleShape;
        }
        return this.rectangleShapeCache;
    }

    /**
   * Moves this camera of (<code>dx</code>, <code>dy</code>) units.
   */
    public void move(float dx, float dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    /**
   * Returns a clone of this camera.
   */
    @Override
    public ObserverCamera clone() {
        return new ObserverCamera(getX(), getY(), getZ(), getYaw(), getPitch(), getFieldOfView());
    }
}
