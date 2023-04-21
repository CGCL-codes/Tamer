package ru.yermak.bookkeeping.ui;

import ru.yermak.bookkeeping.common.DataModel;

/**
 * User: harrier
 * Date: Nov 5, 2008
 */
public class MockTableModel implements DataModel {

    public void load() {
    }

    public void clean() {
    }

    public Object get(int row) {
        return null;
    }

    public Integer getRowById(Integer selectedId) {
        return null;
    }

    public void fireTableDataChanged() {
    }

    @Override
    public void fireTableStructureChanged() {
    }
}
