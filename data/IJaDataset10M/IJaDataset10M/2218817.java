package ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Modèle de JTable dont la première colonne n'est pas éditable
 * @author bruno
 *
 */
public class JTableNonEditCol1 extends JTable {

    /** Pour la sérialisation (non utilisé) */
    private static final long serialVersionUID = 1L;

    /** Indique quels enregistrements sont corrects dans la table de donnée */
    private boolean valide[][];

    /**
	 * Constructeur
	 * @param d les données
	 * @param c les noms des colonnes
	 */
    public JTableNonEditCol1(String[][] d, String[] c) {
        super(d, c);
        valide = new boolean[getRowCount()][getColumnCount()];
        for (int i = 0; i < valide.length; i++) {
            for (int j = 0; j < valide[0].length; j++) {
                valide[i][j] = true;
            }
        }
        TableBrailleRenderer tbr = new TableBrailleRenderer();
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(tbr);
        }
    }

    /**
	 * Associe à la cellule (row,col) la valeur v
	 * @param row indice de la ligne
	 * @param col indice de la colonne
	 * @param v true si la valeur de la cellule est valide, false sinon
	 */
    public void setValide(int row, int col, boolean v) {
        valide[row][col] = v;
    }

    /**
	 * Renvoie la valeur de {@link #valide}[row][col]
	 * @param row indice de la ligne
	 * @param col indice de la colonne
	 * @return le booléen {@link #valide}
	 */
    public boolean getValide(int row, int col) {
        return valide[row][col];
    }

    /**
	 * Vérifie que toutes les données de la table sont correctes en fonction du tableau booleéen "valide"
	 * @return true si les données sont valides
	 */
    public boolean isValide() {
        boolean retour = true;
        int i = 1;
        while (i < getRowCount() && retour) {
            int j = 0;
            while (j < getColumnCount() && retour) {
                if (!valide[i][j]) {
                    retour = false;
                }
                j++;
            }
            i++;
        }
        return retour;
    }

    /**
	 * redéfinition de isCellEditable de JTable
	 * La colonne 0 est toujours non-éditable
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
    @Override
    public boolean isCellEditable(int row, int col) {
        boolean retour = true;
        if (col == 0) {
            retour = false;
        }
        return retour;
    }

    /**
	 * Classe interne pour l'affichage de la JTable
	 * Affiche la 1ère colonne avec une teinte beige
	 * Affiche les cellules des autres colonnes sur fond rouge si leur donnée est non valide, en blanc sinon
	 * @author bruno
	 *
	 */
    public class TableBrailleRenderer extends DefaultTableCellRenderer {

        /** Pour la sérialisation (non utilisé) */
        private static final long serialVersionUID = 1L;

        /**
		 * Méthode redéfinie de DefaultTableCellRenderer
		 * <p>Affiche la 1ère colonne avec une teinte beige</p>
		 * <p>Affiche les cellules des autres colonnes sur fond rouge si leur donnée est non valide, en blanc sinon</p>
		 * <p>Renvoie un Component correspondant à une cellule de la Table</p>
		 * @return un Component correspondant à une cellule de la Table
		 * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
		 */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 0) {
                setBackground(Color.getHSBColor(1, 0, (float) 50.0));
            } else if (!getValide(row, column)) {
                setBackground(Color.red);
                setToolTipText("donnée non valide");
                getAccessibleContext().setAccessibleDescription("donnée non valide");
            } else {
                setBackground(Color.WHITE);
                setToolTipText("");
                getAccessibleContext().setAccessibleDescription("");
            }
            return this;
        }
    }
}
