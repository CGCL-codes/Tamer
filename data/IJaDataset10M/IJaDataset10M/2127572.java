package org.fudaa.fudaa.hydraulique1d.metier.singularite;

import org.fudaa.fudaa.hydraulique1d.metier.MetierLoiHydraulique;
import org.fudaa.fudaa.hydraulique1d.metier.evenement.Notifieur;
import org.fudaa.fudaa.hydraulique1d.metier.singularite.MetierPerteCharge;
import org.fudaa.fudaa.hydraulique1d.metier.MetierHydraulique1d;
import org.fudaa.fudaa.hydraulique1d.metier.MetierSingularite;

/**
 * Impl�mentation de l'objet m�tier singularit� de type "perte de charge" singuli�re.
 * Ajoute un coefficient de perte de charge.
 * @version      $Revision: 1.1.2.1 $ $Date: 2007-09-06 14:40:14 $ by $Author: bmarchan $
 * @author       Jean-Marc Lacombe
 */
public class MetierPerteCharge extends MetierSingularite {

    public void initialise(MetierHydraulique1d _o) {
        super.initialise(_o);
        if (_o instanceof MetierPerteCharge) {
            MetierPerteCharge a = (MetierPerteCharge) _o;
            coefficient(a.coefficient());
        }
    }

    public final MetierHydraulique1d creeClone() {
        MetierPerteCharge p = new MetierPerteCharge();
        p.initialise(this);
        return p;
    }

    public final String toString() {
        MetierLoiHydraulique l = getLoi();
        String s = "perteCharge " + nom_;
        if (l != null) s += "(loi " + l.toString() + ")";
        return s;
    }

    public String[] getInfos() {
        String[] res = new String[2];
        res[0] = "Perte de charge-Singularit� n�" + numero_;
        res[1] = "Abscisse : " + abscisse_ + " Coefficient : " + coefficient_;
        return res;
    }

    public MetierLoiHydraulique creeLoi() {
        return null;
    }

    public MetierLoiHydraulique getLoi() {
        return null;
    }

    public MetierPerteCharge() {
        super();
        nom_ = "Perte de charge-Singularit� n�" + numero_;
        coefficient_ = 1.;
        notifieObjetCree();
    }

    public void dispose() {
        nom_ = null;
        coefficient_ = 0;
        super.dispose();
    }

    private double coefficient_;

    public double coefficient() {
        return coefficient_;
    }

    public void coefficient(double coefficient) {
        if (coefficient_ == coefficient) return;
        coefficient_ = coefficient;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "coefficient");
    }
}
