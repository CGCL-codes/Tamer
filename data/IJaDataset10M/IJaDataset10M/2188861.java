package edu.washington.mysms.server.sample.starbus;

/**
 * A representation of a GPS point in some order series of points.
 * This index is a zero-based index representing this point's
 * position in that series, but no attempt is made at guaranteeing
 * that the stored index corresponds to any actual index in
 * the containing series.
 * 
 * @author Anthony Poon
 */
public class IndexedGPSPoint extends GPSPoint {

    private static final long serialVersionUID = -8620110723686407990L;

    private int index;

    public IndexedGPSPoint(int index, GPSPoint p) {
        super(p.getLatitude(), p.getLongitude(), p.getTime());
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return "#" + this.index + ": " + super.toString();
    }
}
