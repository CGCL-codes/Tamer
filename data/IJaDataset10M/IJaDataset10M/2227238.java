package com.loribel.commons.util.convertor;

import java.net.MalformedURLException;
import java.net.URL;
import com.loribel.commons.exception.GB_ConvertorException;
import com.loribel.commons.util.STools;

/**
 * StringConvertor for URL.
 *
 * @author Gregory Borelli
 * @version 2003/11/20 - 15:23:22 - gen 7.12
 */
public class GB_URLStringConvertor extends GB_StringConvertorAbstract {

    /**
     * Constructor of GB_URLStringConvertor without parameter.
     */
    public GB_URLStringConvertor() {
        super();
    }

    /**
     * Parses String to produce corresponding URL object.
     *
     * @param a_string String - the string to parse
     *
     * @return Object
     */
    public Object stringAsValue(String a_string) throws GB_ConvertorException {
        if (STools.isNull(a_string)) {
            return null;
        }
        try {
            return new URL(a_string);
        } catch (MalformedURLException e) {
            throw new GB_StringConvertorFromStringException(a_string, URL.class, e);
        }
    }

    /**
     * Formats object into <tt>URL</tt>.
     * If value is not a URL object, throws a GB_StringConvertorException
     * exception.
     *
     * @param a_value Object - the value to convert into String
     *    (null accepted => do nothing and return null)
     *
     * @return String
     */
    public String valueAsString(Object a_value) throws GB_ConvertorException {
        if (a_value == null) {
            return null;
        }
        URL l_value = null;
        try {
            l_value = (URL) a_value;
        } catch (ClassCastException e) {
            throw new GB_ConvertorException(e);
        }
        return l_value.toString();
    }
}
