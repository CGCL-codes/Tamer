package com.talios.jira.settings;

import com.intellij.openapi.project.Project;
import com.talios.jira.browser.ColumnSetting;
import com.talios.jira.idea.JiraConfiguration;
import com.talios.jira.idea.JiraConfigurationComponent;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ColumnsTableModel extends AbstractTableModel {

    private JiraConfiguration config;

    private Project project;

    private List columnList;

    public ColumnsTableModel(Project project) {
        this.project = project;
        config = project.getComponent(JiraConfigurationComponent.class).getConfiguration();
        columnList = config.getColumnList();
    }

    public int getColumnCount() {
        return 1;
    }

    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "Column";
            default:
                return "";
        }
    }

    public int getRowCount() {
        return columnList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ColumnSetting columnSetting = getColumnSetting(rowIndex);
        if (columnSetting == null) {
            return "ERR";
        }
        switch(columnIndex) {
            case 0:
                return columnSetting.getId();
            default:
                return "";
        }
    }

    public ColumnSetting getColumnSetting(int rowIndex) {
        if (rowIndex != -1) {
            ColumnSetting columnSetting = (ColumnSetting) columnList.toArray()[rowIndex];
            return columnSetting;
        } else {
            return null;
        }
    }
}
