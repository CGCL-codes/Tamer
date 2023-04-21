package org.gridbus.broker.gui.view;

import java.util.Vector;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.gridbus.broker.gui.model.DimensionEditorModel;

/**
 * @author Xingchen Chu
 * @version 1.0
 * <code> CellEditableTable </node>
 */
public final class CellEditableTable extends JTable {

    private DimensionEditorModel dimensionModel = null;

    public CellEditableTable() {
        super();
    }

    public CellEditableTable(TableModel arg0) {
        super(arg0);
    }

    public CellEditableTable(TableModel arg0, TableColumnModel arg1) {
        super(arg0, arg1);
    }

    public CellEditableTable(TableModel arg0, TableColumnModel arg1, ListSelectionModel arg2) {
        super(arg0, arg1, arg2);
    }

    public CellEditableTable(int arg0, int arg1) {
        super(arg0, arg1);
    }

    public CellEditableTable(Vector arg0, Vector arg1) {
        super(arg0, arg1);
    }

    public CellEditableTable(Object[][] arg0, Object[] arg1) {
        super(arg0, arg1);
    }

    public CellEditableTable(TableModel tableModel, DimensionEditorModel dimensionModel) {
        this(tableModel);
        this.dimensionModel = dimensionModel;
    }

    public void setDimensionEditorModel(DimensionEditorModel dimensionModel) {
        this.dimensionModel = dimensionModel;
    }

    public DimensionEditorModel getDimensionEditorModel() {
        return dimensionModel;
    }

    public TableCellEditor getCellEditor(int row, int col) {
        TableCellEditor cellEditor = null;
        if (dimensionModel != null) {
            cellEditor = dimensionModel.getEditor(row, col);
        }
        return cellEditor != null ? cellEditor : super.getCellEditor(row, col);
    }
}
