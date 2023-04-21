package cartagows.wsframework.wssecurity.policy.model;

import org.apache.neethi.PolicyComponent;
import cartagows.wsframework.wssecurity.policy.SP11Constants;
import cartagows.wsframework.wssecurity.policy.SP12Constants;
import cartagows.wsframework.wssecurity.policy.SPConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class TransportToken extends AbstractSecurityAssertion implements TokenWrapper {

    private Token transportToken;

    public TransportToken(int version) {
        setVersion(version);
    }

    public Token getTransportToken() {
        return transportToken;
    }

    public QName getName() {
        if (version == SPConstants.SP_V12) {
            return SP12Constants.TRANSPORT_TOKEN;
        } else {
            return SP11Constants.TRANSPORT_TOKEN;
        }
    }

    public boolean isOptional() {
        throw new UnsupportedOperationException();
    }

    public PolicyComponent normalize() {
        throw new UnsupportedOperationException();
    }

    public short getType() {
        return org.apache.neethi.Constants.TYPE_ASSERTION;
    }

    public void serialize(XMLStreamWriter writer) throws XMLStreamException {
        String localName = getName().getLocalPart();
        String namespaceURI = getName().getNamespaceURI();
        String prefix = writer.getPrefix(namespaceURI);
        if (prefix == null) {
            prefix = getName().getPrefix();
            writer.setPrefix(prefix, namespaceURI);
        }
        writer.writeStartElement(prefix, localName, namespaceURI);
        String wspPrefix = writer.getPrefix(SPConstants.POLICY.getNamespaceURI());
        if (wspPrefix == null) {
            wspPrefix = SPConstants.POLICY.getPrefix();
            writer.setPrefix(wspPrefix, SPConstants.POLICY.getNamespaceURI());
        }
        writer.writeStartElement(SPConstants.POLICY.getPrefix(), SPConstants.POLICY.getLocalPart(), SPConstants.POLICY.getNamespaceURI());
        transportToken.serialize(writer);
        writer.writeEndElement();
        writer.writeEndElement();
    }

    public void setToken(Token tok) {
        this.transportToken = tok;
    }
}
