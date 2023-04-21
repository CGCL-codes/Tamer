package org.fudaa.fudaa.sipor;

import java.util.ArrayList;

/**
 * Declaration d'une classe servant de structure pour un tableau de stockage de couple dans le cas de la loi
 * deterministe
 */
public class SiporQuais {

    /**
   * Nom du quai
   */
    String Nom_;

    /**
   * Dimension du port
   */
    double longueur_;

    /**
   * Booleen qui precise sir le dehalage est autorise auquel cas c 'est true
   */
    boolean dehalageAutorise_ = true;

    /**
   * Indice qui correspond au numro du bassin qui contient le quai
   */
    int bassin_;

    /**
   * chaine de caractere correpondante au nom du bassin choisi
   */
    String nomBassin_;

    double dureeIndispo_;

    double frequenceMoyenne_;

    int loiIndispo_;

    int loiFrequence_;

    /**
   * Type de loi ENTIER QUI PREND LA VALEUR DE LA LOI CHOISIE: 0 => loi d erlang 1 => deterministe 2 => journaliere par
   * defaut loi d'erlang
   */
    int typeLoi_ = 0;

    /**
   * Declaration du tableau de loi deterministe:
   */
    ArrayList loiDeterministe_ = new ArrayList();

    SiporHoraire h_ = new SiporHoraire();

    SiporQuais() {
    }

    /**
   * Methode d affichage d un quai
   */
    void affichage() {
        System.out.println("\nNom du Quai: " + this.Nom_ + "\nLongueur: " + this.longueur_);
        if (this.dehalageAutorise_) {
            System.out.println("Dehalage autorise");
        } else {
            System.out.println("Dehalage interdit");
        }
    }
}
