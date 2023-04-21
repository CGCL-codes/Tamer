package org.kabeja.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Build a Parser from XML stream with the following format:
 *
 * <pre>
 *
 *  &lt;?xml version=&quot;1.0&quot;encoding=&quot;utf-8&quot; ?&gt;
 *
 *  &lt;parser class=&quot;org.kabeja.parser.DXFParser&quot; xmlns=&quot;http://kabeja.org/parser/1.0&quot;&gt;
 *    &lt;handler class=&quot;org.kabeja.parser.DXFHeaderSectionHandler&quot;/&gt;
 *    &lt;handler class=&quot;org.kabeja.parser.DXFTableSectionHandler&quot;&gt;
 *      &lt;handlers&gt;
 *                    &lt;handler class=&quot;org.kabeja.parser.table.DXFLayerTableHandler&quot;/&gt;
 *      &lt;/handlers&gt;
 *    &lt;/handler&gt;
 *
 *    &lt;!--+
 *        | The block and the entities handler use the same sub handlers.
 *        | If you have create a parser for an entity add the parser in
 *        | both sections.
 *        +--&gt;
 *
 *    &lt;handler class=&quot;org.kabeja.parser.DXFBlocksSectionHandler&quot;&gt;
 *            &lt;handlers&gt;
 *              &lt;handler class=&quot;org.kabeja.parser.entities.DXFArcHandler&quot;/&gt;
 *              &lt;handler class=&quot;org.kabeja.parser.entities.DXFCircleHandler&quot;/&gt;
 *            &lt;/handlers&gt;
 *    &lt;/handler&gt;
 *
 *
 *    &lt;handler class=&quot;org.kabeja.parser.DXFEntitiesSectionHandler&quot;&gt;
 *            &lt;handlers&gt;
 *                    &lt;handler class=&quot;org.kabeja.parser.entities.DXFArcHandler&quot;/&gt;
 *                    &lt;handler class=&quot;org.kabeja.parser.entities.DXFCircleHandler&quot;/&gt;
 *            &lt;handler class=&quot;org.kabeja.parser.entities.DXFEllipseHandler&quot;/&gt;
 *               &lt;/handlers&gt;
 *    &lt;/handler&gt;
 *   &lt;/parser&gt;
 *
 * </pre>
 *
 *
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth</a>
 *
 */
public class SAXParserBuilder implements ContentHandler {

    public static String ELEMENT_PARSER = "parser";

    public static String ELEMENT_HANDLER = "handler";

    public static String ELEMENT_HANDLERS = "handlers";

    public static String ATTRIBUTE_CLASS = "class";

    public static String ATTRIBUTE_EXTENSIONS = "extensions";

    public static String XMLNS_KABEJA_PARSER = "http://kabeja.org/parser/1.0";

    private Parser parser;

    private Stack stack = new Stack();

    private HandlerManager currentHandlerManager;

    private Handler handler;

    public SAXParserBuilder() {
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (localName.equals(ELEMENT_HANDLERS) && namespaceURI.equals(XMLNS_KABEJA_PARSER)) {
            currentHandlerManager = (HandlerManager) stack.pop();
        }
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

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (namespaceURI.equals(XMLNS_KABEJA_PARSER)) {
            if (localName.equals(ELEMENT_HANDLER)) {
                String clazz = atts.getValue(ATTRIBUTE_CLASS);
                try {
                    Class c = this.getClass().getClassLoader().loadClass(clazz);
                    handler = (Handler) c.newInstance();
                    currentHandlerManager.addHandler(handler);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if (localName.equals(ELEMENT_HANDLERS)) {
                stack.add(currentHandlerManager);
                currentHandlerManager = (HandlerManager) handler;
            } else if (localName.equals(ELEMENT_PARSER)) {
                String clazz = atts.getValue(ATTRIBUTE_CLASS);
                try {
                    Class c = this.getClass().getClassLoader().loadClass(clazz);
                    this.parser = (Parser) c.newInstance();
                    if (this.parser instanceof HandlerManager) {
                        this.currentHandlerManager = (HandlerManager) this.parser;
                    }
                    this.stack = new Stack();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public Parser getParser() {
        return parser;
    }

    /**
     *
     * @param in
     *            the InputStream
     * @return The DXFParser build from the XML description
     */
    public static Parser buildFromStream(InputStream in) {
        SAXParserBuilder builder = new SAXParserBuilder();
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setFeature("http://apache.org/xml/features/xinclude", true);
            parser.setContentHandler(builder);
            parser.parse(new InputSource(in));
        } catch (SAXException e) {
            System.err.println(e.getMessage());
        } catch (IOException ioe) {
        }
        return builder.getParser();
    }
}
