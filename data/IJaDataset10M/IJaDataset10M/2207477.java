package com.gorillalogic.dal.utils;

import com.gorillalogic.dal.*;

/**
 * <code>TableDuper</code> provides utilities for
 * duplicating and copying tables.
 *
 * @author bpm
 * @version 1.0
 */
public class TableDuper {

    private Table from;

    public TableDuper(Table from) {
        this.from = from;
    }

    public void copyTo(Table to) throws AccessException {
        TableExtender toExtend = to.extend(true);
        if (toExtend == null) {
            throw new UpdateException("Can't copy to non-extendable table");
        }
        int cc = from.columnCount();
        Table.Itr itr = from.loopLock();
        while (itr.next()) {
            toExtend.addRef(itr);
            Table.Row trow = to.row(itr.getRowId());
            for (int column = 0; column < cc; column++) {
                ColumnHdr hdr = from.column(column);
            }
        }
    }

    public Table dupEmpty() throws AccessException {
        throw new UnsupportedException("TableDuper.dupEmpty");
    }

    public Table dupFull() throws AccessException {
        Table table = dupEmpty();
        copyTo(table);
        return table;
    }
}
