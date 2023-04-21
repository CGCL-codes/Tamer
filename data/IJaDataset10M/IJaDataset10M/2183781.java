package org.racsor.jmeter.flex.messaging.swing;

import org.racsor.jmeter.flex.messaging.swing.AbstractTreeModel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.swing.tree.TreePath;

public class DOMTreeModel extends AbstractTreeModel {

    private Node _root;

    public DOMTreeModel(Node root) {
        _root = root;
    }

    public Object getRoot() {
        return _root;
    }

    public int getChildCount(Object parent) {
        NodeList nodes = ((Node) parent).getChildNodes();
        return nodes.getLength();
    }

    public Object getChild(Object parent, int index) {
        NodeList nodes = ((Node) parent).getChildNodes();
        return nodes.item(index);
    }

    public boolean isLeaf(Object node) {
        return ((Node) node).getNodeType() != Node.ELEMENT_NODE;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }
}
