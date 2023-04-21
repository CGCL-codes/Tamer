package org.jcvi.vics.web.gwt.common.client.ui.table;

import org.jcvi.vics.web.gwt.common.client.ui.table.paging.PaginatorTableController;

/**
 * Supports multi-select on a SortableTable by processing every click on a cell as a select request
 * (instead of the base implementation that toggles processing of clicks as select/unselect
 */
public class MultiSelectSortableTableController extends PaginatorTableController {

    public MultiSelectSortableTableController(SortableTable sortableTable) {
        super(sortableTable);
    }

    /**
     * Selects the given row, regardless of what's already been selected.
     */
    protected void onSelectRequest(int row, int col) {
        _table.selectRow(row);
    }
}
