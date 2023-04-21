package edu.uwa.aidan.robot.world;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A <code>RangeSensor</code> is a simple sensor that measures distance from the <code>Robot</code>s
 * current position to the nearest <code>Obstacle</code>. A <code>RangeSensor</code> is a simple, 
 * straight-line distance measuring sensor. 
 *
 * A <code>RangeSensor</code> is blocked by <code>Obstacle</code>s, and therefore the distance returned
 * is the nearest <code>Obstacle</code> encountered by tracing a straight line from the <code>Robot</code>s
 * current position for a distance of <code>SENSOR_RANGE</code>. 
 * 
 * @author Aidan Morgan
 */
public class RangeSensor {

    /**
	 * The range of the sensor.
	 */
    public static final double SENSOR_RANGE = 200.0;

    /**
	 * The <code>Robot</code> that owns this <code>RangeSensor</code>.
	 */
    private Robot agent;

    /**
	 * The offset angle from the <code>Robot</code>'s "north" that this sensor is set at.
	 */
    private double angle;

    /**
	 * Constructor.
	 * 
	 * @param agent the <code>Robot</code> that owns this <code>RangeSensor</code>.
	 * @param angle the offset angle from the <code>Robot</code>'s "north" that this sensor is set at.
	 */
    public RangeSensor(Robot agent, double angle) {
        this.agent = agent;
        this.angle = angle;
    }

    /**
	 * Returns the java <code>Shape</code> representation of this <code>RangeSensor</code> that is used 
	 * for all geometric operations.
	 * @return the java <code>Shape</code> representation of this <code>RangeSensor</code> that is used 
	 * for all geometric operations.
	 */
    public Line2D getShape() {
        Line2D.Double line = new Line2D.Double(agent.getPosition(), getEndpoint());
        return line;
    }

    /**
	 * Returns the x coordinate of the endpoint of the range sensor, given the <code>Robot</code>'s current
	 * rotation. 
	 * 
	 * @return the x coordinate of the endpoint of the range sensor, given the <code>Robot</code>'s current
	 * rotation.
	 */
    public double getX() {
        return (SENSOR_RANGE * Math.cos(getSensorAngle())) + agent.getPosition().getX();
    }

    /**
	 * Returns the y coordinate of the endpoint of the range sensor, given the <code>Robot</code>'s current
	 * rotation. 
	 * 
	 * @return the y coordinate of the endpoint of the range sensor, given the <code>Robot</code>'s current
	 * rotation.
	 */
    public double getY() {
        return (SENSOR_RANGE * Math.sin(getSensorAngle())) + agent.getPosition().getY();
    }

    /**
	 * Returns the <code>Point2D</code> of the endpoint of the range sensor, given the <code>Robot</code>'s 
	 * current rotation. 
	 * 
	 * @return the <code>Point2D</code> of the endpoint of the range sensor, given the <code>Robot</code>'s current
	 * rotation.
	 */
    public Point2D getEndpoint() {
        return new Point2D.Double(getX(), getY());
    }

    /**
	 * Returns the <code>Point2D</code> at which this <code>RangeSensor</code> has intersected the
	 * nearest <code>Obstacle</code>.
	 * 
	 * @return the <code>Point2D</code> at which this <code>RangeSensor</code> has intersected the
	 * nearest <code>Obstacle</code>.
	 */
    public Point2D getNearestObstacleIntersection() {
        List<Point2D> obstacleIntersections = getObstacleIntersections();
        Point2D closest = null;
        Point2D agentPos = agent.getPosition();
        for (Point2D v : obstacleIntersections) {
            if (closest == null) {
                closest = v;
            } else {
                if (v != null) {
                    double best = closest.distance(agentPos);
                    double proposed = v.distance(agentPos);
                    if (proposed < best) {
                        closest = v;
                    }
                }
            }
        }
        return closest;
    }

    /**
	 * Returns a <code>List</code> of all <code>Point2D</code>'s that represent all intersections of the
	 * <code>RangeSensor</code> with the <code>Obstacles</code>.
	 *
	 * This <code>List</code> contains all intersections between the <code>Robot</code>'s current position
	 * and the endpoint of the sensor.
	 * 
	 * @return a <code>List</code> of all <code>Point2D</code>'s that represent all intersections of the
	 * <code>RangeSensor</code> with the <code>Obstacles</code>.
	 */
    public List<Point2D> getObstacleIntersections() {
        Line2D l = new Line2D.Double(agent.getPosition(), getEndpoint());
        List<Point2D> intersections = new ArrayList<Point2D>();
        for (Obstacle s : agent.getWorld().getObstacles()) {
            Line2D obs = s.getShape();
            Point2D intersection = findIntersection(l, obs);
            if (intersection != null) {
                intersections.add(intersection);
            }
        }
        for (Line2D wall : agent.getWorld().getBoundary()) {
            Point2D intersection = findIntersection(l, wall);
            if (intersection != null) {
                intersections.add(intersection);
            }
        }
        return intersections;
    }

    private Point2D findIntersection(Line2D one, Line2D two) {
        Point2D p1 = one.getP1();
        Point2D p2 = one.getP2();
        Point2D p3 = two.getP1();
        Point2D p4 = two.getP2();
        double xD1, yD1, xD2, yD2, xD3, yD3;
        double dot, deg, len1, len2;
        double segmentLen1, segmentLen2;
        double ua, ub, div;
        xD1 = p2.getX() - p1.getX();
        xD2 = p4.getX() - p3.getX();
        yD1 = p2.getY() - p1.getY();
        yD2 = p4.getY() - p3.getY();
        xD3 = p1.getX() - p3.getX();
        yD3 = p1.getY() - p3.getY();
        len1 = Math.sqrt(xD1 * xD1 + yD1 * yD1);
        len2 = Math.sqrt(xD2 * xD2 + yD2 * yD2);
        dot = (xD1 * xD2 + yD1 * yD2);
        deg = dot / (len1 * len2);
        if (Math.abs(deg) == 1) return null;
        Point2D.Double pt = new Point2D.Double(0.0, 0.0);
        div = yD2 * xD1 - xD2 * yD1;
        ua = (xD2 * yD3 - yD2 * xD3) / div;
        ub = (xD1 * yD3 - yD1 * xD3) / div;
        pt.x = (p1.getX() + ua * xD1);
        pt.y = (p1.getY() + ua * yD1);
        xD1 = (pt.x - p1.getX());
        xD2 = (pt.x - p2.getX());
        yD1 = (pt.y - p1.getY());
        yD2 = (pt.y - p2.getY());
        segmentLen1 = Math.sqrt(xD1 * xD1 + yD1 * yD1) + Math.sqrt(xD2 * xD2 + yD2 * yD2);
        xD1 = (pt.x - p3.getX());
        xD2 = (pt.x - p4.getX());
        yD1 = (pt.y - p3.getY());
        yD2 = (pt.y - p4.getY());
        segmentLen2 = Math.sqrt(xD1 * xD1 + yD1 * yD1) + Math.sqrt(xD2 * xD2 + yD2 * yD2);
        if (Math.abs(len1 - segmentLen1) > 0.01 || Math.abs(len2 - segmentLen2) > 0.01) return null;
        return pt;
    }

    /**
	 * Used to render this <code>RangeSensor</code> to the <code>Graphics2D</code> instance, using the
	 * provided aspect ratio.
	 * 
	 * @param g2d the <code>Graphics2D</code> instance for rendering with,
	 * @param pixelRatio the aspect ratio to use for rendering.
	 */
    public void paint(Graphics2D g, double pixelRatio) {
        Composite lastComp = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.setColor(Color.RED);
        Point2D inters = getNearestObstacleIntersection();
        Point2D center = new Point2D.Double(agent.getShape().getCenterX(), agent.getShape().getCenterY());
        Point2D endpoint = getEndpoint();
        g.draw(ShapeHelper.getLine(new Line2D.Double(center, inters), pixelRatio));
        if (inters != null) {
            Point2D p = ShapeHelper.getPoint(inters, pixelRatio);
            g.fillRect((int) p.getX() - 2, (int) p.getY() - 2, 4, 4);
        }
        g.setComposite(lastComp);
    }

    /**
	 * Returns the distance from the <code>Robot</code>'s current position to the nearest 
	 * <code>Obstacle</code> found by this <codE>RangeSensor</code>.
	 * 
	 * @return the distance from the <code>Robot</code>'s current position to the nearest 
	 * <code>Obstacle</code> found by this <codE>RangeSensor</code>.
	 */
    public double getDistanceToNearestObstacle() {
        Point2D near = getNearestObstacleIntersection();
        if (near != null) {
            return agent.getPosition().distance(near);
        }
        return java.lang.Double.MAX_VALUE;
    }

    /**
	 * Returns the angle, relative to the <code>Robot</code>'s north that this <code>RangeSensor</code>
	 * is set at.
	 * 
	 * @return the angle, relative to the <code>Robot</code>'s north that this <code>RangeSensor</code>
	 * is set at.
	 */
    double getAngle() {
        return angle;
    }

    /**
	 * Returns the actual angle of this <code>RangeSensor</code> by combining the angle of the
	 * sensor with the <code>Robot</code>'s current rotation.
	 * @return the actual angle of this <code>RangeSensor</code> by combining the angle of the
	 * sensor with the <code>Robot</code>'s current rotation.
	 */
    double getSensorAngle() {
        return agent.getRotation() + angle;
    }

    /**
	 * Returns the range of the sensor in pixels.
	 * @return the range of the sensor in pixels.
	 */
    public double getSensorRange() {
        return SENSOR_RANGE;
    }
}
