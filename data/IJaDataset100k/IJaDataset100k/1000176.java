package org.awelements.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColumnGroup extends Column {

    private List<Column> mColumns = new ArrayList();

    public ColumnGroup(String name, String title) {
        super(name, title);
        setPrintable(false);
    }

    public int addColumn(Column column) {
        final int index = mColumns.size();
        column.setIndex(index);
        column.setParent(this);
        mColumns.add(column);
        return index;
    }

    public Column getColumn(int i) {
        if (i < 0 || i >= mColumns.size()) return null;
        return mColumns.get(i);
    }

    public List<Column> getColumns() {
        return mColumns;
    }

    @Override
    public Comparator<Object> getComparator() {
        for (Column column : mColumns) {
            if (column.getComparator() != null) return new ColumnGroupComparator(this.mColumns);
        }
        return null;
    }

    @Override
    public Filter getFilter(String name) {
        return getDefaultFilter();
    }

    @Override
    public Filter getDefaultFilter() {
        final List<Filter> filters = new ArrayList();
        for (Column column : mColumns) {
            final Filter defaultFilter = column.getDefaultFilter();
            if (defaultFilter != null) filters.add(defaultFilter);
        }
        if (filters.isEmpty()) return null;
        final ColumnGroupFilter columnGroupFilter = new ColumnGroupFilter(filters);
        columnGroupFilter.setColumn(this);
        return columnGroupFilter;
    }
}
