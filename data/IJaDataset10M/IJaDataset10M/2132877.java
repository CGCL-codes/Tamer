package Data;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import XML_IO.XML_IO;

public class SimpleLine {

    long head, tail;

    int type;

    public SimpleLine() {
        head = tail = 0;
    }

    public SimpleLine(long h, long t, int ty) {
        head = h;
        tail = t;
        type = ty;
    }

    public SimpleLine(Node node) {
        this();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            NamedNodeMap attributes = node.getAttributes();
            for (int k = 0; k < attributes.getLength(); k++) {
                Node attribute = attributes.item(k);
                if (attribute.getNodeName().equals("type")) {
                    type = Integer.parseInt(XML_IO.getElementValue(attribute));
                }
            }
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals("head")) {
                    head = Integer.parseInt(XML_IO.getElementValue(child));
                }
                if (child.getNodeName().equals("tail")) {
                    tail = Integer.parseInt(XML_IO.getElementValue(child));
                }
            }
        }
    }

    public long getHead() {
        return head;
    }

    public long getTail() {
        return tail;
    }

    public int getType() {
        return type;
    }
}
