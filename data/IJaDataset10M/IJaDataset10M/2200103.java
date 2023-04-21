package com.siberhus.hswing.table;

import java.util.List;

public interface TableDataQuery {

    public String[] getColumnNames();

    public int getRowCount();

    public List<Object[]> fetchRows(int offset, int limit) throws Exception;
}
