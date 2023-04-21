package org.allcolor.services.xml.converters;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.ConverterMatcher;
import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * @author qan
 * 
 */
public class CalendarConverter implements ConverterMatcher, SingleValueConverter {

    @SuppressWarnings("unchecked")
    public boolean canConvert(final Class clazz) {
        return GregorianCalendar.class.isAssignableFrom(clazz);
    }

    public Object fromString(final String value) {
        final SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS");
        try {
            final GregorianCalendar c = new GregorianCalendar();
            c.setTime(sdfdate.parse(value));
            return c;
        } catch (final Exception e) {
            throw new ConversionException(e.getMessage(), e);
        }
    }

    public String toString(final Object ots) {
        final GregorianCalendar date = (GregorianCalendar) ots;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS");
        final String representation = sdf.format(date.getTime());
        return representation;
    }
}
