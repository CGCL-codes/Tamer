package de.ulrich_fuchs.jtypeset;

/**
 *
 * @author  ulrich
 */
public class FloatPlacementData {

    public double x;

    public double y;

    public double width;

    public double height;

    protected Splitpoint fromSp;

    protected Splitpoint toSp;

    /** Creates a new instance of FloatPlacementData */
    public FloatPlacementData(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fromSp = null;
        this.toSp = null;
    }

    public FloatPlacementData(double x, double y, double width, double height, Splitpoint fromSp, Splitpoint toSp) {
        this(x, y, width, height);
        this.fromSp = fromSp;
        this.toSp = toSp;
    }

    public String toString() {
        return "float placement [" + x + ", " + y + ", dimensions " + width + " x " + height + "]";
    }

    /**
	 * @return Returns the fromSp.
	 */
    public final Splitpoint getFromSp() {
        return fromSp;
    }

    /**
	 * @return Returns the height.
	 */
    public final double getHeight() {
        return height;
    }

    /**
	 * @return Returns the toSp.
	 */
    public final Splitpoint getToSp() {
        return toSp;
    }

    /**
	 * @return Returns the width.
	 */
    public final double getWidth() {
        return width;
    }

    /**
	 * @return Returns the x.
	 */
    public final double getX() {
        return x;
    }

    /**
	 * @return Returns the y.
	 */
    public final double getY() {
        return y;
    }
}
