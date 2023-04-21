package net.sf.swinggoodies.components.tables;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.sf.swinggoodies.util.SGPersistence;

public class SGTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private List<Object[]> rows = null;

    private String[] columns = null;

    public SGTableModel(String hql, String[] columns) {
        setRows(hql);
        setColumns(columns);
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    @SuppressWarnings("unchecked")
    public void setRows(String hql) {
        rows = SGPersistence.getEntityManager().createQuery(hql).setMaxResults(10).getResultList();
    }

    /**
	 * Retorna o numero de columns no modelo
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
    public int getColumnCount() {
        return columns.length;
    }

    /**
	 * Retorna o numero de linhas existentes no modelo
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
    public int getRowCount() {
        return rows.size();
    }

    /**
	 * Obtem o valor na linha e coluna
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex)[columnIndex];
    }

    public void addRow(String[] dadosLinha) {
        rows.add(dadosLinha);
        int linha = getRowCount() - 1;
        fireTableRowsInserted(linha, linha);
        return;
    }
}
