package testingapplication.table;

import com.softaspects.jsf.component.base.ComponentDefinitions;
import com.softaspects.jsf.component.base.event.AjaxEvent;
import com.softaspects.jsf.component.base.listener.AjaxEventListener;
import com.softaspects.jsf.component.label.model.LabelModel;
import com.softaspects.jsf.component.label.model.LabelModelImpl;
import com.softaspects.jsf.component.listmodel.ListDataModel;
import com.softaspects.jsf.component.listmodel.ListDataModelImpl;
import com.softaspects.jsf.component.table.*;
import com.softaspects.jsf.renderer.base.RendererUtils;
import com.softaspects.jsf.renderer.base.RenderingUtils;
import com.softaspects.jsf.renderer.table.cell.booleans.BooleanCheckBoxTableHeaderRenderer;
import com.softaspects.jsf.renderer.table.cell.booleans.BooleanImageTableCellRenderer;
import com.softaspects.jsf.support.renderers.ColorFactory;
import testingapplication.common.BaseAjaxOperationBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewCustomTableModels extends BaseAjaxOperationBean implements AjaxEventListener {

    ColumnModel emptyColumnModel;

    ColumnModel listenersColumnModel;

    ColumnModel percentColumnModel;

    ColumnModel numberColumnModel;

    ColumnModel currencyColumnModel;

    ColumnModel calendarColumnModel;

    ColumnModel headerColumnModel;

    ColumnModel existsImageColumnModel;

    ColumnModel keyboardColumnModel;

    ColumnModel percentExistsInvisibleColumnModel;

    ColumnModel percentCurrencyColumnModel;

    ColumnModel linksColumnModel;

    ColumnModel noSelectionColumnModel;

    ColumnModel selectionColumnModel;

    ColumnModel noHiliteFirstColumnModel;

    ColumnModel noHiliteColumnModel;

    DataModel editableDataModel;

    DataModel stringDataValuesDataModel;

    DataModel fullyEditableDataModel;

    DataModel addRemoveDataModel;

    DataModel linksDataModel;

    DataModel percentCurrencyDataModel;

    DataModel fiveRowsDataModel;

    DataModel rowOperationDataModel;

    DataModel rowOperationFilteredDataModel;

    private String currentScope = "Table";

    private Table findTable() {
        FacesContext context = FacesContext.getCurrentInstance();
        Table table = (Table) context.getViewRoot().findComponent("details:TableAdvancedFeaturesSelectedrowsoperations:tableSelectedRowsOperations:tableSelectedRowsOperationsForm").findComponent("table2");
        if (table == null) table = (Table) context.getViewRoot().findComponent("details:TableAdvancedFeaturesSelectedrowsoperations:tableSelectedRowsOperations:tableSelectedRowsOperationsForm").findComponent("srtable");
        return table;
    }

    public TableInterfaceManager getInterfaceManager() {
        return NewTableDataModelUtil.getIntefaceManagerInstance();
    }

    public TableInterfaceManagerModel getInterfaceManagerModel() {
        return NewTableDataModelUtil.getIntefaceManagerInstanceModel();
    }

    public TableInterfaceManager getKeyboardInterfaceManager() {
        TableInterfaceManager keyboardInterfaceManager = NewTableDataModelUtil.getIntefaceManagerInstance();
        keyboardInterfaceManager.setHeaderUnSortedImage("{pathToImages}table/sortnone.gif");
        keyboardInterfaceManager.setHeaderSortedAscImage("{pathToImages}table/sortasc.gif");
        keyboardInterfaceManager.setHeaderSortedDscImage("{pathToImages}table/sortdsc.gif");
        keyboardInterfaceManager.setKeyUp("Up");
        keyboardInterfaceManager.setKeyDown("Down");
        keyboardInterfaceManager.setKeyLeft("Left");
        keyboardInterfaceManager.setKeyRight("Right");
        keyboardInterfaceManager.setKeySort("Ctrl+Up");
        keyboardInterfaceManager.setKeyNavigationFirst("Ctrl+Home");
        keyboardInterfaceManager.setKeyNavigationLast("Ctrl+End");
        keyboardInterfaceManager.setKeyNavigationNext("Ctrl+Right");
        keyboardInterfaceManager.setKeyNavigationPrev("Ctrl+Left");
        keyboardInterfaceManager.setKeyClick("Ctrl+Down");
        return keyboardInterfaceManager;
    }

    public TableInterfaceManagerModel getKeyboardInterfaceManagerModel() {
        TableInterfaceManagerModel keyboardInterfaceManager = NewTableDataModelUtil.getIntefaceManagerInstanceModel();
        keyboardInterfaceManager.setHeaderUnSortedImage("{pathToImages}table/sortnone.gif");
        keyboardInterfaceManager.setHeaderSortedAscImage("{pathToImages}table/sortasc.gif");
        keyboardInterfaceManager.setHeaderSortedDscImage("{pathToImages}table/sortdsc.gif");
        keyboardInterfaceManager.setKeyUp("Up");
        keyboardInterfaceManager.setKeyDown("Down");
        keyboardInterfaceManager.setKeyLeft("Left");
        keyboardInterfaceManager.setKeyRight("Right");
        keyboardInterfaceManager.setKeySort("Ctrl+Up");
        keyboardInterfaceManager.setKeyNavigationFirst("Ctrl+Home");
        keyboardInterfaceManager.setKeyNavigationLast("Ctrl+End");
        keyboardInterfaceManager.setKeyNavigationNext("Ctrl+Right");
        keyboardInterfaceManager.setKeyNavigationPrev("Ctrl+Left");
        keyboardInterfaceManager.setKeyClick("Ctrl+Down");
        return keyboardInterfaceManager;
    }

    public TableInterfaceManagerModel getEmptyInterfaceManagerModel() {
        TableInterfaceManagerModel emptyInterfaceManager = new TableInterfaceManagerModel();
        emptyInterfaceManager.setCellSelectedStyleClass("simpleSelectedStyleClass");
        emptyInterfaceManager.setCellFocusedStyleClass("simpleFocusedStyleClass ");
        emptyInterfaceManager.setHeaderDefaultStyleClass("simpleDefaultStyleClass");
        return emptyInterfaceManager;
    }

    public TableInterfaceManager getPopupMenuInterfaceManager() {
        TableInterfaceManager popupMenuInterfaceManager = NewTableDataModelUtil.getIntefaceManagerInstance();
        popupMenuInterfaceManager.setCellJScriptFunctionOnClick("onTableCellClick");
        return popupMenuInterfaceManager;
    }

    public TableInterfaceManagerModel getNonNavigatorInterfaceManagerModel() {
        TableInterfaceManagerModel nonNavigatorInterfaceManager = NewTableDataModelUtil.getIntefaceManagerInstanceModel();
        nonNavigatorInterfaceManager.setNavigationBarPosition(RendererUtils.NAVIGATION_POSITION_NONE);
        return nonNavigatorInterfaceManager;
    }

    public TableInterfaceManager getIMDefaultNoHilite() {
        TableInterfaceManager managerInstance = new TableInterfaceManager();
        managerInstance.setNavigationBarPosition(RendererUtils.NAVIGATION_POSITION_NONE);
        managerInstance.setCellAltBackground(ColorFactory.convertColorFromHtmlToInt("#EEEEEE"));
        managerInstance.setCellSelectedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        managerInstance.setHeaderDefaultBackground(ColorFactory.convertColorFromHtmlToInt("{skinHardColor}"));
        managerInstance.setHeaderSelectedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        return managerInstance;
    }

    public TableInterfaceManagerModel getIMDefaultNoHiliteModel() {
        TableInterfaceManagerModel managerInstance = new TableInterfaceManagerModel();
        managerInstance.setNavigationBarPosition(RendererUtils.NAVIGATION_POSITION_NONE);
        managerInstance.setCellAltBackground(ColorFactory.convertColorFromHtmlToInt("#EEEEEE"));
        managerInstance.setCellSelectedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        managerInstance.setHeaderDefaultBackground(ColorFactory.convertColorFromHtmlToInt("{skinHardColor}"));
        managerInstance.setHeaderSelectedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        return managerInstance;
    }

    public TableInterfaceManager getIMDefault() {
        TableInterfaceManager managerInstance = getIMDefaultNoHilite();
        managerInstance.setCellFocusedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        managerInstance.setHeaderFocusedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        managerInstance.setCellFocusedBackground(-9263401);
        managerInstance.setHeaderFocusedBackground(-1);
        return managerInstance;
    }

    public TableInterfaceManagerModel getIMDefaultModel() {
        TableInterfaceManagerModel managerInstance = getIMDefaultNoHiliteModel();
        managerInstance.setCellFocusedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        managerInstance.setHeaderFocusedBackground(ColorFactory.convertColorFromHtmlToInt("{skinLiteColor}"));
        managerInstance.setCellFocusedBackground(-9263401);
        managerInstance.setHeaderFocusedBackground(-1);
        return managerInstance;
    }

    public TableInterfaceManager getIMVertical() {
        TableInterfaceManager managerInstance = getIMDefaultNoHilite();
        managerInstance.setCellAltType("vertical");
        return managerInstance;
    }

    public TableInterfaceManagerModel getIMVerticalModel() {
        TableInterfaceManagerModel managerInstance = getIMDefaultNoHiliteModel();
        managerInstance.setCellAltType("vertical");
        return managerInstance;
    }

    public TableInterfaceManagerModel getIMCustomModel() {
        TableInterfaceManagerModel managerInstance = getIMDefaultNoHiliteModel();
        managerInstance.setCellAltType("custom");
        return managerInstance;
    }

    public TableInterfaceManagerModel getIMCustomStepModel() {
        TableInterfaceManagerModel managerInstance = getIMCustomModel();
        managerInstance.setCellAltStep(2);
        return managerInstance;
    }

    public TableInterfaceManager getIMDefaultStep() {
        TableInterfaceManager managerInstance = getIMDefaultNoHilite();
        managerInstance.setCellAltStep(2);
        return managerInstance;
    }

    public TableInterfaceManagerModel getIMDefaultStepModel() {
        TableInterfaceManagerModel managerInstance = getIMDefaultNoHiliteModel();
        managerInstance.setCellAltStep(2);
        return managerInstance;
    }

    public TableInterfaceManagerModel getIMCustomBooleanRendererModel() {
        TableInterfaceManagerModel im = getNonNavigatorInterfaceManagerModel();
        ListDataModel rendererDM = new ListDataModelImpl();
        LabelModel first = new LabelModelImpl();
        first.setDefaultImage("images/table/trace_1.gif");
        rendererDM.addValue(first);
        LabelModel second = new LabelModelImpl();
        second.setDefaultImage("images/table/trace_2.gif");
        rendererDM.addValue(second);
        for (int i = 0; i < 5; i++) {
            im.setRendererDataModel(i, 4, rendererDM);
        }
        return im;
    }

    public ColumnModel getListenersColumnModel() {
        if (listenersColumnModel == null) {
            listenersColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            listenersColumnModel.findColumn(2).setTableCellSelectedAction(false);
            listenersColumnModel.findColumn(3).setTableCellSelectedAction(false);
            listenersColumnModel.findColumn(2).setTableCellDblClickedAction(false);
        }
        return listenersColumnModel;
    }

    public ColumnModel getEmptyColumnModel() {
        if (emptyColumnModel == null) {
            emptyColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            emptyColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setVisible(false);
            emptyColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setColumnClass(Object.class);
            emptyColumnModel.findColumn(TableDataModelUtil.TIME_COLUMN).setColumnClass(Object.class);
            emptyColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setColumnClass(Object.class);
        }
        return emptyColumnModel;
    }

    public ColumnModel getPercentColumnModel() {
        if (percentColumnModel == null) {
            percentColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            percentColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setVisible(false);
            percentColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setColumnClass(ColumnsClass.Percent.class);
            percentColumnModel.findColumn(TableDataModelUtil.TIME_COLUMN).setColumnClass(ColumnsClass.Date.class);
            percentColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setColumnClass(Boolean.class);
        }
        return percentColumnModel;
    }

    public ColumnModel getNumberColumnModel() {
        if (numberColumnModel == null) {
            numberColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            numberColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setVisible(false);
            numberColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setColumnClass(Double.class);
            numberColumnModel.findColumn(TableDataModelUtil.TIME_COLUMN).setColumnClass(ColumnsClass.Time.class);
            numberColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setColumnClass(Boolean.class);
        }
        return numberColumnModel;
    }

    public ColumnModel getCurrencyColumnModel() {
        if (currencyColumnModel == null) {
            currencyColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            currencyColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setVisible(false);
            currencyColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setColumnClass(ColumnsClass.Currency.class);
            currencyColumnModel.findColumn(TableDataModelUtil.TIME_COLUMN).setColumnClass(ColumnsClass.DateTime.class);
            currencyColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setColumnClass(Boolean.class);
        }
        return currencyColumnModel;
    }

    public ColumnModel getCalendarColumnModel() {
        if (calendarColumnModel == null) {
            calendarColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            calendarColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setVisible(false);
            calendarColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setColumnClass(ColumnsClass.Currency.class);
            calendarColumnModel.findColumn(TableDataModelUtil.TIME_COLUMN).setCellEditor(new CustomCalendarCellEditor());
            calendarColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setColumnClass(Boolean.class);
        }
        return calendarColumnModel;
    }

    public ColumnModel getHeaderColumnModel() {
        if (headerColumnModel == null) {
            headerColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            headerColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setHeaderRenderer(new BooleanCheckBoxTableHeaderRenderer());
        }
        return headerColumnModel;
    }

    public ColumnModel getNoHiliteColumnModel() {
        if (noHiliteColumnModel == null) {
            noHiliteColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            Iterator columns = noHiliteColumnModel.getColumns();
            while (columns.hasNext()) {
                Column column = (Column) columns.next();
                column.setHighLightAllowed(false);
            }
        }
        return noHiliteColumnModel;
    }

    public ColumnModel getNoHiliteFirstColumnModel() {
        if (noHiliteFirstColumnModel == null) {
            noHiliteFirstColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            noHiliteFirstColumnModel.findColumn(NewTableDataModelUtil.ROW_COLUMN).setHighLightAllowed(false);
        }
        return noHiliteFirstColumnModel;
    }

    public ColumnModel getNoSelectionColumnModel() {
        if (noSelectionColumnModel == null) {
            noSelectionColumnModel = NewTableDataModelUtil.getColumnNoSelectionModelInstance();
            noSelectionColumnModel.setColumnSelectionAllowed(false);
            noSelectionColumnModel.setHighLightAllowed(false);
        }
        return noSelectionColumnModel;
    }

    public ColumnModel getSelectionColumnModel() {
        if (selectionColumnModel == null) {
            selectionColumnModel = NewTableDataModelUtil.getColumnNoSelectionModelInstance();
            selectionColumnModel.setHighLightAllowed(false);
        }
        return selectionColumnModel;
    }

    public ColumnModel getLinksColumnModel() {
        if (linksColumnModel == null) {
            linksColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            Column links = new Column("linksColumn", "Links", 5);
            links.setPrefferedWidth("100px");
            links.setColumnClass(ColumnsClass.Link.class);
            links.setAlignMode(ComponentDefinitions.ALIGN_CENTER);
            linksColumnModel.addColumn(links);
        }
        return linksColumnModel;
    }

    public ColumnModel getPercentCurrencyColumnModel() {
        if (percentCurrencyColumnModel == null) {
            percentCurrencyColumnModel = getCurrencyColumnModel();
            Column percent = percentCurrencyColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN);
            percent.setValidationErrorMessage("Percent validation error. Wrong value: %2  Row = %0   Column = %1");
            percent.setVisible(true);
            percentCurrencyColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setValidationErrorMessage("Currency validation error. Wrong value: %2  Row = %0   Column = %1");
            percentCurrencyColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setVisible(false);
        }
        return percentCurrencyColumnModel;
    }

    public ColumnModel getPercentExistsInvisibleColumnModel() {
        if (percentExistsInvisibleColumnModel == null) {
            percentExistsInvisibleColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            percentExistsInvisibleColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setVisible(false);
            percentExistsInvisibleColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setVisible(false);
        }
        return percentExistsInvisibleColumnModel;
    }

    public ColumnModel getKeyboardColumnModel() {
        if (keyboardColumnModel == null) {
            keyboardColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            keyboardColumnModel.findColumn(TableDataModelUtil.ROW_COLUMN).setHotKey("CTRL+1");
            keyboardColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setHotKey("CTRL+2");
            keyboardColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setHotKey("CTRL+3");
            keyboardColumnModel.findColumn(TableDataModelUtil.TIME_COLUMN).setHotKey("CTRL+4");
            keyboardColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setHotKey("CTRL+5");
            keyboardColumnModel.findColumn(TableDataModelUtil.ROW_COLUMN).setSortSupported(true);
            keyboardColumnModel.findColumn(TableDataModelUtil.PRECENT_COLUMN).setSortSupported(true);
            keyboardColumnModel.findColumn(TableDataModelUtil.PRICE_COLUMN).setSortSupported(true);
            keyboardColumnModel.findColumn(TableDataModelUtil.TIME_COLUMN).setSortSupported(true);
            keyboardColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setSortSupported(true);
        }
        return keyboardColumnModel;
    }

    public ColumnModel getExistsImageColumnModel() {
        if (existsImageColumnModel == null) {
            existsImageColumnModel = NewTableDataModelUtil.getColumnModelInstance();
            existsImageColumnModel.findColumn(TableDataModelUtil.EXISTS_COLUMN).setCellRenderer(new BooleanImageTableCellRenderer());
        }
        return existsImageColumnModel;
    }

    public DataModel getDefaultEditableDataModel() {
        if (editableDataModel == null) {
            editableDataModel = NewTableDataModelUtil.getDataModelnstance(5);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < getEmptyColumnModel().getColumnCount(); j++) {
                    editableDataModel.setCellEditable(i, j, true);
                }
            }
        }
        return editableDataModel;
    }

    public DataModel getStringDataValuesDataModel() {
        if (stringDataValuesDataModel == null) {
            stringDataValuesDataModel = NewTableDataModelUtil.getDataModelnstance(5, true);
            for (int i = 0; i < stringDataValuesDataModel.getRowCount(); i++) {
                stringDataValuesDataModel.setCellEditable(i, 3, true);
            }
        }
        return stringDataValuesDataModel;
    }

    public DataModel getFullyEditableDataModel() {
        if (fullyEditableDataModel == null) {
            fullyEditableDataModel = NewTableDataModelUtil.getEditableDataModelnstance(5);
        }
        return fullyEditableDataModel;
    }

    public DataModel getAddRemoveDataModel() {
        if (addRemoveDataModel == null) {
            addRemoveDataModel = NewTableDataModelUtil.getEditableDataModelnstance(5);
        }
        return addRemoveDataModel;
    }

    public DataModel getPercentCurrencyDataModel() {
        if (percentCurrencyDataModel == null) {
            percentCurrencyDataModel = NewTableDataModelUtil.getDataModelnstance(5);
            for (int i = 0; i < 5; i++) {
                percentCurrencyDataModel.setCellEditable(i, 1, true);
                percentCurrencyDataModel.setCellEditable(i, 2, true);
            }
        }
        return percentCurrencyDataModel;
    }

    public HeaderModel getHeaderHideModel() {
        return NewTableDataModelUtil.getHeaderInstanceModel();
    }

    public HeaderModel getHeaderNoHilightModel() {
        HeaderModel header = NewTableDataModelUtil.getHeaderInstanceModel();
        header.setHighLightAllowed(false);
        return header;
    }

    public DataModel getLinksDataModel() {
        if (linksDataModel == null) {
            linksDataModel = NewTableDataModelUtil.getLinksDataModelInstance();
        }
        return linksDataModel;
    }

    public DataModel getFiveRowsDataModel() {
        if (fiveRowsDataModel == null) {
            fiveRowsDataModel = NewTableDataModelUtil.getDataModelnstance(5);
        }
        return fiveRowsDataModel;
    }

    public DataModel getRowOperationsDataModel() {
        if (rowOperationDataModel == null) {
            rowOperationDataModel = NewTableDataModelUtil.getDataModelnstance(60);
        }
        if (rowOperationFilteredDataModel != null) {
            return rowOperationFilteredDataModel;
        } else {
            return rowOperationDataModel;
        }
    }

    public String showSelectedRows() {
        Table table = findTable();
        int[] rows = table.getSelectedRows();
        rowOperationFilteredDataModel = new TableDataModel();
        List data = new ArrayList();
        for (int i = 0; i < rows.length; i++) {
            List row = table.getTableDataModel().getRow(i);
            data.add(row);
        }
        rowOperationFilteredDataModel.setData(data, table.getColumnCount());
        table.setTableDataModel(getRowOperationsDataModel());
        table.clearSelection();
        return null;
    }

    public String revertShowSelectedRows() {
        rowOperationFilteredDataModel = null;
        Table table = findTable();
        table.setTableDataModel(getRowOperationsDataModel());
        return null;
    }

    public boolean isFilteredDataModel() {
        return rowOperationFilteredDataModel != null;
    }

    public String getFormClientId() {
        return "details:TableAdvancedFeaturesSelectedrowsoperations:tableSelectedRowsOperations:tableSelectedRowsOperationsForm";
    }

    private void updateScope(AjaxEvent event) {
    }

    public void processAjaxEvent(AjaxEvent event) throws AbortProcessingException {
        if (isOperation(event, "showSelectedRows")) {
            showSelectedRows();
            updateScope(event);
        }
        if (isOperation(event, "revertShowSelectedRows")) {
            revertShowSelectedRows();
            updateScope(event);
        }
        if (isOperation(event, "scopePage")) {
            Table table = findTable();
            table.setRowSelectionScope(Definitions.ROW_SELECTION_SCOPE_PAGE);
            currentScope = "Page";
            try {
                event.getResponse().getWriter().write(RenderingUtils.createHtmlJScriptCommandTimeOut("document.getElementById('" + getFormClientId() + ":scopePage').disabled=true;", "200"));
                event.getResponse().getWriter().write(RenderingUtils.createHtmlJScriptCommandTimeOut("document.getElementById('" + getFormClientId() + ":scopeTable').disabled=false;", "200"));
            } catch (IOException e) {
            }
            updateScope(event);
        }
        if (isOperation(event, "scopeTable")) {
            Table table = findTable();
            table.setRowSelectionScope(Definitions.ROW_SELECTION_SCOPE_TABLE);
            currentScope = "Table";
            updateScope(event);
            try {
                event.getResponse().getWriter().write(RenderingUtils.createHtmlJScriptCommandTimeOut("document.getElementById('" + getFormClientId() + ":scopePage').disabled=false;", "200"));
                event.getResponse().getWriter().write(RenderingUtils.createHtmlJScriptCommandTimeOut("document.getElementById('" + getFormClientId() + ":scopeTable').disabled=true;", "200"));
            } catch (IOException e) {
            }
        }
    }
}
