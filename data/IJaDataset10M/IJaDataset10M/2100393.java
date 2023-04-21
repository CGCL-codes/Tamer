package depth.tree.transform;

import java.util.ArrayList;
import java.util.List;
import depth.tree.TreeNode;

public class TreeNodeList {

    private List<TreeNode> list;

    private TreeNode parent;

    public TreeNodeList() {
        this.list = new ArrayList<TreeNode>();
    }

    public TreeNodeList(TreeNode node) {
        this();
        add(node);
    }

    public void add(TreeNode node) {
        boolean isParent = false;
        if (parent == null) isParent = true;
        add(node, isParent);
    }

    public void add(TreeNode node, boolean isParent) {
        if (node != null) {
            list.add(node);
            if (isParent) this.parent = node;
        }
    }

    public TreeNode getParent() {
        return parent;
    }

    public List<TreeNode> getElements() {
        return list;
    }

    public int getSize() {
        return list.size();
    }

    public void addAll(TreeNodeList childNodeList) {
        if (childNodeList != null) {
            for (TreeNode child : childNodeList.getElements()) {
                add(child);
            }
        }
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("TreeNodeList [");
        for (TreeNode child : list) {
            buffer.append(child.toString());
            buffer.append(", ");
        }
        buffer.append("]");
        return buffer.toString();
    }
}
