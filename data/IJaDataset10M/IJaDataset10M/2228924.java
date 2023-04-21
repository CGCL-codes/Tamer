package com.joejag.mavenstats.client.views.main.projects;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.joejag.mavenstats.client.dto.DependencyDTO;
import java.util.List;

public class DependencyTable extends Composite {

    private FlexTable table = new FlexTable();

    private static final int HEADER_ROWS = 1;

    public DependencyTable() {
        table.setCellSpacing(0);
        table.setCellPadding(0);
        table.setWidth("100%");
        initWidget(table);
        setStyleName("mail-List");
        initTable();
    }

    private void initTable() {
        table.setText(0, 0, "Grouping");
        table.setText(0, 1, "Name");
        table.setText(0, 2, "Version");
        table.getRowFormatter().setStyleName(0, "mail-ListHeader");
    }

    public void update(List dependencies) {
        for (int i = table.getRowCount() - 1; i >= HEADER_ROWS; i--) {
            table.removeRow(i);
        }
        for (int i = 0; i < dependencies.size(); ++i) {
            DependencyDTO dependency = (DependencyDTO) dependencies.get(i);
            table.setText(i + HEADER_ROWS, 0, dependency.grouping);
            table.setText(i + HEADER_ROWS, 1, dependency.name);
            table.setText(i + HEADER_ROWS, 2, dependency.version);
            table.getCellFormatter().setWordWrap(i + HEADER_ROWS, 0, false);
            table.getCellFormatter().setWordWrap(i + HEADER_ROWS, 1, false);
            table.getCellFormatter().setWordWrap(i + HEADER_ROWS, 2, false);
        }
    }
}
