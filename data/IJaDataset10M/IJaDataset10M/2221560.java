package org.fudaa.fudaa.sipor.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * g�re la structure des genes entre bateaux ppour un cercle donn�.
 * @author hadoux
 */
public class SiporStrutureGenes {

    /**
   * Liste de liste de genes.
   */
    List<List<Double>> vecteurDeVecteur_ = new ArrayList<List<Double>>();

    /**
   * Constructeur
   * 
   * @param numNavires: le nombre de navires deja cr��s; permet d initailiser la matrice, ie le vecteur de vecteur
   */
    public SiporStrutureGenes(final int numNavires) {
        initialisationCreationStructCercle(numNavires);
    }

    /**
   * Methode d 'initialisation de la matrice: a n'appeler que dans le cas de la cr�ation d un chenal.
   * 
   * @param numNavires
   */
    public void initialisationCreationStructCercle(final int numNavires) {
        for (int i = 0; i < numNavires; i++) {
            vecteurDeVecteur_.add(new ArrayList<Double>());
        }
        for (int i = 0; i < this.vecteurDeVecteur_.size(); i++) {
            final List<Double> vecteurI = (ArrayList<Double>) this.vecteurDeVecteur_.get(i);
            for (int j = 0; j < numNavires; j++) {
                vecteurI.add(new Double(0));
            }
        }
    }

    /**
   * Methode qui retourne l'�l�menti,j) de la matrice.
   * 
   * @param i indice ligne
   * @param j indice colonne
   * @return un objet de type Booleen qui contient la valeur pour la regle de navigation correspondante
   */
    public Double retournerAij(final int i, final int j) {
        final ArrayList<Double> vecteurI = (ArrayList<Double>) this.vecteurDeVecteur_.get(i);
        return ((Double) vecteurI.get(j));
    }

    /**
   * m�thode d'ajout. Lorsque l'on ajoute une cat�goriede navires, on doit ajouter un vecteur dans le vecteur de
   * vecteur et: pour chaque vecteur du vecteur de vecteur on ajoute un booleen initialis� a true:
   */
    public void ajoutNavire() {
        final ArrayList<Double> nouvelleLigne = new ArrayList<Double>();
        for (int i = 0; i < this.vecteurDeVecteur_.size(); i++) {
            nouvelleLigne.add(new Double(0));
        }
        vecteurDeVecteur_.add(nouvelleLigne);
        for (int i = 0; i < this.vecteurDeVecteur_.size(); i++) {
            final ArrayList<Double> vecteur = (ArrayList<Double>) this.vecteurDeVecteur_.get(i);
            vecteur.add(new Double(0));
        }
    }

    /**
   * Methode de suppression d'un navire:
   * 
   * @param numNavire. indice du navire a supprimer.
   */
    public void suppressionNavire(final int numNavire) {
        this.vecteurDeVecteur_.remove(numNavire);
        for (int i = 0; i < this.vecteurDeVecteur_.size(); i++) {
            final ArrayList<Double> V = (ArrayList<Double>) this.vecteurDeVecteur_.get(i);
            V.remove(numNavire);
        }
    }

    /**
   * Methode de modification des donn�e lors de la saisie des regles de navigations les parametres d entr�es sont un
   * couple (navire 1,navire 2).
   * 
   * @param reponse. double qui correspond a la saisie de l utilisateur
   * @param navire1. indice du navire 1
   * @param navire2. indice du navire 2
   */
    public void modification(final double donnee, final int navire1, final int navire2) {
        final ArrayList<Double> vectNav1 = (ArrayList<Double>) this.vecteurDeVecteur_.get(navire1);
        final Double val = new Double(donnee);
        vectNav1.set(navire2, val);
    }

    /**
   * Methode de duplication d un veceteur de vecteur a partir d un autre: permet d accelerer la saisie des dodonn�et de
   * faciliter l'utilisateur
   * 
   * @param VV veceteur de vecteur que l'on va recopier pour ce vecteur
   */
    public void duplication(final List<List<Double>> VV) {
        this.vecteurDeVecteur_ = new ArrayList<List<Double>>();
        for (int i = 0; i < VV.size(); i++) {
            final ArrayList<Double> nouveauVecteur = new ArrayList<Double>();
            final ArrayList<Double> vecteurLigne = (ArrayList<Double>) VV.get(i);
            for (int k = 0; k < vecteurLigne.size(); k++) {
                final Double donnee = (Double) vecteurLigne.get(k);
                final Double val = new Double(donnee.doubleValue());
                nouveauVecteur.add(val);
            }
            this.vecteurDeVecteur_.add(nouveauVecteur);
        }
    }

    public void affichage() {
        for (int i = 0; i < this.vecteurDeVecteur_.size(); i++) {
            System.out.println("");
            final ArrayList<Double> vecteurI = (ArrayList<Double>) this.vecteurDeVecteur_.get(i);
            for (int j = 0; j < vecteurI.size(); j++) {
                System.out.print("" + vecteurI.get(j).doubleValue());
            }
        }
    }

    public List<List<Double>> getVecteurDeVecteur_() {
        return vecteurDeVecteur_;
    }

    public void setVecteurDeVecteur_(List<List<Double>> vecteurDeVecteur_) {
        this.vecteurDeVecteur_ = vecteurDeVecteur_;
    }
}
