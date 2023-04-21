package org.dasein.examples.uom;

import org.dasein.util.uom.time.Day;
import org.dasein.util.uom.time.TimePeriod;
import org.dasein.util.uom.time.Week;
import javax.annotation.Nonnull;

/**
 * Demonstrates the use of the Dasein unit of measures libraries.
 * @author George Reese (george.reese@imaginary.com)
 * @since 2012.02
 * @version 2012.02
 */
public class UOMExample {

    public static void main(@Nonnull String... args) {
        TimePeriod<Week> weeks = new TimePeriod<Week>(2, TimePeriod.WEEK);
        System.out.println("Initial: " + weeks);
        weeks = (TimePeriod<Week>) weeks.add(new TimePeriod<Week>(1, TimePeriod.WEEK));
        System.out.println("Add a week: " + weeks);
        weeks = (TimePeriod<Week>) weeks.subtract(new TimePeriod<Day>(3, TimePeriod.DAY));
        System.out.println("Subtract 3 days: " + weeks);
        TimePeriod<Day> days = (TimePeriod<Day>) weeks.convertTo(TimePeriod.DAY);
        System.out.println("In days: " + days);
    }
}
