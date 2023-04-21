package org.enerj.apache.commons.beanutils.locale.converters;

import java.util.Locale;

/**
 * <p>Standard {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
 * implementation that converts an incoming
 * locale-sensitive String into a <code>java.lang.Long</code> object,
 * optionally using a default value or throwing a 
 * {@link org.enerj.apache.commons.beanutils.ConversionException}
 * if a conversion error occurs.</p>
 *
 * @author Yauheny Mikulski
 */
public class LongLocaleConverter extends DecimalLocaleConverter {

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. The locale is the default locale for
     * this instance of the Java Virtual Machine and an unlocalized pattern is used
     * for the convertion.
     *
     */
    public LongLocaleConverter() {
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
    public LongLocaleConverter(boolean locPattern) {
        this(Locale.getDefault(), locPattern);
    }

    /**
     * Create a {@link org.enerj.apache.commons.beanutils.locale.LocaleConverter} 
     * that will throw a {@link org.enerj.apache.commons.beanutils.ConversionException}
     * if a conversion error occurs. An unlocalized pattern is used for the convertion.
     *
     * @param locale        The locale
     */
    public LongLocaleConverter(Locale locale) {
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
    public LongLocaleConverter(Locale locale, boolean locPattern) {
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
    public LongLocaleConverter(Locale locale, String pattern) {
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
    public LongLocaleConverter(Locale locale, String pattern, boolean locPattern) {
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
    public LongLocaleConverter(Object defaultValue) {
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
    public LongLocaleConverter(Object defaultValue, boolean locPattern) {
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
    public LongLocaleConverter(Object defaultValue, Locale locale) {
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
    public LongLocaleConverter(Object defaultValue, Locale locale, boolean locPattern) {
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
    public LongLocaleConverter(Object defaultValue, Locale locale, String pattern) {
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
    public LongLocaleConverter(Object defaultValue, Locale locale, String pattern, boolean locPattern) {
        super(defaultValue, locale, pattern, locPattern);
    }
}
