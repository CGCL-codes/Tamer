package com.objetdirect.gwt.umlapi.client.umlcomponents;

import junit.framework.TestCase;

public class TestUMLObjectAttribute extends TestCase {

    /**
	 * These two default values are the one return by the parser in the created UMLObjectAttribute when it is not able
	 * to success the parsing. DO NOT USE these values in the parsed chains for testing purpose.
	 */
    @SuppressWarnings("unused")
    private static final String DEFAULT_ATTRIBUTE_NAME = "attributeName";

    @SuppressWarnings("unused")
    private static final String DEFAULT_VALUENAME = "value";

    private static final String VALID_ATTRIBUTE_STRING_1 = "attribute = \"testValue\"";

    private static final String ATTRIBUTE_1 = "attribute";

    private static final String STRING_VALUE_1 = "testValue";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testParseAttributeString() {
        UMLObjectAttribute umlObjectAttribute = UMLObjectAttribute.parseAttribute(VALID_ATTRIBUTE_STRING_1);
        String attribute = umlObjectAttribute.getAttributeName();
        String value = umlObjectAttribute.getValue();
        assertEquals(ATTRIBUTE_1, attribute);
        assertEquals(STRING_VALUE_1, value);
    }
}
