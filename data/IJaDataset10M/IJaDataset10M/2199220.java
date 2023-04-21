package org.rjam.alert.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.rjam.gui.base.BaseUiLogging;
import javax.swing.JComponent;
import javax.swing.tree.TreeNode;

public abstract class BaseTreeNode extends BaseUiLogging implements TreeNode {

    private List<TreeNode> kids = new ArrayList<TreeNode>();

    private TreeNode parent;

    public BaseTreeNode() {
    }

    public BaseTreeNode(TreeNode parent) {
        this.parent = parent;
    }

    public BaseTreeNode(TreeNode parent, List<TreeNode> kids) {
        this.parent = parent;
        this.kids = kids;
    }

    public void removeAllNodes() {
        kids = new ArrayList<TreeNode>();
    }

    public Enumeration<TreeNode> children() {
        return new Enumeration<TreeNode>() {

            private int pos = 0;

            public boolean hasMoreElements() {
                return pos < kids.size();
            }

            public TreeNode nextElement() {
                return kids.get(pos++);
            }
        };
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public TreeNode getChildAt(int arg0) {
        return kids.get(arg0);
    }

    public int getChildCount() {
        return kids.size();
    }

    public int getIndex(TreeNode arg0) {
        return kids.indexOf(arg0);
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return false;
    }

    public void addNode(TreeNode child) {
        kids.add(child);
    }

    public void addNode(int pos, TreeNode child) {
        kids.add(pos, child);
    }

    public boolean removeNode(TreeNode child) {
        return kids.remove(child);
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public abstract JComponent getDetailComponent();
}
