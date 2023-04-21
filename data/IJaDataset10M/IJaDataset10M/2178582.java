package fr.soleil.util.formatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import fr.soleil.util.UtilLogger;
import fr.soleil.util.parameter.ParameterManager;

/**
 * 
 * @author MOULHAUD
 *
 * Class that allows to control user entries when he wants to add or edit
 * predicates. If the user entry doesn't match the good type (int, float,date),
 * an exception is thrown. UtilFormatter uses singleton pattern.
 * 
 * ************* This is a GENERIC Component ***************
 */
public class UtilFormatter {

    public static final String m_strDATE_FORMAT_SHORT_US = "MM/dd/yyyy";

    public static final String m_strDATE_FORMAT_SHORT_FR = "dd/MM/yyyy";

    private Locale m_locale = null;

    private SimpleDateFormat m_sdf = null;

    private String m_dateFormat = null;

    private Date m_date = null;

    private static UtilFormatter m_instance = null;

    /** 
	 * Default constructor
	 */
    public UtilFormatter(String strDateFormat) {
        String strCountry = ParameterManager.getStringParameter("", "Locale_Country");
        String strLanguage = ParameterManager.getStringParameter("", "Locale_Language");
        if (strCountry == null || strLanguage == null) {
            UtilLogger.logger.addDebugLog("impossible to build the locale for the application");
            strLanguage = "en";
            strCountry = "US";
        }
        m_dateFormat = strDateFormat;
        m_locale = new Locale(strLanguage, strCountry);
        m_sdf = new SimpleDateFormat(m_dateFormat, m_locale);
        m_sdf.setLenient(false);
    }

    /**
	 * We create an instance with the dateFormat specified in paramter
	 * if a singleton instance exist, it will be replace by the new instance created 
	 * @param strDateFormat
	 */
    public static void createInstance(String strDateFormat) {
        m_instance = new UtilFormatter(strDateFormat);
    }

    /**
	 * Get an instance formatter
	 * 
	 * @return UtilFormatter
	 */
    public static UtilFormatter getinstance() {
        return m_instance;
    }

    /**
	 * Format entry in an integer
	 * 
	 * @param strEntry
	 * @throws NumberFormatException
	 */
    public int formatInt(String strEntry) throws NumberFormatException {
        try {
            return Integer.parseInt(strEntry);
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    /**
	 * Format entry in a number (int or float) according to a locale
	 * 
	 * @param strEntry
	 * @throws NumberFormatException
	 */
    public Number formatNumber(String strEntry) throws ParseException {
        NumberFormat nf = DecimalFormat.getInstance(m_locale);
        try {
            return nf.parse(strEntry);
        } catch (ParseException e) {
            throw e;
        }
    }

    /**
	 * Method that allows to format a date according to a date pattern. Ex : dd/MM/yyyy
	 * @param date
	 * @return
	 */
    public String format(Date date, String strPattern) {
        String formattedDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(strPattern);
        dateFormat.setLenient(false);
        formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    /**
	 * Method that allows to format a date according to a date pattern. Ex : dd/MM/yyyy
	 * @param date
	 * @return
	 */
    public String format(Date date) {
        return m_sdf.format(date);
    }

    /**
	 * Method that allows to format a date according to a date pattern. Ex : dd/MM/yyyy
	 * @param date
	 * @return
	 */
    public String formatWithHour(Date date) {
        if (date == null) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(m_dateFormat + " HH:mm", m_locale);
        return dateFormat.format(date);
    }

    /**
	 * Method that allows to format a date according to a locale and a default pattern
	 * @param date
	 * @return
	 */
    public String format(Date date, String strPattern, Locale locale) {
        String formattedDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(strPattern, locale);
        dateFormat.setLenient(false);
        formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    /**
	 * Format entry in a date according to a locale
	 * 
	 * @param strEntry
	 * @throws ParseException  if the entry doesn't match date
	 */
    public Date formatDate(String strEntry) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'CET' yyyy", Locale.US);
            sdf.setLenient(false);
            return sdf.parse(strEntry);
        } catch (ParseException e) {
            throw e;
        } catch (NullPointerException e) {
            ParseException exception = new ParseException(ParameterManager.getStringParameter("", "DATE_NULL_EXCEPTION"), 0);
            throw exception;
        }
    }

    /**
	 * Format entry in a date according to a locale
	 * 
	 * @param strEntry
	 * @return Date
	 * @throws ParseException if the entry doesn't match date
	 */
    public Date getDate(String strEntry) throws ParseException {
        try {
            return m_sdf.parse(strEntry);
        } catch (ParseException e) {
            throw e;
        } catch (NullPointerException e) {
            ParseException exception = new ParseException(ParameterManager.getStringParameter("", "DATE_NULL_EXCEPTION"), 0);
            throw exception;
        }
    }

    /**
	 * Format entry in a date according to a locale
	 * 
	 * @param strEntry
	 * @param strPattern
	 * @return Date
	 * @throws ParseException if the entry doesn't match date
	 */
    public static Date getDate(String strEntry, String strPattern) throws ParseException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(strPattern);
            dateFormat.setLenient(false);
            return dateFormat.parse(strEntry);
        } catch (ParseException e) {
            throw e;
        } catch (NullPointerException e) {
            ParseException exception = new ParseException(ParameterManager.getStringParameter("", "DATE_NULL_EXCEPTION"), 0);
            throw exception;
        }
    }

    /**
	 * Return the Locale used in the formatter
	 * @return
	 */
    public Locale getM_locale() {
        return m_locale;
    }

    /**
	 * Modify the locale use
	 * @param m_locale
	 */
    public void setM_locale(Locale m_locale) {
        this.m_locale = m_locale;
    }

    /**
	 * Return the DateFormat object use in all Date formatting
	 * @return
	 */
    public SimpleDateFormat getSimpleDateFormat() {
        return m_sdf;
    }

    /**
	 * Modify the DateFormat use for formatting the date
	 * @param sdf {@link SimpleDateFormat}
	 *        
	 */
    public void setSimpleDateFormat(SimpleDateFormat sdf) {
        this.m_sdf = sdf;
    }

    /**
	 * Get the date format
	 * @return {@link String}
	 */
    public String getDateFormat() {
        return m_dateFormat;
    }
}
