package com.topcoder.util.dbconverter;

import junit.framework.TestCase;

public class FormatterConfigurationExceptionTest extends TestCase {

    public void testFormatterConfigurationExceptionThrowable() {
        FormatterConfigurationException e = new FormatterConfigurationException(new Throwable());
        assertNotNull(e);
    }

    public void testFormatterConfigurationExceptionString() {
        FormatterConfigurationException e = new FormatterConfigurationException("message");
        assertNotNull(e);
    }

    public void testFormatterConfigurationExceptionStringThrowable() {
        FormatterConfigurationException e = new FormatterConfigurationException("message", new Throwable());
        assertNotNull(e);
    }
}
