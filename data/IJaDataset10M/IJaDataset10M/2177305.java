package net.sourceforge.jpalm.mobiledb.field.definition;

import net.sourceforge.jpalm.mobiledb.field.type.Time;

/**
 * A time field in a MobileDB record.
 * <p>
 * Use {@link Time} with this field.
 */
public class TimeDefinition implements Definition {

    private boolean defaultToCurrentTime;

    /**
     * Creates a new time field that does not default to the current time.
     */
    public TimeDefinition() {
        defaultToCurrentTime = false;
    }

    /**
     * Creates a new time field.
     * 
     * @param defaultToCurrentTime
     *            <code>true</code> if the field should default to the current time;
     *            <code>false</code> otherwise
     */
    public TimeDefinition(boolean defaultToCurrentTime) {
        this.defaultToCurrentTime = defaultToCurrentTime;
    }

    /**
     * Creates a new time field from the string representation for this type.
     * 
     * @param indicator
     *            the string representation
     */
    public TimeDefinition(String indicator) {
        if (indicator.equals("d!")) defaultToCurrentTime = true; else defaultToCurrentTime = false;
    }

    public String getIndicator() {
        if (defaultToCurrentTime) return "d!"; else return "d";
    }

    public String getRegex() {
        return "^d!?$";
    }

    /**
     * Gets the <code>defaultToCurrentTime</code> property.
     * 
     * @return <code>true</code> if the time should default to the current time;
     *         <code>false</code> if it should default to null
     */
    public boolean isDefaultToCurrentTime() {
        return defaultToCurrentTime;
    }

    /**
     * Sets the <code>defaultToCurrentTime</code> property.
     * 
     * @param defaultToCurrentDate
     *            <code>true</code> if the time should default to the current time;
     *            <code>false</code> if it should default to null
     */
    public void setDefaultToCurrentTime(boolean defaultToCurrentDate) {
        this.defaultToCurrentTime = defaultToCurrentDate;
    }
}
