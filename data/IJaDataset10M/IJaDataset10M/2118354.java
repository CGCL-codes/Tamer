package websiteschema.analyzer.browser.bottom.domtree;

import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import java.util.List;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author ray
 */
public class DOMTreeModel implements TreeModel {

    private boolean showAncestors;

    private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();

    private DOMTreeNode rootVisionBlock;

    public DOMTreeModel(DOMTreeNode root) {
        showAncestors = false;
        rootVisionBlock = root;
    }

    /**
     * Used to toggle between show ancestors/show descendant and
     * to change the root of the tree.
     */
    public void showAncestor(boolean b, Object newRoot) {
        showAncestors = b;
        DOMTreeNode oldRoot = rootVisionBlock;
        if (newRoot != null) {
            rootVisionBlock = (DOMTreeNode) newRoot;
        }
        fireTreeStructureChanged(oldRoot);
    }

    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void fireTreeStructureChanged(DOMTreeNode oldRoot) {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, new Object[] { oldRoot });
        for (int i = 0; i < len; i++) {
            ((TreeModelListener) treeModelListeners.elementAt(i)).treeStructureChanged(e);
        }
    }

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    @Override
    public void addTreeModelListener(TreeModelListener l) {
        treeModelListeners.addElement(l);
    }

    /**
     * Returns the child of parent at index index in the parent's child array.
     */
    @Override
    public Object getChild(Object parent, int index) {
        DOMTreeNode p = (DOMTreeNode) parent;
        if (showAncestors) {
            return p.getParent();
        }
        return p.getChildAt(index);
    }

    /**
     * Returns the number of children of parent.
     */
    @Override
    public int getChildCount(Object parent) {
        DOMTreeNode p = (DOMTreeNode) parent;
        if (showAncestors) {
            int count = 0;
            if (p.getParent() != null) {
                count++;
            }
            return count;
        }
        return p.getChildLength();
    }

    /**
     * Returns the index of child in parent.
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        DOMTreeNode p = (DOMTreeNode) parent;
        if (showAncestors) {
            int count = 0;
            DOMTreeNode father = p.getParent();
            if (father != null) {
                count++;
                if (father == child) {
                    return 0;
                }
            }
            return -1;
        }
        List<DOMTreeNode> children = p.getChildren();
        if (null != children) {
            for (int i = 0; i < children.size(); i++) {
                DOMTreeNode c = children.get(i);
                if (c.equals((DOMTreeNode) child)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Returns the root of the tree.
     */
    @Override
    public Object getRoot() {
        return rootVisionBlock;
    }

    /**
     * Returns true if node is a leaf.
     */
    @Override
    public boolean isLeaf(Object node) {
        DOMTreeNode p = (DOMTreeNode) node;
        if (showAncestors) {
            return ((p.getParent() == null));
        }
        return p.getChildLength() == 0;
    }

    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        treeModelListeners.removeElement(l);
    }

    /**
     * Messaged when the user has altered the value for the item
     * identified by path to newValue.  Not used by this model.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : " + path + " --> " + newValue);
    }
}
