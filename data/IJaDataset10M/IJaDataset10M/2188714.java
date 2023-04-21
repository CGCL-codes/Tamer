package org.xactor.ws.coordination.element;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xactor.ws.Constants;
import org.xactor.ws.ElementWriter;
import org.xactor.ws.StringBufferFactory;
import org.xactor.ws.XMLFragmentMetadata;

/**
 * Java mapping of the WS-Coordination <code>CreateCoordinationContext</code> XML element.
 * 
 * This request is used to create a coordination context that supports a coordination type (i.e., a
 * service that provides a set of coordination protocols). This command is required when using a
 * network-accessible <code>Activation</code> service in heterogeneous environments that span
 * vendor implementations. To fully understand the semantics of this operation it is necessary to
 * read the specification where the coordination type is defined (e.g. WS-AtomicTransaction).
 * 
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class CreateCoordinationContext implements Constants {

    /** The timeout of the coordination context. */
    private Expires expires;

    /**
    * When an application propagates an activity using a coordination service, applications MUST
    * include a Coordination context in the outgoing message.
    */
    private CoordinationContext currentContext;

    /**
    * The unique identifier for the desired coordination type for the activity (e.g., a URI to the
    * Atomic Transaction coordination type).
    */
    private URI coordinationType;

    public CreateCoordinationContext() {
    }

    public CreateCoordinationContext(URI coordinationType) {
        this.coordinationType = coordinationType;
    }

    public CreateCoordinationContext(Expires expires, CoordinationContext currentContext, URI coordinationType) {
        this.currentContext = currentContext;
        this.coordinationType = coordinationType;
        setExpires(expires);
    }

    public CreateCoordinationContext(XMLStreamReader parser) throws XMLStreamException {
        for (int ev = parser.next(); ev != XMLStreamConstants.END_DOCUMENT; ev = parser.next()) {
            if (ev == XMLStreamConstants.START_ELEMENT) {
                QName qname = parser.getName();
                if (WSCoor.Elements.EXPIRES.equals(qname)) {
                    expires = new Expires(parser);
                } else if (WSCoor.Elements.CURRENT_CONTEXT.equals(qname)) {
                    currentContext = new CoordinationContext(parser, WSCoor.Elements.CURRENT_CONTEXT);
                } else if (WSCoor.Elements.COORDINATION_TYPE.equals(qname)) {
                    coordinationType = URI.create(parser.getElementText());
                }
            } else if (ev == XMLStreamConstants.END_ELEMENT) {
                QName qname = parser.getName();
                if (WSCoor.Elements.CREATE_COORDINATION_CONTEXT.equals(qname)) break;
            }
        }
        parser.close();
    }

    public XMLFragmentMetadata getMetadata() {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put(WSCoor.Namespaces.PF_WSCOOR, WSCoor.Namespaces.NS_WSCOOR_10);
        StringBuffer buff = StringBufferFactory.newStringBuffer();
        ElementWriter.writeStartElement(buff, WSCoor.Elements.CREATE_COORDINATION_CONTEXT);
        if (expires != null && expires.getValue() > 0) expires.writeMetadataTo(buff, namespaces);
        if (currentContext != null) currentContext.writeMetadataTo(buff, namespaces, WSCoor.Elements.CURRENT_CONTEXT);
        if (coordinationType != null) {
            ElementWriter.writeStartElement(buff, WSCoor.Elements.COORDINATION_TYPE);
            buff.append(coordinationType.toString());
            ElementWriter.writeEndElement(buff, WSCoor.Elements.COORDINATION_TYPE);
        }
        ElementWriter.writeEndElement(buff, WSCoor.Elements.CREATE_COORDINATION_CONTEXT);
        return new XMLFragmentMetadata(buff.toString(), namespaces);
    }

    public Expires getExpires() {
        return expires;
    }

    public void setExpires(Expires expires) {
        if (expires != null && expires.getValue() > 0) this.expires = expires;
    }

    public CoordinationContext getCurrentContext() {
        return currentContext;
    }

    public void setCurrentContext(CoordinationContext context) {
        currentContext = context;
    }

    public URI getCoordinationType() {
        return coordinationType;
    }

    public void setCoordinationType(URI coordinationType) {
        this.coordinationType = coordinationType;
    }
}
