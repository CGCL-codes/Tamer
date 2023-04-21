package libgeo.kml;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Util {

    public static Node createTextElement(Document doc, String tagName, String textContent) {
        Node node = doc.createElement(tagName);
        node.setTextContent(textContent);
        return node;
    }

    public static Iterable<Node> getIterable(NodeList nodes) {
        ArrayList<Node> list = new ArrayList<Node>();
        for (int i = 0; i < nodes.getLength(); ++i) list.add(nodes.item(i));
        return list;
    }

    public static Element getElementByName(Node node, String name) {
        for (Node child : getIterable(node.getChildNodes())) if (child.getNodeName().equals(name)) return (Element) child;
        throw new IllegalArgumentException("node not found: " + name);
    }

    public static List<Element> getElementsByName(Node node, String name) {
        List<Element> nodes = new ArrayList<Element>();
        for (Node child : getIterable(node.getChildNodes())) if (child.getNodeName().equals(name)) nodes.add((Element) child);
        return nodes;
    }

    public static Node getChildByName(Node node, String name) {
        for (Node child : getIterable(node.getChildNodes())) if (child.getNodeName().equals(name)) return child;
        return null;
    }

    public static List<Node> getChildrenByName(Node node, String name) {
        List<Node> nodes = new ArrayList<Node>();
        for (Node child : getIterable(node.getChildNodes())) if (child.getNodeName().equals(name)) nodes.add(child);
        return nodes;
    }

    public static String getString(Node node, String property) {
        Iterable<Node> nodes = getIterable(node.getChildNodes());
        for (Node child : nodes) {
            if (child.getNodeName().equals(property)) return child.getTextContent();
        }
        return null;
    }

    public static Integer getInteger(Node node, String property) {
        Iterable<Node> nodes = getIterable(node.getChildNodes());
        for (Node child : nodes) {
            if (child.getNodeName().equals(property)) return Integer.valueOf(child.getTextContent(), 16);
        }
        return null;
    }

    public static Double getDouble(Node node, String property) {
        Iterable<Node> nodes = getIterable(node.getChildNodes());
        for (Node child : nodes) {
            if (child.getNodeName().equals(property)) return Double.parseDouble(child.getTextContent());
        }
        return null;
    }

    public static Boolean getBoolean(Node node, String property) {
        Iterable<Node> nodes = getIterable(node.getChildNodes());
        for (Node child : nodes) {
            if (child.getNodeName().equals(property)) return Boolean.parseBoolean(child.getTextContent());
        }
        return null;
    }
}
