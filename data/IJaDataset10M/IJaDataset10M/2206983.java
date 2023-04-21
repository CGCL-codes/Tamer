package net.sf.echopm.panel.editor.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import net.sf.echopm.navigation.event.EventDispatcher;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.event.ActionEvent;
import echopointng.tree.DefaultMutableTreeNode;
import echopointng.tree.MutableTreeNode;
import echopointng.tree.TreeNode;

/**
 * @author ron
 */
public abstract class EditorTreeNodeAbstractDecorator implements EditorTreeNode {

    static final long serialVersionUID = 0L;

    private final MutableTreeNode decoratedNode;

    /**
	 * @param treeNode
	 * @param tree
	 */
    public EditorTreeNodeAbstractDecorator(MutableTreeNode treeNode, EditorTree tree) {
        this.decoratedNode = treeNode;
    }

    public void insert(MutableTreeNode child, int index) {
        this.children();
        decoratedNode.insert(child, index);
    }

    public void remove(int index) {
        decoratedNode.remove(index);
    }

    public void remove(MutableTreeNode node) {
        decoratedNode.remove(node);
    }

    public void removeFromParent() {
        MutableTreeNode parent = (MutableTreeNode) getParent();
        if (parent != null) {
            parent.remove(this);
        }
    }

    public void setParent(MutableTreeNode newParent) {
        decoratedNode.setParent(newParent);
    }

    public Object getUserObject() {
        if (decoratedNode instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) decoratedNode).getUserObject();
        }
        throw new RuntimeException("stupid tree implementation.");
    }

    public void setUserObject(Object object) {
        decoratedNode.setUserObject(object);
    }

    public Enumeration children() {
        return decoratedNode.children();
    }

    public String getActionCommand() {
        return decoratedNode.getActionCommand();
    }

    public boolean getAllowsChildren() {
        return decoratedNode.getAllowsChildren();
    }

    public TreeNode getChildAt(int childIndex) {
        return decoratedNode.getChildAt(childIndex);
    }

    public int getChildCount() {
        return decoratedNode.getChildCount();
    }

    public int getIndex(TreeNode node) {
        return decoratedNode.getIndex(node);
    }

    public TreeNode getParent() {
        return decoratedNode.getParent();
    }

    public boolean isLeaf() {
        return decoratedNode.isLeaf();
    }

    public void actionPerformed(ActionEvent e) {
        if (decoratedNode instanceof EditorTreeNode) {
            ((EditorTreeNode) decoratedNode).actionPerformed(e);
        }
    }

    public void dispose() {
        if (decoratedNode instanceof EditorTreeNode) {
            ((EditorTreeNode) decoratedNode).dispose();
        }
    }

    public int getDepth() {
        if (decoratedNode instanceof EditorTreeNode) {
            return ((EditorTreeNode) decoratedNode).getDepth();
        } else if (decoratedNode instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) decoratedNode).getDepth();
        } else if (decoratedNode.getChildCount() == 0) {
            return 0;
        } else {
            throw new RuntimeException("getDepth isn't implemented for general TreeNode.");
        }
    }

    public Component getEditor() {
        if (decoratedNode instanceof EditorTreeNode) {
            return ((EditorTreeNode) decoratedNode).getEditor();
        }
        return null;
    }

    public EventDispatcher getEventDispatcher() {
        if (decoratedNode instanceof EditorTreeNode) {
            return ((EditorTreeNode) decoratedNode).getEventDispatcher();
        }
        return null;
    }

    public TreeNode[] getPath() {
        if (decoratedNode instanceof EditorTreeNode) {
            return ((EditorTreeNode) decoratedNode).getPath();
        } else if (decoratedNode instanceof DefaultMutableTreeNode) {
            return ((DefaultMutableTreeNode) decoratedNode).getPath();
        } else {
            ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>(10);
            TreeNode parent = decoratedNode;
            while (parent != null) {
                nodeList.add(parent);
                parent = parent.getParent();
            }
            ArrayList<TreeNode> reverseNodes = new ArrayList<TreeNode>(nodeList.size());
            for (int i = nodeList.size() - 1; i >= 0; i--) {
                reverseNodes.add(nodeList.get(i));
            }
            return reverseNodes.toArray(new TreeNode[nodeList.size()]);
        }
    }

    public void add(MutableTreeNode newChild) {
        if (decoratedNode instanceof EditorTreeNode) {
            ((EditorTreeNode) decoratedNode).add(newChild);
        } else if (decoratedNode instanceof DefaultMutableTreeNode) {
            ((DefaultMutableTreeNode) decoratedNode).add(newChild);
        } else {
            throw new RuntimeException("The decorated node " + decoratedNode + " doesn't support add()");
        }
    }

    public void setEditor(Component editor) {
        if (decoratedNode instanceof EditorTreeNode) {
            ((EditorTreeNode) decoratedNode).setEditor(editor);
        }
    }

    public void setUpdateListener(EditorTreeNodeUpdateListener updateListener) {
        if (decoratedNode instanceof EditorTreeNode) {
            ((EditorTreeNode) decoratedNode).setUpdateListener(updateListener);
        }
    }

    public MutableTreeNode getDecoratedNode() {
        return decoratedNode;
    }

    /**
	 * @return The node with decorations
	 */
    public Component getRenderComponent() {
        if (decoratedNode instanceof EditorTreeNodeAbstractDecorator) {
            return ((EditorTreeNodeAbstractDecorator) decoratedNode).getRenderComponent();
        }
        return getEditor();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        while (obj instanceof EditorTreeNodeAbstractDecorator) {
            obj = ((EditorTreeNodeAbstractDecorator) obj).getDecoratedNode();
        }
        Object thisNode = getDecoratedNode();
        while (thisNode instanceof EditorTreeNodeAbstractDecorator) {
            thisNode = ((EditorTreeNodeAbstractDecorator) thisNode).getDecoratedNode();
        }
        return thisNode.equals(obj);
    }

    @Override
    public int hashCode() {
        return decoratedNode.hashCode();
    }
}
