package de.consolewars.api.util;

import java.util.Date;

/**
 * date and time operations
 * 
 * @author cerpin (arrewk@gmail.com)
 *
 */
public class DateUtil {

    public static final int SECOND = 1000;

    public static final int MINUTE = SECOND * 60;

    public static final long HOUR = MINUTE * 60;

    public static final long DAY = 24 * HOUR;

    public static final long WEEK = 7 * DAY;

    public static final long MONTH = 30 * DAY;

    /**
	 * relative time passed (current version with german time text)
	 * Example output: vor 34 Minuten (34 minutes ago)
	 * 
	 * @author cerpin (arrewk@gmail.com)
	 * @param date
	 * @return
	 */
    public static String timePassed(Date date) {
        Date now = new Date();
        long secondsPassed = now.getTime() - date.getTime();
        if (secondsPassed > MONTH) return getTimeText(secondsPassed / MONTH, MONTH);
        if (secondsPassed > WEEK) return getTimeText(secondsPassed / WEEK, WEEK);
        if (secondsPassed > DAY) return getTimeText(secondsPassed / DAY, DAY);
        if (secondsPassed > HOUR) return getTimeText(secondsPassed / HOUR, HOUR);
        if (secondsPassed <= HOUR - MINUTE) return getTimeText(secondsPassed / MINUTE, MINUTE);
        return getTimeText(secondsPassed / SECOND, SECOND);
    }

    /**
	 * see timePassed(Date date)
	 * 
	 * @author cerpin (arrewk@gmail.com)
	 * @param unixtime timestamp/unixtime in seconds
	 * @return
	 */
    public static String timePassed(long unixtime) {
        return timePassed(new Date(unixtime * SECOND));
    }

    private static String getTimeText(long time, long period) {
        if (period == MONTH) {
            if (time == 1) return "vor einem Monat"; else return "vor " + time + " Monate";
        } else if (period == WEEK) {
            if (time == 1) return "vor einer Woche"; else return "vor " + time + " Wochen";
        } else if (period == DAY) {
            if (time == 1) return "vor einem Tag"; else return "vor " + time + " Tagen";
        } else if (period == HOUR) {
            if (time == 1) return "vor einer Stunde"; else return "vor " + time + " Stunden";
        } else if (period == MINUTE) {
            if (time == 1) return "vor einer Minute"; else return "vor " + time + " Minuten";
        } else {
            if (time < 30) return "jetzt"; else return "vor " + time + " Sekunden";
        }
    }
}
