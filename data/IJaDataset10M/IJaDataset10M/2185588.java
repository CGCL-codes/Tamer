package org.bounce.viewer.xml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.bounce.QTree;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

/**
 * A tree representing DOM Nodes.
 *
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class DOMNodeTree extends QTree {

    private static final long serialVersionUID = 5210155085492412876L;

    /**
	 * Constructs a tree for a DOM node.
	 * 
	 * @param node the DOM node.
	 */
    public DOMNodeTree(Node node) {
        super(new DefaultTreeModel(new RootTreeNode(node)));
        putClientProperty("JTree.lineStyle", "None");
        setCellRenderer(new DOMNodeCellRenderer());
    }

    /**
     * The tree-path for the W3C DOM node.
     * 
     * @param node the w3c DOM Node
     * @return the tree-path.
     */
    public TreePath getPathForNode(Node node) {
        List<Node> path = new ArrayList<Node>();
        while (node != null) {
            path.add(0, node);
            if (node instanceof Attr) {
                node = ((Attr) node).getOwnerElement();
            } else {
                node = node.getParentNode();
            }
        }
        NodeTreeNode lastNode = (NodeTreeNode) getModel().getRoot();
        for (Node element : path) {
            NodeTreeNode treeNode = getNode(lastNode, element);
            if (treeNode != null) {
                lastNode = treeNode;
            }
        }
        return new TreePath(lastNode.getPath());
    }

    public void setRoot(Node node) {
        ((DefaultTreeModel) getModel()).setRoot(new RootTreeNode(node));
    }

    public void update() {
        ((RootTreeNode) getModel().getRoot()).format();
        ((DefaultTreeModel) getModel()).nodeStructureChanged((RootTreeNode) getModel().getRoot());
    }

    /**
	 * Sets the look and feel to the XML Tree UI look and feel.
	 * Override this method to install a different UI.
	 */
    public void updateUI() {
        setUI(DOMNodeTreeUI.createUI(this));
    }

    @SuppressWarnings("unchecked")
    private NodeTreeNode getNode(NodeTreeNode parent, Node child) {
        Enumeration<NodeTreeNode> children = parent.children();
        while (children.hasMoreElements()) {
            NodeTreeNode node = children.nextElement();
            if (node.getNode() == child) {
                return node;
            }
        }
        return null;
    }
}
