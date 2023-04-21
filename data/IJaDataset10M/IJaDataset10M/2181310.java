package au.com.uptick.serendipity.client.sales.view;

import com.smartgwt.client.widgets.grid.ListGrid;

public class SalesContextAreaListGrid extends ListGrid {

    public SalesContextAreaListGrid() {
        super();
        this.setShowAllRecords(true);
        this.setSortField(1);
        this.setShowFilterEditor(true);
    }
}
