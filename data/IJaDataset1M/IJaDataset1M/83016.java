package net.sourceforge.securevault.gui;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import net.sourceforge.securevault.Secret;

/**
 * @author albert
 * A table model of secrets for SecureVault.
 */
class SVSecretTableModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String[] SVSecretColumnNames = { "Secret", "Category" };

    private Vector<Secret> data = null;

    /**
	 * The class constructor.
	 */
    public SVSecretTableModel() {
        super();
        data = new Vector<Secret>();
    }

    /**
	 * Clears all data stored in the table.
	 *
	 */
    public void clearData() {
        data = new Vector<Secret>();
    }

    /**
	 * Retrieve the class of a column.
	 * @param c The index of the column
	 * @return A class container as a generic.
	 */
    @Override
    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
	 * Retrieve the column count.
	 * @return The number of columns.
	 */
    public int getColumnCount() {
        return SVSecretColumnNames.length;
    }

    /**
	 * Retrieve the name of the column.
	 * @param col The column index.
	 * @return The name of the column as shown to the user.
	 */
    @Override
    public String getColumnName(int col) {
        return SVSecretColumnNames[col];
    }

    /**
	 * Retrieve the number of rows.
	 * @return The number of rows in the table.
	 */
    public int getRowCount() {
        return data.size();
    }

    /**
	 * Retrieves the value in the table.
	 * @param row The index of the row.
	 * @param col The index of the column
	 * @return The Object contained in that cell.
	 */
    public Object getValueAt(int row, int col) {
        if (col > 0) return data.get(row).category().name(); else return data.get(row).name();
    }

    /**
	 * Fill the table with data.
	 * @param s A generator of secrets to populate the table with.
	 */
    public void populateData(Iterator<Secret> s) {
        data = new Vector<Secret>();
        while (s.hasNext()) data.add(s.next());
    }

    /**
	 * Fill the table with data.
	 * @param s A Vector of generators of secrets to populate the table with.
	 */
    public void populateData(Vector<Iterator<Secret>> s) {
        data = new Vector<Secret>();
        for (Iterator<Secret> i : s) {
            while (i.hasNext()) data.add(i.next());
        }
    }

    public boolean repOk() {
        if (data == null) return false;
        return true;
    }

    @Override
    public String toString() {
        String s = new String("SVSecretsTableModel: \n");
        for (int i = 0; i < SVSecretColumnNames.length; i++) s = s.concat(SVSecretColumnNames[i] + "\t");
        s = s.concat("\n");
        for (int r = 0; r < data.size(); r++) {
            s = s.concat(data.get(r).name() + "\t" + data.get(r).category().name());
            s = s.concat("\n");
        }
        return s;
    }
}
