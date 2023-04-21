package com.ar4j.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.sql.Types;
import org.junit.Test;
import com.ar4j.type.SingleCharacterStringBooleanTypeConverter;

/**
 * Tests for the boolean to single character string converter, see we get correct values, and what happens with wrong input. 
 */
public class SingleCharacterStringBooleanTypeConverterTest {

    private SingleCharacterStringBooleanTypeConverter converter = new SingleCharacterStringBooleanTypeConverter();

    @Test
    public void testConverterSetup() {
        assertEquals("SQL type should be CHAR", Types.CHAR, converter.getSqlType());
        assertEquals("Storage type should be String", String.class, converter.getStorageType());
    }

    @Test
    public void convertToStorageTest() {
        assertTrue("Boolean false was not converted to the string \"N\"", "N".equals(converter.convertToStorage(Boolean.FALSE)));
        assertTrue("Boolean true was not converted to the string \"Y\"", "Y".equals(converter.convertToStorage(Boolean.TRUE)));
    }

    @Test
    public void convertFromStorageTest() {
        assertTrue("String N was not converted to the boolean false", Boolean.FALSE.equals(converter.convertFromStorage("N", Boolean.class)));
        assertTrue("String Y was not converted to the boolean true", Boolean.TRUE.equals(converter.convertFromStorage("Y", Boolean.class)));
    }

    @Test
    public void convertToAndFromTest() {
        assertTrue("Conversion from false and back didn't return false", Boolean.FALSE.equals(converter.convertFromStorage(converter.convertToStorage(Boolean.FALSE), Boolean.class)));
        assertTrue("Conversion from true and back didn't return true", Boolean.TRUE.equals(converter.convertFromStorage(converter.convertToStorage(Boolean.TRUE), Boolean.class)));
        assertTrue("Conversion from \"Y\" and back didn't return \"Y\"", "Y".equals(converter.convertToStorage(converter.convertFromStorage("Y", Boolean.class))));
        assertTrue("Conversion from \"N\" and back didn't return \"N\"", "N".equals(converter.convertToStorage(converter.convertFromStorage("N", Boolean.class))));
    }
}
