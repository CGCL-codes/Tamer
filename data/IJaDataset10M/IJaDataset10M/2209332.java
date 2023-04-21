package xmlsync2;

import java.io.FileInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import datawave.util.LoggerCache;
import datawave.xmlsync.util.XMLRender;

public class XMLAPI {

    private static Logger log = LoggerCache.get(Synchronizer.class.getName());

    public static Element skipuntilelement(Node node) {
        while (node != null && node.getNodeType() != Node.ELEMENT_NODE) node = node.getNextSibling();
        return (Element) node;
    }

    public static String encodeuri(final String xml, final String xpath) throws TransformerException, XmlsyncException, UnsupportedEncodingException {
        Document doc = loadxml(xml);
        NodeList list = XPathAPI.selectNodeList(doc, xpath);
        for (int i = 0; i < list.getLength(); i++) switch(list.item(i).getNodeType()) {
            case Node.ELEMENT_NODE:
                encodeelementuri((Element) list.item(i));
                break;
            case Node.ATTRIBUTE_NODE:
                list.item(i).setNodeValue(URLEncoder.encode(Str.systemencode(list.item(i).getNodeValue())));
                break;
        }
        return xml(doc);
    }

    public static String decodeuri(final String xml, final String xpath) throws TransformerException, XmlsyncException, UnsupportedEncodingException {
        Document doc = loadxml(xml);
        NodeList list = XPathAPI.selectNodeList(doc, xpath);
        for (int i = 0; i < list.getLength(); i++) switch(list.item(i).getNodeType()) {
            case Node.ELEMENT_NODE:
                decodeelementuri((Element) list.item(i));
                break;
            case Node.ATTRIBUTE_NODE:
                list.item(i).setNodeValue(URLDecoder.decode(list.item(i).getNodeValue()));
                break;
        }
        return xml(doc);
    }

    /**
	 * Method decodeelementuri.
	 * @param element
	 */
    private static void decodeelementuri(Element element) throws UnsupportedEncodingException {
        Node node = element.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.TEXT_NODE) node.setNodeValue(URLDecoder.decode(Str.systemencode(node.getNodeValue())));
            node = node.getNextSibling();
        }
    }

    /**
	 * Method encodeelementuri.
	 * @param element
	 */
    private static void encodeelementuri(Element element) throws UnsupportedEncodingException {
        Node node = element.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                node.setNodeValue(Str.replace(URLEncoder.encode(Str.systemencode(node.getNodeValue())), "+", "%20"));
            }
            node = node.getNextSibling();
        }
    }

    public static String evaluateloadpath(final String root, final String path) {
        if (path.indexOf(":") < 0) return root + path; else return path;
    }

    public static Document replaceinclude(Document script, final String root, String prefix) throws XmlsyncException {
        try {
            NodeList includelist = XPathAPI.selectNodeList(script, "//" + prefix + ":include");
            for (int i = 0; i < includelist.getLength(); i++) {
                Element include = (Element) includelist.item(i);
                NodeList nodelist = replaceinclude(XMLAPI.load(evaluateloadpath(root, include.getAttribute("href"))), root, prefix).getDocumentElement().getChildNodes();
                for (int j = 0; j < nodelist.getLength(); j++) include.getParentNode().insertBefore(script.importNode(nodelist.item(j), true), include);
                include.getParentNode().removeChild(include);
            }
            return script;
        } catch (Exception xe) {
            throw new XmlsyncException("[[SynchInclude.run]] source : " + xe.getMessage());
        }
    }

    public static Document load(final String path) throws XmlsyncException {
        String uri;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            if (path.indexOf(":") < 2) return factory.newDocumentBuilder().parse(new FileInputStream(path)); else return factory.newDocumentBuilder().parse(path);
        } catch (FileNotFoundException e) {
            Object[] args = { path, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0025"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        } catch (SAXException e) {
            Object[] args = { path, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0026"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        } catch (IOException e) {
            Object[] args = { path, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0027"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        } catch (ParserConfigurationException e) {
            Object[] args = { path, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0026"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
    }

    public static Document loadxml(final String xml) throws XmlsyncException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            Object[] args = { xml, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0028"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
    }

    private static String attributes2str(final NamedNodeMap list) {
        int size = list.getLength();
        if (size > 0) {
            String result = "";
            for (int i = 0; i < size; i++) result = result + " " + ((Attr) list.item(i)).getName() + "=\"" + encode(((Attr) list.item(i)).getValue()) + "\"";
            return result;
        } else return "";
    }

    private static String nodelist2str(final NodeList list) {
        int size = list.getLength();
        if (size > 0) {
            String result = "";
            for (int i = 0; i < size; i++) result = result + XMLAPI.xml(list.item(i));
            return result;
        } else return "";
    }

    public static String xml(final Node node) {
        XMLRender render = new XMLRender();
        return render.toString(node);
    }

    public static Document newdocument() throws XmlsyncException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        try {
            return factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            Object[] args = { e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0029"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
    }

    public static String text(final Element element) {
        XMLRender render = new XMLRender();
        return render.text(element);
    }

    public static String valueof(final Node current, String xpath) throws XmlsyncException {
        Node node = null;
        try {
            node = XPathAPI.selectSingleNode(current, xpath);
        } catch (TransformerException e) {
            Object[] args = { xpath, e.getMessage() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0030"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
        if (node == null) return null; else {
            switch(node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    return text((Element) node);
                default:
                    return node.getNodeValue();
            }
        }
    }

    public static void replaceTextNode(Document target, Element element, final String value) throws XmlsyncException {
        NodeList childlist = element.getChildNodes();
        for (int i = 0; i < childlist.getLength(); i++) {
            Node cur = childlist.item(i);
            if (cur.getNodeType() == Node.TEXT_NODE) {
                element.removeChild(cur);
            }
        }
        element.appendChild(target.createTextNode(value));
    }

    public static void set(Document target, final String xpath, final String value) throws XmlsyncException {
        if (xpath.charAt(0) == '/') {
            int pos = xpath.indexOf("/@");
            if (pos < 0) {
                Element element = forcexpath(target, xpath);
                if (element != null) replaceTextNode(target, element, value);
            } else {
                Element element = forcexpath(target, xpath.substring(0, pos));
                if (element != null) element.setAttribute(xpath.substring(pos + 2, xpath.length()), value);
            }
        } else {
            Object[] args = { xpath };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0031"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
    }

    public static String encode(final String input) {
        StringBuffer buffer = new StringBuffer(input.length());
        for (int i = 0; i < input.length(); i++) switch(input.charAt(i)) {
            case '&':
                buffer.append("&amp;");
                break;
            case '<':
                buffer.append("&lt;");
                break;
            case '"':
                buffer.append("&quot;");
                break;
            default:
                buffer.append(input.charAt(i));
                break;
        }
        return buffer.toString();
    }

    public static String decode(final String input) {
        int length = input.length();
        StringBuffer buffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) if (input.charAt(i) == '&') if (i + 1 < length) switch(input.charAt(i + 1)) {
            case 'a':
                if (i + 4 < input.length() && input.substring(i, i + 5).equals("&amp;")) {
                    buffer.append('&');
                    i += 4;
                    break;
                } else {
                    buffer.append(input.charAt(i));
                    break;
                }
            case 'l':
                if (i + 3 < input.length() && input.substring(i, i + 4).equals("&lt;")) {
                    buffer.append('<');
                    i += 3;
                    break;
                } else {
                    buffer.append(input.charAt(i));
                    break;
                }
            case 'q':
                if (i + 5 < input.length() && input.substring(i, i + 6).equals("&quot;")) {
                    buffer.append('"');
                    i += 5;
                    break;
                } else {
                    buffer.append(input.charAt(i));
                    break;
                }
            default:
                buffer.append(input.charAt(i));
                break;
        } else buffer.append(input.charAt(i)); else buffer.append(input.charAt(i));
        return buffer.toString();
    }

    public static void setxml(Document target, final String xpath, final Document source) throws XmlsyncException {
        if (xpath.charAt(0) == '/' && xpath.indexOf("/@") < 0) {
            if (xpath.equals("/") && target.getDocumentElement() != null) {
                Object[] args = { xpath };
                String msg = MessageFormat.format(Messages.getString("XMLSYNC0032"), args);
                log.error(msg);
                throw new XmlsyncException(msg);
            } else if (xpath.equals("/")) target.appendChild(target.importNode(source.getDocumentElement(), true)); else {
                Element element = forcexpath(target, xpath);
                if (element != null) element.appendChild(target.importNode(source.getDocumentElement(), true));
            }
        } else {
            Object[] args = { xpath };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0031"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
    }

    private static Element forcexpath(Document target, final String elementpath) throws XmlsyncException {
        Element element = null;
        if (elementpath.indexOf("[]") < 0) try {
            element = (Element) XPathAPI.selectSingleNode(target, elementpath);
        } catch (TransformerException e) {
            Object[] args = { elementpath, e.getMessageAndLocation() };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0030"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
        if (element == null) return buildelementpath(target, elementpath); else return element;
    }

    private static Element buildelementpath(Document target, final String elementpath) throws XmlsyncException {
        String naked = Str.trim(elementpath, '/');
        String root = Str.headof(naked, '/');
        if (target.getDocumentElement() == null) target.appendChild(target.createElement(root)); else if (!target.getDocumentElement().getTagName().equals(root)) {
            Object[] args = { elementpath, root };
            String msg = MessageFormat.format(Messages.getString("XMLSYNC0034"), args);
            log.error(msg);
            throw new XmlsyncException(msg);
        }
        return buildelement(target.getDocumentElement(), Str.tailof(naked, '/'));
    }

    private static Element buildelement(Element element, final String elementpath) throws XmlsyncException {
        if (element == null) return null; else {
            if (elementpath.equals("")) return element;
            String head = Str.headof(elementpath, '/');
            String tail = Str.tailof(elementpath, '/');
            if (head.indexOf("[]") < 0) {
                Element found;
                try {
                    found = (Element) XPathAPI.selectSingleNode(element, head);
                } catch (TransformerException e) {
                    Object[] args = { head, e.getMessageAndLocation() };
                    String msg = MessageFormat.format(Messages.getString("XMLSYNC0030"), args);
                    log.error(msg);
                    throw new XmlsyncException(msg);
                }
                if (found == null) if (head.indexOf("[") < 0) return buildelement((Element) element.appendChild(element.getOwnerDocument().createElement(Str.nameof(head))), tail); else return buildelement(padelement(element, head), tail); else return buildelement(found, tail);
            } else return buildelement((Element) element.appendChild(element.getOwnerDocument().createElement(Str.nameof(head))), tail);
        }
    }

    private static Element padelement(Element element, final String elementpath) throws XmlsyncException {
        String predicate = Str.predicateof(elementpath);
        String name = Str.nameof(elementpath);
        if (Str.isnumber(predicate)) {
            int needtoadd;
            try {
                needtoadd = Integer.parseInt(predicate) - XPathAPI.selectNodeList(element, name).getLength();
            } catch (TransformerException e) {
                Object[] args = { name, e.getMessageAndLocation() };
                String msg = MessageFormat.format(Messages.getString("XMLSYNC0030"), args);
                log.error(msg);
                throw new XmlsyncException(msg);
            }
            Element newelement = null;
            for (int i = 0; i < needtoadd; i++) newelement = (Element) element.appendChild(element.getOwnerDocument().createElement(name));
            return newelement;
        } else return null;
    }

    public static NodeList selectnodelistincontext(final String select, final Node context, final Document doc) throws TransformerException, XmlsyncException {
        if (select.length() == 0) throw new XmlsyncException("[[XMLAPI.selectnodelistincontext]] source : select attribute is mandatory");
        if (context == null) return XPathAPI.selectNodeList(doc, select); else return XPathAPI.selectNodeList(context, select);
    }
}
