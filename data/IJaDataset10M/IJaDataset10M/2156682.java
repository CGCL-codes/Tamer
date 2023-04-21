package net.sf.doolin.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestIdentityConverter {

    @Test
    public void testConvert() {
        IdentityConverter<Object> converter = new IdentityConverter<Object>();
        Object o = new Object();
        assertTrue(o == converter.convert(o));
    }

    @Test
    public void testConvertNull() {
        IdentityConverter<Object> converter = new IdentityConverter<Object>();
        assertNull(converter.convert(null));
    }
}
