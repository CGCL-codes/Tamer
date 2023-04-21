package citadelles.metier.partie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import citadelles.core.ContextHolder;
import citadelles.core.SpringBeans;
import citadelles.core.exception.CitadellesFonctionnelleException;
import citadelles.metier.exceptions.CitadellesMetierException;
import citadelles.metier.joueurs.Cite;
import citadelles.metier.joueurs.Joueur;
import citadelles.metier.joueurs.Main;
import citadelles.metier.partie.arbitre.Arbitre;
import citadelles.metier.partie.arbitre.ArbitreListener;
import citadelles.metier.partie.arbitre.InfoArbitre;
import citadelles.metier.partie.arbitre.OrdresArbitre;
import citadelles.metier.partie.arbitre.ScoreJoueur;
import citadelles.metier.partie.exceptions.PartieNAcceptePlusDeJoueurs;
import citadelles.metier.partie.exceptions.PasAssezDeJoueurs;
import citadelles.metier.partie.impl.PartieForTests;
import citadelles.metier.quartiers.QuartierEnum;
import citadelles.metier.roles.Role;
import citadelles.metier.roles.RolesDisponibles;
import citadelles.metier.roles.actions.ActionJeu;
import citadelles.metier.roles.actions.EnumActionsJeu;

public class PartieTest implements ArbitreListener {

    private PartieForTests partie;

    private List<ScoreJoueur> scores;

    @Before
    public void setUp() throws Exception {
        this.partie = new PartieForTests();
    }

    /**
	 * Ce cas de tests peut envoyer une exception QuartierDejaDansLaCite à cause
	 * de son côté aléatoire. Relancer les tests si besoin et vérifier que c'est
	 * bien vert.
	 * 
	 * @throws CitadellesFonctionnelleException
	 */
    @Test
    public void unePartieCommenceAvecAuMoins2JoueursEtSeTermine() throws CitadellesFonctionnelleException {
        final Joueur joueur1 = (Joueur) ContextHolder.getBean(SpringBeans.joueurHumain);
        joueur1.toucherOr(10);
        final Cite cite = joueur1.getCite();
        cite.construire(QuartierEnum.comptoir);
        cite.construire(QuartierEnum.eglise);
        cite.construire(QuartierEnum.taverne);
        cite.construire(QuartierEnum.caserne);
        cite.construire(QuartierEnum.chateau);
        cite.construire(QuartierEnum.cathedrale);
        cite.construire(QuartierEnum.dracoport);
        final Joueur joueur2 = (Joueur) ContextHolder.getBean(SpringBeans.joueurHumain);
        joueur2.toucherOr(10);
        partie.ajouterJoueur(joueur1);
        partie.ajouterJoueur(joueur2);
        partie.initialisationDeLaPartie();
        partie.abonner(this);
        partie.commencerLaPartie();
        assertEquals(joueur1, scores.get(0).getJoueur());
    }

    @Test(expected = PasAssezDeJoueurs.class)
    public void ilNestPasPossibleDInitialiserUnePartieAvec1Joueur() throws CitadellesFonctionnelleException {
        partie.ajouterJoueur((Joueur) ContextHolder.getBean(SpringBeans.joueurHumain));
        partie.initialisationDeLaPartie();
    }

    @Test(expected = PartieNAcceptePlusDeJoueurs.class)
    public void ilNestPasPossibleDAjouterPlusDeJoueurALaPartieQueLaLimiteAutorisee() throws CitadellesFonctionnelleException {
        final int maxJoueurs = partie.getMaxJoueurs();
        for (int i = 0; i <= maxJoueurs + 1; i++) {
            partie.ajouterJoueur((Joueur) ContextHolder.getBean(SpringBeans.joueurHumain));
        }
    }

    private ActionJeu getAction(final Role role, final EnumActionsJeu actionChoisie) {
        ActionJeu actionJeu = null;
        for (final ActionJeu action : role.listeDActions()) {
            if (action.getNomAction().equals(actionChoisie)) {
                actionJeu = action;
                break;
            }
        }
        return actionJeu;
    }

    public void process(final InfoArbitre info) throws CitadellesMetierException {
        final OrdresArbitre ordre = info.getOrdre();
        final Joueur joueur = info.getJoueur();
        Role role;
        switch(ordre) {
            case joueurDoitChoisirRole:
                final Arbitre arbitre = (Arbitre) info.getSource();
                final RolesDisponibles rd = arbitre.getRolesDisponibles();
                role = rd.iterator().next();
                joueur.choisiRole(role);
                break;
            case RoleDoitSeReveler:
                getAction(info.getRole(), EnumActionsJeu.SeReveler).agir();
                break;
            case RolePeutJouer:
                role = info.getRole();
                getAction(role, EnumActionsJeu.PrendreOr).agir();
                getAction(role, EnumActionsJeu.Construire).agir();
                final Main main = joueur.getMain();
                joueur.construire(main.iterator().next());
                getAction(role, EnumActionsJeu.TerminerMonTourDeJeu).agir();
                break;
            case Resultats:
                scores = info.getScores();
                break;
            default:
                fail();
        }
    }
}
