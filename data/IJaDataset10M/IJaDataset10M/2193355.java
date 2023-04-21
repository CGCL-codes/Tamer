package org.CDS.Gui;

import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

public class AdminPanel extends Window {

    Grid adminGrid;

    Button backButton = new Button();

    Button groupButton, deptButton, formButton, messageButton;

    public AdminPanel() {
        adminGrid = new Grid();
        adminGrid.setWidth("150px");
        adminGrid.setParent(this);
        Columns adminColumns = new Columns();
        adminColumns.setParent(adminGrid);
        Column adminColumn = new Column();
        adminColumn.setParent(adminColumns);
        adminColumn.setLabel("Admin Panel ...");
        Rows adminRows = new Rows();
        adminRows.setParent(adminGrid);
        Row groupRow = new Row();
        Row deptRow = new Row();
        Row formRow = new Row();
        Row messageRow = new Row();
        groupRow.setParent(adminRows);
        deptRow.setParent(adminRows);
        formRow.setParent(adminRows);
        messageRow.setParent(adminRows);
        groupButton = new Button();
        deptButton = new Button();
        formButton = new Button();
        messageButton = new Button();
        groupButton.setLabel("Create Group");
        deptButton.setLabel("Create Department");
        formButton.setLabel("Create Form");
        messageButton.setLabel("Create Message");
        groupButton.setParent(groupRow);
        deptButton.setParent(deptRow);
        formButton.setParent(formRow);
        messageButton.setParent(messageRow);
        adminColumns.setParent(adminGrid);
        backButton.setParent(this);
        backButton.setLabel("Back...");
    }
}
