package com.explosion.utilities;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;

public class RowHeaderUtil {

    public RowHeaderUtil() {
    }

    public static boolean isRowHeaderVisible(JTable table) {
        Container p = table.getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                JViewport rowHeaderViewPort = scrollPane.getRowHeader();
                if (rowHeaderViewPort != null) return rowHeaderViewPort.getView() != null;
            }
        }
        return false;
    }

    /**
     * * Creates row header for table with row number (starting with 1)
     * displayed
     */
    public static void removeRowHeader(JTable table) {
        Container p = table.getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                scrollPane.setRowHeader(null);
            }
        }
    }

    /**
     * * Creates row header for table with row number (starting with 1)
     * displayed
     */
    public void setRowHeader(JTable table, boolean showRowNumber) {
        Container p = table.getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                scrollPane.setRowHeaderView(new TableRowHeader(table, showRowNumber));
                scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, new UpperLeftComponent(table));
            }
        }
    }
}

class TableRowHeader extends JList {

    private JTable table;

    public TableRowHeader(JTable table, boolean showRowNumber) {
        super(new TableRowHeaderModel(table));
        this.table = table;
        this.setBackground(table.getParent().getBackground());
        setFixedCellHeight(table.getRowHeight());
        setFixedCellWidth(preferredHeaderWidth());
        setCellRenderer(new RowHeaderRenderer(table, showRowNumber));
        setSelectionModel(table.getSelectionModel());
    }

    /***************************************************************************
     * * Returns the bounds of the specified range of items in JList
     * coordinates. Returns null if index isn't valid. * *
     * 
     * @param index0 the index of the first JList cell in the range *
     * @param index1 the index of the last JList cell in the range *
     * @return the bounds of the indexed cells in pixels
     */
    public Rectangle getCellBounds(int index0, int index1) {
        Rectangle rect0 = table.getCellRect(index0, 0, true);
        Rectangle rect1 = table.getCellRect(index1, 0, true);
        int y, height;
        if (rect0.y < rect1.y) {
            y = rect0.y;
            height = rect1.y + rect1.height - y;
        } else {
            y = rect1.y;
            height = rect0.y + rect0.height - y;
        }
        return new Rectangle(0, y, getFixedCellWidth(), height);
    }

    private int preferredHeaderWidth() {
        JLabel longestRowLabel = new JLabel("999");
        JTableHeader header = table.getTableHeader();
        longestRowLabel.setBorder(header.getBorder());
        longestRowLabel.setHorizontalAlignment(JLabel.CENTER);
        longestRowLabel.setFont(header.getFont());
        return longestRowLabel.getPreferredSize().width;
    }
}

class TableRowHeaderModel extends AbstractListModel {

    private JTable table;

    public TableRowHeaderModel(JTable table) {
        this.table = table;
    }

    public int getSize() {
        return table.getRowCount();
    }

    public Object getElementAt(int index) {
        return null;
    }
}

class RowHeaderRenderer extends JLabel implements ListCellRenderer {

    private boolean showRowNumber = false;

    private JTable table;

    private Border selectedBorder;

    private Border normalBorder;

    private Font selectedFont;

    private Font normalFont;

    RowHeaderRenderer(JTable table, boolean showRowNumber) {
        this.table = table;
        this.showRowNumber = showRowNumber;
        normalBorder = UIManager.getBorder("TableHeader.cellBorder");
        selectedBorder = BorderFactory.createRaisedBevelBorder();
        JTableHeader header = table.getTableHeader();
        normalFont = header.getFont();
        selectedFont = normalFont.deriveFont(normalFont.getStyle() | Font.BOLD);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setOpaque(true);
        setHorizontalAlignment(CENTER);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (table.getSelectionModel().isSelectedIndex(index)) {
            setFont(selectedFont);
            setBorder(selectedBorder);
        } else {
            setFont(normalFont);
            setBorder(normalBorder);
        }
        String label = String.valueOf(index + 1);
        if (showRowNumber) setText(label); else setText("");
        return this;
    }
}

class UpperLeftComponent extends JLabel {

    public UpperLeftComponent(final JTable table) {
        Border normalBorder = UIManager.getBorder("TableHeader.cellBorder");
        Border selectedBorder = BorderFactory.createRaisedBevelBorder();
        setBorder(normalBorder);
        JTableHeader header = table.getTableHeader();
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        this.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                table.selectAll();
            }
        });
    }
}
