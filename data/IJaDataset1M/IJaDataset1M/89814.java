package uchicago.src.sim.engine.gui.components;

import java.awt.event.KeyEvent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 * This table, unlike the regular JTable out-of-the-box will:
 *         - support multiple row deletion with delete key
 *  - support cell's content deletion with delete key
 *  - end cell editing with Enter key
 *  - stop cell editing on loss of focus
 *
 * @author wes maciorowski
 *  Created on Sep 25, 2003
 *
 */
public class EnhancedJTable extends JTable {

    /** Source code revision. */
    public static final String revision = "$Revision: 1.3 $";

    EnhancedTableModel anEnhancedTableModel;

    /**
     * Default constructor
     *
     * Sep 2, 2004
     * @param anEnhancedTableModel
     * @param selectedColumnWidth
     */
    public EnhancedJTable(final EnhancedTableModel anEnhancedTableModel, int selectedColumnWidth) {
        super(anEnhancedTableModel);
        this.anEnhancedTableModel = anEnhancedTableModel;
        this.anEnhancedTableModel.setEnhancedJTable(this);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                    int[] selRow = getSelectedRows();
                    if (selRow.length >= 1) {
                        for (int i = selRow.length - 1; i >= 0; i--) {
                            anEnhancedTableModel.removeRow(i);
                        }
                    } else if (selRow.length == 1) {
                        anEnhancedTableModel.setValueAt(null, selRow[0], getSelectedColumn());
                    }
                    anEnhancedTableModel.fireTableDataChanged();
                } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    TableCellEditor editor = getCellEditor();
                    if (isEditing() && (editor != null)) {
                        editor.stopCellEditing();
                        anEnhancedTableModel.fireTableDataChanged();
                    }
                } else {
                    super.keyPressed(evt);
                }
            }
        });
        this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        fixColumnSizing(this, selectedColumnWidth);
    }

    /**
     * Sets all columns of aTable to provided colWidth width.
     * @param aTable
     * @param colWidth
     */
    protected void fixColumnSizing(int col, int colWidth) {
        TableColumn aTableColumn2 = null;
        aTableColumn2 = getColumnModel().getColumn(col);
        if (aTableColumn2.getPreferredWidth() < (colWidth + 10)) {
            aTableColumn2.setPreferredWidth(colWidth + 10);
        }
    }

    /**
     * Sets all columns of aTable to provided colWidth width.
     * @param aTable
     * @param colWidth
     */
    private void fixColumnSizing(JTable aTable, int colWidth) {
        aTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn aTableColumn2 = null;
        for (int l = 0; l < aTable.getColumnModel().getColumnCount(); l++) {
            aTableColumn2 = aTable.getColumnModel().getColumn(l);
            aTableColumn2.setPreferredWidth(colWidth);
        }
    }
}
