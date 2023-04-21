package royere.cwi.layout;

import royere.cwi.util.Vector2D;
import royere.cwi.util.Vector3D;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * EnclosingBox information about the domain and extent of the layout
 */
public class EnclosingBox {

    /** Debug object.  Logs data to various channels. */
    private static Logger logger = Logger.getLogger("royere.cwi.layout.EnclosingBox");

    /** Percentage of the box that should be an empty margin */
    protected static double marginPercentage = 15.0;

    public double getMarginPercentage() {
        return EnclosingBox.marginPercentage;
    }

    public void setMarginPercentage(double marginPercentage) {
        EnclosingBox.marginPercentage = marginPercentage;
    }

    /** Formatter for toString() */
    protected static DecimalFormat df = null;

    protected static NumberFormat nf = NumberFormat.getInstance(Locale.US);

    static {
        try {
            df = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);
        } catch (ClassCastException cce) {
            System.err.println(cce);
            DecimalFormat df = new DecimalFormat();
        }
    }

    private int dimension;

    private double xmin, xmax, ymin, ymax, zmin, zmax;

    public double getXMargin() {
        return (xmax - xmin) * marginPercentage / 2.0 / 100.0;
    }

    public double getYMargin() {
        return (ymax - ymin) * marginPercentage / 2.0 / 100.0;
    }

    public double getZMargin() {
        return (zmax - zmin) * marginPercentage / 2.0 / 100.0;
    }

    public double getXmin() {
        return xmin - getXMargin();
    }

    public double getXmax() {
        return xmax + getXMargin();
    }

    public double getYmin() {
        return ymin - getYMargin();
    }

    public double getYmax() {
        return ymax + getYMargin();
    }

    public double getZmin() {
        return zmin - getZMargin();
    }

    public double getZmax() {
        return zmax + getZMargin();
    }

    public Vector3D getMin() {
        return new Vector3D(getXmin(), getYmin(), getZmin());
    }

    public Vector3D getMax() {
        return new Vector3D(getXmax(), getYmax(), getZmax());
    }

    public double getXsize() {
        return getXmax() - getXmin();
    }

    public double getYsize() {
        return getYmax() - getYmin();
    }

    public double getZsize() {
        return getZmax() - getZmin();
    }

    public double getArea() {
        return getXsize() * getYsize();
    }

    public boolean isFinite() {
        return (xmin < Double.MAX_VALUE && ymin < Double.MAX_VALUE && xmax > -Double.MAX_VALUE && ymax > -Double.MAX_VALUE);
    }

    public EnclosingBox() {
        xmin = Double.MAX_VALUE;
        ymin = Double.MAX_VALUE;
        xmax = -Double.MAX_VALUE;
        ymax = -Double.MAX_VALUE;
        zmin = Double.MAX_VALUE;
        zmax = -Double.MAX_VALUE;
    }

    /**
   * Initialize the box giving it a single 2D point.
   */
    public EnclosingBox(Vector2D v) {
        dimension = 2;
        xmin = xmax = v.getX();
        ymin = ymax = v.getY();
    }

    /**
   * Initialize the box giving it two 2D points.
   */
    public EnclosingBox(Vector2D v, Vector2D w) {
        dimension = 2;
        xmin = xmax = v.getX();
        ymin = ymax = v.getY();
        update(w);
    }

    /**
   * Initialize the box giving it a single 3D point.
   */
    public EnclosingBox(Vector3D v) {
        dimension = 3;
        xmin = xmax = v.getX();
        ymin = ymax = v.getY();
        zmin = zmax = v.getZ();
    }

    /**
   * Initialize the box giving it two 3D points.
   */
    public EnclosingBox(Vector3D v, Vector3D w) {
        dimension = 3;
        xmin = xmax = v.getX();
        ymin = ymax = v.getY();
        zmin = zmax = v.getZ();
        update(w);
    }

    public EnclosingBox(double x, double y) {
        dimension = 2;
        xmin = xmax = x;
        ymin = ymax = y;
    }

    public EnclosingBox(double x, double y, double z) {
        dimension = 3;
        xmin = xmax = x;
        ymin = ymax = y;
        zmin = zmax = z;
    }

    public static EnclosingBox getUnitSquare(double x, double y) {
        EnclosingBox eb = new EnclosingBox(x, y);
        eb.update(x - 0.5, y - 0.5);
        eb.update(x + 0.5, y + 0.5);
        return eb;
    }

    public static EnclosingBox getUnitSquare() {
        return EnclosingBox.getUnitSquare(0.0, 0.0);
    }

    /**
   * Update min and max values but in 2D only
   *
   * @param x x value to compare with
   * @param y y value to compare with
   */
    public void update(double x, double y) {
        logger.log(Priority.DEBUG, "update(" + x + ", " + y + "): against [" + this.hashCode() + "]");
        if (x < xmin) {
            xmin = x;
        }
        if (x > xmax) {
            xmax = x;
        }
        if (y < ymin) {
            ymin = y;
        }
        if (y > ymax) {
            ymax = y;
        }
    }

    /**
   * Update min and max values.
   *
   * @param x x value to compare with
   * @param y y value to compare with
   * @param z z value to compare with
   */
    public void update(double x, double y, double z) {
        if (z < zmin) {
            zmin = z;
        }
        if (z > zmax) {
            zmax = z;
        }
        update(x, y);
    }

    /**
   * Update min and max values with a 2D point.
   *
   * @param p point to compare with
   */
    public void update(Vector2D p) {
        update(p.getX(), p.getY());
    }

    /**
   * Update min and max values with a 2D point.
   *
   * @param p point to compare with
   */
    public void update(Vector3D p) {
        update(p.getX(), p.getY(), p.getZ());
    }

    public void update(Coordinates c) {
        if (c == null) {
            logger.log(Priority.ERROR, "update(Coordinates): Coordinates object is null");
            return;
        }
        int dim = c.getDimensions();
        if (dim == 2) {
            update(c.getX(), c.getY());
        } else {
            update(c.getX(), c.getY(), c.getZ());
        }
    }

    public Object clone() {
        EnclosingBox eb = new EnclosingBox();
        if (dimension == 2) {
            eb.update(xmin, ymin);
            eb.update(xmax, ymax);
        } else {
            eb.update(xmin, ymin, zmin);
            eb.update(xmax, ymax, zmax);
        }
        return eb;
    }

    /**
   * Stretch the box to include the given coordinates.
   */
    public void update(Coordinates c, NodeSize nodeSize) {
        update(c.getX() + nodeSize.getWidth() / 2.0, c.getY() + nodeSize.getHeight() / 2.0);
        update(c.getX() - nodeSize.getWidth() / 2.0, c.getY() - nodeSize.getHeight() / 2.0);
    }

    /**
   * Output raw coordinates in the form
   * <tt>(<em>xmin xmax ymin ymax zmin zmax</em>)</tt>
   * <p>
   * Typically used only for debugging.
   */
    public String toStringRaw() {
        StringBuffer b = new StringBuffer();
        b.append("(");
        b.append(xmin + " " + xmax + " ");
        b.append(ymin + " " + ymax + " ");
        b.append(zmin + " " + zmax + ")");
        return b.toString();
    }

    /**
   * Output formatted coordinates in the form
   * <tt>(<em>xmin xmax ymin ymax zmin zmax</em>)</tt>
   */
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("(");
        b.append(format(xmin) + " " + format(xmax) + " ");
        b.append(format(ymin) + " " + format(ymax) + " ");
        b.append(format(zmin) + " " + format(zmax) + ")");
        return b.toString();
    }

    public String format(double d) {
        if (d <= -1000000.0) {
            return "MIN";
        }
        if (d >= 1000000.0) {
            return "MAX";
        }
        return df.format(d);
    }

    public static Double getDouble(StringTokenizer st) {
        if (st.hasMoreTokens()) {
            String dStr = st.nextToken();
            if ("MIN".equals(dStr)) {
                return new Double(Double.MIN_VALUE);
            }
            if ("MAX".equals(dStr)) {
                return new Double(Double.MAX_VALUE);
            }
            try {
                Number num = nf.parse(dStr);
                return new Double(num.doubleValue());
            } catch (ParseException pe) {
                logger.error("EnclosingBox.getDouble(): Couldn't parse '" + dStr + "'", pe);
                return null;
            } catch (Exception e) {
                logger.error("EnclosingBox.getDouble(): Error interpreting '" + dStr + "'", e);
                return null;
            }
        }
        return null;
    }

    public static EnclosingBox fromString(String ebStr) {
        if (ebStr == null || !ebStr.startsWith("(") || !ebStr.endsWith(")")) {
            return null;
        }
        ebStr = ebStr.substring(1, ebStr.length() - 1);
        StringTokenizer st = new StringTokenizer(ebStr, " ");
        Double xmin = getDouble(st);
        if (xmin == null) {
            return null;
        }
        Double xmax = getDouble(st);
        if (xmax == null) {
            return null;
        }
        Double ymin = getDouble(st);
        if (ymin == null) {
            return null;
        }
        Double ymax = getDouble(st);
        if (ymax == null) {
            return null;
        }
        Double zmin = getDouble(st);
        if (zmin == null) {
            return null;
        }
        Double zmax = getDouble(st);
        if (zmax == null) {
            return null;
        }
        EnclosingBox retval = new EnclosingBox();
        retval.update(xmin.doubleValue(), ymin.doubleValue(), zmin.doubleValue());
        retval.update(xmax.doubleValue(), ymax.doubleValue(), zmax.doubleValue());
        return retval;
    }
}
