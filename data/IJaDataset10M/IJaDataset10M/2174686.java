package org.fudaa.dodico.hydraulique1d.metier.calageauto;

import org.fudaa.dodico.hydraulique1d.metier.calageauto.MetierApportCrueCalageAuto;
import org.fudaa.dodico.hydraulique1d.metier.evenement.Notifieur;
import org.fudaa.dodico.hydraulique1d.metier.MetierHydraulique1d;

/**
 * Impl�mentation de l'objet apport pour une crue de calage "MetierApportCrueCalageAuto".
 * @version      $Revision: 1.2 $ $Date: 2007-11-20 11:43:25 $ by $Author: bmarchan $
 * @author       Bertrand Marchand
 */
public class MetierApportCrueCalageAuto extends MetierHydraulique1d {

    private double abscisse_;

    private double debit_;

    public MetierApportCrueCalageAuto() {
        super();
        abscisse_ = 0;
        debit_ = 0;
        notifieObjetCree();
    }

    public void initialise(MetierHydraulique1d _o) {
        super.initialise(_o);
        if (_o instanceof MetierApportCrueCalageAuto) {
            MetierApportCrueCalageAuto q = (MetierApportCrueCalageAuto) _o;
            abscisse(q.abscisse());
            debit(q.debit());
        }
    }

    public final MetierHydraulique1d creeClone() {
        MetierApportCrueCalageAuto p = new MetierApportCrueCalageAuto();
        p.initialise(this);
        return p;
    }

    public String[] getInfos() {
        String[] res = new String[2];
        res[0] = "Apport";
        res[1] = super.getInfos()[1] + " abscisse : " + abscisse_ + " debit : " + debit_;
        return res;
    }

    public void dispose() {
        abscisse_ = 0;
        debit_ = 0;
        super.dispose();
    }

    public double abscisse() {
        return abscisse_;
    }

    public void abscisse(double _abscisse) {
        if (abscisse_ == _abscisse) return;
        abscisse_ = _abscisse;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "abscisse");
    }

    public double debit() {
        return debit_;
    }

    public void debit(double _debit) {
        if (debit_ == _debit) return;
        debit_ = _debit;
        Notifieur.getNotifieur().fireObjetModifie(toString(), this, "debit");
    }
}
