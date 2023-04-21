package org.openliberty.xmltooling.idsis.dap;

import org.openliberty.xmltooling.subs.NotifyResponseType;
import org.openliberty.xmltooling.utility_2_0.ResponseType;
import org.opensaml.xml.AbstractXMLObjectBuilder;

/**
 * 
 * <pre>
 *   &lt;xs:complexType name="NotifyResponseType"&gt;
 *     &lt;xs:complexContent&gt;
 *       &lt;xs:extension base="subs:NotifyResponseType"/&gt;
 *     &lt;/xs:complexContent&gt;
 *   &lt;/xs:complexType&gt;
 * </pre>
 * @author asa
 *
 */
public class DAPNotifyResponse extends NotifyResponseType {

    public static String LOCAL_NAME = "NotifyResponse";

    protected DAPNotifyResponse(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /**
     * {@link NotifyResponseType} extends {@link ResponseType}
     * 
     * @author asa
     *
     */
    public static class Marshaller extends ResponseType.Marshaller {
    }

    /**
     * {@link NotifyResponseType} extends {@link ResponseType}
     * 
     * @author asa
     *
     */
    public static class Unmarshaller extends ResponseType.Unmarshaller {
    }

    /**
     * Internal Builder
     * 
     * @author asa
     *
     */
    public static class Builder extends AbstractXMLObjectBuilder<DAPNotifyResponse> {

        @Override
        public DAPNotifyResponse buildObject(String namespaceURI, String localName, String namespacePrefix) {
            return new DAPNotifyResponse(namespaceURI, localName, namespacePrefix);
        }
    }
}
