package de.jollyday.parser.impl;

import java.util.Set;
import org.joda.time.LocalDate;
import de.jollyday.config.FixedMovingOnWeekend;
import de.jollyday.config.Holidays;
import de.jollyday.parser.AbstractHolidayParser;
import de.jollyday.util.CalendarUtil;
import de.jollyday.util.XMLUtil;

public class FixedMovingOnWeekendParser extends AbstractHolidayParser {

    public void parse(int year, Set<LocalDate> holidays, Holidays config) {
        for (FixedMovingOnWeekend fm : config.getFixedMovingOnWeekend()) {
            if (!isValid(fm, year)) continue;
            LocalDate fixed = CalendarUtil.create(year, fm);
            if (CalendarUtil.isWeekend(fixed)) {
                int weekday = XMLUtil.getWeekday(fm.getNextWeekday());
                while (fixed.getDayOfWeek() != weekday) {
                    fixed = fixed.plusDays(1);
                }
            }
            holidays.add(fixed);
        }
    }
}
