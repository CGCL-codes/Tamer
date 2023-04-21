package model.beans.vacation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateWrapper {

    private boolean selected;

    private Date date;

    private DateFormat justTime;

    public static int DISABLED = 0;

    public static int ENABLED = 1;

    public static int SELECTED = 2;

    public DateWrapper() {
        justTime = new SimpleDateFormat("HH:mm");
    }

    /**
	 * @return A string that represents the time component of this date
	 */
    public String getTimeString() {
        return justTime.format(date);
    }

    /**
	 * Sets this date as selected / not selected
	 * @param isSelected
	 */
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    /**
	 * Return true if this date should not be selectable
	 * @return disabled
	 */
    public boolean isDisabled() {
        return getStatus() == DISABLED;
    }

    /**
	 * Returns the status Possible results are:
	 * DateWrapper.DISBALED
	 * DateWrapper.ENABLED
	 * DateWrapper.SELECTED
	 * @return the status
	 */
    public int getStatus() {
        Date now = new Date();
        int result = ENABLED;
        if (date.before(now)) {
            result = DISABLED;
        } else if (selected) {
            result = SELECTED;
        }
        return result;
    }

    /**
	 * Returns true if this date is part of the selection
	 * @return
	 */
    public boolean isSelected() {
        return selected;
    }

    /**
	 * Sets a new date in this DateWrapper
	 * @param date
	 */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
	 * Returns the Date in this DateWrapper
	 * @return
	 */
    public Date getDate() {
        return date;
    }
}
