package ch.mye.nmapi.lib.handler;

import ch.mye.nmapi.lib.xml.XMLNetconfReader;
import ch.mye.nmapi.lib.xml.XMLAttribute;
import ch.mye.nmapi.lib.element.RpcElement;
import ch.mye.nmapi.lib.exception.MissingAttributeException;
import ch.mye.nmapi.lib.exception.NetconfException;
import ch.mye.nmapi.lib.xml.XMLNetconfStartElement;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

/**
 *
 * @author Lukas Zaugg
 */
class RpcElementHandler implements NetconfHandler<RpcElement> {

    private static final String NAME = "rpc";

    public static final String ELEMENT_NAMESPACE = "urn:ietf:params:xml:ns:netconf:base:1.0";

    private static final String MESSAGEID = "message-id";

    /**
     * Default constructor.
     */
    public RpcElementHandler() {
        super();
    }

    public String getXml(RpcElement rpcElement) {
        return "<" + NAME + "  xmlns=\"" + ELEMENT_NAMESPACE + "\" message-id=\"" + rpcElement.getMessageId() + "\"/>";
    }

    public RpcElement getElement(String xml) throws NetconfException {
        QName okQName = new QName(ELEMENT_NAMESPACE, NAME);
        XMLNetconfReader reader = new XMLNetconfReader(xml);
        XMLNetconfStartElement selement = reader.getNextAsStartElement(okQName);
        String messageId = null;
        List<XMLAttribute> attributes = new ArrayList<XMLAttribute>();
        for (XMLAttribute attribute : selement.getAttributes()) {
            if (MESSAGEID.equals(attribute.getName()) && "".equals(attribute.getNamespace())) {
                messageId = attribute.getValue();
                continue;
            }
            attributes.add(attribute);
        }
        if (messageId == null) {
            throw new MissingAttributeException(MESSAGEID, NAME, "message-id missing");
        }
        reader.getNextAsEndElement(okQName);
        RpcElement rpcElement = new RpcElement(messageId);
        rpcElement.setAttributes(attributes);
        return rpcElement;
    }
}
