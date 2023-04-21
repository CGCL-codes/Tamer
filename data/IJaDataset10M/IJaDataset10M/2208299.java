package net.sourceforge.jsetimon;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/** Will render a component within a table cell as the actual component stored
  * as the value at the location within the table model.  If the value stored
  * in the cell is not a component, an error may occur.
  *
  * @author Adam C Jones
  */
public class TableCellComponentRenderer implements TableCellRenderer {

    /** Called by the UI component to return the component which contains the
     * rendering for the given cell within the table.
     *
     * @param JTable table - the table the cell is in.
     * @param Object value - the value at the row, column in the table.
     * @param boolean isSelected - indicates if the cell is selected.
     * @param boolean hasFocus - indicates if the cell has focus.
     * @param int row - row that the cell is in.
     * @param int column - column that the cell is in.
     * 
     * @return Component - the rendered component for this cell.
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return (Component) table.getModel().getValueAt(row, column);
    }
}
