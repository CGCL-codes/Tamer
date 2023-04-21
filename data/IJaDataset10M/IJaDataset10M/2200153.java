package citadelles.metier.partie.arbitre.resolutions;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import citadelles.core.ContextHolder;
import citadelles.core.SpringBeans;
import citadelles.core.exception.CitadellesFonctionnelleException;
import citadelles.metier.joueurs.EnumActionsJoueur;
import citadelles.metier.joueurs.InfoJoueur;
import citadelles.metier.joueurs.Joueur;
import citadelles.metier.partie.arbitre.Arbitre;
import citadelles.metier.roles.Role;
import citadelles.metier.roles.actions.ActionJeuFactory;
import citadelles.metier.roles.actions.EnumActionsJeu;
import citadelles.metier.roles.actions.InfoActionJeu;

public class ArbitreResolutionTest {

    @Mock
    private Arbitre arbitre;

    @Mock
    private Joueur joueur;

    @Mock
    private Role role;

    private ArbitreResolutionFactory arf;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.arf = (ArbitreResolutionFactory) ContextHolder.getBean(SpringBeans.arbitreResolutionFactory);
    }

    @Test
    public void toutesLesActionsDeJoueursOntUneResolutionAssociee() throws CitadellesFonctionnelleException {
        for (final EnumActionsJoueur aj : EnumActionsJoueur.values()) {
            final ArbitreResolution arbitreResolution = arf.create(arbitre, new InfoJoueur(joueur, aj));
            assertNotNull(arbitreResolution);
        }
    }

    @Test
    public void toutesLesActionsJeuOntUneResolutionAssociee() throws CitadellesFonctionnelleException {
        final ActionJeuFactory ajf = (ActionJeuFactory) ContextHolder.getBean(SpringBeans.actionJeuFactory);
        for (final EnumActionsJeu action : EnumActionsJeu.values()) {
            final ArbitreResolution arbitreResolution = arf.create(arbitre, new InfoActionJeu(ajf.create(action, role)));
            assertNotNull(arbitreResolution);
        }
    }
}
