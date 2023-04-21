package citadelles.services.partie.impl;

import java.rmi.RemoteException;
import citadelles.core.ContextHolder;
import citadelles.core.SpringBeans;
import citadelles.metier.exceptions.CitadellesMetierException;
import citadelles.metier.joueurs.Joueur;
import citadelles.metier.partie.Partie;
import citadelles.services.partie.PartieService;

public class PartieServiceImpl implements PartieService {

    @Override
    public Partie nouvellePartie() {
        return (Partie) ContextHolder.getBean(SpringBeans.partie);
    }

    @Override
    public void ajouterJoueur(final Partie partie, final Joueur joueur) throws CitadellesMetierException, RemoteException {
        partie.ajouterJoueur(joueur);
    }

    @Override
    public void demarrerPartie(final Partie partie) {
        try {
            try {
                partie.commencerLaPartie();
            } catch (final RemoteException e) {
                e.printStackTrace();
            }
        } catch (final CitadellesMetierException e) {
            e.printStackTrace();
        }
    }
}
