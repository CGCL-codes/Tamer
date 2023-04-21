package net.ar.guia.own.implementation;

import java.util.*;
import net.ar.guia.helpers.*;
import net.ar.guia.own.interfaces.*;

public class DefaultTable implements Table {

    protected List columnNames = new Vector();

    protected List rows = new Vector();

    protected List header = new Vector();

    protected TableComponent tableComponent;

    public DefaultTable() {
    }

    public DefaultTable(TableComponent aTableComponent) {
        tableComponent = aTableComponent;
    }

    public void clear() {
        rows.clear();
    }

    public int getColumnCount() {
        if (rows.size() > 0) return ((List) rows.get(0)).size(); else return 0;
    }

    public VisualComponent getElementAt(int y, int x) {
        List rowAt = getRowAt(y);
        if (x >= rowAt.size()) return null; else return (VisualComponent) rowAt.get(x);
    }

    public VisualComponent getHeaderAt(int x) {
        GuiaHelper.fillList(x, header);
        Object result = header.get(x);
        return (VisualComponent) result;
    }

    public List getRowAt(int y) {
        GuiaHelper.fillList(y, rows);
        List row = (List) rows.get(y);
        if (row == null) rows.set(y, row = new Vector());
        return row;
    }

    public int getRowCount() {
        return rows.size();
    }

    public List getRows() {
        return rows;
    }

    public void setElementAt(int y, int x, VisualComponent aVisualComponent) {
        List rowAt = getRowAt(y);
        GuiaHelper.fillList(x, rowAt);
        rowAt.set(x, aVisualComponent);
        aVisualComponent.setParent(tableComponent);
    }

    public void setHeaderAt(int x, VisualComponent aVisualComponent) {
        header.add(x, aVisualComponent);
        aVisualComponent.setParent(tableComponent);
    }

    public void setRows(List aRows) {
        rows = aRows;
    }
}
