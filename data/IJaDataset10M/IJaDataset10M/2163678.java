package deesel.units;

/**
 * Created by IntelliJ IDEA.
 *
 * @author <a href="mailto:troyhen@comcast.net>Troy Heninger</a> Date: Nov 5,
 *         2004
 */
public final class Month extends TimePeriod {

    public Month(double value) {
        super(new Double(value));
    }

    public Month(long value) {
        super(new Long(value));
    }

    public Month(Number value) {
        super(value);
    }

    public Month(TimePeriod value) {
        super(value.toMonth().getValue());
    }

    public SimpleUnit standardize() {
        return new Second(doubleValue() * MONTH2SEC);
    }
}
