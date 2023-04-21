package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import tame.EditableHeader;
import tame.EditableHeaderTableColumn;

/**
 * Creates a table for importing data on invididuals from a CSV file.
 * @author Paul A. Rubin <rubin@msu.edu>
 */
public class ImportTable extends JScrollPane {

    private String[][] data;

    private int rows;

    private int cols;

    private JTable table;

    private JTable rhTable;

    private TableModel rhModel;

    private String[][] rowHeads;

    private Main gui;

    /**
   * Constructor.
   * @param g the owning GUI
   * @param importedData the data read from the CSV file
   * @param attributes list of existing attribute names
   */
    public ImportTable(Main g, String[][] importedData, ArrayList<String> attributes) {
        gui = g;
        data = importedData;
        rows = importedData.length;
        cols = 0;
        for (int i = 0; i < rows; i++) {
            cols = Math.max(cols, data[i].length);
        }
        rowHeads = new String[rows][2];
        for (int i = 0; i < rows; i++) {
            rowHeads[i][0] = Integer.toString(i + 1);
            rowHeads[i][1] = "import";
        }
        String[] junk = new String[cols];
        for (int i = 0; i < cols; i++) {
            junk[i] = "";
        }
        table = new JTable(data, junk);
        TableColumnModel cm = table.getColumnModel();
        table.setTableHeader(new EditableHeader(cm));
        EditableHeaderTableColumn ec;
        for (int i = 0; i < cols; i++) {
            ec = (EditableHeaderTableColumn) table.getColumnModel().getColumn(i);
            ImportColumnHeader h = new ImportColumnHeader(gui, this, i, ec, attributes);
            ec.setHeaderValue(h.getItemAt(0));
            ec.setHeaderRenderer(h);
            ec.setHeaderEditor(new DefaultCellEditor(h));
        }
        table.getTableHeader().setReorderingAllowed(false);
        table.setBorder(new BevelBorder(1));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        String[] colLabels = new String[] { "Row", "Action" };
        rhModel = new DefaultTableModel(rowHeads, colLabels) {

            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 1;
            }
        };
        rhTable = new JTable(rhModel);
        rhTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rhTable.setDefaultRenderer(rhTable.getColumnClass(0), new RowHeaderRenderer(table));
        rhTable.setGridColor(table.getTableHeader().getBackground());
        rhTable.getColumnModel().getColumn(1).setCellEditor(new ImportRowActionEditor(gui, rhTable, table));
        rhTable.setBorder(new BevelBorder(0));
        Dimension d = rhTable.getPreferredScrollableViewportSize();
        d.width = rhTable.getPreferredSize().width;
        rhTable.setPreferredScrollableViewportSize(d);
        this.setViewportView(table);
        this.setRowHeaderView(rhTable);
    }

    /**
   * Renders row header cells.
   */
    class RowHeaderRenderer extends JLabel implements TableCellRenderer {

        /**
     * Constructor.
     * @param table table of row headers
     */
        RowHeaderRenderer(JTable table) {
            JTableHeader header = table.getTableHeader();
            setOpaque(true);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(CENTER);
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
        }

        /**
     * Renders cell entries as strings.
     * @param table the table of row headers
     * @param value the value of the current header cell
     * @param isSelected is the current header cell selected
     * @param hasFocus does the current header cell have focus
     * @param row the row containing the cell
     * @param column the column among the row headers containing the cell
     * @return the header cell with the current content set as text
     */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
   * Gets the user's choice of row action for a table row.
   * @param row the index of the row
   * @return the user's choice
   */
    public String getRowHeaderValue(int row) {
        if (row < 0 || row >= rows) {
            return null;
        } else {
            return (String) rhTable.getValueAt(row, 1);
        }
    }

    /**
   * Gets the user's choice of column header (attribute name or skip).
   * @param col the index of the column
   * @return the current column header value
   */
    public String getColumnHeaderValue(int col) {
        if (col < 0 || col >= cols) {
            return null;
        } else {
            return (String) table.getColumnModel().getColumn(col).getHeaderValue();
        }
    }

    /**
   * Gets the number of data columns.
   * @return the column count
   */
    public int getColumnCount() {
        return cols;
    }

    /**
   * Gets the number of data rows.
   * @return the row count
   */
    public int getRowCount() {
        return rows;
    }

    /**
   * Gets the content of a cell in the data table.
   * @param row the row index
   * @param col the column index
   * @return the cell contents
   */
    public String getCellValue(int row, int col) {
        return (String) table.getValueAt(row, col);
    }

    /**
   * Resets the width of a column to accommodate a header change.
   * @param i the index of the column to resize
   * @param w the new preferred width
   */
    public void resetColumnWidth(int i, int w) {
        TableColumn q = table.getColumnModel().getColumn(i);
        q.setPreferredWidth(w);
        table.doLayout();
    }
}
