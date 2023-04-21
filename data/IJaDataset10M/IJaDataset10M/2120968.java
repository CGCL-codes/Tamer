package net.sourceforge.jpalm.mobiledb.field.definition;

import net.sourceforge.jpalm.mobiledb.field.type.Date;

/**
 * A date field in a MobileDB record.
 * <p>
 * Use {@link Date} with this field.
 */
public class DateDefinition implements Definition {

    private boolean defaultToCurrentDate;

    /**
     * Creates a new date that does not default to the current date.
     */
    public DateDefinition() {
        defaultToCurrentDate = false;
    }

    /**
     * Creates a new date.
     * 
     * @param defaultToCurrentDate
     *            <code>true</code> if the field should default to the current date;
     *            <code>false</code> otherwise
     */
    public DateDefinition(boolean defaultToCurrentDate) {
        this.defaultToCurrentDate = defaultToCurrentDate;
    }

    /**
     * Creates a new date from the string representation for this type.
     * 
     * @param indicator
     *            the string representation
     */
    public DateDefinition(String indicator) {
        if (indicator.equals("D!")) defaultToCurrentDate = true; else defaultToCurrentDate = false;
    }

    public String getIndicator() {
        if (defaultToCurrentDate) return "D!"; else return "D";
    }

    public String getRegex() {
        return "^D!?$";
    }

    /**
     * Gets the <code>defaultToCurrentDate</code> property.
     * 
     * @return <code>true</code> if the date should default to the current date;
     *         <code>false</code> if it should default to null
     */
    public boolean isDefaultToCurrentDate() {
        return defaultToCurrentDate;
    }

    /**
     * Sets the <code>defaultToCurrentDate</code> property.
     * 
     * @param defaultToCurrentDate
     *            <code>true</code> if the date should default to the current date;
     *            <code>false</code> if it should default to null
     */
    public void setDefaultToCurrentDate(boolean defaultToCurrentDate) {
        this.defaultToCurrentDate = defaultToCurrentDate;
    }
}
