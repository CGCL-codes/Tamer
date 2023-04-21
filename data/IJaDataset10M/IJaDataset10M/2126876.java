package edu.biik.gui;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import edu.biik.entities.TopAttackers;

public class TopAttackersTableModel extends AbstractTableModel {

    private ArrayList<TopAttackers> topAttackersList;

    private String[] columnNames = { "Rank", "IP Address", "Count", "# of Destinations" };

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return topAttackersList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return topAttackersList.get(rowIndex).getSourceIP();
            case 2:
                return topAttackersList.get(rowIndex).getAttackCount();
            case 3:
                return topAttackersList.get(rowIndex).getNumOfDests();
            default:
                return "";
        }
    }

    public ArrayList<TopAttackers> getTopAttackersList() {
        return topAttackersList;
    }

    public void setTopAttackersList(ArrayList<TopAttackers> topAttackersList) {
        this.topAttackersList = topAttackersList;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
