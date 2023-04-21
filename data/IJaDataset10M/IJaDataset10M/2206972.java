package org.awelements.table;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * This content provider can extract elements from anything that is an Object array,
 * derived from {@link List} or is a bean that provides appropriate getter methods
 * for the table' column names.
 * 
 * @author guido
 */
public class DefaultContentProvider implements IContentProvider {

    private Table mTable;

    public Object getElement(int columnIndex, Object rowObject) {
        if (rowObject == null || columnIndex < 0) return null;
        if (rowObject instanceof Object[]) {
            final Object[] array = (Object[]) rowObject;
            if (columnIndex >= array.length) return null;
            return array[columnIndex];
        } else if (rowObject instanceof List) {
            final List list = (List) rowObject;
            if (columnIndex >= list.size()) return null;
            return list.get(columnIndex);
        } else if (columnIndex < mTable.getNumColumns()) {
            final Column column = mTable.getColumn(columnIndex);
            if (column instanceof ColumnGroup) return getAllProperties(rowObject, (ColumnGroup) column);
            return getProperty(rowObject, column.getName());
        }
        return null;
    }

    private Object[] getAllProperties(Object rowObject, ColumnGroup columnGroup) {
        final List<Object> result = new ArrayList();
        for (Column column : columnGroup.getColumns()) {
            if (column instanceof ColumnGroup) {
                result.add(getAllProperties(rowObject, (ColumnGroup) column));
            } else {
                result.add(getProperty(rowObject, column.getName()));
            }
        }
        return result.toArray();
    }

    private Object getProperty(Object rowObject, String name) {
        try {
            return PropertyUtils.getProperty(rowObject, name);
        } catch (Exception e) {
            return null;
        }
    }

    public Object[] getElements(Object rowObject) {
        final Object[] result = new Object[mTable.getNumColumns()];
        for (int columnIdx = 0; columnIdx < mTable.getNumColumns(); ++columnIdx) result[columnIdx] = getElement(columnIdx, rowObject);
        return result;
    }

    public void setTable(Table table) {
        mTable = table;
    }
}
