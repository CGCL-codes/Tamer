package com.gwtext.sample.showcase2.client.grid;

import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GroupingView;
import com.gwtext.sample.showcase2.client.SampleData;
import com.gwtext.sample.showcase2.client.SampleGrid;
import com.gwtext.sample.showcase2.client.ShowcasePanel;

public class GridGroupingSample extends ShowcasePanel {

    public String getSourceUrl() {
        return "source/grid/GridGroupingSample.java.html";
    }

    public Panel getViewPanel() {
        if (panel == null) {
            MemoryProxy proxy = new MemoryProxy(SampleData.getCompanyDataLarge());
            RecordDef recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("company"), new FloatFieldDef("price"), new FloatFieldDef("change"), new FloatFieldDef("pctChange"), new DateFieldDef("lastChanged", "n/j h:ia"), new StringFieldDef("symbol"), new StringFieldDef("industry") });
            ArrayReader reader = new ArrayReader(recordDef);
            GroupingStore store = new GroupingStore();
            store.setReader(reader);
            store.setDataProxy(proxy);
            store.setSortInfo(new SortState("company", SortDir.ASC));
            store.setGroupField("industry");
            store.load();
            GroupingView gridView = new GroupingView();
            gridView.setForceFit(true);
            gridView.setGroupTextTpl("{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"Items\" : \"Item\"]})");
            GridPanel grid = new SampleGrid();
            grid.setStore(store);
            grid.setView(gridView);
            grid.setFrame(true);
            grid.setWidth(620);
            grid.setHeight(400);
            grid.setCollapsible(true);
            grid.setAnimCollapse(false);
            grid.setTitle("Grouping Example");
            grid.setIconCls("grid-icon");
            panel = new Panel();
            panel.add(grid);
        }
        return panel;
    }

    public String getIntro() {
        return "This is an example of a Grouping Grid where data is grouped on a certain data column of the underlying Store. " + "In this example the grouping is done on Industry type.";
    }
}
