package us.wthr.jdem846.ui.base;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;

@SuppressWarnings("serial")
public class Table extends JTable {

    @SuppressWarnings("unused")
    private static Log log = Logging.getLog(Table.class);

    public Table() {
        super();
    }

    public Table(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public Table(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    public Table(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public Table(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public Table(TableModel dm) {
        super(dm);
    }
}
