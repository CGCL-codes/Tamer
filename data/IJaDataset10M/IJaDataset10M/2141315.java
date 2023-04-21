package com.greentea.relaxation.jnmf.gui.components.project.data.filtration;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 21.03.2009 Time: 17:13:34 To change this template
 * use File | Settings | File Templates.
 */
public class FiltrationTableCellRenderer extends DefaultTableCellRenderer {

    private Color notSelectdCellColor;

    private Color selectedCellColor;

    private Set<Integer> selectedRowsViewIndexes = new TreeSet<Integer>();

    private Set<Integer> selectedColumns = new TreeSet<Integer>();

    public FiltrationTableCellRenderer(Color notSelectdCellColor, Color selectedCellColor) {
        this.notSelectdCellColor = notSelectdCellColor;
        this.selectedCellColor = selectedCellColor;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            Color backgroundColor = notSelectdCellColor;
            if (selectedColumns.contains(column) || selectedRowsViewIndexes.contains(row)) {
                backgroundColor = selectedCellColor;
            }
            component.setBackground(backgroundColor);
        }
        return component;
    }

    public void addSelectedColumns(Iterable<Integer> indexes) {
        for (int index : indexes) {
            addSelectedColumn(index);
        }
    }

    public void removeSelectedColumns(Iterable<Integer> indexes) {
        for (int index : indexes) {
            removeSelectedColumn(index);
        }
    }

    public void addSelectedRows(Iterable<Integer> indexes) {
        for (int index : indexes) {
            addSelectedRow(index);
        }
    }

    public void removeSelectedRows(Iterable<Integer> indexes) {
        for (int index : indexes) {
            removeSelectedRow(index);
        }
    }

    public void addSelectedColumns(int[] indexes) {
        for (int index : indexes) {
            addSelectedColumn(index);
        }
    }

    public void removeSelectedColumns(int[] indexes) {
        for (int index : indexes) {
            removeSelectedColumn(index);
        }
    }

    public void addSelectedRows(int[] indexes) {
        for (int index : indexes) {
            addSelectedRow(index);
        }
    }

    public void removeSelectedRows(int[] indexes) {
        for (int index : indexes) {
            removeSelectedRow(index);
        }
    }

    public void addSelectedColumn(int index) {
        if (!selectedColumns.contains(index)) {
            selectedColumns.add(index);
        }
    }

    public void removeSelectedColumn(int index) {
        if (selectedColumns.contains(index)) {
            selectedColumns.remove((Object) index);
        }
    }

    public void addSelectedRow(int index) {
        if (!selectedRowsViewIndexes.contains(index)) {
            selectedRowsViewIndexes.add(index);
        }
    }

    public void removeSelectedRow(int index) {
        if (selectedRowsViewIndexes.contains(index)) {
            selectedRowsViewIndexes.remove((Object) index);
        }
    }

    public Color getNotSelectdCellColor() {
        return notSelectdCellColor;
    }

    public void setNotSelectdCellColor(Color notSelectdCellColor) {
        this.notSelectdCellColor = notSelectdCellColor;
    }

    public Color getSelectedCellColor() {
        return selectedCellColor;
    }

    public void setSelectedCellColor(Color selectedCellColor) {
        this.selectedCellColor = selectedCellColor;
    }

    public Set<Integer> getSelectedRowsViewIndexes() {
        return selectedRowsViewIndexes;
    }

    public void setSelectedRowsViewIndexes(Set<Integer> selectedRowsViewIndexes) {
        this.selectedRowsViewIndexes = selectedRowsViewIndexes;
    }

    public Set<Integer> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(Set<Integer> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }
}
