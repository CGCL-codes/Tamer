package net.sf.istcontract.wsimport.addressing;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.stream.buffer.XMLStreamBufferSource;
import com.sun.xml.stream.buffer.stax.StreamWriterBufferCreator;
import net.sf.istcontract.wsimport.addressing.v200408.MemberSubmissionAddressingConstants;
import net.sf.istcontract.wsimport.api.addressing.AddressingVersion;
import net.sf.istcontract.wsimport.developer.MemberSubmissionEndpointReference;
import net.sf.istcontract.wsimport.util.DOMUtil;
import net.sf.istcontract.wsimport.util.xml.XmlUtil;
import net.sf.istcontract.wsimport.wsdl.parser.WSDLConstants;
import org.w3c.dom.*;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rama Pulavarthi
 */
public class EndpointReferenceUtil {

    /**
     * Gives the EPR based on the clazz. It may need to perform tranformation from
     * W3C EPR to MS EPR or vise-versa.
     */
    public static <T extends EndpointReference> T transform(Class<T> clazz, @NotNull EndpointReference epr) {
        assert epr != null;
        if (clazz.isAssignableFrom(W3CEndpointReference.class)) {
            if (epr instanceof W3CEndpointReference) {
                return (T) epr;
            } else if (epr instanceof MemberSubmissionEndpointReference) {
                return (T) toW3CEpr((MemberSubmissionEndpointReference) epr);
            }
        } else if (clazz.isAssignableFrom(MemberSubmissionEndpointReference.class)) {
            if (epr instanceof W3CEndpointReference) {
                return (T) toMSEpr((W3CEndpointReference) epr);
            } else if (epr instanceof MemberSubmissionEndpointReference) {
                return (T) epr;
            }
        }
        throw new WebServiceException("Unknwon EndpointReference: " + epr.getClass());
    }

    private static W3CEndpointReference toW3CEpr(MemberSubmissionEndpointReference msEpr) {
        StreamWriterBufferCreator writer = new StreamWriterBufferCreator();
        w3cMetadataWritten = false;
        try {
            writer.writeStartDocument();
            writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "EndpointReference", AddressingVersion.W3C.nsUri);
            writer.writeNamespace(AddressingVersion.W3C.getPrefix(), AddressingVersion.W3C.nsUri);
            writer.writeStartElement(AddressingVersion.W3C.getPrefix(), W3CAddressingConstants.WSA_ADDRESS_NAME, AddressingVersion.W3C.nsUri);
            writer.writeCharacters(msEpr.addr.uri);
            writer.writeEndElement();
            if ((msEpr.referenceProperties != null && msEpr.referenceProperties.elements.size() > 0) || (msEpr.referenceParameters != null && msEpr.referenceParameters.elements.size() > 0)) {
                writer.writeStartElement(AddressingVersion.W3C.getPrefix(), "ReferenceParameters", AddressingVersion.W3C.nsUri);
                if (msEpr.referenceProperties != null) {
                    for (Element e : msEpr.referenceProperties.elements) {
                        DOMUtil.serializeNode(e, writer);
                    }
                }
                if (msEpr.referenceParameters != null) {
                    for (Element e : msEpr.referenceParameters.elements) {
                        DOMUtil.serializeNode(e, writer);
                    }
                }
                writer.writeEndElement();
            }
            Element wsdlElement = null;
            if ((msEpr.elements != null) && (msEpr.elements.size() > 0)) {
                for (Element e : msEpr.elements) {
                    if (e.getNamespaceURI().equals(MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI()) && e.getLocalName().equals(MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart())) {
                        NodeList nl = e.getElementsByTagNameNS(WSDLConstants.NS_WSDL, WSDLConstants.QNAME_DEFINITIONS.getLocalPart());
                        if (nl != null) wsdlElement = (Element) nl.item(0);
                    }
                }
            }
            if (wsdlElement != null) {
                DOMUtil.serializeNode(wsdlElement, writer);
            }
            if (w3cMetadataWritten) writer.writeEndElement();
            if ((msEpr.elements != null) && (msEpr.elements.size() > 0)) {
                for (Element e : msEpr.elements) {
                    if (e.getNamespaceURI().equals(WSDLConstants.NS_WSDL) && e.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) {
                    }
                    DOMUtil.serializeNode(e, writer);
                }
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
        } catch (XMLStreamException e) {
            throw new WebServiceException(e);
        }
        return new W3CEndpointReference(new XMLStreamBufferSource(writer.getXMLStreamBuffer()));
    }

    private static boolean w3cMetadataWritten = false;

    private static void writeW3CMetadata(StreamWriterBufferCreator writer) throws XMLStreamException {
        if (!w3cMetadataWritten) {
            writer.writeStartElement(AddressingVersion.W3C.getPrefix(), W3CAddressingConstants.WSA_METADATA_NAME, AddressingVersion.W3C.nsUri);
            w3cMetadataWritten = true;
        }
    }

    private static MemberSubmissionEndpointReference toMSEpr(W3CEndpointReference w3cEpr) {
        DOMResult result = new DOMResult();
        w3cEpr.writeTo(result);
        Node eprNode = result.getNode();
        Element e = DOMUtil.getFirstElementChild(eprNode);
        if (e == null) return null;
        MemberSubmissionEndpointReference msEpr = new MemberSubmissionEndpointReference();
        NodeList nodes = e.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) nodes.item(i);
                if (child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri) && child.getLocalName().equals(W3CAddressingConstants.WSA_ADDRESS_NAME)) {
                    if (msEpr.addr == null) msEpr.addr = new MemberSubmissionEndpointReference.Address();
                    msEpr.addr.uri = XmlUtil.getTextForNode(child);
                    msEpr.addr.attributes = getAttributes(child);
                } else if (child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri) && child.getLocalName().equals("ReferenceParameters")) {
                    NodeList refParams = child.getChildNodes();
                    for (int j = 0; j < refParams.getLength(); j++) {
                        if (refParams.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            if (msEpr.referenceParameters == null) {
                                msEpr.referenceParameters = new MemberSubmissionEndpointReference.Elements();
                                msEpr.referenceParameters.elements = new ArrayList<Element>();
                            }
                            msEpr.referenceParameters.elements.add((Element) refParams.item(j));
                        }
                    }
                } else if (child.getNamespaceURI().equals(AddressingVersion.W3C.nsUri) && child.getLocalName().equals(W3CAddressingConstants.WSA_METADATA_NAME)) {
                    NodeList metadata = child.getChildNodes();
                    for (int j = 0; j < metadata.getLength(); j++) {
                        Node node = metadata.item(j);
                        if (node.getNodeType() != Node.ELEMENT_NODE) continue;
                        Element elm = (Element) node;
                        if (elm.getNamespaceURI().equals(AddressingVersion.W3C.wsdlNsUri) && elm.getLocalName().equals(W3CAddressingConstants.WSAW_SERVICENAME_NAME)) {
                            msEpr.serviceName = new MemberSubmissionEndpointReference.ServiceNameType();
                            msEpr.serviceName.portName = elm.getAttribute(W3CAddressingConstants.WSAW_ENDPOINTNAME_NAME);
                            String service = elm.getTextContent();
                            String prefix = XmlUtil.getPrefix(service);
                            String name = XmlUtil.getLocalPart(service);
                            if (name == null) continue;
                            if (prefix != null) {
                                String ns = elm.lookupNamespaceURI(prefix);
                                if (ns != null) msEpr.serviceName.name = new QName(ns, name, prefix);
                            } else {
                                msEpr.serviceName.name = new QName(null, name);
                            }
                            msEpr.serviceName.attributes = getAttributes(elm);
                        } else if (elm.getNamespaceURI().equals(AddressingVersion.W3C.wsdlNsUri) && elm.getLocalName().equals(W3CAddressingConstants.WSAW_INTERFACENAME_NAME)) {
                            msEpr.portTypeName = new MemberSubmissionEndpointReference.AttributedQName();
                            String portType = elm.getTextContent();
                            String prefix = XmlUtil.getPrefix(portType);
                            String name = XmlUtil.getLocalPart(portType);
                            if (name == null) continue;
                            if (prefix != null) {
                                String ns = elm.lookupNamespaceURI(prefix);
                                if (ns != null) msEpr.portTypeName.name = new QName(ns, name, prefix);
                            } else {
                                msEpr.portTypeName.name = new QName(null, name);
                            }
                            msEpr.portTypeName.attributes = getAttributes(elm);
                        } else if (elm.getNamespaceURI().equals(WSDLConstants.NS_WSDL) && elm.getLocalName().equals(WSDLConstants.QNAME_DEFINITIONS.getLocalPart())) {
                            Document doc = DOMUtil.createDom();
                            Element mexEl = doc.createElementNS(MemberSubmissionAddressingConstants.MEX_METADATA.getNamespaceURI(), MemberSubmissionAddressingConstants.MEX_METADATA.getPrefix() + ":" + MemberSubmissionAddressingConstants.MEX_METADATA.getLocalPart());
                            Element metadataEl = doc.createElementNS(MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getNamespaceURI(), MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getPrefix() + ":" + MemberSubmissionAddressingConstants.MEX_METADATA_SECTION.getLocalPart());
                            metadataEl.setAttribute(MemberSubmissionAddressingConstants.MEX_METADATA_DIALECT_ATTRIBUTE, MemberSubmissionAddressingConstants.MEX_METADATA_DIALECT_VALUE);
                            metadataEl.appendChild(elm);
                            mexEl.appendChild(metadataEl);
                        } else {
                            if (msEpr.elements == null) {
                                msEpr.elements = new ArrayList<Element>();
                            }
                            msEpr.elements.add(elm);
                        }
                    }
                } else {
                    if (msEpr.elements == null) {
                        msEpr.elements = new ArrayList<Element>();
                    }
                    msEpr.elements.add((Element) child);
                }
            } else if (nodes.item(i).getNodeType() == Node.ATTRIBUTE_NODE) {
                Node n = nodes.item(i);
                if (msEpr.attributes == null) {
                    msEpr.attributes = new HashMap<QName, String>();
                    String prefix = fixNull(n.getPrefix());
                    String ns = fixNull(n.getNamespaceURI());
                    String localName = n.getLocalName();
                    msEpr.attributes.put(new QName(ns, localName, prefix), n.getNodeValue());
                }
            }
        }
        return msEpr;
    }

    private static Map<QName, String> getAttributes(Node node) {
        Map<QName, String> attribs = null;
        NamedNodeMap nm = node.getAttributes();
        for (int i = 0; i < nm.getLength(); i++) {
            if (attribs == null) attribs = new HashMap<QName, String>();
            Node n = nm.item(i);
            String prefix = fixNull(n.getPrefix());
            String ns = fixNull(n.getNamespaceURI());
            String localName = n.getLocalName();
            if (prefix.equals("xmlns") || prefix.length() == 0 && localName.equals("xmlns")) continue;
            if (!localName.equals(W3CAddressingConstants.WSAW_ENDPOINTNAME_NAME)) attribs.put(new QName(ns, localName, prefix), n.getNodeValue());
        }
        return attribs;
    }

    @NotNull
    private static String fixNull(@Nullable String s) {
        if (s == null) return ""; else return s;
    }
}
