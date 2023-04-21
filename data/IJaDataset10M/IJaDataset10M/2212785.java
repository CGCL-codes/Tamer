package ch.unibe.inkml;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import ch.unibe.eindermu.euclidian.Segment;
import ch.unibe.inkml.InkChannel.ChannelName;

/**
 * An InkTracePoint represents a sample point of digital Ink.
 * It inherits from Point2D, so it depends on the Channel values X and Y. 
 * 
 * A InkTracePoint gives access to the values of all Channels available.
 * The two main channels X and Y have their own methods.
 * 
 * This class has static method to calculate useful derived values. 
 * 
 * @author emanuel
 */
public abstract class InkTracePoint extends Point2D {

    /**
     * Change the X coordinate to x
     * @param x the new x value
     */
    public void setX(double x) {
        set(ChannelName.X, x);
    }

    /**
     * Change the Y coordinate to y
     * @param y the new y value 
     */
    public void setY(double y) {
        set(ChannelName.Y, y);
    }

    @Override
    public double getX() {
        return get(ChannelName.X);
    }

    @Override
    public double getY() {
        return get(ChannelName.Y);
    }

    @Override
    public void setLocation(double x, double y) {
        setX(x);
        setY(y);
    }

    /**
     * Returns the value of the specified channel in double precision.
     * If the channel has not double precision type then the conversion to
     * double will take place as defined in the channel.
     * @param channel Name of the channel
     * @return
     */
    public abstract double get(ChannelName channel);

    /**
     * Returns the value of the specified channel as object of the type specified by the channel.
     * @param channel Name of the channel
     * @return
     */
    public abstract Object getObject(ChannelName channel);

    /**
     * Sets the value of the specified channel as double value.
     * @param channel Name of the channel
     * @param d value.
     */
    public abstract void set(ChannelName channel, double d);

    /**
     * Sets the value of the specified channel as the object type specified by the named channel.
     * @param channel Name of the channel
     * @param value value.
     */
    public abstract void set(ChannelName channel, Object value);

    /**
     * Returns the distance from point p to the nearest of all points in the list of points given by l 
     * @param l list of points
     * @param p point for which the nearest distance should be given. 
     * @return distance to nearest point in double precision.
     */
    public static double distanceToPoint(Iterable<InkTracePoint> l, Point2D p) {
        double dist = java.lang.Double.MAX_VALUE;
        for (InkTracePoint po : l) {
            double d = po.distance(p);
            if (d < dist) {
                dist = d;
            }
        }
        return dist;
    }

    /**
     * Returns a polygon created from the X and Y Channel of the given list of InkTracePoints 
     * @param l lits of points
     * @return
     */
    public static Polygon getPolygon(Iterable<InkTracePoint> l) {
        Vector<Integer> vxs = new Vector<Integer>();
        Vector<Integer> vys = new Vector<Integer>();
        for (InkTracePoint p : l) {
            vxs.add((int) p.getX());
            vys.add((int) p.getY());
        }
        int[] xs = new int[vxs.size()];
        int[] ys = new int[vys.size()];
        for (int i = 0; i < vxs.size(); i++) {
            xs[i] = vxs.get(i);
            ys[i] = vys.get(i);
        }
        return new Polygon(xs, ys, vxs.size());
    }

    /**
     * Returns the center of gravity of all points in the list of points.
     * @param points
     * @return
     */
    public static Point2D getCenterOfGravity(Iterable<InkTracePoint> points) {
        double x = 0, y = 0, i = 0;
        for (InkTracePoint p : points) {
            x += p.getX();
            y += p.getY();
            i++;
        }
        return new Point2D.Double(x / i, y / i);
    }

    /**
     * Distance between two set of traces. Not only the distance between points are considered,
     * but also the distance between points and connecting lines between subsequental points.
     * and the distance between all connecting lines. So if the two traces cross each other somewhere
     * 0 is returned. 
     * @param points_l
     * @param points_r
     */
    public static double distanceTraceToTrace(Iterable<InkTracePoint> points_l, Iterable<InkTracePoint> points_r) {
        List<Segment> segments_l = new ArrayList<Segment>();
        List<Segment> segments_r = new ArrayList<Segment>();
        InkTracePoint lastPoint_l = null;
        InkTracePoint lastPoint_r = null;
        ;
        for (InkTracePoint point_l : points_l) {
            if (lastPoint_l != null) {
                segments_l.add(new Segment(lastPoint_l, point_l));
            }
            lastPoint_l = point_l;
        }
        for (InkTracePoint point_r : points_r) {
            if (lastPoint_r != null) {
                segments_r.add(new Segment(lastPoint_r, point_r));
            }
            lastPoint_r = point_r;
        }
        assert lastPoint_l != null;
        assert lastPoint_r != null;
        double dist = Integer.MAX_VALUE;
        if (segments_l.size() == 0) {
            if (segments_r.size() == 0) {
                return lastPoint_l.distance(lastPoint_r);
            }
            for (Segment segment_r : segments_r) {
                dist = Math.min(segment_r.ptSegDist(lastPoint_l), dist);
            }
            return dist;
        }
        if (segments_r.size() == 0) {
            for (Segment segment_l : segments_l) {
                dist = Math.min(segment_l.ptSegDist(lastPoint_r), dist);
            }
            return dist;
        }
        for (int l = 0; l < segments_l.size(); l++) {
            for (int r = 0; r < segments_r.size(); r++) {
                dist = Math.min(dist, segments_l.get(l).distance(segments_r.get(r)));
                if (dist < 0.000001) {
                    return dist;
                }
            }
        }
        return dist;
    }
}
