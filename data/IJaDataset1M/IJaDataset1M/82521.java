package testingapplication.table;

import com.softaspects.jsf.component.table.*;
import com.softaspects.jsf.component.table.event.TableValueChangedEvent;
import com.softaspects.jsf.component.table.listener.TableValueChangedListener;
import com.softaspects.jsf.component.listmodel.ListDataModel;
import com.softaspects.jsf.component.listmodel.ListDataModelImpl;
import com.softaspects.jsf.component.label.model.LabelModel;
import com.softaspects.jsf.component.label.model.LabelModelImpl;
import com.softaspects.jsf.renderer.table.cell.object.ObjectListBoxTableCellEditor;
import javax.faces.event.AbortProcessingException;
import java.util.ArrayList;
import java.util.List;

public class TableChangeBean implements TableValueChangedListener {

    public static int ROWS = 5;

    public static String[] COLORS = new String[] { "Black", "Green", "Cyan", "Blue" };

    public static String[] NUMBERS = new String[] { "10", "20", "30", "40" };

    private TableDataModel changeValueDataModel;

    private ColumnModel changeValueColumnModel;

    private TableDataModel changeEditorDataModel;

    private ColumnModel changeEditorColumnModel;

    private TableInterfaceManagerModel changeEditorInterfaceManager;

    private TableDataModel changeEditorModelDataModel;

    private ColumnModel changeEditorModelColumnModel;

    private TableInterfaceManagerModel changeEditorModelInterfaceManager;

    public ListDataModel getColorsEditorDataModel() {
        return getValueEditorDataModel(COLORS, "");
    }

    public ListDataModel getNumbersEditorDataModel(String color) {
        return getValueEditorDataModel(NUMBERS, color + " ");
    }

    public ColumnModel getChangeValueColumnModel() {
        if (changeValueColumnModel == null) {
            changeValueColumnModel = getIDColorColumnModel();
            Column descrColumn = new Column("Description", "Description", 2);
            descrColumn.setPrefferedWidth("200pt");
            changeValueColumnModel.addColumn(descrColumn);
        }
        return changeValueColumnModel;
    }

    public ColumnModel getChangeEditorColumnModel() {
        if (changeEditorColumnModel == null) {
            changeEditorColumnModel = getIDColorsColumnModel();
        }
        return changeEditorColumnModel;
    }

    public ColumnModel getChangeEditorModelColumnModel() {
        if (changeEditorModelColumnModel == null) {
            changeEditorModelColumnModel = getIDColorsColumnModel();
        }
        return changeEditorModelColumnModel;
    }

    public TableDataModel getChangeValueDataModel() {
        if (changeValueDataModel == null) {
            int columnCount = getChangeValueColumnModel().getColumnCount();
            changeValueDataModel = new TableDataModel();
            List vTable = new ArrayList();
            for (int rowId = 0; rowId < ROWS; rowId++) {
                vTable.add(getDataModelRow(rowId, "Description of " + COLORS[rowId % 4]));
            }
            changeValueDataModel.setData(vTable, columnCount);
            makeModelEditable(changeValueDataModel);
        }
        return changeValueDataModel;
    }

    public TableDataModel getChangeEditorDataModel() {
        if (changeEditorDataModel == null) {
            int columnCount = getChangeValueColumnModel().getColumnCount();
            changeEditorDataModel = new TableDataModel();
            List vTable = new ArrayList();
            for (int rowId = 0; rowId < ROWS; rowId++) {
                vTable.add(getDataModelRow(rowId, new Integer(0)));
            }
            changeEditorDataModel.setData(vTable, columnCount);
            makeModelEditable(changeEditorDataModel);
        }
        return changeEditorDataModel;
    }

    public TableDataModel getChangeEditorModelDataModel() {
        if (changeEditorModelDataModel == null) {
            int columnCount = getChangeValueColumnModel().getColumnCount();
            changeEditorModelDataModel = new TableDataModel();
            List vTable = new ArrayList();
            for (int rowId = 0; rowId < ROWS; rowId++) {
                vTable.add(getDataModelRow(rowId, new Integer(0)));
            }
            changeEditorModelDataModel.setData(vTable, columnCount);
            makeModelEditable(changeEditorModelDataModel);
        }
        return changeEditorModelDataModel;
    }

    public TableInterfaceManagerModel getChangeValueInterfaceManager() {
        TableInterfaceManagerModel result = TableDataModelUtil.getIntefaceManagerInstance();
        result.setEditorDataModel(1, getColorsEditorDataModel());
        return result;
    }

    public TableInterfaceManagerModel getChangeEditorInterfaceManager() {
        if (changeEditorInterfaceManager == null) {
            changeEditorInterfaceManager = TableDataModelUtil.getIntefaceManagerInstance();
            changeEditorInterfaceManager.setEditorDataModel(1, getColorsEditorDataModel());
            changeEditorInterfaceManager.setEditorDataModel(2, getNumbersEditorDataModel(COLORS[0]));
        }
        return changeEditorInterfaceManager;
    }

    public TableInterfaceManagerModel getChangeEditorModelInterfaceManager() {
        if (changeEditorModelInterfaceManager == null) {
            changeEditorModelInterfaceManager = TableDataModelUtil.getIntefaceManagerInstance();
            changeEditorModelInterfaceManager.setEditorDataModel(1, getColorsEditorDataModel());
            for (int i = 0; i < ROWS; i++) {
                Integer color = (Integer) changeEditorModelDataModel.getValueAt(i, 0);
                changeEditorModelInterfaceManager.setEditorDataModel(i, 2, getNumbersEditorDataModel(COLORS[color.intValue() % COLORS.length]));
            }
        }
        return changeEditorModelInterfaceManager;
    }

    public void processTableValueChanged(TableValueChangedEvent event) throws AbortProcessingException {
        TableListeners.logListenerCalledMessage(event);
        int row = event.getRow();
        int col = event.getColumn();
        if (col == 1) if (event.getComponent().getId().indexOf("changeEditorTable") >= 0) {
            Integer color = (Integer) changeEditorDataModel.getValueAt(row, col);
            changeEditorInterfaceManager.setEditorDataModel(2, getNumbersEditorDataModel(COLORS[color.intValue()]));
        } else if (event.getComponent().getId().indexOf("changeValueTable") >= 0) {
            Integer color = (Integer) changeValueDataModel.getValueAt(row, col);
            changeValueDataModel.setValueAt("Description of " + COLORS[color.intValue() % COLORS.length], row, 2);
        } else if (event.getComponent().getId().indexOf("changeEditorModelTable") >= 0) {
            Integer color = (Integer) changeEditorModelDataModel.getValueAt(row, col);
            changeEditorModelInterfaceManager.setEditorDataModel(row, 2, getNumbersEditorDataModel(COLORS[color.intValue() % COLORS.length]));
        }
    }

    public static ListDataModel getValueEditorDataModel(String[] data, String add) {
        ListDataModel result = new ListDataModelImpl();
        for (int i = 0; i < data.length; i++) {
            LabelModel labelModel = new LabelModelImpl();
            labelModel.setText(String.valueOf(i));
            labelModel.setHintText(add + data[i]);
            result.addValue(labelModel);
        }
        return result;
    }

    public static ColumnModel getIDColorColumnModel() {
        ColumnModel result = new TableColumnModel();
        Column idColumn = new Column("ID", "ID", 0);
        idColumn.setPrefferedWidth("50pt");
        Column colorColumn = new Column("Color", "Color", 1);
        colorColumn.setPrefferedWidth("100pt");
        colorColumn.setCellEditor(new ObjectListBoxTableCellEditor());
        result.addColumn(idColumn);
        result.addColumn(colorColumn);
        return result;
    }

    public static ColumnModel getIDColorsColumnModel() {
        ColumnModel result = getIDColorColumnModel();
        Column descrColumn = new Column("Description", "Description", 2);
        descrColumn.setPrefferedWidth("100pt");
        descrColumn.setCellEditor(new ObjectListBoxTableCellEditor());
        result.addColumn(descrColumn);
        return result;
    }

    public static List getDataModelRow(int rowId, Object lastCol) {
        List vRow = new ArrayList();
        vRow.add(new Integer(rowId));
        vRow.add(new Integer(rowId % 4));
        vRow.add(lastCol);
        return vRow;
    }

    public static void makeModelEditable(DataModel dataModel) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < 3; j++) {
                dataModel.setCellEditable(i, j, true);
            }
        }
    }
}
