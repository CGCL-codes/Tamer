package com.loribel.commons.util.convertor;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import javax.swing.Icon;
import junit.framework.TestCase;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_StringConvertor;
import com.loribel.commons.exception.GB_ConvertorException;

/**
 * Classe de test.
 *
 * @author Gr�gory Borelli
 */
public class GB_StringConvertorsTest extends TestCase {

    public GB_StringConvertorsTest(String a_name) {
        super(a_name);
    }

    public void test_addConvertor() {
        GB_StringConvertor newConvertor;
        GB_StringConvertor anotherNewConvertor;
        GB_StringConvertor returnedConvertor;
        GB_StringConvertors convertors;
        Object previousValue;
        convertors = GB_StringConvertors.getInstance();
        newConvertor = new GB_IntegerStringConvertor();
        previousValue = convertors.addConvertor(javax.swing.JTextField.class, newConvertor);
        assertNull(1.1 + " - Previous value associated with JTextField was not null", previousValue);
        returnedConvertor = convertors.getConvertor(javax.swing.JTextField.class);
        assertNotNull(2.1 + " - The returned value for javax.swing.JTextField.class is null", returnedConvertor);
        assertEquals(2.2 + " - The returned convertor is not the same", newConvertor, returnedConvertor);
        anotherNewConvertor = new GB_IntegerStringConvertor();
        previousValue = convertors.addConvertor(javax.swing.JTextField.class, anotherNewConvertor);
        assertNotNull(3.1 + " - Previous value associated with JTextField was null", previousValue);
        assertEquals(3.2 + " - Previous value for JTextField was not newConvertor", newConvertor, previousValue);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_StringConvertors#getConvertor(
     * java.lang.Class)} method
     */
    public void test_getConvertor() {
        GB_StringConvertorsMock convertor;
        Map convertorMap;
        convertor = new GB_StringConvertorsMock();
        GB_StringConvertors.initWith(convertor);
        convertorMap = convertor.getMapStringConvertor();
        assertNotNull(1.1 + " - Convertor map is null", convertorMap);
        assertEquals(1.2 + " - Number of convertors in map is not correct", 35, convertorMap.size());
        test_getConvertor(2, Boolean.class, GB_BooleanStringConvertor.class);
        test_getConvertor(3, Class.class, GB_ClassStringConvertor.class);
        test_getConvertor(4, Date.class, GB_DateStringConvertor.class);
        test_getConvertor(5, Double.class, GB_DoubleStringConvertor.class);
        test_getConvertor(6, File.class, GB_FileStringConvertor.class);
        test_getConvertor(7, Float.class, GB_FloatStringConvertor.class);
        test_getConvertor(8, Integer.class, GB_IntegerStringConvertor.class);
        test_getConvertor(9, Long.class, GB_LongStringConvertor.class);
        test_getConvertor(10, Short.class, GB_ShortStringConvertor.class);
        test_getConvertor(11, String.class, GB_StringStringConvertor.class);
        test_getConvertor(12, URL.class, GB_URLStringConvertor.class);
        test_getConvertor(13, Icon.class, GB_IconStringConvertor.class);
        test_getConvertor(14, TimeZone.class, GB_TimeZoneStringConvertor.class);
        test_getConvertor(20, Boolean[].class, GB_ArrayStringConvertor.class);
        test_getConvertor(21, Double[].class, GB_ArrayStringConvertor.class);
        test_getConvertor(22, Float[].class, GB_ArrayStringConvertor.class);
        test_getConvertor(23, Integer[].class, GB_ArrayStringConvertor.class);
        test_getConvertor(24, Long[].class, GB_ArrayStringConvertor.class);
        test_getConvertor(25, Short[].class, GB_ArrayStringConvertor.class);
        test_getConvertor(26, String[].class, GB_ArrayStringConvertor.class);
        test_getConvertor(30, double[].class, GB_ArrayDoubleStringConvertor.class);
        test_getConvertor(31, float[].class, GB_ArrayFloatStringConvertor.class);
        test_getConvertor(32, int[].class, GB_ArrayIntegerStringConvertor.class);
        test_getConvertor(33, long[].class, GB_ArrayLongStringConvertor.class);
        test_getConvertor(34, short[].class, GB_ArrayShortStringConvertor.class);
        test_getConvertor(40, GB_LabelIcon.class, GB_LabelIconStringConvertor.class);
        test_getConvertor(100, javax.swing.JComboBox.class, null);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_StringConvertors#getConvertor(
     * java.lang.Class)} method. This private method has 2 parameters : the key
     * used when calling the getConvertor method, and the expected class of the
     * returned convertor.
     *
     * When the second parameter is null, this method uses
     * the <code>assertNull</code> method on the returned convertor(mostly used
     * when testing if <code>getConvertor</code> returns <code>null</code> when
     * <code>a_key</code> is invalid).
     *
     * @param a_key The key used when calling getConvertor
     * @param a_expectedResult The expected class of the returned convertor
     */
    private void test_getConvertor(int a_index, Class a_key, Class a_expectedResult) {
        GB_StringConvertor returnedConvertor;
        GB_StringConvertors convertor;
        convertor = GB_StringConvertors.getInstance();
        returnedConvertor = convertor.getConvertor(a_key);
        if (a_expectedResult != null) {
            assertNotNull(a_index + .1 + " - Convertor is null", returnedConvertor);
            assertEquals(a_index + .2 + " - Convertor class is not as expected", a_expectedResult, returnedConvertor.getClass());
        } else {
            assertNull(a_index + .1 + " - Convertor is not null", returnedConvertor);
        }
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_StringConvertors#getInstance()}
     * method
     */
    public void test_getInstance() {
        GB_StringConvertors convertor;
        GB_StringConvertors secondConvertor;
        convertor = GB_StringConvertors.getInstance();
        assertNotNull(1.1 + " - Convertor is null", convertor);
        secondConvertor = GB_StringConvertors.getInstance();
        assertNotNull(2.1 + " - Second convertor is null", secondConvertor);
        assertEquals(2.2 + " - Second convertor is not the same as first convertor", convertor, secondConvertor);
    }

    /**
     * Tests the {@link com.loribel.commons.util.GB_StringConvertors#initWith(
     * com.loribel.commons.util.convertor.GB_StringConvertors)} method
     */
    public void test_initWith() {
        GB_StringConvertors secondConvertor;
        GB_StringConvertors dummy;
        dummy = new GB_StringConvertorsMock();
        GB_StringConvertors.initWith(dummy);
        secondConvertor = GB_StringConvertors.getInstance();
        assertNotNull(1.1 + " - Second convertor is null", secondConvertor);
        assertEquals(1.2 + " - Convertor did not change after call to initWith", dummy, secondConvertor);
    }

    protected void test_stringAsString(int a_index, Class a_class, String a_string) throws GB_ConvertorException {
        GB_StringConvertors l_convertor = GB_StringConvertors.getInstance();
        Object l_value;
        String l_result;
        l_value = l_convertor.stringAsValue(a_string, a_class);
        l_result = l_convertor.valueAsString(l_value, a_class);
        if (a_string == null) {
            assertNull(a_index + .1 + " - value not null", l_value);
            assertNull(a_index + .2 + " - result not null", l_result);
        } else {
            assertTrue(a_index + .1 + " - result type is not valid", a_class.isInstance(l_value));
            assertEquals(a_index + .2 + " - " + a_string, a_string, l_result);
        }
        l_value = l_convertor.stringAsValueSafe(a_string, a_class, 0);
        l_result = l_convertor.valueAsStringSafe(l_value, a_class, 0);
        if (a_string == null) {
            assertNull(a_index + .3 + " - value not null", l_value);
            assertNull(a_index + .4 + " - result not null", l_result);
        } else {
            assertTrue(a_index + .3 + " - result type is not valid", a_class.isInstance(l_value));
            assertEquals(a_index + .4 + " - " + a_string, a_string, l_result);
        }
    }

    /**
     * Test : <tt>stringAsValue</tt> et <tt>valueAsString</tt>
     * for the value null.
     */
    public void test_stringAsString1() throws GB_ConvertorException {
        test_stringAsString(1, Boolean.class, null);
        test_stringAsString(2, Class.class, null);
        test_stringAsString(3, Date.class, null);
        test_stringAsString(4, Double.class, null);
        test_stringAsString(5, File.class, null);
        test_stringAsString(6, Float.class, null);
        test_stringAsString(7, Integer.class, null);
        test_stringAsString(8, Long.class, null);
        test_stringAsString(9, Short.class, null);
        test_stringAsString(10, String.class, null);
        test_stringAsString(11, URL.class, null);
        test_stringAsString(12, Boolean[].class, null);
        test_stringAsString(13, Double[].class, null);
        test_stringAsString(14, Float[].class, null);
        test_stringAsString(15, Integer[].class, null);
        test_stringAsString(16, Long[].class, null);
        test_stringAsString(17, Short[].class, null);
        test_stringAsString(18, String[].class, null);
        test_stringAsString(19, double[].class, null);
        test_stringAsString(20, float[].class, null);
        test_stringAsString(21, int[].class, null);
        test_stringAsString(22, long[].class, null);
        test_stringAsString(23, short[].class, null);
    }

    /**
     * Test : <tt>stringAsValue</tt> et <tt>valueAsString</tt>
     * for not null value.
     */
    public void test_stringAsString2() throws GB_ConvertorException {
        test_stringAsString(1, Boolean.class, "true");
        test_stringAsString(2, Class.class, "Integer[][]");
        test_stringAsString(3, Date.class, "2002-03-01");
        test_stringAsString(4, Double.class, "1.52147E2");
        test_stringAsString(5, File.class, "C:/temp/toto.xml");
        test_stringAsString(6, Float.class, "1.52147E2");
        test_stringAsString(7, Integer.class, "1524");
        test_stringAsString(8, Long.class, "1524");
        test_stringAsString(9, Short.class, "1524");
        test_stringAsString(10, String.class, "");
        test_stringAsString(11, URL.class, "http://java.sun.com");
        test_stringAsString(12, Boolean[].class, "true true false false false true");
        test_stringAsString(13, Double[].class, "1 2 3.2E-4 4 5 6 -7.23");
        test_stringAsString(14, Float[].class, "1 2 3.2E-4 4 5 6 -7.23");
        test_stringAsString(15, Integer[].class, "1 2 3 4 5 6666 -7");
        test_stringAsString(16, Long[].class, "1 2 3 4 5 6666 -7");
        test_stringAsString(17, Short[].class, "1 2 3 4 5 6666 -7");
        test_stringAsString(18, String[].class, "toto|titi|tata");
        test_stringAsString(19, double[].class, "1 2 3.2E-4 4 5 6 -7.23");
        test_stringAsString(20, float[].class, "1 2 3.2E-4 4 5 6 -7.23");
        test_stringAsString(21, int[].class, "1 2 3 4 5 6666 -7");
        test_stringAsString(22, long[].class, "1 2 3 4 5 6666 -7");
        test_stringAsString(23, short[].class, "1 2 3 4 5 6666 -7");
        test_stringAsStringForPrimitive(24, Boolean.TYPE, "true");
        test_stringAsStringForPrimitive(25, Double.TYPE, "1.52147E2");
        test_stringAsStringForPrimitive(26, Float.TYPE, "1.52147E2");
        test_stringAsStringForPrimitive(27, Integer.TYPE, "1524");
        test_stringAsStringForPrimitive(28, Long.TYPE, "1524");
        test_stringAsStringForPrimitive(29, Short.TYPE, "1524");
    }

    /**
     * Idem test_stringAsString except don't test type of result.
     *    Integer.Type => Integer.class value
     */
    protected void test_stringAsStringForPrimitive(int a_index, Class a_class, String a_string) throws GB_ConvertorException {
        GB_StringConvertors l_convertor = GB_StringConvertors.getInstance();
        Object l_value;
        String l_result;
        l_value = l_convertor.stringAsValue(a_string, a_class);
        l_result = l_convertor.valueAsString(l_value, a_class);
        if (a_string == null) {
            assertNull(a_index + .1 + " - value not null", l_value);
            assertNull(a_index + .2 + " - result not null", l_result);
        } else {
            assertEquals(a_index + .2 + " - " + a_string, a_string, l_result);
        }
        l_value = l_convertor.stringAsValueSafe(a_string, a_class, 0);
        l_result = l_convertor.valueAsStringSafe(l_value, a_class, 0);
        if (a_string == null) {
            assertNull(a_index + .3 + " - value not null", l_value);
            assertNull(a_index + .4 + " - result not null", l_result);
        } else {
            assertEquals(a_index + .4 + " - " + a_string, a_string, l_result);
        }
    }
}
