package org.progeeks.meta.format;

import java.text.*;
import java.util.*;

/**
 *  PropertyFormat implementation that uses a SimpleDateFormat
 *  to convert java.util.Date objects (and their subclasses) to
 *  a consistent date string.  Note that the use of SimpleDateFormat
 *  instances by this class is not synchronized, so users of this
 *  class must synchronize themselves if the DatePropertyFormat will
 *  be accessed by multiple threads.  Defaults to the format:
 *  2001-02-28 13:45:01
 *
 *  @version   $Revision: 1.5 $
 *  @author    Paul Speed
 */
public class DatePropertyFormat extends AbstractPropertyFormat {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private SimpleDateFormat format;

    /**
     *  Creates a DatePropertyFormat using the DEFAULT_FORMAT.
     */
    public DatePropertyFormat() {
        this(DEFAULT_FORMAT);
    }

    /**
     *  Creates a new DatePropertyFormat using the specified SimpleDateFormat
     *  format string.
     */
    public DatePropertyFormat(String formatString) {
        this.format = new SimpleDateFormat(formatString);
    }

    public DatePropertyFormat(String formatString, String timeZoneId) {
        this.format = new SimpleDateFormat(formatString);
        if (timeZoneId != null) {
            this.format.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        }
    }

    /**
     *  Sets the SimpleDateFormat format string for this property format.
     */
    public void setFormatString(String formatString) {
        this.format = new SimpleDateFormat(formatString);
    }

    /**
     *  Returns the SimpleDateFormat format string for this property format.
     */
    public String getFormatString() {
        return (format.toPattern());
    }

    public void setTimeZoneId(String id) {
        format.setTimeZone(TimeZone.getTimeZone(id));
    }

    public String getTimeZoneId() {
        return format.getTimeZone().getID();
    }

    /**
     *  Returns the formatted String for the specified Object.
     */
    public String format(Object obj) {
        if (obj instanceof Date) return (format.format((Date) obj));
        return (String.valueOf(obj));
    }

    /**
     *  Parses the text starting at the specified index and converts
     *  it into an Object of the appropriate type.  If this method is
     *  not supported then an UnsupportedOperationException will be
     *  thrown.
     */
    public Object parseObject(String source, int index) {
        return (format.parse(source, new ParsePosition(index)));
    }

    /**
     * String summary.
     */
    public String toString() {
        return "DatePropertyFormat[formatString=" + getFormatString() + "]";
    }
}
