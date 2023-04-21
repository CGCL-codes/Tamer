package org.openliberty.xmltooling.soapbinding;

import org.openliberty.xmltooling.Konstantz;
import org.openliberty.xmltooling.wsa.EndpointReference;
import org.openliberty.xmltooling.wsa.EndpointReferenceMarshaller;
import org.openliberty.xmltooling.wsa.EndpointReferenceUnmarshaller;
import org.opensaml.xml.AbstractXMLObjectBuilder;

public class InteractionService extends EndpointReference {

    public static final String LOCAL_NAME = "InteractionService";

    public InteractionService() {
        super(Konstantz.SB_NS, LOCAL_NAME, Konstantz.SB_PREFIX);
    }

    public InteractionService(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /**
     * Internal Unmarshaller
     * 
     * @author asa
     *
     */
    public static class Unmarshaller extends EndpointReferenceUnmarshaller {
    }

    /**
     * Internal Marshaller
     * 
     * @author asa
     */
    public static class Marshaller extends EndpointReferenceMarshaller {
    }

    /**
     * Internal Builder
     * 
     * @author asa
     *
     */
    public static class Builder extends AbstractXMLObjectBuilder<InteractionService> {

        @Override
        public InteractionService buildObject(String namespaceURI, String localName, String namespacePrefix) {
            return new InteractionService(namespaceURI, localName, namespacePrefix);
        }
    }
}
