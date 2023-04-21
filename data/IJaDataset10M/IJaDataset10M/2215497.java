package org.fudaa.dodico.hydraulique1d;

import org.fudaa.dodico.corba.hydraulique1d.IInformationTemps;
import org.fudaa.dodico.corba.hydraulique1d.IResultatsBiefPasTemps;
import org.fudaa.dodico.corba.hydraulique1d.IResultatsBiefPasTempsHelper;
import org.fudaa.dodico.corba.hydraulique1d.IResultatsBiefPasTempsOperations;
import org.fudaa.dodico.corba.objet.IObjet;
import org.fudaa.dodico.objet.UsineLib;

/**
 * Impl�mentation de l'objet m�tier "r�sultats du bief par pas de temps".
 * Pas utilis� dans Fudaa-Mascaret.
 * @version      $Id: DResultatsBiefPasTemps.java,v 1.9 2005-06-29 18:07:57 jm_lacombe Exp $
 * @author       Axel von Arnim
 */
public class DResultatsBiefPasTemps extends DHydraulique1d implements IResultatsBiefPasTemps, IResultatsBiefPasTempsOperations {

    public void initialise(IObjet _o) {
        super.initialise(_o);
        if (_o instanceof IResultatsBiefPasTemps) {
            IResultatsBiefPasTemps q = IResultatsBiefPasTempsHelper.narrow(_o);
            if (q.infoTemps() != null) infoTemps((IInformationTemps) q.infoTemps().creeClone());
            volum(q.volum());
            volums(q.volums());
            vbief(q.vbief());
            vappo(q.vappo());
            vperdu(q.vperdu());
        }
    }

    public final IObjet creeClone() {
        IResultatsBiefPasTemps p = UsineLib.findUsine().creeHydraulique1dResultatsBiefPasTemps();
        p.initialise(tie());
        return p;
    }

    public final String toString() {
        String s = "resultatsBief ";
        if (infoTemps_ != null) s += "pas " + infoTemps_.toString(); else s += "?";
        return s;
    }

    public DResultatsBiefPasTemps() {
        super();
        infoTemps_ = null;
        volum_ = 0.;
        volums_ = 0.;
        vbief_ = 0.;
        vappo_ = 0.;
        vperdu_ = 0.;
    }

    public void dispose() {
        infoTemps_ = null;
        volum_ = 0.;
        volums_ = 0.;
        vbief_ = 0.;
        vappo_ = 0.;
        vperdu_ = 0.;
        super.dispose();
    }

    private IInformationTemps infoTemps_;

    public IInformationTemps infoTemps() {
        return infoTemps_;
    }

    public void infoTemps(IInformationTemps s) {
        if (infoTemps_ == s) return;
        infoTemps_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "infoTemps");
    }

    private double volum_;

    public double volum() {
        return volum_;
    }

    public void volum(double s) {
        if (volum_ == s) return;
        volum_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "volum");
    }

    private double volums_;

    public double volums() {
        return volums_;
    }

    public void volums(double s) {
        if (volums_ == s) return;
        volums_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "volums");
    }

    private double vbief_;

    public double vbief() {
        return vbief_;
    }

    public void vbief(double s) {
        if (vbief_ == s) return;
        vbief_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "vbief");
    }

    private double vappo_;

    public double vappo() {
        return vappo_;
    }

    public void vappo(double s) {
        if (vappo_ == s) return;
        vappo_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "vappo");
    }

    private double vperdu_;

    public double vperdu() {
        return vperdu_;
    }

    public void vperdu(double s) {
        if (vperdu_ == s) return;
        vperdu_ = s;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "vperdu");
    }
}
