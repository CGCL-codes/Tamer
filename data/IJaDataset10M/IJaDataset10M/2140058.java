package org.openliberty.xmltooling.epr;

import org.opensaml.xml.AbstractXMLObjectBuilder;

public class MetadataBuilder extends AbstractXMLObjectBuilder<Metadata> {

    @Override
    public Metadata buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new Metadata(namespaceURI, localName, namespacePrefix);
    }
}
