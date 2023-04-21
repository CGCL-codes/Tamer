package vegadataeditor.output.dateFormaters;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author lawinslow
 */
public class JulianDayDateFormat implements IDateFormatter {

    String delimiter;

    Calendar cal = Calendar.getInstance();

    double dayOfYear;

    int dayOfYearInt;

    int hour;

    int minute;

    int second;

    /**
     * This outputs a date format consisting of year and julian day. The julian
     * day is 1 for January 1st and time is expressed as fraction of a day.
     * @param delim Field delimiter to use when delimiter is required.
     */
    public JulianDayDateFormat(String delim) {
        this.delimiter = delim;
    }

    public JulianDayDateFormat() {
        this(",");
    }

    public String format(Date d) {
        cal.setTime(d);
        dayOfYearInt = cal.get(Calendar.DAY_OF_YEAR);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        second = cal.get(Calendar.SECOND);
        dayOfYear = dayOfYearInt + hour / 24.0 + minute / (24 * 60.0) + second / (24 * 60 * 60.0);
        return cal.get(Calendar.YEAR) + delimiter + Double.toString(dayOfYear);
    }

    public String getHeader() {
        return "year" + delimiter + "daynum";
    }
}
