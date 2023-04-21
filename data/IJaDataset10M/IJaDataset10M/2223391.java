package org.pojosoft.ria.gwt.client.ui.treetable;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A {@link TreeTableListener} for highlighting the selected row
 *
 * @author POJO Software
 */
public class HighlightRowTreeTableListener extends TreeTableListenerAdapter {

    public void onRowClick(TreeTable.RowClickEvent event) {
        if (event.getRowId() != null && event.getRowId().equals("_header")) return;
        selectedRowId = event.getRowId();
        TreeTable treeTable = (TreeTable) event.getSender();
        Element rowElement = treeTable.getRowElement(event.getRowId());
        selectRow(rowElement);
    }

    String selectedRowId;

    Element selectedRowElem;

    protected void selectRow(Element rowElem) {
        if (selectedRowElem != null) {
            int childCount = DOM.getChildCount(selectedRowElem);
            for (int i = 0; i < childCount; i++) {
                Element tdElem = DOM.getChild(selectedRowElem, i);
                DOM.setElementProperty(tdElem, "className", "pojo-TreeTable-Td");
            }
        }
        int childCount = DOM.getChildCount(rowElem);
        for (int i = 0; i < childCount; i++) {
            Element tdElem = DOM.getChild(rowElem, i);
            DOM.setElementProperty(tdElem, "className", "pojo-TreeTable-Td-Selected");
        }
        selectedRowElem = rowElem;
    }

    public String getSelectedRowId() {
        return selectedRowId;
    }
}
