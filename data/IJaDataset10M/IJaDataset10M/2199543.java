package org.openliberty.xmltooling.disco;

import org.openliberty.xmltooling.Konstantz;
import org.opensaml.xml.AbstractXMLObjectBuilder;

public class DiscoQueryBuilder extends AbstractXMLObjectBuilder<DiscoQuery> {

    public DiscoQuery buildObject() {
        return buildObject(Konstantz.DISCO_NS, DiscoQuery.LOCAL_NAME, Konstantz.DISCO_PREFIX);
    }

    @Override
    public DiscoQuery buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new DiscoQuery(namespaceURI, localName, namespacePrefix);
    }
}
