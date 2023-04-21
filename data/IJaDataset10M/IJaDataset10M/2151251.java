package org.wikiup.core.imp.df;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wikiup.core.imp.document.DocumentImpl;
import org.wikiup.core.inf.Document;
import org.wikiup.util.Assert;
import org.wikiup.util.StringUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AbstractDocumentFilter {

    private DocumentBuilder builder = null;

    private DocumentBuilder getDOMBuilder() {
        try {
            if (builder == null) builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Assert.fail(ex);
        }
        return builder;
    }

    private Document duplicate(org.w3c.dom.Element node) {
        DocumentImpl data = new DocumentImpl(node.getNodeName());
        loadChildren(node, data);
        return data;
    }

    private Document duplicate(org.w3c.dom.Document doc) {
        return duplicate(doc.getDocumentElement());
    }

    private void loadAttributes(org.w3c.dom.Element element, Document doc) {
        NamedNodeMap attributes = element.getAttributes();
        int i, count = attributes.getLength();
        for (i = 0; i < count; i++) {
            Node curNode = attributes.item(i);
            doc.addAttribute(curNode.getNodeName()).setObject(curNode.getNodeValue());
        }
    }

    private void loadChildren(Element element, Document entry) {
        boolean valued = false;
        NodeList nodeList = element.getChildNodes();
        int i, count = nodeList.getLength();
        loadAttributes(element, entry);
        for (i = 0; i < count; i++) {
            Node curChild = nodeList.item(i);
            switch(curChild.getNodeType()) {
                case Node.TEXT_NODE:
                    if (!valued) entry.setObject(StringUtil.trim(curChild.getNodeValue()));
                    valued = true;
                    break;
                case Node.ELEMENT_NODE:
                    Element curElement = (Element) curChild;
                    loadChildren(curElement, entry.addChild(curElement.getNodeName()));
                    break;
                case Node.CDATA_SECTION_NODE:
                    entry.setObject(curChild.getNodeValue());
                    valued = true;
                    break;
                default:
                    break;
            }
        }
    }

    protected Document parse(InputSource source) {
        DocumentBuilder docBuilder = getDOMBuilder();
        Document doc = null;
        try {
            doc = duplicate(docBuilder.parse(source));
        } catch (IOException ex) {
            Assert.fail(ex);
        } catch (SAXException ex) {
            Assert.fail(ex);
        }
        return doc;
    }
}
