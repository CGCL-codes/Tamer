package org.apache.ws.secpolicy.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.neethi.PolicyComponent;
import org.apache.ws.secpolicy.SP11Constants;
import org.apache.ws.secpolicy.SP12Constants;
import org.apache.ws.secpolicy.SPConstants;

public class RequiredElements extends AbstractSecurityAssertion {

    private ArrayList xPathExpressions = new ArrayList();

    private HashMap declaredNamespaces = new HashMap();

    private String xPathVersion;

    public RequiredElements(int version) {
        setVersion(version);
    }

    /**
     * @return Returns the xPathExpressions.
     */
    public ArrayList getXPathExpressions() {
        return xPathExpressions;
    }

    public void addXPathExpression(String expr) {
        this.xPathExpressions.add(expr);
    }

    /**
     * @return Returns the xPathVersion.
     */
    public String getXPathVersion() {
        return xPathVersion;
    }

    /**
     * @param pathVersion
     *            The xPathVersion to set.
     */
    public void setXPathVersion(String pathVersion) {
        xPathVersion = pathVersion;
    }

    public HashMap getDeclaredNamespaces() {
        return declaredNamespaces;
    }

    public void addDeclaredNamespaces(String uri, String prefix) {
        declaredNamespaces.put(prefix, uri);
    }

    public void serialize(XMLStreamWriter writer) throws XMLStreamException {
        String localName = getName().getLocalPart();
        String namespaceURI = getName().getNamespaceURI();
        String prefix;
        String writerPrefix = writer.getPrefix(namespaceURI);
        if (writerPrefix == null) {
            prefix = getName().getPrefix();
            writer.setPrefix(prefix, namespaceURI);
        } else {
            prefix = writerPrefix;
        }
        writer.writeStartElement(prefix, localName, namespaceURI);
        writer.writeNamespace(prefix, namespaceURI);
        if (writerPrefix == null) {
            writer.writeNamespace(prefix, namespaceURI);
        }
        if (xPathVersion != null) {
            writer.writeAttribute(prefix, namespaceURI, SPConstants.XPATH_VERSION, xPathVersion);
        }
        String xpathExpression;
        for (Iterator iterator = xPathExpressions.iterator(); iterator.hasNext(); ) {
            xpathExpression = (String) iterator.next();
            writer.writeStartElement(prefix, SPConstants.XPATH_EXPR, namespaceURI);
            writer.writeCharacters(xpathExpression);
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    public QName getName() {
        if (version == SPConstants.SP_V12) {
            return SP12Constants.REQUIRED_ELEMENTS;
        } else {
            return SP11Constants.REQUIRED_ELEMENTS;
        }
    }

    public PolicyComponent normalize() {
        return this;
    }
}
