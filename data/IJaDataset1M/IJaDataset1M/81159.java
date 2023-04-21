package linkChecker.model;

import java.util.ArrayList;

public class TreeNode {

    TreeNode parent;

    ArrayList<TreeNode> children;

    String url;

    public TreeNode(TreeNode parent, String url) {
        this.parent = parent;
        this.children = new ArrayList<TreeNode>();
        this.url = url;
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }

    public String getURL() {
        return this.url;
    }

    public ArrayList<TreeNode> getChildList() {
        return this.children;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void setURL(String url) {
        this.url = url;
    }
}
