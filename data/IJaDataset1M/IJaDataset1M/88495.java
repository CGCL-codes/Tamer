package com.nexirius.framework.dataviewer;

public interface DataModelTreeViewerListener {

    public void treeSelectionChanged(DataModelTreeViewer treeViewer, DataModelTreeComponent component);

    public void treeFoldingChanged(DataModelTreeViewer treeViewer, DataModelTreeComponent component, boolean recursive);
}
