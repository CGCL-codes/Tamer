package org.databene.commons.converter;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.databene.commons.ConversionException;
import org.databene.commons.Patterns;

/**
 * Converts {@link Time} objects to {@link String}s.<br/><br/>
 * Created: 26.02.2010 07:59:11
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class Time2StringConverter extends ThreadSafeConverter<Time, String> {

    public Time2StringConverter() {
        super(Time.class, String.class);
    }

    public String convert(Time target) throws ConversionException {
        return new SimpleDateFormat(Patterns.DEFAULT_TIME_MILLIS_PATTERN).format(new Date(target.getTime()));
    }
}
