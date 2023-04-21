package org.datanucleus.sql4o;

import org.datanucleus.sql4o.Converter;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * 
 */
public class ConverterTest extends TestCase {

    public void testConvertFromStringBoolean() throws Exception {
        Class toGet = Boolean.class;
        Object expected = Boolean.FALSE;
        assertConvert(toGet, "false", expected);
        assertConvert(Boolean.TYPE, "false", expected);
    }

    private void assertConvert(Class toGet, String input, Object expected) throws Exception {
        Object ob = Converter.convertFromString(toGet, input);
        Assert.assertEquals(expected, ob);
    }
}
