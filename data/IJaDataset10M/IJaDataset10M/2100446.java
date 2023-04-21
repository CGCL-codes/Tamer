package com.loribel.commons.util.convertor;

import java.util.List;
import com.loribel.commons.exception.GB_ConvertorException;
import com.loribel.commons.util.GB_StringTools;
import com.loribel.commons.util.STools;

/**
 * StringConvertor for long array.
 *
 * @author Gregory Borelli
 */
public class GB_ArrayLongStringConvertor extends GB_StringConvertorAbstract {

    /**
     * Attribute separator.
     */
    private char separator = ' ';

    /**
     * Attribute primitiveConvertor.
     */
    static GB_LongStringConvertor primitiveConvertor = new GB_LongStringConvertor();

    /**
     * Constructor of GB_ArrayLongStringConvertor without parameter.
     */
    public GB_ArrayLongStringConvertor() {
        this(' ');
    }

    /**
     * Constructor of GB_ArrayLongStringConvertor with parameter(s).
     *
     * @param a_separator char -
     */
    public GB_ArrayLongStringConvertor(char a_separator) {
        super();
        separator = a_separator;
    }

    /**
     * Parses String to build an Array of <tt>long</tt>.
     *
     * @param a_string String -
     *
     * @return Object
     */
    public Object stringAsValue(String a_string) throws GB_ConvertorException {
        if (STools.isNull(a_string)) {
            return null;
        }
        List l_items = GB_StringTools.toStrings(a_string, "" + separator);
        int len = l_items.size();
        long[] retour = new long[len];
        for (int i = 0; i < len; i++) {
            String l_item = (String) l_items.get(i);
            retour[i] = primitiveConvertor.stringAsValueLong(l_item);
        }
        return retour;
    }

    /**
     * Formats an long Array into a string.
     * Use the default convertor for this type and the separator between items.
     *
     * @param a_value Object -
     *
     * @return String
     */
    public String valueAsString(Object a_value) throws GB_ConvertorException {
        if (a_value == null) {
            return null;
        }
        long[] l_value;
        try {
            l_value = (long[]) a_value;
        } catch (ClassCastException e) {
            throw new GB_ConvertorException(e);
        }
        int len = l_value.length;
        if (len == 0) {
            return null;
        }
        StringBuffer retour = new StringBuffer();
        for (int i = 0; i < len; i++) {
            String l_item = primitiveConvertor.valueAsString(l_value[i]);
            retour.append(l_item + separator);
        }
        String r = retour.substring(0, retour.length() - 1);
        return r;
    }
}
