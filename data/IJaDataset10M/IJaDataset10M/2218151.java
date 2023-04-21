package com.phloc.commons.microdom.convert;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.typeconvert.TypeConverterException;

/**
 * Test class for class {@link MicroTypeConverter}.
 * 
 * @author philip
 */
public final class MicroTypeConverterTest {

    @Test
    public void testConvertToMicroElement() {
        assertNull(MicroTypeConverter.convertToMicroElement(null, "tag"));
        try {
            MicroTypeConverter.convertToMicroElement("value", "");
            fail();
        } catch (final IllegalArgumentException ex) {
        }
    }

    @Test
    public void testConvertToNative() {
        assertNull(MicroTypeConverter.convertToNative(null, String.class));
        try {
            MicroTypeConverter.convertToNative(new MicroElement("any"), null);
            fail();
        } catch (final NullPointerException ex) {
        }
        try {
            MicroTypeConverter.convertToNative(new MicroElement("any"), IHasID.class);
            fail();
        } catch (final TypeConverterException ex) {
        }
    }
}
