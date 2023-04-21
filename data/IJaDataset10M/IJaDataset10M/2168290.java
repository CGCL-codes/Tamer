package jbced.citadellesspring.modele.partie.arbitre.resolutions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import jbced.citadellesspring.modele.exceptions.CitadellesModeleExceptions;
import jbced.citadellesspring.modele.joueurs.Joueur;
import jbced.citadellesspring.modele.partie.arbitre.Arbitre;
import jbced.citadellesspring.modele.partie.arbitre.InfoArbitre;
import jbced.citadellesspring.modele.partie.arbitre.impl.ArbitreForTests;
import jbced.citadellesspring.modele.quartiers.Paquet;
import jbced.citadellesspring.modele.roles.Role;
import jbced.citadellesspring.modele.roles.RolesDisponibles;
import jbced.citadellesspring.modele.roles.actions.EnumActionsJeu;
import org.junit.Test;

public class ResoudreActionJeuRecevoir1OrTest extends ResoudreActionJeuAbstractTests {

    private Arbitre arbitre;

    @Test
    public void lActionRecevoir1OrFonctionneEtLaSupprimeDeLaListe() throws Exception {
        final List<Joueur> joueurs = new ArrayList<Joueur>();
        final Joueur joueur = super.createJoueurHumain();
        final Joueur joueur2 = super.createJoueurHumain();
        joueurs.add(joueur);
        joueurs.add(joueur2);
        final Paquet paquet = super.createPaquet();
        arbitre = new ArbitreForTests(joueurs, paquet);
        final RolesDisponibles rd = arbitre.getRolesDisponibles();
        final Role marchand = rd.tousLesRoles().get(5);
        marchand.abonneAuxActions(arbitre);
        arbitre.add(this);
        marchand.setJoueur(joueur);
        final int orAvant = marchand.getJoueur().or();
        getActionJeuSpeciale(marchand, EnumActionsJeu.Recevoir1Or).agir();
        assertEquals(orAvant + 1, marchand.getJoueur().or());
        assertNull(getActionJeuSpeciale(marchand, EnumActionsJeu.Recevoir1Or));
    }

    public void process(InfoArbitre info) throws CitadellesModeleExceptions {
    }
}
