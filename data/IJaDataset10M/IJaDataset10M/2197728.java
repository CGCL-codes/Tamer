package cz.cvut.fel.mvod.gui.table;

/**
 * Rozšířená implementace javax.swing.table.AbstractTableModel.
 * @author jakub
 */
abstract class AbstractTableModel<T> extends javax.swing.table.AbstractTableModel {

    /**
	 * Informace o jednotlivých sloupcích.
	 */
    protected final TableColumnInformation[] COLUMNS;

    public AbstractTableModel(TableColumnInformation[] COLLUMNS) {
        this.COLUMNS = COLLUMNS;
    }

    /**
	 * Vrátí počet sloupců definovaných v {@link #COLUMNS}.
	 * @return počet sloupců
	 */
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMNS[columnIndex].NAME;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMNS[columnIndex].TYPE;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return COLUMNS[columnIndex].EDITABLE;
    }

    /**
	 * Odstraní položku na vybraném řádku.
	 * @param rowIndex číslo řádky
	 * @return odstraněný prvek
	 */
    public abstract T remove(int rowIndex);

    /**
	 * Vrátí položku zobrazenou na vybraném řádku.
	 * @param rowIndex číslo řádku
	 * @return položka na vybraném řádku
	 */
    public abstract T getValueAt(int rowIndex);
}
