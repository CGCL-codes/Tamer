package com.datag.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import com.datag.form.frame.DataGenerator;
import com.datag.form.panel.ModulDataWizardPanel;
import com.datag.form.panel.ModulPropertyBinding;
import com.datag.form.panel.TablesWizardPanel;
import com.datag.util.ColumnMetaData;
import com.datag.util.ColumnPropertyBinding;
import com.datag.util.DataGeneratorUtility;
import com.datag.util.ForeignKeyInfo;
import com.datag.util.PostgreSQLType;
import com.datas.bean.enums.FieldType;
import com.datas.bean.model.system.SysComboItems;
import com.datas.bean.model.system.SysField;
import com.datas.bean.model.system.SysFkfield;
import com.datas.bean.model.system.SysModul;
import com.datas.bean.model.system.SysTable;
import com.datas.bean.model.system.SysTableField;
import com.datas.bean.model.system.SysTabpane;
import com.datas.component.enums.ClassNameType;
import com.datas.util.StringUtility;

/**
 * @author kimi
 * 
 */
public class DataGeneratorManager {

    private SysModul sysModul;

    private SysTable sysTable;

    private SortedMap<String, SysTabpane> tabpanes;

    private SortedMap<String, SysField> fields;

    private SortedMap<String, SysTableField> tableFields;

    private SortedMap<String, SysFkfield> fkFields;

    private SortedMap<String, SysComboItems> comboItems;

    private TablesWizardPanel tablesWizardPanel;

    private ModulDataWizardPanel modulDataWizardPanel;

    private ModulPropertyBinding modulPropertyBindingWizardPanel;

    private static String ID_TABPANE = "00010";

    public DataGeneratorManager() {
    }

    public void createModul() {
        sysModul = new SysModul();
        String bean = tablesWizardPanel.getMasterBeanTextField().getText();
        sysModul.setIdModul(modulDataWizardPanel.getIdModulTextField().getText());
        sysModul.setTitle(modulDataWizardPanel.getTitleTextField().getText());
        String layout = modulDataWizardPanel.getLayoutComboBox().getSelectedItem().toString();
        sysModul.setIdLayout(layout.substring(0, layout.indexOf("-")).trim());
        sysModul.setClassName(modulDataWizardPanel.getClassNameTextField().getText());
        sysModul.setBean(DataGeneratorUtility.getPackageName(new File(bean)) + "." + bean.substring(bean.lastIndexOf("\\") + 1, bean.lastIndexOf(".")));
        sysModul.setSqlMap(modulDataWizardPanel.getSqlMapTextField().getText().substring(0, modulDataWizardPanel.getSqlMapTextField().getText().indexOf(".")));
        sysModul.setxPosition(0);
        sysModul.setyPosition(0);
        sysModul.setWidth(Integer.valueOf(modulDataWizardPanel.getWidthTextField().getText()));
        sysModul.setHeight(Integer.valueOf(modulDataWizardPanel.getHeightTextField().getText()));
        sysModul.setRunCode(modulDataWizardPanel.getRunCodeTextField().getText());
        sysModul.setType(modulDataWizardPanel.getTypeComboBox().getSelectedIndex());
        sysModul.setLibrary(modulDataWizardPanel.getLibraryComboBox().getSelectedIndex());
        sysModul.setChanges(modulDataWizardPanel.getChangesCheckBox().isSelected());
        sysModul.setDialogs(modulDataWizardPanel.getShowDialogsCheckBox().isSelected());
        sysModul.setAutoId(modulDataWizardPanel.getAutoIdCheckBox().isSelected());
        sysModul.setInsertBtn(modulDataWizardPanel.getInsertButtonCheckBox().isSelected());
        sysModul.setCopyBtn(modulDataWizardPanel.getCopyButtonCheckBox().isSelected());
        sysModul.setDeleteBtn(modulDataWizardPanel.getDeleteButtonCheckBox().isSelected());
        sysModul.setPrintBtn(modulDataWizardPanel.getPrintButtonCheckBox().isSelected());
        sysModul.setChartBtn(modulDataWizardPanel.getChartButtonCheckBox().isSelected());
        sysModul.setShortcutsBtn(modulDataWizardPanel.getShortcutsButtonCheckBox().isSelected());
    }

    public void createMasterFields(String table) {
        int index = 0;
        fields = new TreeMap<String, SysField>();
        SortedMap<String, ColumnMetaData> columnMetaData = DataGeneratorUtility.getColumnsMetaData(table);
        for (Iterator<String> it = columnMetaData.keySet().iterator(); it.hasNext(); ) {
            index++;
            SysField sysField = getField(table, columnMetaData.get(it.next()), index);
            fields.put(sysField.getIdField(), sysField);
        }
    }

    public void createDetailFields(String table) {
        int index = 0;
        tableFields = new TreeMap<String, SysTableField>();
        SortedMap<String, ColumnMetaData> columnMetaData = DataGeneratorUtility.getColumnsMetaData(table);
        for (Iterator<String> it = columnMetaData.keySet().iterator(); it.hasNext(); ) {
            index++;
            SysTableField sysTableField = convertFieldToTableField(getField(table, columnMetaData.get(it.next()), index));
            tableFields.put(sysTableField.getIdField(), sysTableField);
        }
    }

    public void createTable() {
        sysTable = new SysTable();
        sysTable.setIdModul(sysModul.getIdModul());
        sysTable.setIdTable(modulDataWizardPanel.getIdTableTextField().getText());
        String bean = tablesWizardPanel.getDetailBeanTextField().getText();
        sysTable.setBean(DataGeneratorUtility.getPackageName(new File(bean)) + "." + bean.substring(bean.lastIndexOf("\\") + 1, bean.lastIndexOf(".")));
        sysTable.setxPosition(Integer.valueOf(modulDataWizardPanel.getXPositionTableTextField().getText()));
        sysTable.setyPosition(Integer.valueOf(modulDataWizardPanel.getYPositionTableTextField().getText()));
        sysTable.setWidth(Integer.valueOf(modulDataWizardPanel.getWidthTableTextField().getText()));
        sysTable.setHeight(Integer.valueOf(modulDataWizardPanel.getHeightTableTextField().getText()));
        sysTable.setFilterBtn(true);
        sysTable.setDetailBtn(modulDataWizardPanel.getButtonDetailsCheckBox().isSelected());
        sysTable.setInsertBtn(modulDataWizardPanel.getButtonInsertCheckBox().isSelected());
        sysTable.setDeleteBtn(modulDataWizardPanel.getButtonDeleteCheckBox().isSelected());
        sysTable.setFrameWidth(Integer.valueOf(modulDataWizardPanel.getWidthFrameTextField().getText()));
        sysTable.setFrameHeight(Integer.valueOf(modulDataWizardPanel.getHeightFrameTextField().getText()));
        sysTable.setMandatory(false);
        sysTable.setIdTabpane(ID_TABPANE);
        sysTable.setNumTabpane("01");
        sysTable.setClassName("-");
    }

    public void createTabpanes(ListModel model) {
        tabpanes = new TreeMap<String, SysTabpane>();
        for (int i = 0; i < model.getSize(); i++) {
            SysTabpane sysTabpane = new SysTabpane();
            sysTabpane.setIdModul(sysModul.getIdModul());
            sysTabpane.setIdTabpane(ID_TABPANE);
            sysTabpane.setNumTabpane(StringUtility.lpad(String.valueOf((i + 1)), "0", 2));
            sysTabpane.setCaption((String) model.getElementAt(i));
            sysTabpane.setIdTable("0");
            sysTabpane.setDetail(false);
            tabpanes.put(sysTabpane.getIdTabpane() + sysTabpane.getNumTabpane(), sysTabpane);
        }
    }

    public void createForeignKeys(HashMap<String, DefaultListModel> map) {
        int index = 1;
        fkFields = new TreeMap<String, SysFkfield>();
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
            SysFkfield sysFkfield = new SysFkfield();
            SysField sysField = DataGeneratorUtility.getFieldByCaption(fields, it.next());
            if (sysField != null) {
                sysFkfield.setIdField(sysField.getIdField());
                sysFkfield.setIdFkfield(StringUtility.lpad(String.valueOf(index * 10), "0", 5));
                sysFkfield.setIdModul(sysField.getIdModul());
                sysFkfield.setIdTable("0");
                sysFkfield.setxPosition(sysField.getxPosition());
                sysFkfield.setyPosition(sysField.getyPosition() + sysField.getWidth());
                sysFkfield.setWidth(10);
                sysFkfield.setHeight(1);
                sysFkfield.setCaption(null);
                sysFkfield.setCaptionXPosition(null);
                sysFkfield.setCaptionYPosition(null);
                sysFkfield.setCaptionWidth(null);
                sysFkfield.setCaptionHeight(null);
                sysFkfield.setEditable(true);
                sysFkfield.setFocusable(true);
                sysFkfield.setShowButton(true);
            }
            index++;
            fkFields.put(sysFkfield.getIdFkfield(), sysFkfield);
        }
    }

    public void createComboItems(HashMap<String, DefaultListModel> map) {
        int index = 1;
        SysComboItems sysComboItems = null;
        comboItems = new TreeMap<String, SysComboItems>();
        if (map != null) {
            for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                DefaultListModel model = map.get(key);
                SysField sysField = DataGeneratorUtility.getFieldByCaption(fields, key);
                for (int i = 0; i < model.getSize(); i++) {
                    if (sysField != null) {
                        sysComboItems = new SysComboItems();
                        sysComboItems.setIdField(sysField.getIdField());
                        sysComboItems.setIdItem(StringUtility.lpad(String.valueOf(index * 10), "0", 5));
                        sysComboItems.setIdModul(sysField.getIdModul());
                        sysComboItems.setIdTable("0");
                        sysComboItems.setName((String) model.get(i));
                        sysComboItems.setIndex(i - 1);
                        comboItems.put(sysComboItems.getIdField() + sysComboItems.getIdItem(), sysComboItems);
                    }
                }
                index++;
            }
        }
    }

    public void save() {
        DataGenerator.getInstance().getServiceContainer().getSystemService().insertGeneratedData(sysModul, fields, tabpanes, sysTable, tableFields, fkFields, comboItems);
    }

    private SysField getField(String table, ColumnMetaData columnMetaData, int index) {
        SysField sysField = new SysField();
        List<String> primaryKeyList = DataGeneratorUtility.getPrimaryKeyInfo(table);
        List<String> indexList = DataGeneratorUtility.getIndexInfo(table);
        List<ForeignKeyInfo> foreignKeyList = DataGeneratorUtility.getForeignKeyInfo(table);
        sysField.setIdModul(sysModul.getIdModul());
        sysField.setBean(sysModul.getBean());
        sysField.setIdField(StringUtility.lpad(String.valueOf(index * 10), "0", 5));
        sysField.setOrdinalPosition(index);
        if (columnMetaData.getType() == PostgreSQLType.BOOL) {
            sysField.setType(FieldType.FIELD_TYPE_BOOLEAN.getType());
            sysField.setClassName(ClassNameType.THREESTATE_CHECKBOX_CLASS_NAME.getName());
            sysField.setWidth(2);
            sysField.setLength(0);
        } else if (columnMetaData.getType() == PostgreSQLType.DATE) {
            sysField.setType(FieldType.FIELD_TYPE_DATE.getType());
            sysField.setClassName(ClassNameType.ADVANCEDDATEFIELD_CLASS_NAME.getName());
            sysField.setWidth(6);
            sysField.setLength(10);
        } else if (columnMetaData.getType() == PostgreSQLType.INT4) {
            sysField.setType(FieldType.FIELD_TYPE_INTEGER.getType());
            sysField.setClassName(ClassNameType.ADVANCEDTEXTFIELD_CLASS_NAME.getName());
            sysField.setWidth(5);
            sysField.setLength(5);
        } else if (columnMetaData.getType() == PostgreSQLType.INT8) {
            sysField.setType(FieldType.FIELD_TYPE_LONG.getType());
            sysField.setClassName(ClassNameType.ADVANCEDTEXTFIELD_CLASS_NAME.getName());
            sysField.setWidth(5);
            sysField.setLength(8);
        } else if (columnMetaData.getType() == PostgreSQLType.BIG_DECIMAL) {
            sysField.setType(FieldType.FIELD_TYPE_BIGDECIMAL.getType());
            sysField.setClassName(ClassNameType.ADVANCEDTEXTFIELD_CLASS_NAME.getName());
            sysField.setWidth(5);
            sysField.setLength(15);
        } else if (columnMetaData.getType() == PostgreSQLType.TEXT && columnMetaData.getDisplaySize() > 1000) {
            sysField.setType(FieldType.FIELD_TYPE_STRING.getType());
            sysField.setClassName(ClassNameType.JTEXTAREA_CLASS_NAME.getName());
            sysField.setLength(-1);
            sysField.setWidth(10);
        } else if (columnMetaData.getType() == PostgreSQLType.TIMESTAMP) {
            sysField.setType(FieldType.FIELD_TYPE_DATE.getType());
            sysField.setClassName(ClassNameType.ADVANCEDTIMESTAMPFIELD_CLASS_NAME.getName());
            sysField.setWidth(6);
            sysField.setLength(10);
        } else if (columnMetaData.getType() == PostgreSQLType.VARCHAR) {
            sysField.setType(FieldType.FIELD_TYPE_STRING.getType());
            sysField.setClassName(ClassNameType.ADVANCEDTEXTFIELD_CLASS_NAME.getName());
            sysField.setLength(columnMetaData.getDisplaySize());
            if (columnMetaData.getDisplaySize() <= 5) sysField.setWidth(4); else if (columnMetaData.getDisplaySize() >= 5 && columnMetaData.getDisplaySize() <= 10) sysField.setWidth(5); else if (columnMetaData.getDisplaySize() >= 10 && columnMetaData.getDisplaySize() <= 20) sysField.setWidth(8); else if (columnMetaData.getDisplaySize() >= 20 && columnMetaData.getDisplaySize() <= 40) sysField.setWidth(10); else if (columnMetaData.getDisplaySize() >= 40 && columnMetaData.getDisplaySize() <= 200) sysField.setWidth(15); else sysField.setWidth(2);
        }
        sysField.setHeight(1);
        sysField.setxPosition(index * 2);
        sysField.setyPosition(14);
        sysField.setEditable(true);
        sysField.setFocusable(true);
        sysField.setMandatory(columnMetaData.isMandatory());
        sysField.setUniqueKey(false);
        for (String column : indexList) {
            if (column.equalsIgnoreCase(columnMetaData.getName())) {
                sysField.setUniqueKey(true);
                break;
            }
        }
        sysField.setPrimaryKey(false);
        for (String column : primaryKeyList) {
            if (column.equalsIgnoreCase(columnMetaData.getName())) {
                sysField.setPrimaryKey(true);
                sysField.setEditable(false);
                sysField.setFocusable(false);
                break;
            }
        }
        sysField.setForeignKey(false);
        for (ForeignKeyInfo foreignKeyInfo : foreignKeyList) {
            if (foreignKeyInfo.getForeignKeyColumn().equalsIgnoreCase(columnMetaData.getName())) {
                sysField.setForeignKey(true);
                sysField.setClassName(ClassNameType.FOREIGNKEYFIELD_CLASS_NAME.getName());
                sysField.setFkBean(foreignKeyInfo.getPrimaryKeyTable());
                sysField.setFkFrameWidth(500);
                sysField.setFkFrameHeight(300);
                break;
            }
        }
        sysField.setVisible(true);
        sysField.setVisibleInTable(true);
        sysField.setCaption(columnMetaData.getName());
        sysField.setCaptionXPosition(index * 2);
        sysField.setCaptionYPosition(2);
        sysField.setCaptionWidth(11);
        sysField.setCaptionHeight(1);
        for (int i = 0; i < modulPropertyBindingWizardPanel.getBindingTable().getBeanTableModel().size(); i++) {
            ColumnPropertyBinding bind = (ColumnPropertyBinding) modulPropertyBindingWizardPanel.getBindingTable().getBeanTableModel().get(i);
            if (bind.getColumn().equalsIgnoreCase(columnMetaData.getName())) {
                String get = "get" + bind.getProperty().substring(0, 1).toUpperCase() + bind.getProperty().substring(1);
                String set = "set" + bind.getProperty().substring(0, 1).toUpperCase() + bind.getProperty().substring(1);
                sysField.setProperty(bind.getProperty() + "|" + get + "|" + set);
                break;
            }
        }
        return sysField;
    }

    private SysTableField convertFieldToTableField(SysField field) {
        SysTableField tableField = new SysTableField();
        tableField.setIdField(field.getIdField());
        tableField.setIdModul(field.getIdModul());
        tableField.setIdTable(sysTable.getIdTable());
        tableField.setOrdinalPosition(field.getOrdinalPosition());
        tableField.setClassName(field.getClassName());
        tableField.setType(field.getType());
        tableField.setxPosition(field.getxPosition());
        tableField.setyPosition(field.getyPosition());
        tableField.setWidth(field.getWidth());
        tableField.setHeight(field.getHeight());
        tableField.setMandatory(field.getMandatory());
        tableField.setUniqueKey(field.getUniqueKey());
        tableField.setPrimaryKey(field.getPrimaryKey());
        tableField.setForeignKey(field.getForeignKey());
        tableField.setLength(field.getLength());
        tableField.setVisible(field.getVisible());
        tableField.setVisibleInTable(field.getVisibleInTable());
        tableField.setCaption(field.getCaption());
        tableField.setCaptionXPosition(field.getCaptionXPosition());
        tableField.setCaptionYPosition(field.getCaptionYPosition());
        tableField.setCaptionWidth(field.getCaptionWidth());
        tableField.setCaptionHeight(field.getCaptionHeight());
        tableField.setBean(field.getBean());
        tableField.setProperty("-");
        tableField.setIdTabpane(field.getIdTabpane());
        tableField.setNumTabpane(field.getNumTabpane());
        tableField.setDefaultValue(field.getDefaultValue());
        tableField.setHelp(field.getHelp());
        tableField.setPrecision(field.getPrecision());
        tableField.setEditable(field.getEditable());
        tableField.setFocusable(field.getFocusable());
        tableField.setFkBean(field.getFkBean());
        tableField.setFkFrameWidth(field.getFkFrameWidth());
        tableField.setFkFrameHeight(field.getFkFrameHeight());
        return tableField;
    }

    public SysModul getSysModul() {
        return sysModul;
    }

    public SortedMap<String, SysField> getFields() {
        return fields;
    }

    public SortedMap<String, SysFkfield> getFkFields() {
        return fkFields;
    }

    public SortedMap<String, SysComboItems> getComboItems() {
        return comboItems;
    }

    public SortedMap<String, SysTabpane> getTabpanes() {
        return tabpanes;
    }

    public TablesWizardPanel getTablesWizardPanel() {
        return tablesWizardPanel;
    }

    public void setTablesWizardPanel(TablesWizardPanel tablesWizardPanel) {
        this.tablesWizardPanel = tablesWizardPanel;
    }

    public ModulDataWizardPanel getModulDataWizardPanel() {
        return modulDataWizardPanel;
    }

    public void setModulDataWizardPanel(ModulDataWizardPanel modulDataWizardPanel) {
        this.modulDataWizardPanel = modulDataWizardPanel;
    }

    public ModulPropertyBinding getModulPropertyBindingWizardPanel() {
        return modulPropertyBindingWizardPanel;
    }

    public void setModulPropertyBindingWizardPanel(ModulPropertyBinding modulPropertyBindingWizardPanel) {
        this.modulPropertyBindingWizardPanel = modulPropertyBindingWizardPanel;
    }
}
