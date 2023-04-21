package net.sf.repairslab.util;

import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class LovCellEditor extends AbstractCellEditor implements TableCellEditor {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private LovChooser component;

    private String query;

    private String colValue;

    private String colLabel;

    private LovResultsBin lrb;

    private Connection con = null;

    public LovCellEditor(LovResultsBin lrb, String query, String colValue, String colLabel, Connection con) {
        this.query = query;
        this.colValue = colValue;
        this.colLabel = colLabel;
        this.con = con;
        this.lrb = lrb;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        component = new LovChooser(lrb, query, colValue, colLabel, row, con);
        component.setBackground(Color.WHITE);
        if (value == null) {
            component.setResults(0, "");
        }
        return component;
    }

    public Object getCellEditorValue() {
        return component.getValue();
    }
}
