package net.sourceforge.jisocreator.model.isoexplorer.impl;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.jisocreator.model.isoexplorer.decl.ITreeNode;
import net.sourceforge.jisocreator.model.isoexplorer.decl.TreeNodeAdapter;

public class TreeNode extends TreeNodeAdapter {

    protected ITreeNode parent;

    protected List<ITreeNode> children;

    public TreeNode(ITreeNode parent) {
        this.parent = parent;
        children = new ArrayList<ITreeNode>();
    }

    @Override
    public Object[] getChildren() {
        return children.toArray();
    }

    @Override
    public ITreeNode getParent() {
        return parent;
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }
}
