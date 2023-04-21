package org.fudaa.dodico.hydraulique1d.loi;

import java.util.Arrays;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.dodico.corba.hydraulique1d.loi.ILoiRegulation;
import org.fudaa.dodico.corba.hydraulique1d.loi.ILoiRegulationOperations;
import org.fudaa.dodico.corba.objet.IObjet;
import org.fudaa.dodico.hydraulique1d.DLoiHydraulique;
import org.fudaa.dodico.objet.UsineLib;

/**
 * Impl�mentation de l'objet m�tier d'une "loi de r�gulation" des donn�es hydraulique.
 * D�finie une courbe d�bit amont = f(cote aval).
 * @version      $Revision: 1.13 $ $Date: 2006-09-28 13:21:06 $ by $Author: opasteur $
 * @author       Jean-Marc Lacombe
 */
public class DLoiRegulation extends DLoiHydraulique implements ILoiRegulation, ILoiRegulationOperations {

    public void initialise(IObjet _o) {
        super.initialise(_o);
        if (_o instanceof ILoiRegulation) {
            ILoiRegulation l = (ILoiRegulation) _o;
            qAmont((double[]) l.qAmont().clone());
            zAval((double[]) l.zAval().clone());
        }
    }

    public final IObjet creeClone() {
        ILoiRegulation l = UsineLib.findUsine().creeHydraulique1dLoiRegulation();
        l.initialise(tie());
        return l;
    }

    public DLoiRegulation() {
        super();
        nom_ = "loi 9999999999 r�gulation";
        qAmont_ = new double[0];
        zAval_ = new double[0];
    }

    public void dispose() {
        nom_ = null;
        qAmont_ = null;
        zAval_ = null;
        super.dispose();
    }

    private double[] qAmont_;

    public double[] qAmont() {
        return qAmont_;
    }

    public void qAmont(double[] qAmont) {
        if (Arrays.equals(qAmont, qAmont_)) return;
        qAmont_ = qAmont;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "qAmont");
    }

    private double[] zAval_;

    public double[] zAval() {
        return zAval_;
    }

    public void zAval(double[] zAval) {
        if (Arrays.equals(zAval, zAval_)) return;
        zAval_ = zAval;
        UsineLib.findUsine().fireObjetModifie(toString(), tie(), "zAval");
    }

    public double gqAmontu(int i) {
        return qAmont_[i];
    }

    public void sqAmontu(int i, double v) {
        qAmont_[i] = v;
    }

    public double gzAvalu(int i) {
        return zAval_[i];
    }

    public void szAvalu(int i, double v) {
        zAval_[i] = v;
    }

    public void creePoint(int i) {
    }

    public void supprimePoints(int[] i) {
    }

    public String typeLoi() {
        String classname = getClass().getName();
        int index = classname.lastIndexOf('.');
        if (index >= 0) classname = classname.substring(index + 1);
        return classname.substring(4);
    }

    public int nbPoints() {
        return Math.min(qAmont_.length, zAval_.length);
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
                if (ligne < qAmont_.length) qAmont_[ligne] = valeur;
                break;
            case 1:
                if (ligne < zAval_.length) zAval_[ligne] = valeur;
                break;
        }
    }

    public double getValeur(int ligne, int colonne) {
        switch(colonne) {
            case 0:
                if (ligne < qAmont_.length) return qAmont_[ligne]; else return Double.NaN;
            case 1:
                if (ligne < zAval_.length) return zAval_[ligne]; else return Double.NaN;
            default:
                return Double.NaN;
        }
    }

    public void setPoints(double[][] pts) {
        double[][] points = CtuluLibArray.transpose(pts);
        if (points == null || points.length == 0) {
            qAmont_ = new double[0];
            zAval_ = new double[0];
            UsineLib.findUsine().fireObjetModifie(toString(), tie(), "qAmont");
            UsineLib.findUsine().fireObjetModifie(toString(), tie(), "zAval");
            return;
        } else {
            boolean qAmontModif = !Arrays.equals(qAmont_, points[0]);
            boolean zAvalModif = !Arrays.equals(zAval_, points[1]);
            if (qAmontModif || zAvalModif) {
                qAmont_ = points[0];
                zAval_ = points[1];
                if (qAmontModif) UsineLib.findUsine().fireObjetModifie(toString(), tie(), "qAmont");
                if (zAvalModif) UsineLib.findUsine().fireObjetModifie(toString(), tie(), "zAval");
            }
        }
    }

    public double[][] pointsToDoubleArray() {
        double[][] tableau = new double[2][qAmont_.length];
        tableau[0] = (double[]) qAmont_.clone();
        tableau[1] = (double[]) zAval_.clone();
        return CtuluLibArray.transpose(tableau);
    }
}
