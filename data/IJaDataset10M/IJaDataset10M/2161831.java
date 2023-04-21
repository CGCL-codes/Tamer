package org.objectwiz.javafx.table;

import org.objectwiz.javafx.swing.OrderingListener;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.sort.TableSortController;
import javax.swing.SortOrder;

/**
 * A custom sort controller that notifies a listener that the user has changed
 * the order of one column and shunts the internal ordering mechanism (ordering
 * operation is left to the model).
 *
 * @author Ailing Qin <ailing.qin at helmet.fr>
 */
public class CustomTableSortController extends TableSortController {

    private OrderingListener listener;

    public CustomTableSortController(TableModel model, OrderingListener listener) {
        super();
        setModel(model);
        this.listener = listener;
        setSortsOnUpdates(false);
    }

    @Override
    public void toggleSortOrder(int columnIndex) {
        String columnName = ((TableModel) this.getModel()).getColumnName(columnIndex);
        SortOrder previousOrder = this.getSortOrder(columnIndex);
        if (SortOrder.UNSORTED.equals(previousOrder)) {
            listener.orderChanged(columnName, true);
            setSortOrder(columnIndex, SortOrder.DESCENDING);
        } else {
            listener.orderChanged(columnName, SortOrder.ASCENDING.equals(previousOrder));
            super.toggleSortOrder(columnIndex);
        }
    }

    @Override
    public int convertRowIndexToModel(int index) {
        return index;
    }

    @Override
    public int convertRowIndexToView(int index) {
        return index;
    }
}
