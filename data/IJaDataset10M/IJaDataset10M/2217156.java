package net.sf.woko.util;

import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Locale;

/**
 * Utility method for converting data types, or types between other
 * representations.
 * 
 * For example, convert the date pattern representation of Java to the one used
 * for Web date pickers.
 * 
 * @author mhaller
 */
public class ConverterUtil {

    private static final Logger logger = Logger.getLogger(ConverterUtil.class);

    public String computeJavaScriptDatePattern(Locale locale) {
        String javaPattern = computeJavaDatePattern(locale);
        final StringBuilder buf = new StringBuilder();
        boolean dayAdded = false;
        boolean monthAdded = false;
        boolean yearAdded = false;
        for (int i = 0; i < javaPattern.length(); i++) {
            char c = javaPattern.charAt(i);
            if ((c == 'd' || c == 'D') && !dayAdded) {
                buf.append("%d");
                dayAdded = true;
                if (!(dayAdded && monthAdded && yearAdded)) {
                    buf.append("/");
                }
            }
            if ((c == 'm' || c == 'M') && !monthAdded) {
                buf.append("%m");
                monthAdded = true;
                if (!(dayAdded && monthAdded && yearAdded)) {
                    buf.append("/");
                }
            }
            if ((c == 'y' || c == 'Y') && !yearAdded) {
                buf.append("%y");
                yearAdded = true;
                if (!(dayAdded && monthAdded && yearAdded)) {
                    buf.append("/");
                }
            }
        }
        return buf.toString();
    }

    /**
     * Computes the pattern for passed locale.
     */
    public String computeJavaDatePattern(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat) df).toPattern();
        } else {
            logger.warn("Unable to compute java pattern from locale (DateFormat instance is not a SimpleDateFormat)");
            return new SimpleDateFormat().toPattern();
        }
    }
}
