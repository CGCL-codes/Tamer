package org.databene.commons.converter;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.databene.commons.ConversionException;
import org.databene.commons.Patterns;
import org.databene.commons.StringUtil;

/**
 * Parses a String as a time value.<br/>
 * <br/>
 * Created: 14.03.2008 22:15:58
 * @author Volker Bergmann
 */
public class String2TimeConverter extends ThreadSafeConverter<String, Time> {

    private String pattern;

    public String2TimeConverter() {
        this(null);
    }

    public String2TimeConverter(String pattern) {
        super(String.class, Time.class);
        this.pattern = pattern;
    }

    public Time convert(String sourceValue) throws ConversionException {
        return parse(sourceValue, pattern);
    }

    public static Time parse(String value) throws ConversionException {
        return parse(value, null);
    }

    public static Time parse(String value, String pattern) throws ConversionException {
        if (StringUtil.isEmpty(value)) return null;
        pattern = choosePattern(value, pattern);
        try {
            Date simpleDate = new SimpleDateFormat(pattern).parse(value);
            long millis = simpleDate.getTime();
            return new Time(millis);
        } catch (ParseException e) {
            throw new ConversionException(e);
        }
    }

    private static String choosePattern(String sourceValue, String pattern) {
        if (pattern == null) switch(sourceValue.length()) {
            case 12:
                pattern = Patterns.DEFAULT_TIME_MILLIS_PATTERN;
                break;
            case 8:
                pattern = Patterns.DEFAULT_TIME_SECONDS_PATTERN;
                break;
            case 5:
                pattern = Patterns.DEFAULT_TIME_MINUTES_PATTERN;
                break;
            default:
                throw new IllegalArgumentException("Not a supported time format: " + sourceValue);
        }
        return pattern;
    }
}
