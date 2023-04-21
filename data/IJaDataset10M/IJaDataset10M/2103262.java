package jtrackbase.gui.table;

import java.util.Collection;
import jtrackbase.db.Label;

public class LabelTableModel extends EntityTableModel<Label> {

    public LabelTableModel() {
        super();
    }

    public LabelTableModel(Collection<Label> coll) {
        super(coll);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? String.class : null;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnIndex == 0 ? "Name" : null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Label m = get(rowIndex);
        if (m == null) {
            return null;
        }
        switch(columnIndex) {
            case 0:
                {
                    return m.getName();
                }
        }
        return null;
    }
}
