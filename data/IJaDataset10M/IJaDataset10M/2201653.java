package com.strategicgains.openef.presentation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.strategicgains.openef.i18n.I18n;
import com.strategicgains.openef.util.Strings;

/**
 * @author Todd Fredrich
 * @since Dec 13, 2004
 * @version $Revision: 1.8 $
 */
public class TimestampAttribute extends DateAttribute {

    private static final int DATE_HOUR = 3;

    private static final int DATE_MINUTE = 4;

    private static final int DATE_SECOND = 5;

    private static final int DATE_MILLIS = 6;

    private static final String SET_VALUE_DATE_FORMAT = "yyyy/MM/dd k:m:s:S";

    private static final SimpleDateFormat setValueDateFormat = new SimpleDateFormat(SET_VALUE_DATE_FORMAT);

    /**
	 * @param name
	 */
    public TimestampAttribute(String name) {
        super(name);
    }

    /**
	 * @param name
	 * @param value
	 */
    public TimestampAttribute(String name, Date value) {
        super(name, value);
    }

    public void marshalValue(StringBuffer xml, I18n i18n) {
        super.marshalValue(xml, i18n);
        String dateString = xmlDateFormat.format(getValue());
        String dateElements[] = dateString.split(",");
        xml.append("<Hour>" + dateElements[DATE_HOUR] + "</Hour>");
        xml.append("<Minute>" + dateElements[DATE_MINUTE] + "</Minute>");
        xml.append("<Second>" + dateElements[DATE_SECOND] + "</Second>");
        xml.append("<Millisecond>" + dateElements[DATE_MILLIS] + "</Millisecond>");
    }

    /**
	 * Sets the attribute value from a date/time string.  Value string must be in the format 
	 * "YYYY/MM/DD hours:min:sec:milliseconds" to parse correctly.
	 * 
	 * @see com.strategicgains.openef.presentation.PresentationAttribute#setValue(java.lang.String)
	 */
    public void setValue(String valueString) throws PresentationModelException {
        try {
            if (valueString != null) {
                setValue(setValueDateFormat.parse(valueString));
            } else {
                setValue((Date) null);
            }
        } catch (ParseException e) {
            throw new PresentationModelException(e);
        }
    }

    public String localize(I18n i18n) {
        String result = Strings.EMPTY_STRING;
        if (getValue() != null) result = i18n.localize(getValue(), DateFormat.LONG);
        return result;
    }
}
