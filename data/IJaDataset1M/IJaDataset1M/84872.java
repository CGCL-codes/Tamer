package net.sourceforge.eclipsetrader.core.db;

import java.util.Calendar;
import java.util.Date;

/**
 * Security dividends data.
 * 
 * @author Marco Maccaferri
 * @since 1.0
 */
public class Dividend {

    Date date = Calendar.getInstance().getTime();

    double value;

    public Dividend() {
    }

    /**
     * Returns the dividend date.
     * 
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the dividend date.
     * 
     * @param date the date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the dividend value.
     * 
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the dividend date.
     * 
     * @param value the value
     */
    public void setValue(double value) {
        this.value = value;
    }
}
