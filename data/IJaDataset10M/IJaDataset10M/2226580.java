package org.dinopolis.gpstool.plugin.swissgrid;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import org.dinopolis.gpstool.util.angle.Angle;
import org.dinopolis.gpstool.util.angle.AngleFormat;
import org.dinopolis.gpstool.util.angle.Latitude;
import org.dinopolis.gpstool.util.angle.Longitude;
import com.bbn.openmap.LatLonPoint;
import com.bbn.openmap.proj.Cylindrical;
import com.bbn.openmap.proj.ProjMath;

/**
 * Implements the Flat projection for use of SwissTopo maps. The main methods
 * are <code>forward</code> and <code>inverse</code>.
 * <p>
 * The forward method forwards latitude/longitude to screen coordinates, the
 * inverse method the other way round. Usage:
 * <p>
 * java.awt.Point forward(float latitude, float longitude) <br>
 * java.awt.Point forward(float latitude, float longitude, java.awt.point reuse)
 * <br>
 * java.awt.Point forward(com.bbn.openmap.LatLonPoint point) <br>
 * java.awt.Point forward(com.bbn.openmap.LatLonPoint point, java.awt.Point
 * reuse) <br>
 * com.bbn.openmap.LatLonPoint inverse(int x, int y)<br>
 * com.bbn.openmap.LatLonPoint inverse(int x, int y, com.bbn.openmap.LatLonPoint
 * reuse) <br>
 * com.bbn.openmap.LatLonPoint inverse(java.awt.Point screen_point,
 * com.bbn.openmap.LatLonPoint reuse)
 * 
 * @see com.bbn.openmap.proj.Projection
 * 
 * @author Samuel Benz
 * @version $Revision: 784 $
 */
public class SwissProjection extends Cylindrical {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7855047752252779555L;

    /**
	 * The name.
	 */
    public static final transient String FlatProjectionName = "CH1903";

    /**
	 * The type of projection.
	 */
    public static final transient int FlatProjectionType = 1903;

    LatLonPoint center_;

    float scale_;

    int width_;

    int height_;

    /**
	 * scale / PIXELFACT gives meters per pixel<br>
	 * d_screen [pixel] = d_map [m] * r_map [dpi] * (1/0.0254) in/m <br>
	 * d_map = d_real / scale <br>
	 * PIXELFACT = 72 dpi * 1/0.0254 in/m = 2834.65 pixel/m, <br>
	 * PIXELFACT = 254 dpi * 1/0.0254 in/m = 10000 pixel/m, <br>
	 * PIXELFACT = 508 dpi * 1/0.0254 in/m = 20000 pixel/m, <br>
	 */
    public static final double PIXELFACT = 10000;

    /**
	 * Construct a LLXY projection.
	 * 
	 * @param center
	 *            LatLonPoint center of projection
	 * @param scale
	 *            float scale of projection
	 * @param width
	 *            width of screen
	 * @param height
	 *            height of screen
	 */
    public SwissProjection(LatLonPoint center, float scale, int width, int height) {
        super(center, scale, width, height, FlatProjectionType);
    }

    public SwissProjection(LatLonPoint center, float scale, int width, int height, int type) {
        super(center, scale, width, height, type);
    }

    /**
	 * Return stringified description of this projection.
	 * 
	 * @return String
	 * @see com.bbn.openmap.proj.Projection#getProjectionID
	 */
    public String toString() {
        return "FlatProjection[" + super.toString() + "]";
    }

    /**
	 * Called when some fundamental parameters change.
	 * 
	 * Each projection will decide how to respond to this change. For instance,
	 * they may need to recalculate "constant" paramters used in the forward()
	 * and inverse() calls.
	 */
    protected void computeParameters() {
        super.computeParameters();
    }

    /**
	 * Sets radian latitude to something sane.
	 * 
	 * @param lat
	 *            float latitude in radians
	 * @return float latitude (-PI/2 &lt;= y &lt;= PI/2)
	 * @see com.bbn.openmap.LatLonPoint#normalize_latitude(float)
	 */
    public float normalize_latitude(float lat) {
        if (lat > NORTH_POLE) {
            return NORTH_POLE;
        } else if (lat < SOUTH_POLE) {
            return SOUTH_POLE;
        }
        return lat;
    }

    /**
	 * Sets radian latitude to something sane.
	 * 
	 * @param lat
	 *            float latitude in degrees
	 * @return float latitude (-90 &lt;= y &lt;= 90)
	 * @see com.bbn.openmap.LatLonPoint#normalize_latitude(float)
	 */
    public static double normalize_latitude_degree(double lat) {
        if (lat > 90.0f) {
            return 90.0f;
        } else if (lat < -90.0f) {
            return -90.0f;
        }
        return lat;
    }

    /**
	 * Checks if a LatLonPoint is plot-able.
	 * 
	 * A point is always plot-able in the LLXY projection.
	 * 
	 * @param lat
	 *            float latitude in decimal degrees
	 * @param lon
	 *            float longitude in decimal degrees
	 * @return boolean
	 */
    public boolean isPlotable(float lat, float lon) {
        return true;
    }

    /**
	 * Projects a point from Lat/Lon space to X/Y space.
	 * 
	 * @param pt
	 *            LatLonPoint
	 * @param p
	 *            Point retval
	 * @return Point p
	 */
    public Point forward(LatLonPoint pt, Point p) {
        return forward(pt.getLatitude(), pt.getLongitude(), p, false);
    }

    /**
	 * Forward projects a lat,lon coordinates.
	 * 
	 * @param lat
	 *            raw latitude in decimal degrees
	 * @param lon
	 *            raw longitude in decimal degrees
	 * @param p
	 *            Resulting XY Point
	 * @return Point p
	 */
    public Point forward(float lat, float lon, Point p) {
        return forward(lat, lon, p, false);
    }

    /**
	 * Forward projects lat,lon into XY space and returns a Point.
	 * 
	 * @param lat
	 *            float latitude in radians
	 * @param lon
	 *            float longitude in radians
	 * @param p
	 *            Resulting XY Point
	 * @return Point p
	 */
    public Point forward(float lat, float lon, Point p, boolean isRadian) {
        if (isRadian) {
            lat = ProjMath.radToDeg(normalize_latitude(lat));
            lon = ProjMath.radToDeg(lon);
        }
        Point2D result = forward(getCenter().getLatitude(), getCenter().getLongitude(), lat, lon, getScale());
        result.setLocation(result.getX() + getWidth() / 2, -result.getY() + getHeight() / 2);
        p.setLocation((int) result.getX(), (int) result.getY());
        return (p);
    }

    /**
	 * This method calculates the horizontal and vertical distance of two
	 * locations given in latitude and longitude. The result is in pixels on the
	 * screen. The closer the two locations are, the more accurate is the
	 * result! A kind of flat projection is used. The algorithm is taken from
	 * gpsdrive of Fritz Ganter (http://gpsdrive.kraftvoll.at)
	 * 
	 * @param reference_latitude
	 *            the latitude of the reference location in degrees
	 * @param reference_longitude
	 *            the longitude of the reference location in degrees
	 * @param latitude
	 *            the latitude of the location
	 * @param longitude
	 *            the longitude of the location
	 * @param scale
	 *            the scale to use.
	 * @return a point containing of the horizontal and vertical distance in
	 *         meters.
	 */
    public static Point2D forward(float reference_latitude, float reference_longitude, float latitude, float longitude, float scale) {
        Point2D point = forward(reference_latitude, reference_longitude, latitude, longitude);
        double posx = point.getX();
        double posy = point.getY();
        posx = posx * PIXELFACT / scale;
        posy = posy * PIXELFACT / scale;
        return (new Point2D.Double(posx, posy));
    }

    /**
	 * This method calculates the horizontal and vertical distance of two
	 * locations given in latitude and longitude. The closer the two locations
	 * are, the more accurate is the result! The SwissGrid Projection is used!
	 * 
	 * @param reference_latitude
	 *            the latitude of the reference location in degrees
	 * @param reference_longitude
	 *            the longitude of the reference location in degrees
	 * @param latitude
	 *            the latitude of the location
	 * @param longitude
	 *            the longitude of the location
	 * @return a point containing of the horizontal and vertical distance in
	 *         meters.
	 */
    public static Point2D forward(float reference_latitude, float reference_longitude, float latitude, float longitude) {
        double posx, posy;
        Point2D referencep = ll2lv03(reference_latitude, reference_longitude);
        Point2D p = ll2lv03(latitude, longitude);
        posy = p.getX() - referencep.getX();
        posx = p.getY() - referencep.getY();
        return (new Point2D.Double(posx, posy));
    }

    /**
	 * This method calculates converts WGS84 (ll) <-> SwissGrid (lv03)
	 * this is an approximation (precision +- 1m)
	 * see:
	 * http://www.swisstopo.ch/pub/down/basics/geo/system/ch1903_wgs84_de.pdf
	 * 
	 * @param lat
	 *            the latitude of the reference location in degrees
	 * @param lon
	 *            the longitude of the reference location in degrees
	 * @return a point in Swiss Coordinates
	 */
    public static Point2D ll2lv03(float lat, float lon) {
        double posx, posy;
        float phi = lat * 3600;
        float lambda = lon * 3600;
        double phip = (phi - 169028.66) / 10000;
        double lambdap = (lambda - 26782.5) / 10000;
        posy = ((600072.37) + (211455.93) * lambdap - (10938.51) * lambdap * phip - (0.36) * lambdap * (phip * phip) - (44.54f) * (lambdap * lambdap * lambdap));
        posx = ((200147.07) + (308807.95) * phip + (3745.25) * (lambdap * lambdap) + (76.63) * (phip * phip) - (194.56) * (lambdap * lambdap) * phip + (119.79) * (phip * phip * phip));
        return (new Point2D.Double(posx, posy));
    }

    /**
	 * This method calculates converts SwissGrid (lv03) <-> WGS84 (ll)
	 * this is an approximation (precision +- 1m)
	 * see:
	 * http://www.swisstopo.ch/pub/down/basics/geo/system/ch1903_wgs84_de.pdf
	 * 
	 * @param x
	 *            the x coordinate in Swissgrid
	 * @param y
	 *            the y coordinate in SwissGrid
	 * @return a LatLonPoint in WGS84
	 */
    public static LatLonPoint lv032ll(int x, int y) {
        double lambdap, phip;
        double xp, yp;
        xp = (x - 200000) * 0.000001;
        yp = (y - 600000) * 0.000001;
        lambdap = 2.6779094 + (4.728982 * yp) + (0.791484 * yp * xp) + (0.1306 * yp * (xp * xp)) - (0.0436 * (yp * yp * yp));
        phip = 16.9023892 + (3.238272 * xp) - (0.270978 * (yp * yp)) - (0.002528 * (xp * xp)) - (0.0447 * (yp * yp) * xp) - (0.0140 * (xp * xp * xp));
        LatLonPoint llp = new LatLonPoint((float) phip * 10000 / 3600, (float) lambdap * 10000 / 3600);
        return (llp);
    }

    /**
	 * This method calculates the Map Sheet Number from SwissTopo
	 * see:
	 * http://www.swisstopo.ch/
	 * 
	 * @param x
	 *            the x/y coordinate in Swissgrid Point2D
	 * @param sclae
	 *            an int whit the scale {25|50|100}
	 * @return an int with the map number at the given position
	 */
    public static int lv03tosn(Point2D swiss, int scale) {
        double sheet = 0;
        double northing;
        double easting;
        northing = swiss.getX();
        easting = swiss.getY();
        if (scale == 100) {
            sheet = 25 + 5 * (4 - Math.floor((northing - 62000) / 48000)) + Math.floor((easting - 480000) / 70000);
        } else if (scale == 50) {
            sheet = 200 + 10 * (9 - Math.floor((northing - 62000) / 24000)) + Math.floor((easting - 480000) / 35000);
        } else if (scale == 25) {
            sheet = 600 + 20 * (19 - Math.floor((northing - 302000) / 12000)) + Math.floor((easting - 480000) / 17500);
        }
        return ((int) sheet);
    }

    /**
	 * Inverse project a Point.
	 * 
	 * @param pt
	 *            x,y Point
	 * @param llp
	 *            resulting LatLonPoint
	 * @return LatLonPoint llp
	 */
    public LatLonPoint inverse(Point pt, LatLonPoint llp) {
        return inverse(pt.x, pt.y, llp);
    }

    /**
	 * Inverse project x,y coordinates into a LatLonPoint.
	 * 
	 * @param x
	 *            integer x coordinate
	 * @param y
	 *            integer y coordinate
	 * @param llp
	 *            LatLonPoint
	 * @return LatLonPoint llp
	 * @see com.bbn.openmap.proj.Proj#inverse(Point)
	 */
    public LatLonPoint inverse(int x, int y, LatLonPoint llp) {
        int delta_x = getWidth() / 2 - x;
        int delta_y = -1 * (getHeight() / 2 - y);
        LatLonPoint result = inverse(getCenter().getLatitude(), getCenter().getLongitude(), delta_x, delta_y, getScale());
        llp.setLatLon((float) result.getLatitude(), (float) result.getLongitude());
        return llp;
    }

    /**
	 * Get the name string of the projection.
	 */
    public String getName() {
        return FlatProjectionName;
    }

    /**
	 * This method calculates the longitude and latitude of a point that is
	 * located a given amount of meters in horizontal and vertical away. The
	 * algorithm is taken from gpsdrive of Fritz Ganter
	 * (http://gpsdrive.kraftvoll.at)
	 * 
	 * @param reference_latitude
	 *            the latitude of the reference location
	 * @param reference_longitude
	 *            the longitude of the reference location
	 * @param px
	 *            the horizontal distance in meters
	 * @param py
	 *            the vertical distance in meters
	 * @return a point containing the resulting latitude and longitude.
	 */
    public static LatLonPoint inverse(float reference_latitude, float reference_longitude, double px, double py) {
        Point2D referencep = ll2lv03(reference_latitude, reference_longitude);
        double posx = referencep.getX() - py;
        double posy = referencep.getY() - px;
        LatLonPoint llpout = new LatLonPoint(lv032ll((int) posx, (int) posy));
        return (llpout);
    }

    /**
	 * This method calculates the longitude and latitude of a point that is
	 * located at a given numer of pixels in horizontal and vertical away. The
	 * algorithm is taken from gpsdrive of Fritz Ganter
	 * (http://gpsdrive.kraftvoll.at)
	 * 
	 * @param reference_latitude
	 *            the latitude of the reference location
	 * @param reference_longitude
	 *            the longitude of the reference location
	 * @param delta_x
	 *            the horizontal distance in pixels
	 * @param delta_y
	 *            the vertical distance in pixels
	 * @param scale
	 *            the scale of the image used.
	 * @return a point containing the resulting latitude and longitude.
	 */
    public static LatLonPoint inverse(float reference_latitude, float reference_longitude, double delta_x, double delta_y, double scale) {
        double px = delta_x / PIXELFACT * scale;
        double py = delta_y / PIXELFACT * scale;
        return (inverse(reference_latitude, reference_longitude, px, py));
    }

    /**
	 * Draw the background for the projection.
	 * 
	 * @param g
	 *            Graphics
	 */
    public void drawBackground(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public static void main(String[] args) {
        System.out.println("Swiss Projection LV03:\n");
        if (args.length > 1 && args.length < 3) {
            String northing = args[0];
            String easting = args[1];
            AngleFormat latlon_formatter_ = new AngleFormat("DD�MM'SS\"");
            LatLonPoint inpoint = new LatLonPoint();
            if (easting.matches("^......$") && northing.matches("[0123456789].*")) {
                inpoint.setLatLon(lv032ll(new Integer(northing).intValue(), new Integer(easting).intValue()));
            } else {
                Angle angle = new Angle(easting);
                Longitude lon = new Longitude(angle.degrees());
                Angle angle2 = new Angle(northing);
                Latitude lat = new Latitude(angle2.degrees());
                inpoint.setLatLon((float) lat.degrees(), (float) lon.degrees());
            }
            Point2D outpoint = ll2lv03(inpoint.getLatitude(), inpoint.getLongitude());
            System.out.println("LV03: \t" + (int) outpoint.getX() + " " + (int) outpoint.getY() + " [" + lv03tosn(outpoint, 25) + "/" + lv03tosn(outpoint, 50) + "/" + lv03tosn(outpoint, 100) + "]");
            latlon_formatter_.applyPattern("DD.ddddd�");
            System.out.println("WGS84: \t" + latlon_formatter_.format(inpoint.getLatitude()) + " " + latlon_formatter_.format(inpoint.getLongitude()));
            latlon_formatter_.applyPattern("DD�MM'SS.ss\"");
            System.out.println("WGS84: \t" + latlon_formatter_.format(inpoint.getLatitude()) + " " + latlon_formatter_.format(inpoint.getLongitude()));
        } else {
            System.out.println("Use: SwissProjection northing easting\n");
            return;
        }
    }
}
