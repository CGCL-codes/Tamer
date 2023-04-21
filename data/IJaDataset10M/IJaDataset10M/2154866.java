package org.juddi.handler;

import java.io.IOException;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.juddi.datatype.IdentifierBag;
import org.juddi.datatype.KeyedReference;
import org.juddi.datatype.RegistryObject;
import org.juddi.util.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author anou_mana@users.sourceforge.net
 */
public class IdentifierBagHandlerTests extends TestCase {

    private static final String TEST_ID = "juddi.handler.DeletePublisher.test";

    private IdentifierBagHandler handler = null;

    public IdentifierBagHandlerTests(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(IdentifierBagHandlerTests.class);
    }

    public void setUp() {
        HandlerMaker maker = HandlerMaker.getInstance();
        handler = (IdentifierBagHandler) maker.lookup(IdentifierBagHandler.TAG_NAME);
    }

    private RegistryObject getRegistryObject() {
        IdentifierBag object = new IdentifierBag();
        object.addKeyedReference(new KeyedReference("idBagKeyName", "idBagKeyValue"));
        object.addKeyedReference(new KeyedReference("uuid:3860b975-9e0c-4cec-bad6-87dfe00e3864", "idBagKeyName2", "idBagKeyValue2"));
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
        assertNotNull("Marshalled  IdentifierBag ", marshalledString);
    }

    public void testUnMarshal() {
        Element child = getMarshalledElement(null);
        RegistryObject regObject = handler.unmarshal(child);
        assertNotNull("UnMarshalled  IdentifierBag ", regObject);
    }

    public void testMarshUnMarshal() {
        Element child = getMarshalledElement(null);
        String marshalledString = this.getXMLString(child);
        assertNotNull("Marshalled  IdentifierBag ", marshalledString);
        RegistryObject regObject = handler.unmarshal(child);
        child = getMarshalledElement(regObject);
        String unMarshalledString = this.getXMLString(child);
        assertNotNull("Unmarshalled  IdentifierBag ", unMarshalledString);
        boolean equals = marshalledString.equals(unMarshalledString);
        assertEquals("Expected result: ", marshalledString, unMarshalledString);
    }
}
