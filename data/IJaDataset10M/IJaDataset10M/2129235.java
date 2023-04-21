package org.dfdaemon.il2.core.logfile.filter;

import org.dfdaemon.il2.core.event.Event;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author aka50
 */
public class DatedLineFilter extends AbstractRegexLineFilter {

    private final DateFormat _formatter = new SimpleDateFormat("MMMM d, yyyy h:m:s a", Locale.ENGLISH);

    private DatedLineFilter(String patternString, String[] properties, Class<? extends Event> eventClass) {
        super(patternString, properties, eventClass);
    }

    public String getDatePrefix() {
        return "^\\[(\\w{3} \\d{1,2}\\, \\d{4} \\d{1,2}\\:\\d{2}\\:\\d{2} \\w\\w)\\] ";
    }

    public Date handleDate(Date bias, String dateString) throws ParseException {
        return _formatter.parse(dateString);
    }

    public static DatedLineFilter createFilter(String patternString, String[] properties, Class<? extends Event> eventClass) {
        return new DatedLineFilter(patternString, properties, eventClass);
    }
}
