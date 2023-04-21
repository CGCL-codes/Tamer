package com.ncs.crm.client.report;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class MannerListGrid extends ListGrid {

    MannerListGrid() {
        setWidth(120);
        setHeight(150);
        setShowEdges(true);
        setBorder("2px");
        setBodyStyleName("normal");
        setAlternateRecordStyles(true);
        setShowHeader(false);
        setLeaveScrollbarGap(false);
        setCanDragRecordsOut(true);
        setCanAcceptDroppedRecords(true);
        setDragDataAction(DragDataAction.MOVE);
        ListGridField id = new ListGridField("id");
        id.setHidden(true);
        ListGridField manner = new ListGridField("mannerName", "拜访方式");
        manner.setAlign(Alignment.CENTER);
        setFields(id, manner);
    }
}
