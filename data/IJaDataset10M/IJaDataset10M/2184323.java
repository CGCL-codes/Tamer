package org.fudaa.dodico.hydraulique1d.metier.loi;

import java.util.Arrays;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.dodico.hydraulique1d.metier.evenement.Notifieur;
import org.fudaa.dodico.hydraulique1d.metier.loi.MetierLoiLimniHydrogramme;
import org.fudaa.dodico.hydraulique1d.metier.MetierLoiHydraulique;
import org.fudaa.dodico.hydraulique1d.metier.MetierHydraulique1d;

/**
 * Impl�mentation de l'objet m�tier d'une "loi limni-hydrogramme" des donn�es hydraulique.
 * D�finie 2 courbes : d�bit = f(temps) et cote = f(temps).
 * @version      $Revision: 1.2 $ $Date: 2007-11-20 11:43:18 $ by $Author: bmarchan $
 * @author       Jean-Marc Lacombe
 */
public class MetierLoiLimniHydrogramme extends MetierLoiHydraulique {

    public void initialise(MetierHydraulique1d _o) {
        super.initialise(_o);
        if (_o instanceof MetierLoiLimniHydrogramme) {
            MetierLoiLimniHydrogramme l = (MetierLoiLimniHydrogramme) _o;
            t((double[]) l.t().clone());
            z((double[]) l.z().clone());
            q((double[]) l.q().clone());
        }
    }

    public final MetierHydraulique1d creeClone() {
        MetierLoiLimniHydrogramme l = new MetierLoiLimniHydrogramme();
        l.initialise(this);
        return l;
    }

    public MetierLoiLimniHydrogramme() {
        super();
        nom_ = "loi 9999999999 limnihydrogramme";
        t_ = new double[0];
        z_ = new double[0];
        q_ = new double[0];
        notifieObjetCree();
    }

    public void dispose() {
        nom_ = null;
        t_ = null;
        z_ = null;
        q_ = null;
        super.dispose();
    }

    private double[] t_;

    public double[] t() {
        return t_;
    }

    public void t(double[] t) {
        if (Arrays.equals(t, t_)) return;
        t_ = t;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "t");
    }

    private double[] z_;

    public double[] z() {
        return z_;
    }

    public void z(double[] z) {
        if (Arrays.equals(z, z_)) return;
        z_ = z;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "z");
    }

    private double[] q_;

    public double[] q() {
        return q_;
    }

    public void q(double[] q) {
        if (Arrays.equals(q, q_)) return;
        q_ = q;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "q");
    }

    public double gtu(int i) {
        return t_[i];
    }

    public void stu(int i, double v) {
        t_[i] = v;
    }

    public double gzu(int i) {
        return z_[i];
    }

    public void szu(int i, double v) {
        z_[i] = v;
    }

    public double gqu(int i) {
        return q_[i];
    }

    public void squ(int i, double v) {
        q_[i] = v;
    }

    public void creePoint(int i) {
    }

    public void supprimePoints(int[] i) {
    }

    public String typeLoi() {
        return "LimniHydrogramme";
    }

    public int nbPoints() {
        return Math.min(Math.min(t_.length, z_.length), q_.length);
    }

    public boolean verifiePermanent() {
        return false;
    }

    public boolean verifieTempsNonPermanent() {
        return true;
    }

    public void setValeur(double valeur, int ligne, int colonne) {
        switch(colonne) {
            case 0:
                if (ligne < t_.length) t_[ligne] = valeur;
                break;
            case 1:
                if (ligne < z_.length) z_[ligne] = valeur;
                break;
            case 2:
                if (ligne < q_.length) q_[ligne] = valeur;
                break;
        }
    }

    public double getValeur(int ligne, int colonne) {
        switch(colonne) {
            case 0:
                if (ligne < t_.length) return t_[ligne]; else return Double.NaN;
            case 1:
                if (ligne < z_.length) return z_[ligne]; else return Double.NaN;
            case 2:
                if (ligne < q_.length) return q_[ligne]; else return Double.NaN;
            default:
                return Double.NaN;
        }
    }

    public void setPoints(double[][] pts) {
        double[][] points = CtuluLibArray.transpose(pts);
        if (points == null || points.length == 0) {
            t_ = new double[0];
            z_ = new double[0];
            q_ = new double[0];
            Notifieur.getNotifieur().fireObjetModifie(toString(), this, "t");
            Notifieur.getNotifieur().fireObjetModifie(toString(), this, "z");
            Notifieur.getNotifieur().fireObjetModifie(toString(), this, "q");
            return;
        } else {
            boolean tModif = !Arrays.equals(t_, points[0]);
            boolean zModif = !Arrays.equals(z_, points[1]);
            boolean qModif = !Arrays.equals(q_, points[2]);
            if (tModif || zModif || qModif) {
                t_ = points[0];
                z_ = points[1];
                q_ = points[2];
                if (tModif) Notifieur.getNotifieur().fireObjetModifie(toString(), this, "t");
                if (zModif) Notifieur.getNotifieur().fireObjetModifie(toString(), this, "z");
                if (qModif) Notifieur.getNotifieur().fireObjetModifie(toString(), this, "q");
            }
        }
    }

    public double[][] pointsToDoubleArray() {
        double[][] tableau = new double[3][t_.length];
        tableau[0] = (double[]) t_.clone();
        tableau[1] = (double[]) z_.clone();
        tableau[2] = (double[]) q_.clone();
        return CtuluLibArray.transpose(tableau);
    }
}
