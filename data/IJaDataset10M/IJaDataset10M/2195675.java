package org.fudaa.fudaa.sinavi2;

/**
 * @author maneuvrier Le type Sinavi2TypeAttente a �t� cr�� afin de selectionner des donn�es dans le fichier *.his pour
 *         r�aliser des graphes et faire des calculs et notamment des %
 */
public class Sinavi2TypeAttente {

    /**
   * constructeur avec initialisation si besoin
   */
    public Sinavi2TypeAttente() {
    }

    /**
   * constructeur de recopie
   */
    public Sinavi2TypeAttente(final Sinavi2TypeAttente _attente) {
        element_ = _attente.element_;
        typeElement_ = _attente.typeElement_;
        typeBateaux_ = _attente.typeBateaux_;
        nbBatMontant_ = _attente.nbBatMontant_;
        nbBatAvalant_ = _attente.nbBatAvalant_;
        nbBatTotal_ = _attente.nbBatTotal_;
        nbBatMontantAtt_ = _attente.nbBatMontantAtt_;
        dureeMinMontantAtt_ = _attente.dureeMinMontantAtt_;
        dureeMoyMontantAtt_ = _attente.dureeMoyMontantAtt_;
        dureeMoyTtlMontantAtt_ = _attente.dureeMoyTtlMontantAtt_;
        dureeMaxMontantAtt_ = _attente.dureeMaxMontantAtt_;
        dureeTtlMontantAtt_ = _attente.dureeTtlMontantAtt_;
        nbBatAvalantAtt_ = _attente.nbBatAvalantAtt_;
        dureeMinAvalantAtt_ = _attente.dureeMinAvalantAtt_;
        dureeMoyAvalantAtt_ = _attente.dureeMoyAvalantAtt_;
        dureeMoyTtlAvalantAtt_ = _attente.dureeMoyTtlAvalantAtt_;
        dureeMaxAvalantAtt_ = _attente.dureeMaxAvalantAtt_;
        dureeTtlAvalantAtt_ = _attente.dureeTtlAvalantAtt_;
        nbBatTotalAtt_ = _attente.nbBatTotalAtt_;
        dureeMinTotalAtt_ = _attente.dureeMinTotalAtt_;
        dureeMoyTotalAtt_ = _attente.dureeMoyTotalAtt_;
        dureeMoyTtlTotalAtt_ = _attente.dureeMoyTtlTotalAtt_;
        dureeMaxTotalAtt_ = _attente.dureeMaxTotalAtt_;
        dureeTtlTotalAtt_ = _attente.dureeTtlTotalAtt_;
        pourcentageMontantAtt_ = _attente.pourcentageMontantAtt_;
        pourcentageAvalantAtt_ = _attente.pourcentageAvalantAtt_;
        pourcentageTotalAtt_ = _attente.pourcentageTotalAtt_;
        listeMontantDesTempsDAtt_ = _attente.listeMontantDesTempsDAtt_;
        listeAvalantDesTempsDAtt_ = _attente.listeAvalantDesTempsDAtt_;
        dejaReguler_ = false;
    }

    public double getDureeMaxAvalantAtt() {
        return dureeMaxAvalantAtt_;
    }

    public void setDureeMaxAvalantAtt(final double _dureeMaxAvalantAtt) {
        dureeMaxAvalantAtt_ = _dureeMaxAvalantAtt;
    }

    public double getDureeMaxMontantAtt() {
        return dureeMaxMontantAtt_;
    }

    public void setDureeMaxMontantAtt(final double _dureeMaxMontantAtt) {
        dureeMaxMontantAtt_ = _dureeMaxMontantAtt;
    }

    public double getDureeMaxTotalAtt() {
        return dureeMaxTotalAtt_;
    }

    public void setDureeMaxTotalAtt(final double _dureeMaxTotalAtt) {
        dureeMaxTotalAtt_ = _dureeMaxTotalAtt;
    }

    public double getDureeMinAvalantAtt() {
        return dureeMinAvalantAtt_;
    }

    public void setDureeMinAvalantAtt(final double _dureeMinAvalantAtt) {
        dureeMinAvalantAtt_ = _dureeMinAvalantAtt;
    }

    public double getDureeMinMontantAtt() {
        return dureeMinMontantAtt_;
    }

    public void setDureeMinMontantAtt(final double _dureeMinMontantAtt) {
        dureeMinMontantAtt_ = _dureeMinMontantAtt;
    }

    public double getDureeMinTotalAtt() {
        return dureeMinTotalAtt_;
    }

    public void setDureeMinTotalAtt(final double _dureeMinTotalAtt) {
        dureeMinTotalAtt_ = _dureeMinTotalAtt;
    }

    public double getDureeMoyAvalantAtt() {
        return dureeMoyAvalantAtt_;
    }

    public void setDureeMoyAvalantAtt(final double _dureeMoyAvalantAtt) {
        dureeMoyAvalantAtt_ = _dureeMoyAvalantAtt;
    }

    public double getDureeMoyMontantAtt() {
        return dureeMoyMontantAtt_;
    }

    public void setDureeMoyMontantAtt(final double _dureeMoyMontantAtt) {
        dureeMoyMontantAtt_ = _dureeMoyMontantAtt;
    }

    public double getDureeMoyTotalAtt() {
        return dureeMoyTotalAtt_;
    }

    public void setDureeMoyTotalAtt(final double _dureeMoyTotalAtt) {
        dureeMoyTotalAtt_ = _dureeMoyTotalAtt;
    }

    public double getDureeTtlAvalantAtt() {
        return dureeTtlAvalantAtt_;
    }

    public void setDureeTtlAvalantAtt(final double _dureeTtlAvalantAtt) {
        dureeTtlAvalantAtt_ = _dureeTtlAvalantAtt;
    }

    public double getDureeTtlMontantAtt() {
        return dureeTtlMontantAtt_;
    }

    public void setDureeTtlMontantAtt(final double _dureeTtlMontantAtt) {
        dureeTtlMontantAtt_ = _dureeTtlMontantAtt;
    }

    public double getDureeTtlTotalAtt() {
        return dureeTtlTotalAtt_;
    }

    public void setDureeTtlTotalAtt(final double _dureeTtlTotalAtt) {
        dureeTtlTotalAtt_ = _dureeTtlTotalAtt;
    }

    public String getElement() {
        return element_;
    }

    public void setElement(final String _element) {
        element_ = _element;
    }

    public int getNbBatAvalant() {
        return nbBatAvalant_;
    }

    public void setNbBatAvalant(final int _nbBatAvalant) {
        nbBatAvalant_ = _nbBatAvalant;
    }

    public int getNbBatAvalantAtt() {
        return nbBatAvalantAtt_;
    }

    public void setNbBatAvalantAtt(final int _nbBatAvalantAtt) {
        nbBatAvalantAtt_ = _nbBatAvalantAtt;
    }

    public int getNbBatMontant() {
        return nbBatMontant_;
    }

    public void setNbBatMontant(final int _nbBatMontant) {
        nbBatMontant_ = _nbBatMontant;
    }

    public int getNbBatMontantAtt() {
        return nbBatMontantAtt_;
    }

    public void setNbBatMontantAtt(final int _nbBatMontantAtt) {
        nbBatMontantAtt_ = _nbBatMontantAtt;
    }

    public int getNbBatTotal() {
        return nbBatTotal_;
    }

    public void setNbBatTotal(final int _nbBatTotal) {
        nbBatTotal_ = _nbBatTotal;
    }

    public int getNbBatTotalAtt() {
        return nbBatTotalAtt_;
    }

    public void setNbBatTotalAtt(final int _nbBatTotalAtt) {
        nbBatTotalAtt_ = _nbBatTotalAtt;
    }

    public double getPourcentageAvalantAtt() {
        return pourcentageAvalantAtt_;
    }

    public void setPourcentageAvalantAtt(final double _pourcentageAvalantAtt) {
        pourcentageAvalantAtt_ = _pourcentageAvalantAtt;
    }

    public double getPourcentageMontantAtt() {
        return pourcentageMontantAtt_;
    }

    public void setPourcentageMontantAtt(final double _pourcentageMontantAtt) {
        pourcentageMontantAtt_ = _pourcentageMontantAtt;
    }

    public double getPourcentageTotalAtt() {
        return pourcentageTotalAtt_;
    }

    public void setPourcentageTotalAtt(final double _pourcentageTotalAtt) {
        pourcentageTotalAtt_ = _pourcentageTotalAtt;
    }

    public String getTypeBateaux() {
        return typeBateaux_;
    }

    public void setTypeBateaux(final String _typeBateaux) {
        typeBateaux_ = _typeBateaux;
    }

    public char getTypeElement() {
        return typeElement_;
    }

    public void setTypeElement(final char _typeElement) {
        typeElement_ = _typeElement;
    }

    /**
   * calcul les totaux en regardant les valeur avalant et montant
   */
    public void calculeTotaux() {
        nbBatTotal_ = nbBatAvalant_ + nbBatMontant_;
        nbBatTotalAtt_ = nbBatAvalantAtt_ + nbBatMontantAtt_;
        if (dureeMaxAvalantAtt_ > dureeMaxMontantAtt_) {
            dureeMaxTotalAtt_ = dureeMaxAvalantAtt_;
        } else {
            dureeMaxTotalAtt_ = dureeMaxMontantAtt_;
        }
        if (dureeMinAvalantAtt_ > dureeMinMontantAtt_) {
            dureeMinTotalAtt_ = dureeMinAvalantAtt_;
        } else {
            dureeMinTotalAtt_ = dureeMinMontantAtt_;
        }
    }

    /**
   * calcul des moyennes des attentes pour avalant montant et les deux sens moyenne des attentes sur les bateaux qui
   * attendent et moyenne sur tous les bateaux
   */
    public void calculeMoy() {
        double temp = 0;
        if (listeMontantDesTempsDAtt_ != null) {
            for (int i = 0; i < (listeMontantDesTempsDAtt_.length); i++) {
                temp += listeMontantDesTempsDAtt_[i];
            }
        }
        dureeTtlMontantAtt_ = temp;
        System.out.println("temp :" + temp);
        dureeMoyMontantAtt_ = temp / nbBatMontantAtt_;
        dureeMoyTtlMontantAtt_ = temp / nbBatMontant_;
        System.out.println("duree moy mont " + dureeMoyMontantAtt_);
        double temp2 = 0;
        if (listeAvalantDesTempsDAtt_ != null) {
            for (int i = 0; i < (listeAvalantDesTempsDAtt_.length); i++) {
                temp2 += listeAvalantDesTempsDAtt_[i];
            }
        }
        dureeTtlAvalantAtt_ = temp2;
        System.out.println("duree moy aval " + dureeMoyAvalantAtt_);
        dureeMoyAvalantAtt_ = temp2 / nbBatAvalantAtt_;
        dureeMoyTtlAvalantAtt_ = temp2 / nbBatAvalant_;
        dureeMoyTotalAtt_ = temp + temp2;
        dureeMoyTotalAtt_ = dureeMoyTotalAtt_ / nbBatTotalAtt_;
        dureeMoyTtlTotalAtt_ = temp + temp2;
        dureeMoyTtlTotalAtt_ = dureeMoyTtlTotalAtt_ / nbBatTotal_;
        System.out.println("duree moy total " + dureeMoyTotalAtt_);
        dureeTtlTotalAtt_ = temp + temp2;
    }

    /**
   * On a fait les moyennes sur NB_SIMULATIONS simulations on veut donc r�tablir les valeurs � 1 simulation en ce qui
   * concerne le nombre de bateaux et les dur�es totales des attentes
   */
    public void reguleNbBateaux() {
        if (!dejaReguler_) {
            nbBatMontant_ = Math.round(nbBatMontant_ / Sinavi2Implementation.NB_SIMULATIONS);
            nbBatAvalant_ = Math.round(nbBatAvalant_ / Sinavi2Implementation.NB_SIMULATIONS);
            nbBatTotal_ = nbBatMontant_ + nbBatAvalant_;
            nbBatMontantAtt_ = Math.round(nbBatMontantAtt_ / Sinavi2Implementation.NB_SIMULATIONS);
            nbBatAvalantAtt_ = Math.round(nbBatAvalantAtt_ / Sinavi2Implementation.NB_SIMULATIONS);
            nbBatTotalAtt_ = nbBatMontantAtt_ + nbBatAvalantAtt_;
            dureeTtlMontantAtt_ = Math.round(dureeTtlMontantAtt_ / Sinavi2Implementation.NB_SIMULATIONS);
            dureeTtlAvalantAtt_ = Math.round(dureeTtlAvalantAtt_ / Sinavi2Implementation.NB_SIMULATIONS);
            dureeTtlTotalAtt_ = dureeTtlMontantAtt_ + dureeTtlAvalantAtt_;
            dejaReguler_ = true;
        }
    }

    /**
   * calcul tous les pourcentages
   * 
   * @param _montant :sens montant
   * @param _avalant : sens avalant true true : total true false : montant false true : avalant
   */
    public void calculePourcentage(final boolean _montant, final boolean _avalant) {
        if (_montant && _avalant) {
            pourcentageTotalAtt_ = nbBatTotalAtt_ * 100;
            pourcentageTotalAtt_ = pourcentageTotalAtt_ / nbBatTotal_;
            final Double d = new Double(pourcentageTotalAtt_);
            if (d.isNaN()) {
                pourcentageTotalAtt_ = 0;
            } else {
                pourcentageTotalAtt_ = Sinavi2Lib.conversionDeuxChiffres(pourcentageTotalAtt_);
            }
            System.out.println(nbBatTotalAtt_ + "/" + nbBatTotal_ + "*100");
            System.out.println(pourcentageTotalAtt_ + "%");
        } else if (_montant) {
            pourcentageMontantAtt_ = nbBatMontantAtt_ * 100;
            pourcentageMontantAtt_ = pourcentageMontantAtt_ / nbBatMontant_;
            final Double d = new Double(pourcentageMontantAtt_);
            if (d.isNaN()) {
                pourcentageMontantAtt_ = 0;
            } else {
                pourcentageMontantAtt_ = Sinavi2Lib.conversionDeuxChiffres(pourcentageMontantAtt_);
            }
        } else if (_avalant) {
            pourcentageAvalantAtt_ = nbBatAvalantAtt_ * 100;
            pourcentageAvalantAtt_ = pourcentageAvalantAtt_ / nbBatAvalant_;
            final Double d = new Double(pourcentageAvalantAtt_);
            if (d.isNaN()) {
                pourcentageAvalantAtt_ = 0;
            } else {
                pourcentageAvalantAtt_ = Sinavi2Lib.conversionDeuxChiffres(pourcentageAvalantAtt_);
            }
        }
    }

    /**
   * g�rer le cas ou la valeur n'est pas un nombre et la mettre � 0 pour �viter les erreurs
   */
    public void ConversionDouble() {
        Double d = new Double(dureeMoyMontantAtt_);
        if (d.isNaN()) {
            dureeMoyMontantAtt_ = 0;
        }
        d = new Double(dureeMoyTtlMontantAtt_);
        if (d.isNaN()) {
            dureeMoyTtlMontantAtt_ = 0;
        }
        d = new Double(dureeMoyAvalantAtt_);
        if (d.isNaN()) {
            dureeMoyAvalantAtt_ = 0;
        }
        d = new Double(dureeMoyTtlAvalantAtt_);
        if (d.isNaN()) {
            dureeMoyTtlAvalantAtt_ = 0;
        }
        d = new Double(dureeMoyTotalAtt_);
        if (d.isNaN()) {
            dureeMoyTotalAtt_ = 0;
        }
        d = new Double(dureeMoyTtlTotalAtt_);
        if (d.isNaN()) {
            dureeMoyTtlTotalAtt_ = 0;
        }
    }

    public String element_;

    public char typeElement_;

    String typeBateaux_;

    int nbBatMontant_;

    int nbBatAvalant_;

    int nbBatTotal_;

    int nbBatMontantAtt_;

    double dureeMinMontantAtt_;

    double dureeMoyMontantAtt_;

    double dureeMoyTtlMontantAtt_;

    double dureeMaxMontantAtt_;

    double dureeTtlMontantAtt_;

    int nbBatAvalantAtt_;

    double dureeMinAvalantAtt_;

    double dureeMoyAvalantAtt_;

    double dureeMoyTtlAvalantAtt_;

    double dureeMaxAvalantAtt_;

    double dureeTtlAvalantAtt_;

    int nbBatTotalAtt_;

    double dureeMinTotalAtt_;

    double dureeMoyTotalAtt_;

    double dureeMoyTtlTotalAtt_;

    double dureeMaxTotalAtt_;

    double dureeTtlTotalAtt_;

    public double pourcentageMontantAtt_;

    public double pourcentageAvalantAtt_;

    public double pourcentageTotalAtt_;

    public double[] listeMontantDesTempsDAtt_;

    public double[] listeAvalantDesTempsDAtt_;

    public boolean dejaReguler_ = false;

    public double getDureeMoyTtlAvalantAtt() {
        return dureeMoyTtlAvalantAtt_;
    }

    public void setDureeMoyTtlAvalantAtt(final double _dureeMoyTtlAvalantAtt) {
        dureeMoyTtlAvalantAtt_ = _dureeMoyTtlAvalantAtt;
    }

    public double getDureeMoyTtlMontantAtt() {
        return dureeMoyTtlMontantAtt_;
    }

    public void setDureeMoyTtlMontantAtt(final double _dureeMoyTtlMontantAtt) {
        dureeMoyTtlMontantAtt_ = _dureeMoyTtlMontantAtt;
    }

    public double getDureeMoyTtlTotalAtt() {
        return dureeMoyTtlTotalAtt_;
    }

    public void setDureeMoyTtlTotalAtt(final double _dureeMoyTtlTotalAtt) {
        dureeMoyTtlTotalAtt_ = _dureeMoyTtlTotalAtt;
    }
}
