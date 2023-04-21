package annone.ui;

import java.util.Set;

public interface Grid extends View {

    EditMode getMode();

    void setMode(EditMode mode);

    int getColumnCount();

    Set<Column> getColumns();

    void addColumn(Column column);

    void removeColumn(Column column);

    int getRowCount();

    int addRow(int row);

    void removeRow(int row);

    Object getValue(Column column, int row);

    void setValue(Column column, int row, Object value);
}
