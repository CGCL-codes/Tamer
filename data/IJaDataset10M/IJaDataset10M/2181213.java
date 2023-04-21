package com.xduke.xswing;

import com.xduke.xswing.DataTipCell;
import com.xduke.xswing.DataTipListener;
import com.xduke.xswing.ListDataTipCell;
import javax.swing.*;
import java.awt.*;

class ListDataTipListener extends DataTipListener {

    ListDataTipListener() {
    }

    DataTipCell getCell(JComponent component, Point point) {
        JList list = (JList) component;
        int index = list.locationToIndex(point);
        if (index < 0) {
            return DataTipCell.NONE;
        }
        return new ListDataTipCell(list, index);
    }
}
