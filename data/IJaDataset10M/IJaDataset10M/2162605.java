package org.fudaa.fudaa.sinavi3;

import java.util.ArrayList;

/**
 * Classes regroupant les donn�es des ecluses.
 * 
 * @author Adrien Hadoux
 */
public class Sinavi3Ecluse {

    String nom_;

    double longueur_;

    double largeur_;

    int tempsFausseBassinneeMontant_;

    int tempsFausseBassinneeAvalant_;

    double profondeur_;

    double hauteurchute = 0;

    int dureeManoeuvreEntrant_;

    int dureeManoeuvreSortant_ = 24;

    Sinavi3Horaire h_ = new Sinavi3Horaire();

    double dureeIndispo_;

    double frequenceMoyenne_;

    int loiIndispo_;

    int loiFrequence_;

    /**
   * Type de loi ENTIER QUI PREND LA VALEUR DE LA LOI CHOISIE.<br>: 0 => loi d erlang<br>
   * 1 => deterministe <br>
   * 2 => journaliere par defaut loi d'erlang
   */
    int typeLoi_;

    /**
   * Declaration du tableau de loi deterministe.
   */
    ArrayList loiDeterministe_ = new ArrayList();

    /**
   * Gare amont de l ecluse par defaut mis a zero.
   */
    int gareAmont_;

    /**
   * gare aval mis par defaut a la gare 1.
   */
    int gareAval_ = 1;

    /**
   * constructeur par d�faut de la classe ecluse.
   */
    public Sinavi3Ecluse() {
    }

    /**
   * Constructeur de la classe ecluse pour une saisie des attributs.
   * 
   * @param _nom
   * @param _longueur
   * @param _largeur
   * @param _tempsEclusee
   * @param _tempsFausseBassinnee
   * @param _creneauEtaleAvantPleineMerDeb
   * @param _creneauEtaleAvantPleineMerFin
   * @param _creneauEtaleApresPleineMerDeb
   * @param _creneauEtaleApresPleineMerFin
   * @param _h
   * @param _gareAmont
   * @param _gareAval
   */
    public Sinavi3Ecluse(final String _nom, final double _longueur, final double _largeur, final int _tempsEclusee, final int _tempsFausseBassinnee, final int _creneauEtaleAvantPleineMerDeb, final double _creneauEtaleAvantPleineMerFin, final double _creneauEtaleApresPleineMerDeb, final int _creneauEtaleApresPleineMerFin, final Sinavi3Horaire _h, final int _gareAmont, final int _gareAval) {
        super();
        this.nom_ = _nom;
        this.longueur_ = _longueur;
        this.largeur_ = _largeur;
        this.tempsFausseBassinneeMontant_ = _tempsEclusee;
        this.tempsFausseBassinneeAvalant_ = _tempsFausseBassinnee;
        this.dureeManoeuvreEntrant_ = _creneauEtaleAvantPleineMerDeb;
        this.dureeManoeuvreSortant_ = _creneauEtaleApresPleineMerFin;
        this.h_ = _h;
        this.gareAmont_ = _gareAmont;
        this.gareAval_ = _gareAval;
    }

    String[] affichage() {
        final String[] t = new String[8];
        t[0] = " nom: " + nom_;
        t[1] = "\n longueur: " + (float) longueur_;
        t[2] = "\n largeur: " + (float) largeur_;
        t[3] = "\n temps eclusee: " + (float) tempsFausseBassinneeMontant_;
        t[4] = "\n fausse bassinnee: " + (float) tempsFausseBassinneeAvalant_;
        t[5] = "\n creneau etale:";
        t[6] = "\n        avant pleine mer: " + (float) this.dureeManoeuvreEntrant_;
        t[7] = "\n        apr�s pleine mer: " + (float) this.dureeManoeuvreSortant_;
        return t;
    }
}
