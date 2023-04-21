package citadelles.metier.partie.arbitre.resolutions.impl;

import citadelles.metier.exceptions.CitadellesMetierException;
import citadelles.metier.joueurs.Cite;
import citadelles.metier.joueurs.Joueur;
import citadelles.metier.partie.arbitre.Arbitre;
import citadelles.metier.partie.arbitre.InfoArbitre;
import citadelles.metier.partie.arbitre.OrdresArbitre;
import citadelles.metier.quartiers.QuartierEnum;
import citadelles.metier.roles.Role;
import citadelles.metier.roles.actions.ActionJeu;
import citadelles.metier.roles.actions.EnumActionsJeu;
import citadelles.metier.roles.actions.InfoActionJeu;

class ActionJeuPrendreQuartier extends ActionJeuAbstract {

    public ActionJeuPrendreQuartier(final Arbitre arbitre, final InfoActionJeu info) {
        super(arbitre, info);
    }

    @Override
    public void analyser() throws CitadellesMetierException {
        OrdresArbitre ordre = OrdresArbitre.ChoisirQuartierParmis2Cartes;
        final Role role = infoActionJeu.getActionJeu().getRole();
        final Joueur joueur = role.getJoueur();
        final Cite cite = joueur.getCite();
        for (final QuartierEnum quartier : cite) {
            if (quartier.equals(QuartierEnum.observatoire)) {
                ordre = OrdresArbitre.ChoisirQuartierParmis3Cartes;
            }
        }
        final InfoArbitre info = new InfoArbitre(arbitre, ordre, infoActionJeu.getActionJeu());
        arbitre.informer(info);
        super.analyser();
        supprimerActionPrendreOr(role);
    }

    private void supprimerActionPrendreOr(final Role role) {
        ActionJeu actionJeu = null;
        for (final ActionJeu aj : role.listeDActions()) {
            if (aj.getNomAction().equals(EnumActionsJeu.PrendreOr)) {
                actionJeu = aj;
            }
        }
        role.removeActionJeu(actionJeu);
    }
}
