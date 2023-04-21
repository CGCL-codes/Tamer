package edu.mit.lcs.haystack.server.extensions.infoextraction.tagtree;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.NodeID;
import edu.mit.lcs.haystack.server.extensions.wrapperinduction.dom.ie.IEDOMElement;

/**
 * @author yks
 */
public class AugmentedTreeBuilder {

    /**
     * generates a replicate tree of the given
     * org.w3c.dom.Node compliant tree composed of AugmentedNode nodes
     * @param node
     * @return
     */
    public static void testNodeID(AugmentedNode node) {
        NodeID id = node.getNodeID();
        System.err.println(id.toString());
        NodeList children = node.getChildNodes();
        int len = children.getLength();
        for (int i = 0; i < len; i++) {
            AugmentedNode child = (AugmentedNode) children.item(i);
            testNodeID(child);
        }
    }

    public static AugmentedNode cloneTree(Node node) {
        String treeNodeName = node.getNodeName();
        AugmentedNode clone = new AugmentedNode(node.getNodeType(), treeNodeName);
        clone.setNodeValue(node.getNodeValue());
        clone.setTagName(treeNodeName);
        if (node.getNodeType() == Node.TEXT_NODE) {
            clone.setNodeValue(((IEDOMElement) node).getNodeText());
        }
        NodeList children = node.getChildNodes();
        int len = children.getLength();
        for (int i = 0; i < len; i++) {
            Node child = children.item(i);
            String nodeName = child.getNodeName();
            AugmentedNode twin;
            if (!nodeName.equalsIgnoreCase("STYLE") && !nodeName.equalsIgnoreCase("SCRIPT")) {
                twin = cloneTree(child);
            } else {
                twin = new AugmentedNode(Node.ELEMENT_NODE, nodeName);
            }
            twin.setParent(clone);
            twin.setSiblingNo(i);
            clone.appendChild(twin);
        }
        return clone;
    }
}
