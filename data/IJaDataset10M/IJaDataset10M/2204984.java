package org.fudaa.fudaa.hydraulique1d.tableau;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Mod�le abstrait de tableau d'hydraulique1d.
 * G�re une liste de noms des colonnes.
 * @see org.fudaa.fudaa.hydraulique1d.tableau.Hydraulique1dTableau
 * @author Jean-Marc Lacombe
 * @version $Revision: 1.5 $ $Date: 2006-09-08 16:04:25 $ by $Author: opasteur $
 */
public abstract class Hydraulique1dAbstractTableauModel extends AbstractTableModel {

    /**
   * Le tableau contenant le nom des colonnes.
   * La dimension du tableau indique le nombre de colonnes.
   */
    protected List listeColumnNames_ = new ArrayList(5);

    /**
   * Constructeur par d�faut.
   * Initialise la liste de noms de colonne.
   */
    public Hydraulique1dAbstractTableauModel() {
        super();
        listeColumnNames_ = new ArrayList(5);
    }

    /**
   * Constructeur pr�cisant les noms de colonnes et le nombre de lignes vides � la fin.
   * @param columnNames le tableau des noms de colonnes.
   */
    public Hydraulique1dAbstractTableauModel(String[] columnNames) {
        super();
        listeColumnNames_ = new ArrayList(columnNames.length);
        for (int i = 0; i < columnNames.length; i++) {
            listeColumnNames_.add(columnNames[i]);
        }
    }

    /**
   * Retourne les noms des colonnes.
   * @return le tableau des noms de colonnes.
   */
    public String[] getColumnNames() {
        return (String[]) listeColumnNames_.toArray(new String[0]);
    }

    /**
   * Retourne de nom d'une colonne.
   * @param col l'indice de la colonne.
   * @return le nom de la colonne d'indice en param�tre.
   */
    public String getColumnName(int col) {
        return (String) listeColumnNames_.get(col);
    }

    /**
   * Initialise les noms des colonnes.
   * @param columnNames le tableau des noms de colonnes.
   */
    public void setColumnNames(String[] columnNames) {
        if (columnNames == null) throw new IllegalArgumentException("tableau columnNames nulle pas autoris�");
        if (columnNames.length < 1) throw new IllegalArgumentException("taille du tableau columnNames insuffisante " + columnNames.length);
        listeColumnNames_ = new ArrayList(columnNames.length);
        for (int i = 0; i < columnNames.length; i++) {
            listeColumnNames_.add(columnNames[i]);
        }
        fireTableStructureChanged();
    }

    /**
   * Initialise un nom de colonne.
   * @param columnName le nom d'une colonne.
   * @param index l'indice de la colonne dont le nom est modifi�.
   */
    public void setColumnName(String columnName, int index) {
        if (columnName == null) throw new IllegalArgumentException("chaine columnName nulle pas autoris�");
        listeColumnNames_.set(index, columnName);
        fireTableStructureChanged();
    }

    /**
   * Retourne le nombre de colonne.
   * @return le nombre d'�l�ment de columnNames_.
   */
    public int getColumnCount() {
        return listeColumnNames_.size();
    }

    /**
   * R�cup�re les donn�es de l'objet m�tier et les tranferts vers le mod�le de tableau.
   * Ne fait rien. Pr�voir l'impl�m�ntation pour les classes filles.
   */
    public void setValeurs() {
    }

    /**
   * Transferts les donn�es du tableau vers l'objet m�tier.
   * retourne toujours faux. Pr�voir l'impl�mentation pour les classes filles.
   * @return vrai s'il existe des diff�rences, faux sinon.
   */
    public boolean getValeurs() {
        return false;
    }
}
