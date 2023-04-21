package gate.corpora;

import java.io.IOException;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import gate.Document;
import gate.Resource;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleResource;
import gate.event.StatusListener;
import gate.sgml.Sgml2Xml;
import gate.util.DocumentFormatException;
import gate.xml.XmlDocumentHandler;

/** The format of Documents. Subclasses of DocumentFormat know about
  * particular MIME types and how to unpack the information in any
  * markup or formatting they contain into GATE annotations. Each MIME
  * type has its own subclass of DocumentFormat, e.g. XmlDocumentFormat,
  * RtfDocumentFormat, MpegDocumentFormat. These classes register themselves
  * with a static index residing here when they are constructed. Static
  * getDocumentFormat methods can then be used to get the appropriate
  * format class for a particular document.
  */
@CreoleResource(name = "GATE SGML Document Format", isPrivate = true, autoinstances = { @AutoInstance(hidden = true) })
public class SgmlDocumentFormat extends TextualDocumentFormat {

    /** Debug flag */
    private static final boolean DEBUG = false;

    /** Default construction */
    public SgmlDocumentFormat() {
        super();
    }

    /** Unpack the markup in the document. This converts markup from the
    * native format (e.g. SGML) into annotations in GATE format.
    * Uses the markupElementsMap to determine which elements to convert, and
    * what annotation type names to use.
    * The doc's content is first converted to a wel formed XML.
    * If this succeddes then the document is saved into a temp file and parsed
    * as an XML document.
    *
    * @param doc The gate document you want to parse.
    *
    */
    public void unpackMarkup(Document doc) throws DocumentFormatException {
        if ((doc == null) || (doc.getSourceUrl() == null && doc.getContent() == null)) {
            throw new DocumentFormatException("GATE document is null or no content found. Nothing to parse!");
        }
        StatusListener statusListener = new StatusListener() {

            public void statusChanged(String text) {
                fireStatusChanged(text);
            }
        };
        XmlDocumentHandler xmlDocHandler = null;
        try {
            Sgml2Xml sgml2Xml = new Sgml2Xml(doc);
            fireStatusChanged("Performing SGML to XML...");
            String xmlUri = sgml2Xml.convert();
            fireStatusChanged("DONE !");
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setValidating(false);
            saxParserFactory.setNamespaceAware(true);
            SAXParser parser = saxParserFactory.newSAXParser();
            if (null != doc) {
                xmlDocHandler = new XmlDocumentHandler(doc, this.markupElementsMap, this.element2StringMap);
                xmlDocHandler.addStatusListener(statusListener);
                parser.parse(xmlUri, xmlDocHandler);
                ((DocumentImpl) doc).setNextAnnotationId(xmlDocHandler.getCustomObjectsId());
            }
        } catch (ParserConfigurationException e) {
            throw new DocumentFormatException("XML parser configuration exception ", e);
        } catch (SAXException e) {
            throw new DocumentFormatException(e);
        } catch (IOException e) {
            throw new DocumentFormatException("I/O exception for " + doc.getSourceUrl().toString());
        } finally {
            if (xmlDocHandler != null) xmlDocHandler.removeStatusListener(statusListener);
        }
    }

    /** This method converts the document's content from SGML 2 XML.*/
    private String sgml2Xml(Document doc) {
        String xmlUri = doc.getSourceUrl().toString();
        return xmlUri;
    }

    /** Initialise this resource, and return it. */
    public Resource init() throws ResourceInstantiationException {
        MimeType mime = new MimeType("text", "sgml");
        mimeString2ClassHandlerMap.put(mime.getType() + "/" + mime.getSubtype(), this);
        mimeString2mimeTypeMap.put(mime.getType() + "/" + mime.getSubtype(), mime);
        suffixes2mimeTypeMap.put("sgm", mime);
        suffixes2mimeTypeMap.put("sgml", mime);
        setMimeType(mime);
        return this;
    }
}
