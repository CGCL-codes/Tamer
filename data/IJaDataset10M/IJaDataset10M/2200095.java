package com.hifi.core.api.model.table;

import java.util.Comparator;
import com.hifi.core.model.table.SortedElement;

public interface ITableModelComparator {

    public void setSortedColumn(ITableColumn col);

    public Comparator<SortedElement> getComparator();
}
