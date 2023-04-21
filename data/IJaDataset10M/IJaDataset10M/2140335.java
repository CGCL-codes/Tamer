package org.whatsitcalled.wicket;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.data.DataView;

public class AutoOrderByBorder extends OrderByBorder {

    private String property;

    private SortableDataProvider dp;

    private DataView dv;

    private boolean ascending = false;

    public AutoOrderByBorder(String id, String property, SortableDataProvider dp, DataView dv) {
        super(id, property, dp);
        this.property = property;
        this.dp = dp;
        this.dv = dv;
    }

    public void onSortChanged() {
        ascending = !ascending;
        dp.setSort(property, ascending);
        dv.setCurrentPage(0);
    }
}
