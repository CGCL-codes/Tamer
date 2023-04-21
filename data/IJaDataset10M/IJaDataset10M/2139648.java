package com.w20e.socrates.rendering;

import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import com.w20e.socrates.expression.Undef;
import com.w20e.socrates.expression.XNumber;
import com.w20e.socrates.expression.XObject;

/**
 * Control for date input. Date format defaults to dd/MM/yyyy. See SimpleDateFormat
 * for possible formats. To set the format, use the setProperty method of the
 * Properties class.
 * @author dokter
 *
 */
public class Date extends ControlImpl {

    /**
	 * UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Formatter for parsing dates.
	 */
    private DateFormat formatter;

    /**
	 * Construct input type.
	 */
    public Date(String id) {
        super(id);
        setType("date");
        setProperty("format", "dd/MM/yyyy");
    }

    /**
	 * Process data. This will return the String representation of the data
	 * for this control's id in the map.
	 * @todo We might need a Locale here...
	 */
    public XObject processInput(Map<String, Object> data, Class type) {
        long time;
        try {
            this.formatter = new SimpleDateFormat(getProperty("format"));
            time = this.formatter.parse(data.get(getId()).toString()).getTime();
        } catch (ParseException e) {
            return new Undef();
        }
        return new XNumber(Long.valueOf(time));
    }

    /**
	 * Return the display value for the given value.
	 * @todo Should we use the locale to format the date? I would think so...
	 */
    public Object getDisplayValue(XObject value, Locale locale) {
        return this.formatter.format(value.toNumber());
    }
}
