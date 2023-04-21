package ca.huy.ximple.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ca.huy.ximple.XerialException;

/**
 * 
 * @author huyd
 */
public class Util {

    public static void writeStartTag(String name, String id, String className, Map attribs, Writer out) throws IOException, XerialException {
        if (attribs.containsKey("id") || attribs.containsKey("class")) {
            throw new XerialException("Attributes contains forbidden 'id' or 'class' attribute");
        }
        out.write("<" + name);
        if (id != null && id.length() > 0) {
            out.write(" id=\"" + id + "\"");
        }
        if (className != null && className.length() > 0) {
            out.write(" class=\"" + className + "\"");
        }
        if (attribs != null) {
            for (Iterator entries = attribs.entrySet().iterator(); entries.hasNext(); ) {
                Map.Entry entry = (Map.Entry) entries.next();
                out.write(' ');
                out.write((String) entry.getKey());
                out.write("=\"");
                out.write(Util.escapeXml((String) entry.getValue()));
                out.write('"');
            }
        }
        out.write(">");
    }

    public static void writeReference(String elementName, String refId, Writer out) throws IOException {
        out.write("<" + elementName + " refid=\"" + refId + "\"/>");
    }

    /**
     * Will escape the give basic XML entities (gt, lt, quot, amp, apos).
     * 
     * @param s the XML String to escape
     * @return the escaped XML String
     */
    public static String escapeXml(String s) {
        String result = replace(s, "&", "&amp;");
        result = replace(result, "<", "&lt;");
        result = replace(result, ">", "&gt;");
        result = replace(result, "\"", "&quot;");
        result = replace(result, "'", "&apos;");
        return result;
    }

    public static String unescapeXml(String s) {
        String result = replace(s, "&amp;", "&");
        result = replace(result, "&lt;", "<");
        result = replace(result, "&gt;", ">");
        result = replace(result, "&quot;", "\"");
        result = replace(result, "&apos;", "'");
        return result;
    }

    public static String replace(String s, String target, String replacement) {
        StringBuffer sb = new StringBuffer();
        int lastIdx = 0;
        int nextIdx = s.indexOf(target);
        while (nextIdx >= 0) {
            sb.append(s.substring(lastIdx, nextIdx));
            sb.append(replacement);
            lastIdx = nextIdx + target.length();
            nextIdx = s.indexOf(target, lastIdx);
        }
        sb.append(s.substring(lastIdx));
        return sb.toString();
    }

    public static String getElementText(Element root) {
        StringBuffer content = new StringBuffer();
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                content.append(node.getNodeValue());
            }
        }
        return content.toString();
    }
}
