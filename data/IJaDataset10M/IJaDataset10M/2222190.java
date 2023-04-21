package com.hba.web.lib.client.ui.panel;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class BasicSearchView extends FlexTable {

    private Integer MAX_COLUMN_VIEW = 3;

    public BasicSearchView() {
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            setStyleName("display");
        } else {
            setStyleName("no-display");
        }
        super.setVisible(visible);
    }

    public void resetValues() {
    }

    @Override
    public void add(Widget child) {
        int rowNum = getRowCount() - 1;
        int cellNum = 0;
        if (rowNum < 0) {
            rowNum = 0;
            cellNum = 0;
        } else {
            cellNum = getCellCount(rowNum);
        }
        if (cellNum < MAX_COLUMN_VIEW) {
            this.setWidget(rowNum, cellNum, child);
        } else {
            this.setWidget(rowNum + 1, 0, child);
        }
    }
}
