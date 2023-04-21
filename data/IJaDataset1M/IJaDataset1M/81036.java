package org.juddi.handler;

import java.io.IOException;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.juddi.datatype.KeyedReference;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.assertion.PublisherAssertion;
import org.juddi.util.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author anou_mana@users.sourceforge.net
 */
public class PublisherAssertionHandlerTests extends TestCase {

    private static final String TEST_ID = "juddi.handler.DeletePublisher.test";

    private PublisherAssertionHandler handler = null;

    public PublisherAssertionHandlerTests(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PublisherAssertionHandlerTests.class);
    }

    public void setUp() {
        HandlerMaker maker = HandlerMaker.getInstance();
        handler = (PublisherAssertionHandler) maker.lookup(PublisherAssertionHandler.TAG_NAME);
    }

    private RegistryObject getRegistryObject() {
        PublisherAssertion object = new PublisherAssertion();
        object.setFromKey("b2f072e7-6013-4385-93b4-9c1c2ece1c8f");
        object.setToKey("115be72d-0c04-4b5f-a729-79a522629c19");
        object.setKeyedReference(new KeyedReference("uuid:8ff45356-acde-4a4c-86bf-f953611d20c6", "catBagKeyName2", "catBagKeyValue2"));
        return object;
    }

    private String getXMLString(Element element) {
        StringWriter writer = new StringWriter();
        XMLUtils.writeXML(element, writer);
        String xmlString = writer.toString();
        try {
            writer.close();
        } catch (IOException exp) {
        }
        return xmlString;
    }

    private Element getMarshalledElement(RegistryObject regObject) {
        Element parent = XMLUtils.newRootElement();
        Element child = null;
        if (regObject == null) regObject = this.getRegistryObject();
        handler.marshal(regObject, parent);
        child = (Element) parent.getFirstChild();
        parent.removeChild(child);
        return child;
    }

    public void testMarshal() {
        Element child = getMarshalledElement(null);
        String marshalledString = this.getXMLString(child);
        assertNotNull("Marshalled  PublisherAssertion ", marshalledString);
    }

    public void testUnMarshal() {
        Element child = getMarshalledElement(null);
        RegistryObject regObject = handler.unmarshal(child);
        assertNotNull("UnMarshalled  PublisherAssertion ", regObject);
    }

    public void testMarshUnMarshal() {
        Element child = getMarshalledElement(null);
        String marshalledString = this.getXMLString(child);
        assertNotNull("Marshalled  PublisherAssertion ", marshalledString);
        RegistryObject regObject = handler.unmarshal(child);
        child = getMarshalledElement(regObject);
        String unMarshalledString = this.getXMLString(child);
        assertNotNull("Unmarshalled  PublisherAssertion ", unMarshalledString);
        boolean equals = marshalledString.equals(unMarshalledString);
        assertEquals("Expected result: ", marshalledString, unMarshalledString);
    }
}
