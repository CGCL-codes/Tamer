package ru.adv.db.filter;

import java.util.*;

/**
 * Фильтр для преобразования текущее время во время указанной TimeZone.<p>
 * in - String TimeZone name exmp: Europe/Moscow<br>
 * out - String with time<p>
 * При преобразовании использует фильтр {@link TimeToStr} и все его параметры приведения
 * даты к строке.
 *
 * @version $Revision: 1.5 $
 * @see Filter
 * @see TimeToStr
 */
public class TZ2Now extends Filter {

    private static Set availableTZ = null;

    private Date testTime = null;

    private void init() {
        if (availableTZ == null) {
            availableTZ = Collections.unmodifiableSet(new HashSet(Arrays.asList(TimeZone.getAvailableIDs())));
        }
    }

    public void setTestTime(Date time) {
        testTime = time;
    }

    public Object perform(Object value, FilterConfig config) throws FilterException {
        if (isNull(value)) {
            return value;
        }
        init();
        String tzString = value.toString();
        TimeZone tz;
        if (availableTZ.contains(tzString)) {
            tz = TimeZone.getTimeZone(tzString);
        } else {
            FilterException fe = new FilterException(FilterException.FILTER_INVALID_TIME_ZONE, "Invalid TimeZone='" + tzString + "'");
            fe.setTimeZone(tzString);
            throw fe;
        }
        Calendar calendar = new GregorianCalendar();
        if (testTime == null) {
            calendar.setTime(new Date());
        } else {
            calendar.setTime(testTime);
        }
        calendar.setTimeZone(tz);
        Filter time2str = new TimeToStr();
        return time2str.perform(calendar, config);
    }
}
