package org.fudaa.fudaa.sipor.ui.modeles;

import javax.swing.table.AbstractTableModel;
import com.memoire.bu.BuDialogError;
import org.fudaa.fudaa.sipor.SiporImplementation;

/**
 * Classe qui d�finit un modele pour le tableau de saisie des donn�es marees
 *@version $Version$
 * @author hadoux
 *
 */
public class SiporTableModelMaree extends AbstractTableModel {

    double[] tableau_;

    String[] titreColonnes_;

    public SiporTableModelMaree(double[] _tableau) {
        tableau_ = _tableau;
        titreColonnes_ = new String[2];
        titreColonnes_[0] = "Intervalle";
        titreColonnes_[1] = "Nb douzi�mes";
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return tableau_.length;
    }

    /** Methode qui retourne l'objet correspondant  **/
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) return "" + (rowIndex + 1);
        return "" + (tableau_[rowIndex]);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        double valeur;
        try {
            valeur = Double.parseDouble((String) value);
            if (valeur < 0) {
                new BuDialogError(null, SiporImplementation.INFORMATION_SOFT, "La valeur ne peut pas �tre n�gative.").activate();
                return;
            } else if (valeur > 12) {
                new BuDialogError(null, SiporImplementation.INFORMATION_SOFT, "La valeur doit �tre inf�rieure � 12.").activate();
                return;
            }
        } catch (NumberFormatException e) {
            new BuDialogError(null, SiporImplementation.INFORMATION_SOFT, "La valeur n'est pas coh�rente: ce n'est pas un reel.").activate();
            return;
        }
        tableau_[rowIndex] = valeur;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) return false; else return true;
    }

    /**Methode qui permet de recuperer els noms des colonnes du tableau **/
    public String getColumnName(int column) {
        return titreColonnes_[column];
    }

    public double[] getTableau_() {
        return tableau_;
    }

    public void setTableau_(double[] tableau_) {
        this.tableau_ = tableau_;
    }

    public String[] getTitreColonnes_() {
        return titreColonnes_;
    }

    public void setTitreColonnes_(String[] titreColonnes_) {
        this.titreColonnes_ = titreColonnes_;
    }
}
