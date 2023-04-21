package com.gwtaf.ext.core.client.grid;

import com.gwtaf.ext.core.client.store.GroupingStoreEx;
import com.gwtext.client.core.TextAlign;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridPanel;

public class RowNumberColumnConfig extends ColumnConfig {

    public RowNumberColumnConfig(GroupingStoreEx<?> groupingStore) {
        super("", "", 23, false, new RowNumberRenderer(groupingStore), null);
        setAlign(TextAlign.RIGHT);
        setResizable(false);
        setId("numberer");
        setFixed(true);
    }

    public RowNumberColumnConfig(GridPanel gridPanel) {
        super("", "", 23, false, new RowNumberRenderer(gridPanel), null);
        setAlign(TextAlign.RIGHT);
        setResizable(false);
        setId("numberer");
        setFixed(true);
    }
}
