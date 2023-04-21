package org.fudaa.fudaa.sipor.structures;

import java.util.ArrayList;

/**
 * Classe de gestion des bassins. permet de stocker les donn�es relatives au bassin
 * 
 * @author Adrien Hadoux
 */
public class SiporCercles {

    /**
   * Liste de string contenant les noms des bassins.
   */
    ArrayList listeCercles_ = new ArrayList();

    /**
   * Methode d'ajout d'un cercle.
   * 
   * @param _c cercle � ajouter
   */
    public void ajout(final SiporCercle _c) {
        listeCercles_.add(_c);
        SiporDataSimulation.setProperty("cercle");
    }

    /**
   * Methode qui retourne le i eme cercle.
   * 
   * @param _i indice du cercle du tableau de cercles a retourner
   * @return un objet de type string qui pourra etre modifi et renvoy
   */
    public SiporCercle retournerCercle(final int _i) {
        if (_i < this.listeCercles_.size()) {
            return (SiporCercle) this.listeCercles_.get(_i);
        }
        return null;
    }

    /**
   * Methode de suppression du n ieme element de la liste de cercles.
   * 
   * @param _n
   */
    public void suppression(final int _n) {
        listeCercles_.remove(_n);
        SiporDataSimulation.setProperty("cercle");
    }

    /**
   * Methode de modification du n ieme cercle par le cercle entr� en parametre d entr�e.
   * 
   * @param n: indice du cercle a modifier
   * @param cercle a remplacer
   */
    public void modification(final int _n, final SiporCercle _c) {
        this.listeCercles_.set(_n, _c);
        SiporDataSimulation.setProperty("cercle");
    }

    /**
   * methode qui permet de determiner si le nom d'un bassin est unique ou s'il appartient deja a la liste des bassins.
   */
    public boolean existeDoublon(final String _nomComposant, final int _k) {
        for (int i = 0; i < this.listeCercles_.size(); i++) {
            if (i != _k) {
                if (this.retournerCercle(i).getNom_().equals(_nomComposant)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Methode qui retourne l'indice du cercle associ� au nom entr� en parametre. retourne -1 en cas de non existence
   * 
   * @param _nomCercle chaine de caracteres du cercle a trouver
   * @return l indice du tableau de cercle correpondant. -1 en acs de non existence
   */
    public int retourneIndice(final String _nomCercle) {
        for (int i = 0; i < this.listeCercles_.size(); i++) {
            if (this.retournerCercle(i).getNom_().equals(_nomCercle)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList getListeCercles_() {
        return listeCercles_;
    }

    public void setListeCercles_(ArrayList listeCercles_) {
        this.listeCercles_ = listeCercles_;
    }
}
