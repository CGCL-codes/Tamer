package org.etu.swing;

import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.etu.Sudoku;

/**
 * Table de 9 x 9 affichant une grille de sudoku
 * @author Mathieu BROUTIN
 *
 */
public class SudoTable extends JTable {

    private static final long serialVersionUID = 1L;

    int[][] valeurs;

    int[][] val_ini;

    /**
	 * Constructeur initialisant le tableau à 9x9
	 */
    public SudoTable() {
        super(9, 9);
        this.valeurs = new int[9][9];
        this.val_ini = new int[9][9];
    }

    /**
	 * Constructeur initialisant le tableau à 9x9 et le rempli avec le parametre valeurs
	 * @param valeurs
	 */
    public SudoTable(int[][] valeurs) {
        super(9, 9);
        this.valeurs = (int[][]) valeurs.clone();
        this.val_ini = (int[][]) valeurs;
    }

    /**
	 * Fonction pour récuperer la valeur de la cellule à la position (row, column)
	 * @param row Le numero de la ligne
	 * @param column Le numero de la colonne
	 * @return L'entier correspondant à la case
	 */
    public Object getValueAt(int row, int column) {
        if (valeurs[row][column] == 0) return "";
        return new Integer(valeurs[row][column]);
    }

    /**
	 * Permet de savoir si le tableau est Editable ou non
	 */
    public boolean isEditing() {
        return true;
    }

    /**
	 * Permet de placer les valeurs dans le tableau selon le parametre v
	 * @param v Le tableau des valeurs
	 */
    public void setValeurs(int[][] v) {
        this.valeurs = (int[][]) v.clone();
        this.val_ini = (int[][]) v;
    }

    /**
	 * Méthode pour l'insertion de données
	 * @param aValue La valeur inserée
	 * @param row Le numéro de la ligne
	 * @param column Le numéro de la colonne
	 */
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue.toString().equals("")) valeurs[row][column] = 0; else {
            try {
                int val = (new Integer(aValue.toString()).intValue());
                if (this.valeurs[row][column] == val) return;
                if (val_ini[row][column] != 0) {
                    return;
                }
                if (val > 0 && val < 10) {
                    int retour = Sudoku.contientFromSudoku(valeurs, row, column, val);
                    if (retour != Sudoku.BON) {
                        switch(retour) {
                            case Sudoku.CARRE:
                                JOptionPane.showMessageDialog((JFrame) null, "Attention " + val + " est dans le carré");
                                break;
                            case Sudoku.LIGNE:
                                JOptionPane.showMessageDialog((JFrame) null, "Attention " + val + " est dans la ligne");
                                break;
                            case Sudoku.COLONNE:
                                JOptionPane.showMessageDialog((JFrame) null, "Attention " + val + " est dans la colonne");
                                break;
                        }
                    } else valeurs[row][column] = val;
                }
            } catch (Exception e) {
                System.err.println("[ERREUR] Problème dans le nombre entré : \"" + aValue + "\" n'est pas un nombre");
            }
        }
    }
}
