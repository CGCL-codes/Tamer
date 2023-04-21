package websiteschema.crawler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Administrator
 */
public class HTMLParserToDom {

    public Document[] HTMLConvert(NodeList nodelist) throws ParserConfigurationException, ParserException {
        if (0 == nodelist.size()) {
            return null;
        }
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element html = null;
        Element body = null;
        NodeIterator nodeiterator = nodelist.elements();
        while (nodeiterator.hasMoreNodes()) {
            Node node = nodeiterator.nextNode();
            if (node instanceof Tag) {
                System.err.println(node.getText());
            }
            if (node.getChildren() != null) {
                nodeiterator = node.getChildren().elements();
            }
        }
        Node iter_node = nodelist.elementAt(0);
        NodeList nodelist1 = iter_node.getChildren();
        html = doc.createElement("HTML");
        for (int i = 0; i < nodelist1.size(); i++) {
            body = doc.createElement("BODY");
            body.setTextContent(nodelist1.elementAt(i).toPlainTextString());
            html.appendChild(body);
        }
        doc.appendChild(html);
        return new Document[] { doc };
    }
}
