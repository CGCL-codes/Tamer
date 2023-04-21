package org.statcato.spreadsheet;

import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.LookAndFeel;
import java.awt.event.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;

/**
 * A table for row header of a spreadsheet.
 *   
 * @author myau
 * @version %I%, %G%
 * @see SpreadsheetModel
 * @since 1.0
 */
public class RowHeaderTable extends JTable {

    JTable table;

    /**
     * Constructor, given a spreadsheet.  Each row of this table
     * represents a row label of the spreadsheet.
     * 
     * @param table spreadsheet
     */
    public RowHeaderTable(JTable table) {
        super(0, 1);
        this.table = table;
        DefaultTableModel headerData = (DefaultTableModel) getModel();
        int numRows = table.getModel().getRowCount();
        headerData.addRow(new Object[] { "Var" });
        for (int i = 1; i < numRows; i++) {
            headerData.addRow(new Object[] { i });
        }
        LookAndFeel.installColorsAndFont(this, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
        setIntercellSpacing(new Dimension(0, 0));
        Dimension d = getPreferredScrollableViewportSize();
        d.width = getPreferredSize().width;
        setPreferredScrollableViewportSize(d);
        setRowHeight(table.getRowHeight());
        setDefaultRenderer(Object.class, new RowHeaderRenderer(table));
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSelectedCells();
                }
            }
        });
    }

    /**
     * Add a row to this table.
     */
    public void addHeaderRow() {
        DefaultTableModel headerData = (DefaultTableModel) getModel();
        int numRows = headerData.getRowCount();
        headerData.addRow(new Object[] { numRows });
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
     * Deletes selected cell in the spreadsheet.
     */
    private void deleteSelectedCells() {
        ((Spreadsheet) table).deleteSelectedCells();
    }

    /**
     * A renderer for the row header.
     */
    public class RowHeaderRenderer extends DefaultTableCellRenderer {

        protected Border noFocusBorder, focusBorder;

        protected JTable table;

        protected Color Foreground, Background;

        public RowHeaderRenderer(JTable table) {
            this.table = table;
            setOpaque(true);
            Border cell = UIManager.getBorder("TableHeader.cellBorder");
            Border focus = UIManager.getBorder("Table.focusCellHighlightBorder");
            focusBorder = new BorderUIResource.CompoundBorderUIResource(cell, focus);
            Insets i = focus.getBorderInsets(this);
            noFocusBorder = new BorderUIResource.CompoundBorderUIResource(cell, BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right));
            JTableHeader header = table.getTableHeader();
            Foreground = header.getForeground();
            Background = header.getBackground();
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setForeground(Foreground);
            setBackground(Background);
            setFont(header.getFont());
            setBorder(noFocusBorder);
        }

        public void updateUI() {
            super.updateUI();
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    ((Component) e.getSource()).setBackground(Color.white);
                    ((Component) e.getSource()).setForeground(Color.blue);
                }

                public void mouseExited(MouseEvent e) {
                    ((Component) e.getSource()).setBackground(Background);
                    ((Component) e.getSource()).setForeground(Foreground);
                }
            });
            setFont(UIManager.getFont("TableHeader.font"));
            setEnabled(true);
            if (focused) setBorder(focusBorder); else setBorder(noFocusBorder);
            setValue(value);
            return this;
        }
    }
}
