package org.bungeni.translators.process.actions;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.bungeni.translators.configurations.steps.OAProcessStep;
import org.bungeni.translators.translator.OADocumentBuilder;
import org.bungeni.translators.utility.transformer.GenericTransformer;
import org.bungeni.translators.utility.xpathresolver.XPathResolver;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * This action unescapes escaped HTML content in a XML document xpath
 * It also cleans up the HTML to ensure valid xml is generated.
 * The html cleaning is implemented using JSoup
 *
 * @author Ashok
 */
public class ProcessUnescape implements IProcessAction {

    private static org.apache.log4j.Logger log = Logger.getLogger(ProcessUnescape.class.getName());

    public Document process(Document inputDocument, OAProcessStep processInfo) throws TransformerException, SAXException, IOException {
        Document outputDocument = inputDocument;
        try {
            String xPath = processInfo.getParam();
            NodeList nlMatching = (NodeList) XPathResolver.getInstance().evaluate(outputDocument, xPath, XPathConstants.NODESET);
            GenericTransformer genTrans = GenericTransformer.getInstance();
            Transformer transformer = genTrans.getTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            for (int i = 0; i < nlMatching.getLength(); i++) {
                Node nNodetoUnescape = nlMatching.item(i);
                if (nNodetoUnescape.hasChildNodes()) {
                    NodeList childNodes = nNodetoUnescape.getChildNodes();
                    StringWriter swOutputChildren = new StringWriter();
                    for (int c = 0; c < childNodes.getLength(); c++) {
                        Node nchild = childNodes.item(c);
                        transformer.transform(new DOMSource(nchild), new StreamResult(swOutputChildren));
                    }
                    String unescapedHTML = unescapeHtml(swOutputChildren.toString());
                    String jsoup_html = Jsoup.clean(unescapedHTML, Whitelist.relaxed());
                    jsoup_html = "<div xmlns=\"http://www.w3.org/1999/xhtml/\">" + unescapeHtml(jsoup_html) + "</div>";
                    Document docJsoupNode = OADocumentBuilder.getInstance().getDocumentBuilder().parse(new InputSource(new StringReader(jsoup_html)));
                    Node importThisNode = docJsoupNode.getDocumentElement();
                    while (nNodetoUnescape.hasChildNodes()) {
                        nNodetoUnescape.removeChild(nNodetoUnescape.getLastChild());
                    }
                    outputDocument.adoptNode(importThisNode);
                    nNodetoUnescape.appendChild(importThisNode);
                }
            }
        } catch (TransformerConfigurationException ex) {
            log.error("Error while processing UNESCAPE", ex);
        } catch (XPathExpressionException ex) {
            log.error("Error while processing UNESCAPE", ex);
        } finally {
            try {
                GenericTransformer.getInstance().getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            } catch (TransformerConfigurationException ex) {
                log.error("Error while resetting transformer configuration", ex);
            }
        }
        return outputDocument;
    }

    private Pattern pentity = Pattern.compile("&[a-zA-Z]+;");

    /***
     * Recursive UN-ESCAPER ; unescape escaped entities e.g. &amp;nbsp;
     * @param inputHtml
     * @return
     */
    private String unescapeHtml(String inputHtml) {
        String sUnescaped = StringEscapeUtils.unescapeHtml(inputHtml);
        Matcher mentity = pentity.matcher(sUnescaped);
        for (; true == mentity.find(); ) {
            sUnescaped = StringEscapeUtils.unescapeHtml(sUnescaped);
            mentity = pentity.matcher(sUnescaped);
        }
        return sUnescaped;
    }
}
