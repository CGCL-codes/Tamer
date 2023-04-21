package org.fudaa.fudaa.hydraulique1d.tableau;

/**
 * Definit le mod�le d'une ligne de tableau commen�ant par une chaine puis des  bool�ennes.
 * @author Olivier Pasteur
 * @version 1.0
 */
public class Hydraulique1dLigneChaineEtBooleensTableau extends Hydraulique1dLigneBooleensTableau {

    /**
    * La chaine en d�but de ligne (peut �tre null).
    */
    private String chaine_;

    /**
   * Constructeur le plus g�n�ral pr�cisant la taille de la ligne.
   * @param taille le nombre de bool�ens de la ligne.
   * Au d�part les bool�ens sont initialis�s � null.
   */
    public Hydraulique1dLigneChaineEtBooleensTableau(int taille) {
        super(taille);
    }

    /**
   * Constructeur pour une ligne initialis�e par 3 bool�ens primitifs.
   * @param chaine premier �l�ment de la ligne.
   * @param x bool�en deuxi�me �l�ment de la ligne.
   * @param y bool�en troisi�me �l�ment de la ligne.
   */
    public Hydraulique1dLigneChaineEtBooleensTableau(String chaine, boolean x, boolean y) {
        this(chaine, booleanValue(x), booleanValue(y));
    }

    /**
   * Constructeur pour une ligne initialis�e par 3 bool�ens non primitifs.
   * @param chaine premier �l�ment de la ligne.
   * @param x bool�en deuxi�me �l�ment de la ligne.
   * @param y bool�en troisi�me �l�ment de la ligne.
   */
    public Hydraulique1dLigneChaineEtBooleensTableau(String chaine, Boolean x, Boolean y) {
        super(x, y);
        chaine(chaine);
    }

    /**
   * @return la valeur chaine de caract�re de la ligne (1�re colonne).
   * peut �tre nulle si la cellule est vide.
   */
    public String chaine() {
        return chaine_;
    }

    /**
   * Initialise le premier �lement de la ligne.
   * @param chaine La nouvelle valeur enti�re de la ligne (peut �tre nulle).
   */
    public void chaine(String chaine) {
        chaine_ = chaine;
    }

    /**
   * Efface la ligne en mettant ques des valeurs nulles.
   * Met la chaine � nulle.
   */
    public void effaceLigne() {
        super.effaceLigne();
        chaine(null);
    }

    /**
   * @return le nombre d'�l�ment possible de la ligne.
   */
    public int getTaille() {
        return super.getTaille() + 1;
    }

    /**
   * @return Vrai, s'il existe une valeur null (cellule vide), Faux sinon.
   */
    public boolean isExisteNulle() {
        boolean res = super.isExisteNulle();
        if (res) return true; else if (chaine_ == null) return true;
        return false;
    }

    /**
   * @return Vrai, s'il y a que des valeurs null (cellule vide), Faux sinon.
   */
    public boolean isToutNulle() {
        boolean res = super.isToutNulle();
        if (!res) return false; else if (chaine_ == null) return true;
        return false;
    }

    /**
   * @return les diff�rents �l�ments de la ligne s�par�e par un espace.
   */
    public String toString() {
        if (chaine_ == null) {
            return " vide , " + super.toString();
        }
        return chaine_ + ", " + super.toString();
    }
}
