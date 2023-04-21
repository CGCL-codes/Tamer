package com.apelon.beans.apeltable;

import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;
import java.awt.Color;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import com.apelon.beans.apeldnd.*;
import com.apelon.common.log4j.Categories;

/**
 * Insert the type's description here.
 * Creation date: (5/11/2000 9:14:10 AM)
 * @author: Christopher Hopkins
 */
public class ApelTable extends ApelDNDPanel {

    private JTable ivjJTable = null;

    private JScrollPane ivjtableScrollPane = null;

    protected ApelTableMgr fApelTableMgr = null;

    private boolean fieldRowsUnique = false;

    private int fieldMaxRows = 0;

    private boolean fRendererInitd = false;

    /**
 * ApelTable constructor comment.
 */
    public ApelTable() {
        super();
        initialize();
    }

    /**
 * ApelTable constructor comment.
 * @param layout java.awt.LayoutManager
 */
    public ApelTable(java.awt.LayoutManager layout) {
        super(layout);
    }

    /**
 * ApelTable constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
    public ApelTable(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
 * ApelTable constructor comment.
 * @param isDoubleBuffered boolean
 */
    public ApelTable(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
   * Call this method to add a row to the ApelTable
   *
   * Creation date: (5/15/2000 12:03:21 PM)
   * @author Chris Hopkins
   * @param newRow com.apelon.beans.apeltable.ApelTableRowObject
   */
    public void addRow(ApelTableRowObject newRow) {
        if (newRow == null) return;
        ApelTableRowObject[] rowObjects = getApelTableModel().getRowObjects();
        if (getRowsUnique()) {
            if (rowObjects != null && rowObjects.length > 0) {
                for (int i = 0; i < rowObjects.length; i++) if (rowObjects[i].equals(newRow)) return;
            }
        }
        if (getMaxRows() > 0) {
            if (rowObjects.length == getMaxRows()) {
                int[] idxs = new int[] { 0 };
                deleteRowsByIndex(idxs, false);
            }
        }
        int length = 0;
        if (rowObjects != null) length = rowObjects.length;
        ApelTableRowObject[] rowobs = new ApelTableRowObject[length + 1];
        for (int i = 0; i < length; i++) rowobs[i] = rowObjects[i];
        rowobs[length] = newRow;
        rowObjects = rowobs;
        getApelTableModel().setRowObjects(rowObjects);
        reDisplay();
    }

    protected void setTableHeader(JTableHeader newTableHeader) {
        getJTable().setTableHeader(newTableHeader);
    }

    /**
   * overrides default functionality to set the background colors for all of the visible children of this
   * component to newColor (not including the JTable or its header)
   *
   * @author: Matt Munz
   */
    public void setBackground(Color newColor) {
        gettableScrollPane().setBackground(newColor);
        gettableScrollPane().getViewport().setBackground(newColor);
    }

    /**
 * Compute a column header's width.
 * Creation date: (6/22/2000 3:46:50 PM)
 * @return int
 * @param col javax.swing.table.TableColumn
 */
    private int columnHeaderWidth(TableColumn col) {
        String clName = getClass().getName();
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = new DefaultTableCellRenderer();
        }
        java.awt.Component comp = renderer.getTableCellRendererComponent(getJTableObject(), col.getHeaderValue(), false, false, -1, 0);
        return comp.getPreferredSize().width;
    }

    /**
   * Call this method to delete any and all selected rows.
   *
   * Creation date: (5/12/2000 9:10:27 AM)
   * @author Chris Hopkins
   */
    protected void deleteRowsByIndex(int[] rowIdxs, boolean reDisplay) {
        if (rowIdxs == null) return;
        ApelTableRowObject[] rowObjects = getApelTableModel().getRowObjects();
        for (int i = rowIdxs.length - 1; i >= 0; i--) {
            rowObjects[rowIdxs[i]] = null;
        }
        Vector v = new Vector();
        for (int j = 0; j < rowObjects.length; j++) {
            if (rowObjects[j] != null) v.add(rowObjects[j]);
        }
        rowObjects = new ApelTableRowObject[v.size()];
        v.copyInto(rowObjects);
        getApelTableModel().setRowObjects(rowObjects);
        if (reDisplay) reDisplay();
    }

    /**
   * Call this method to delete any and all selected rows.
   *
   * Creation date: (5/12/2000 9:10:27 AM)
   * @author Chris Hopkins
   */
    public void deleteSelectedRows() {
        JTable table = getJTable();
        int[] rowIdxs = table.getSelectedRows();
        deleteRowsByIndex(rowIdxs, true);
    }

    /**
   * Insert the method's description here.
   * Creation date: (3/15/00 9:48:51 AM)
   * @param dge java.awt.dnd.DragGestureEvent
   */
    public void dragGestureRecognized(DragGestureEvent dge) {
        JTable table = getJTable();
        int[] rowidxs = table.getSelectedRows();
        if (rowidxs == null || rowidxs.length == 0) return;
        int colIndex = table.getSelectedColumn();
        String colName = table.getColumnName(colIndex);
        ApelTableRowObject[] rowObjects = getApelTableModel().getRowObjects();
        Vector v = new Vector();
        for (int i = 0; i < rowidxs.length; i++) {
            if (rowidxs[i] >= 0 && rowidxs[i] < rowObjects.length) v.addElement(rowObjects[rowidxs[i]]);
        }
        ApelTableRowObject[] rowobs = new ApelTableRowObject[v.size()];
        v.copyInto(rowobs);
        Transferable transOb = fApelTableMgr.getDragObject(rowobs, colName);
        if (transOb != null) {
            ApelDND.initiateDrag(getJTable(), dge, transOb);
            return;
        }
        String dragStr = fApelTableMgr.getDragString(rowobs, colName);
        ApelDND.initiateDrag(getJTable(), dge, dragStr);
    }

    /**
   * Call this method to enable dragging from the table to other components.
   * The string used in the drag will be retrieved from the manager based
   * on the selected rows
   *
   * Creation date: (7/17/2000 2:20:43 PM)
   * @param en boolean
   */
    public void enableDragFromTable(boolean en) {
        if (en) {
            registerDragComponent(getJTable(), this, this);
        } else {
        }
    }

    /**
   * Call this method to retrieve the registered table manager.
   *
   * Creation date: (5/11/2000 9:31:49 AM)
   * @author Chris Hopkins
   * @return com.apelon.beans.apeltable.ApelTableMgr
   */
    public ApelTableMgr getApelTableMgr() {
        return fApelTableMgr;
    }

    public ApelTableModel getModel() {
        return getApelTableModel();
    }

    /**
 * Return the ApelTable's model.
 * Creation date: (8/4/2000 2:10:06 PM)
 * @return com.apelon.beans.apeltable.ApelTableModel
 */
    protected ApelTableModel getApelTableModel() {
        return ((ApelTableModel) getJTable().getModel());
    }

    /**
 * Method generated to support the promotion of the autoResizeMode attribute.
 * @return int
 */
    public int getAutoResizeMode() {
        return getJTable().getAutoResizeMode();
    }

    private static void getBuilderData() {
    }

    private javax.swing.JTable getJTable() {
        if (ivjJTable == null) {
            try {
                ivjJTable = new javax.swing.JTable();
                ivjJTable.setName("JTable");
                gettableScrollPane().setColumnHeaderView(ivjJTable.getTableHeader());
                gettableScrollPane().getViewport().setBackingStoreEnabled(true);
                ivjJTable.setAutoResizeMode(0);
                ivjJTable.setPreferredSize(new java.awt.Dimension(450, 400));
                ivjJTable.setModel(new com.apelon.beans.apeltable.ApelTableModel());
                ivjJTable.setBounds(0, 0, 450, 400);
                ivjJTable.setPreferredScrollableViewportSize(new java.awt.Dimension(432, 382));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJTable;
    }

    /**
   * Call this method to retrieve the JTable object. This is useful for
   * registering the JTable as a drag-n-drop component.
   *
   * Creation date: (5/16/2000 10:00:48 AM)
   * @author Chris Hopkins
   */
    public JTable getJTableObject() {
        return getJTable();
    }

    /**
 * Gets the maxRows property (int) value.
 * @return The maxRows property value.
 * @see #setMaxRows
 */
    public int getMaxRows() {
        return fieldMaxRows;
    }

    /**
 * Compute the preferred width for a column based on the column header and the
 * widest data.
 * Creation date: (6/22/2000 3:43:09 PM)
 * @return int
 * @param col javax.swing.table.TableColumn
 */
    public int getPreferredWidthForColumn(TableColumn col) {
        String clName = getClass().getName();
        int hw = columnHeaderWidth(col);
        int cw = widestCellInColumn(col);
        return 10 + (hw > cw ? hw : cw);
    }

    /**
   * Gets the rowsUnique property (boolean) value. This indicates whether
   * the table forces rows to be unique before adding them or not
   *
   * @return The rowsUnique property value.
   * @see #setRowsUnique
   */
    public boolean getRowsUnique() {
        return fieldRowsUnique;
    }

    /**
   * Call this method to return a two dimensional array of all the data
   * associated with all of the selected rows.
   *
   * Creation date: (5/12/2000 9:11:43 AM)
   * @author Chris Hopkins
   * @return java.lang.String[][]
   */
    public ApelTableRowObject[] getSelectedRowObjects() {
        int[] rowIdxs = getJTable().getSelectedRows();
        if (rowIdxs == null || getApelTableModel().getRowCount() == 0) return null;
        ApelTableRowObject[] data = new ApelTableRowObject[rowIdxs.length];
        for (int i = 0; i < rowIdxs.length; i++) {
            data[i] = getApelTableModel().getRowObjects()[rowIdxs[i]];
        }
        return data;
    }

    /**
 * Method generated to support the promotion of the showHorizontalLines attribute.
 * @return boolean
 */
    public boolean getShowHorizontalLines() {
        return getJTable().getShowHorizontalLines();
    }

    /**
 * Method generated to support the promotion of the showVerticalLines attribute.
 * @return boolean
 */
    public boolean getShowVerticalLines() {
        return getJTable().getShowVerticalLines();
    }

    private javax.swing.JScrollPane gettableScrollPane() {
        if (ivjtableScrollPane == null) {
            try {
                ivjtableScrollPane = new javax.swing.JScrollPane();
                ivjtableScrollPane.setName("tableScrollPane");
                ivjtableScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                ivjtableScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                gettableScrollPane().setViewportView(getJTable());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjtableScrollPane;
    }

    /**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
        Categories.uiView().error("Caught Exception", exception);
    }

    /**
 * Insert the method's description here.
 * Creation date: (10/12/2000 11:06:27 AM)
 */
    protected void initColumnRenderer() {
        if (fRendererInitd) return;
        String[] colNames = fApelTableMgr.getColumnNames();
        if (colNames == null) return;
        for (int i = 0; i < colNames.length; i++) {
            TableColumn col = ivjJTable.getColumn(colNames[i]);
            if (col == null) continue;
            col.setCellRenderer(new ApelTableCellRenderer());
        }
        fRendererInitd = true;
    }

    private void initialize() {
        try {
            setName("ApelTable");
            setLayout(new java.awt.BorderLayout());
            setSize(400, 350);
            add(gettableScrollPane(), "Center");
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
    public static void main(java.lang.String[] args) {
        try {
            JFrame frame = new javax.swing.JFrame();
            ApelTable aApelTable;
            aApelTable = new ApelTable();
            frame.setContentPane(aApelTable);
            frame.setSize(aApelTable.getSize());
            frame.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            frame.setVisible(true);
        } catch (Throwable exception) {
            Categories.uiView().error("Caught Exception", exception);
        }
    }

    /**
   * Call this method to force the table to repaint itself.
   *
   * Creation date: (5/15/2000 1:20:36 PM)
   * @author Chris Hopkins
   */
    protected void reDisplay() {
        sizeTableToFitData();
    }

    /**
   * Call this method to register the table manager for this ApelTable.
   * This will call methods in the table manager to setup the table.
   *
   * Creation date: (5/11/2000 9:31:49 AM)
   * @author Chris Hopkins
   * @param newApelTableMgr com.apelon.beans.apeltable.ApelTableMgr
   */
    public void setApelTableMgr(ApelTableMgr newApelTableMgr) {
        fApelTableMgr = newApelTableMgr;
        fApelTableMgr.setApelTable(this);
        getApelTableModel().setColumnNames(fApelTableMgr.getColumnNames());
        initColumnRenderer();
        setData(fApelTableMgr.getData());
    }

    /**
 * Method generated to support the promotion of the autoResizeMode attribute.
 * @param arg1 int
 */
    public void setAutoResizeMode(int arg1) {
        getJTable().setAutoResizeMode(arg1);
    }

    /**
   * This method is called when we register a table manager. This sets up
   * the initial data for the table.
   *
   * Creation date: (5/11/2000 9:44:12 AM)
   * @author Chris Hopkins
   * @param colNames java.lang.String[]
   */
    void setData(ApelTableRowObject[] data) {
        getApelTableModel().setRowObjects(data);
        reDisplay();
    }

    /**
 * Sets the maxRows property (int) value.
 * @param maxRows The new value for the property.
 * @see #getMaxRows
 */
    public void setMaxRows(int maxRows) {
        fieldMaxRows = maxRows;
    }

    /**
   * Sets the rowsUnique property (boolean) value. Call this method to
   * force the table to ensure rows are unique before adding them.
   *
   * @param rowsUnique The new value for the property.
   * @see #getRowsUnique
   */
    public void setRowsUnique(boolean rowsUnique) {
        fieldRowsUnique = rowsUnique;
    }

    /**
   * Call this method to set the value in the 'col' column to be the 'value'
   * passed.
   *
   * Creation date: (5/15/2000 11:59:01 AM)
   * @author Chris Hopkins
   * @param column int
   * @param value java.lang.String
   */
    public void setSelectedRowsValue(int col, String value) {
        int[] rowIdxs = getJTable().getSelectedRows();
        if (rowIdxs == null) return;
        for (int i = rowIdxs.length - 1; i >= 0; i--) {
            getApelTableModel().setValueAt(value, rowIdxs[i], col);
        }
        reDisplay();
    }

    /**
 * Method generated to support the promotion of the showHorizontalLines attribute.
 * @param arg1 boolean
 */
    public void setShowHorizontalLines(boolean arg1) {
        getJTable().setShowHorizontalLines(arg1);
    }

    /**
 * Method generated to support the promotion of the showVerticalLines attribute.
 * @param arg1 boolean
 */
    public void setShowVerticalLines(boolean arg1) {
        getJTable().setShowVerticalLines(arg1);
    }

    /**
   * Size the ApelTable to fit the data.
   * Creation date: (6/22/2000 5:54:49 PM)
   */
    public void sizeTableToFitData() {
        String clName = getClass().getName();
        java.awt.Rectangle rect;
        JTable table = getJTable();
        int numRows = table.getModel().getRowCount();
        int tableWidth = 0;
        int colWidth = 0;
        TableColumn col = null;
        TableColumnModel tcm = table.getColumnModel();
        for (int c = 0; c < table.getColumnCount(); ++c) {
            col = tcm.getColumn(c);
            colWidth = getPreferredWidthForColumn(col);
            col.setMinWidth(colWidth);
            col.setPreferredWidth(colWidth);
            tableWidth = tableWidth + col.getPreferredWidth() + tcm.getColumnMargin();
        }
        int rowMargin = table.getRowMargin();
        int rowHeight = table.getRowHeight();
        int tableHeight = numRows * (rowHeight + rowMargin);
        java.awt.Dimension newSize = new java.awt.Dimension(tableWidth, tableHeight);
        table.setPreferredSize(newSize);
        table.setPreferredScrollableViewportSize(newSize);
        if (table.getTableHeader() != null) {
            table.getTableHeader().revalidate();
            table.getTableHeader().repaint();
        } else {
            table.revalidate();
            table.repaint();
        }
    }

    /**
 * Compute column data's maximum width.
 * Creation date: (6/22/2000 3:51:57 PM)
 * @return int
 * @param col javax.swing.table.TableColumn
 */
    private int widestCellInColumn(TableColumn col) {
        int c = col.getModelIndex();
        int width = 0;
        int maxw = 0;
        JTable table = getJTableObject();
        for (int r = 0; r < table.getRowCount(); ++r) {
            TableCellRenderer renderer = table.getCellRenderer(r, c);
            java.awt.Component comp = renderer.getTableCellRendererComponent(table, table.getValueAt(r, c), false, false, r, c);
            width = comp.getPreferredSize().width;
            maxw = width > maxw ? width : maxw;
        }
        return maxw;
    }

    public void deleteAllRows(boolean reDisplay) {
        ApelTableRowObject[] rowObjects = getApelTableModel().getRowObjects();
        if ((Object) rowObjects == null) return;
        for (int i = rowObjects.length - 1; i >= 0; i--) {
            rowObjects[i] = null;
        }
        Vector v = new Vector();
        for (int j = 0; j < rowObjects.length; j++) {
            if (rowObjects[j] != null) v.add(rowObjects[j]);
        }
        rowObjects = new ApelTableRowObject[v.size()];
        v.copyInto(rowObjects);
        getApelTableModel().setRowObjects(rowObjects);
        if (reDisplay) reDisplay();
    }
}
