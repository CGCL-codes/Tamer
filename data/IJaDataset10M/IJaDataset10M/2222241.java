package com.xduke.xswing;

import com.xduke.xswing.DataTipCell;
import com.xduke.xswing.DataTipListener;
import com.xduke.xswing.TableDataTipCell;
import javax.swing.*;
import java.awt.*;

class TableDataTipListener extends DataTipListener {

    TableDataTipListener() {
    }

    DataTipCell getCell(JComponent component, Point point) {
        JTable table = (JTable) component;
        int rowIndex = table.rowAtPoint(point);
        int columnIndex = table.columnAtPoint(point);
        if (rowIndex < 0 || columnIndex < 0) {
            return DataTipCell.NONE;
        }
        if (table.isEditing()) return DataTipCell.NONE;
        TableDataTipCell cellPosition = new TableDataTipCell(table, rowIndex, columnIndex);
        return cellPosition;
    }
}
