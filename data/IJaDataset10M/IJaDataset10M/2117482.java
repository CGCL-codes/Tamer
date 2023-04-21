package com.tensegrity.palowebviewer.modules.widgets.client.tree;

import com.tensegrity.palowebviewer.modules.widgets.client.list.AbstractListModel;

public class ListModelAdapter extends AbstractListModel {

    private Object parent;

    private ITreeModel treeModel;

    private final ITreeModelListener treeListener = new ITreeModelListener() {

        public void treeNodesChanged(TreeModelEvent e) {
            checkEvent(e.getPath());
        }

        private void checkEvent(Object[] path) {
            if (path == null || path.length == 0) {
                fireRebuild();
            } else if (path[path.length - 1] == parent) {
                fireRebuild();
            }
        }

        public void treeNodesInserted(TreeModelEvent e) {
            checkEvent(e.getPath());
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            checkEvent(e.getPath());
        }

        public void treeStructureChanged(TreeModelEvent e) {
            checkEvent(e.getPath());
        }
    };

    private void fireRebuild() {
        listeners.fireModelChanged();
    }

    public ListModelAdapter() {
    }

    public void setTreeModel(ITreeModel treeModel) {
        if (this.treeModel != null) {
            this.treeModel.removeTreeModelListener(treeListener);
        }
        this.treeModel = treeModel;
        if (this.treeModel != null) {
            this.treeModel.addTreeModelListener(treeListener);
        }
        fireRebuild();
    }

    public void setParent(Object parent) {
        this.parent = parent;
        fireRebuild();
    }

    public boolean contains(Object item) {
        boolean result = indexOf(item) >= 0;
        return result;
    }

    public Object getElementAt(int index) {
        Object result = null;
        if (treeModel != null) {
            result = treeModel.getChild(parent, index);
        } else {
            throw new IndexOutOfBoundsException("list is empty");
        }
        return result;
    }

    public int getSize() {
        int result = 0;
        if (treeModel != null) {
            result = treeModel.getChildCount(parent);
        }
        return result;
    }

    public int indexOf(Object item) {
        int result = -1;
        if (treeModel != null) {
            result = treeModel.getIndexOfChild(parent, item);
        }
        return result;
    }

    public void removeItems(int index0, int index1) {
        throw new RuntimeException("this is adapter list");
    }
}
