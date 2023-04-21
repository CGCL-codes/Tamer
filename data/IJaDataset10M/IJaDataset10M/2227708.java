package au.gov.naa.digipres.xena.kernel.normalise;

import javax.xml.transform.Result;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Class to indicate a normaliser is actually a text normaliser, and provide a method to set the output file extension
 * @author Justin Waddell
 *
 */
public abstract class AbstractTextNormaliser extends AbstractNormaliser {

    public static final String DEFAULT_TEXT_OUTPUT_FILE_EXTENSION = ".txt";

    public abstract String getOutputFileExtension();

    @Override
    public ContentHandler getContentHandler() {
        return new TextContentHandler();
    }

    /**
	 * This class ensures that whenever the characters method is called, output escaping is disabled.
	 * Output escaping is re-enabled after the call to the superclass content handler has been made.
	 * 
	 * All other methods simply pass the call through to the superclass content handler.
	 * @author Justin Waddell
	 *
	 */
    public class TextContentHandler implements ContentHandler {

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            contentHandler.processingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, "");
            contentHandler.characters(ch, start, length);
            contentHandler.processingInstruction(Result.PI_ENABLE_OUTPUT_ESCAPING, "");
        }

        @Override
        public void endDocument() throws SAXException {
            contentHandler.endDocument();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            contentHandler.endElement(uri, localName, qName);
        }

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {
            contentHandler.endPrefixMapping(prefix);
        }

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
            contentHandler.ignorableWhitespace(ch, start, length);
        }

        @Override
        public void processingInstruction(String target, String data) throws SAXException {
            contentHandler.processingInstruction(target, data);
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            contentHandler.setDocumentLocator(locator);
        }

        @Override
        public void skippedEntity(String name) throws SAXException {
            contentHandler.skippedEntity(name);
        }

        @Override
        public void startDocument() throws SAXException {
            contentHandler.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
            contentHandler.startElement(uri, localName, qName, atts);
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {
            contentHandler.startPrefixMapping(prefix, uri);
        }
    }
}
