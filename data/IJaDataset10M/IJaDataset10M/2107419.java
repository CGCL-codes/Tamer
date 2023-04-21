package com.rapidminer.gui.attributeeditor;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableCellEditor;

/**
 * A generic collection class for cell editors. This class manages a vector
 * containing the cell editors for each row, i.e. a vector of a vector of cell
 * editors.
 * 
 * @author Ingo Mierswa
 */
public class CellEditors {

    private List<List<TableCellEditor>> cellEditors;

    /** Creates a new cell editor collection. */
    public CellEditors(int size) {
        cellEditors = new ArrayList<List<TableCellEditor>>(size);
        for (int i = 0; i < size; i++) {
            cellEditors.add(new ArrayList<TableCellEditor>());
        }
    }

    /** Adds a new cell editor in the given row. */
    public void add(int row, TableCellEditor editor) {
        cellEditors.get(row).add(editor);
    }

    /** Returns the cell renderer in the given row and column. */
    public TableCellEditor get(int row, int column) {
        return cellEditors.get(row).get(column);
    }

    /** Returns the number of rows. */
    public int getSize() {
        return cellEditors.size();
    }

    /** Returns the size of the i-th row. */
    public int getSize(int i) {
        return cellEditors.get(i).size();
    }
}
