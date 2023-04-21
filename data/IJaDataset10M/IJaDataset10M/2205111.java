package org.spark.util;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtils {

    public static final String[] XML_ENTITIES = new String[] { "<", ">", "&", "'", "\"" };

    public static final String[] XML_ENTITIES_REF = new String[] { "&lt;", "&gt;", "&amp;", "&apos;", "&quot;" };

    public static Document newDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (Exception err) {
            return null;
        }
    }

    public static Document parse(String _xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(_xml)));
        } catch (Exception err) {
            return null;
        }
    }

    public static Document parse(InputStream _stream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(_stream);
        } catch (Exception err) {
            return null;
        }
    }

    public static NodeList getNodeList(String _xml, String _xpath) {
        return getNodeList(parse(_xml), _xpath);
    }

    public static NodeList getNodeList(Node _node, String _xpath) {
        try {
            return XPathAPI.selectNodeList(_node, _xpath);
        } catch (Exception err) {
            return null;
        }
    }

    public static Node getNode(String _xml, String _xpath) {
        return getNode(parse(_xml), _xpath);
    }

    public static Node getNode(Node _node, String _xpath) {
        try {
            return XPathAPI.selectSingleNode(_node, _xpath);
        } catch (Exception err) {
            return null;
        }
    }

    public static Object getValue(Object _obj, String _xpath) {
        if (_obj instanceof String) return getValue((String) _obj, _xpath); else {
            try {
                JXPathContext context = JXPathContext.newContext(_obj);
                return context.getValue(_xpath);
            } catch (Throwable err) {
                return null;
            }
        }
    }

    public static String getValue(String _xml, String _xpath) {
        return getValue(parse(_xml), _xpath);
    }

    public static String getValue(Node _node, String _xpath) {
        return getValue(getNode(_node, _xpath));
    }

    public static String getValue(Node _node) {
        if (_node == null) return null;
        if (Node.TEXT_NODE == _node.getNodeType() || Node.ATTRIBUTE_NODE == _node.getNodeType() || Node.CDATA_SECTION_NODE == _node.getNodeType()) return _node.getNodeValue(); else {
            if (!_node.hasChildNodes()) return ""; else return _node.getFirstChild().getNodeValue();
        }
    }

    public static String translate(String _xml, String _xslt) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new StringReader(_xslt)));
            StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(new StringReader(_xml)), new StreamResult(writer));
            return writer.toString();
        } catch (Exception err) {
            return null;
        }
    }

    public static String translate(InputStream _xmlStream, InputStream _xsltStream) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(_xsltStream));
            StringWriter writer = new StringWriter();
            transformer.transform(new StreamSource(_xmlStream), new StreamResult(writer));
            return writer.toString();
        } catch (Exception err) {
            return null;
        }
    }

    public static String encodeEntities(String _src) {
        return StringUtils.replace(_src, XML_ENTITIES, XML_ENTITIES_REF);
    }

    public static String decodeEntities(String _src) {
        return StringUtils.replace(_src, XML_ENTITIES_REF, XML_ENTITIES);
    }
}
