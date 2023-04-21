package br.org.databasetools.core.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableDataRow {

    private List<TableDataColumn> columns = new ArrayList<TableDataColumn>();

    public List<TableDataColumn> getColumns() {
        return columns;
    }

    public void add(TableDataColumn column) {
        this.columns.add(column);
    }

    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Iterator i = columns.iterator(); i.hasNext(); ) {
            TableDataColumn tdc = (TableDataColumn) i.next();
            sb.append("[");
            sb.append(tdc.getValue());
            sb.append("]");
        }
        return sb.toString();
    }
}
