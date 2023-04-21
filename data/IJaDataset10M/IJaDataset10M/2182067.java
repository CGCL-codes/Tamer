package com.outOfCaffeineException.jTimeTracking.application.layer1Front.gui.tachesJTable.model;

import javax.swing.table.AbstractTableModel;
import com.outOfCaffeineException.jTimeTracking.application.layer1Front.gui.tachesJTable.buttonbar.ButtonsBarJPanel;
import com.outOfCaffeineException.jTimeTracking.application.layer2Controler.Dispatcher;
import com.outOfCaffeineException.jTimeTracking.model.business.Application;
import com.outOfCaffeineException.jTimeTracking.model.tech.controler.ActionParametersContainer;
import com.outOfCaffeineException.jTimeTracking.model.tech.controler.ParametersConstants;

public class TableModel extends AbstractTableModel {

    private static final long serialVersionUID = -2404388697495330801L;

    private String[] columnNames = { "", "Nom", "Temps pass�", "" };

    @Override
    public int getRowCount() {
        return Application.getInstance().getTaches().size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Boolean.class;
        } else if (columnIndex == 3) {
            return ButtonsBarJPanel.class;
        } else return super.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return new Boolean(Application.getInstance().getTaches().get(rowIndex).isTermine());
        } else if (columnIndex == 1) {
            return Application.getInstance().getTaches().get(rowIndex).getName();
        } else if (columnIndex == 2) {
            return formatTime(Application.getInstance().getTaches().get(rowIndex).getDuree());
        } else if (columnIndex == 3) {
            return Application.getInstance().getTaches().get(rowIndex).getDateLimite();
        } else return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 2) return false; else return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ActionParametersContainer params = new ActionParametersContainer();
        if (columnIndex < 2 && !getValueAt(rowIndex, columnIndex).equals(aValue)) {
            if (columnIndex == 0) params.putValues(ParametersConstants.ATT_TACHE_TERMINE, aValue); else if (columnIndex == 1) params.putValues(ParametersConstants.ATT_TACHE_NAME, aValue);
            params.putValues(ParametersConstants.TACHE_INDEX, rowIndex);
            Dispatcher.getInstance().execute(Dispatcher.ACTION_UPDATE_TACHE, params);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    protected String formatTime(int timeInS) {
        StringBuffer result = new StringBuffer(30);
        int minutes = timeInS / 60;
        int secRest = timeInS - (60 * minutes);
        int hour = minutes / 60;
        int minRest = minutes - (60 * hour);
        result.append(hour).append(" h ").append(minRest).append(" min. ").append(secRest).append(" sec.");
        return result.toString();
    }
}
