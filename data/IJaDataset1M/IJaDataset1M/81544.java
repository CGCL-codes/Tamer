package org.springframework.richclient.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class TreeSelectionListenerSupport implements TreeSelectionListener {

    private static final TreePath[] EMPTY_TREE_PATH_ARRAY = new TreePath[0];

    private int itemsSelected = 0;

    protected int getItemsSelected() {
        return itemsSelected;
    }

    public void valueChanged(TreeSelectionEvent e) {
        TreePath[] paths = e.getPaths();
        List addedPaths = new ArrayList();
        List removedPaths = new ArrayList();
        for (int i = 0; i < paths.length; i++) {
            if (e.isAddedPath(i)) {
                itemsSelected++;
                addedPaths.add(paths[i]);
            } else {
                itemsSelected--;
                removedPaths.add(paths[i]);
            }
        }
        if (itemsSelected == 1) {
            onSingleSelection(e.getNewLeadSelectionPath());
        } else if (itemsSelected == 0) {
            onNoSelection((TreePath[]) removedPaths.toArray(EMPTY_TREE_PATH_ARRAY));
        } else {
            onMultiSelection((TreePath[]) addedPaths.toArray(EMPTY_TREE_PATH_ARRAY), (TreePath[]) removedPaths.toArray(EMPTY_TREE_PATH_ARRAY));
        }
    }

    protected void onSingleSelection(TreePath selectedPath) {
    }

    protected void onNoSelection(TreePath[] removedPaths) {
        onNoSelection();
    }

    protected void onNoSelection() {
    }

    protected void onMultiSelection(TreePath[] addedPaths, TreePath[] removedPaths) {
        onMultiSelection(addedPaths);
    }

    protected void onMultiSelection(TreePath[] addedPaths) {
    }
}
