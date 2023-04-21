package de.nec.nle.siafu.types;

/**
 * This class represents a time period, as given by two <code>EasyTime</code>
 * objects.
 * 
 * @author Miquel Martin
 * 
 */
public class TimePeriod implements Publishable {

    /**
	 * The start of the period.
	 */
    private EasyTime start;

    /**
	 * The end of the period.
	 */
    private EasyTime end;

    /**
	 * Creates a TimePeriod object using the specified begin and end
	 * <code>EasyTime</code> objects.
	 * 
	 * @param start the start of the period
	 * @param end the end of the period
	 */
    public TimePeriod(final EasyTime start, final EasyTime end) {
        this.start = start;
        this.end = end;
    }

    /**
	 * Builds a TimePeriod object by taking a FlatData object of the format
	 * <code>EasyTime:startHH#startMM#endHH#endMM</code>.
	 * 
	 * @param flatData the flattened data with the time period
	 */
    public TimePeriod(final FlatData flatData) {
        final int startHHField = 0;
        final int startMMField = 1;
        final int endHHField = 2;
        final int endMMField = 3;
        String data = flatData.getData();
        TypeUtils.check(this, data);
        String period = data.substring(data.indexOf(':') + 1);
        String[] part = period.split("#");
        this.start = new EasyTime(new Integer(part[startHHField]), new Integer(part[startMMField]));
        this.end = new EasyTime(new Integer(part[endHHField]), new Integer(part[endMMField]));
    }

    /**
	 * Get the <code>EasyTime</code> object that represents the start of
	 * this time period.
	 * 
	 * @return the start of the period in an EasyTime object
	 */
    public EasyTime getStart() {
        return start;
    }

    /**
	 * Get the <code>EasyTime</code> object that represents the end of this
	 * time period.
	 * 
	 * @return the end of the period in an EasyTime object
	 */
    public EasyTime getEnd() {
        return end;
    }

    /**
	 * Returns a representation of the time with the format HH:MM.
	 * 
	 * @return the time string
	 */
    public String toString() {
        return start.toString() + "-" + end.toString();
    }

    /**
	 * Generates a flattened version of the data, of the type
	 * <code>Text:startHour#startMinute#endHour#endMinute</code>.
	 * 
	 * @return a <code>FlatData</code> object representing this object
	 */
    public FlatData flatten() {
        String data;
        data = this.getClass().getSimpleName() + ":";
        data += start.getHour() + "#" + start.getMinute();
        data += "#";
        data += end.getHour() + "#" + end.getMinute();
        return new FlatData(data);
    }
}
