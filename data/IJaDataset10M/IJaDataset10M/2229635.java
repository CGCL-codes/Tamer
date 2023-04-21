package org.fudaa.dodico.hydraulique1d.metier;

import org.fudaa.dodico.hydraulique1d.metier.MetierHydraulique1d;
import org.fudaa.dodico.hydraulique1d.metier.MetierLaisse;
import org.fudaa.dodico.hydraulique1d.metier.MetierSite;
import org.fudaa.dodico.hydraulique1d.metier.evenement.Notifieur;

/**
 * Impl�mentation de l'objet m�tier "Un �l�ment d'une laisse de crue".
 * Associe une cote, un site (une abscisse sur un bief) et un nom.
 * @version      $Revision: 1.2 $ $Date: 2007-11-20 11:42:25 $ by $Author: bmarchan $
 * @author       Axel von Arnim
 */
public class MetierLaisse extends MetierHydraulique1d {

    public void initialise(MetierHydraulique1d _o) {
        if (_o instanceof MetierLaisse) {
            MetierLaisse q = (MetierLaisse) _o;
            nom(q.nom());
            site(q.site());
            cote(q.cote());
        }
    }

    public final MetierHydraulique1d creeClone() {
        MetierLaisse p = new MetierLaisse();
        p.initialise(this);
        return p;
    }

    public final String toString() {
        String s = "laisse " + nom_;
        if (site_ != null) s += " (" + site_.toString() + ")";
        return s;
    }

    public MetierLaisse() {
        super();
        site_ = new MetierSite();
        nom_ = "Laisse";
        cote_ = 0.;
        notifieObjetCree();
    }

    public void dispose() {
        site_ = null;
        nom_ = null;
        cote_ = 0.;
        super.dispose();
    }

    private String nom_;

    public String nom() {
        return nom_;
    }

    public void nom(String s) {
        if (nom_.equals(s)) return;
        nom_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "nom");
    }

    private MetierSite site_;

    public MetierSite site() {
        return site_;
    }

    public void site(MetierSite s) {
        if (site_ == s) return;
        site_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "site");
    }

    private double cote_;

    public double cote() {
        return cote_;
    }

    public void cote(double s) {
        if (cote_ == s) return;
        cote_ = s;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "cote");
    }
}
