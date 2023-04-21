package de.volkerraum.pokerbot.tournamentengine.ui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import de.volkerraum.pokerbot.tableengine.TableStatistics;
import de.volkerraum.pokerbot.tournamentengine.TournamentEngine;

/** Address list table */
public class TableStatsTableModel extends DefaultTableModel {

    private String[] theTableStatsColumns;

    private int[] theColumnWidths;

    TournamentEngine engine;

    public TableStatsTableModel(TournamentEngine engine) {
        this.engine = engine;
        setColnames();
    }

    private void setColnames() {
        theTableStatsColumns = new String[] { "TableId", "Active Players", "Gamecount" };
        theColumnWidths = new int[] { 90, 90, 90 };
    }

    public String getColumnName(int column) {
        return theTableStatsColumns[column];
    }

    public int getColumnCount() {
        return theTableStatsColumns.length;
    }

    public int getRowCount() {
        int retVal = 0;
        if (engine != null && engine.getTableStatistics() != null) retVal = engine.getTableStatistics().size();
        return retVal;
    }

    public Class getColumnClass(int columnIndex) {
        Class tempReturn = null;
        switch(columnIndex) {
            case 0:
                tempReturn = Long.class;
                break;
            case 1:
                tempReturn = Long.class;
                break;
            case 2:
                tempReturn = Long.class;
                break;
            default:
                tempReturn = String.class;
                break;
        }
        return tempReturn;
    }

    public Object getValueAt(int row, int column) {
        Object retValue = null;
        TableStatistics tempStatistics = null;
        try {
            tempStatistics = (TableStatistics) engine.getTableStatistics().get(row);
        } catch (RuntimeException e) {
            return null;
        }
        switch(column) {
            case 0:
                retValue = tempStatistics.getTableId();
                break;
            case 1:
                retValue = tempStatistics.getActivePlayers();
                break;
            case 2:
                retValue = tempStatistics.getGamecount();
                break;
        }
        return retValue;
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
    * Set the ColumnWidths of a table containing a model like THIS.
    * 
    * @param theTable
    *           THE MODEL OF THE TABLE MUST BE OF THIS CLASS
    */
    public void setColumnWidthsToDefault(JTable aTable) {
        if (!(aTable.getModel() instanceof TableStatsTableModel)) return;
        for (int i = 0; i < theTableStatsColumns.length; ++i) {
            TableColumn tc = aTable.getColumnModel().getColumn(i);
            tc.setPreferredWidth(this.theColumnWidths[i]);
            aTable.revalidate();
        }
    }
}
