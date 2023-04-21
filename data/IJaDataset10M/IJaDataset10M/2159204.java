package com.gwtext.client.widgets.tree;

import com.google.gwt.core.client.JavaScriptObject;
import com.gwtext.client.core.JsObject;
import com.gwtext.client.widgets.tree.event.MultiSelectionModelListener;

/**
 * Selection model for multi selecting tree nodes.
 *
 * @author Sanjiv Jivan
 * @see com.gwtext.client.widgets.tree.DefaultSelectionModel
 */
public class MultiSelectionModel extends JsObject implements TreeSelectionModel {

    public MultiSelectionModel() {
        jsObj = create();
    }

    private native JavaScriptObject create();

    public MultiSelectionModel(JavaScriptObject jsObj) {
        super(jsObj);
    }

    public static MultiSelectionModel instance(JavaScriptObject jsObj) {
        return new MultiSelectionModel(jsObj);
    }

    public native void clearSelections();

    public native void clearSelections(boolean suppressEvent);

    public TreeNode[] getSelectedNodes() {
        JavaScriptObject nativeArray = getSelectedNodes(jsObj);
        return TreePanel.convertFromNativeTreeNodeArray(nativeArray);
    }

    private native JavaScriptObject getSelectedNodes(JavaScriptObject sm);

    public native boolean isSelected(TreeNode treeNode);

    public native void select(TreeNode treeNode);

    public native void select(TreeNode treeNode, boolean keepExisting);

    public native void selectNext();

    public native void selectPrevious();

    public native void unselect(TreeNode treeNode);

    public native void addSelectionModelListener(MultiSelectionModelListener listener);
}
