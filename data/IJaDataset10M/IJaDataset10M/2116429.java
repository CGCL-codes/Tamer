package org.enerj.apache.commons.beanutils.locale.converters;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.enerj.apache.commons.beanutils.locale.BaseLocaleConverter;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * <p>Standard {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
 * implementation that converts an incoming
 * locale-sensitive String into a <code>java.util.Date</code> object,
 * optionally using a default value or throwing a 
 * {@link org.enerj.apache.commons.beanutils.ConversionException}
 * if a conversion error occurs.</p>
 *
 * @author Yauheny Mikulski
 * @author Michael Szlapa
 */
public class DateLocaleConverter extends BaseLocaleConverter {

    /** All logging goes through this logger */
    private static Log log = LogFactory.getLog(DateLocaleConverter.class);

    /** Should the date conversion be lenient? */
    boolean isLenient = false;

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine and an unlocalized pattern is used
     * for the convertion.
     *
     */
    public DateLocaleConverter() {
        this(false);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine.
     *
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public DateLocaleConverter(boolean locPattern) {
        this(Locale.getDefault(), locPattern);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param locale        The locale
     */
    public DateLocaleConverter(Locale locale) {
        this(locale, false);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     *
     * @param locale        The locale
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public DateLocaleConverter(Locale locale, boolean locPattern) {
        this(locale, (String) null, locPattern);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param locale        The locale
     * @param pattern       The convertion pattern
     */
    public DateLocaleConverter(Locale locale, String pattern) {
        this(locale, pattern, false);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs.
     *
     * @param locale        The locale
     * @param pattern       The convertion pattern
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public DateLocaleConverter(Locale locale, String pattern, boolean locPattern) {
        super(locale, pattern, locPattern);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine and an unlocalized pattern is used
     * for the convertion.
     *
     * @param defaultValue  The default value to be returned
     */
    public DateLocaleConverter(Object defaultValue) {
        this(defaultValue, false);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine.
     *
     * @param defaultValue  The default value to be returned
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public DateLocaleConverter(Object defaultValue, boolean locPattern) {
        this(defaultValue, Locale.getDefault(), locPattern);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     */
    public DateLocaleConverter(Object defaultValue, Locale locale) {
        this(defaultValue, locale, false);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public DateLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
        this(defaultValue, locale, null, locPattern);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     * @param pattern       The convertion pattern
     */
    public DateLocaleConverter(Object defaultValue, Locale locale, String pattern) {
        this(defaultValue, locale, pattern, false);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will return the specified default value
     * if a conversion error occurs.
     *
     * @param defaultValue  The default value to be returned
     * @param locale        The locale
     * @param pattern       The convertion pattern
     * @param locPattern    Indicate whether the pattern is localized or not
     */
    public DateLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
        super(defaultValue, locale, pattern, locPattern);
    }

    /**
     * Returns whether date formatting is lenient.
     *
     * @return true if the <code>DateFormat</code> used for formatting is lenient
     * @see java.text.DateFormat#isLenient
     */
    public boolean isLenient() {
        return isLenient;
    }

    /**
     * Specify whether or not date-time parsing should be lenient.
     * 
     * @param lenient true if the <code>DateFormat</code> used for formatting should be lenient
     * @see java.text.DateFormat#setLenient
     */
    public void setLenient(boolean lenient) {
        isLenient = lenient;
    }

    /**
     * Convert the specified locale-sensitive input object into an output object of the
     * specified type.
     *
     * @param value The input object to be converted
     * @param pattern The pattern is used for the convertion
     *
     * @exception org.enerj.apache.commons.beanutils.ConversionException if conversion cannot be performed
     *  successfully
     */
    protected Object parse(Object value, String pattern) throws ParseException {
        SimpleDateFormat formatter = getFormatter(pattern, locale);
        if (locPattern) {
            formatter.applyLocalizedPattern(pattern);
        } else {
            formatter.applyPattern(pattern);
        }
        return formatter.parse((String) value);
    }

    /**
     * Gets an appropriate <code>SimpleDateFormat</code> for given locale, 
     * default Date format pattern is not provided.
     */
    private SimpleDateFormat getFormatter(String pattern, Locale locale) {
        if (pattern == null) {
            pattern = locPattern ? new SimpleDateFormat().toLocalizedPattern() : new SimpleDateFormat().toPattern();
            log.warn("Null pattern was provided, defaulting to: " + pattern);
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
        format.setLenient(isLenient);
        return format;
    }
}
