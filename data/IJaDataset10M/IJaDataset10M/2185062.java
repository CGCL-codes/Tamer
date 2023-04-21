package org.fudaa.fudaa.sipor.structures;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de gestion des diffrents horaires.
 * 
 * @author Adrien Hadoux
 */
public class SiporListeHoraires {

    /**
   * Tableau d'horaires.
   */
    List lHoraires_ = new ArrayList();

    int nbHoraires_;

    public SiporListeHoraires() {
    }

    /**
   * Mthode d'ajout d'un horaire.
   */
    public void ajout(final SiporHoraire _h) {
        lHoraires_.add(_h);
        nbHoraires_++;
    }

    /**
   * Methode de modification d'un horaire: le n ieme horaire est remplac par l'horaire h.
   * 
   * @param _n indice de l'horaire modifier
   * @param _h objet de type horaire a remplacer
   */
    public void modification(final int _n, final SiporHoraire _h) {
        this.lHoraires_.set(_n, _h);
    }

    /**
   * Methode qui retourne le i eme Horaire tres puissant car c'est cette methode qui sera a la base de la modification
   * d'un horaire: il suffit de faire appel a la fonction et de stock le horaire retourn dans une variable temp, de
   * modifier cette variable temp, ce qui aura alors pour effet de modifier le contenu du tableau d'horaires!
   * 
   * @param _i indice du horaire du tableau de horaires a retourner
   * @return un objet de type horaire qui pourra etre modifi et renvoy
   */
    public SiporHoraire retournerHoraire(final int _i) {
        if (_i < this.lHoraires_.size()) {
            return (SiporHoraire) this.lHoraires_.get(_i);
        }
        return null;
    }

    /**
   * Methode de suppression d'un Horaire.
   * 
   * @param _n entier correspondant l'indice de l'horaire a detruire
   */
    public void suppression(final int _n) {
        lHoraires_.remove(_n);
    }

    /**
   * Methode d'affichage de la liste de quais.
   */
    public void affichage() {
        for (int i = 0; i < this.lHoraires_.size(); i++) {
            ((SiporHoraire) lHoraires_.get(i)).affichage();
        }
    }

    public List getlHoraires_() {
        return lHoraires_;
    }

    public void setlHoraires_(List lHoraires_) {
        this.lHoraires_ = lHoraires_;
    }

    public int getNbHoraires_() {
        return nbHoraires_;
    }

    public void setNbHoraires_(int nbHoraires_) {
        this.nbHoraires_ = nbHoraires_;
    }
}
