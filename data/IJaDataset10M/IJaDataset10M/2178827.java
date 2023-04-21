package org.intellij.trinkets.logFilter.impl;

import javax.swing.table.AbstractTableModel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

final class GroupMonitoringModel extends AbstractTableModel {

    private final String[] columnNames = new String[] { "Group", "Matched", "Monitor" };

    private final Class[] columnClasses = new Class[] { Integer.class, String.class, Boolean.class };

    private final List<GroupMonitoringConfiguration> rows = new ArrayList<GroupMonitoringConfiguration>(9);

    public GroupMonitoringModel() {
        for (int i = 0; i < 9; i++) {
            GroupMonitoringConfiguration rule = new GroupMonitoringConfiguration();
            rule.setGroup(i + 1);
            rule.setMonitor(false);
            rows.add(rule);
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rows.get(rowIndex).getGroup();
            case 1:
                return rows.get(rowIndex).getMatched();
            case 2:
                return rows.get(rowIndex).isMonitor();
        }
        throw new IllegalArgumentException(MessageFormat.format("rowIndex {0}, columnIndex = {1}", rowIndex, columnIndex));
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        while (rowIndex + 1 > rows.size()) {
            rows.add(new GroupMonitoringConfiguration());
        }
        switch(columnIndex) {
            case 0:
                rows.get(rowIndex).setGroup((Integer) aValue);
                fireTableDataChanged();
                return;
            case 1:
                rows.get(rowIndex).setMatched((String) aValue);
                fireTableDataChanged();
                return;
            case 2:
                rows.get(rowIndex).setMonitor((Boolean) aValue);
                fireTableDataChanged();
                return;
        }
        throw new IllegalArgumentException(MessageFormat.format("rowIndex {0}, columnIndex = {1}", rowIndex, columnIndex));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof GroupMonitoringModel) {
            GroupMonitoringModel that = (GroupMonitoringModel) o;
            return rows.equals(that.rows);
        }
        return false;
    }

    public int hashCode() {
        return rows.hashCode();
    }
}
