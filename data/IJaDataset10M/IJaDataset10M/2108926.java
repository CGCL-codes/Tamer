package net.sourceforge.javabits.xml.dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jochen Kuhnle
 */
public class Nodes {

    public static final Node[] EMPTY_ARRAY = new Node[0];

    public static final Comparator<Node> COMPARE_TEXT_CONTENT = new Comparator<Node>() {

        public int compare(Node o1, Node o2) {
            return o1.getTextContent().compareTo(o2.getTextContent());
        }
    };

    public static final Comparator<Node> COMPARE_NAME = new Comparator<Node>() {

        public int compare(Node o1, Node o2) {
            return o1.getNodeName().compareTo(o2.getNodeName());
        }
    };

    public static Element appendElement(Node parent, String name) {
        Element child = parent.getOwnerDocument().createElement(name);
        parent.appendChild(child);
        return child;
    }

    public static Element appendTextElement(Node parent, String name, String value) {
        Element child = appendElement(parent, name);
        child.setTextContent(value);
        return child;
    }

    public static final Node getChild(Node parent, String name, boolean create) {
        Node result = null;
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (name.equals(child.getNodeName())) {
                result = child;
                break;
            }
        }
        if (result == null && create) {
            result = appendElement(parent, name);
        }
        return result;
    }

    public static final Node getPath(Node element, String path, boolean create) {
        Node result = element;
        for (StringTokenizer st = new StringTokenizer(path, "/"); st.hasMoreElements(); ) {
            String name = st.nextToken();
            Node child = getChild(result, name, create);
            if (child == null) {
                result = null;
                break;
            }
            result = child;
        }
        return result;
    }

    public static final void removeChildren(Node parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); ) {
            Node child = children.item(i);
            if (name.equals(child.getNodeName())) {
                parent.removeChild(child);
            } else {
                ++i;
            }
        }
    }

    public static final String getPathText(Node context, String path, String defaultValue) {
        String result = defaultValue;
        Node node = getPath(context, path, false);
        if (node != null) {
            result = node.getTextContent();
        }
        return result;
    }

    public static final void sort(Node node, Comparator<Node> comparator) {
        NodeList children = node.getChildNodes();
        List<Node> childList = new ArrayList<Node>();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            childList.add(child);
        }
        for (Node child : childList) {
            node.removeChild(child);
        }
        Collections.sort(childList, comparator);
        for (Node child : childList) {
            node.appendChild(child);
        }
    }

    public static final Element setTextElement(Element context, String name, String value) {
        Element element = (Element) getChild(context, name, true);
        element.setTextContent(value);
        return element;
    }
}
