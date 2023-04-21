package Composants;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Tiep
 */
public class MonJTableModel extends DefaultTableModel {

    MonJTableModel(String[] stNomColonne, int iNbColonne) {
        super(stNomColonne, iNbColonne);
    }

    /**
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int iColumn) {
        return getValueAt(0, iColumn).getClass();
    }
}
