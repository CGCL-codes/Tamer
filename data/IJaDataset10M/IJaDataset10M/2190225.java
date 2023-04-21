package com.google.code.configprocessor.util;

import static org.junit.Assert.*;
import org.apache.commons.lang.*;
import org.junit.*;

public class PropertiesUtilsTest {

    @Test
    public void getAsTextSimple() {
        executeExportingTest("value", "value");
    }

    @Test
    public void getAsTextNullValue() {
        executeExportingTest(null, null);
    }

    @Test
    public void getAsTextEmptyValue() {
        executeExportingTest("", StringUtils.EMPTY);
    }

    @Test
    public void getAsTextEscapingBackslashes() {
        executeExportingTest("c:\\\\file.txt", "c:\\file.txt");
    }

    @Test
    public void getAsTextEscapingLineBreaks() {
        executeExportingTest("value1\\\rvalue2", "value1\rvalue2");
        executeExportingTest("value1\\\nvalue2", "value1\nvalue2");
        executeExportingTest("value1\\\r\nvalue2", "value1\r\nvalue2");
    }

    protected void executeExportingTest(String expected, String value) {
        assertEquals(expected, PropertiesUtils.escapePropertyValue(value));
    }
}
