package net.sourceforge.nattable.painter.region;

import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.model.INatTableModel;
import net.sourceforge.nattable.renderer.ICellRenderer;

public class DefaultColumnHeaderRegionPainter extends DefaultRegionPainter {

    private INatTableModel model;

    public DefaultColumnHeaderRegionPainter(NatTable natTable) {
        super(natTable);
        this.model = natTable.getNatTableModel();
    }

    @Override
    protected ICellRenderer getCellRenderer() {
        return model.getColumnHeaderCellRenderer();
    }

    @Override
    protected int getRowHeight(int row) {
        return model.getColumnHeaderRowHeight(row);
    }

    @Override
    protected int getColumnWidth(int col) {
        return model.getBodyColumnWidth(col);
    }
}
