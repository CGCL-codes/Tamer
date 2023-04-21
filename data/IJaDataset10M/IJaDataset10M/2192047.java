package com.loribel.commons.util.convertor;

import java.util.TimeZone;
import com.loribel.commons.exception.GB_ConvertorException;
import com.loribel.commons.util.STools;

/**
 * StringConvertor for TimeZone.
 */
public class GB_TimeZoneStringConvertor extends GB_StringConvertorAbstract {

    /**
     * Constructor.
     */
    public GB_TimeZoneStringConvertor() {
        super();
    }

    /**
     * Parses String as TimeZone id.
     */
    public Object stringAsValue(String a_string) throws GB_ConvertorException {
        if (STools.isNull(a_string)) {
            return null;
        }
        return TimeZone.getTimeZone(a_string);
    }

    /**
     * Return the id of the TimeZone.
     */
    public String valueAsString(Object a_value) throws GB_ConvertorException {
        if (a_value == null) {
            return null;
        }
        TimeZone l_tz = null;
        try {
            l_tz = (TimeZone) a_value;
        } catch (ClassCastException e) {
            throw new GB_ConvertorException(e);
        }
        return l_tz.getID();
    }
}
