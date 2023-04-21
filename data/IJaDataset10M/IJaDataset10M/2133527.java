package org.easygen.ui.modules.hibernate.pages;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.easygen.core.config.DataField;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.db.SQLDataConverter;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.ModulePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author Mektoub Created on 23 mars 07
 *
 */
public class TableSelectorPage extends ModulePage {

    public static final String NAME = "tableSelectorPage";

    private Table tableSelectionTable = null;

    private TableViewer tableViewer;

    private Button selectDeselectButton;

    private List<DataObject> objects;

    private static final String[] columnNames = new String[] { Localization.get("hibernate.label.tableSelector.selected"), Localization.get("hibernate.label.tableSelector.tableName"), Localization.get("hibernate.label.tableSelector.className"), Localization.get("hibernate.label.tableSelector.useCache") };

    /**
	 * @param pName
	 * @param pTitle
	 */
    public TableSelectorPage() {
        super(NAME, Localization.get("hibernate.title.tableSelector"));
    }

    /**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void initControl(Composite pMainComposite) {
        createTableSelectionGroup(pMainComposite);
    }

    /**
	 * @see org.easygen.ui.wizards.pages.ICommonPage#init(org.easygen.core.config.ProjectConfig)
	 */
    @Override
    public void init(ProjectConfig pProjectConfig) {
        objects = pProjectConfig.getObjects();
        tableViewer.setInput(objects);
        setPageComplete(validatePage());
    }

    /**
	 * This method initializes tableSelectionGroup
	 */
    protected void createTableSelectionGroup(Composite pParent) {
        Group tableSelectionGroup = WidgetUtils.createGroup(pParent, Localization.get("hibernate.title.tableSelector.tableSelection"), 1);
        selectDeselectButton = WidgetUtils.createSimpleButton(tableSelectionGroup, Localization.get("hibernate.label.tableSelector.invertSelection"));
        tableSelectionTable = WidgetUtils.createTable(tableSelectionGroup, 200);
        tableSelectionTable.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                TableItem item = (TableItem) e.item;
                if (item != null && item.getData() != null) {
                    DataObject dataObject = (DataObject) item.getData();
                    handleDataObjectForeignKeys(dataObject);
                }
                setPageComplete(validatePage());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        int index = 0;
        WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 20);
        WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 150);
        WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 150);
        WidgetUtils.createTableColumn(tableSelectionTable, columnNames[index++], 100);
        tableViewer = WidgetUtils.createTableViewer(tableSelectionTable, columnNames);
        index = 0;
        CellEditor[] editors = new CellEditor[tableSelectionTable.getColumnCount()];
        editors[index++] = new CheckboxCellEditor(tableSelectionTable);
        editors[index++] = new TextCellEditor(tableSelectionTable);
        editors[index++] = new TextCellEditor(tableSelectionTable);
        editors[index++] = new CheckboxCellEditor(tableSelectionTable);
        tableViewer.setCellEditors(editors);
        TableCellModifier tableCellModifier = new TableCellModifier(tableViewer, columnNames);
        tableViewer.setCellModifier(tableCellModifier);
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(tableCellModifier);
        selectDeselectButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                for (DataObject data : objects) {
                    data.setSelected(!data.isSelected());
                }
                tableViewer.refresh();
                setPageComplete(validatePage());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    /**
	 * @see org.easygen.ui.wizards.pages.CommonPage#validatePage()
	 */
    @Override
    public boolean validatePage() {
        if (!super.validatePage()) return false;
        if (objects == null) {
            String msg = Localization.get("easygen.message.error.field.required", new String[] { "tables" });
            setErrorMessage(msg);
            return false;
        }
        for (DataObject object : objects) {
            if (object.isSelected()) return true;
        }
        return false;
    }

    /**
	 * When a table is selected or unselected, this method update the foreign keys
	 * @param dataObject which state (selected or not) changed
	 */
    private void handleDataObjectForeignKeys(DataObject dataObject) {
        for (DataField field : dataObject.getFields()) {
            if (field.isForeignKey()) {
                DataObject foreignObject = field.getForeignObject();
                if (dataObject.isSelected() == true && field.isForeignList() == false) {
                    DataField newField = new DataField();
                    newField.setForeignKey(true);
                    newField.setForeignList(true);
                    newField.setColumnName(field.getColumnName());
                    newField.setPropertyName(WordUtils.uncapitalize(dataObject.getClassName() + 's'));
                    newField.setPropertyType(dataObject.getClassName());
                    newField.setForeignObject(dataObject);
                    logger.debug("Added: " + newField + " to " + foreignObject);
                    foreignObject.addField(newField);
                } else {
                    Iterator<DataField> foreignFielditerator = foreignObject.getFields().iterator();
                    while (foreignFielditerator.hasNext()) {
                        DataField foreignField = foreignFielditerator.next();
                        if (foreignField.isForeignKey() && foreignField.getColumnName().equals(field.getColumnName())) {
                            if (field.isForeignList() == false && dataObject.isSelected() == false) {
                                logger.debug("Removed: " + foreignField + " from " + foreignObject);
                                foreignFielditerator.remove();
                            } else {
                                if (dataObject.isSelected()) {
                                    foreignField.setForeignKey(true);
                                    foreignField.setPropertyName(SQLDataConverter.convertColumnNameToJavaField(dataObject.getTableName()));
                                    foreignField.setPropertyType(SQLDataConverter.convertTableNameToJavaClass(dataObject.getTableName()));
                                } else {
                                    foreignField.setForeignKey(false);
                                    foreignField.setPropertyName(SQLDataConverter.convertColumnNameToJavaField(foreignField.getColumnName()));
                                    foreignField.setPropertyType(SQLDataConverter.getJavaTypeForSqlType(foreignField));
                                }
                                logger.debug("Updated: " + foreignField + " in " + foreignObject);
                            }
                        }
                    }
                }
            }
        }
    }
}
