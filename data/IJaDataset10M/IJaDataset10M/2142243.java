package net.sf.synon2hbm.synon.field.base;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import net.sf.synon2hbm.globals.Globals;

/**
 * Base class for all Synon time fields.
 * 
 * @version $Revision: 1.3 $
 */
public abstract class TimeField extends AbstractDateTimeField {

    Time value;

    /**
	 * Null-Constructor.
	 */
    public TimeField() {
        this.value = null;
    }

    /**
	 * Constructor.
	 */
    public TimeField(Time value) {
        this.value = value;
    }

    /**
	 * Constructor using a java.util.Date object.
	 * 
	 * @param time
	 *            the time
	 */
    public TimeField(Date time) {
        this.value = new Time(time.getTime());
    }

    /**
	 * Constructor using a String.
	 * 
	 * @param time
	 *            the time
	 * @throws ParseException
	 */
    public TimeField(String time) throws ParseException {
        setValue(time);
    }

    public final Time getValue() {
        return value;
    }

    public final void setValue(Time value) {
        this.value = value;
    }

    @Override
    public void setValue(String string) throws ParseException {
        DateFormat format = Globals.getTimeFormat();
        java.util.Date date = format.parse(string);
        value = new Time(date.getTime());
    }

    @Override
    public String formattedValue() {
        DateFormat format = Globals.getTimeFormat();
        return format.format(value);
    }

    /**
	 * Convert integer time in the format used for Synon TME fields to a
	 * java.sql.Time.
	 * 
	 * @param value
	 * @return integer time in the format used for Synon TME fields to a
	 *         java.sql.Time.
	 */
    public static final Time valueOf(int value) {
        if (value == 0) return null;
        int s = value % 100;
        int remain = value / 100;
        int m = (remain % 100);
        int h = remain / 100;
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        cal.set(Calendar.SECOND, s);
        return new Time(cal.getTimeInMillis());
    }

    /**
	 * Get integer value for this time field in the format used by Synon TME
	 * fields.
	 * 
	 * @return integer value for this time field in the format used by Synon TME
	 *         fields.
	 */
    public final int intValue() {
        if (value == null) return 0;
        Calendar cal = new GregorianCalendar();
        cal.setTime(value);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        return h * 10000 + m * 100 + s;
    }

    /**
	 * Check if the value of this object matches the supplied TimeCondition.
	 * 
	 * @param condition
	 * @return true if the condition is satisfied.
	 */
    public final boolean is(TimeCondition condition) {
        return condition.check(getValue());
    }

    public final boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof TimeField)) return false;
        TimeField otherTimeField = (TimeField) obj;
        Time thisTime = getValue();
        Time otherTime = otherTimeField.getValue();
        if (thisTime == null && otherTime == null) return true;
        if (thisTime == null) return false;
        if (otherTime == null) return false;
        return thisTime.equals(otherTime);
    }

    public final int hashCode() {
        Time tm = getValue();
        if (tm == null) return 0;
        return tm.hashCode();
    }
}
