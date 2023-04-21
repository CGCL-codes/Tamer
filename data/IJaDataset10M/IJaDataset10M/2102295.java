package org.tockit.crepe.gui.treeviews;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeNode;
import org.tockit.cgs.model.Type;

public class TypeHierachyTreeNode implements TreeNode {

    private Type type;

    private TreeNode parent;

    private TypeHierachyTreeNode[] subtypeNodes;

    public TypeHierachyTreeNode(Type type, TreeNode parent) {
        super();
        this.type = type;
        this.parent = parent;
        Type[] subtypes = type.getDirectSubtypes();
        this.subtypeNodes = new TypeHierachyTreeNode[subtypes.length];
        for (int i = 0; i < subtypes.length; i++) {
            Type subtype = subtypes[i];
            this.subtypeNodes[i] = new TypeHierachyTreeNode(subtype, this);
        }
    }

    public TreeNode getChildAt(int childIndex) {
        return this.subtypeNodes[childIndex];
    }

    public int getChildCount() {
        return this.subtypeNodes.length;
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public int getIndex(TreeNode node) {
        for (int i = 0; i < subtypeNodes.length; i++) {
            TypeHierachyTreeNode subtypeNode = subtypeNodes[i];
            if (subtypeNode == node) {
                return i;
            }
        }
        return -1;
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return subtypeNodes.length == 0;
    }

    public Enumeration children() {
        Vector temp = new Vector();
        temp.copyInto(this.subtypeNodes);
        return temp.elements();
    }

    public String toString() {
        return this.type.getName();
    }

    public Type getType() {
        return type;
    }
}
