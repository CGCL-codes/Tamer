package org.fudaa.fudaa.sipor.ui.tools;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import com.memoire.bu.BuDialogError;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.fudaa.sipor.SiporImplementation;
import org.fudaa.fudaa.sipor.factory.FonctionsSimu;
import org.fudaa.fudaa.sipor.structures.GenarrListeNavires;
import org.fudaa.fudaa.sipor.structures.GenarrNavire;
import org.fudaa.fudaa.sipor.structures.SiporDataSimulation;
import org.fudaa.fudaa.sipor.ui.modeles.SiporModeleExcel;

public class GenarrModeleTable extends SiporModeleExcel {

    GenarrListeNavires listeNavires_;

    SiporDataSimulation donnees_;

    String[] titreColonnes_ = { "Navire", "Cat�gorie", "Jour", "Heure", "Minute" };

    public GenarrModeleTable(SiporDataSimulation d) {
        listeNavires_ = d.getGenarr_();
        donnees_ = d;
    }

    public int getColumnCount() {
        return 5;
    }

    public int getRowCount() {
        return listeNavires_.taille();
    }

    /** retourne l'objet correspondant correspondant au parcours **/
    public Object getValueAt(int i, int j) {
        if (j == 0) return "" + listeNavires_.retourner(i).getNavire(); else if (j == 1) return donnees_.getCategoriesNavires_().retournerNavire(listeNavires_.retourner(i).getCategorie()).getNom(); else if (j == 2) return "" + listeNavires_.retourner(i).getJour(); else if (j == 3) return "" + listeNavires_.retourner(i).getHeure(); else return "" + listeNavires_.retourner(i).getMinute();
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        int nbMaxCategories = donnees_.getCategoriesNavires_().getListeNavires_().size();
        int nbMaxJoursSimu = donnees_.getParams_().donneesGenerales.nombreJours;
        if (columnIndex == 1) {
            try {
                String nomCateg = (String) value;
                int val = donnees_.getCategoriesNavires_().retournerIndiceNavire(nomCateg);
                if (val == -1) {
                    new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "La cat�gorie " + nomCateg + " n'existe pas.").activate();
                    return;
                }
                GenarrNavire gn = listeNavires_.retourner(rowIndex);
                gn.setCategorie(val);
            } catch (NumberFormatException e) {
                new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "La cat�gorie doit �tre un entier compris entre 0 et " + (nbMaxCategories - 1)).activate();
                return;
            }
        } else if (columnIndex == 2) {
            try {
                int val = Integer.parseInt((String) value);
                if (val > nbMaxJoursSimu * FonctionsSimu.NOMBRE_SIMULATIONS + 2) {
                    new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Le jour doit �tre un entier inf�rieur � " + (nbMaxJoursSimu * FonctionsSimu.NOMBRE_SIMULATIONS + 2)).activate();
                    return;
                }
                GenarrNavire gn = listeNavires_.retourner(rowIndex);
                gn.setJour(val);
                this.listeNavires_.trierNaviresChronologiquement();
            } catch (NumberFormatException e) {
                new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Le jour doit �tre un entier inf�rieur � " + (nbMaxJoursSimu)).activate();
                return;
            }
        } else if (columnIndex == 3) {
            try {
                int val = Integer.parseInt((String) value);
                if (val > 24) {
                    new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "L'heure doit �tre un entier inf�rieur strict � 24").activate();
                    return;
                }
                GenarrNavire gn = listeNavires_.retourner(rowIndex);
                gn.setHeure(val);
                this.listeNavires_.trierNaviresChronologiquement();
            } catch (NumberFormatException e) {
                new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "L'heure doit �tre un entier inf�rieur � 24").activate();
                return;
            }
        } else if (columnIndex == 4) {
            try {
                int val = Integer.parseInt((String) value);
                if (val > 60) {
                    new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Les minutes doivent �tre un entier inf�rieur strict � 60").activate();
                    return;
                }
                GenarrNavire gn = listeNavires_.retourner(rowIndex);
                gn.setMinute(val);
                this.listeNavires_.trierNaviresChronologiquement();
            } catch (NumberFormatException e) {
                new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Les minutes doivent �tre un entier inf�rieur strict � 60").activate();
                return;
            }
        }
        if (columnIndex >= 2) {
            miseAjour();
        } else fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void miseAjour() {
        fireTableDataChanged();
    }

    public boolean isCellEditable(int row, int col) {
        if (col < 1) {
            return false;
        } else {
            return true;
        }
    }

    /**Methode qui permet de recuperer les noms des colonnes du tableau **/
    public String getColumnName(int column) {
        return titreColonnes_[column];
    }

    public int getMaxCol() {
        return getColumnCount();
    }

    /**
   * retourne le nombre de ligne
   */
    public int getMaxRow() {
        return getRowCount();
    }

    /**
   * retoune un tableau pour le format excel Celui-ci sera utilis� avec la fonction write
   * 
   * @see org.fudaa.ctulu.table.CtuluTableModelInterface#getExcelWritable(int, int)
   */
    public WritableCell getExcelWritable(final int _row, final int _col, int _rowXls, int _colXls) {
        final int r = _row;
        final int c = _col;
        final Object o = getValueAt(r, c);
        if (o == null) {
            return null;
        }
        String s = o.toString();
        if (CtuluLibString.isEmpty(s)) return null;
        try {
            return new Number(_colXls, _rowXls, Double.parseDouble(s));
        } catch (final NumberFormatException e) {
        }
        return new Label(_colXls, _rowXls, s);
    }

    public GenarrListeNavires getListeNavires_() {
        return listeNavires_;
    }

    public void setListeNavires_(GenarrListeNavires listeNavires_) {
        this.listeNavires_ = listeNavires_;
    }

    public SiporDataSimulation getDonnees_() {
        return donnees_;
    }

    public void setDonnees_(SiporDataSimulation donnees_) {
        this.donnees_ = donnees_;
    }

    public String[] getTitreColonnes_() {
        return titreColonnes_;
    }

    public void setTitreColonnes_(String[] titreColonnes_) {
        this.titreColonnes_ = titreColonnes_;
    }
}
