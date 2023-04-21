package com.volantis.mcs.xdime;

import com.volantis.mcs.xdime.initialisation.ElementFactoryMap;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMock;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Verifies the behaviour of {@link XDIMEElementHandler}.
 */
public class XDIMEElementHandlerTestCase extends TestCaseAbstract {

    private static final String defaultNamespace = "mcs";

    private ElementFactoryMock factoryMock;

    private ElementType elementType;

    private XDIMEContextInternalMock contextMock;

    /**
     * Handler to use when testing. Will be recreated for each test.
     */
    XDIMEElementHandler handler;

    public void setUp() throws Exception {
        super.setUp();
        factoryMock = new ElementFactoryMock("factoryMock", expectations);
        ElementFactoryMapBuilder builder = new ElementFactoryMapBuilder();
        elementType = new ElementType("foo", "bar");
        builder.addMapping(elementType, factoryMock);
        ElementFactoryMap map = builder.buildFactoryMap();
        handler = new XDIMEElementHandler(map);
        contextMock = new XDIMEContextInternalMock("contextMock", expectations);
    }

    public void tearDown() {
        handler = null;
    }

    public void testCreateXDIMEAttributesWithNamespace() throws XDIMEException {
        AttributesImpl saxAttributes = new AttributesImpl();
        for (int i = 0; i < 4; i++) {
            String index = Integer.toString(i);
            saxAttributes.addAttribute(defaultNamespace, index, "mcs:" + index, "String", "value" + index);
        }
        XDIMEAttributesImpl attributes = (XDIMEAttributesImpl) handler.createXDIMEAttributes(elementType, saxAttributes);
        assertNotNull(attributes);
        int newLength = attributes.getLength();
        assertEquals(saxAttributes.getLength(), newLength);
        for (int i = 0; i < newLength; i++) {
            String index = Integer.toString(i);
            assertEquals("value" + index, attributes.getValue(defaultNamespace, index));
        }
    }

    public void testCreateXDIMEAttributesWithoutNamespace() throws XDIMEException {
        AttributesImpl saxAttributes = new AttributesImpl();
        for (int i = 0; i < 4; i++) {
            String index = Integer.toString(i);
            saxAttributes.addAttribute("", index, index, "String", "value" + index);
        }
        XDIMEAttributesImpl attributes = (XDIMEAttributesImpl) handler.createXDIMEAttributes(elementType, saxAttributes);
        assertNotNull(attributes);
        int newLength = attributes.getLength();
        assertEquals(saxAttributes.getLength(), newLength);
        for (int i = 0; i < newLength; i++) {
            String index = Integer.toString(i);
            assertEquals("value" + index, attributes.getValue("", index));
        }
    }

    public void testCreateXDIMEAttributesFromEmptySAXAttributes() throws XDIMEException {
        AttributesImpl saxAttributes = new AttributesImpl();
        XDIMEAttributesImpl attributes = (XDIMEAttributesImpl) handler.createXDIMEAttributes(elementType, saxAttributes);
        assertNotNull(attributes);
        int newLength = attributes.getLength();
        assertEquals(saxAttributes.getLength(), newLength);
        assertEquals(0, newLength);
    }

    public void testCreateXDIMEElement() throws XDIMEException {
        final XDIMEElementInternalMock elementMock = new XDIMEElementInternalMock("elementMock", expectations);
        factoryMock.expects.createElement(contextMock).returns(elementMock);
        XDIMEElement element = handler.createXDIMEElement(elementType, contextMock);
        assertNotNull(element);
    }

    public void testCreateXDIMEElementWithNullNamespace() {
        try {
            handler.createXDIMEElement(null, contextMock);
            fail("Namespaces must be present");
        } catch (XDIMEException e) {
        }
    }

    public void testCreateXDIMEUnknownElement() {
        try {
            ElementType unknownElementType = new ElementType("unknown", "abc");
            handler.createXDIMEElement(unknownElementType, contextMock);
            fail("Handler should recognise that element is not supported");
        } catch (XDIMEException e) {
        }
    }
}
