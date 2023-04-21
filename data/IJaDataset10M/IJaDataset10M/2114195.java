package citadelles.metier.partie.arbitre.resolutions.impl;

import citadelles.metier.exceptions.CitadellesMetierException;
import citadelles.metier.partie.arbitre.Arbitre;
import citadelles.metier.roles.actions.InfoActionJeu;

class ActionJeuPrendreDeuxQuartiers extends ActionJeuAbstract {

    public ActionJeuPrendreDeuxQuartiers(final Arbitre arbitre, final InfoActionJeu info) {
        super(arbitre, info);
    }

    public void analyser() throws CitadellesMetierException {
    }
}
