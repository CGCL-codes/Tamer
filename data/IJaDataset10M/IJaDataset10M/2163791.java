package ca.sqlpower.wabit.swingui.olap;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.olap4j.CellSet;
import org.olap4j.CellSetAxis;
import org.olap4j.metadata.Member;

public class CellSetTableModel implements TableModel {

    private final CellSet cellSet;

    private CellSetAxis columnsAxis;

    private CellSetAxis rowsAxis;

    public CellSetTableModel(CellSet cellSet) {
        this.cellSet = cellSet;
        columnsAxis = cellSet.getAxes().get(0);
        rowsAxis = cellSet.getAxes().get(1);
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public String getColumnName(int columnIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        for (Member member : columnsAxis.getPositions().get(columnIndex).getMembers()) {
            sb.append(member.getName()).append("<br>");
        }
        return sb.toString();
    }

    public int getColumnCount() {
        return columnsAxis.getPositionCount();
    }

    public int getRowCount() {
        return rowsAxis.getPositionCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return cellSet.getCell(columnsAxis.getPositions().get(columnIndex), rowsAxis.getPositions().get(rowIndex)).getFormattedValue();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not editable! Not by a long shot!");
    }
}
