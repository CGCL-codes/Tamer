package org.springframework.binding.format;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Produces NumberFormat instances that format percent values.
 * 
 * @see NumberFormat
 * @author Keith Donald
 */
public class PercentNumberFormatFactory extends AbstractNumberFormatFactory {

    protected NumberFormat getNumberFormat(Locale locale) {
        return NumberFormat.getPercentInstance(locale);
    }
}
