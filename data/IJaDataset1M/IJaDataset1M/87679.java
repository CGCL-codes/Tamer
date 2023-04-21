package org.objectconverter;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.concurrent.Immutable;

/**
 * @author Masafumi Koba
 */
@Immutable
final class DateConvertable implements Convertable<Date> {

    @SuppressWarnings("nls")
    private static final String[] AVAILABLE_PATTERNS = { "yyyy-MM-dd", "HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.S" };

    @Override
    public Date convert(final Object from) throws CannotConversionException {
        if (from == null) {
            return null;
        }
        if (from instanceof Date) {
            return new Date(((Date) from).getTime());
        }
        if (from instanceof Number) {
            return new Date(((Number) from).longValue());
        }
        if (from instanceof CharSequence) {
            return new Date(parse(((CharSequence) from).toString()).getTime());
        }
        if (from instanceof Calendar) {
            return ((Calendar) from).getTime();
        }
        throw new CannotConversionException(from, Date.class);
    }

    private Date parse(final String from) {
        final SimpleDateFormat parser = new SimpleDateFormat();
        parser.setLenient(false);
        final ParsePosition position = new ParsePosition(0);
        for (final String pattern : AVAILABLE_PATTERNS) {
            if (from.length() != pattern.length()) {
                continue;
            }
            parser.applyPattern(pattern);
            position.setIndex(0);
            final Date result = parser.parse(from, position);
            if ((result != null) && (position.getIndex() != 0)) {
                return result;
            }
        }
        throw new CannotConversionException(from, Date.class);
    }
}
