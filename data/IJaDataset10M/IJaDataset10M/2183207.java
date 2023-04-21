package tradeWatch;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class WorkingOrderTable extends AbstractTableModel implements TableModelListener {

    private static final long serialVersionUID = 1L;

    private static int NUM_COLUMNS = 9;

    private WorkingOrderManager table;

    public WorkingOrderTable(WorkingOrderManager orderManager) {
        table = orderManager;
        orderManager.addTableModelListener(this);
    }

    public WorkingOrderManager getManager() {
        return table;
    }

    public int getRowCount() {
        return table.getRowCount();
    }

    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    public boolean isCellEditable(int r, int c) {
        return false;
    }

    public Object getValueAt(int r, int c) {
        WorkingOrder workingOrder = table.getRow(r);
        switch(c) {
            case 0:
                return workingOrder.getSymbol();
            case 1:
                return workingOrder.getOrderId();
            case 2:
                return workingOrder.getDirection();
            case 3:
                return workingOrder.getOrderType();
            case 4:
                return workingOrder.getQuantity();
            case 5:
                return workingOrder.getFilled();
            case 6:
                return workingOrder.getFillPrice();
            case 7:
                return workingOrder.getStatus();
            case 8:
                return workingOrder.getOrderExecuted();
            default:
                return null;
        }
    }

    public String getColumnName(int c) {
        switch(c) {
            case 0:
                return "Symbol";
            case 1:
                return "Order ID";
            case 2:
                return "Direction";
            case 3:
                return "Order";
            case 4:
                return "Quantity";
            case 5:
                return "Filled";
            case 6:
                return "Price";
            case 7:
                return "Status";
            case 8:
                return "Executed";
            default:
                return null;
        }
    }

    public void tableChanged(TableModelEvent e) {
        fireTableDataChanged();
    }
}
