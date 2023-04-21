package com.ploed.statsviewer;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * User: mikeploed
 * Date: 21.12.2007
 * Time: 23:10:07
 */
public abstract class StatsviewerPanel extends Panel {

    public StatsviewerPanel(String id) {
        super(id);
    }

    /**
     * Convenience Method for creating sortable columns
     *
     * @param dp       Sortable Data Provider
     * @param dataView The DataView
     * @param order    Ordering
     * @return Wicket Markup for an order by border
     */
    protected OrderByBorder createOrderBy(SortableDataProvider dp, final DataView dataView, String order) {
        return new OrderByBorder(order, order, dp) {

            protected void onSortChanged() {
                dataView.setCurrentPage(0);
            }
        };
    }
}
