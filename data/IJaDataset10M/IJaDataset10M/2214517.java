package org.probatron.officeotron;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class OOXMLContentTypeHandler implements ContentHandler {

    static Logger logger = Logger.getLogger(OOXMLContentTypeHandler.class);

    private OOXMLTargetCollection col;

    private OOXMLDefaultTypeMap dtm;

    public OOXMLContentTypeHandler(OOXMLTargetCollection col, OOXMLDefaultTypeMap dtm) {
        this.col = col;
        this.dtm = dtm;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (uri.equals("http://schemas.openxmlformats.org/package/2006/content-types")) {
            String name = atts.getValue("PartName");
            if (localName.equals("Override")) {
                OOXMLTarget t = this.col.getTargetByName(name);
                if (t != null) {
                    String ct = atts.getValue("ContentType");
                    logger.debug("Setting MIME type for entry " + name + " as: " + ct);
                    t.setMimeType(ct);
                }
            } else if (localName.equals("Default")) {
                String extension = atts.getValue("Extension");
                String mt = atts.getValue("ContentType");
                this.dtm.put(extension, mt);
                logger.debug("associating extension \"" + extension + "\" with MIME type " + mt);
            }
        }
    }

    public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
    }

    public void endPrefixMapping(String arg0) throws SAXException {
    }

    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
    }

    public void processingInstruction(String arg0, String arg1) throws SAXException {
    }

    public void setDocumentLocator(Locator arg0) {
    }

    public void skippedEntity(String arg0) throws SAXException {
    }

    public void startDocument() throws SAXException {
    }

    public void startPrefixMapping(String arg0, String arg1) throws SAXException {
    }
}
