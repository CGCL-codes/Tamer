package org.openliberty.xmltooling.idsis.dap;

import org.openliberty.xmltooling.Konstantz;
import org.opensaml.xml.AbstractXMLObjectBuilder;
import org.opensaml.xml.schema.impl.XSStringImpl;
import org.opensaml.xml.schema.impl.XSStringMarshaller;
import org.opensaml.xml.schema.impl.XSStringUnmarshaller;

/**
 *  The Search filter supplied in &lt;Select&gt; SHOULD be passed verbatim to the underlying directory system 
 *  or repository. Full [RFC2254] language MUST be accepted by the ID-SIS-DAP service, though it need not 
 *  ensure that the underlying directory understands the filters. Extensions to filter language MAY be accepted.
 *  <p>
 *  http://www.ietf.org/rfc/rfc2254.txt
 *  <p>
 *  Example:<br />
 * 
 * @author asa
 *
 */
public class Filter extends XSStringImpl {

    public static final String LOCAL_NAME = "filter";

    protected Filter() {
        super(Konstantz.DAP_NS, LOCAL_NAME, Konstantz.DAP_PREFIX);
    }

    protected Filter(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    public static class Marshaller extends XSStringMarshaller {
    }

    public static class Unmarshaller extends XSStringUnmarshaller {
    }

    public static class Builder extends AbstractXMLObjectBuilder<Filter> {

        @Override
        public Filter buildObject(String namespaceURI, String localName, String namespacePrefix) {
            return new Filter(namespaceURI, localName, namespacePrefix);
        }
    }
}
