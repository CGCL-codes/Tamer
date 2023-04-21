package org.hardtokenmgmt.admin.model;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import org.hardtokenmgmt.admin.common.GlobalPropertySetting;
import org.hardtokenmgmt.admin.ui.panels.editglprops.GlobalPropertyListDeleteButton;
import org.hardtokenmgmt.admin.ui.panels.editglprops.TemplateListDeleteButton;
import org.hardtokenmgmt.core.ui.UIHelper;

public class TemplateGlobalPropertiesTableModel extends AbstractTableModel implements IButtonColumnTableModel {

    private static final String COLUMNNAME_KEY = "editglobprops.key";

    private static final String COLUMNNAME_VALUE = "editglobprops.value";

    private static final long serialVersionUID = 1L;

    private Vector<ColumnType> columnNames = new Vector<ColumnType>();

    private Vector<GlobalPropertySetting> data = new Vector<GlobalPropertySetting>();

    private Vector<TemplateListDeleteButton> deleteButtons = new Vector<TemplateListDeleteButton>();

    public TemplateGlobalPropertiesTableModel(List<GlobalPropertySetting> data) {
        genColumnNames();
        setData(data);
    }

    private void genColumnNames() {
        columnNames.clear();
        columnNames.add(new ColumnType(String.class, "editglobprops.key", false));
        columnNames.add(new ColumnType(String.class, "editglobprops.value", true));
        columnNames.add(new ButtonColumnType("editadmins.delete"));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnNames.get(columnIndex).getClassType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnNames.get(columnIndex).editable();
    }

    @Override
    public void setValueAt(Object object, int rowIndex, int columnIndex) {
        ColumnType ct = columnNames.get(columnIndex);
        if (ct.editable()) {
            GlobalPropertySetting setting = data.get(rowIndex);
            if (ct.getColumnName().equals(COLUMNNAME_VALUE)) {
                setting.setValue((String) object);
            }
            fireTableDataChanged();
        }
    }

    @Override
    public String getColumnName(int index) {
        return columnNames.get(index).toString();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    public int getColumnPreferedWidth(int index) {
        if (columnNames.get(index) instanceof ButtonColumnType) {
            return 30;
        }
        if (columnNames.get(index).getColumnName().equals(COLUMNNAME_KEY)) {
            return 150;
        }
        return 250;
    }

    @Override
    public Object getValueAt(int row, int column) {
        ColumnType ct = columnNames.get(column);
        return ct.getValue(row);
    }

    private class ColumnType {

        protected String columnName;

        private Class<?> classType;

        private boolean editable;

        public ColumnType(Class<?> classType, String columnName, boolean editable) {
            super();
            this.classType = classType;
            this.columnName = columnName;
            this.editable = editable;
        }

        public String getColumnName() {
            return columnName;
        }

        public Class<?> getClassType() {
            return classType;
        }

        public boolean editable() {
            return editable;
        }

        public String toString() {
            return UIHelper.getText(columnName);
        }

        public Object getValue(int rowIndex) {
            GlobalPropertySetting setting = data.get(rowIndex);
            if (columnName.equals(COLUMNNAME_KEY)) {
                return setting.getKey();
            }
            if (columnName.equals(COLUMNNAME_VALUE)) {
                return setting.getValue();
            }
            return "";
        }
    }

    private class ButtonColumnType extends ColumnType {

        public ButtonColumnType(String columnName) {
            super(GlobalPropertyListDeleteButton.class, columnName, false);
        }

        public Object getValue(int rowIndex) {
            if (rowIndex < deleteButtons.size()) {
                return deleteButtons.get(rowIndex);
            }
            return null;
        }
    }

    public boolean isButtonColumn(int columnIndex) {
        return columnNames.get(columnIndex) instanceof ButtonColumnType;
    }

    public GlobalPropertySetting getGlobalPropertySetting(int rowIndex) {
        return data.get(rowIndex);
    }

    public List<GlobalPropertySetting> getData() {
        return data;
    }

    public void setData(List<GlobalPropertySetting> data) {
        this.data = new Vector<GlobalPropertySetting>();
        for (GlobalPropertySetting setting : data) {
            this.data.add(setting);
            deleteButtons.add(new TemplateListDeleteButton(setting, this));
        }
    }

    public void removeButton(TemplateListDeleteButton button) {
        deleteButtons.remove(button);
    }
}
