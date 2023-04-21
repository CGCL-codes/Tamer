package ch.ethz.mxquery.xdmio.xmlAdapters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import ch.ethz.mxquery.contextConfig.XQStaticContext;
import ch.ethz.mxquery.datamodel.Namespace;
import ch.ethz.mxquery.datamodel.types.Type;
import ch.ethz.mxquery.datamodel.xdm.NamedToken;
import ch.ethz.mxquery.datamodel.xdm.ProcessingInstrToken;
import ch.ethz.mxquery.datamodel.xdm.TextToken;
import ch.ethz.mxquery.datamodel.xdm.TokenInterface;
import ch.ethz.mxquery.exceptions.MXQueryException;
import ch.ethz.mxquery.model.XDMIterator;

public class Token2StaxAdapter implements XMLStreamReader {

    private XDMIterator it;

    private XQStaticContext ctx;

    private boolean endSequence;

    private boolean startSequence = true;

    private boolean inAttributeCount = false;

    private ArrayList attributesList;

    private LinkedList tokensList;

    private TokenInterface currentToken;

    private TokenInterface textToken;

    private boolean elemStarted = false;

    private int prevAttrCount;

    public Token2StaxAdapter(XQStaticContext context, XDMIterator iter, boolean docWrapping) {
        this.it = iter;
        this.ctx = context;
        this.attributesList = new ArrayList();
        this.tokensList = new LinkedList();
    }

    public void close() throws XMLStreamException {
    }

    public int getAttributeCount() {
        int count = 0;
        prevAttrCount = attributesList.size();
        int event = getEventType();
        if (event == XMLStreamConstants.START_ELEMENT) try {
            inAttributeCount = true;
            TokenInterface tok = it.next();
            if (tok.isAttribute()) attributesList.add(tok); else tokensList.addLast(tok);
            while (tok.isAttribute() && hasNext()) {
                count++;
                tok = it.next();
                if (tok.isAttribute()) attributesList.add(tok); else tokensList.addLast(tok);
            }
            inAttributeCount = false;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (MXQueryException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getAttributeLocalName(int arg0) {
        NamedToken tok = (NamedToken) attributesList.get(arg0 + prevAttrCount);
        if (tok == null) return null; else return tok.getLocal();
    }

    public QName getAttributeName(int arg0) {
        NamedToken tok = (NamedToken) attributesList.get(arg0 + prevAttrCount);
        if (tok == null) return null; else if (tok.getPrefix() != null) return new QName(tok.getNS(), tok.getPrefix(), tok.getLocal()); else return new QName(tok.getNS(), tok.getLocal());
    }

    public String getAttributeNamespace(int arg0) {
        NamedToken tok = (NamedToken) attributesList.get(arg0 + prevAttrCount);
        if (tok == null) return null; else return tok.getNS();
    }

    public String getAttributePrefix(int arg0) {
        NamedToken tok = (NamedToken) attributesList.get(arg0 + prevAttrCount);
        if (tok == null) return null; else return tok.getPrefix();
    }

    public String getAttributeType(int arg0) {
        throw new RuntimeException("getAttributeType not supported");
    }

    public String getAttributeValue(int arg0) {
        NamedToken tok = (NamedToken) attributesList.get(arg0 + prevAttrCount);
        if (tok == null) return null; else return tok.getValueAsString();
    }

    public String getAttributeValue(String arg0, String arg1) {
        if (getEventType() == XMLStreamConstants.ATTRIBUTE) return currentToken.getValueAsString();
        return null;
    }

    public String getCharacterEncodingScheme() {
        return "";
    }

    public String getElementText() throws XMLStreamException {
        StringBuffer content = new StringBuffer();
        try {
            if (getEventType() != XMLStreamConstants.START_ELEMENT) {
                throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
            }
            int eventType = next();
            while (eventType != XMLStreamConstants.END_ELEMENT) {
                if (eventType == XMLStreamConstants.CHARACTERS || eventType == XMLStreamConstants.CDATA || eventType == XMLStreamConstants.SPACE || eventType == XMLStreamConstants.ENTITY_REFERENCE) {
                    content.append(getText());
                } else if (eventType == XMLStreamConstants.PROCESSING_INSTRUCTION || eventType == XMLStreamConstants.COMMENT) {
                } else if (eventType == XMLStreamConstants.END_DOCUMENT) {
                    throw new XMLStreamException("unexpected end of document when reading element text content", getLocation());
                } else if (eventType == XMLStreamConstants.START_ELEMENT) {
                    throw new XMLStreamException("element text content may not contain START_ELEMENT", getLocation());
                } else {
                    throw new XMLStreamException("Unexpected event type " + eventType);
                }
                eventType = next();
            }
        } catch (XMLStreamException xse) {
        }
        return content.toString();
    }

    public String getEncoding() {
        throw new RuntimeException("getEncoding not supported");
    }

    public int getEventType() {
        if (startSequence) {
            startSequence = false;
            return XMLStreamConstants.START_DOCUMENT;
        }
        if (currentToken instanceof TextToken) {
            textToken = currentToken;
            return XMLStreamConstants.CHARACTERS;
        }
        if (currentToken.isAttribute()) {
            return XMLStreamConstants.ATTRIBUTE;
        }
        int event = currentToken.getEventType();
        switch(event) {
            case Type.COMMENT:
                event = XMLStreamConstants.COMMENT;
                break;
            case Type.END_DOCUMENT:
                event = XMLStreamConstants.END_DOCUMENT;
                endSequence = true;
                break;
            case Type.END_SEQUENCE:
                event = XMLStreamConstants.END_DOCUMENT;
                endSequence = true;
                break;
            case Type.END_TAG:
                event = XMLStreamConstants.END_ELEMENT;
                break;
            case Type.ENTITY:
                event = XMLStreamConstants.ENTITY_DECLARATION;
                break;
            case Type.ENTITY_REF:
                event = XMLStreamConstants.ENTITY_REFERENCE;
                break;
            case Type.NAMESPACE:
                event = XMLStreamConstants.NAMESPACE;
                break;
            case Type.PROCESSING_INSTRUCTION:
                event = XMLStreamConstants.PROCESSING_INSTRUCTION;
                break;
            case Type.NOTATION:
                event = XMLStreamConstants.NOTATION_DECLARATION;
                break;
            case Type.START_DOCUMENT:
                event = XMLStreamConstants.START_DOCUMENT;
                break;
            case Type.START_SEQUENCE:
                event = XMLStreamConstants.START_DOCUMENT;
                break;
            case Type.START_TAG:
                event = XMLStreamConstants.START_ELEMENT;
                break;
            default:
        }
        return event;
    }

    public String getLocalName() {
        int event = getEventType();
        if (event == XMLStreamConstants.START_ELEMENT || event == XMLStreamConstants.END_ELEMENT) return ((NamedToken) currentToken).getLocal(); else if (event == XMLStreamConstants.ENTITY_REFERENCE) return null; else throw new IllegalStateException();
    }

    public Location getLocation() {
        return null;
    }

    public QName getName() {
        int event = getEventType();
        if (event == XMLStreamConstants.START_ELEMENT || event == XMLStreamConstants.END_ELEMENT) {
            NamedToken nmToken = (NamedToken) currentToken;
            if (nmToken.getPrefix() != null) return new QName(nmToken.getNS(), nmToken.getLocal(), nmToken.getPrefix()); else return new QName(nmToken.getNS(), nmToken.getLocal(), "");
        } else throw new IllegalStateException();
    }

    public NamespaceContext getNamespaceContext() {
        throw new RuntimeException("getNamespacecontext not supported");
    }

    public int getNamespaceCount() {
        return 0;
    }

    public String getNamespacePrefix(int arg0) {
        throw new RuntimeException("getNamespacePrefix not supported");
    }

    public String getNamespaceURI() {
        if (getEventType() == XMLStreamConstants.END_ELEMENT || getEventType() == XMLStreamConstants.START_ELEMENT) return ((NamedToken) currentToken).getNS();
        return null;
    }

    public String getNamespaceURI(String prefix) {
        Namespace ns = ctx.getNamespace(prefix);
        if (ns != null) return ns.getURI(); else return null;
    }

    public String getNamespaceURI(int arg0) {
        throw new RuntimeException("getNamespacePrefix   not supported");
    }

    public String getPIData() {
        if (currentToken instanceof ProcessingInstrToken) return ((ProcessingInstrToken) currentToken).getText(); else throw new IllegalStateException();
    }

    public String getPITarget() {
        if (currentToken instanceof ProcessingInstrToken) return ((ProcessingInstrToken) currentToken).getName(); else throw new IllegalStateException();
    }

    public String getPrefix() {
        int eventType = getEventType();
        if (eventType == XMLStreamConstants.START_ELEMENT || eventType == XMLStreamConstants.END_ELEMENT) return ((NamedToken) currentToken).getPrefix(); else throw new IllegalStateException();
    }

    public Object getProperty(String arg0) throws IllegalArgumentException {
        throw new RuntimeException("getProperty not supported");
    }

    public String getText() {
        if (hasText()) return currentToken.getValueAsString(); else throw new IllegalStateException();
    }

    public char[] getTextCharacters() {
        if (hasText()) return getText().toCharArray(); else throw new IllegalStateException();
    }

    public int getTextCharacters(int arg0, char[] arg1, int arg2, int arg3) throws XMLStreamException {
        throw new RuntimeException("getTextCharacters not supported");
    }

    public int getTextLength() {
        if (hasText()) return getTextCharacters().length; else throw new IllegalStateException();
    }

    public int getTextStart() {
        return 0;
    }

    public String getVersion() {
        return "1.0";
    }

    public boolean hasName() {
        int event = getEventType();
        if ((event == XMLStreamConstants.START_ELEMENT) || (event == XMLStreamConstants.END_ELEMENT)) return true;
        return false;
    }

    public boolean hasNext() throws XMLStreamException {
        if (endSequence) return false; else return true;
    }

    public boolean hasText() {
        int eventType = getEventType();
        if (eventType == XMLStreamConstants.SPACE || eventType == XMLStreamConstants.CHARACTERS || eventType == XMLStreamConstants.COMMENT || eventType == XMLStreamConstants.DTD || eventType == XMLStreamConstants.ENTITY_REFERENCE) return true; else return false;
    }

    public boolean isAttributeSpecified(int arg0) {
        throw new RuntimeException("isAttributeSpecified not supported");
    }

    public boolean isCharacters() {
        if (getEventType() == XMLStreamConstants.CHARACTERS) return true; else return false;
    }

    public boolean isEndElement() {
        if (getEventType() == XMLStreamConstants.END_ELEMENT) return true;
        return false;
    }

    public boolean isStandalone() {
        return false;
    }

    public boolean isStartElement() {
        if (getEventType() == XMLStreamConstants.START_ELEMENT) return true;
        return false;
    }

    public boolean isWhiteSpace() {
        return false;
    }

    public int next() throws XMLStreamException {
        if (!hasNext()) throw new NoSuchElementException(); else try {
            if ((!inAttributeCount && !tokensList.isEmpty())) {
                currentToken = (TokenInterface) tokensList.removeFirst();
            } else currentToken = it.next();
        } catch (MXQueryException e) {
            e.printStackTrace();
        }
        if (!elemStarted) {
            if (Type.isAttribute(currentToken.getEventType())) throw new XMLStreamException("Standalone attributes cannot be serialized into an Java XML Stream/StAX");
            if (currentToken.getEventType() == Type.START_TAG) elemStarted = true;
        }
        return getEventType();
    }

    public int nextTag() throws XMLStreamException {
        int eventType = next();
        while ((eventType == XMLStreamConstants.CHARACTERS && isWhiteSpace()) || (eventType == XMLStreamConstants.CDATA && isWhiteSpace()) || eventType == XMLStreamConstants.SPACE || eventType == XMLStreamConstants.PROCESSING_INSTRUCTION || eventType == XMLStreamConstants.COMMENT) {
            eventType = next();
        }
        if (eventType != XMLStreamConstants.START_ELEMENT && eventType != XMLStreamConstants.END_ELEMENT) {
            throw new XMLStreamException("expected start or end tag", getLocation());
        }
        return eventType;
    }

    public void require(int arg0, String arg1, String arg2) throws XMLStreamException {
        throw new RuntimeException("require not supported");
    }

    public boolean standaloneSet() {
        throw new RuntimeException("standalone set  not supported");
    }

    public TokenInterface getTextToken() {
        return textToken;
    }

    public TokenInterface getCurrentToken() {
        return currentToken;
    }
}
