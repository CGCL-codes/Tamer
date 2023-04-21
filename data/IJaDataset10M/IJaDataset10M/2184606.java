package com.loribel.commons.util.convertor;

import java.util.List;
import com.loribel.commons.exception.GB_ConvertorException;
import com.loribel.commons.util.GB_StringTools;
import com.loribel.commons.util.STools;

/**
 * StringConvertor for int array.
 *
 * @author Gregory Borelli
 * @version 2003/11/20 - 15:23:22 - gen 7.12
 */
public class GB_ArrayIntegerStringConvertor extends GB_StringConvertorAbstract {

    /**
     * Attribute separator. <br />.
     */
    private char separator = ' ';

    /**
     * Attribute primitiveConvertor. <br />.
     */
    static GB_IntegerStringConvertor primitiveConvertor = new GB_IntegerStringConvertor();

    /**
     * Constructor of GB_ArrayIntegerStringConvertor without parameter.
     */
    public GB_ArrayIntegerStringConvertor() {
        this(' ');
    }

    /**
     * Constructor of GB_ArrayIntegerStringConvertor with parameter(s).
     *
     * @param a_separator char -
     */
    public GB_ArrayIntegerStringConvertor(char a_separator) {
        super();
        separator = a_separator;
    }

    /**
     * Parses String to build an Array of <tt>int</tt>.
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
        int[] retour = new int[len];
        for (int i = 0; i < len; i++) {
            String l_item = (String) l_items.get(i);
            retour[i] = primitiveConvertor.stringAsValueInt(l_item);
        }
        return retour;
    }

    /**
     * Formats an int Array into a string.
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
        int[] l_value;
        try {
            l_value = (int[]) a_value;
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
