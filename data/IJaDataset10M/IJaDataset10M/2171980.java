package deesel.units;

/**
 * Created by IntelliJ IDEA.
 *
 * @author <a href="mailto:troyhen@comcast.net>Troy Heninger</a> Date: Nov 5,
 *         2004
 */
public abstract class Distance extends SimpleUnit implements Comparable {

    public static final int KM2M = 1000;

    public static final int MM2M = 1000000;

    public static final int M2CM = 100;

    public static final int M2MM = 1000;

    public static final int M2UM = 1000000;

    protected Distance(Number value) {
        super(value);
    }

    public int compareTo(Object other) {
        SimpleUnit me = standardize();
        SimpleUnit unit = ((SimpleUnit) other).standardize();
        return me.intValue() - unit.intValue();
    }
}
