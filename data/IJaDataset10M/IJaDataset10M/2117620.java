package be.oniryx.lean.utils.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class describes the Belgian official holidays. There are 10 holidays (excluding week-ends).
 * This calendar is not applicable before 1582 (start of the GregorianCalendar) and not before 1830 (Belgian independance)
 */
public class BelgianOfficialHolidayCalendar extends AbstractHolidayCalendar {

    public List<Calendar> getHolidays(int startYear, int endYear) {
        List<Calendar> holidays = new ArrayList<Calendar>();
        Calendar holiday;
        int dayOfWeek;
        for (int year = startYear; year <= endYear; year++) {
            holiday = new ISOCalendar(year, 1 - 1, 1);
            holidays.add(holiday);
            Calendar easter = getEasterDate(year);
            holidays.add(easter);
            holiday = new ISOCalendar();
            holiday.setTime(easter.getTime());
            holiday.add(Calendar.DAY_OF_MONTH, 1);
            holidays.add(holiday);
            holiday = new ISOCalendar(year, 5 - 1, 1);
            holidays.add(holiday);
            holiday = new ISOCalendar();
            holiday.setTime(easter.getTime());
            holiday.add(Calendar.DAY_OF_MONTH, 1);
            dayOfWeek = holiday.get(Calendar.DAY_OF_WEEK);
            while (dayOfWeek != Calendar.THURSDAY) {
                holiday.add(Calendar.DAY_OF_MONTH, 1);
                dayOfWeek = holiday.get(Calendar.DAY_OF_WEEK);
            }
            holiday.add(Calendar.DAY_OF_MONTH, 5 * 7);
            holidays.add(holiday);
            holiday = new ISOCalendar();
            holiday.setTime(easter.getTime());
            holiday.add(Calendar.DAY_OF_MONTH, 1);
            dayOfWeek = holiday.get(Calendar.DAY_OF_WEEK);
            while (dayOfWeek != Calendar.SUNDAY) {
                holiday.add(Calendar.DAY_OF_MONTH, 1);
                dayOfWeek = holiday.get(Calendar.DAY_OF_WEEK);
            }
            holiday.add(Calendar.DAY_OF_MONTH, 6 * 7);
            holidays.add(holiday);
            Calendar mondayAfter = new ISOCalendar();
            mondayAfter.setTime(holiday.getTime());
            mondayAfter.add(Calendar.DAY_OF_MONTH, 1);
            holidays.add(mondayAfter);
            holiday = new ISOCalendar(year, 7 - 1, 21);
            holidays.add(holiday);
            holiday = new ISOCalendar(year, 8 - 1, 15);
            holidays.add(holiday);
            holiday = new ISOCalendar(year, 11 - 1, 1);
            holidays.add(holiday);
            holiday = new ISOCalendar(year, 11 - 1, 11);
            holidays.add(holiday);
            holiday = new ISOCalendar(year, 12 - 1, 25);
            holidays.add(holiday);
        }
        return sortAndRemoveDuplicates(holidays);
    }

    public int getMaximumHolidaysPerYear() {
        return 12;
    }

    private Calendar getEasterDate(int year) throws IllegalArgumentException {
        if (year <= 1582) {
            throw new IllegalArgumentException("Algorithm invalid before April 1583");
        }
        int golden, century, x, z, d, epact, n;
        golden = (year % 19) + 1;
        century = (year / 100) + 1;
        x = (3 * century / 4) - 12;
        z = ((8 * century + 5) / 25) - 5;
        d = (5 * year / 4) - x - 10;
        epact = (11 * golden + 20 + z - x) % 30;
        if ((epact == 25 && golden > 11) || epact == 24) epact++;
        n = 44 - epact;
        n += 30 * (n < 21 ? 1 : 0);
        n += 7 - ((d + n) % 7);
        if (n > 31) return new ISOCalendar(year, 4 - 1, n - 31); else return new ISOCalendar(year, 3 - 1, n);
    }
}
