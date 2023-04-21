package de.jaret.util.date;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import de.jaret.util.misc.FormatHelper;

/**
 * Delegate for the JaretDate class supplying convenient methods for generating formatted, i8ned output.
 * 
 * @author Peter Kliem
 * @version $Id: JaretDateFormatter.java 243 2007-02-11 22:08:49Z olk $
 */
public class JaretDateFormatter {

    Locale _locale;

    private DateFormat _df;

    private DateFormat _df2;

    private DateFormatSymbols _dateFormatSymbols;

    private SimpleDateFormat _dfTimeSeconds;

    private SimpleDateFormat _dfTimeNoSeconds;

    /**
     * Construcor specifying a locale.
     * 
     * @param locale locale to be used
     */
    public JaretDateFormatter(Locale locale) {
        setLocale(locale);
    }

    /**
     * Default constructor unsing default locale.
     */
    public JaretDateFormatter() {
        this(Locale.getDefault());
    }

    /**
     * Retrieve the used locale.
     * 
     * @return the locale set for the JaretDateFormatter
     */
    public Locale getLocale() {
        return _locale;
    }

    /**
     * Set the locale for this JaretDateFormatter.
     * 
     * @param locale Locale to be used
     */
    public void setLocale(Locale locale) {
        _locale = locale;
        _df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, _locale);
        _df2 = DateFormat.getDateInstance(DateFormat.SHORT, _locale);
        _df.setLenient(false);
        _df2.setLenient(false);
    }

    public Date parseTextualDate(String text) {
        Date date = null;
        try {
            date = _df2.parse(text);
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        JaretDateFormatter jdf = (JaretDateFormatter) obj;
        return jdf.getLocale().equals(_locale);
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return _locale.hashCode();
    }

    /**
     * Generate a textual representation of the given JaretDate.
     * 
     * @param date the JaretDate to be formatted
     * @return JaretDate to format
     */
    public String generateDisplayString(JaretDate date) {
        return _df.format(date.getDate());
    }

    /**
     * Generate a textual representation of the given JaretDate.
     * 
     * @param date the JaretDate to be formatted
     * @return textual represantation of the date (day)
     */
    public String generateDisplayStringDate(JaretDate date) {
        return _df2.format(date.getDate());
    }

    /**
     * Generate an output of the format hh:mm:ss for a given number of seconds.
     * 
     * @param sec Seconds
     * @return textual representation
     */
    public static String secondsToDisplayString(int sec) {
        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int seconds = (sec % 60);
        String str = FormatHelper.NFInt2Digits().format(hours) + ":" + FormatHelper.NFInt2Digits().format(minutes) + ":" + FormatHelper.NFInt2Digits().format(seconds);
        return str;
    }

    /**
     * Retrieve a localized name for the day of the week.
     * 
     * @param dayOfWeek
     * @return the localized name of the weekday
     */
    public String getDayOfWeekString(int dayOfWeek) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getWeekdays()[dayOfWeek];
    }

    public String getMonthString(int month) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getMonths()[month];
    }

    public String getShortDayOfWeekString(int dayOfWeek) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getShortWeekdays()[dayOfWeek];
    }

    public String getShortMonthString(int month) {
        if (_dateFormatSymbols == null) {
            _dateFormatSymbols = new DateFormatSymbols(_locale);
        }
        return _dateFormatSymbols.getShortMonths()[month];
    }

    /**
     * Generates a textual representation of the time only.
     * 
     * @param date JaretDate to be represenetd
     * @param seconds if true seconds will be included (hh:mm:ss), hh:mm otherwise
     * @return a Textual represenation of the time
     */
    public String toDisplayStringTime(JaretDate date, boolean seconds) {
        if (_dfTimeSeconds == null) {
            _dfTimeSeconds = new SimpleDateFormat("HH:mm:ss", _locale);
            _dfTimeNoSeconds = new SimpleDateFormat("HH:mm", _locale);
        }
        if (seconds) {
            return _dfTimeSeconds.format(date.getDate());
        } else {
            return _dfTimeNoSeconds.format(date.getDate());
        }
    }
}
