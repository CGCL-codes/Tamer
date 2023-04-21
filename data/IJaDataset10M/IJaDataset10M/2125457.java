package sourceforge.xmlschematograp;

import java.net.URL;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.xml.sax.*;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.*;

public class XsomUtil {

    private static final Logger LOG = Logger.getLogger(XsomUtil.class);

    public static XSSchemaSet read(Collection<URL> urls) throws Exception {
        LOG.debug("creating parser");
        XSOMParser parser = new XSOMParser();
        parser.setAnnotationParser(new AnnotationParserFactory() {

            public AnnotationParser create() {
                return new AnnotationParser() {

                    final StringBuffer content = new StringBuffer();

                    public ContentHandler getContentHandler(AnnotationContext context, String parentElementName, ErrorHandler errorHandler, EntityResolver entityResolver) {
                        return new ContentHandler() {

                            public void characters(char[] ch, int start, int length) throws SAXException {
                                content.append(ch, start, length);
                            }

                            public void endDocument() throws SAXException {
                            }

                            public void endElement(String uri, String localName, String name) throws SAXException {
                            }

                            public void endPrefixMapping(String prefix) throws SAXException {
                            }

                            public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
                            }

                            public void processingInstruction(String target, String data) throws SAXException {
                            }

                            public void setDocumentLocator(Locator locator) {
                            }

                            public void skippedEntity(String name) throws SAXException {
                            }

                            public void startDocument() throws SAXException {
                            }

                            public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
                            }

                            public void startPrefixMapping(String prefix, String uri) throws SAXException {
                            }
                        };
                    }

                    @Override
                    public Object getResult(Object existing) {
                        return content.toString();
                    }
                };
            }
        });
        for (URL url : urls) {
            LOG.debug("Parsing " + url);
            parser.parse(url);
        }
        LOG.debug("Parse done");
        return parser.getResult();
    }
}
