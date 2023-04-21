package org.fudaa.fudaa.sinavi2;

import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.corba.sinavi2.SParametresEcluse;

class SinaviTypeEcluse {

    public SinaviTypeEcluse() {
    }

    /**
   * @param id
   * @param longueur peut etre null
   * @param largeur_
   * @param profondeur
   * @param dur�e bassin�e montante
   * @param dur�e bassin�e descendante
   */
    public SinaviTypeEcluse(final String _id, final double _longueur, final double _largeur, final double _profondeur, final int _dureeBassineeMontanteMinute, final int _dureeBasineeMontanteSeconde, final int _dureeBassineeDescendanteMinute, final int _dureeBassineeDescendanteSeconde, final int _manoeuvreEntrantMinute, final int _manoeuvreEntrantSeconde, final int _manoeuvreSortantMinute, final int _manoeuvreSortantSeconde) {
        ecluse_ = new SParametresEcluse();
        ecluse_.identification = _id;
        ecluse_.longueur = _longueur;
        ecluse_.largeur = _largeur;
        ecluse_.profondeur = _profondeur;
        final int dureeBassineeDescendante = _dureeBassineeDescendanteMinute + _dureeBassineeDescendanteSeconde;
        final int dureeBassineeMontante = _dureeBassineeMontanteMinute + _dureeBasineeMontanteSeconde;
        ecluse_.dureeBassineeDescendante = determineHeure(dureeBassineeDescendante);
        ecluse_.dureeBassineeMontante = determineHeure(dureeBassineeMontante);
        ecluse_.gareEnAmont = 0;
        ecluse_.gareEnAval = 0;
        final int dureeManoeuvreEnEntree = _manoeuvreEntrantMinute + _manoeuvreEntrantSeconde;
        final int dureeManoeuvreEnSortie = _manoeuvreSortantMinute + _manoeuvreSortantSeconde;
        ecluse_.dureeManoeuvresEnEntree = determineHeure(dureeManoeuvreEnEntree);
        ecluse_.dureeManoeuvresEnSortie = determineHeure(dureeManoeuvreEnSortie);
    }

    public static int determineMinuteSeule(final double _heure) {
        final int m = (int) (_heure * 100);
        return m / 100 * 60;
    }

    public static int determineSecondeSeule(final double _heure) {
        final int m = (int) (_heure * 100);
        return m % 100;
    }

    public static double determineHeure(final int _nbSecondes) {
        final int minute = _nbSecondes / 60;
        final int seconde = _nbSecondes - minute * 60;
        String s;
        if (seconde > 9) {
            s = new String(minute + CtuluLibString.DOT + seconde);
        } else {
            s = new String(minute + ".0" + seconde);
        }
        final Double h = new Double(s);
        return h.doubleValue();
    }

    public static double determineHeure2(final int _nbSecondes) {
        final int heure = _nbSecondes / 3600;
        final int minute = _nbSecondes / 60 - heure * 60;
        String s;
        if (minute > 9) {
            s = new String(heure + CtuluLibString.DOT + minute);
        } else {
            s = new String(heure + ".0" + minute);
        }
        final Double h = new Double(s);
        return h.doubleValue();
    }

    public static int determineSeconde(final double _heure) {
        final int m = (int) (_heure * 100);
        final int sec = m % 100;
        final int min = m / 100 * 60;
        return sec + min;
    }

    public String getIdentification() {
        return ecluse_.identification;
    }

    public double getLongueur() {
        return ecluse_.longueur;
    }

    public double getLargeur() {
        return ecluse_.largeur;
    }

    public double getProfondeur() {
        return ecluse_.profondeur;
    }

    public double getDureeBassineeDescendante() {
        return ecluse_.dureeBassineeDescendante;
    }

    public double getDureeBassineeMontante() {
        return ecluse_.dureeBassineeMontante;
    }

    public double getDureeManoeuvresEnEntree() {
        return ecluse_.dureeManoeuvresEnEntree;
    }

    public double getDureeManoeuvresEnSortie() {
        return ecluse_.dureeManoeuvresEnSortie;
    }

    public void setIdentification(final String _id) {
        ecluse_.identification = _id;
    }

    public void setLongueur(final double _longueur) {
        ecluse_.longueur = _longueur;
    }

    public void setLargeur(final double _largeur) {
        ecluse_.largeur = _largeur;
    }

    public void setProfondeur(final double _profondeur) {
        ecluse_.profondeur = _profondeur;
    }

    public void setDureeBassineeMontante(final double _dureeBassineeMontante) {
        ecluse_.dureeBassineeMontante = _dureeBassineeMontante;
    }

    public void setDureeBassineeDescendante(final double _dureeBassineeDescendante) {
        ecluse_.dureeBassineeDescendante = _dureeBassineeDescendante;
    }

    public void setDureeManoeuvresEnEntree(final double _dureeManoeuvresEnEntree) {
        ecluse_.dureeManoeuvresEnEntree = _dureeManoeuvresEnEntree;
    }

    public void setDureeManoeuvresEnSortie(final double _dureeManoeuvresEnSortie) {
        ecluse_.dureeManoeuvresEnSortie = _dureeManoeuvresEnSortie;
    }

    public boolean typeEcluseEquals(final SinaviTypeEcluse _b) {
        return (this.getProfondeur() == _b.getProfondeur() && this.getDureeBassineeDescendante() == _b.getDureeBassineeDescendante() && this.getDureeBassineeMontante() == _b.getDureeBassineeMontante() && this.getIdentification() == _b.getIdentification() && this.getLargeur() == _b.getLargeur() && this.getLongueur() == _b.getLongueur());
    }

    SParametresEcluse ecluse_;
}
