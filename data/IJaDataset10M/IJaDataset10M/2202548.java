package fr.helmet.javafx.table;

import fr.helmet.javafx.swing.OrderingListener;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.SortOrder;

/**
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class CustomJXTable extends JXTable {

    private OrderingListener listener;

    public CustomJXTable(OrderingListener listener) {
        this.listener = listener;
    }

    @Override
    public void toggleSortOrder(int columnIndex) {
        boolean currentlyAscending = SortOrder.ASCENDING.equals(this.getSortOrder(columnIndex));
        listener.orderChanged(this.getColumnName(columnIndex), !currentlyAscending);
        super.toggleSortOrder(columnIndex);
    }
}
