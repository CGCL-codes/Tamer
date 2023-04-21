package org.nomadpim.core.util.text;

import org.joda.time.DateTime;

public final class DateMillisecondsConverter extends StringConverter<DateTime> {

    public String format(DateTime t) {
        return Long.toString(t.getMillis());
    }

    public DateTime parse(String value) throws ParseException {
        try {
            return new DateTime(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new ParseException(value, e, "123");
        }
    }
}
