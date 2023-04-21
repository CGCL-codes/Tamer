package org.plazmaforge.studio.reportdesigner.model.table;

import java.util.List;
import org.plazmaforge.studio.reportdesigner.model.Band;

public class TableBand extends Band implements ITable {

    private ITableBody tableBody;

    public ITableBody getTableBody() {
        if (tableBody == null) {
            tableBody = new TableBody(this);
        }
        return tableBody;
    }

    public List<ITableColumn> getColumns() {
        return getTableHeader().getColumns();
    }

    public void addColumn(ITableColumn column) {
        getTableHeader().addColumn(column);
    }

    public void removeColumn(ITableColumn column) {
        getTableHeader().removeColumn(column);
    }

    public List<ITableRow> getRows() {
        return getTableBody().getRows();
    }

    public List<ITableCell> getCells() {
        return getTableBody().getCells();
    }

    public void addRow(ITableRow row) {
        getTableBody().addRow(row);
    }

    public void removeRow(ITableRow row) {
        getTableBody().removeRow(row);
    }

    public void addCell(ITableCell cell) {
        getTableBody().addCell(cell);
    }

    public void removeCell(ITableCell cell) {
        getTableBody().removeCell(cell);
    }

    public TableReportContent getTableReportContent() {
        return (TableReportContent) getReportContent();
    }

    public ITableHeader getTableHeader() {
        return (ITableHeader) getTableReportContent();
    }

    public boolean isEmptyColumns() {
        return getTableHeader().isEmptyColumns();
    }

    public boolean isLastColumn(ITableColumn column) {
        return getTableHeader().isLastColumn(column);
    }

    public int indexOfColumn(ITableColumn column) {
        return getTableHeader().indexOfColumn(column);
    }

    public boolean isEmptyRows() {
        return getTableBody().isEmptyRows();
    }

    public boolean isLastRow(ITableRow row) {
        return getTableBody().isLastRow(row);
    }

    public int indexOfRow(ITableRow row) {
        return getTableBody().indexOfRow(row);
    }

    public int getColumnCount() {
        return getTableHeader().getColumnCount();
    }

    public int getRowCount() {
        return getTableBody().getRowCount();
    }

    public void removeAllColumns() {
        getTableHeader().removeAllColumns();
    }

    public void removeAllRows() {
        getTableBody().removeAllRows();
    }

    public void removeAllCells() {
        getTableBody().removeAllCells();
    }
}
