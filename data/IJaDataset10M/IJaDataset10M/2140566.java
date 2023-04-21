package com.certesystems.swingforms.gridset.components;

import org.apache.commons.jxpath.JXPathContext;
import com.certesystems.swingforms.grid.Grid;

public class GridSetRow implements Comparable<GridSetRow> {

    private GridSetModel model;

    private int index;

    public GridSetRow(GridSetModel model, int index) {
        this.model = model;
        this.index = index;
    }

    public int compareTo(GridSetRow other) {
        return compareToRow(other) * (model.isInverseSort() ? -1 : 1);
    }

    private int compareToRow(GridSetRow otherRow) {
        String sortColumnName = model.getGridSet().getFields().get(model.getSortColumn()).getMapping();
        Object a = JXPathContext.newContext(((Grid) model.getGridSet().getGrids().get(index)).getRegister()).getValue(sortColumnName);
        Object b = JXPathContext.newContext(((Grid) model.getGridSet().getGrids().get(otherRow.index)).getRegister()).getValue(sortColumnName);
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return -1;
        } else if (b == null) {
            return 1;
        } else if (a instanceof Comparable) {
            return ((Comparable) a).compareTo(b);
        } else {
            return index - otherRow.index;
        }
    }

    public final int getIndex() {
        return index;
    }

    public final void setIndex(int index) {
        this.index = index;
    }
}
