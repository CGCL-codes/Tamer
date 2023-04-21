package com.ail.util;

import com.ail.core.Type;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/16 21:08:54 $
 * @source $Source: /home/bob/CVSRepository/projects/common/commercial.ear/commercial.jar/com/ail/util/DateOfBirth.java,v $
 * @stereotype type
 */
public class DateOfBirth extends Type {

    static final long serialVersionUID = 4525629482776785515L;

    private Date date;

    /**
     * Default Constructor.
     */
    public DateOfBirth() {
    }

    /**
     * Constructor
     * @param dob
     */
    public DateOfBirth(Date dob) {
        setDate(dob);
    }

    /**
     * Constructor to build a data of birth for a specific date.
     * @param year Four digit year
     * @param month Month number (0-11)
     * @param day Day in month
     */
    public DateOfBirth(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        setDate(c.getTime());
    }

    /**
     * Get the dob as a dat
     * @return The date of birth
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the date of birth
     * @param date The data of birth
     */
    public void setDate(Date date) {
        if (date != null && date.after(new Date())) {
            throw new IllegalArgumentException("Attempt to create date of birth in the future.");
        }
        this.date = date;
    }

    /**
     * Fetch the date in the format defined for the default locale.
     * @return A string in the locale defined format.
     */
    public String getDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(getDate());
    }

    /**
     * Set the date from a string in the format defined for the default locale.
     * @param date The date as a String
     * @throws ParseException If the String does not represent a valid date in this locale.
     */
    public void setDateAsString(String date) throws ParseException {
        setDate(DateFormat.getDateInstance(DateFormat.SHORT).parse(date));
    }

    /**
	 * Fetch the date in the locale defined format.
	 * @param locale Locale to format date for.
	 * @return A string in the locale defined format.
	 */
    public String getDateAsString(Locale locale) {
        return DateFormat.getDateInstance(DateFormat.SHORT, locale).format(getDate());
    }

    /**
	 * Set the date from a string in the locale defined date format.
	 * @param date The date as a String
	 * @param locale The locale the date string is in
	 * @throws ParseException If the String does not represent a valid date in this locale.
	 */
    public void setDateAsString(String date, Locale locale) throws ParseException {
        setDate(DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(date));
    }

    /**
	 * Fetch the date as a String in a specified format.
	 * @param format The format of the date String required {@see SimpleDateFormat SimpleDateFormat}
	 * @return A string in the locale defined format.
	 */
    public String getDateAsString(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(getDate());
    }

    /**
	 * Set the date from a string in the locale defined date format.
	 * @param date The date as a String
	 * @param locale The locale the date string is in
	 * @throws ParseException If the String does not represent a valid date in this locale.
	 */
    public void setDateAsString(String date, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        setDate(dateFormat.parse(date));
    }

    /**
     * Return the current age
     * @return Current age in years.
     */
    public int currentAge() {
        return ageAtDate(new Date());
    }

    /**
     * Return the age at a specific date.
     * @param then The date the age should be calculate for.
     * @return Current age in years.
     */
    public int ageAtDate(Date then) {
        if (date.after(then)) {
            throw new IllegalArgumentException("Age less than zero");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.date);
        int bornIn = cal.get(Calendar.YEAR);
        cal.setTime(then);
        return cal.get(Calendar.YEAR) - bornIn;
    }
}
