package paperdoll;

import geardatabase.*;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Mani
 */
public class CItemChooserTableModel extends AbstractTableModel {

    ArrayList<String> m_columnNameList = new ArrayList<String>();

    ArrayList<ArrayList> m_arrayList = new ArrayList<ArrayList>();

    public void addColumnData(ArrayList<CItem> p_arrayList, int p_column) {
        m_arrayList.get(p_column).addAll(p_arrayList);
        this.fireTableDataChanged();
    }

    public void setWidth(JTable p_table, int p_width) {
        TableColumnModel l_model = p_table.getColumnModel();
        if (m_columnNameList.size() == 1) {
            p_table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        } else {
            for (int i = 0; i < m_columnNameList.size(); i++) {
                TableColumn l_col = l_model.getColumn(i);
                l_col.setPreferredWidth(p_width);
                l_col.setWidth(p_width);
            }
        }
        this.fireTableDataChanged();
    }

    public void resetColumnData() {
        m_arrayList.clear();
        for (int i = 0; i < m_columnNameList.size(); i++) {
            m_arrayList.add(new ArrayList());
        }
    }

    public void addColumn(String p_columnName) {
        m_columnNameList.add(p_columnName);
        m_arrayList.add(new ArrayList());
        this.fireTableStructureChanged();
    }

    public int getColumnCount() {
        return m_columnNameList.size();
    }

    public int getRowCount() {
        int l_maxRowCount = 0;
        for (int i = 0; i < m_columnNameList.size(); i++) {
            l_maxRowCount = Math.max(l_maxRowCount, m_arrayList.get(i).size());
        }
        return l_maxRowCount;
    }

    @Override
    public String getColumnName(int col) {
        return m_columnNameList.get(col);
    }

    public Object getValueAt(int row, int col) {
        if (m_arrayList.get(col).size() <= row) {
            return null;
        }
        return m_arrayList.get(col).get(row);
    }

    @Override
    public Class getColumnClass(int c) {
        return CItem.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        m_arrayList.get(col).set(row, value);
        fireTableCellUpdated(row, col);
    }
}
