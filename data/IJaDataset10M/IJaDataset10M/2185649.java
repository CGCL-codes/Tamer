package com.liferay.portal.xml;

import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.XPath;
import java.util.List;

/**
 * <a href="XPathImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class XPathImpl implements XPath {

    public XPathImpl(org.dom4j.XPath xPath) {
        _xPath = xPath;
    }

    public boolean booleanValueOf(Object context) {
        return _xPath.booleanValueOf(toOldContext(context));
    }

    public boolean equals(Object obj) {
        org.dom4j.XPath xPath = ((XPathImpl) obj).getWrappedXPath();
        return _xPath.equals(xPath);
    }

    public Object evaluate(Object context) {
        return toNewContext(_xPath.evaluate(toOldContext(context)));
    }

    public String getText() {
        return _xPath.getText();
    }

    public org.dom4j.XPath getWrappedXPath() {
        return _xPath;
    }

    public int hashCode() {
        return _xPath.hashCode();
    }

    public boolean matches(Node node) {
        NodeImpl nodeImpl = (NodeImpl) node;
        return _xPath.matches(nodeImpl.getWrappedNode());
    }

    public Number numberValueOf(Object context) {
        return _xPath.numberValueOf(toOldContext(context));
    }

    public List<Node> selectNodes(Object context) {
        return SAXReaderImpl.toNewNodes(_xPath.selectNodes(toOldContext(context)));
    }

    public List<Node> selectNodes(Object context, XPath sortXPath) {
        XPathImpl sortXPathImpl = (XPathImpl) sortXPath;
        return SAXReaderImpl.toNewNodes(_xPath.selectNodes(toOldContext(context), sortXPathImpl.getWrappedXPath()));
    }

    public List<Node> selectNodes(Object context, XPath sortXPath, boolean distinct) {
        XPathImpl sortXPathImpl = (XPathImpl) sortXPath;
        return SAXReaderImpl.toNewNodes(_xPath.selectNodes(toOldContext(context), sortXPathImpl.getWrappedXPath(), distinct));
    }

    public Node selectSingleNode(Object context) {
        org.dom4j.Node node = _xPath.selectSingleNode(toOldContext(context));
        if (node == null) {
            return null;
        } else {
            return new NodeImpl(node);
        }
    }

    public void sort(List<Node> nodes) {
        _xPath.sort(SAXReaderImpl.toOldNodes(nodes));
    }

    public void sort(List<Node> nodes, boolean distinct) {
        _xPath.sort(SAXReaderImpl.toOldNodes(nodes), distinct);
    }

    public String valueOf(Object context) {
        return _xPath.valueOf(toOldContext(context));
    }

    protected Object toNewContext(Object context) {
        if (context == null) {
            return null;
        } else if (context instanceof org.dom4j.Document) {
            org.dom4j.Document document = (org.dom4j.Document) context;
            return new DocumentImpl(document);
        } else if (context instanceof org.dom4j.Node) {
            org.dom4j.Node node = (org.dom4j.Node) context;
            return new NodeImpl(node);
        } else if (context instanceof List) {
            return SAXReaderImpl.toNewNodes((List<org.dom4j.Node>) context);
        } else {
            return context;
        }
    }

    protected Object toOldContext(Object context) {
        if (context == null) {
            return null;
        } else if (context instanceof DocumentImpl) {
            DocumentImpl documentImpl = (DocumentImpl) context;
            return documentImpl.getWrappedDocument();
        } else if (context instanceof NodeImpl) {
            NodeImpl nodeImpl = (NodeImpl) context;
            return nodeImpl.getWrappedNode();
        } else if (context instanceof List) {
            return SAXReaderImpl.toOldNodes((List<Node>) context);
        } else {
            return context;
        }
    }

    private org.dom4j.XPath _xPath;
}
