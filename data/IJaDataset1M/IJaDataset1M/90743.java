package org.yuzz.xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang.StringEscapeUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.yuzz.functor.Tuples;
import org.yuzz.functor.Tuples.Tuple2;

/**
 * To use in eclipse: 
 * Run As Java Application
 * Paste your html into the Console (Window->Show View->Console)
 * Hit Control-Z in the Console
 * @author Sean McEligot 
 *
 */
public class HtmlToSource {

    public void parse(PrintWriter out, InputSource in) throws SAXException, IOException {
        DOMParser parser = new DOMParser();
        parser.setFeature("http://cyberneko.org/html/features/augmentations", true);
        parser.setFeature("http://cyberneko.org/html/features/balance-tags", true);
        parser.parse(in);
        Document document = parser.getDocument();
        LinkedList<Tuples.Tuple2<String, Node>> list = filter(document, new LinkedList<Tuples.Tuple2<String, Node>>());
        for (int index = list.size() - 1; index >= 0; index--) {
            Tuple2<String, Node> pair = list.get(index);
            String id = pair._1();
            Node node = pair._2();
            out.println("Node " + id + " = " + print(node, "") + ";");
        }
        String str = print(document, "");
        out.println(str);
    }

    public String getIdAttribute(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return null;
        }
        for (int i = 0; i < attributes.getLength(); i++) {
            Node att = attributes.item(i);
            String name = att.getLocalName();
            if ("id".equalsIgnoreCase(name)) {
                return att.getNodeValue();
            }
        }
        return null;
    }

    private LinkedList<Tuples.Tuple2<String, Node>> filter(Node node, LinkedList<Tuples.Tuple2<String, Node>> list) {
        String id = getIdAttribute(node);
        if (id != null) {
            list.add(new Tuples.Tuple2<String, Node>(id, node));
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            filter(childNodes.item(i), list);
        }
        return list;
    }

    public void push(Queue<String> qq, String str) {
        if (empty(str)) {
            return;
        }
        qq.offer(str);
    }

    public boolean empty(String str) {
        return ((str == null) || (str.trim().length() == 0));
    }

    public String print(Node node, String indent) {
        StringBuilder buf = new StringBuilder();
        LinkedList<String> qq = new LinkedList<String>();
        if (node instanceof Text) {
            String nodeValue = node.getNodeValue();
            if (!empty(nodeValue)) {
                return "t(\"" + StringEscapeUtils.escapeJava(nodeValue.trim()) + "\")";
            }
            return "";
        }
        buf.append("\n" + indent + toMethodName(node.getNodeName()) + "");
        String nodeValue = node.getNodeValue();
        if (!empty(nodeValue)) {
            push(qq, "\"" + nodeValue.trim() + "\"");
        }
        NamedNodeMap atts = node.getAttributes();
        if (atts != null) {
            int attlen = atts.getLength();
            for (int i = 0; i < attlen; i++) {
                Attr att = (Attr) atts.item(i);
                String attstr = formatAtt(att);
                push(qq, attstr);
            }
        }
        Node child = node.getFirstChild();
        while (child != null) {
            String id = getIdAttribute(child);
            if (id != null) {
                push(qq, id);
            } else {
                push(qq, print(child, indent + " "));
            }
            child = child.getNextSibling();
        }
        if (qq.size() > 0) {
            buf.append(", ");
            Iterator<String> it = qq.iterator();
            while (it.hasNext()) {
                buf.append(it.next());
                if (it.hasNext()) {
                    buf.append(", ");
                }
            }
        }
        buf.append(")");
        return buf.toString();
    }

    private String formatAtt(Attr att) {
        String attstr = "a(\"" + att.getName() + "\", \"" + att.getValue() + "\")";
        return attstr;
    }

    private String toMethodName(String string) {
        string = string.replace("#", "");
        return "n(\"" + string.toLowerCase() + "\"";
    }

    public static void main(String[] argv) throws Exception {
        HtmlToSource hp = new HtmlToSource();
        PrintWriter out = new PrintWriter(System.out);
        hp.parse(out, new InputSource(System.in));
        out.println(";");
        out.flush();
        out.close();
    }
}
