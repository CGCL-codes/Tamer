package org.apache.jetspeed.portal.portlets;

import org.apache.ecs.ConcreteElement;
import org.apache.ecs.StringElement;
import org.apache.jetspeed.portal.PortletConfig;
import org.apache.jetspeed.portal.PortletException;
import org.apache.jetspeed.util.JetspeedClearElement;
import org.apache.jetspeed.util.MimeType;
import org.apache.jetspeed.cache.disk.JetspeedDiskCache;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserFactory;

/**
 * This portlet serves well-formed WML file content and strips
 * them of superfluous tags liks <wml>
 *
 * <strong>The strip capability has been temporarily disabled due to
 * class conflicts in Tomcat 3.2, so this portlet can only currently
 * serve fragments of WML documents, without the <wml> tag</strong>
 *
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @version $Id: WMLFilePortlet.java,v 1.11 2004/02/23 04:03:33 jford Exp $ 
 */
public class WMLFilePortlet extends FileWatchPortlet {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(WMLFilePortlet.class.getName());

    private ConcreteElement content = new StringElement("Not available");

    /**
      * @see Portlet#supportsType
      */
    public boolean supportsType(MimeType type) {
        return type.equals(MimeType.WML);
    }

    /** Initialize the content of the portlet
    */
    public void init() throws PortletException {
        PortletConfig config = this.getPortletConfig();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            content = new JetspeedClearElement(JetspeedDiskCache.getInstance().getEntry(config.getURL()).getData());
        } catch (Exception e) {
            throw new PortletException(e.getMessage());
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
            }
        }
    }

    public ConcreteElement getContent(RunData data) {
        return content;
    }

    class WMLFilter extends HandlerBase {

        private static final String DEFAULT_PARSER_NAME = "javax.xml.parsers.SAXParser";

        protected PrintWriter out = new PrintWriter(System.out);

        public WMLFilter(PrintWriter outPW) throws UnsupportedEncodingException {
            this.out = outPW;
        }

        public void filter(String uri) {
            try {
                HandlerBase handler = this;
                Parser parser = ParserFactory.makeParser(DEFAULT_PARSER_NAME);
                parser.setDocumentHandler(handler);
                parser.setErrorHandler(handler);
                parser.parse(uri);
            } catch (Exception e) {
                logger.error("Exception", e);
            }
        }

        public void processingInstruction(String target, String data) {
        }

        public void startElement(String name, AttributeList attrs) {
            if (name.equals("wml")) return;
            out.print('<');
            out.print(name);
            if (attrs != null) {
                int len = attrs.getLength();
                for (int i = 0; i < len; i++) {
                    out.print(' ');
                    out.print(attrs.getName(i));
                    out.print("=\"");
                    out.print(normalize(attrs.getValue(i)));
                    out.print('"');
                }
            }
            out.print('>');
        }

        public void characters(char ch[], int start, int length) {
            out.print(normalize(new String(ch, start, length)));
        }

        public void ignorableWhitespace(char ch[], int start, int length) {
            characters(ch, start, length);
        }

        public void endElement(String name) {
            if (name.equals("wml")) return;
            out.print("</");
            out.print(name);
            out.print('>');
        }

        public void endDocument() {
            out.flush();
        }

        public void warning(SAXParseException ex) {
            logger.info(getLocationString(ex) + ": " + ex.getMessage());
        }

        public void error(SAXParseException ex) {
            logger.error(getLocationString(ex) + ": " + ex, ex);
        }

        public void fatalError(SAXParseException ex) throws SAXException {
            logger.error(getLocationString(ex) + ": " + ex, ex);
            throw ex;
        }

        /**
        Retrieves the error location in the input stream
        */
        private String getLocationString(SAXParseException ex) {
            StringBuffer str = new StringBuffer();
            String systemId = ex.getSystemId();
            if (systemId != null) {
                int index = systemId.lastIndexOf('/');
                if (index != -1) systemId = systemId.substring(index + 1);
                str.append(systemId);
            }
            str.append(':');
            str.append(ex.getLineNumber());
            str.append(':');
            str.append(ex.getColumnNumber());
            return str.toString();
        }

        /**
        Escapes characters data
        */
        protected String normalize(String s) {
            StringBuffer str = new StringBuffer();
            int len = (s != null) ? s.length() : 0;
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                switch(ch) {
                    case '<':
                        {
                            str.append("&lt;");
                            break;
                        }
                    case '>':
                        {
                            str.append("&gt;");
                            break;
                        }
                    case '&':
                        {
                            str.append("&amp;");
                            break;
                        }
                    case '"':
                        {
                            str.append("&quot;");
                            break;
                        }
                    default:
                        {
                            str.append(ch);
                        }
                }
            }
            return str.toString();
        }
    }
}
