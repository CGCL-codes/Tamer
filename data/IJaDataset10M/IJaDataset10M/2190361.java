package de.ipkgatersleben.agbi.uploader.gui.panels.filePreview;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * @author Thomas Rutkowski
 */
public class FilePreviewTabModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Vector dataVector = new Vector();

    private List columnIdentifiers = new Vector();

    public void setDataVector(Vector data) {
        this.dataVector = data;
        this.fireTableStructureChanged();
    }

    /** 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        int dataCol = 0;
        if (this.getRowCount() > 0) dataCol = ((List) dataVector.get(this.getRowCount() - 1)).size();
        int headerCol = 0;
        if (this.columnIdentifiers.size() > 0) headerCol = columnIdentifiers.size();
        return Math.max(headerCol, dataCol);
    }

    /**
     * TODO not used ?
     * @return liefert Spalten im datensatzvector
     */
    public int getDataColumnCount() {
        if (this.getRowCount() > 0) return ((List) dataVector.get(0)).size();
        return 0;
    }

    /** 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        Object val = null;
        if (row < this.dataVector.size()) {
            Object rowVal = this.dataVector.get(row);
            if (rowVal != null && col < ((List) rowVal).size()) val = ((List) rowVal).get(col);
        }
        return val;
    }

    /** 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        if (this.dataVector == null) return 0;
        return this.dataVector.size();
    }

    /**
     * 
     * @param newHeader
     */
    public void setColumnIdentifiers(List newHeader) {
        this.columnIdentifiers = newHeader;
        this.fireTableStructureChanged();
    }

    /** 
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int col) {
        if (this.columnIdentifiers != null && col < this.columnIdentifiers.size() && columnIdentifiers.get(col) != null) return columnIdentifiers.get(col).toString(); else return "Column " + (col + 1);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public List<String> getFirstRow() {
        if (this.getRowCount() > 0) {
            return (List) dataVector.get(0);
        }
        return new Vector<String>(0);
    }
}
