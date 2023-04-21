package org.fudaa.fudaa.sinavi2;

import org.fudaa.dodico.corba.sinavi2.SResultatConsommationDEau;

public class SinaviTypeConsommationDEau {

    /** * constructeur sans param�tre* */
    public SinaviTypeConsommationDEau() {
    }

    /** * constructeur avec param�tre* */
    public SinaviTypeConsommationDEau(final SResultatConsommationDEau _cons) {
        this.numeroEcluse_ = _cons.numeroEcluse;
        this.avalantMontant_ = avalantMontant(_cons.sens);
        this.dateHeureOuverture_ = _cons.dateHeureOuverture;
        this.dateHeureFermeture_ = _cons.dateHeureFermeture;
        this.nbBateaux_ = _cons.nombreBateaux;
        this.typeBateau_ = _cons.typeBateau;
    }

    /** constructeur avec param�tres* */
    public static char avalantMontant(final boolean _avalantMontant) {
        if (_avalantMontant) {
            return 'A';
        } else {
            return 'M';
        }
    }

    /** * accesseurs** */
    public char getAvalantMontant() {
        return avalantMontant_;
    }

    public void setAvalantMontant(final char _avalantMontant) {
        avalantMontant_ = _avalantMontant;
    }

    public int getNumeroEcluse() {
        return numeroEcluse_;
    }

    public void SetNumeroEcluse(final int _numeroEcluse) {
        numeroEcluse_ = _numeroEcluse;
    }

    public int getTypeBateau() {
        return typeBateau_;
    }

    public void SetTypeBateau(final int _typeBateau) {
        typeBateau_ = _typeBateau;
    }

    public double getDateHeureOuverture() {
        return dateHeureOuverture_;
    }

    public void SetDateHeureOuverture(final double _dateHeureOuverture) {
        dateHeureOuverture_ = _dateHeureOuverture;
    }

    public double getDateFermeture() {
        return dateHeureFermeture_;
    }

    public void SetDateHeureFermeture(final double _dateHeureFermeture) {
        dateHeureFermeture_ = _dateHeureFermeture;
    }

    public int getNbBateaux() {
        return nbBateaux_;
    }

    public void SetNbBateaux(final int _nbBateaux) {
        nbBateaux_ = _nbBateaux;
    }

    int numeroEcluse_;

    char avalantMontant_;

    double dateHeureOuverture_;

    double dateHeureFermeture_;

    int nbBateaux_;

    int typeBateau_;
}
