package org.databene.commons.converter;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the {@link String2CharConverter}.<br/><br/>
 * Created: 29.07.2010 17:24:32
 * @since 0.6.3
 * @author Volker Bergmann
 */
public class String2CharConverterTest extends AbstractConverterTest {

    private static final String2CharConverter CONVERTER = new String2CharConverter();

    public String2CharConverterTest() {
        super(String2CharConverter.class);
    }

    @Test
    public void testTypes() {
        assertEquals(String.class, CONVERTER.getSourceType());
        assertEquals(Character.class, CONVERTER.getTargetType());
    }

    @Test
    public void testStandardConversions() {
        assertEquals('A', CONVERTER.convert("A").charValue());
        assertEquals('1', CONVERTER.convert("1").charValue());
    }

    @Test
    public void testNullConversion() {
        assertNull(CONVERTER.convert(null));
    }
}
