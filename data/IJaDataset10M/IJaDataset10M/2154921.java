package org.dbe.composer.wfengine.bpel.impl.endpoints;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.activebpel.rt.IAeConstants;
import org.dbe.composer.wfengine.bpel.ISdlEndpointReference;
import org.dbe.composer.wfengine.bpel.def.ISdlBPELConstants;
import org.dbe.composer.wfengine.util.SdlXmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 * Endpoint serializer for WS-Addressing endpoints.
 */
public class SdlWSAddressingEndpointSerializer implements ISdlEndpointSerializer {

    /** singleton instance */
    private static final SdlWSAddressingEndpointSerializer sSingleton = new SdlWSAddressingEndpointSerializer();

    /**
    * Private ctor for singleton pattern
    */
    private SdlWSAddressingEndpointSerializer() {
    }

    /**
    * Getter for the singleton
    */
    public static SdlWSAddressingEndpointSerializer getInstance() {
        return sSingleton;
    }

    public DocumentFragment serializeEndpoint(ISdlEndpointReference aRef) {
        Document doc = SdlXmlUtil.newDocument();
        Map qnameToPrefixMap = new HashMap();
        DocumentFragment frag = doc.createDocumentFragment();
        Element er = SdlXmlUtil.addElementNS(frag, ISdlBPELConstants.WSA_NAMESPACE_URI, "EndpointReference", null);
        er.setAttribute("xmlns", ISdlBPELConstants.WSA_NAMESPACE_URI);
        SdlXmlUtil.addElementNS(er, ISdlBPELConstants.WSA_NAMESPACE_URI, "Address", aRef.getAddress());
        if (!aRef.getProperties().isEmpty()) {
            Element props = SdlXmlUtil.addElementNS(er, ISdlBPELConstants.WSA_NAMESPACE_URI, "ReferenceProperties", null);
            for (Iterator iter = aRef.getProperties().keySet().iterator(); iter.hasNext(); ) {
                QName key = (QName) iter.next();
                String value = aRef.getProperties().get(key).toString();
                SdlXmlUtil.addElementNS(props, key.getNamespaceURI(), getQNameStr(key, qnameToPrefixMap), value);
            }
        }
        if (aRef.getPortType() != null) {
            String portType = getQNameStr(aRef.getPortType(), qnameToPrefixMap);
            SdlXmlUtil.addElementNS(er, ISdlBPELConstants.WSA_NAMESPACE_URI, "PortType", portType);
        }
        if (aRef.getServiceName() != null) {
            String serviceName = getQNameStr(aRef.getServiceName(), qnameToPrefixMap);
            Element svcName = SdlXmlUtil.addElementNS(er, ISdlBPELConstants.WSA_NAMESPACE_URI, "ServiceName", serviceName);
            if (aRef.getServicePort() != null) svcName.setAttribute("PortName", aRef.getServicePort());
        }
        if (aRef.getPolicies().size() > 0) {
        }
        for (Iterator iter = aRef.getExtensibilityElements(); iter.hasNext(); ) er.appendChild(doc.importNode((Element) iter.next(), true));
        for (Iterator iter = qnameToPrefixMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Entry) iter.next();
            String namespace = (String) entry.getKey();
            String prefix = (String) entry.getValue();
            er.setAttributeNS(IAeConstants.W3C_XMLNS, "xmlns:" + prefix, namespace);
        }
        return frag;
    }

    /**
    * Converts a QName to a String using the prefix already found in the map or
    * it'll generate a new prefix and add it to the map for this QName
    * @param aName
    * @param qnameToPrefixMap
    */
    private String getQNameStr(QName aName, Map qnameToPrefixMap) {
        String prefix = (String) qnameToPrefixMap.get(aName.getNamespaceURI());
        if (prefix == null) {
            prefix = "ns" + (qnameToPrefixMap.size() + 1);
            qnameToPrefixMap.put(aName.getNamespaceURI(), prefix);
        }
        return prefix + ":" + aName.getLocalPart();
    }
}
