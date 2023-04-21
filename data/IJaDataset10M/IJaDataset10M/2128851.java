package gov.sns.apps.jeri.tools.swing;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Renders the text in a row of <CODE>JTable</CODE> cells a certain color.
 * 
 * @author Chris Fowlkes
 */
public class RowColorTableRenderer extends DefaultTableCellRenderer {

    /**
   * Holds the rows associated with each <CODE>Color</CODE>. The keys will be
   * instances of <CODE>Integer</CODE> reflecting the row number, the values 
   * will be instances of <CODE>Color</CODE>.
   */
    private HashMap rowColors = new HashMap();

    /**
   * Creates a new <CODE>RowColorTableRenderer</CODE>.
   */
    public RowColorTableRenderer() {
    }

    /**
   * Sets the given row numbers to a certain <CODE>Color</CODE>.
   * 
   * @param rowColor The <CODE>Color</CODE> the text should be for the given row numbers.
   * @param rowNumbers The indexes of the rows that should have Text the given <CODE>Color</CODE>.
   */
    public void setRowColor(Color rowColor, int[] rowNumbers) {
        for (int i = 0; i < rowNumbers.length; i++) rowColors.put(new Integer(rowNumbers[i]), rowColor);
    }

    /**
   * Gets the <CODE>Component</CODE> responsible for rendering the requested 
   * cell.
   * 
   * @param table The table the renderer will appear in.
   * @param value The value of the cell to render.
   * @param isSelected <CODE>true</CODE> if the cell is selected, <CODE>false</CODE> if not.
   * @param hasFocus <CODE>true</CODE> if the cell has the focus, <CODE>false</CODE> if not.
   * @param row The row index of the cell to render.
   * @param column The column of the cell to render.
   * @return The <CODE>Component</CODE> responsible for rendering the given cell.
   */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            Color background = (Color) rowColors.get(new Integer(row));
            if (background != null) renderer.setBackground(background); else renderer.setBackground(table.getBackground());
        }
        return renderer;
    }

    /**
   * Clears all previously set row colors.
   */
    public void clearRowColors() {
        rowColors.clear();
    }
}
