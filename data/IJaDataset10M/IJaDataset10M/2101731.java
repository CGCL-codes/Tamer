package com.bap.ele.workbench.ui.views.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author zhangxinchao
 * @date Apr 10, 2009
 */
public class CourseTreeNode implements IContentNode {

    private String name;

    private IContentNode parent;

    private List<IContentNode> children;

    public CourseTreeNode(String name) {
        this.name = name;
        children = new LinkedList<IContentNode>();
    }

    public void addChild(IContentNode child) {
        System.out.println("--node=" + child.getName());
        children.add(child);
    }

    public IContentNode[] getChildren() {
        return (IContentNode[]) children.toArray(new IContentNode[children.size()]);
    }

    public String getName() {
        return name;
    }

    public IContentNode getParent() {
        return this.parent;
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public void removeChild(IContentNode child) {
        this.children.remove(child);
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    public void setParent(IContentNode parent) {
        this.parent = parent;
    }

    public String toString() {
        return name;
    }

    public boolean isLeaf() {
        return false;
    }

    public String getType() {
        return null;
    }
}
