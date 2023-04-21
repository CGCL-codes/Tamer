package data;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class ListModel extends DefaultTableModel {

    private configuration config;

    public ListModel(Object[] columns, int x, configuration c) {
        super(columns, x);
        config = c;
    }

    public boolean isCellEditable(int row, int col) {
        if (col == config.wlistcol || col == config.blistcol || col == config.watchcol || col == config.twhitecol || col == config.tblackcol || col == config.twatchcol) return (true); else return (false);
    }

    public Class getColumnClass(int column) {
        Vector v = (Vector) dataVector.elementAt(0);
        return v.elementAt(column).getClass();
    }
}
