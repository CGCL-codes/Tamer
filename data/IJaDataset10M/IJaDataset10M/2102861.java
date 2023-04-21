package citadelles.metier.partie.arbitre.impl;

import java.util.List;
import citadelles.core.exception.CitadellesFonctionnelleException;
import citadelles.metier.joueurs.Joueur;
import citadelles.metier.quartiers.Paquet;

/**
 * Classe dotée d'accesseur pour réaliser des tests sur des éléments qui n'en
 * n'ont pas.
 */
public class ArbitreForTests extends ArbitreImpl {

    public ArbitreForTests(final List<Joueur> joueurs, final Paquet paquet) throws CitadellesFonctionnelleException {
        super(joueurs, paquet);
    }

    public Joueur joueurCouronne() {
        return joueurCouronne;
    }

    public void definirJoueurCouronneSansInformerListeners(final Joueur joueur) {
        this.joueurCouronne = joueur;
    }
}
