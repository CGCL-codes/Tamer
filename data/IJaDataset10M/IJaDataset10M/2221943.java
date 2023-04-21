package org.biblestudio.util;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmlUtils {

    private static Transformer transformer = null;

    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();

    private static final char[] AMP_ENCODE = "&amp;".toCharArray();

    private static final char[] LT_ENCODE = "&lt;".toCharArray();

    private static final char[] GT_ENCODE = "&gt;".toCharArray();

    private XmlUtils() {
    }

    private static Transformer getTransformer() {
        if (transformer == null) {
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty("omit-xml-declaration", "yes");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return transformer;
    }

    public static Element getFirstChildEl(Element parentEl, String tagName) {
        NodeList list = parentEl.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) list.item(i);
                if (child.getTagName().equalsIgnoreCase(tagName)) {
                    return child;
                }
            }
        }
        return null;
    }

    public static Collection<Element> addChildElements(Collection<Element> col, Element parentEl) {
        NodeList children = parentEl.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) children.item(i);
                col.add(child);
            }
        }
        return col;
    }

    public static Collection<Element> addChildElements(Collection<Element> col, Element parentEl, String... tagNames) {
        if (tagNames == null || tagNames.length == 0) {
            throw new IllegalArgumentException();
        }
        if (col == null) {
            col = new ArrayList<Element>();
        }
        Map<String, String> map = null;
        String tagName = null;
        if (tagNames.length == 1) {
            tagName = tagNames[0].toLowerCase();
        } else {
            map = new Hashtable<String, String>(tagNames.length);
            for (String tag : tagNames) {
                map.put(tag.toLowerCase(), tag.toLowerCase());
            }
        }
        NodeList children = parentEl.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element) children.item(i);
                String tag = child.getTagName().toLowerCase();
                if ((tagName != null && tag.equals(tagName)) || (map != null && tag.equals(map.get(tag)))) {
                    col.add(child);
                }
            }
        }
        return col;
    }

    public static String getOuterXml(Node node) {
        try {
            Transformer transformer = getTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String coalesceAttribute(Element element, String attrName, String value) {
        return StringUtils.coalesce(element.getAttribute(attrName), value);
    }

    public static String coalesceAttribute(String value, Element element, String attrName) {
        return StringUtils.coalesce(value, element.getAttribute(attrName));
    }

    public static Document parseXmlDocument(String xml) throws Exception {
        return parseXmlDocument(xml, null);
    }

    public static Document createEmptyDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            return factory.newDocumentBuilder().newDocument();
        } catch (Exception e) {
            return null;
        }
    }

    public static Document parseXmlDocument(String xml, String encoding) throws Exception {
        byte[] data = null;
        if (encoding == null) {
            data = xml.getBytes();
        } else {
            data = xml.getBytes(encoding);
        }
        return parseXmlDocument(data);
    }

    public static Document parseXmlDocument(byte[] xmlData) throws Exception {
        return parseInputDocument(FileUtils.createInputStream(xmlData));
    }

    public static Document parseInputDocument(InputStream input) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        return factory.newDocumentBuilder().parse(input);
    }

    /**
     * Escapes all necessary characters in the String so that it can be used
     * in an XML doc.
     *
     * @param string the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static String escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            } else if (ch == '>') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            } else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            } else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * Unescapes the String by converting XML escape sequences back into normal
     * characters.
     *
     * @param string the string to unescape.
     * @return the string with appropriate characters unescaped.
     */
    public static String unescapeFromXML(String string) {
        string = StringUtils.replace(string, "&lt;", "<");
        string = StringUtils.replace(string, "&gt;", ">");
        string = StringUtils.replace(string, "&quot;", "\"");
        return StringUtils.replace(string, "&amp;", "&");
    }

    /**
     * This method takes a string and strips out all tags leaving
     * the tag body intact.
     * @param in the text to be converted.
     * @return the input string with all tags removed.
     */
    public static String stripAllTags(String xml) {
        if (xml == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = xml.toCharArray();
        int len = input.length;
        StringBuilder out = new StringBuilder((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
            } else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
            } else if (ch == '>') {
                last = i + 1;
            }
        }
        if (last == 0) {
            return xml;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }
}
