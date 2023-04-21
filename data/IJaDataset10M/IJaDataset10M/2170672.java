package com.loribel.commons.util.convertor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.loribel.commons.abstraction.GB_StringConvertor;
import com.loribel.commons.exception.GB_ConvertorException;
import com.loribel.commons.util.GB_StringTools;
import com.loribel.commons.util.STools;

/**
 * StringConvertor for Array Primitives.
 *
 * @author Gregory Borelli
 * @version 2003/11/20 - 15:23:22 - gen 7.12
 */
public class GB_ArrayPrimitiveConvertor extends GB_StringConvertorAbstract {

    /**
     * The type of Primitive (Integer.TYPE, Boolean.TYPE, ...).
     */
    private Class type;

    /**
     * Separator used between items of the array for the String representation.
     */
    private char separator = ' ';

    /**
     * Constructor of GB_ArrayPrimitiveConvertor with parameter(s).
     *
     * @param a_type Class - the type of Primitive (Integer.TYPE, Boolean.TYPE, ...)
     */
    public GB_ArrayPrimitiveConvertor(Class a_type) {
        super();
        type = a_type;
    }

    /**
     * Constructor of GB_ArrayPrimitiveConvertor with parameter(s).
     *
     * @param a_type Class - the type of Primitive (Integer.TYPE, Boolean.TYPE, ...)
     * @param a_separator char - separator used between items of the array for the String representation
     */
    public GB_ArrayPrimitiveConvertor(Class a_type, char a_separator) {
        super();
        type = a_type;
        separator = a_separator;
    }

    /**
     * Return the convertor to convert item of the array.
     * If no convertor found, throws an GB_StringConvertorException exception.
     *
     * @return GB_StringConvertor
     */
    protected GB_StringConvertor buildItemConvertor() throws GB_ConvertorException {
        GB_StringConvertor retour = GB_StringConvertors.getInstance().getConvertor(type);
        if (retour == null) {
            String l_msg = AA.EXCEPTION_CONVERTOR_NO_CONVERTOR_FOR_CLASS;
            l_msg = STools.replace(l_msg, type.getName());
            throw new GB_ConvertorException(l_msg);
        }
        return retour;
    }

    /**
     * Parses String to build an Array of <tt>type</tt>.
     *
     * @param a_string String -
     *
     * @return Object
     */
    public Object stringAsValue(String a_string) throws GB_ConvertorException {
        if (STools.isNull(a_string)) {
            return null;
        }
        GB_StringConvertor l_convertor = buildItemConvertor();
        List l_items = GB_StringTools.toStrings(a_string, "" + separator);
        int len = l_items.size();
        Collection retourList = new ArrayList();
        for (int i = 0; i < len; i++) {
            String l_item = (String) l_items.get(i);
            retourList.add(l_convertor.stringAsValue(l_item));
        }
        Object retour = null;
        try {
            Object[] retourType = (Object[]) Array.newInstance(type, len);
            retour = retourList.toArray(retourType);
        } catch (Exception e) {
            throw new GB_ConvertorException(e);
        }
        return retour;
    }

    /**
     * Formats Array object into a string.
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
        Object[] l_value;
        try {
            l_value = (Object[]) a_value;
        } catch (ClassCastException e) {
            throw new GB_ConvertorException(e);
        }
        GB_StringConvertor l_convertor = buildItemConvertor();
        int len = l_value.length;
        StringBuffer retour = new StringBuffer();
        for (int i = 0; i < len; i++) {
            String l_item = l_convertor.valueAsString(l_value[i]);
            retour.append(l_item + separator);
        }
        String r = retour.substring(0, retour.length() - 1);
        return r;
    }
}
