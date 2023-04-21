package org.jtestcase.core.converter;

import org.jtestcase.core.converter.SimpleTypeConverter;
import org.jtestcase.core.converter.TypeConversionException;
import org.jtestcase.core.type.AssertType;
import org.jtestcase.core.type.ParamType;
import junit.framework.TestCase;

public class TypeConverterTest extends TestCase {

    public void testConvertSimpleParam() {
        String content = "1";
        String name = "param1";
        String type = "java.lang.Integer";
        ParamType paramType = new ParamType(name, type, content, "", "", null, false, "");
        SimpleTypeConverter converter = new SimpleTypeConverter();
        Object converted = null;
        try {
            converted = converter._convertType(paramType);
        } catch (TypeConversionException e) {
            e.printStackTrace();
            fail("error converting type");
        }
        assertTrue("converted object is not of correct type", Integer.class.isInstance(converted));
        assertEquals("converted object has not the right value", 1, ((Integer) converted).intValue());
    }

    public void testConvertSimpleAssert() {
        String content = "1";
        String name = "param1";
        String action = "EQUALS";
        String type = "java.lang.Integer";
        AssertType assertType = new AssertType(name, type, action, content, "", "", null, false);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        Object converted = null;
        try {
            converted = converter._convertType(assertType);
        } catch (TypeConversionException e) {
            e.printStackTrace();
            fail("error converting type");
        }
        assertTrue("converted object is not of correct type", Integer.class.isInstance(converted));
        assertEquals("converted object has not the right value", 1, ((Integer) converted).intValue());
    }
}
