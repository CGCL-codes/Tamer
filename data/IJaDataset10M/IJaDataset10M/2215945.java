package de.bielefeld.uni.cebitec.r2cat.gui;

import java.util.Observable;
import java.util.Observer;
import javax.swing.table.AbstractTableModel;
import de.bielefeld.uni.cebitec.qgram.MatchList;

/**
 * This is a Table model for displaying an {@link MatchList} as a
 * table.
 * 
 * @author Peter Husemann
 * 
 */
public class MatchesTableModel extends AbstractTableModel implements Observer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5076289599342214580L;

    private String[] columnNames = { "Query", "Start", "Stop", "Target", "Start", "Stop", "q-hits", "repeat Count" };

    private MatchList alPosList;

    /**
	 * Constructs a table model from an {@link MatchList}.
	 * 
	 * @param alPosList
	 */
    public MatchesTableModel(MatchList matches) {
        this.setMathesList(matches);
    }

    public void setMathesList(MatchList matches) {
        this.alPosList = matches;
        this.alPosList.addObserver(this);
        this.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public int getRowCount() {
        return alPosList.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Object out = null;
        switch(columnIndex) {
            case 0:
                out = alPosList.getMatchAt(rowIndex).getQuery().getId();
                break;
            case 1:
                out = alPosList.getMatchAt(rowIndex).getQueryStart();
                break;
            case 2:
                out = alPosList.getMatchAt(rowIndex).getQueryEnd();
                break;
            case 3:
                out = alPosList.getMatchAt(rowIndex).getTarget().getId();
                break;
            case 4:
                out = alPosList.getMatchAt(rowIndex).getTargetStart();
                break;
            case 5:
                out = alPosList.getMatchAt(rowIndex).getTargetEnd();
                break;
            case 6:
                if (alPosList.getMatchAt(rowIndex).getNumberOfQHits() > 0) {
                    out = alPosList.getMatchAt(rowIndex).getNumberOfQHits();
                } else {
                    out = "N/A";
                }
                break;
            case 7:
                out = alPosList.getMatchAt(rowIndex).getRepeatCount();
                break;
            default:
                break;
        }
        return out;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.getValueAt(0, columnIndex).getClass();
    }

    public void update(Observable o, Object arg) {
    }
}
