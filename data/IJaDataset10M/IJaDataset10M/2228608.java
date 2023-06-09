package org.frameworkset.util.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import com.frameworkset.util.StringUtil;

/**
 * SAX <code>XMLReader</code> that reads from a StAX <code>XMLStreamReader</code>.  Reads from an
 * <code>XMLStreamReader</code>, and calls the corresponding methods on the SAX callback interfaces.
 *
 * @author Arjen Poutsma
 * @see XMLStreamReader
 * @see #setContentHandler(org.xml.sax.ContentHandler)
 * @see #setDTDHandler(org.xml.sax.DTDHandler)
 * @see #setEntityResolver(org.xml.sax.EntityResolver)
 * @see #setErrorHandler(org.xml.sax.ErrorHandler)
 * @since 3.0
 */
class StaxStreamXMLReader extends AbstractStaxXMLReader {

    private final XMLStreamReader reader;

    /**
	 * Constructs a new instance of the <code>StaxStreamXmlReader</code> that reads from the given
	 * <code>XMLStreamReader</code>.  The supplied stream reader must be in <code>XMLStreamConstants.START_DOCUMENT</code>
	 * or <code>XMLStreamConstants.START_ELEMENT</code> state.
	 *
	 * @param reader the <code>XMLEventReader</code> to read from
	 * @throws IllegalStateException if the reader is not at the start of a document or element
	 */
    StaxStreamXMLReader(XMLStreamReader reader) {
        int event = reader.getEventType();
        if (!(event == XMLStreamConstants.START_DOCUMENT || event == XMLStreamConstants.START_ELEMENT)) {
            throw new IllegalStateException("XMLEventReader not at start of document or element");
        }
        this.reader = reader;
    }

    @Override
    protected void parseInternal() throws SAXException, XMLStreamException {
        boolean documentStarted = false;
        boolean documentEnded = false;
        int elementDepth = 0;
        int eventType = reader.getEventType();
        while (true) {
            if (eventType != XMLStreamConstants.START_DOCUMENT && eventType != XMLStreamConstants.END_DOCUMENT && !documentStarted) {
                handleStartDocument();
                documentStarted = true;
            }
            switch(eventType) {
                case XMLStreamConstants.START_ELEMENT:
                    elementDepth++;
                    handleStartElement();
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    elementDepth--;
                    if (elementDepth >= 0) {
                        handleEndElement();
                    }
                    break;
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    handleProcessingInstruction();
                    break;
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.SPACE:
                case XMLStreamConstants.CDATA:
                    handleCharacters();
                    break;
                case XMLStreamConstants.START_DOCUMENT:
                    setLocator(reader.getLocation());
                    handleStartDocument();
                    documentStarted = true;
                    break;
                case XMLStreamConstants.END_DOCUMENT:
                    handleEndDocument();
                    documentEnded = true;
                    break;
                case XMLStreamConstants.COMMENT:
                    handleComment();
                    break;
                case XMLStreamConstants.DTD:
                    handleDtd();
                    break;
                case XMLStreamConstants.ENTITY_REFERENCE:
                    handleEntityReference();
                    break;
            }
            if (reader.hasNext() && elementDepth >= 0) {
                eventType = reader.next();
            } else {
                break;
            }
        }
        if (!documentEnded) {
            handleEndDocument();
        }
    }

    private void handleStartDocument() throws SAXException {
        if (getContentHandler() != null) {
            getContentHandler().startDocument();
            if (reader.standaloneSet()) {
                setStandalone(reader.isStandalone());
            }
        }
    }

    private void handleStartElement() throws SAXException {
        if (getContentHandler() != null) {
            QName qName = reader.getName();
            if (hasNamespacesFeature()) {
                for (int i = 0; i < reader.getNamespaceCount(); i++) {
                    String prefix = reader.getNamespacePrefix(i);
                    if (prefix == null) {
                        prefix = "";
                    }
                    getContentHandler().startPrefixMapping(prefix, reader.getNamespaceURI(i));
                }
                getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), getAttributes());
            } else {
                getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
            }
        }
    }

    private void handleEndElement() throws SAXException {
        if (getContentHandler() != null) {
            QName qName = reader.getName();
            if (hasNamespacesFeature()) {
                getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
                for (int i = 0; i < reader.getNamespaceCount(); i++) {
                    String prefix = reader.getNamespacePrefix(i);
                    if (prefix == null) {
                        prefix = "";
                    }
                    getContentHandler().endPrefixMapping(prefix);
                }
            } else {
                getContentHandler().endElement("", "", toQualifiedName(qName));
            }
        }
    }

    private void handleCharacters() throws SAXException {
        if (getContentHandler() != null && reader.isWhiteSpace()) {
            getContentHandler().ignorableWhitespace(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
            return;
        }
        if (XMLStreamConstants.CDATA == reader.getEventType() && getLexicalHandler() != null) {
            getLexicalHandler().startCDATA();
        }
        if (getContentHandler() != null) {
            getContentHandler().characters(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
        }
        if (XMLStreamConstants.CDATA == reader.getEventType() && getLexicalHandler() != null) {
            getLexicalHandler().endCDATA();
        }
    }

    private void handleComment() throws SAXException {
        if (getLexicalHandler() != null) {
            getLexicalHandler().comment(reader.getTextCharacters(), reader.getTextStart(), reader.getTextLength());
        }
    }

    private void handleDtd() throws SAXException {
        if (getLexicalHandler() != null) {
            javax.xml.stream.Location location = reader.getLocation();
            getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
        }
        if (getLexicalHandler() != null) {
            getLexicalHandler().endDTD();
        }
    }

    private void handleEntityReference() throws SAXException {
        if (getLexicalHandler() != null) {
            getLexicalHandler().startEntity(reader.getLocalName());
        }
        if (getLexicalHandler() != null) {
            getLexicalHandler().endEntity(reader.getLocalName());
        }
    }

    private void handleEndDocument() throws SAXException {
        if (getContentHandler() != null) {
            getContentHandler().endDocument();
        }
    }

    private void handleProcessingInstruction() throws SAXException {
        if (getContentHandler() != null) {
            getContentHandler().processingInstruction(reader.getPITarget(), reader.getPIData());
        }
    }

    private Attributes getAttributes() {
        AttributesImpl attributes = new AttributesImpl();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String namespace = reader.getAttributeNamespace(i);
            if (namespace == null || !hasNamespacesFeature()) {
                namespace = "";
            }
            String type = reader.getAttributeType(i);
            if (type == null) {
                type = "CDATA";
            }
            attributes.addAttribute(namespace, reader.getAttributeLocalName(i), toQualifiedName(reader.getAttributeName(i)), type, reader.getAttributeValue(i));
        }
        if (hasNamespacePrefixesFeature()) {
            for (int i = 0; i < reader.getNamespaceCount(); i++) {
                String prefix = reader.getNamespacePrefix(i);
                String namespaceUri = reader.getNamespaceURI(i);
                String qName;
                if (StringUtil.hasLength(prefix)) {
                    qName = "xmlns:" + prefix;
                } else {
                    qName = "xmlns";
                }
                attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
            }
        }
        return attributes;
    }
}
