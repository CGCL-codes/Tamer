package net.javlov.world;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class DistanceSensor implements GroupedSensor {

    private int numRays;

    private double rayLength, aperture, interRayAngle;

    private Point2D.Double origin;

    private Path2D.Double rayPath;

    private Body body;

    private World world;

    public DistanceSensor(int numRays, double rayLength, double aperture) {
        origin = new Point2D.Double();
        this.numRays = numRays;
        this.rayLength = rayLength;
        this.aperture = aperture;
        createRays();
    }

    public DistanceSensor(int numRays, double rayLength, double aperture, World world) {
        this(numRays, rayLength, aperture);
        setWorld(world);
    }

    protected void createRays() {
        interRayAngle = aperture / (numRays - 1);
        double angle = aperture / 2;
        rayPath = new Path2D.Double();
        for (int i = 0; i < numRays; i++) {
            rayPath.moveTo(origin.x, origin.y);
            rayPath.lineTo(rayLength * Math.cos(angle), rayLength * Math.sin(angle));
            angle -= interRayAngle;
        }
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /**
	 * Ugly, but quick fix for ignoring own body detection. Could be fixed more nicely if
	 * detection wasn't based on object bounding boxes. Done like this for now for performance
	 * reasons.
	 * @param b
	 */
    public void setBody(Body b) {
        body = b;
    }

    @Override
    public void init() {
    }

    @Override
    public void reset() {
    }

    @Override
    public Shape getRangeArea() {
        return rayPath;
    }

    @Override
    public void rotate(double angle) {
        rayPath.transform(AffineTransform.getRotateInstance(angle, origin.x, origin.y));
    }

    @Override
    public void rotate(double angle, double x, double y) {
        AffineTransform at = AffineTransform.getRotateInstance(angle, x, y);
        at.transform(origin, origin);
        rayPath.transform(at);
    }

    @Override
    public void translate(double dx, double dy) {
        origin.x += dx;
        origin.y += dy;
        rayPath.transform(AffineTransform.getTranslateInstance(dx, dy));
    }

    /**
	 * NOTE: assumes that there is only 1 object at the time intersecting a single ray of
	 * the sensor.
	 */
    @Override
    public double[] getReading() {
        List<Body> objectsInRange = world.getIntersectingObjects(rayPath.getBounds2D());
        if (objectsInRange.size() < 2) return new double[1];
        return getReadingFromObjects(objectsInRange);
    }

    /**
	 * NOTE: assumes that there is only 1 object at the time intersecting a single ray of
	 * the sensor.
	 */
    @Override
    public double[] getReadingFromObjects(List<Body> objectsInRange) {
        double[] rayCoords = new double[6], tempReading = new double[numRays];
        PathIterator rays = rayPath.getPathIterator(null);
        int i = 0;
        while (!rays.isDone()) {
            rays.next();
            rays.currentSegment(rayCoords);
            rays.next();
            for (Body b : objectsInRange) {
                if ((tempReading[i] = getRayReading(rayCoords[0], rayCoords[1], b)) > 0) {
                    i++;
                    break;
                }
            }
        }
        if (numRays > 2) return new double[] { getWeightedAverageReading(tempReading, 1) }; else return new double[] { getAverageReading(tempReading) };
    }

    /**
	 * Compute weighted average of ray readings according to Gaussian distribution of ray
	 * weights. The closer to the sensor axis a ray is, the larger its weight. The shape of
	 * the distribution is governed by {@code gaussianWidth}: smaller gaussianWidth means a
	 * higher peak (larger weight to rays around the centre).
	 * 
	 * Formula is the same as the one used by the Cyberbotics Webots(tm) distance sensors.
	 * 
	 * @param reading the raw readings from each ray
	 * @param gaussianWidth governs the shape of the ray weight distribution
	 * @return weighted average of the readings
	 */
    public double getWeightedAverageReading(double[] reading, double gaussianWidth) {
        double vals[] = new double[numRays], sumVals = 0, avgReading = 0, axisAngle = 0;
        for (int i = 0; i < numRays; i++) {
            axisAngle = aperture / 2 - i * interRayAngle;
            vals[i] = Math.exp(-Math.pow((axisAngle / (aperture * gaussianWidth)), 2));
            sumVals += vals[i];
        }
        if (sumVals == 0) return 0;
        for (int i = 0; i < numRays; i++) {
            avgReading += vals[i] * reading[i] / sumVals;
        }
        return avgReading;
    }

    public double getAverageReading(double[] reading) {
        double sum = 0;
        for (int i = 0; i < numRays; i++) sum += reading[i];
        return sum / numRays;
    }

    /**
	 * Code adapted from Java Rectangle2D.intersectsLine, so that if a ray intersects we
	 * immediately have the point of intersection with the bounding box of the shape (repeat:
	 * the BOUNDING BOX of the shape, so this method is inaccurate for non-rectangular shapes
	 * or rotated rectangular shapes. The level of inaccuracy depends on how different from a
	 * rectangle the shape is).
	 * 
	 * @param x2 x-coord of ray endpoint
	 * @param y2 y-coord of ray endpoint
	 * @param s the shape to check for intersection (only checks bounding box)
	 * @return 1 - d/r, where d is distance between the origin of the ray and the
	 * intersection point, and r is ray length; or 0 if the ray doesn't intersect the 
	 * bounding box of the shape.
	 */
    protected double getRayReading(double x2, double y2, Body b) {
        Rectangle2D bounds = b.getFigure().getBounds2D();
        double x1 = origin.x, y1 = origin.y;
        int out1, out2 = bounds.outcode(x2, y2);
        while ((out1 = bounds.outcode(x1, y1)) != 0) {
            if ((out1 & out2) != 0) {
                return 0;
            }
            if ((out1 & (Rectangle2D.OUT_LEFT | Rectangle2D.OUT_RIGHT)) != 0) {
                double x = bounds.getX();
                if ((out1 & Rectangle2D.OUT_RIGHT) != 0) {
                    x += bounds.getWidth();
                }
                y1 = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                x1 = x;
            } else {
                double y = bounds.getY();
                if ((out1 & Rectangle2D.OUT_BOTTOM) != 0) {
                    y += bounds.getHeight();
                }
                x1 = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                y1 = y;
            }
        }
        double d = Point2D.distance(origin.x, origin.y, x1, y1);
        if (d == 0) {
            Point2D pos = body.getLocation();
            if (pos.getX() <= bounds.getCenterX() + 0.001 && pos.getX() >= bounds.getCenterX() - 0.001 && pos.getY() <= bounds.getCenterY() + 0.001 && pos.getY() >= bounds.getCenterY() - 0.001) {
                return 0;
            }
        }
        return 1 - d / rayLength;
    }

    @Override
    public int getReadingDim() {
        return 1;
    }
}
