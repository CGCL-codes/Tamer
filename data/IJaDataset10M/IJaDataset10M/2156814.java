package org.juddi.handler;

import java.io.IOException;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.juddi.datatype.RegistryObject;
import org.juddi.datatype.publisher.PublisherID;
import org.juddi.datatype.request.GetPublisherDetail;
import org.juddi.util.XMLUtils;
import org.w3c.dom.Element;

/**
 * @author anou_mana@users.sourceforge.net
 */
public class GetPublisherDetailHandlerTests extends TestCase {

    private static final String TEST_ID = "juddi.handler.DeletePublisher.test";

    private GetPublisherDetailHandler handler = null;

    public GetPublisherDetailHandlerTests(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(GetPublisherDetailHandlerTests.class);
    }

    public void setUp() {
        HandlerMaker maker = HandlerMaker.getInstance();
        handler = (GetPublisherDetailHandler) maker.lookup(GetPublisherDetailHandler.TAG_NAME);
    }

    private RegistryObject getRegistryObject() {
        GetPublisherDetail object = new GetPublisherDetail();
        object.addPublisherID("guest");
        object.addPublisherID(new PublisherID("guest"));
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
        assertNotNull("Marshalled  GetPublisherDetail ", marshalledString);
    }

    public void testUnMarshal() {
        Element child = getMarshalledElement(null);
        RegistryObject regObject = handler.unmarshal(child);
        assertNotNull("UnMarshalled  GetPublisherDetail ", regObject);
    }

    public void testMarshUnMarshal() {
        Element child = getMarshalledElement(null);
        String marshalledString = this.getXMLString(child);
        assertNotNull("Marshalled  GetPublisherDetail ", marshalledString);
        RegistryObject regObject = handler.unmarshal(child);
        child = getMarshalledElement(regObject);
        String unMarshalledString = this.getXMLString(child);
        assertNotNull("Unmarshalled  GetPublisherDetail ", unMarshalledString);
        boolean equals = marshalledString.equals(unMarshalledString);
        assertEquals("Expected result: ", marshalledString, unMarshalledString);
    }
}
