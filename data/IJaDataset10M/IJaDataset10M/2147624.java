package pikes.html.xhtml.version10;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import pikes.html.AbstractHtmlPage;
import pikes.xml.XMLTag;

/**
 * General XHTML 1.0 compliant web page. This class can be used as it is. The default HTML content will be an XML
 * document with transient HTML doc type and 'html' root element.<br>
 * The user of this class can set the following properties: <code>
 * <ul>
 *  <li><code>docType</code> of type {@link HtmlDocType}. The default is transient {@link TransitionalDocType}</li>
 * </ul>
 * </code> <br>
 * The implementation of this class can override {@link HtmlPage#buildHtmlPage(XMLTag, Map, HttpServletRequest)} method
 * to build content for the html page.
 * @author Peter Bona
 * @since 0.0.1
 */
public class HtmlPage extends AbstractHtmlPage {

    public HtmlPage() {
        setDocType(new TransitionalDocType());
    }

    protected final XMLTag createRootHtmlTag(Map model, HttpServletRequest request) throws Exception {
        XMLTag htmlTag = new XMLTag("html");
        buildHtmlPage(htmlTag, model, request);
        return htmlTag;
    }

    /**
	 * Builds the content of the page by adding children tags to the root html tag. This methods is to be overridden by
	 * subclasses because it does nothing.
	 * @param htmlTag
	 * @param model
	 * @param request
	 */
    protected void buildHtmlPage(XMLTag htmlTag, Map model, HttpServletRequest request) {
    }
}
