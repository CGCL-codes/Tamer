package imp.gui;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    public static int ROW_HEADER_WIDTH = 200;

    JTable table;

    ArrayList<String> rowHeaders;

    RowHeaderRenderer(ArrayList<String> rowHeaders, JTable table) {
        this.rowHeaders = rowHeaders;
        this.table = table;
        JTableHeader header = table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(LEFT);
        setForeground(header.getForeground());
        setBackground(StyleCellRenderer.backgroundBassColor);
        setFont(header.getFont());
        setSize(ROW_HEADER_WIDTH, header.getHeight());
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                System.out.println("event" + e);
            }
        });
    }

    public int getNumRows() {
        return rowHeaders.size();
    }

    public String getValue(int index) {
        return rowHeaders.get(index);
    }

    public void setValue(String text, int index) {
        rowHeaders.set(index, text);
        int ncols = table.getColumnCount();
        for (int j = StyleTableModel.FIRST_PATTERN_COLUMN; j < ncols; j++) {
            Object cell = table.getValueAt(index, j);
            if (cell != null & cell instanceof DrumRuleDisplay) {
                ((DrumRuleDisplay) cell).setInstrument(text);
            }
        }
    }

    /**
   * called when the cell is rendered or re-rendered due to some screen change.
  If the index is not that of an instrument name, use a fixed label.
  Otherwise, take take the instrument name from the first column.
  (This should be changed when we can figure out a better way to do it.)
  @param list
  @param value
  @param index
  @param isSelected
  @param cellHasFocus
  @return
   */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (index < rowHeaders.size()) {
            setText(rowHeaders.get(index));
        }
        if (index <= StyleTableModel.BASS_PATTERN_ROW) {
            setBackground(StyleCellRenderer.backgroundBassColor);
        } else if (index <= StyleTableModel.CHORD_PATTERN_ROW) {
            setBackground(StyleCellRenderer.backgroundChordColor);
        } else {
            setBackground(StyleCellRenderer.backgroundDrumColor);
        }
        return this;
    }
}
