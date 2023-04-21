package com.tensegrity.palowebviewer.modules.ui.client.tree;

import com.tensegrity.palowebviewer.modules.widgets.client.tree.FilterTreeModel;
import com.tensegrity.palowebviewer.modules.widgets.client.tree.IObjectAcceptor;

public class DatabaseBrowserTreeModel extends FilterTreeModel {

    protected class NodeFilter implements IObjectAcceptor {

        public boolean accept(Object object) {
            boolean result = true;
            if (result && !isShowCubeDimensions()) result = !(object instanceof CubeDimensionsFolderNode);
            if (result && !isShowDatabaseDimensions()) result = !(object instanceof DimensionsFolderNode);
            result &= !(object instanceof SubsetElementFolder);
            return result;
        }
    }

    private boolean showCubeDimensions = false;

    private boolean showDatabaseDimensions = false;

    private final NodeFilter filter = new NodeFilter();

    public DatabaseBrowserTreeModel(PaloTreeModel model) {
        super(model);
        resetFiltering();
        this.setFilter(filter);
    }

    protected void resetFiltering() {
        boolean filtering = false;
        filtering |= !isShowCubeDimensions();
        filtering |= !isShowDatabaseDimensions();
        setFiltering(filtering);
    }

    public boolean isShowCubeDimensions() {
        return showCubeDimensions;
    }

    public boolean isShowDatabaseDimensions() {
        return showDatabaseDimensions;
    }

    public void setShowCubeDimensions(boolean value) {
        this.showCubeDimensions = value;
        resetFiltering();
    }

    public void setShowDimensions(boolean value) {
        this.showDatabaseDimensions = value;
        resetFiltering();
    }
}
