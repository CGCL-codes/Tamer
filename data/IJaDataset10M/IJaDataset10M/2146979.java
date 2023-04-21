package org.juddi.handler;

import java.io.IOException;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.request.FindQualifier;
import org.juddi.datatype.request.FindQualifiers;
import org.juddi.util.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author anou_mana@users.sourceforge.net
 */
public class FindQualifiersHandlerTests extends TestCase {

    private static final String TEST_ID = "juddi.handler.DeletePublisher.test";

    private FindQualifiersHandler handler = null;

    public FindQualifiersHandlerTests(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(FindQualifiersHandlerTests.class);
    }

    public void setUp() {
        HandlerMaker maker = HandlerMaker.getInstance();
        handler = (FindQualifiersHandler) maker.lookup(FindQualifiersHandler.TAG_NAME);
    }

    private RegistryObject getRegistryObject() {
        FindQualifiers object = new FindQualifiers();
        object.addFindQualifier(new FindQualifier(FindQualifier.sortByDateAsc));
        object.addFindQualifier(new FindQualifier(FindQualifier.andAllKeys));
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
        assertNotNull("Marshalled  FindQualifiers ", marshalledString);
    }

    public void testUnMarshal() {
        Element child = getMarshalledElement(null);
        RegistryObject regObject = handler.unmarshal(child);
        assertNotNull("UnMarshalled  FindQualifiers ", regObject);
    }

    public void testMarshUnMarshal() {
        Element child = getMarshalledElement(null);
        String marshalledString = this.getXMLString(child);
        assertNotNull("Marshalled  FindQualifiers ", marshalledString);
        RegistryObject regObject = handler.unmarshal(child);
        child = getMarshalledElement(regObject);
        String unMarshalledString = this.getXMLString(child);
        assertNotNull("Unmarshalled  FindQualifiers ", unMarshalledString);
        boolean equals = marshalledString.equals(unMarshalledString);
        assertEquals("Expected result: ", marshalledString, unMarshalledString);
    }
}
