package pspdash;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.event.TreeModelListener;

public class PropTreeModel extends DefaultTreeModel {

    TreeModelListener listener;

    public PropTreeModel(DefaultMutableTreeNode root, TreeModelListener l) {
        super(root);
        listener = l;
    }

    public void useTreeModelListener(boolean listen) {
        if (listen) addTreeModelListener(listener); else removeTreeModelListener(listener);
    }

    private void fill(DefaultMutableTreeNode parent, PSPProperties props, PropertyKey key) {
        int numChildren, i;
        DefaultMutableTreeNode child;
        numChildren = props.getNumChildren(key);
        for (i = 0; i < numChildren; i++) {
            child = new DefaultMutableTreeNode(props.getChildName(key, i));
            insertNodeInto(child, parent, i);
            fill(child, props, props.getChildKey(key, i));
        }
    }

    public void fill(PSPProperties props) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        fill(root, props, PropertyKey.ROOT);
    }

    public void reload(PSPProperties props, DefaultMutableTreeNode node, PropertyKey key) {
        DefaultMutableTreeNode child;
        int childIndex = 0;
        Prop value = props.pget(key);
        node.setUserObject(key.name());
        int numPropChildren = value.getNumChildren();
        while (numPropChildren > childIndex) {
            if (getChildCount(node) <= childIndex) {
                child = new DefaultMutableTreeNode("changeMeLater");
                insertNodeInto(child, node, childIndex);
            } else child = (DefaultMutableTreeNode) getChild(node, childIndex);
            reload(props, child, props.getChildKey(key, childIndex));
            childIndex++;
        }
        while (getChildCount(node) > numPropChildren) removeNodeFromParent((MutableTreeNode) getChild(node, getChildCount(node) - 1));
    }

    public void reload(PSPProperties props) {
        reload(props, (DefaultMutableTreeNode) getRoot(), PropertyKey.ROOT);
    }

    public PropertyKey getPropKey(PSPProperties props, Object[] path) {
        PropertyKey key = PropertyKey.ROOT;
        if (path != null) for (int i = 1; i < path.length; i++) {
            int index = getIndexOfChild(path[i - 1], path[i]);
            key = props.getChildKey(key, index);
        }
        return key;
    }

    public TreeNode getNodeForKey(PSPProperties props, PropertyKey key) {
        if (PropertyKey.ROOT.equals(key)) return (TreeNode) getRoot();
        PropertyKey parentKey = key.getParent();
        TreeNode parent = getNodeForKey(props, parentKey);
        PropertyKey childKey;
        for (int i = props.getNumChildren(parentKey); i-- > 0; ) {
            childKey = props.getChildKey(parentKey, i);
            if (childKey.equals(key)) return parent.getChildAt(i);
        }
        return null;
    }

    public Object[] getPathToKey(PSPProperties props, PropertyKey key) {
        return getPathToRoot(getNodeForKey(props, key));
    }
}
