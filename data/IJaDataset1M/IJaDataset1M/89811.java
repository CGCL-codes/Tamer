package dr.app.tracer.traces;

import dr.inference.trace.TraceAnalysis;
import jam.framework.Exportable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * A panel that displays correlation plots of 2 traces in integer and category type
 *
 * @author Andrew Rambaut
 * @author Alexei Drummond
 * @author Walter Xie
 * @version $Id: TableScrollPane.java,v 1.1.1.2 2006/04/25 23:00:09 rambaut Exp $
 */
public class TableScrollPane extends JScrollPane implements Exportable {

    Object[] rowNames;

    Object[] columnNames;

    double[][] data;

    ListModel lm = new RowHeaderModel();

    DataTableModel dataTableModel = new DataTableModel();

    JTable dataTable = new JTable(dataTableModel);

    private boolean defaultNumberFormat = true;

    public TableScrollPane() {
        super();
        setViewportView(dataTable);
        setOpaque(false);
    }

    public void setTable(Object[] rowNames, Object[] columnNames, double[][] data, boolean defaultNumberFormat) {
        this.rowNames = rowNames;
        this.columnNames = columnNames;
        this.data = data;
        this.defaultNumberFormat = defaultNumberFormat;
        dataTableModel = new DataTableModel();
        dataTable = new JTable(dataTableModel);
        setViewportView(dataTable);
        lm = new RowHeaderModel();
        JList rowHeader = new JList(lm);
        rowHeader.setFixedCellWidth(50);
        rowHeader.setFixedCellHeight(dataTable.getRowHeight() + dataTable.getRowMargin());
        rowHeader.setCellRenderer(new RowHeaderRenderer(dataTable));
        setRowHeaderView(rowHeader);
        dataTableModel.fireTableDataChanged();
        validate();
        repaint();
    }

    public JComponent getExportableComponent() {
        return this;
    }

    class RowHeaderModel extends AbstractListModel {

        public int getSize() {
            if (rowNames == null) return 0;
            return rowNames.length;
        }

        public Object getElementAt(int index) {
            return rowNames[index];
        }
    }

    class RowHeaderRenderer extends JLabel implements ListCellRenderer {

        RowHeaderRenderer(JTable table) {
            JTableHeader header = table.getTableHeader();
            setOpaque(true);
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            setHorizontalAlignment(CENTER);
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class DataTableModel extends DefaultTableModel {

        public DataTableModel() {
        }

        public int getColumnCount() {
            if (columnNames == null) return 0;
            return columnNames.length;
        }

        public int getRowCount() {
            if (lm == null) {
                return 0;
            }
            return lm.getSize();
        }

        public Object getValueAt(int row, int col) {
            if (defaultNumberFormat) {
                return TraceAnalysis.formattedNumber(data[row][col]);
            }
            return data[row][col];
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public String getColumnName(int column) {
            return columnNames[column].toString();
        }
    }
}
