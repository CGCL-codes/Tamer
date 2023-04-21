package jkad.structures.tree;

import java.util.List;

public class TreeNodeImp implements SimpleTreeNode<TreeNodeImp> {

    private static Class<? extends List> childrenListClass = java.util.ArrayList.class;

    private TreeNodeImp parent;

    private List<TreeNodeImp> children;

    public static void setChildrenListClass(Class<? extends List> listClass) {
        childrenListClass = listClass;
    }

    public static Class<? extends List> getChildrenListClass() {
        return childrenListClass;
    }

    public boolean addChild(TreeNodeImp node) {
        if (node != null) {
            node.setParent(this);
            getBuiltChildrenList().add(node);
            return true;
        } else {
            return false;
        }
    }

    public boolean addChild(int position, TreeNodeImp node) {
        if (node != null) {
            node.setParent(this);
            getBuiltChildrenList().add(position, node);
            return true;
        } else {
            return false;
        }
    }

    public boolean addChildren(List<TreeNodeImp> nodes) {
        if (nodes != null && nodes.size() > 0) {
            adjustChildren(this, nodes);
            getBuiltChildrenList().addAll(nodes);
            return true;
        } else {
            return false;
        }
    }

    public TreeNodeImp getChild(int position) {
        return this.children.get(position);
    }

    public List<TreeNodeImp> getChildren() {
        return this.children;
    }

    public TreeNodeImp getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return (children == null || children.size() == 0);
    }

    public boolean isRoot() {
        return (getParent() == null);
    }

    public boolean removeChild(TreeNodeImp node) {
        return children.remove(node);
    }

    public TreeNodeImp removeChild(int position) {
        return children.remove(position);
    }

    public List<TreeNodeImp> removeChildren() {
        adjustChildren(null, children);
        List<TreeNodeImp> result = children;
        children = null;
        return result;
    }

    public void setChildren(List<TreeNodeImp> nodes) {
        if (nodes != null) {
            getBuiltChildrenList().addAll(nodes);
            adjustChildren(this, children);
        } else {
            children = null;
        }
    }

    public void setParent(TreeNodeImp node) {
        this.parent = node;
    }

    @SuppressWarnings("unchecked")
    protected List<TreeNodeImp> getBuiltChildrenList() {
        if (this.children == null) {
            try {
                this.children = (List<TreeNodeImp>) childrenListClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this.children;
    }

    protected void adjustChildren(TreeNodeImp node, List<TreeNodeImp> children) {
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                children.get(i).setParent(node);
            }
        }
    }
}
