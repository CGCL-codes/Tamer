package org.objectwiz.javafx.table;

import org.objectwiz.javafx.swing.OrderingListener;
import org.jdesktop.swingx.JXTable;
import javax.swing.RowSorter;

/**
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class CustomJXTable extends JXTable {

    private OrderingListener listener;

    public CustomJXTable(OrderingListener listener) {
        this.listener = listener;
    }

    @Override
    protected RowSorter createDefaultRowSorter() {
        return new CustomTableSortController(getModel(), listener);
    }
}
