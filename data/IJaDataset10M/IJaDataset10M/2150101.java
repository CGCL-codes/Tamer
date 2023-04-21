package org.illico.common.model.grid.page;

import org.illico.common.model.grid.CellChangedListener;
import org.illico.common.model.grid.GridModel;
import org.illico.common.model.list.page.AbstractPaginedListModel;

public class DefaultPaginedGridModel<L, C> extends AbstractPaginedListModel<GridModel<L, C>, L> implements GridModel<L, C> {

    public DefaultPaginedGridModel(GridModel<L, C> gridModel) {
        super(gridModel);
    }

    public void addCell(int lineIndex, C cell) {
        getListModel().addCell(lineIndex, cell);
    }

    public void addCellChangedListener(CellChangedListener<L, C> listener) {
        getListModel().addCellChangedListener(listener);
    }

    public C getCell(int columnIndex, int lineIndex) {
        return getListModel().getCell(columnIndex, lineIndex);
    }

    public int getColSpan(int columnIndex, int lineIndex) {
        return getListModel().getColSpan(columnIndex, lineIndex);
    }

    public int getColumnCount() {
        return getListModel().getColumnCount();
    }

    public int getRowSpan(int columnIndex, int lineIndex) {
        return getListModel().getRowSpan(columnIndex, lineIndex);
    }

    public C removeCell(int columnIndex, int lineIndex) {
        return getListModel().removeCell(columnIndex, lineIndex);
    }

    public void removeCellChangedListener(CellChangedListener<L, C> listener) {
        getListModel().removeCellChangedListener(listener);
    }

    public C replaceCell(int columnIndex, int lineIndex, C cell) {
        return getListModel().replaceCell(columnIndex, lineIndex, cell);
    }
}
