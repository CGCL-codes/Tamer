package net.pandoragames.util.i18n;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Dummy Localizer implementation if no specific Localizer is set.
 * This implementation does not perform <i>any</i> translation of label
 * code and uses the standard parse and format methods provided by the
 * classes in package java.lang.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class DummyLocalizer implements Localizer {

    private static final String df = "dd.MM.yyyy";

    private SimpleDateFormat dateformat = new SimpleDateFormat(df);

    private NumberFormat numberformat = NumberFormat.getNumberInstance(Locale.ENGLISH);

    private int dummydigits = 2;

    /**
	 * Default constructor. The dummy is not locale sensitive
	 */
    public DummyLocalizer() {
        numberformat.setMaximumFractionDigits(dummydigits);
        numberformat.setMinimumFractionDigits(dummydigits);
    }

    /**
	 * Returns "dd.MM.yyyy".
	 */
    public String getDateFormat() {
        return df;
    }

    /**
	 * Returns "en".
	 */
    public String getLanguage() {
        return "en";
    }

    /**
	 * Returns the english locale.
	 */
    public Locale getLocale() {
        return Locale.ENGLISH;
    }

    /**
	 * Formats the date to "dd.MM.yyyy"
	 */
    public String localize(Date date) {
        if (date == null) return "";
        return dateformat.format(date);
    }

    /**
	 * This implementatio ignore the pattern code and returns the same String as 
	 * the <tt>localize( Date )</tt> method.
	 */
    public String localize(Date date, String patternCode) {
        return localize(date);
    }

    /**
	 * Returns the double as a string with the specified number of digits and
	 * the dot (.) as decimal seperator.
	 */
    public String localize(double number, int digits) {
        if (digits != dummydigits) {
            dummydigits = digits;
            numberformat.setMaximumFractionDigits(dummydigits);
            numberformat.setMinimumFractionDigits(dummydigits);
        }
        return numberformat.format(number);
    }

    /**
	 * Returns the long as a string
	 */
    public String localize(long number) {
        return String.valueOf(number);
    }

    /**
	 * Returns the code itself and ignores any replacement value
	 */
    public String localize(String code, Object replacementValue) {
        return code;
    }

    /**
	 * Returns the code itself and ignores any replacement value
	 */
    public String localize(String code, Object[] replacementValues) {
        return code;
    }

    /**
	 * Returns the code it self.
	 */
    public String localize(String code) {
        return code;
    }

    /**
	 * Expects "dd.MM.yyyy" as date format
	 */
    public Date parseDate(String date) {
        if (date == null) return null;
        try {
            return dateformat.parse(date);
        } catch (ParseException px) {
            return null;
        }
    }

    /**
	 * Parses the string into a Double as by <tt><b>new</b> Double( String )</tt>
	 */
    public Double parseDouble(String doubleNumber) {
        if (doubleNumber == null) return null;
        try {
            return new Double(doubleNumber);
        } catch (NumberFormatException nfx) {
            return null;
        }
    }

    /**
	 * Parses the string into an Integer as by <tt><b>new</b> Integer( String )</tt>
	 */
    public Integer parseInt(String integerNumber) {
        if (integerNumber == null) return null;
        try {
            return new Integer(integerNumber);
        } catch (NumberFormatException nfx) {
            return null;
        }
    }

    /**
	 * Parses the string into a Long as by <tt><b>new</b> Long( String )</tt>
	 */
    public Long parseLong(String longNumber) {
        if (longNumber == null) return null;
        try {
            return new Long(longNumber);
        } catch (NumberFormatException nfx) {
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public Date parseDate(String date, String patternCode) {
        if (date == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(patternCode);
            return sdf.parse(date);
        } catch (ParseException px) {
            return null;
        }
    }
}
