package com.tkok.xdv.xpath.jaxen;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import com.tkok.xdv.xpath.XPathFactory;
import com.tkok.xdv.xpath.XPathValue;
import com.tkok.xdv.xpath.XPathValueNodeSet;
import com.tkok.xdv.xpath.XPathVariableMap;
import com.tkok.xdv.xpath.XPathXdvException;
import com.tkok.xdv.xpath.common.CommonXPathValueBoolean;
import com.tkok.xdv.xpath.common.CommonXPathValueNumber;
import com.tkok.xdv.xpath.common.CommonXPathValueString;
import com.tkok.xdv.xpath.common.CommonXPathVariableMap;
import com.tkok.xdv.xpath.common.DomXPathValueNodeSet;

/**
 * XPath factory for Jaxen XPath engine bound to use W3C DOM documents.
 *
 * @author Tomasz Kokoszka (tkokoszka)
 */
public class JaxenXPathFactory implements XPathFactory<Document, Node> {

    /**
   * Loads XML document from given input stream.
   */
    public static Document loadXmlDocument(InputStream input) throws IOException, SAXException, ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
    }

    @Override
    public JaxenXPathExecutionUnit createExecutionUnit(Document document) {
        return new JaxenXPathExecutionUnit(this, document);
    }

    @Override
    public JaxenXPathExpression createExpression(String xpath) throws XPathXdvException {
        DOMXPath x;
        try {
            x = new DOMXPath(xpath);
        } catch (JaxenException e) {
            throw new XPathXdvException(e.getMessage(), xpath, e);
        }
        return new JaxenXPathExpression(x);
    }

    @Override
    public XPathVariableMap createVariableMap() {
        return new CommonXPathVariableMap();
    }

    @Override
    public XPathValue createValueBoolean(boolean value) {
        return new CommonXPathValueBoolean(Boolean.valueOf(value));
    }

    @Override
    public XPathValue createValueNumber(double value) {
        return new CommonXPathValueNumber(Double.valueOf(value));
    }

    @Override
    public XPathValue createValueString(String value) {
        return new CommonXPathValueString(value);
    }

    @Override
    public XPathValueNodeSet<Node> createValueNodeSet(List<Node> nodes) {
        return new DomXPathValueNodeSet(nodes);
    }
}
