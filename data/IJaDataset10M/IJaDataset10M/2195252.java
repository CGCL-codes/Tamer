package vehikel.codeconverter;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.antlr.runtime.tree.Tree;

public class JTreeVehikelASTModel implements TreeModel {

    Tree root = null;

    public JTreeVehikelASTModel(Tree t) {
        if (t == null) {
            throw new IllegalArgumentException("root is null");
        }
        root = t;
    }

    public void addTreeModelListener(TreeModelListener l) {
    }

    public Object getChild(Object parent, int index) {
        if (parent == null) {
            return null;
        }
        Tree p = (Tree) parent;
        Tree c = p.getChild(index);
        if (c == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        return c;
    }

    public int getChildCount(Object parent) {
        if (parent == null) {
            throw new IllegalArgumentException("root is null");
        }
        return ((Tree) parent).getChildCount();
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            throw new IllegalArgumentException("root or child is null");
        }
        Tree p = (Tree) parent;
        Tree c = p.getChild(0);
        if (c == null) {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }
        int i = 1;
        while (i < p.getChildCount() && c != null && c != child) {
            i++;
            c = p.getChild(i);
        }
        if (c == child) {
            return i;
        }
        throw new java.util.NoSuchElementException("node is not a child");
    }

    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        if (node == null) {
            throw new IllegalArgumentException("node is null");
        }
        Tree t = (Tree) node;
        return t.getChild(0) == null;
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        System.err.println("heh, who is calling this mystery method?");
    }
}
