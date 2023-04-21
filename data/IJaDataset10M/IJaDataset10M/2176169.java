package edu.pitt.dbmi.odie.gapp.gwt.client.util.picker.pastel;

import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.core.Rectangle;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellHoverEvent;
import com.smartgwt.client.widgets.grid.events.CellHoverHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class ODIE_PastelPickerItem extends TextItem implements RecordClickHandler, CellHoverHandler {

    private static ODIE_PastelPickerItem currentEditor;

    private static Dialog dialog;

    private static ListGrid colorPickerGrid = null;

    private static HandlerRegistration colorPickerGridCellHoverRegistration = null;

    private static HandlerRegistration colorPickerGridOnRecordClickRegistration = null;

    private static int currentRow = 0;

    private static int currentCol = 0;

    private static void makeDialog() {
        dialog = new Dialog();
        dialog.setAutoCenter(true);
        dialog.setIsModal(true);
        dialog.setShowHeader(false);
        dialog.setShowEdges(false);
        dialog.setEdgeSize(10);
        dialog.setShowToolbar(false);
        dialog.setWidth(345);
        dialog.setHeight(445);
        colorPickerGrid = buildColorPickerGrid();
        attachListeners();
        dialog.addItem(colorPickerGrid);
    }

    private static ListGrid buildColorPickerGrid() {
        final ListGrid colorPickerGrid = new ListGrid() {

            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
                String pastelColumnName = "pastel-" + (colNum + 1);
                String htmlColor = record.getAttribute(pastelColumnName);
                String cellStyle = "{color:#000000; background-color:" + htmlColor + ";}";
                return cellStyle;
            }

            protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {
                String cellStyleResult = "odieColorPickerBaseCell";
                int lastRowNum = currentRow;
                int lastColNum = currentCol;
                if (lastRowNum == rowNum && lastColNum == colNum) {
                    cellStyleResult = "odieColorPickerHiliteCell";
                }
                return cellStyleResult;
            }
        };
        colorPickerGrid.setShowAllRecords(true);
        colorPickerGrid.setDataSource(ODIE_PastelDS.getInstance());
        colorPickerGrid.setAutoFetchData(true);
        colorPickerGrid.setCanEdit(false);
        colorPickerGrid.setSelectionType(SelectionStyle.NONE);
        colorPickerGrid.setCanHover(true);
        colorPickerGrid.setCanSelectAll(false);
        colorPickerGrid.setShowHeader(false);
        colorPickerGrid.setShowHover(false);
        colorPickerGrid.disableHiliting();
        return colorPickerGrid;
    }

    private static void attachListeners() {
        if (colorPickerGridCellHoverRegistration != null) {
            colorPickerGridCellHoverRegistration.removeHandler();
            colorPickerGridCellHoverRegistration = null;
        }
        if (colorPickerGridOnRecordClickRegistration != null) {
            colorPickerGridOnRecordClickRegistration.removeHandler();
            colorPickerGridOnRecordClickRegistration = null;
        }
        colorPickerGridCellHoverRegistration = colorPickerGrid.addCellHoverHandler(currentEditor);
        colorPickerGridOnRecordClickRegistration = colorPickerGrid.addRecordClickHandler(currentEditor);
    }

    public void onCellHover(CellHoverEvent event) {
        currentRow = event.getRowNum();
        currentCol = event.getColNum();
        colorPickerGrid.refreshRow(currentRow);
    }

    public void onRecordClick(RecordClickEvent event) {
        int currentColIdx = event.getFieldNum() + 1;
        Record record = event.getRecord();
        String htmlColor = record.getAttribute("pastel-" + currentColIdx);
        if (htmlColor != null) {
            ODIE_PastelPickerItem.this.setValue(htmlColor);
            String cellStyle = "{color:#000000; background-color:" + htmlColor + ";}";
            ODIE_PastelPickerItem.this.setCellStyle(cellStyle);
            dialog.hide();
        }
    }

    ;

    private static void showDialog(int left, int top) {
        dialog.show();
        dialog.moveTo(left, top);
    }

    public ODIE_PastelPickerItem() {
        FormItemIcon formItemIcon = new FormItemIcon();
        setIcons(formItemIcon);
        addIconClickHandler(new IconClickHandler() {

            public void onIconClick(IconClickEvent event) {
                Rectangle iconRect = getIconPageRect(event.getIcon());
                ODIE_PastelPickerItem.currentEditor = ODIE_PastelPickerItem.this;
                if (ODIE_PastelPickerItem.dialog == null) {
                    ODIE_PastelPickerItem.makeDialog();
                }
                ODIE_PastelPickerItem.showDialog(iconRect.getLeft(), iconRect.getTop());
            }
        });
    }
}
