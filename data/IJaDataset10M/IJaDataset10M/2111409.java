package uk.co.weft.fisherman.entities;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import uk.co.weft.dbutil.Context;
import uk.co.weft.dbutil.Contexts;
import uk.co.weft.dbutil.DataStoreException;
import uk.co.weft.dbutil.TableDescriptor;

/**
 * <p>
 * A zone is an area in which fishers are permitted to fish. It is basically
 * thing on which we can hang an ordered sequence of locations which define a
 * clockwise walk around the zone. The idea behind entities is that they
 * wouldn't get instantiated, but it makes sense to instantiate zones so that
 * we can use the awt point and polygon stuff to quickly check for inside
 * zones.
 * </p>
 * 
 * <p>
 * NOTE NOTE NOTE: Parts of this file cribbed from IBM Java 1.5
 * java.awt.Polygon (C) Copyright IBM Corp. 1998, 2005. All Rights Reserved;
 * (C) Copyright Sun Microsystems Inc, 1992, 2004. All rights reserved.
 * </p>
 * 
 * <p>
 * TODO: URGENT! rewrite contains( double, double) (and, if still needed,
 * calculateBounds() to remove dependency on copyright code.
 * </p>
 */
public class Zone extends Entity {

    /** the key field in my table */
    public static final String KEYFN = "zone_id";

    /** the name of my name field */
    public static final String NAMEFN = "zn_name";

    /** the name of my table */
    public static final String TABLENAME = "ZONE";

    /** the name of the link table which links me to location */
    public static final String LOCATIONLINKTABLE = "LN_ZONE_LOCATION";

    /** the name of the link table which links me to licence_type */
    public static final String LICENCETYPELINKTABLE = "LN_ZONE_LICENCE_TYPE";

    /** all zones that have been created */
    static Hashtable instances = new Hashtable();

    /**
	 * a rectangle within which this zone fully fits; specifically, the
	 * smallest possible such rectangle whose vertices are numbered in
	 * integer numbers of degrees latitude and longitude.
	 */
    protected Rectangle bounds = null;

    /** the name of this zone */
    protected String name = null;

    /** the keyfield value of this zone */
    private Integer id;

    /** the licence types permitted to fish in this zone */
    private Vector licencetypes = new Vector();

    /** a vector of the vertices of this zone */
    private Vector polygon = new Vector();

    /**
	 * Create me using this context to interrogate the database. Context is
	 * assumed to contain database authentication tokens, and a value for
	 * KEYFIELD
	 *
	 * @param context a context which contains database authentication tokens,
	 * 		  and a value for KEYFIELD
	 *
	 * @exception DataStoreException if fail to connect to database (e.g. bad
	 * 			  authentication tokens)
	 */
    public Zone(Context context) throws DataStoreException {
        super();
        TableDescriptor.getDescriptor(TABLENAME, KEYFN, context).fetch(context);
        name = context.getValueAsString(NAMEFN);
        id = context.getValueAsInteger(KEYFN);
        Context scratch = new Context();
        scratch.copyDBTokens(context);
        scratch.put(KEYFN, context.get(KEYFN));
        Vector orderby = new Vector();
        orderby.add("Rank");
        Enumeration e = TableDescriptor.getDescriptor(LOCATIONLINKTABLE, null, context).match(scratch, orderby, true).elements();
        while (e.hasMoreElements()) {
            Context row = (Context) e.nextElement();
            row.copyDBTokens(context);
            TableDescriptor.getDescriptor(Location.TABLENAME, Location.KEYFN, row).fetch(row);
            Point p = new Point(row.getValueAsDouble(Location.LONGITUDEFN).doubleValue(), row.getValueAsDouble(Location.LATITUDEFN).doubleValue());
            polygon.add(p);
        }
        calculateBounds(polygon);
        e = TableDescriptor.getDescriptor(LICENCETYPELINKTABLE, null, context).match(scratch, true).elements();
        while (e.hasMoreElements()) {
            Context row = (Context) e.nextElement();
            licencetypes.add(row.getValueAsInteger(LicenceType.KEYFN));
        }
        instances.put(name, this);
        instances.put(id, this);
    }

    /**
	 * get the zone whose KEYFN value or NAMEFN value is contained in this
	 * context. Will fetch the zone currently in the instances cache rather
	 * than from the database unless <samp>refetch</samp> is true. If a KEYFN
	 * value is present in the context and there are appropriate database
	 * authentication tokens in the context then the zone will be refetched
	 * from the database if it isn't already cached or if
	 * <samp>refetch</samp> is true.
	 *
	 * @param context a context containing either a value for KEYFN or a value
	 * 		  for NAMEFN, and (ideally) suitable database authentication
	 * 		  tokens
	 * @param refetch true if the data should be refetched from the database,
	 * 		  false if a cached version is acceptable.
	 */
    public static Zone getZone(Context context, boolean refetch) throws DataStoreException {
        Zone result = null;
        Integer key = context.getValueAsInteger(KEYFN);
        if (refetch) {
            if (key != null) {
                result = (Zone) instances.get(key);
            }
            if (result == null) {
                result = (Zone) instances.get(context.getValueAsString(NAMEFN));
            }
        }
        if ((result == null) && (key != null)) {
            result = new Zone(context);
        }
        return result;
    }

    /**
	 * Returns the bounds of this <code>Polygon</code>.
	 *
	 * @return the bounds of this <code>Polygon</code>.
	 */
    public Rectangle getBounds() {
        if (polygon.size() == 0) {
            return new Rectangle();
        }
        if (bounds == null) {
            calculateBounds(polygon);
        }
        return bounds.getBounds();
    }

    /**
	 * Determines if the specified coordinates are inside this
	 * <code>Polygon</code>.  For the definition of <i>insideness</i>, see
	 * the class comments of {@link Shape}.  TODO: rewrite; this is cribbed.
	 *
	 * @param x the specified x coordinate
	 * @param y the specified y coordinate
	 *
	 * @return <code>true</code> if the <code>Polygon</code> contains the
	 * 		   specified coordinates; <code>false</code> otherwise.
	 */
    public boolean contains(double x, double y) {
        int npoints = polygon.size();
        if ((npoints <= 2) || !getBounds().contains(x, y)) {
            return false;
        }
        int hits = 0;
        double lastx = ((Point) polygon.elementAt(npoints - 1)).getX();
        double lasty = ((Point) polygon.elementAt(npoints - 1)).getY();
        double curx;
        double cury;
        for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
            curx = ((Point) polygon.elementAt(i)).getX();
            cury = ((Point) polygon.elementAt(i)).getY();
            if (cury == lasty) {
                continue;
            }
            double leftx;
            if (curx < lastx) {
                if (x >= lastx) {
                    continue;
                }
                leftx = curx;
            } else {
                if (x >= curx) {
                    continue;
                }
                leftx = lastx;
            }
            double test1;
            double test2;
            if (cury < lasty) {
                if ((y < cury) || (y >= lasty)) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            } else {
                if ((y < lasty) || (y >= cury)) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }
            if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
                hits++;
            }
        }
        return ((hits & 1) != 0);
    }

    /**
	 * Specialisation: return me as name followed by space followed by left
	 * parenthesis followed by ' Licence_Types: ' followed by a space
	 * separated list of permitted licence type numbers followed by '
	 * Boundary: ' followed by a space separated list of longitude,latitude
	 * pairs followed by space followed by right parenthesis
	 *
	 * @return a string formatted as described above
	 *
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        StringBuffer buffy = new StringBuffer(name);
        buffy.append(" ( Licence_Types: ");
        Enumeration e = licencetypes.elements();
        while (e.hasMoreElements()) {
            buffy.append(e.nextElement()).append(' ');
        }
        buffy.append("Boundary: ");
        e = polygon.elements();
        while (e.hasMoreElements()) {
            Point p = (Point) e.nextElement();
            buffy.append(p.getX()).append(',');
            buffy.append(p.getY()).append(' ');
        }
        buffy.append(')');
        return buffy.toString();
    }

    /**
	 * Calculates the bounding box of the points passed to the constructor.
	 * Sets <code>bounds</code> to the result. TODO: maybe rewrite. This is
	 * cribbed, but it's a sufficiently obvious algorithm that I don't feel
	 * too bad about that, and in any case it's substantially altered.
	 *
	 * @param xpoints[] array of <i>x</i> coordinates
	 * @param ypoints[] array of <i>y</i> coordinates
	 * @param npoints the total number of points
	 */
    private void calculateBounds(Vector polygon) {
        double boundsMinX = Double.MAX_VALUE;
        double boundsMinY = Double.MAX_VALUE;
        double boundsMaxX = Double.MIN_VALUE;
        double boundsMaxY = Double.MIN_VALUE;
        for (int i = 0; i < polygon.size(); i++) {
            double x = ((Point) polygon.elementAt(i)).getX();
            boundsMinX = Math.min(boundsMinX, x);
            boundsMaxX = Math.max(boundsMaxX, x);
            double y = ((Point) polygon.elementAt(i)).getY();
            boundsMinY = Math.min(boundsMinY, y);
            boundsMaxY = Math.max(boundsMaxY, y);
        }
        bounds = new Rectangle((int) Math.floor(boundsMinX), (int) Math.floor(boundsMinY), (int) Math.ceil(boundsMaxX - boundsMinX), (int) Math.ceil(boundsMaxY - boundsMinY));
    }

    /**
	 * java.awt.Point has methods double getX() and double getY(), but in
	 * practice they return integer values. We need real floating point
	 * values, and double precision at that.
	 */
    protected class Point {

        double x;

        double y;

        /**
		 * @param x
		 * @param y
		 */
        public Point(double x, double y) {
            super();
            this.x = x;
            this.y = y;
        }

        /**
		 * @return Returns the x.
		 */
        public double getX() {
            return x;
        }

        /**
		 * @return Returns the y.
		 */
        public double getY() {
            return y;
        }
    }
}
