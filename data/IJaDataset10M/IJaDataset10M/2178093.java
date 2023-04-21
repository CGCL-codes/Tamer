package com.umc.gui;

import com.umc.beans.Dir;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class ScanTableModel extends AbstractTableModel {

    private List<Dir> dirs = new ArrayList<Dir>();

    public ScanTableModel(List<Dir> dirs) {
        this.dirs = dirs;
    }

    public ScanTableModel() {
        this.dirs = new ArrayList<Dir>();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return dirs.size();
    }

    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "PC Pfad";
            case 1:
                return "NMT Pfad";
            default:
                return "";
        }
    }

    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                return dirs.get(row).getPC_Dir();
            case 1:
                return dirs.get(row).getPCH_Dir();
            default:
                return "";
        }
    }

    public Dir getDir(int row) {
        return dirs.get(row);
    }

    public List<Dir> getAllDirs() {
        return this.dirs;
    }

    public Class getColumnClass(int col) {
        switch(col) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            default:
                return String.class;
        }
    }
}

;
