package com.ms.wsdiscovery.standard11;

import com.ms.wsdiscovery.WsDiscoveryConstants;
import com.ms.wsdiscovery.exception.WsDiscoveryException;
import com.ms.wsdiscovery.datatypes.WsDiscoveryActionTypes;
import com.ms.wsdiscovery.datatypes.WsDiscoveryNamespaces;
import com.skjegstad.soapoverudp.exceptions.SOAPOverUDPException;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Document;
import com.ms.wsdiscovery.exception.WsDiscoveryXMLException;
import com.ms.wsdiscovery.jaxb.standard11.wsdiscovery.AppSequenceType;
import com.ms.wsdiscovery.jaxb.standard11.wsdiscovery.ObjectFactory;
import com.skjegstad.soapoverudp.interfaces.ISOAPOverUDPMessage;
import com.skjegstad.soapoverudp.messages.SOAPOverUDPWSA200508Message;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.UnmarshalException;
import javax.xml.soap.SOAPEnvelope;

/**
 * Class representing a WS-Discovery SOAP message. <p>
 * The SOAP body can contain one of the following JAXB elements:
 * <li>HelloType</li>
 * <li>ByeType</li>
 * <li>ProbeType</li>
 * <li>ProbeMatchesType</li>
 * <li>ResolveType</li>
 * <li>ResolveMatchesType</li>
 * 
 * @param <E> JAXB element type in SOAP body.
 * @author Magnus Skjegstad
 */
public class WsDiscoveryS11SOAPMessage<E> extends SOAPOverUDPWSA200508Message implements ISOAPOverUDPMessage {

    /**
     * Namespace used by this message type. The enumerator contains
     * JAXB (un)marshaller and both WS-Addressing and WS-Discovery namespace URIs.
     */
    protected final WsDiscoveryNamespaces namespace = WsDiscoveryNamespaces.WS_DISCOVERY_2009_01;

    /**
     * WS-Discovery Instance ID
     */
    protected long instanceId;

    /**
     * WS-Discovery Sequence ID
     */
    protected String sequenceId;

    /**
     * WS-Discovery Message Number.
     */
    protected long messageNumber;

    protected boolean addAppSequence = false;

    /**
     * JAXB body of message.
     */
    protected JAXBElement<E> jaxbBody = null;

    /**
     * "Identifies a message within the context of a sequence number and an instance identifier."
     * This value is incremented each time this class is insantiated.
     */
    private static int lastMessageNumber = 0;

    /**
     * Create SOAP message of specified action type containing a JAXB element.
     * 
     * @param action Action type.
     * @param jaxb JAXB element.
     */
    public WsDiscoveryS11SOAPMessage(WsDiscoveryActionTypes action, JAXBElement<E> jaxb) throws SOAPOverUDPException {
        super(WsDiscoveryConstants.defaultSoapProtocol, WsDiscoveryConstants.defaultEncoding);
        instanceId = WsDiscoveryConstants.instanceId;
        sequenceId = "urn:uuid:" + WsDiscoveryConstants.sequenceId;
        messageNumber = ++lastMessageNumber;
        try {
            SOAPEnvelope e = soapMessage.getSOAPPart().getEnvelope();
            e.addNamespaceDeclaration("wsd", namespace.getWsDiscoveryNamespace());
            e.addNamespaceDeclaration("wsa", namespace.getWsAddressingNamespace());
        } catch (SOAPException ex) {
            throw new SOAPOverUDPException("Unable to read SOAP envelope");
        }
        this.setAction(action.toURI());
        this.setTo(URI.create("urn:docs-oasis-open-org:ws-dd:ns:discovery:2009:01"));
        this.setJAXBBody(jaxb);
    }

    /**
     * Create new instance based on an existing SOAP message.
     * @param soap SOAP message.
     * @throws WsDiscoveryXMLException on failure.
     */
    public WsDiscoveryS11SOAPMessage(SOAPMessage soap) throws SOAPOverUDPException {
        setSOAPMessage(soap);
    }

    public WsDiscoveryS11SOAPMessage(ISOAPOverUDPMessage message) throws SOAPOverUDPException, WsDiscoveryException {
        this.setAction(message.getAction());
        this.setRelationshipType(message.getRelationshipType());
        this.setDstAddress(message.getDstAddress());
        this.setDstPort(message.getDstPort());
        this.setSrcAddress(message.getSrcAddress());
        this.setSrcPort(message.getSrcPort());
        this.setMessageId(message.getMessageId());
        this.setReplyTo(message.getReplyTo());
        this.setTo(message.getTo());
        this.setRelatesTo(message.getRelatesTo());
        this.soapMessage = message.getSOAPMessage();
        readWSDHeader();
        readWSDBody();
    }

    private void removeWSDHeader() throws WsDiscoveryException {
        JAXBElement j = null;
        Unmarshaller u = namespace.getUnmarshaller();
        SOAPHeader header;
        try {
            header = soapMessage.getSOAPHeader();
        } catch (SOAPException ex) {
            throw new WsDiscoveryException("Unable to get SOAP header", ex);
        }
        for (Iterator<SOAPHeaderElement> i = header.examineAllHeaderElements(); i.hasNext(); ) {
            SOAPHeaderElement headerElement = i.next();
            try {
                j = (JAXBElement) u.unmarshal(headerElement);
            } catch (UnmarshalException ex) {
                continue;
            } catch (JAXBException ex) {
                throw new WsDiscoveryException("An unexpected error occured while unmarshalling header element " + headerElement.getTagName(), ex);
            }
            try {
                String tag = j.getName().getLocalPart();
                if (j.getValue().getClass().equals(AppSequenceType.class)) {
                    if (tag.equalsIgnoreCase("AppSequence")) {
                        headerElement.detachNode();
                    }
                }
            } catch (Exception ex) {
                throw new WsDiscoveryException("Got unexpected exception while extracting name tag from " + headerElement.getTagName() + (j == null ? " (j was null)" : ""), ex);
            }
        }
    }

    private void removeWSDBody() throws WsDiscoveryException {
        JAXBElement j = null;
        Unmarshaller u = namespace.getUnmarshaller();
        SOAPBody body;
        try {
            body = soapMessage.getSOAPBody();
        } catch (SOAPException ex) {
            throw new WsDiscoveryException("Unable to get WS-Discovery SOAP body", ex);
        }
        for (Iterator<Object> i = body.getChildElements(); i.hasNext(); ) {
            Object o = i.next();
            SOAPElement bodyElement;
            if (o instanceof SOAPElement) bodyElement = (SOAPElement) o; else continue;
            try {
                j = (JAXBElement) u.unmarshal(bodyElement);
            } catch (UnmarshalException ex) {
                continue;
            } catch (JAXBException ex) {
                throw new WsDiscoveryException("An unexpected error occured while unmarshalling WS-Discovery body element " + bodyElement.getTagName(), ex);
            }
            bodyElement.detachNode();
        }
    }

    /**
     * Read the WS-Discovery header from the SOAP message.
     */
    protected void readWSDHeader() throws WsDiscoveryException {
        Unmarshaller u = namespace.getUnmarshaller();
        SOAPHeader soapHeader;
        try {
            soapHeader = soapMessage.getSOAPHeader();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to read WS-Discovery SOAP header", ex);
        }
        JAXBElement j = null;
        for (Iterator<SOAPHeaderElement> i = soapHeader.examineAllHeaderElements(); i.hasNext(); ) {
            SOAPHeaderElement headerElement = i.next();
            try {
                j = (JAXBElement) u.unmarshal(headerElement);
            } catch (UnmarshalException ex) {
                continue;
            } catch (JAXBException ex) {
                throw new WsDiscoveryXMLException("Unable to unmarshal WS-Discovery header element " + headerElement.getTagName(), ex);
            }
            try {
                String tag = j.getName().getLocalPart();
                if (j.getValue().getClass().equals(AppSequenceType.class)) {
                    AppSequenceType a = (AppSequenceType) j.getValue();
                    if (tag.equals("AppSequence")) {
                        instanceId = a.getInstanceId();
                        messageNumber = a.getMessageNumber();
                        sequenceId = a.getSequenceId();
                    }
                }
            } catch (Exception ex) {
                throw new WsDiscoveryException("Got unexpected exception while extracting name tag from " + headerElement.getTagName() + (j == null ? " (j was null)" : ""), ex);
            }
        }
    }

    /**
     * Read the WS-Discovery body from the SOAP message.
     */
    protected void readWSDBody() throws WsDiscoveryException {
        Unmarshaller u = namespace.getUnmarshaller();
        Document soapDoc;
        SOAPBody soapBody;
        try {
            soapBody = soapMessage.getSOAPBody();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to read SOAP body", ex);
        }
        try {
            soapDoc = soapBody.extractContentAsDocument();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to extract document from SOAP body", ex);
        }
        try {
            jaxbBody = (JAXBElement<E>) u.unmarshal(soapDoc);
        } catch (JAXBException ex) {
            throw new WsDiscoveryXMLException("Unable to unmarshal SOAP document.", ex);
        }
        try {
            soapBody.addDocument(soapDoc);
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to reattach SOAP unmarshalled document to body", ex);
        }
    }

    protected void saveWSDHeader() throws WsDiscoveryException {
        removeWSDHeader();
        SOAPHeader header;
        String myns = namespace.getWsDiscoveryNamespace();
        try {
            header = soapMessage.getSOAPHeader();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to get SOAP header.");
        }
        {
            boolean found = false;
            for (Iterator<String> i = header.getVisibleNamespacePrefixes(); i.hasNext(); ) {
                String s = i.next();
                String ns = header.getNamespaceURI(s);
                if (ns.equalsIgnoreCase(myns)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                try {
                    header.addNamespaceDeclaration("wsd", myns);
                } catch (SOAPException ex) {
                    throw new WsDiscoveryException("Unable to add namespace " + myns + " to header.", ex);
                }
            }
        }
        if (addAppSequence) {
            ObjectFactory o = new ObjectFactory();
            AppSequenceType a = o.createAppSequenceType();
            a.setInstanceId(instanceId);
            a.setMessageNumber(messageNumber);
            a.setSequenceId(sequenceId);
            Marshaller m = namespace.getMarshaller();
            try {
                m.marshal(o.createAppSequence(a), header);
            } catch (JAXBException ex) {
                throw new WsDiscoveryXMLException("Unable to marshal AppSequence into SOAP header.", ex);
            }
        }
    }

    protected void saveWSDBody() throws WsDiscoveryException {
        removeWSDBody();
        SOAPBody body;
        try {
            body = this.soapMessage.getSOAPBody();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to get SOAP body.", ex);
        }
        for (Iterator i = body.getChildElements(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof SOAPElement) {
                SOAPElement e = (SOAPElement) o;
                for (Iterator<String> j = body.getVisibleNamespacePrefixes(); j.hasNext(); ) {
                    String prefix = j.next();
                    String ns = body.getNamespaceURI(prefix);
                    try {
                        e.addNamespaceDeclaration(prefix, ns);
                    } catch (SOAPException ex) {
                        throw new WsDiscoveryXMLException("Unable to declare namespace " + prefix + ":" + ns, ex);
                    }
                }
            }
        }
        Marshaller m = namespace.getMarshaller();
        if (jaxbBody != null) {
            try {
                m.marshal(jaxbBody, soapMessage.getSOAPBody());
            } catch (SOAPException ex) {
                throw new WsDiscoveryXMLException("Unable to get SOAP body", ex);
            } catch (JAXBException ex) {
                throw new WsDiscoveryXMLException("Unable to marshal JAXB into SOAP body.", ex);
            }
        }
    }

    /**
     * Get WS-Discovery Instance ID.
     * @return Instance ID
     */
    public long getInstanceId() {
        return instanceId;
    }

    /**
     * Get WS-Discovery Message number.
     * @return Message number
     */
    public long getMessageNumber() {
        return messageNumber;
    }

    /**
     * Get WS-Discovery Sequence ID.
     * @return Sequence ID
     */
    public String getSequenceId() {
        return sequenceId;
    }

    /**
     * Get the JAXB element that represents the body of the SOAP message.
     * @return JAXB element.
     */
    public E getJAXBBody() {
        return jaxbBody.getValue();
    }

    @Override
    public void setSOAPMessage(SOAPMessage soapMessage) throws SOAPOverUDPException {
        super.setSOAPMessage(soapMessage);
        try {
            readWSDHeader();
        } catch (WsDiscoveryException ex) {
            throw new SOAPOverUDPException("Could not read WS-Discovery header", ex);
        }
        SOAPBody body;
        try {
            body = this.soapMessage.getSOAPBody();
        } catch (SOAPException ex) {
            throw new SOAPOverUDPException("Unable to get SOAP body.", ex);
        }
        for (Iterator i = body.getChildElements(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof SOAPElement) {
                SOAPElement e = (SOAPElement) o;
                for (Iterator<String> j = body.getVisibleNamespacePrefixes(); j.hasNext(); ) {
                    String prefix = j.next();
                    String ns = body.getNamespaceURI(prefix);
                    try {
                        e.addNamespaceDeclaration(prefix, ns);
                    } catch (SOAPException ex) {
                        throw new SOAPOverUDPException("Unable to declare namespace " + prefix + ":" + ns);
                    }
                }
            }
        }
        try {
            readWSDBody();
        } catch (WsDiscoveryException ex) {
            throw new SOAPOverUDPException("Could not read WS-Discovery body", ex);
        }
    }

    public void setJAXBBody(JAXBElement<E> jaxb) {
        this.jaxbBody = jaxb;
        this.setSaveRequired();
    }

    /**
     * Redeclares namespaces from header and body in SOAP envelope.
     */
    private void redeclareNamespaces() throws WsDiscoveryXMLException {
        Map<String, String> foundNamespaces = new HashMap();
        SOAPBody body;
        SOAPHeader header;
        SOAPEnvelope envelope;
        try {
            body = soapMessage.getSOAPBody();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to get SOAP body", ex);
        }
        try {
            header = soapMessage.getSOAPHeader();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to get SOAP header", ex);
        }
        try {
            envelope = soapMessage.getSOAPPart().getEnvelope();
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to get SOAP envelope", ex);
        }
        for (Iterator<String> j = body.getVisibleNamespacePrefixes(); j.hasNext(); ) {
            String prefix = j.next();
            String ns = body.getNamespaceURI(prefix);
            foundNamespaces.put(prefix, ns);
        }
        for (Iterator<String> j = header.getVisibleNamespacePrefixes(); j.hasNext(); ) {
            String prefix = j.next();
            String ns = header.getNamespaceURI(prefix);
            foundNamespaces.put(prefix, ns);
        }
        try {
            for (Entry<String, String> n : foundNamespaces.entrySet()) envelope.addNamespaceDeclaration(n.getKey(), n.getValue().toString());
        } catch (SOAPException ex) {
            throw new WsDiscoveryXMLException("Unable to add namespaces to SOAP envelope.");
        }
    }

    @Override
    public void saveChanges() throws SOAPOverUDPException {
        if (this.saveRequired()) {
            super.saveChanges();
            try {
                this.saveWSDHeader();
            } catch (WsDiscoveryException ex) {
                throw new SOAPOverUDPException("Unable to save WS-Discovery header");
            }
            try {
                this.saveWSDBody();
            } catch (WsDiscoveryException ex) {
                throw new SOAPOverUDPException("Unable to save WS-Discovery body");
            }
        }
        try {
            redeclareNamespaces();
        } catch (WsDiscoveryXMLException ex) {
            throw new SOAPOverUDPException("Unable to redeclare namespaces in envelope.");
        }
    }

    public boolean getAddAppSequence() {
        return addAppSequence;
    }

    public void setAddAppSequence(boolean addAppSequence) {
        this.addAppSequence = addAppSequence;
    }
}
