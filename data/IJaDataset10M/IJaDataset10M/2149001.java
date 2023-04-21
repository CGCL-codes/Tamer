package org.soda.dpws.soap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Namespace;
import org.soda.dpws.addressing.EndpointReference;
import org.soda.dpws.addressing.WSAConstants;
import org.soda.dpws.exchange.AbstractMessage;
import org.soda.dpws.exchange.InMessage;
import org.soda.dpws.exchange.MessageSerializer;
import org.soda.dpws.exchange.OutMessage;
import org.soda.dpws.fault.DPWSFault;
import org.soda.dpws.fault.Soap12FaultSerializer;
import org.soda.dpws.internal.DPWSContextImpl;
import org.soda.dpws.registry.discovery.WSDConstants;
import org.soda.dpws.util.jdom.JDOMReader;
import org.soda.dpws.util.serialize.XMLSerializer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * 
 * 
 */
public class SoapSerializer implements MessageSerializer {

    /**
   * 
   */
    public static final String SERIALIZE_PROLOG = "xfire.serializeProlog";

    private MessageSerializer serializer;

    /**
   * @param serializer
   */
    public SoapSerializer(MessageSerializer serializer) {
        this.serializer = serializer;
    }

    /**
   * @return the {@link MessageSerializer}
   */
    public MessageSerializer getSerializer() {
        if (serializer == null) return new Soap12FaultSerializer();
        return serializer;
    }

    /**
   * Sends a message wrapped in a SOAP Envelope and Body.
   * 
   * @param message
   * @param ser
   * @param context
   * @throws DPWSFault
   */
    public void writeMessage(OutMessage message, XMLSerializer ser, DPWSContextImpl context) throws DPWSFault {
        try {
            AttributesImpl atts = new AttributesImpl();
            QName env = SoapVersion.envelope;
            Boolean serializeObj = (Boolean) context.getProperty(SERIALIZE_PROLOG);
            boolean serializeProlog = (serializeObj != null) ? serializeObj.booleanValue() : true;
            if (serializeProlog) ser.startDocument();
            ser.startPrefixMapping(env.getPrefix(), env.getNamespaceURI());
            ser.startPrefixMapping(WSDConstants.WSD_PREFIX, WSDConstants.WSD_NAMESPACE);
            ser.startPrefixMapping(WSAConstants.WSA_PREFIX, WSAConstants.WSA_NAMESPACE_200408);
            ser.startElement(env.getNamespaceURI(), env.getLocalPart(), null, atts);
            EndpointReference epr = context.getEndpointReference();
            if (epr != null) {
                Map<QName, Object> refParams = epr.getReferenceParameters();
                if (refParams != null && !refParams.isEmpty()) {
                    if (message.getHeader() == null) message.setHeader(message.getOrCreateHeader());
                    Iterator<QName> it = refParams.keySet().iterator();
                    while (it.hasNext()) {
                        QName paramName = it.next();
                        Element tmpElt = new Element(paramName.getLocalPart(), paramName.getPrefix(), paramName.getNamespaceURI());
                        if (paramName.getNamespaceURI() != null) ser.startPrefixMapping(paramName.getPrefix(), paramName.getNamespaceURI());
                        Content content = (Content) refParams.get(paramName);
                        tmpElt.addContent((Content) content.clone());
                        message.getHeader().addContent(tmpElt);
                    }
                }
            }
            if (message.getHeader() != null && message.getHeader().getContentSize() > 0) {
                QName header = SoapVersion.header;
                ser.startElement(header.getNamespaceURI(), header.getLocalPart(), null, atts);
                writeHeaders(message, ser);
                ser.endElement(header.getNamespaceURI(), header.getLocalPart(), null);
            }
            QName body = SoapVersion.body;
            ser.startElement(body.getNamespaceURI(), body.getLocalPart(), null, atts);
            getSerializer().writeMessage(message, ser, context);
            ser.endElement(body.getNamespaceURI(), body.getLocalPart(), null);
            ser.endElement(env.getNamespaceURI(), env.getLocalPart(), null);
            if (serializeProlog) ser.endDocument();
        } catch (SAXException e) {
            throw new DPWSFault("Couldn't write message.", e, DPWSFault.RECEIVER);
        }
    }

    @SuppressWarnings("unchecked")
    private void writeHeaders(AbstractMessage msg, XMLSerializer ser) throws SAXException {
        Element header2 = msg.getHeader();
        Element header = header2;
        List<Namespace> namespaces = header.getAdditionalNamespaces();
        if (namespaces != null) {
            for (int i = 0; i < namespaces.size(); i++) {
                Namespace namespace = namespaces.get(i);
                ser.startPrefixMapping(namespace.getPrefix(), namespace.getURI());
            }
        }
        List<Element> elements = header2.getChildren();
        if (!elements.isEmpty()) {
            JDOMReader reader = new JDOMReader();
            for (int i = 0; i < elements.size(); i++) {
                Element e = elements.get(i);
                reader.setRootContent(e);
                reader.setContentHandler(ser);
                reader.parse("");
            }
        }
    }

    public void readMessage(InMessage message, DPWSContextImpl context) throws DPWSFault {
        throw new UnsupportedOperationException();
    }
}
