package model.beans.vacation;

/**
 * This class is used to display an entire row in the vacation calendar (which is a week)
 * @author Mark
 *
 */
public class DateRow {

    private DateWrapper monday;

    private DateWrapper tuesday;

    private DateWrapper wednesday;

    private DateWrapper thursday;

    private DateWrapper friday;

    /**
	 * @return the monday
	 */
    public DateWrapper getMonday() {
        return monday;
    }

    /**
	 * @param monday the monday to set
	 */
    public void setMonday(DateWrapper monday) {
        this.monday = monday;
    }

    /**
	 * @return the tuesday
	 */
    public DateWrapper getTuesday() {
        return tuesday;
    }

    /**
	 * @param tuesday the tuesday to set
	 */
    public void setTuesday(DateWrapper tuesday) {
        this.tuesday = tuesday;
    }

    /**
	 * @return the wednesday
	 */
    public DateWrapper getWednesday() {
        return wednesday;
    }

    /**
	 * @param wednesday the wednesday to set
	 */
    public void setWednesday(DateWrapper wednesday) {
        this.wednesday = wednesday;
    }

    /**
	 * @return the thursday
	 */
    public DateWrapper getThursday() {
        return thursday;
    }

    /**
	 * @param thursday the thursday to set
	 */
    public void setThursday(DateWrapper thursday) {
        this.thursday = thursday;
    }

    /**
	 * @return the friday
	 */
    public DateWrapper getFriday() {
        return friday;
    }

    /**
	 * @param friday the friday to set
	 */
    public void setFriday(DateWrapper friday) {
        this.friday = friday;
    }
}
