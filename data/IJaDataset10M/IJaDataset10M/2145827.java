package org.fudaa.fudaa.sipor;

import java.util.ArrayList;

/**
 * Classe de gestion des bassins permet de stocker les donnes relatives au bassin.
 * 
 * @author Adrien Hadoux
 */
public class SiporBassins {

    /**
   * Liste de string contenant les noms des bassins.
   */
    ArrayList listeBassins_ = new ArrayList();

    /**
   * Methode d'ajout d'un bassin.
   * 
   * @param _nom nom du bassin
   */
    void ajout(final String _nom) {
        final SiporBassin b = new SiporBassin();
        b.nom_ = _nom;
        listeBassins_.add(b);
        SiporDataSimulation.setProperty("bassin");
    }

    /**
   * Methode d'ajout d'un bassin.
   * 
   * @param _nom nom du bassin
   */
    void ajout(final SiporBassin _b) {
        listeBassins_.add(_b);
        SiporDataSimulation.setProperty("bassin");
    }

    /**
   * Methode qui retourne le i eme bassin.
   * 
   * @param _i indice du bassin du tableau de bassin a retourner
   * @return un objet de type string qui pourra etre modifi et renvoy
   */
    String retournerBassin(final int _i) {
        if (_i < this.listeBassins_.size()) return ((SiporBassin) this.listeBassins_.get(_i)).nom_;
        return null;
    }

    /**
   * Methode qui retourne le i eme bassin.
   * 
   * @param _i indice du bassin du tableau de bassin a retourner
   * @return un objet de type Bassin qui pourra etre modifi et renvoy
   */
    SiporBassin retournerBassin2(final int _i) {
        if (_i < this.listeBassins_.size()) {
            return ((SiporBassin) this.listeBassins_.get(_i));
        }
        return null;
    }

    /**
   * Methode de suppression du n ieme element de la liste de basssins.
   * 
   * @param _n
   */
    void suppression(final int _n) {
        listeBassins_.remove(_n);
        SiporDataSimulation.setProperty("bassin");
    }

    /**
   * Methode de modification du n ieme bassin par le bassin entr en parametre d entre.
   * 
   * @param n: indice du bassin a modifier
   * @param _bassin a remplacer
   */
    void modification(final int _n, final String _bassin) {
        final SiporBassin b = new SiporBassin();
        b.nom_ = _bassin;
        this.listeBassins_.set(_n, b);
        SiporDataSimulation.setProperty("bassin");
    }

    /**
   * methode qui permet de determiner si le nom d'un bassin est unique ou s'il appartient deja a la liste des bassins.
   */
    boolean existeDoublon(final String _nomComposant, final int _k) {
        for (int i = 0; i < this.listeBassins_.size(); i++) {
            if (i != _k) {
                if (this.retournerBassin(i).equals(_nomComposant)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
   * Super methode qui permet de retrouver le numero du bassin par rapport a son nom.
   * 
   * @param _nomBassin chaine de caractere correpondante au nom du bassin recherch
   * @return int indice du bassin (portant le nom entr en parametre) dans la liste des bassins
   * @return -1 si le bassin recherch ne figure pas dans la liste!!!!!!!!!!
   */
    int retrouveBassin(final String _nomBassin) {
        for (int i = 0; i < this.listeBassins_.size(); i++) {
            if (this.retournerBassin(i).equals(_nomBassin)) {
                return i;
            }
        }
        return -1;
    }
}
