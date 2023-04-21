package com.simonstl.moe.adapter;

import com.simonstl.moe.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.HashSet;
import java.util.Iterator;

/**
* <p>The MOESAXA adapter class presents a MOE object as SAX events.</p>
*
*<p><strong>Note</strong>: MOESAX currently expects all objects to be complete.</p>
*
*<p>Dang.  This appears to duplicate functionality in {@link com.simonstl.moe.visitor.MOEtoSAX MOEtoSAX}.  Ouch.</p>
*
* <p>version 0.01 is the initial release.</p>
*
* @version 0.01 20 September 2001
* @author Simon St.Laurent
**/
public class MOESAXA implements XMLReader {

    protected ContentHandler contentHandler = null;

    protected DTDHandler dTDHandler = null;

    protected ErrorHandler errorHandler = null;

    protected EntityResolver entityResolver = null;

    protected CoreComponentI MOEtalker;

    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    public DTDHandler getDTDHandler() {
        return dTDHandler;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public boolean getFeature(String name) {
        return false;
    }

    public Object getProperty(String name) {
        return null;
    }

    public void parse(InputSource input) throws SAXException {
        parse();
    }

    public void parse(String systemId) throws SAXException {
        parse();
    }

    public void setContentHandler(ContentHandler handler) {
        contentHandler = handler;
    }

    public void setDTDHandler(DTDHandler handler) {
        dTDHandler = handler;
    }

    public void setEntityResolver(EntityResolver resolver) {
        entityResolver = resolver;
    }

    public void setErrorHandler(ErrorHandler handler) {
        errorHandler = handler;
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("MOESAXA does not currently support SAX features.");
    }

    public void setProperty(String name, java.lang.Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        throw new SAXNotRecognizedException("MOESAXA does not currently support SAX properties.");
    }

    public void setMOEComponent(CoreComponentI talker) {
        MOEtalker = talker;
    }

    public CoreComponentI setMOEComponent() {
        return MOEtalker;
    }

    /**
<p>The parse method walks the MOE tree and reports it as a series of SAX events.</p>
<p>Note that this method currently expects a document or element object as its foundation and won't be especially happy if you feed it something else.</p>
*/
    public void parse() throws SAXException {
        parse(MOEtalker);
    }

    public void parse(CoreComponentI component) throws SAXException {
        NodeType type = component.getBasicType();
        if (type == NodeType.ELEMENT) {
            parseAsElement(component);
        } else if (type == NodeType.DOCUMENT) {
            parseAsDocument(component);
        } else if (type == NodeType.CHARS) {
            parseAsCharacters(component);
        } else if (type == NodeType.IG_WHITESPACE) {
            parseAsWhitespace(component);
        } else if (type == NodeType.PI) {
            parseAsPI(component);
        } else if (type == NodeType.COMMENT) {
            parseAsComment(component);
        } else if (type == NodeType.CDATA) {
            parseAsCDATA(component);
        }
    }

    protected void parseAsElement(CoreComponentI talker) throws SAXException {
        String nsURI;
        String localName;
        String QName;
        AttributesImpl atts = new AttributesImpl();
        nsURI = talker.getNsURI();
        localName = talker.getLocalName();
        QName = talker.getQName();
        if (talker.getUnorderedContent() != null) {
            Iterator it = talker.getUnorderedContent().iterator();
            while (it.hasNext()) {
                CoreComponentI next = (CoreComponentI) it.next();
                atts.addAttribute(next.getNsURI(), next.getLocalName(), next.getQName(), "CDATA", next.getTextContent());
            }
        }
        if (contentHandler != null) {
            contentHandler.startElement(nsURI, localName, QName, atts);
        }
        ComponentListI children = talker.getContent();
        if (children != null) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                CoreComponentI nextComponent = (CoreComponentI) it.next();
                parse(nextComponent);
            }
        }
        if (contentHandler != null) {
            contentHandler.endElement(nsURI, localName, QName);
        }
    }

    protected void parseAsCharacters(CoreComponentI talker) throws SAXException {
        String textContent = talker.getTextContent();
        contentHandler.characters(textContent.toCharArray(), 0, textContent.length());
    }

    protected void parseAsWhitespace(CoreComponentI talker) throws SAXException {
        String textContent = talker.getTextContent();
        contentHandler.ignorableWhitespace(textContent.toCharArray(), 0, textContent.length());
    }

    protected void parseAsDocument(CoreComponentI talker) throws SAXException {
        contentHandler.startDocument();
        ComponentListI children = talker.getContent();
        if (children != null) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                CoreComponentI nextComponent = (CoreComponentI) it.next();
                parse(nextComponent);
            }
        }
        contentHandler.endDocument();
    }

    protected void parseAsPI(CoreComponentI talker) throws SAXException {
        String target = talker.getLocalName();
        String data = talker.getTextContent();
        contentHandler.processingInstruction(target, data);
    }

    protected void parseAsCDATA(CoreComponentI talker) throws SAXException {
        parseAsCharacters(talker);
    }

    protected void parseAsComment(CoreComponentI talker) throws SAXException {
    }
}
