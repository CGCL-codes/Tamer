package org.fudaa.fudaa.sinavi3;

import java.util.ArrayList;

/**
 * Classe de gestion des donn�es des gares: on g�re un tableau(arrayList) de string, chaque cha�ne correspondant � un
 * nom de gare. les methode d'acces,de suppression, d'ajout et de modification y figurent. ce sont les plus importantes.
 * 
 * @author Adrien Hadoux
 */
public class Sinavi3Gares {

    /**
   * Liste de string contenant les noms des gares.
   */
    ArrayList listeGares_ = new ArrayList();

    /**
   * Methode d'ajout d'une gare.
   * 
   * @param _nom nom de la gare
   */
    void ajout(final String _nom) {
        listeGares_.add(_nom);
        Sinavi3DataSimulation.setProperty("gare");
    }

    /**
   * Methode qui retourne la i eme gare.
   * 
   * @param _i indice de la gare du tableau de gare a retourner
   * @return un objet de type string qui pourra etre modifi� et renvoy�
   */
    String retournerGare(final int _i) {
        if (_i < this.listeGares_.size()) {
            return (String) this.listeGares_.get(_i);
        }
        return null;
    }

    /**
   * Methode de suppression du n ieme element de la liste de gares.
   * 
   * @param _n indice de l'�l�ment � supprimer dans le tableau de gares
   */
    void suppression(final int _n) {
        listeGares_.remove(_n);
        Sinavi3DataSimulation.setProperty("gare");
    }

    /**
   * Methode de modification de la n ieme gare par la gare entr�e en parametre d entr�e.
   * 
   * @param n: indice de la gare a modifier
   * @param gare a remplacer
   */
    void modification(final int _n, final String _gare) {
        this.listeGares_.set(_n, _gare);
        Sinavi3DataSimulation.setProperty("gare");
    }

    /**
   * Methode qui permet de reetrouver l'indice de la gare dans le tableau de gare par rapport � son nom entr� en
   * param�tre.
   */
    int retrouverGare(final String _nomGare) {
        for (int i = 0; i < this.listeGares_.size(); i++) {
            if (this.retournerGare(i).equals(_nomGare)) {
                return i;
            }
        }
        return -1;
    }

    /**
   * methode qui permet de determiner si le nom d'un bassin est unique ou s'il appartient deja a la liste des bassins.
   */
    boolean existeDoublon(final String _nomComposant, final int _k) {
        for (int i = 0; i < this.listeGares_.size(); i++) {
            if (i != _k) {
                if (this.retournerGare(i).equals(_nomComposant)) {
                    return true;
                }
            }
        }
        return false;
    }
}
