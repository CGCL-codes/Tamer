package jbced.citadellesspring.modele.joueurs;

import static org.junit.Assert.*;
import jbced.citadellesspring.core.exceptions.CitadellesException;
import jbced.citadellesspring.core.SpringBeans;
import jbced.citadellesspring.modele.quartiers.QuartierEnum;
import jbced.citadellesspring.modele.roles.Role;
import jbced.citadellesspring.modele.joueurs.exceptions.QuartierPasDansLaMainDuJoueur;
import jbced.citadellesspring.modele.joueurs.exceptions.PasAssezDOr;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.spring.contextholder.ContextHolder;
import org.springframework.context.ApplicationContext;
import static org.mockito.Mockito.*;

public class JoueurTest implements JoueurListener {

    private Joueur joueur;

    @Mock
    private Role role;

    private InfoJoueur info;

    @Before
    public void setUp() {
        ApplicationContext context = ContextHolder.getApplicationContext();
        this.joueur = (Joueur) context.getBean(SpringBeans.joueurHumain.name());
        this.joueur.add(this);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void unNouveauJoueurNAPasOr() throws Exception {
        assertEquals(0, joueur.or());
    }

    @Test
    public void unNouveauJoueurNAPAsDeQuartierDansSaMain() throws Exception {
        Main main = joueur.main();
        assertEquals(0, main.nbQuartiers());
    }

    @Test
    public void unNouveauJoueurNAPAsDeQuartierDansSaCite() throws Exception {
        Cite cite = joueur.cite();
        assertEquals(0, cite.nbQuartiers());
    }

    @Test
    public void prendreDeLOr() throws Exception {
        int orInitial = joueur.or();
        joueur.toucherOr(2);
        assertEquals(orInitial + 2, joueur.or());
    }

    @Test
    public void prendreUnquartierLAjouteALaMain() throws Exception {
        QuartierEnum quartier = QuartierEnum.chateau;
        joueur.prendreEnMain(quartier);
        for (QuartierEnum q : joueur.main()) {
            assertEquals(quartier, q);
        }
    }

    @Test(expected = QuartierPasDansLaMainDuJoueur.class)
    public void unJoueurNePeutConstruireUnQuartierQuiNeProvientPasDeSaMain() throws Exception {
        QuartierEnum taverne = QuartierEnum.taverne;
        joueur.toucherOr(2);
        joueur.construire(taverne);
    }

    @Test(expected = PasAssezDOr.class)
    public void unJoueurNePeutPasConstruireUnQuartierSIlNaPasAssezDOr() throws Exception {
        QuartierEnum taverne = QuartierEnum.taverne;
        joueur.prendreEnMain(taverne);
        joueur.construire(taverne);
    }

    @Test
    public void construireUnQuartierLAjouteALaCiteLaRetireDeLaMainEtMetAJourLOrDuJoueur() throws Exception {
        joueur.toucherOr(2);
        QuartierEnum quartier = QuartierEnum.taverne;
        joueur.prendreEnMain(quartier);
        joueur.construire(quartier);
        for (QuartierEnum q : joueur.cite()) {
            assertEquals(quartier, q);
        }
        assertEquals(0, joueur.main().nbQuartiers());
        assertEquals(1, joueur.cite().nbQuartiers());
        assertEquals(1, joueur.or());
    }

    @Test
    public void unJoueurPeutChoisirUnRoleEtInformerSesListeners() {
        when(role.getJoueur()).thenReturn(joueur);
        joueur.choisiRole(role);
        assertEquals(joueur, role.getJoueur());
        assertSame(InfoJoueur.class, this.info.getClass());
    }

    @Test
    public void unJoueurNInformePlisSesListenersDesabonnes() {
        when(role.getJoueur()).thenReturn(joueur);
        joueur.remove(this);
        joueur.choisiRole(role);
        assertNull(this.info);
    }

    @Override
    public void process(InfoJoueur info) throws CitadellesException {
        this.info = info;
    }
}
