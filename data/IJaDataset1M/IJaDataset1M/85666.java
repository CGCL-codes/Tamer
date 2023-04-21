package sts.gui.races;

/**
 *
 * @author ken
 */
public class TimeIntervalOption {

    /**
     * Holds value of property description.
     */
    private String description;

    /**
     * Holds value of property interval.
     */
    private int interval;

    /**
     * Holds value of property multiplier.
     */
    private int multiplier;

    /** Creates a new instance of TimeIntervalOption */
    public TimeIntervalOption(String description, int interval, int multiplier) {
        setDescription(description);
        setInterval(interval);
        setMultiplier(multiplier);
    }

    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for property interval.
     * @return Value of property interval.
     */
    public int getInterval() {
        return this.interval;
    }

    /**
     * Setter for property interval.
     * @param interval New value of property interval.
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String toString() {
        return description;
    }

    /**
     * Getter for property multiplier.
     * @return Value of property multiplier.
     */
    public int getMultiplier() {
        return this.multiplier;
    }

    /**
     * Setter for property multiplier.
     * @param multiplier New value of property multiplier.
     */
    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeIntervalOption)) return false;
        TimeIntervalOption other = (TimeIntervalOption) o;
        if (other.getDescription() == null && this.getDescription() != null) return false;
        if (other.getDescription() != null && this.getDescription() == null) return false;
        if (!getDescription().equals(other.getDescription())) return false;
        if (interval != other.interval) return false;
        if (multiplier != other.multiplier) return false;
        return true;
    }
}
