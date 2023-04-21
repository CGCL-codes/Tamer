package edu.collablab.brenthecht.wikapidia.sis;

import java.util.HashMap;
import java.util.Vector;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * This class is a convenience class on jts' Point class entirely designed to avoid latitude and longitude confusion by the coder.
 * Because lat is y and is usually specified before x (longitude), this creates a lot of confusion.  This
 * class makes the coder expliclity request lat or lon instead of x or y.
 * @author Brent Hecht
 *
 */
public class EarthPoint extends Point {

    public double getLongitude() {
        return getX();
    }

    public double getLatitude() {
        return getY();
    }

    public EarthPoint(double lat, double lon) {
        super(((new PackedCoordinateSequenceFactory()).create(new Coordinate[] { new Coordinate(lon, lat) })), new GeometryFactory());
    }

    public Vector<Double> getLatLonVector() {
        Vector<Double> rVal = new Vector<Double>();
        rVal.add(getY());
        rVal.add(getX());
        return rVal;
    }

    public Vector<Double> getLonLatVector() {
        Vector<Double> rVal = new Vector<Double>();
        rVal.add(getX());
        rVal.add(getY());
        return rVal;
    }
}
