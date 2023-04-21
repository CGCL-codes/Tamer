package uk.co.demon.ursus.dom.pmr;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

public class PMRNamedNodeMapImpl implements PMRNamedNodeMap {

    NamedNodeMap namedNodeMap;

    PMRNode contextNode;

    public PMRNamedNodeMapImpl(NamedNodeMap namedNodeMap, PMRNode contextNode) {
        this.namedNodeMap = namedNodeMap;
        this.contextNode = contextNode;
    }

    public Node removeNamedItemNS(String s, String t) {
        Node node = (Node) namedNodeMap.removeNamedItemNS(s, t);
        return PMRNodeImpl.getPMRNode(node, contextNode);
    }

    public int getLength() {
        return namedNodeMap.getLength();
    }

    public Node getNamedItemNS(String s, String t) {
        Node node = namedNodeMap.getNamedItemNS(s, t);
        return PMRNodeImpl.getPMRNode(node, contextNode);
    }

    public Node getNamedItem(String s) {
        Node node = namedNodeMap.getNamedItem(s);
        return PMRNodeImpl.getPMRNode(node, contextNode);
    }

    public Node setNamedItem(Node node) {
        Node node1 = namedNodeMap.setNamedItem(node);
        return PMRNodeImpl.getPMRNode(node1, contextNode);
    }

    public Node setNamedItemNS(Node node) {
        Node node1 = namedNodeMap.setNamedItemNS(node);
        return PMRNodeImpl.getPMRNode(node1, contextNode);
    }

    public Node item(int index) {
        Node node = namedNodeMap.item(index);
        return PMRNodeImpl.getPMRNode(node, contextNode);
    }

    public Node removeNamedItem(String item) {
        Node node = namedNodeMap.removeNamedItem(item);
        return PMRNodeImpl.getPMRNode(node, contextNode);
    }
}
