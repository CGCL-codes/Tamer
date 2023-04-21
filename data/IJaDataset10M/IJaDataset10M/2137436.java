package com.ail.openquote;

import com.ail.core.Attribute;
import junit.framework.TestCase;

public class FactTest extends TestCase {

    public void testFactTypeConversionStringToNumber() {
        Fact fact;
        fact = new Fact("id", new Attribute("id", "21", "string"));
        assertEquals(21.0, fact.getNumericValue());
        fact = new Fact("id", "21");
        assertEquals(21.0, fact.getNumericValue());
    }
}
