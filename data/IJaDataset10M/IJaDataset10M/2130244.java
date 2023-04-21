package org.fudaa.dodico.crue.metier.emh;

/**
 * @author deniger
 */
public class ResCalBrancheSaintVenant extends ResCalcul {

    private double qlat;

    private double splanAct;

    private double splanTot;

    private double splanSto;

    public double getQlat() {
        return qlat;
    }

    public void setQlat(double qlat) {
        this.qlat = qlat;
    }

    public double getSplanAct() {
        return splanAct;
    }

    public void setSplanAct(double splanAct) {
        this.splanAct = splanAct;
    }

    public double getSplanTot() {
        return splanTot;
    }

    public void setSplanTot(double splanTot) {
        this.splanTot = splanTot;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public double getSplanSto() {
        return splanSto;
    }

    public void setSplanSto(double splanSto) {
        this.splanSto = splanSto;
    }

    private double vol;
}
