package org.fudaa.fudaa.hydraulique1d.metier.qualitedeau;

import org.fudaa.fudaa.hydraulique1d.metier.evenement.Notifieur;
import org.fudaa.fudaa.hydraulique1d.metier.qualitedeau.MetierOptionTraceur;
import org.fudaa.fudaa.hydraulique1d.metier.qualitedeau.MetierParametresConvecDiffu;
import org.fudaa.fudaa.hydraulique1d.metier.qualitedeau.EnumMetierOptionConvec;
import org.fudaa.fudaa.hydraulique1d.metier.qualitedeau.EnumMetierOptionDiffus;
import org.fudaa.fudaa.hydraulique1d.metier.MetierHydraulique1d;

public class MetierParametresConvecDiffu extends MetierHydraulique1d {

    public MetierParametresConvecDiffu() {
        super();
        optionDesTracers_ = new MetierOptionTraceur[0];
        optionConvection_ = EnumMetierOptionConvec.HYP1FA_CONS;
        ordreSchemaConvec_ = 1;
        paramW_ = 0;
        LimitPente_ = false;
        optionCalculDiffusion_ = EnumMetierOptionDiffus.K_C1U_C2;
        coeffDiffusion1_ = 0;
        coeffDiffusion2_ = 0;
        notifieObjetCree();
    }

    /**
     * LimitPente
     *
     * @return boolean
     */
    private boolean LimitPente_;

    public boolean LimitPente() {
        return LimitPente_;
    }

    /**
     * LimitPente
     *
     * @param newLimitPente boolean
     */
    public void LimitPente(boolean newLimitPente) {
        if (LimitPente_ == newLimitPente) return;
        LimitPente_ = newLimitPente;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "LimitPente");
    }

    /**
     * coeffDiffusion1
     *
     * @return double
     */
    private double coeffDiffusion1_;

    public double coeffDiffusion1() {
        return coeffDiffusion1_;
    }

    /**
     * coeffDiffusion1
     *
     * @param newCoeffDiffusion1 double
     */
    public void coeffDiffusion1(double newCoeffDiffusion1) {
        if (coeffDiffusion1_ == newCoeffDiffusion1) return;
        coeffDiffusion1_ = newCoeffDiffusion1;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "coeffDiffusion1");
    }

    /**
     * coeffDiffusion2
     *
     * @return double
     */
    private double coeffDiffusion2_;

    public double coeffDiffusion2() {
        return coeffDiffusion2_;
    }

    /**
     * coeffDiffusion2
     *
     * @param newCoeffDiffusion2 double
     */
    public void coeffDiffusion2(double newCoeffDiffusion2) {
        if (coeffDiffusion2_ == newCoeffDiffusion2) return;
        coeffDiffusion2_ = newCoeffDiffusion2;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "coeffDiffusion2");
    }

    /**
     * creeClone
     *
     * @return MetierHydraulique1d
     */
    public MetierHydraulique1d creeClone() {
        MetierParametresConvecDiffu p = new MetierParametresConvecDiffu();
        p.initialise(this);
        return p;
    }

    /**
     * dispose
     *
     */
    public void dispose() {
        optionDesTracers_ = null;
        optionConvection_ = EnumMetierOptionConvec.HYP1FA_CONS;
        ordreSchemaConvec_ = 0;
        paramW_ = 0;
        LimitPente_ = false;
        optionCalculDiffusion_ = EnumMetierOptionDiffus.K_C1U_C2;
        coeffDiffusion1_ = 0;
        coeffDiffusion2_ = 0;
    }

    /**
     * initialise
     *
     * @param o MetierHydraulique1d

     */
    public void initialise(MetierHydraulique1d _o) {
        if (_o instanceof MetierParametresConvecDiffu) {
            MetierParametresConvecDiffu q = (MetierParametresConvecDiffu) _o;
            optionDesTracers((MetierOptionTraceur[]) q.optionDesTracers().clone());
            optionConvection(q.optionConvection());
            ordreSchemaConvec(q.ordreSchemaConvec());
            paramW(q.paramW());
            LimitPente(q.LimitPente());
            optionCalculDiffusion(q.optionCalculDiffusion());
            coeffDiffusion1(q.coeffDiffusion1());
            coeffDiffusion2(q.coeffDiffusion2());
        }
    }

    /**
     * optionCalculDiffusion
     *
     * @return EnumMetierOptionDiffus
     */
    private EnumMetierOptionDiffus optionCalculDiffusion_;

    public EnumMetierOptionDiffus optionCalculDiffusion() {
        return optionCalculDiffusion_;
    }

    /**
     * optionCalculDiffusion
     *
     * @param newOptionCalculDiffusion EnumMetierOptionDiffus
     */
    public void optionCalculDiffusion(EnumMetierOptionDiffus newOptionCalculDiffusion) {
        if (optionCalculDiffusion_ == newOptionCalculDiffusion) return;
        optionCalculDiffusion_ = newOptionCalculDiffusion;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "optionCalculDiffusion");
    }

    /**
     * optionConvection
     *
     * @return EnumMetierOptionConvec
     */
    private EnumMetierOptionConvec optionConvection_;

    public EnumMetierOptionConvec optionConvection() {
        return optionConvection_;
    }

    /**
     * optionConvection
     *
     * @param newOptionConvection EnumMetierOptionConvec
     */
    public void optionConvection(EnumMetierOptionConvec newOptionConvection) {
        if (optionConvection_ == newOptionConvection) return;
        optionConvection_ = newOptionConvection;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "optionConvection");
    }

    /**
     * optionDesTracers
     *
     * @param newOptionDesTracers MetierOptionTraceur[]
     */
    public void optionDesTracers(MetierOptionTraceur[] newOptionDesTracers) {
        if (optionDesTracers_ == newOptionDesTracers) return;
        optionDesTracers_ = newOptionDesTracers;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "optionDesTracers");
    }

    /**
     * optionDesTracers
     *
     * @return MetierOptionTraceur[]
     */
    private MetierOptionTraceur[] optionDesTracers_;

    public MetierOptionTraceur[] optionDesTracers() {
        return optionDesTracers_;
    }

    /**
     * ordreSchemaConvec
     *
     * @param newOrdreSchemaConvec int
     */
    public void ordreSchemaConvec(int newOrdreSchemaConvec) {
        if (ordreSchemaConvec_ == newOrdreSchemaConvec) return;
        ordreSchemaConvec_ = newOrdreSchemaConvec;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "ordreSchemaConvec");
    }

    /**
     * ordreSchemaConvec
     *
     * @return int
     */
    private int ordreSchemaConvec_;

    public int ordreSchemaConvec() {
        return ordreSchemaConvec_;
    }

    /**
     * paramW
     *
     * @param newParamW double
     */
    public void paramW(double newParamW) {
        if (paramW_ == newParamW) return;
        paramW_ = newParamW;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "paramW");
    }

    /**
     * paramW
     *
     * @return double
     */
    private double paramW_;

    public double paramW() {
        return paramW_;
    }
}
