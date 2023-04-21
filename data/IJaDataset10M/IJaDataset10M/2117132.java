package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.table;

import org.apache.myfaces.trinidad.component.CollectionComponent;

/**
 * @version $Name:  $ ($Revision: 245 $) $Date: 2008-11-25 19:05:42 -0500 (Tue, 25 Nov 2008) $
 */
public final class RowData {

    public RowData(TableRenderingContext tContext) {
        CollectionComponent table = tContext.getCollectionComponent();
        _tableBase = table;
        _rowCount = TableUtils.getVisibleRowCount(table);
    }

    /**
   * gets the index of the current row, as an index in the currently visible
   * range.
   */
    public int getRangeIndex() {
        int first = _tableBase.getFirst() + 1;
        if (first <= 0) first = 1;
        return _tableBase.getRowIndex() - first + 1;
    }

    /**
   * @return the number of visible table rows
   */
    public int getVisibleRowCount() {
        return _rowCount;
    }

    /**
   * Return true if this is a special "empty" table with no rendered data rows.
   */
    public boolean isEmptyTable() {
        return getVisibleRowCount() <= 0;
    }

    /**
   * sets the rowHeader ID for the current row.
   */
    public void setCurrentRowHeaderID(String id) {
        _currRowHeaderID = id;
    }

    /**
   * gets the rowHeader ID for the current row. This ID must be part of the
   * headers attribute for each table cell on the current row.  */
    public String getCurrentRowHeaderID() {
        return _currRowHeaderID;
    }

    /**
   * gets the max row span for the current row.
   */
    public int getCurrentRowSpan() {
        return _currRowSpan;
    }

    /**
   * sets the max row span for the current row.
   * @param rowSpan use -1 to reset between rows.
   */
    public void setCurrentRowSpan(int rowSpan) {
        if (rowSpan < 0) {
            _currRowSpan = 1;
            _currSpanRow = 0;
        } else {
            if (rowSpan > _currRowSpan) _currRowSpan = rowSpan;
        }
    }

    /**
   * gets the current sub row index for the current row. This is useful only
   * if the current row has a rowSpan that is greater than one
   */
    public int getCurrentSubRow() {
        assert (_currSpanRow < _currRowSpan);
        return _currSpanRow;
    }

    /**
   * increments the current sub row index by one.
   */
    public void incCurrentSubRow() {
        _currSpanRow++;
    }

    private String _currRowHeaderID = null;

    private final int _rowCount;

    private int _currRowSpan = 1, _currSpanRow = 0;

    private final CollectionComponent _tableBase;
}
