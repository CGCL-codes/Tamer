package com.eteks.jeks;

import com.eteks.parser.CompiledExpression;
import com.eteks.parser.CompilationException;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.event.TableModelEvent;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * A spreadsheet table component. This table use a <code>JeksExpressionParser</code> parser
 * to parse compile and evaluate computed cell. Computed cells are rendered with a
 * <code>JeksCellRenderer</code> cell renderer.
 * If it's editable it uses an instance of <code>JeksCellEditor</code> as editor.
 * An instance of <code>ReferringCellsListener</code> is added to the table model to
 * automaticaly update cells after changes.
 *
 * @version 1.0
 * @author  Emmanuel Puybaret
 * @since   Jeks 1.0
 * @see     com.eteks.jeks.JeksExpressionParser
 * @see     com.eteks.jeks.JeksCellRenderer
 * @see     com.eteks.jeks.JeksCellEditor
 * @see     com.eteks.jeks.ReferringCellsListener
 */
public class JeksTable extends JTable {

    private TableCellEditor editor;

    private JeksExpressionParser expressionParser;

    private ReferringCellsListener cellsListener;

    private int copyRow;

    private int copyColumn;

    private Object[][] copyBuffer;

    private boolean cut;

    /**
   * Creates an editable spreadsheet table with an instance of <code>JeksExpressionParser</code>
   * as parser for computed cells.
   */
    public JeksTable() {
        this(500, 200);
    }

    /**
   * Creates an editable spreadsheet table of <code>rowCount</code> rows and <code>columnCount</code> columns
   * with an instance of <code>JeksExpressionParser</code> as parser for computed cells.
   */
    public JeksTable(int rowCount, int columnCount) {
        this(new JeksTableModel(rowCount, columnCount));
    }

    /**
   * Creates an editable spreadsheet table using the model <code>model</code>
   * and an instance of <code>JeksExpressionParser</code> as parser for computed cells.
   */
    public JeksTable(TableModel model) {
        this(model, new JeksExpressionParser(model), true);
    }

    /**
   * Creates a spreadsheet table.
   */
    public JeksTable(TableModel model, JeksExpressionParser expressionParser, boolean editable) {
        super();
        this.expressionParser = expressionParser;
        setModel(model);
        cellsListener = new ReferringCellsListener();
        model.addTableModelListener(cellsListener);
        if (editable) editor = new JeksCellEditor(expressionParser);
        setDefaultRenderer(JeksExpression.class, new JeksCellRenderer((JeksExpressionSyntax) expressionParser.getSyntax(), expressionParser.getInterpreter()));
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setRowSelectionAllowed(false);
        setCellSelectionEnabled(true);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /**
   * Returns the expression parser used by this table.
   */
    public JeksExpressionParser getExpressionParser() {
        return expressionParser;
    }

    /**
   * Return the referring cells listener added to the model of this table.
   */
    public ReferringCellsListener getReferringCellsListener() {
        return cellsListener;
    }

    /**
   * Cut the selected cells of this table for future paste operations.
   * Copied cells are kept in an internal buffer.
   */
    public void cutSelectedCells() {
        copySelectedCells();
        deleteSelectedCells();
        cut = true;
    }

    /**
   * Copy the selected cells of this table for future paste operations.
   * Copied cells are kept in an internal buffer.
   */
    public void copySelectedCells() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        if (row != -1 && column != -1) {
            int rowCount = getSelectedRowCount();
            int columnCount = getSelectedColumnCount();
            copyBuffer = new Object[rowCount][columnCount];
            copyRow = row;
            copyColumn = column;
            for (int i = 0; i < rowCount; i++) for (int j = 0; j < columnCount; j++) copyBuffer[i][j] = getValueAt(row + i, column + j);
        }
        cut = false;
    }

    /**
   * Deletes the selected cells of this table.
   */
    public void deleteSelectedCells() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        if (row != -1 && column != -1) {
            int rowCount = getSelectedRowCount();
            int columnCount = getSelectedColumnCount();
            getModel().removeTableModelListener(cellsListener);
            for (int i = 0; i < rowCount; i++) for (int j = 0; j < columnCount; j++) setValueAt(null, row + i, column + j);
            Vector updatedSet = new Vector();
            for (int i = 0; i < columnCount; i++) {
                int modelColumn = convertColumnIndexToModel(column + i);
                updatedSet.addElement(new JeksCellSet(row, row + rowCount - 1, modelColumn, modelColumn));
            }
            cellsListener.tableUpdated(getModel(), updatedSet);
            getModel().addTableModelListener(cellsListener);
        }
    }

    /**
   * Pastes the previously cut or copied cells from the current selected cell.
   * Returns <code>true</code> if a circularity has been detected during the paste operation.
   */
    public boolean pasteCopiedCells() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        boolean circularity = false;
        if (row != -1 && column != -1 && copyBuffer != null) {
            TableModel model = getModel();
            model.removeTableModelListener(cellsListener);
            int rowCount = copyBuffer.length;
            int columnCount = copyBuffer[0].length;
            Hashtable updatedReferringCells = null;
            Vector updatedSet = new Vector();
            for (int i = 0; i < rowCount; i++) for (int j = 0; j < columnCount; j++) {
                if (copyBuffer[i][j] instanceof CompiledExpression) {
                    JeksExpression pastedExpression = null;
                    try {
                        String pastedString;
                        if (cut) pastedString = getExpressionParser().shiftExpression((CompiledExpression) copyBuffer[i][j], row - copyRow, column - copyColumn, copyRow, copyColumn, copyRow + rowCount - 1, copyColumn + columnCount - 1); else pastedString = getExpressionParser().shiftExpression((CompiledExpression) copyBuffer[i][j], row - copyRow, column - copyColumn);
                        pastedExpression = (JeksExpression) getExpressionParser().compileExpression(pastedString);
                    } catch (CompilationException ex) {
                    }
                    setValueAt(pastedExpression, row + i, column + j);
                } else setValueAt(copyBuffer[i][j], row + i, column + j);
                if (cut) {
                    Vector referringCells = cellsListener.getReferringCells(new JeksCell(copyRow + i, convertColumnIndexToModel(copyColumn + j)));
                    if (referringCells != null) for (int k = 0; k < referringCells.size(); k++) try {
                        JeksCell referringCell = (JeksCell) referringCells.elementAt(k);
                        if (referringCell.getRow() < row || referringCell.getRow() >= row + rowCount || referringCell.getColumn() < column || referringCell.getColumn() >= column + columnCount) {
                            Object cellValue = model.getValueAt(referringCell.getRow(), referringCell.getColumn());
                            if (cellValue instanceof JeksExpression) {
                                if (updatedReferringCells == null) updatedReferringCells = new Hashtable();
                                if (updatedReferringCells.get(referringCell) == null) {
                                    JeksExpression computedCell = (JeksExpression) cellValue;
                                    String pastedString = getExpressionParser().shiftExpression(computedCell, row - copyRow, column - copyColumn, copyRow, copyColumn, copyRow + rowCount - 1, copyColumn + columnCount - 1);
                                    computedCell = (JeksExpression) getExpressionParser().compileExpression(pastedString);
                                    updatedReferringCells.put(referringCell, computedCell);
                                    updatedSet.addElement(referringCell);
                                }
                            }
                        }
                    } catch (CompilationException ex) {
                    }
                }
            }
            for (int i = 0; i < columnCount; i++) {
                int modelColumn = convertColumnIndexToModel(column + i);
                updatedSet.addElement(new JeksCellSet(row, row + rowCount - 1, modelColumn, modelColumn));
            }
            for (int i = 0; i < rowCount; i++) for (int j = 0; j < columnCount; j++) {
                JeksExpression computedCell = null;
                try {
                    Object cellValue = getValueAt(row + i, column + j);
                    if (cellValue instanceof JeksExpression) (computedCell = (JeksExpression) cellValue).checkCircularity(model, new JeksCell(row + i, convertColumnIndexToModel(column + j)));
                } catch (CircularityException ex) {
                    circularity = true;
                    computedCell.invalidateValue(ex);
                }
                if (cut && updatedReferringCells != null) {
                    for (Enumeration enume = updatedReferringCells.keys(); enume.hasMoreElements(); ) try {
                        JeksCell referringCell = (JeksCell) enume.nextElement();
                        computedCell = (JeksExpression) updatedReferringCells.get(referringCell);
                        model.setValueAt(computedCell, referringCell.getRow(), referringCell.getColumn());
                        computedCell.checkCircularity(model, referringCell);
                    } catch (CircularityException ex) {
                        circularity = true;
                        computedCell.invalidateValue(ex);
                    }
                }
            }
            cellsListener.tableUpdated(getModel(), updatedSet);
            model.addTableModelListener(cellsListener);
            cut = false;
        }
        return circularity;
    }

    /**
   * Returns the default renderer of the class of the object stored at
   * (<code>row</code>,<code>column</code>).
   */
    public TableCellRenderer getCellRenderer(int row, int column) {
        Object value = getValueAt(row, column);
        if (value != null) return getDefaultRenderer(getValueAt(row, column).getClass()); else return super.getCellRenderer(row, column);
    }

    /**
   * Returns a instance of <code>JeksCellEditor</code> if this table is
   * editable.
   */
    public TableCellEditor getCellEditor(int row, int column) {
        if (editor != null) return editor; else return super.getCellEditor(row, column);
    }

    /**
   * Returns the localized column name of this table.
   */
    public String getColumnName(int column) {
        if (expressionParser == null) return super.getColumnName(column); else return ((JeksExpressionSyntax) expressionParser.getSyntax()).getColumnName(convertColumnIndexToModel(column));
    }

    public void addColumn(TableColumn column) {
        if (expressionParser == null) super.addColumn(column); else {
            if (column.getHeaderValue() == null) column.setHeaderValue(((JeksExpressionSyntax) expressionParser.getSyntax()).getColumnName(column.getModelIndex()));
            getColumnModel().addColumn(column);
        }
    }
}
