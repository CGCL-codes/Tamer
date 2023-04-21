package org.apache.xerces.xpath2.functions;

import java.util.Iterator;
import java.util.List;
import org.apache.xerces.xpath2.tree.XDMNode;
import org.apache.xerces.xpath2.tree.XPathConstants;

public class NodeFunctions {

    public static String getName(XDMNode node) {
        return node.getQName();
    }

    public static String getLocalName(XDMNode node) {
        return node.getLocalName();
    }

    public static String getNamespaceUri(XDMNode node) {
        String namespaceUri = "";
        if (node.getNodeType() == XPathConstants.ELEMENT) {
            namespaceUri = namespaceUriHelper(node);
        } else if (node.getNodeType() == XPathConstants.ATTRIBUTE) {
            XDMNode elemNode = node.getParent();
            namespaceUri = namespaceUriHelper(elemNode);
        }
        return namespaceUri;
    }

    public static XDMNode getRoot(XDMNode node) {
        XDMNode parent = node.getParent();
        if (parent == null) {
            return node;
        } else {
            return getRoot(parent);
        }
    }

    private static String namespaceUriHelper(XDMNode node) {
        String namespaceUri = "";
        String qName = node.getQName();
        String prefix = "";
        int cIndex = qName.indexOf(":");
        if (cIndex != -1) {
            prefix = qName.substring(0, cIndex);
        }
        List namespaceNodes = node.getNamespaces();
        for (Iterator iter = namespaceNodes.iterator(); iter.hasNext(); ) {
            XDMNode nsNode = (XDMNode) iter.next();
            if (nsNode.getLocalName().equals(prefix)) {
                namespaceUri = nsNode.getStrValue();
                break;
            }
        }
        return namespaceUri;
    }
}
