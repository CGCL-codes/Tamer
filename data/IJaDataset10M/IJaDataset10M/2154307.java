package logique.preferences;

import logique.reseau.Troncon;
import logique.reseau.caracteristiques.Radar;

/**
 * 
 * @author camille
 *
 */
public class CritereTronconRadar extends CritereTroncon {

    public CritereTronconRadar(boolean favoriser) {
        super(favoriser);
    }

    @Override
    public double eval(Troncon troncon, int position, int nbCriteresTotal) {
        boolean radar = troncon.getCaracteristique(Radar.getInstance().intitule()) != null;
        double coef = 0.8;
        if (radar != favoriser) {
            coef = 1 / coef;
        }
        return eval(coef, position, nbCriteresTotal);
    }
}
