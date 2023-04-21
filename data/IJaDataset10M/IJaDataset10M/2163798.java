package com.gm.common.utils.tree.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @function 树模型
 * @author Azheng
 * Mar 26, 2009 7:30:21 AM
 */
public class Tree {

    private String id;

    private String text;

    private boolean leaf;

    private List<TreeNode> children;

    public void addChildren(TreeNode extTreeNode) {
        if (this.children == null) {
            children = new ArrayList<TreeNode>();
        }
        this.children.add(extTreeNode);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
