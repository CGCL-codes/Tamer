package br.com.klis.batendoumabola.client.widgets;

import br.com.klis.batendoumabola.shared.Pelada;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Tabela extends Composite {

    private static final int HEADER_ROW_INDEX = 0;

    private FlexTable flexTable;

    public Tabela(Pelada[] rowData) {
        FlexTable flexTable = new FlexTable();
        flexTable.insertRow(HEADER_ROW_INDEX);
        flexTable.getRowFormatter().addStyleName(HEADER_ROW_INDEX, "FlexTable-Header");
        addColumn("Nome");
        addColumn("Data");
        for (int row = 0; row < rowData.length; row++) {
            addRow(rowData[row]);
        }
        applyDataRowStyles();
        flexTable.setCellSpacing(0);
        flexTable.addStyleName("FlexTable");
    }

    private void addColumn(Object columnHeading) {
        Widget widget = createCellWidget(columnHeading);
        int cell = flexTable.getCellCount(HEADER_ROW_INDEX);
        widget.setWidth("100%");
        widget.addStyleName("FlexTable-ColumnLabel");
        flexTable.setWidget(HEADER_ROW_INDEX, cell, widget);
        flexTable.getCellFormatter().addStyleName(HEADER_ROW_INDEX, cell, "FlexTable-ColumnLabelCell");
    }

    private Widget createCellWidget(Object cellObject) {
        Widget widget = null;
        if (cellObject instanceof Widget) {
            widget = (Widget) cellObject;
        } else {
            widget = new Label(cellObject.toString());
        }
        return widget;
    }

    private void addRow(Pelada pelada) {
        int rowIndex = flexTable.getRowCount();
        Widget widget1 = createCellWidget(pelada.getNome());
        flexTable.setWidget(rowIndex, 0, widget1);
        Widget widget2 = createCellWidget(pelada.getData());
        flexTable.setWidget(rowIndex, 1, widget2);
    }

    private void applyDataRowStyles() {
        HTMLTable.RowFormatter rf = flexTable.getRowFormatter();
        for (int row = 1; row < flexTable.getRowCount(); ++row) {
            if ((row % 2) != 0) {
                rf.addStyleName(row, "FlexTable-OddRow");
            } else {
                rf.addStyleName(row, "FlexTable-EvenRow");
            }
        }
    }
}
