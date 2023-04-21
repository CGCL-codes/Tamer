package org.fudaa.fudaa.lido.ihmhelper.gestion;

import org.fudaa.dodico.corba.lido.SParametresBiefBlocPRO;
import org.fudaa.dodico.corba.lido.SParametresCAL;
import org.fudaa.dodico.corba.lido.SParametresPRO;
import org.fudaa.dodico.corba.lido.SParametresPasLigneCAL;
import org.fudaa.fudaa.commun.projet.FudaaParamEvent;
import org.fudaa.fudaa.commun.projet.FudaaProjet;
import org.fudaa.fudaa.lido.LidoResource;

/**
 * @version      $Revision: 1.10 $ $Date: 2006-09-19 15:05:06 $ by $Author: deniger $
 * @author       Axel von Arnim 
 */
public class LidoPH_Calcul extends LidoPH_Base {

    private SParametresCAL cal_;

    private SParametresPRO pro_;

    LidoPH_Calcul(final FudaaProjet p, final LidoParamsHelper ph) {
        super(p, ph);
    }

    void setProjet(final FudaaProjet p) {
        cal_ = (SParametresCAL) p.getParam(LidoResource.CAL);
        if (cal_ == null) {
            System.err.println("LidoPH_Calcul: Warning: passing null CAL to constructor");
        }
        pro_ = (SParametresPRO) p.getParam(LidoResource.PRO);
        if (pro_ == null) {
            System.err.println("LidoPH_Calcul: Warning: passing null PRO to constructor");
        }
    }

    private SParametresPasLigneCAL supprimePlani(final SParametresPasLigneCAL p) {
        if ((cal_ == null) || (cal_.planimetrage == null) || (cal_.planimetrage.varsPlanimetrage == null) || (cal_.planimetrage.varPlanNbPas == 0)) {
            System.err.println("LidoPH_Calcul: Warning: plani null");
            return null;
        }
        if (p == null) {
            return null;
        }
        final SParametresPasLigneCAL[] plani = cal_.planimetrage.varsPlanimetrage;
        final SParametresPasLigneCAL[] nouvPlani = new SParametresPasLigneCAL[plani.length - 1];
        int i = 0;
        for (i = 0; i < plani.length; i++) {
            if (p == plani[i]) {
                break;
            }
        }
        if (i >= plani.length) {
            return null;
        }
        int n = 0;
        for (int j = 0; j < plani.length; j++) {
            if (p != plani[j]) {
                nouvPlani[n++] = plani[j];
            }
        }
        cal_.planimetrage.varsPlanimetrage = nouvPlani;
        cal_.planimetrage.varPlanNbPas = nouvPlani.length;
        fireParamStructDeleted(new FudaaParamEvent(this, 0, LidoResource.CAL, cal_.planimetrage, "plani " + i));
        prop_.firePropertyChange("plani", plani, nouvPlani);
        return p;
    }

    boolean majPlaniProfilAjoute(final SParametresBiefBlocPRO p) {
        boolean maj = false;
        if ((cal_ == null) || (cal_.planimetrage == null) || (cal_.planimetrage.varsPlanimetrage == null) || (cal_.planimetrage.varPlanNbPas == 0)) {
            System.err.println("LidoPH_Calcul: Warning: plani null");
            return maj;
        }
        if (p == null) {
            return maj;
        }
        for (int i = 0; i < cal_.planimetrage.varPlanNbPas; i++) {
            if (cal_.planimetrage.varsPlanimetrage[i].profilDebut >= p.indice) {
                cal_.planimetrage.varsPlanimetrage[i].profilDebut++;
                maj = true;
            }
            if (cal_.planimetrage.varsPlanimetrage[i].profilFin >= p.indice) {
                cal_.planimetrage.varsPlanimetrage[i].profilFin++;
                maj = true;
            }
        }
        if (maj) {
            fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.CAL, cal_.planimetrage, "plani"));
        }
        return maj;
    }

    boolean majPlaniProfilSupprime(final SParametresBiefBlocPRO p) {
        boolean majp = false;
        if ((cal_ == null) || (cal_.planimetrage == null) || (cal_.planimetrage.varsPlanimetrage == null) || (cal_.planimetrage.varPlanNbPas == 0)) {
            System.err.println("LidoPH_Calcul: Warning: plani null");
            return majp;
        }
        if (p == null) {
            return majp;
        }
        for (int i = 0; i < cal_.planimetrage.varPlanNbPas; i++) {
            if (cal_.planimetrage.varsPlanimetrage[i].profilDebut == cal_.planimetrage.varsPlanimetrage[i].profilFin) {
                supprimePlani(cal_.planimetrage.varsPlanimetrage[i]);
                System.err.println("Suppression plani " + i);
                majp = true;
            } else {
                if ((cal_.planimetrage.varsPlanimetrage[i].profilDebut > (p.indice + 1))) {
                    cal_.planimetrage.varsPlanimetrage[i].profilDebut--;
                    majp = true;
                }
                if ((cal_.planimetrage.varsPlanimetrage[i].profilFin >= (p.indice + 1))) {
                    cal_.planimetrage.varsPlanimetrage[i].profilFin--;
                    majp = true;
                }
            }
        }
        if (majp) {
            prop_.firePropertyChange("plani", null, cal_.planimetrage.varsPlanimetrage);
            fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.CAL, cal_.planimetrage, "plani"));
        }
        return majp;
    }

    boolean majGencalProfilSupprime(final SParametresBiefBlocPRO p) {
        boolean majp = false;
        if ((cal_ == null) || (cal_.genCal == null)) {
            System.err.println("LidoPH_Calcul: Warning: gencal null");
            return majp;
        }
        if (p == null) {
            return majp;
        }
        final double xori = getXOrigine();
        final double xfin = getXFin();
        if (((cal_.genCal.biefXOrigi == 0.) && (cal_.genCal.biefXFin == 0.)) || ((cal_.genCal.biefXOrigi < xori) || (cal_.genCal.biefXFin > xfin))) {
            cal_.genCal.biefXOrigi = xori;
            cal_.genCal.biefXFin = xfin;
            fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.CAL, cal_.genCal, "xorigine/xfin"));
            majp = true;
        }
        if (majp) {
            prop_.firePropertyChange("gencal", null, cal_.genCal);
        }
        return majp;
    }

    public double getXOrigine() {
        if ((pro_ == null) || (pro_.nbProfils == 0)) {
            return 0.;
        }
        double res = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pro_.nbProfils; i++) {
            if (pro_.profilsBief[i].abscisse < res) {
                res = pro_.profilsBief[i].abscisse;
            }
        }
        return res;
    }

    public double getXFin() {
        if ((pro_ == null) || (pro_.nbProfils == 0)) {
            return 0.;
        }
        double res = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < pro_.nbProfils; i++) {
            if (pro_.profilsBief[i].abscisse > res) {
                res = pro_.profilsBief[i].abscisse;
            }
        }
        return res;
    }

    public void recalculeXOrigiXFin() {
        if ((cal_ == null) || (cal_.genCal == null)) {
            System.err.println("LidoPH_Calcul: Warning: gencal null");
            return;
        }
        final double xori = getXOrigine();
        final double xfin = getXFin();
        if (xori > Double.NEGATIVE_INFINITY) {
            cal_.genCal.biefXOrigi = xori;
        }
        if (xfin > Double.NEGATIVE_INFINITY) {
            cal_.genCal.biefXFin = xfin;
        }
        fireParamStructModified(new FudaaParamEvent(this, 0, LidoResource.CAL, cal_.genCal, "xorigine/xfin"));
    }

    public boolean isRegimePermanent() {
        if ((cal_ == null) || (cal_.genCal == null)) {
            System.err.println("LidoPH_Calcul: Warning: gencal null");
            return true;
        }
        return "P".equalsIgnoreCase(cal_.genCal.regime.trim());
    }
}
