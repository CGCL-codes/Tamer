package net.sourceforge.squirrel_sql.fw.gui;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import net.sourceforge.squirrel_sql.fw.resources.LibraryResources;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;
import net.sourceforge.squirrel_sql.fw.datasetviewer.RowNumberTableColumn;
import net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.SquirrelTableCellRenderer;

/**
 * @version 	$Id: ButtonTableHeader.java,v 1.9 2008-05-18 16:13:13 gerdwagner Exp $
 * @author		Johan Compagner
 */
public class ButtonTableHeader extends JTableHeader {

    /** Logger for this class. */
    private static ILogger s_log = LoggerController.createLogger(ButtonTableHeader.class);

    private static final String PREF_KEY_ALWAYS_ADJUST_ALL_COLUMN_HEADERS = "Squirrel.alwaysAdoptAllColumnHeaders";

    /** Icon for "Sorted ascending". */
    private static Icon s_ascIcon;

    /** Icon for "Sorted descending". */
    private static Icon s_descIcon;

    /** Listens for changes in the underlying data. */
    private TableDataListener _dataListener = new TableDataListener();

    /** If <TT>true</TT> then the mouse button is currently pressed. */
    private boolean _pressed;

    /**
    * If <TT>true</TT> then the mouse is being dragged. This is only relevant
    * while the mouse is pressed.
    */
    private boolean _dragged;

    /**
    * if <tt>_pressed</tt> is <tt>true</tt> then this is the physical column
    * that the mouse was pressed in.
    */
    private int _pressedColumnIdx;

    /** Icon for the currently sorted column. */
    private Icon _currentSortedColumnIcon;

    /** Physical (as opposed to model) index of the currently sorted column. */
    private int _currentlySortedColumnIdx = -1;

    static {
        try {
            LibraryResources rsrc = new LibraryResources();
            s_descIcon = rsrc.getIcon(LibraryResources.IImageNames.TABLE_DESCENDING);
            s_ascIcon = rsrc.getIcon(LibraryResources.IImageNames.TABLE_ASCENDING);
        } catch (Exception ex) {
            s_log.error("Error retrieving icons", ex);
        }
    }

    /**
    * Constructor for ButtonTableHeader.
    */
    public ButtonTableHeader() {
        super();
        _pressed = false;
        _dragged = false;
        _pressedColumnIdx = -1;
        setDefaultRenderer(new ButtonTableRenderer(getFont()));
        HeaderListener hl = new HeaderListener();
        addMouseListener(hl);
        addMouseMotionListener(hl);
    }

    public void setTable(JTable table) {
        JTable oldTable = getTable();
        if (oldTable != null) {
            Object obj = oldTable.getModel();
            if (obj instanceof SortableTableModel) {
                SortableTableModel model = (SortableTableModel) obj;
                model.getActualModel().removeTableModelListener(_dataListener);
            }
        }
        super.setTable(table);
        if (table != null) {
            Object obj = table.getModel();
            if (obj instanceof SortableTableModel) {
                SortableTableModel model = (SortableTableModel) obj;
                model.getActualModel().addTableModelListener(_dataListener);
            }
        }
        _currentSortedColumnIcon = null;
        _currentlySortedColumnIdx = -1;
    }

    public String getToolTipText(MouseEvent e) {
        int col = columnAtPoint(e.getPoint());
        String retStr = null;
        if (col >= 0) {
            TableColumn tcol = getColumnModel().getColumn(col);
            int colWidth = tcol.getWidth();
            TableCellRenderer h = tcol.getHeaderRenderer();
            if (h == null) {
                h = getDefaultRenderer();
            }
            Component c = h.getTableCellRendererComponent(table, tcol.getHeaderValue(), false, false, -1, col);
            int prefWidth = c.getPreferredSize().width;
            if (prefWidth > colWidth) {
                retStr = tcol.getHeaderValue().toString();
            }
        }
        return retStr;
    }

    /**
    * @return The currently sorted column index. If no column is sorted -1.
    */
    public int getCurrentlySortedColumnIdx() {
        return _currentlySortedColumnIdx;
    }

    /**
    *
    * @return The direction of the currently sorted column. If no column is sorted false.
    */
    public boolean isAscending() {
        return _currentSortedColumnIcon == s_ascIcon;
    }

    public void columnIndexWillBeRemoved(int colIx) {
        if (colIx < _currentlySortedColumnIdx) {
            --_currentlySortedColumnIdx;
        } else if (colIx == _currentlySortedColumnIdx) {
            _currentlySortedColumnIdx = -1;
        }
    }

    public void columnIndexWillBeAdded(int colIx) {
        if (colIx <= _currentlySortedColumnIdx) {
            ++_currentlySortedColumnIdx;
        }
    }

    public void adjustAllColWidths(boolean includeColHeaders) {
        for (int i = 0; i < getTable().getColumnModel().getColumnCount(); ++i) {
            adjustColWidth(i, includeColHeaders);
        }
    }

    public static boolean isAlwaysAdjustAllColWidths() {
        return Preferences.userRoot().getBoolean(PREF_KEY_ALWAYS_ADJUST_ALL_COLUMN_HEADERS, false);
    }

    public static void setAlwaysAdjustAllColWidths(boolean b) {
        Preferences.userRoot().putBoolean(PREF_KEY_ALWAYS_ADJUST_ALL_COLUMN_HEADERS, b);
    }

    public void initColWidths() {
        if (isAlwaysAdjustAllColWidths()) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    adjustAllColWidths(true);
                }
            });
        }
    }

    private final class TableDataListener implements TableModelListener {

        public void tableChanged(TableModelEvent evt) {
            _currentSortedColumnIcon = null;
            _currentlySortedColumnIdx = -1;
        }
    }

    private void adjustColWidth(int colIx, boolean includeColHeaders) {
        int modelIx = getTable().convertColumnIndexToModel(colIx);
        int rowCount = getTable().getModel().getRowCount();
        int newWidth = 20;
        if (includeColHeaders) {
            TableColumn column = getTable().getColumnModel().getColumn(colIx);
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (null == headerRenderer) {
                headerRenderer = getTable().getTableHeader().getDefaultRenderer();
            }
            Component headerComp = headerRenderer.getTableCellRendererComponent(getTable(), null, false, false, 0, colIx);
            FontMetrics headerFontMetrics = headerComp.getFontMetrics(headerComp.getFont());
            Rectangle2D bounds = headerFontMetrics.getStringBounds("" + column.getHeaderValue(), headerComp.getGraphics());
            newWidth = Math.max(newWidth, bounds.getBounds().width);
        }
        if (0 == rowCount) {
            if (includeColHeaders) {
                getColumnModel().getColumn(colIx).setPreferredWidth(newWidth + 10);
            }
            return;
        }
        TableCellRenderer cellRenderer = getTable().getCellRenderer(0, colIx);
        Component cellComp = cellRenderer.getTableCellRendererComponent(getTable(), null, false, false, rowCount - 1, colIx);
        FontMetrics cellFontMetrics = cellComp.getFontMetrics(cellComp.getFont());
        for (int i = 0; i < rowCount; i++) {
            Object value = getTable().getModel().getValueAt(i, modelIx);
            String stringVal = "";
            if (getTable().getCellRenderer(i, colIx) instanceof SquirrelTableCellRenderer) {
                stringVal += ((SquirrelTableCellRenderer) getTable().getCellRenderer(i, colIx)).renderValue(value);
            } else {
                stringVal += value;
            }
            Rectangle2D bounds = cellFontMetrics.getStringBounds(stringVal, cellComp.getGraphics());
            newWidth = Math.max(newWidth, bounds.getBounds().width);
        }
        getColumnModel().getColumn(colIx).setPreferredWidth(newWidth + 10);
    }

    class HeaderListener extends MouseAdapter implements MouseMotionListener {

        public void mousePressed(MouseEvent e) {
            if (getCursor().getType() == Cursor.E_RESIZE_CURSOR) {
                return;
            }
            _pressed = true;
            if (RowNumberTableColumn.ROW_NUMBER_MODEL_INDEX == table.convertColumnIndexToModel(columnAtPoint(e.getPoint()))) {
                return;
            }
            _pressedColumnIdx = columnAtPoint(e.getPoint());
            repaint();
        }

        public void mouseClicked(MouseEvent e) {
            if (2 == e.getClickCount() && getCursor().getType() == Cursor.E_RESIZE_CURSOR) {
                int colIx = columnAtPoint(e.getPoint());
                Rectangle headerRect = getHeaderRect(colIx);
                int distToColBegin = e.getPoint().x - headerRect.x;
                int distToColEnd = headerRect.x + headerRect.width - e.getPoint().x;
                if (distToColBegin < distToColEnd && 0 < colIx && colIx < getColumnModel().getColumnCount()) {
                    --colIx;
                }
                adjustColWidth(colIx, true);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (getCursor().getType() == Cursor.E_RESIZE_CURSOR) {
                return;
            }
            if (RowNumberTableColumn.ROW_NUMBER_MODEL_INDEX == table.convertColumnIndexToModel(columnAtPoint(e.getPoint()))) {
                _pressed = false;
                _dragged = false;
                return;
            }
            _pressed = false;
            if (!_dragged) {
                _currentSortedColumnIcon = null;
                int column = getTable().convertColumnIndexToModel(_pressedColumnIdx);
                TableModel tm = table.getModel();
                if (column > -1 && column < tm.getColumnCount() && tm instanceof SortableTableModel) {
                    ((SortableTableModel) tm).sortByColumn(column);
                    if (((SortableTableModel) tm).isSortedAscending()) {
                        _currentSortedColumnIcon = s_ascIcon;
                    } else {
                        _currentSortedColumnIcon = s_descIcon;
                    }
                    _currentlySortedColumnIdx = _pressedColumnIdx;
                }
                repaint();
            }
            _dragged = false;
        }

        public void mouseDragged(MouseEvent e) {
            _dragged = true;
            if (_pressed) {
                _currentSortedColumnIcon = null;
                _currentlySortedColumnIdx = -1;
                _pressed = false;
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
            _dragged = false;
        }
    }

    protected class ButtonTableRenderer implements TableCellRenderer {

        JButton _buttonRaised;

        JButton _buttonLowered;

        ButtonTableRenderer(Font font) {
            _buttonRaised = new JButton();
            _buttonRaised.setMargin(new Insets(0, 0, 0, 0));
            _buttonRaised.setFont(font);
            _buttonLowered = new JButton();
            _buttonLowered.setMargin(new Insets(0, 0, 0, 0));
            _buttonLowered.setFont(font);
            _buttonLowered.getModel().setArmed(true);
            _buttonLowered.getModel().setPressed(true);
            _buttonLowered.setMinimumSize(new Dimension(50, 25));
            _buttonRaised.setMinimumSize(new Dimension(50, 25));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value == null) {
                value = "";
            }
            if (_pressedColumnIdx == column && _pressed) {
                _buttonLowered.setText(value.toString());
                if (column == _currentlySortedColumnIdx && _currentSortedColumnIcon != null) {
                    _buttonLowered.setIcon(_currentSortedColumnIcon);
                } else {
                    _buttonLowered.setIcon(null);
                }
                return _buttonLowered;
            }
            _buttonRaised.setText(value.toString());
            if (_currentSortedColumnIcon != null && column == _currentlySortedColumnIdx) {
                _buttonRaised.setIcon(_currentSortedColumnIcon);
            } else {
                _buttonRaised.setIcon(null);
            }
            return _buttonRaised;
        }
    }
}
