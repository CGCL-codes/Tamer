package com.spextreme.nunit.xml.internal;

import static org.junit.Assert.assertNull;
import org.jdom.Element;
import org.junit.Test;

/**
 * Tests the culture info object.
 */
public class CultureInfoProcessorTest {

    /**
	 * Test method for
	 * {@link com.spextreme.nunit.xml.internal.CultureInfoProcessor#parseElement(org.jdom.Element)}.
	 */
    @Test
    public void testParseElement() {
        assertNull(new CultureInfoProcessor().parseElement(new Element("Test")));
    }
}
