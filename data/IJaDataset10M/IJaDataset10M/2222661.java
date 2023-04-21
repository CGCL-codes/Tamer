package net.sf.hippopotam.framework.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * <br>Author: Dmitry Ermakov dim_er@mail.ru
 * <br>Date: 29.08.2007
 */
public abstract class AbstractTreeModel implements TreeModel {

    protected DefaultMutableTreeNode rootNode;

    protected DefaultTreeModel internalModel;

    protected AbstractTreeModel() {
        super();
        rootNode = createRootNode();
        internalModel = new DefaultTreeModel(rootNode);
    }

    protected DefaultMutableTreeNode createRootNode() {
        return new RootNode();
    }

    public Object getRoot() {
        return internalModel.getRoot();
    }

    public Object getChild(Object parent, int index) {
        return internalModel.getChild(parent, index);
    }

    public int getChildCount(Object parent) {
        return internalModel.getChildCount(parent);
    }

    public boolean isLeaf(Object node) {
        return internalModel.isLeaf(node);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public int getIndexOfChild(Object parent, Object child) {
        return internalModel.getIndexOfChild(parent, child);
    }

    public void addTreeModelListener(TreeModelListener l) {
        internalModel.addTreeModelListener(l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        internalModel.removeTreeModelListener(l);
    }
}
