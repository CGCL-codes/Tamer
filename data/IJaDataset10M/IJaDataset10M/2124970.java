package controller.dateMultiChooser;

import java.util.Calendar;
import java.util.Date;

public class DateChooserDay {

    private int day;

    private boolean inNeighbourMonth;

    private boolean current;

    private Date date;

    public DateChooserDay(final Calendar calendar, final Date currentDate, final boolean neighbour) {
        day = calendar.get(Calendar.DATE);
        current = calendar.getTime().equals(currentDate);
        inNeighbourMonth = neighbour;
        date = calendar.getTime();
    }

    /**
     * @return true if this day is the actual calendar day
     */
    public boolean isCurrent() {
        return current;
    }

    /**
     * @return true if the day is in the month before or after the current month
     */
    public boolean isInNeighbourMonth() {
        return inNeighbourMonth;
    }

    /**
     * @return the Date object corresponding to this day
     */
    public Date getDate() {
        return date;
    }

    /**
     * @return the day string to be displayed on the date chooser
     */
    @Override
    public String toString() {
        return String.valueOf(day);
    }
}
