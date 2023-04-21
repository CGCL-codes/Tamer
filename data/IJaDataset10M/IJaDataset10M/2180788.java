package GUI.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * TableSorter is a decorator for TableModels; adding sorting
 * functionality to a supplied RuleTableModel. TableSorter does
 * not store or copy the data in its RuleTableModel; instead it maintains
 * a map from the row indexes of the view to the row indexes of the
 * model. As requests are made of the sorter (like getValueAt(row, col))
 * they are passed to the underlying model after the row numbers
 * have been translated via the internal mapping array. This way,
 * the TableSorter appears to hold another copy of the table
 * with the rows in a different order.
 * <p/>
 * TableSorter registers itself as a listener to the underlying model,
 * just as the JTable itself would. Events recieved from the model
 * are examined, sometimes manipulated (typically widened), and then
 * passed on to the TableSorter's listeners (typically the JTable).
 * If a change to the model has invalidated the order of TableSorter's
 * rows, a note of this is made and the sorter will resort the
 * rows the next time a value is requested.
 * <p/>
 * When the tableHeader property is set, either by using the
 * setTableHeader() method or the two argument constructor, the
 * table header may be used as a complete UI for TableSorter.
 * The default renderer of the tableHeader is decorated with a renderer
 * that indicates the sorting status of each column. In addition,
 * a mouse listener is installed with the following behavior:
 * <ul>
 * <li>
 * Mouse-click: Clears the sorting status of all other columns
 * and advances the sorting status of that column through three
 * values: {NOT_SORTED, ASCENDING, DESCENDING} (then back to
 * NOT_SORTED again).
 * <li>
 * SHIFT-mouse-click: Clears the sorting status of all other columns
 * and cycles the sorting status of the column through the same
 * three values, in the opposite order: {NOT_SORTED, DESCENDING, ASCENDING}.
 * <li>
 * CONTROL-mouse-click and CONTROL-SHIFT-mouse-click: as above except
 * that the changes to the column do not cancel the statuses of columns
 * that are already sorting - giving a way to initiate a compound
 * sort.
 * </ul>
 * <p/>
 * This is a long overdue rewrite of a class of the same name that
 * first appeared in the swing table demos in 1997.
 * 
 * 
 * @author Philip Milne
 * @author Brendon McLean
 * @author Dan van Enckevort
 * @author Parwinder Sekhon
 * @version 2.0 02/27/04
 */
public class TableSorter extends AbstractTableModel {

    /** The table model for this table sorter. */
    protected AbstractTableModel tableModel;

    /** A variable indicating to sort descending. */
    public static final int DESCENDING = -1;

    /** A variable indicating not to sort. */
    public static final int NOT_SORTED = 0;

    /** A variable indicating to sort ascending. */
    public static final int ASCENDING = 1;

    /** A variable which sets the default sorting directive. */
    private static Directive EMPTY_DIRECTIVE = new Directive(-1, NOT_SORTED);

    /** The comparator to compare two comparable objects. */
    public static final Comparator COMPARABLE_COMAPRATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };

    /** the comparator to compare two objects based on their lexical representation. */
    public static final Comparator LEXICAL_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    };

    /** An array of rows. */
    private Row[] viewToModel;

    /** An array of int's. */
    private int[] modelToView;

    /** The heading of the table to be sorted. */
    private JTableHeader tableHeader;

    /** A mouse listener, it is notified when the user clicks on a table header. */
    private MouseListener mouseListener;

    /** A table model listener. */
    private TableModelListener tableModelListener;

    /** A hashmap of column comparators. */
    private Map columnComparators = new HashMap();

    /** An arraylist of sorting columns. */
    private List sortingColumns = new ArrayList();

    /**
     * Generates a new table sorter.
     */
    public TableSorter() {
        this.mouseListener = new MouseHandler();
        this.tableModelListener = new TableModelHandler();
    }

    /**
     * Generates a table sorter for the given table model.
     * @param tableModel The table model of this table sorter.
     */
    public TableSorter(AbstractTableModel tableModel) {
        this();
        setTableModel(tableModel);
    }

    /**
     * Clear the sorting state.
     */
    private void clearSortingState() {
        viewToModel = null;
        modelToView = null;
    }

    /**
     * Get the table model.
     * @return The table model of this table sorter.
     */
    public AbstractTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Set the table model of this table sorter.
     * @param tableModel The new table model.
     */
    public void setTableModel(AbstractTableModel tableModel) {
        if (this.tableModel != null) {
            this.tableModel.removeTableModelListener(tableModelListener);
        }
        this.tableModel = tableModel;
        if (this.tableModel != null) {
            this.tableModel.addTableModelListener(tableModelListener);
        }
        clearSortingState();
        fireTableStructureChanged();
    }

    /**
     * Get the table header of this table sorter.
     * @return The table header.
     */
    public JTableHeader getTableHeader() {
        return tableHeader;
    }

    /**
     * Set the table header of this table sorter.
     * @param tableHeader the new table header.
     */
    public void setTableHeader(JTableHeader tableHeader) {
        if (this.tableHeader != null) {
            this.tableHeader.removeMouseListener(mouseListener);
            TableCellRenderer defaultRenderer = this.tableHeader.getDefaultRenderer();
            if (defaultRenderer instanceof SortableHeaderRenderer) {
                this.tableHeader.setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
            }
        }
        this.tableHeader = tableHeader;
        if (this.tableHeader != null) {
            this.tableHeader.addMouseListener(mouseListener);
            this.tableHeader.setDefaultRenderer(new SortableHeaderRenderer(this.tableHeader.getDefaultRenderer()));
        }
    }

    /**
     * Check if this table sorter has actually sorted its table model.
     * @return A boolean indicating if the table model of this table sorter is sorted.
     */
    public boolean isSorting() {
        return sortingColumns.size() != 0;
    }

    /**
     * Get the sort direction of the given column.
     * @param column The column number.
     * @return The sort direction of the given column.
     */
    private Directive getDirective(int column) {
        for (int i = 0; i < sortingColumns.size(); i++) {
            Directive directive = (Directive) sortingColumns.get(i);
            if (directive.column == column) {
                return directive;
            }
        }
        return EMPTY_DIRECTIVE;
    }

    /**
     * Get the sorting status of the given column.
     * @param column The column number.
     * @return The sorting status of the given column.
     */
    public int getSortingStatus(int column) {
        return getDirective(column).direction;
    }

    /**
     * This method is executed when the sorting status has changed. It fires a 
     * tableDatachanged event and redraws the table header if there is one.
     */
    private void sortingStatusChanged() {
        clearSortingState();
        fireTableDataChanged();
        if (tableHeader != null) {
            tableHeader.repaint();
        }
    }

    /**
     * Set the sorting status of a given column.
     * @param column The column.
     * @param status The new status.
     */
    public void setSortingStatus(int column, int status) {
        Directive directive = getDirective(column);
        if (directive != EMPTY_DIRECTIVE) {
            sortingColumns.remove(directive);
        }
        if (status != NOT_SORTED) {
            sortingColumns.add(new Directive(column, status));
        }
        sortingStatusChanged();
    }

    /**
     * Get the icon of a given column which should be placed in the header.
     * @param column The column.
     * @param size The size of the icon.
     * @return The icon.
     */
    protected Icon getHeaderRendererIcon(int column, int size) {
        Directive directive = getDirective(column);
        if (directive == EMPTY_DIRECTIVE) {
            return null;
        }
        return new Arrow(directive.direction == DESCENDING, size, sortingColumns.indexOf(directive));
    }

    /**
     * Cancel all sorting.
     */
    private void cancelSorting() {
        sortingColumns.clear();
        sortingStatusChanged();
    }

    /**
     * Set the comparator for a given class.
     * @param type The class for which the comparator is set.
     * @param comparator The comparator.
     */
    public void setColumnComparator(Class type, Comparator comparator) {
        if (comparator == null) {
            columnComparators.remove(type);
        } else {
            columnComparators.put(type, comparator);
        }
    }

    /**
     * Get the comparator for a given column.
     * @param column The column.
     * @return The comparator for the given column.
     */
    protected Comparator getComparator(int column) {
        Class columnType = tableModel.getColumnClass(column);
        Comparator comparator = (Comparator) columnComparators.get(columnType);
        if (comparator != null) {
            return comparator;
        }
        if (Comparable.class.isAssignableFrom(columnType)) {
            return COMPARABLE_COMAPRATOR;
        }
        return LEXICAL_COMPARATOR;
    }

    /**
     * Thet the view of the sorted data in an array of rows.
     * @return The row array.
     */
    private Row[] getViewToModel() {
        if (viewToModel == null) {
            int tableModelRowCount = tableModel.getRowCount();
            viewToModel = new Row[tableModelRowCount];
            for (int row = 0; row < tableModelRowCount; row++) {
                viewToModel[row] = new Row(row);
            }
            if (isSorting()) {
                Arrays.sort(viewToModel);
            }
        }
        return viewToModel;
    }

    /**
     * Get the model index for a specific view index.
     * @param viewIndex The view index.
     * @return the model index.
     */
    public int modelIndex(int viewIndex) {
        return getViewToModel()[viewIndex].modelIndex;
    }

    /**
     * Convert the model to a view.
     * @return An arrayof view indices.
     */
    private int[] getModelToView() {
        if (modelToView == null) {
            int n = getViewToModel().length;
            modelToView = new int[n];
            for (int i = 0; i < n; i++) {
                modelToView[modelIndex(i)] = i;
            }
        }
        return modelToView;
    }

    /**
     * Get the number of rows.
     * @return The number of rows.
     */
    public int getRowCount() {
        return (tableModel == null) ? 0 : tableModel.getRowCount();
    }

    /**
     * Get the number of columns.
     * @return The number of columns.
     */
    public int getColumnCount() {
        return (tableModel == null) ? 0 : tableModel.getColumnCount();
    }

    /**
     * Get the name of a given column.
     * @param column The column number.
     * @return The name of the column.
     */
    public String getColumnName(int column) {
        return tableModel.getColumnName(column);
    }

    /**
     * Get the class type of a given column.
     * @param column The column number.
     * @return The class type of the column.
     */
    public Class getColumnClass(int column) {
        return tableModel.getColumnClass(column);
    }

    /**
     * Check if the given cell is editable.
     * @param row The row number.
     * @param column The column number.
     * @return A boolean indicating if this cell is editable.
     */
    public boolean isCellEditable(int row, int column) {
        return tableModel.isCellEditable(modelIndex(row), column);
    }

    /**
     * Get the value of the given cell.
     * @param row The row number.
     * @param column The column number.
     * @return The value of the given cell.
     */
    public Object getValueAt(int row, int column) {
        return tableModel.getValueAt(modelIndex(row), column);
    }

    /**
     * Set the valule of the given cell.
     * @param row The row number.
     * @param column The column number.
     * @param aValue The new value.
     */
    public void setValueAt(Object aValue, int row, int column) {
        tableModel.setValueAt(aValue, modelIndex(row), column);
    }

    /**
     * A comparalbe row.
     */
    private class Row implements Comparable {

        /** The model index. */
        private int modelIndex;

        /**
         * Generates a new Row for the given model index.
         * @param index The index of this row in the model.
         */
        public Row(int index) {
            this.modelIndex = index;
        }

        /**
         * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
         * </p>
         * In the foregoing description, the notation sgn(expression) designates the mathematical signum function, which is defined to return one of -1, 0, or 1 according to whether the value of expression is negative, zero or positive. The implementor must ensure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y. (This implies that x.compareTo(y) must throw an exception iff y.compareTo(x) throws an exception.)
         * </p>
         * The implementor must also ensure that the relation is transitive: (x.compareTo(y)>0 && y.compareTo(z)>0) implies x.compareTo(z)>0.
         * </p>
         * Finally, the implementer must ensure that x.compareTo(y)==0 implies that sgn(x.compareTo(z)) == sgn(y.compareTo(z)), for all z.
         * </p>
         * It is strongly recommended, but not strictly required that (x.compareTo(y)==0) == (x.equals(y)). Generally speaking, any class that implements the Comparable interface and violates this condition should clearly indicate this fact. The recommended language is "Note: this class has a natural ordering that is inconsistent with equals." 
         *
         * @param o The Object to be compared.
         * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
         */
        public int compareTo(Object o) {
            int row1 = modelIndex;
            int row2 = ((Row) o).modelIndex;
            for (Iterator it = sortingColumns.iterator(); it.hasNext(); ) {
                Directive directive = (Directive) it.next();
                int column = directive.column;
                Object o1 = tableModel.getValueAt(row1, column);
                Object o2 = tableModel.getValueAt(row2, column);
                int comparison = 0;
                if (o1 == null && o2 == null) {
                    comparison = 0;
                } else if (o1 == null) {
                    comparison = -1;
                } else if (o2 == null) {
                    comparison = 1;
                } else {
                    comparison = getComparator(column).compare(o1, o2);
                }
                if (comparison != 0) {
                    return directive.direction == DESCENDING ? -comparison : comparison;
                }
            }
            return 0;
        }
    }

    /**
     * The handler of the table model, it implements the TableModelListener.
     */
    private class TableModelHandler implements TableModelListener {

        /**
         * This fine grain notification tells listeners the exact range of cells, rows, or columns that changed.
         * @param e The TableModelEvent.
         */
        public void tableChanged(TableModelEvent e) {
            if (!isSorting()) {
                clearSortingState();
                fireTableChanged(e);
                return;
            }
            if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                cancelSorting();
                fireTableChanged(e);
                return;
            }
            int column = e.getColumn();
            if (e.getFirstRow() == e.getLastRow() && column != TableModelEvent.ALL_COLUMNS && getSortingStatus(column) == NOT_SORTED && modelToView != null) {
                int viewIndex = getModelToView()[e.getFirstRow()];
                fireTableChanged(new TableModelEvent(TableSorter.this, viewIndex, viewIndex, column, e.getType()));
                return;
            }
            clearSortingState();
            fireTableDataChanged();
            return;
        }
    }

    /**
     * The mouse handler.
     */
    private class MouseHandler extends MouseAdapter {

        /**
         * Invoked when the mouse has been clicked on a table header.
         * @param e The MouseEvent.
         */
        public void mouseClicked(MouseEvent e) {
            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = columnModel.getColumnIndexAtX(e.getX());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            if (column != -1) {
                int status = getSortingStatus(column);
                if (!e.isControlDown()) {
                    cancelSorting();
                }
                status = status + (e.isShiftDown() ? -1 : 1);
                status = (status + 4) % 3 - 1;
                setSortingStatus(column, status);
            }
        }
    }

    /**
     * The arrow which is used as an icon on the column headers that are sorted.
     */
    private static class Arrow implements Icon {

        /** A boolean indicating if te arrow is descending. */
        private boolean descending;

        /** The size of the arrow. */
        private int size;

        /** The priority of the arrow. */
        private int priority;

        /**
         * Genereates a new arrow.
         * @param descending A boolean indicating if te arrow is descending.
         * @param size The size of the arrow.
         * @param priority The priority of the arrow.
         */
        public Arrow(boolean descending, int size, int priority) {
            this.descending = descending;
            this.size = size;
            this.priority = priority;
        }

        /**
         * Paint the icon to a component on a specific place.
         * @param c The component to which the icon needs to be painted.
         * @param g The graphics object on which the icon needs to be drawn.
         * @param x The x location.
         * @param y The y location.
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color color = c == null ? Color.GRAY : c.getBackground();
            int dx = (int) (size / 2 * Math.pow(0.8, priority));
            int dy = descending ? dx : -dx;
            y = y + 5 * size / 6 + (descending ? -dy : 0);
            int shift = descending ? 1 : -1;
            g.translate(x, y);
            g.setColor(color.darker());
            g.drawLine(dx / 2, dy, 0, 0);
            g.drawLine(dx / 2, dy + shift, 0, shift);
            g.setColor(color.brighter());
            g.drawLine(dx / 2, dy, dx, 0);
            g.drawLine(dx / 2, dy + shift, dx, shift);
            if (descending) {
                g.setColor(color.darker().darker());
            } else {
                g.setColor(color.brighter().brighter());
            }
            g.drawLine(dx, 0, 0, 0);
            g.setColor(color);
            g.translate(-x, -y);
        }

        /**
         * Get the width of the icon.
         * @return The width of the icon.
         */
        public int getIconWidth() {
            return size;
        }

        /**
         * Get the height of the icon.
         * @return The height of the icon.
         */
        public int getIconHeight() {
            return size;
        }
    }

    /**
     * The renderer of the sortable table header. It implements the TableCellRenderer 
     * interface.
     */
    private class SortableHeaderRenderer implements TableCellRenderer {

        /** the TableCellRenderer. */
        private TableCellRenderer tableCellRenderer;

        /**
         * Generate a sortable table header renderer.
         * @param tableCellRenderer The table cell renderer to be used.
         */
        public SortableHeaderRenderer(TableCellRenderer tableCellRenderer) {
            this.tableCellRenderer = tableCellRenderer;
        }

        /**
         * Get the table cell renderer component.
         * @param table The JTable.
         * @param value The vale for which a renderer has to be created.
         * @param isSelected A boolean indicating if the cell is selected.
         * @param hasFocus A boolean indicating if the cell has focus.
         * @param row The row number.
         * @param column The column number.
         * @return The table cell renderer component.
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setHorizontalTextPosition(JLabel.LEFT);
                int modelColumn = table.convertColumnIndexToModel(column);
                l.setIcon(getHeaderRendererIcon(modelColumn, l.getFont().getSize()));
            }
            return c;
        }
    }

    /**
     * The directive of the sort.
     */
    private static class Directive {

        /** The column number. */
        private int column;

        /** The sort direction. */
        private int direction;

        /**
         * Generates a new sort directive.
         * @param column The column number.
         * @param direction The sort direction.
         */
        public Directive(int column, int direction) {
            this.column = column;
            this.direction = direction;
        }
    }
}
