package org.vizzini.ui;

import org.vizzini.ui.table.TableSorter;
import org.vizzini.ui.table.TableStyle;
import org.vizzini.ui.text.TextColorSet;
import org.vizzini.ui.text.TextStyle;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Provides a <code>JTable</code> which has a table sorter installed between it
 * and it's table model. A mouse listener is added to the header, which causes a
 * sort to occur. A special header renderer is used to indicate the sort
 * direction.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class SortTable extends JTable {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SortTable.class.getName());

    /** The table sorter. Keep a reference for efficiency. */
    protected TableSorter _tableSorter;

    /** Alternate row background color. */
    private Color _alternateRowColor = TableStyle.DEFAULT_ALTERNATE_ROW_COLOR;

    /** Table header renderer. */
    private TableHeaderRenderer _headerRenderer;

    /** Flag indicating whether to use alternating row colors. */
    private boolean _isAlternatingRowColors = false;

    /**
     * Construct this object.
     *
     * @since  v0.2
     */
    public SortTable() {
        super();
        init();
    }

    /**
     * Construct this object.
     *
     * @param  tableModel  Table model.
     *
     * @since  v0.2
     */
    public SortTable(TableModel tableModel) {
        super(tableModel);
        init();
    }

    /**
     * Apply the given text style to this table's header.
     *
     * @param  textStyle  Text style.
     *
     * @since  v0.2
     */
    public void applyHeaderTextStyle(TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("textStyle == null");
        }
        setHeaderFont(textStyle.getFont());
        TextColorSet set = textStyle.getNormalTextColorSet();
        setHeaderForeground(set.getForeground());
        setHeaderBackground(set.getBackground());
    }

    /**
     * Apply the given table style to this table.
     *
     * @param  tableStyle  Table style.
     *
     * @since  v0.2
     */
    public void applyTableStyle(TableStyle tableStyle) {
        if (tableStyle == null) {
            throw new IllegalArgumentException("tableStyle == null");
        }
        setAlternateRowColor(tableStyle.getAlternateRowColor());
        setGridColor(tableStyle.getGridColor());
        applyHeaderTextStyle(tableStyle.getHeaderTextStyle());
        applyTextStyle(tableStyle.getTextStyle());
    }

    /**
     * Apply the given text style to this table.
     *
     * @param  textStyle  Text style.
     *
     * @since  v0.2
     */
    public void applyTextStyle(TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("textStyle == null");
        }
        setFont(textStyle.getFont());
        TextColorSet set = textStyle.getNormalTextColorSet();
        setForeground(set.getForeground());
        setBackground(set.getBackground());
        Container parent = getParent();
        if (parent instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) parent;
            scrollPane.getViewport().setBackground(set.getBackground());
        }
        set = textStyle.getSelectionTextColorSet();
        setSelectionForeground(set.getForeground());
        setSelectionBackground(set.getBackground());
    }

    /**
     * Derive a text style from this table's header's current settings.
     *
     * @return  the style.
     *
     * @since   v0.2
     */
    public TextStyle deriveHeaderTextStyle() {
        TextStyle answer = new TextStyle();
        answer.setFont(getHeaderFont());
        TextColorSet set = new TextColorSet(getHeaderForeground(), getHeaderBackground());
        answer.setNormalTextColorSet(set);
        return answer;
    }

    /**
     * Derive a table style from this table's current settings.
     *
     * @return  the style.
     *
     * @since   v0.2
     */
    public TableStyle deriveTableStyle() {
        TableStyle answer = new TableStyle();
        answer.setHeaderTextStyle(deriveHeaderTextStyle());
        answer.setTextStyle(deriveTextStyle());
        answer.setGridColor(getGridColor());
        answer.setAlternateRowColor(_alternateRowColor);
        return answer;
    }

    /**
     * Derive a text style from this table's current settings.
     *
     * @return  the style.
     *
     * @since   v0.2
     */
    public TextStyle deriveTextStyle() {
        TextStyle answer = new TextStyle();
        answer.setFont(getFont());
        TextColorSet set = new TextColorSet(getForeground(), getHeaderBackground());
        answer.setNormalTextColorSet(set);
        set = new TextColorSet(getSelectionForeground(), getSelectionBackground());
        answer.setSelectionTextColorSet(set);
        return answer;
    }

    /**
     * @return  Return alternateRowColor.
     *
     * @since   v0.2
     */
    public Color getAlternateRowColor() {
        return _alternateRowColor;
    }

    /**
     * @return  the actual data model.
     *
     * @since   v0.2
     */
    public TableModel getDataModel() {
        TableModel answer = null;
        if (_tableSorter != null) {
            answer = _tableSorter.getModel();
        }
        return answer;
    }

    /**
     * @return  the header background color.
     *
     * @since   v0.2
     */
    public Color getHeaderBackground() {
        return _headerRenderer.getBackground();
    }

    /**
     * @return  the header font.
     *
     * @since   v0.2
     */
    public Font getHeaderFont() {
        return _headerRenderer.getFont();
    }

    /**
     * @return  the header foreground color.
     *
     * @since   v0.2
     */
    public Color getHeaderForeground() {
        return _headerRenderer.getForeground();
    }

    /**
     * @see  javax.swing.JTable#getSelectedRow()
     */
    @Override
    public int getSelectedRow() {
        int row = super.getSelectedRow();
        return _tableSorter.getSortedRowIndex(row);
    }

    /**
     * @see  javax.swing.JTable#getSelectedRows()
     */
    @Override
    public int[] getSelectedRows() {
        int[] rows = super.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            rows[i] = _tableSorter.getSortedRowIndex(rows[i]);
        }
        return rows;
    }

    /**
     * @return  Return isAlternatingRowColors.
     *
     * @since   v0.2
     */
    public boolean isAlternatingRowColors() {
        return _isAlternatingRowColors;
    }

    /**
     * Extends the super method to alternate the row colors.
     *
     * @param   renderer   Renderer.
     * @param   rowIndex   Row index.
     * @param   vColIndex  Column index.
     *
     * @return  the component.
     *
     * @since   v0.2
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
        if (_isAlternatingRowColors && !isRowSelected(rowIndex)) {
            if (((rowIndex % 2) == 0) && !isCellSelected(rowIndex, vColIndex)) {
                c.setBackground(_alternateRowColor);
            } else {
                c.setBackground(getBackground());
            }
        }
        return c;
    }

    /**
     * @param  alternateRowColor  The alternateRowColor to set.
     *
     * @since  v0.2
     */
    public void setAlternateRowColor(Color alternateRowColor) {
        _alternateRowColor = alternateRowColor;
    }

    /**
     * @param  isAlternatingRowColors  The isAlternatingRowColors to set.
     *
     * @since  v0.2
     */
    public void setAlternatingRowColors(boolean isAlternatingRowColors) {
        _isAlternatingRowColors = isAlternatingRowColors;
    }

    /**
     * @param  tableModel  tableModel to set
     *
     * @since  v0.4
     */
    public void setDataModel(TableModel tableModel) {
        if (_tableSorter != null) {
            _tableSorter.setModel(tableModel);
        }
    }

    /**
     * Set the table font. Extends the super method to adjust the row height for
     * the new font.
     *
     * @param  font  Font.
     *
     * @since  v0.2
     */
    @Override
    public void setFont(Font font) {
        LOGGER.finer("font = " + font);
        Graphics g = getGraphics();
        if (g != null) {
            FontMetrics fm = g.getFontMetrics(font);
            int fontHeight = fm.getHeight();
            setRowHeight(fontHeight);
        }
        super.setFont(font);
    }

    /**
     * Set the header background color.
     *
     * @param  color  Color.
     *
     * @since  v0.2
     */
    public void setHeaderBackground(Color color) {
        LOGGER.finer("color = " + color);
        _headerRenderer.setBackground(color);
        JTableHeader myTableHeader = getTableHeader();
        myTableHeader.resizeAndRepaint();
    }

    /**
     * Set the header font.
     *
     * @param  font  Font.
     *
     * @since  v0.2
     */
    public void setHeaderFont(Font font) {
        LOGGER.finer("font = " + font);
        _headerRenderer.setFont(font);
        JTableHeader myTableHeader = getTableHeader();
        myTableHeader.resizeAndRepaint();
    }

    /**
     * Set the header foreground color.
     *
     * @param  color  Color.
     *
     * @since  v0.2
     */
    public void setHeaderForeground(Color color) {
        LOGGER.finer("color = " + color);
        _headerRenderer.setForeground(color);
        JTableHeader myTableHeader = getTableHeader();
        myTableHeader.resizeAndRepaint();
    }

    /**
     * Set the selected rows, translated through the sort.
     *
     * @param  rows  Rows.
     *
     * @since  v0.2
     */
    public void setSelectedRows(int[] rows) {
        ListSelectionModel mySelectionModel = getSelectionModel();
        for (int i = 0; i < rows.length; i++) {
            int row = _tableSorter.getDesortedRowIndex(rows[i]);
            mySelectionModel.addSelectionInterval(row, row);
        }
    }

    /**
     * Sort the table.
     *
     * @since  v0.2
     */
    public void sort() {
        _tableSorter.sort(this);
    }

    /**
     * Initialize.
     *
     * @since  v0.2
     */
    private void init() {
        _tableSorter = new TableSorter(getModel());
        setModel(_tableSorter);
        _tableSorter.addMouseListenerToHeaderInTable(this);
        _headerRenderer = new TableHeaderRenderer();
        JTableHeader myTableHeader = getTableHeader();
        myTableHeader.setDefaultRenderer(_headerRenderer);
    }

    /**
     * This class is a renderer for the header of the sort table. It provides a
     * sort direction icon.
     *
     * @author   Jeffrey M. Thompson
     * @version  v0.4
     * @since    v0.2
     */
    public class TableHeaderRenderer extends JLabel implements TableCellRenderer {

        /** Serial version UID. */
        private static final long serialVersionUID = 1L;

        /** Down icon. */
        private final Icon _ascendingIcon;

        /** Up icon. */
        private final Icon _descendingIcon;

        /**
         * Construct this object.
         *
         * @since  v0.2
         */
        public TableHeaderRenderer() {
            setForeground(Color.black);
            setOpaque(true);
            setBorder(BorderFactory.createRaisedBevelBorder());
            _descendingIcon = ApplicationSupport.getIcon("ICON_sortDescending");
            _ascendingIcon = ApplicationSupport.getIcon("ICON_sortAscending");
            setHorizontalAlignment(CENTER);
            setHorizontalTextPosition(LEFT);
        }

        /**
         * @param   table       The owning table.
         * @param   value       The value for this cell.
         * @param   isSelected  Flag indicating if this cell is selected.
         * @param   hasFocus    Flag indicating if this cell has focus.
         * @param   row         The row of this cell.
         * @param   column      The column of this cell.
         *
         * @return  a component with the proper value and icon.
         *
         * @since   v0.2
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                setText(value.toString());
                setToolTipText(value.toString());
            }
            Boolean ascending = _tableSorter.isColumnSortedAscending(table, column);
            if (ascending == null) {
                setIcon(null);
            } else if (ascending == Boolean.TRUE) {
                setIcon(_ascendingIcon);
            } else {
                setIcon(_descendingIcon);
            }
            return this;
        }
    }
}
