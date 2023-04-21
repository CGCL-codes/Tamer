package org.dcm4che.data;

import java.util.Date;

/**
 *
 * @author  gunter.zeilinger@tiani.com
 * @version 1.0
 */
public interface DateTimeFormat {

    public String formatDate(Date date);

    public String formatTime(Date time);

    public String formatDateTime(Date dateTime);

    public String formatDateRange(Date from, Date to);

    public String formatTimeRange(Date from, Date to);

    public String formatDateTimeRange(Date from, Date to);

    public Date parseDate(String s);

    public Date parseTime(String s);

    public Date parseDateTime(String s);

    public Date parseDateTime(String date, String time);

    public boolean isDateRange(String s);

    public Date[] parseDateRange(String date);

    public Date[] parseTimeRange(String time);

    public Date[] parseDateTimeRange(String datetime);

    public Date[] parseDateTimeRange(String date, String time);
}
