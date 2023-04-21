package org.freeworld.prilib.impl.pojo;

import java.io.Serializable;
import org.freeworld.prilib.column.ColumnSchema;
import org.freeworld.prilib.column.ColumnSchemaGroup;
import org.freeworld.prilib.row.AbstractTableRow;
import org.freeworld.prilib.row.TableRow;
import org.freeworld.prilib.util.BeanUtil;

public class PojoRow extends AbstractTableRow {

    private static final long serialVersionUID = 1L;

    private Serializable object = null;

    public PojoRow() {
    }

    public PojoRow(ColumnSchemaGroup<ColumnSchema> columnSchemas) {
        super(columnSchemas);
    }

    public PojoRow(TableRow row) {
        super(row);
    }

    @Override
    public void setValue(String columnName, Object value) {
        if (object != null) BeanUtil.setValue(object, columnName, value);
    }

    @Override
    public void setValue(int columnIndex, Object value) {
        setValue(getColumnNames()[columnIndex], value);
    }

    @Override
    public Object getValue(String columnName) {
        if (object != null) return BeanUtil.getValue(object, columnName); else return null;
    }

    @Override
    public Object getValue(int columnIndex) {
        return getValue(getColumnNames()[columnIndex]);
    }

    @Override
    protected void fixColumnSchemaMappings() {
    }

    @Override
    protected void removeValue(ColumnSchema schema) {
    }

    public void setObject(Serializable object) {
        this.object = object;
    }

    public Serializable getObject() {
        return object;
    }

    @Override
    public TableRow newCopy(boolean preserveColumns) {
        PojoRow retr = new PojoRow();
        retr.setObject(getObject());
        return retr;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }
}
