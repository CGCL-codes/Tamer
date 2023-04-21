package net.sf.jmoney.gui;

import java.util.Collections;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class SortedTreeNode extends DefaultMutableTreeNode implements Comparable {

    private static final long serialVersionUID = -32238679169645975L;

    public SortedTreeNode() {
    }

    public SortedTreeNode(Object usrObj) {
        super(usrObj, true);
    }

    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
        super.insert(child, index);
        sortChildren();
    }

    public void sortChildren() {
        if (children == null) return;
        Collections.sort(children);
    }
}
