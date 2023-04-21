package org.fudaa.fudaa.hydraulique1d.tableau;

import java.util.ArrayList;
import java.util.List;

/**
 * Definit le mod�le d'une ligne de tableau de chaines.
 * @see Hydraulique1dTableauChaineModel
 * @author Olivier Pasteur
 * @version $Revision: 1.2 $ $Date: 2006-09-08 16:04:26 $ by $Author: opasteur $
 */
public class Hydraulique1dLigneChaineTableau implements Comparable {

    /**
   * le tableau contenant les diff�rents chaines de la ligne
   */
    private List listeString_;

    /**
   * Constructeur le plus g�n�ral pr�cisant la taille de la ligne.
   * @param taille le nombre de r�el de la ligne.
   * Au d�part les chaines sont initialis�s � null.
   */
    public Hydraulique1dLigneChaineTableau(int taille) {
        if (taille >= 0) {
            listeString_ = new ArrayList(taille);
            for (int i = 0; i < taille; i++) {
                listeString_.add(null);
            }
        } else {
            throw new IllegalStateException("Taille de la ligne incorrecte : " + taille);
        }
    }

    /**
   * Constructeur pour une ligne initialis�e par 3 chaines non primitifs.
   * @param x premier �l�ment de la ligne, peut �tre null (cellule vide).
   * @param y deuxi�me �l�ment de la ligne, peut �tre null (cellule vide).
   * @param z troisi�me �l�ment de la ligne, peut �tre null (cellule vide).
   */
    public Hydraulique1dLigneChaineTableau(String x) {
        this(1);
        listeString_.set(0, x);
    }

    /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.<p>
   *
   * In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i>
   * is negative, zero or positive.
   *
   * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
   * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
   * <tt>y.compareTo(x)</tt> throws an exception.)<p>
   *
   * The implementor must also ensure that the relation is transitive:
   * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
   * <tt>x.compareTo(z)&gt;0</tt>.<p>
   *
   * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
   * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
   * all <tt>z</tt>.<p>
   *
   * It is strongly recommended, but <i>not</i> strictly required that
   * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
   * class that implements the <tt>Comparable</tt> interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   *
   * @param   o the Object to be compared.
   * @return  a negative integer, zero, or a positive integer as this object
   *		is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this Object.
   */
    public int compareTo(Object o) {
        Hydraulique1dLigneChaineTableau l = (Hydraulique1dLigneChaineTableau) o;
        if (listeString_.size() == 0) return 0;
        if ((getValue(0) == null) && (l.getValue(0) == null)) {
            return 0;
        } else if (getValue(0) == null) {
            return 1;
        } else if (l.getValue(0) == null) {
            return -1;
        } else {
            return getValue(0).compareTo(l.getValue(0));
        }
    }

    /**
   * Retourne la i�me valeur de la ligne.
   * @param i Indice de la cellule.
   * @return la i�me valeur de la ligne.
   */
    public String value(int i) {
        return (String) listeString_.get(i);
    }

    /**
   * Retourne la i�me valeur de la ligne.
   * @param i Indice de la cellule.
   * @return la i�me valeur de la ligne. Elle peut �tre nulle.
   */
    public String getValue(int i) {
        return (String) listeString_.get(i);
    }

    /**
   * Initialise la i�me valeur de la ligne
   * @param i Indice de la cellule.
   * @param valeur La nouvelle valeur de la cellule, peut �tre null pour une cellule vide.
   */
    public void setValue(int i, String valeur) {
        listeString_.set(i, valeur);
    }

    /**
   * R�cup�re les valeurs de la ligne sous forme d'un tableau
   * @return z le tableau des �l�ments de la ligne.
   */
    public String[] getTab() {
        String[] res = new String[listeString_.size()];
        for (int i = 0; i < listeString_.size(); i++) {
            res[i] = getValue(i);
        }
        return res;
    }

    /**
   * @return le nombre d'�l�ment possible de la ligne.
   */
    public int getTaille() {
        return listeString_.size();
    }

    /**
   * @return Vrai, s'il existe une valeur null (cellule vide), Faux sinon.
   */
    public boolean isExisteNulle() {
        for (int i = 0; i < listeString_.size(); i++) {
            if (listeString_.get(i) == null) return true;
        }
        return false;
    }

    /**
   * @return Vrai, s'il y a que des valeurs null (cellule vide), Faux sinon.
   */
    public boolean isToutNulle() {
        for (int i = 0; i < listeString_.size(); i++) {
            if (listeString_.get(i) != null) return false;
        }
        return true;
    }

    /**
   * Diminue le nombre d'�l�ment de la ligne en supprimant une cellule � l'indexe indiqu�.
   * @param indexe L'indexe de la cellule a supprimmer.
   */
    public void supprimeCellule(int indexe) {
        listeString_.remove(indexe);
    }

    /**
   * Augmente le nombre d'�l�ment de la ligne en ins�rant une cellule vide � l'indexe indiqu�.
   * @param indexe L'indexe de la cellule vide rajout�.
   */
    public void ajoutCelluleVide(int indexe) {
        listeString_.add(indexe, null);
    }

    /**
   * Augmente le nombre d'�l�ment de la ligne en ins�rant une cellule vide � fin.
   */
    public void ajoutCelluleVide() {
        listeString_.add(null);
    }

    /**
   * Efface la ligne en mettant ques des valeurs nulles.
   * Doit �tre surcharg� pour les classes filles.
   */
    public void effaceLigne() {
        for (int i = 0; i < listeString_.size(); i++) {
            listeString_.set(i, null);
        }
    }

    /**
   * @return les diff�rents �l�ments de la ligne s�par�e par un espace.
   */
    public String toString() {
        String res = "";
        for (int i = 0; i < listeString_.size(); i++) {
            if (listeString_.get(i) == null) {
                res += " vide ";
            } else {
                res += listeString_.get(i) + ", ";
            }
        }
        res += "\n";
        return res;
    }
}
