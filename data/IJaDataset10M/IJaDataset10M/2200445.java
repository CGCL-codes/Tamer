package javax.microedition.global;

import com.sun.j2me.global.FormatAbstractionLayer;
import java.util.Calendar;
import com.sun.j2me.global.CommonFormatter;
import com.sun.j2me.global.MessageFormat;
import com.sun.j2me.global.LocaleHelpers;

public final class Formatter {

    /**
     *  Constant for long time style.
     */
    public static final int TIME_LONG = 3;

    /**
     *  Constant for short time style.
     */
    public static final int TIME_SHORT = 2;

    /**
     *  Constant long date style.
     */
    public static final int DATE_LONG = 1;

    /**
     *  Constant short date style.
     */
    public static final int DATE_SHORT = 0;

    /**
     *  Constant long datetime style.
     */
    public static final int DATETIME_LONG = 5;

    /**
     *  Constant for short datetime style.
     */
    public static final int DATETIME_SHORT = 4;

    /**
     *  Current Formatter locale.
     */
    private String locale;

    /**
     *  FormatAbstractionLayer subclass instance for obtaining of Formatter
     *  realization.
     */
    private static FormatAbstractionLayer formatAbstractionLayer = FormatAbstractionLayer.getInstance();

    /**
     *  Current Formatter realization instance.
     */
    private CommonFormatter formatterImpl;

    public Formatter() throws UnsupportedLocaleException {
        this(System.getProperty("microedition.locale"));
    }

    /**
     *  Constructs a formatter for the specified locale.
     *
     * @param  locale  desired Formatter locale.
     * @throws  UnsupportedLocaleException  The exception
     * is thrown when locale isn't supported.
     * @throws  IllegalArgumentException  The exception
     * is thrown when locale has invalid format, i.e.
     * the locale format is not as the following:
     * null|({a..z}{a..z}[{-|_}{A..Z}{A..Z}[{-|_}<any symbols>]])
     */
    public Formatter(String locale) throws UnsupportedLocaleException, IllegalArgumentException {
        if (!LocaleHelpers.isValidLocale(locale) && !("".equals(locale))) {
            throw new IllegalArgumentException("Invalid locale format");
        }
        locale = LocaleHelpers.normalizeLocale(locale);
        if ("".equals(locale)) {
            this.locale = null;
        } else {
            this.locale = locale;
        }
        if (this.locale == null) {
            formatterImpl = FormatAbstractionLayer.getNeutralFormatter();
        } else {
            formatterImpl = formatAbstractionLayer.getFormatter(locale);
        }
    }

    public static String formatMessage(String template, String[] params) {
        if (template == null || params == null) {
            throw new NullPointerException("Template or parameter array is null.");
        }
        return MessageFormat.format(template, params);
    }

    public String formatDateTime(Calendar dateTime, int style) {
        if (dateTime == null) {
            throw new NullPointerException("Calendar is null.");
        }
        if (style < DATE_SHORT || style > DATETIME_LONG) {
            throw new IllegalArgumentException("Illegal style value");
        }
        return formatterImpl.formatDateTime(dateTime, style);
    }

    public String formatCurrency(double number) {
        return formatterImpl.formatCurrency(number);
    }

    public String formatCurrency(double number, String currencyCode) throws IllegalArgumentException {
        if (currencyCode.length() != 3 || currencyCode.charAt(0) < 'A' || currencyCode.charAt(0) > 'Z' || currencyCode.charAt(1) < 'A' || currencyCode.charAt(1) > 'Z' || currencyCode.charAt(2) < 'A' || currencyCode.charAt(2) > 'Z') {
            throw new IllegalArgumentException("Illegal currency code");
        }
        return formatterImpl.formatCurrency(number, currencyCode);
    }

    public String formatNumber(double number) {
        return formatterImpl.formatNumber(number);
    }

    public String formatNumber(double number, int decimals) throws IllegalArgumentException {
        if (decimals < 1 || decimals > 15) {
            throw new IllegalArgumentException("Illegal number of decimals");
        }
        return formatterImpl.formatNumber(number, decimals);
    }

    public String formatNumber(long number) {
        return formatterImpl.formatNumber(number);
    }

    public String formatPercentage(long number) {
        return formatterImpl.formatPercentage(number);
    }

    public String formatPercentage(float number, int decimals) throws IllegalArgumentException {
        if (decimals < 1 || decimals > 15) {
            throw new IllegalArgumentException("Illegal number of decimals");
        }
        return formatterImpl.formatPercentage(number, decimals);
    }

    public static String[] getSupportedLocales() {
        return formatAbstractionLayer.getSupportedLocales();
    }

    public String getLocale() {
        return locale;
    }
}
