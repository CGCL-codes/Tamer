package hudson.zipscript.parser.template.element.lang.variable.format;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.exception.ExecutionException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefinedNumberFormatter implements Formatter {

    private String format;

    private Locale locale;

    private NumberFormat formatter;

    private Map localeFormatters;

    public DefinedNumberFormatter(String format, Locale locale) {
        this.format = format;
        this.locale = locale;
        this.formatter = getNumberFormat(locale);
    }

    public String format(Object object, ExtendedContext context) throws Exception {
        if (null == context.getLocale() || null == this.locale || this.locale.equals(context.getLocale())) {
            return formatter.format((Number) object);
        } else {
            if (null == localeFormatters) localeFormatters = new HashMap(2);
            NumberFormat formatter = (NumberFormat) localeFormatters.get(locale);
            if (null == formatter) {
                formatter = getNumberFormat(locale);
                localeFormatters.put(locale, formatter);
            }
            return formatter.format((Number) object);
        }
    }

    private NumberFormat getNumberFormat(Locale locale) {
        if (format.equals("currency")) {
            return NumberFormat.getCurrencyInstance(locale);
        } else if (format.equals("number")) {
            return NumberFormat.getNumberInstance(locale);
        } else if (format.equals("percent")) {
            return NumberFormat.getPercentInstance(locale);
        } else {
            throw new ExecutionException("Undefined number format '" + format + "'", null);
        }
    }
}
