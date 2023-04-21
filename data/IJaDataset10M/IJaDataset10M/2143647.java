package websiteschema.element;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.ParserConfigurationException;
import org.jaxen.SimpleNamespaceContext;
import java.util.List;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.NamedNodeMap;
import java.io.StringWriter;
import java.util.Collections;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import websiteschema.element.xpath.XPathParser;
import websiteschema.utils.FileUtil;

/**
 *
 * @author ray
 */
public class DocumentUtil {

    public static String getXMLString(Document doc) {
        return toString(doc);
    }

    private static String toString(Document document) {
        String result = null;
        if (document != null) {
            verifyDocument(document);
            StringWriter strWtr = new StringWriter();
            StreamResult strResult = new StreamResult(strWtr);
            TransformerFactory tfac = TransformerFactory.newInstance();
            try {
                Transformer t = tfac.newTransformer();
                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.setOutputProperty(OutputKeys.METHOD, "xml");
                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                t.transform(new DOMSource(document.getDocumentElement()), strResult);
            } catch (Exception e) {
                System.err.println("DocumentUtil.toString(Document): " + e);
                e.printStackTrace();
            }
            result = strResult.getWriter().toString();
        }
        return result;
    }

    public static List<Node> getByXPath(Document doc, String xpathExpr) {
        List<Node> nodes = null;
        try {
            String ns = doc.getDocumentElement().getNamespaceURI();
            DOMXPath xpath = new DOMXPath(buildXPath(xpathExpr, ns != null ? "pre" : null));
            if (null != ns) {
                SimpleNamespaceContext nsContext = new SimpleNamespaceContext();
                nsContext.addNamespace("pre", ns);
                xpath.setNamespaceContext(nsContext);
            }
            String clazz = doc.getClass().getName();
            nodes = xpath.selectNodes(doc);
            if (null != nodes) {
                if (clazz.startsWith("org.apache.xerces.dom")) {
                } else if (clazz.startsWith("com.gargoylesoftware.htmlunit.html")) {
                } else if (clazz.startsWith("$Proxy")) {
                    Collections.reverse(nodes);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nodes;
    }

    /**
     * 剔除属性值为空的属性
     * @param xpath
     * @param pre
     * @return
     */
    public static void verifyDocument(Document doc) {
        verifyDocument(doc.getDocumentElement());
    }

    private static void verifyDocument(Node node) {
        NamedNodeMap nodeMap = node.getAttributes();
        if (null != nodeMap) {
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Node attr = nodeMap.item(i);
                String name = attr.getNodeName();
                String value = attr.getNodeValue();
                if ("".equals(value)) {
                    nodeMap.removeNamedItem(name);
                    break;
                }
            }
        }
    }

    public static Document createEmptyDocument() {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Document getDocument(String file) {
        try {
            String content = null;
            File f = new File(file);
            if (f.exists()) {
                content = FileUtil.read(f);
            } else {
                content = FileUtil.readResource(file);
            }
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
            return doc;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Document convertTo(String xml) throws ParserConfigurationException, IOException, SAXException {
        StringReader sr = new StringReader(xml);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(is);
        return doc;
    }

    /**
     * 为XPATH增加pre namespace，实现简单，如果需要完全正确的结果，需要做XPATH的词法分析。
     * @param xpath
     * @param pre
     * @return
     */
    public static String buildXPath(String xpath, String pre) {
        if (null == pre || "".equals(pre)) {
            return xpath;
        }
        XPathParser parser = new XPathParser(xpath);
        parser.setNamespace(pre);
        return parser.toString();
    }

    public static String buildXPath2(String xpath, String pre) {
        if (null == pre || "".equals(pre)) {
            return xpath;
        }
        StringBuilder sb = new StringBuilder();
        String[] array = xpath.split("(?<!/)/(?!/)");
        if (array.length > 1) {
            for (int i = 0; i < array.length; i++) {
                String str = array[i];
                if (str.trim().length() > 0) {
                    if (!str.startsWith("//")) {
                        if (!str.startsWith("@") && !str.startsWith("*") && !str.endsWith(")")) {
                            sb.append(pre).append(":").append(str).append("/");
                        } else {
                            sb.append(str).append("/");
                        }
                    } else {
                        String content = str.substring(2);
                        if (!content.startsWith("@") && !content.startsWith("*") && !content.endsWith(")")) {
                            sb.append("//").append(pre).append(":").append(str.substring(2)).append("/");
                        } else {
                            sb.append(str).append("/");
                        }
                    }
                } else if (i == 0) {
                    sb.append("/");
                }
            }
        } else {
            String str = array[0];
            if (!str.startsWith("//")) {
                sb.append(pre).append(":").append(array[0]);
            } else {
                String content = str.substring(2);
                if (!content.startsWith("@") && !content.startsWith("*") && !content.endsWith(")")) {
                    sb.append("//").append(pre).append(":").append(content).append("/");
                } else {
                    sb.append(str);
                }
            }
        }
        String ret = sb.toString();
        return ret.endsWith("/") ? ret.substring(0, ret.length() - 1) : ret;
    }
}
