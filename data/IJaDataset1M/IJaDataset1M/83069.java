package jbced.citadellesspring.modele.partie.arbitre.resolutions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import jbced.citadellesspring.core.SpringBeans;
import jbced.citadellesspring.modele.exceptions.CitadellesModeleExceptions;
import jbced.citadellesspring.modele.joueurs.Joueur;
import jbced.citadellesspring.modele.joueurs.Main;
import jbced.citadellesspring.modele.partie.arbitre.Arbitre;
import jbced.citadellesspring.modele.partie.arbitre.ArbitreFactory;
import jbced.citadellesspring.modele.partie.arbitre.InfoArbitre;
import jbced.citadellesspring.modele.partie.arbitre.OrdresArbitre;
import jbced.citadellesspring.modele.quartiers.QuartierEnum;
import jbced.citadellesspring.modele.quartiers.exceptions.PasAssezDeCartesDansLePaquet;
import jbced.citadellesspring.modele.quartiers.impl.PaquetForTests;
import jbced.citadellesspring.modele.roles.Role;
import jbced.citadellesspring.modele.roles.RolesDisponibles;
import jbced.citadellesspring.modele.roles.actions.ActionJeu;
import jbced.citadellesspring.modele.roles.actions.EnumActionsJeu;
import org.junit.Before;
import org.junit.Test;
import org.spring.contextholder.ContextHolder;
import org.springframework.context.ApplicationContext;

public class ResoudreActionJeuEchangerCartesAvecPaquetTest extends ResoudreActionJeuAbstractTests {

    private Arbitre arbitre;

    private Role magicien;

    private PaquetForTests paquet;

    private QuartierEnum q1, q2, q3;

    private final ApplicationContext context = ContextHolder.getApplicationContext();

    @Before
    public void setUp() throws Exception {
        final List<Joueur> joueurs = new ArrayList<Joueur>();
        final Joueur joueur = super.createJoueurHumain();
        final Joueur joueur2 = super.createJoueurHumain();
        joueurs.add(joueur2);
        paquet = new PaquetForTests();
        paquet.melanger();
        q1 = paquet.piocher(1).get(0);
        q2 = paquet.piocher(1).get(0);
        q3 = paquet.piocher(1).get(0);
        final Main main = joueur.main();
        main.ajouter(q1);
        main.ajouter(q2);
        main.ajouter(q3);
        final ArbitreFactory af = (ArbitreFactory) context.getBean(SpringBeans.arbitreFactory.name());
        this.arbitre = af.create(joueurs, paquet);
        final RolesDisponibles rd = arbitre.getRolesDisponibles();
        magicien = rd.tousLesRoles().get(2);
        magicien.abonneAuxActions(arbitre);
        arbitre.add(this);
        magicien.setJoueur(joueur);
    }

    @Test
    public void lActionEchagerCartesAvecPaquetDemandeDeSelectionnerDesQuartiersDeSaMain() throws Exception {
        int nbCartesPaquetAvant = paquet.nbQuartiers();
        int nbCartesMain = magicien.getJoueur().main().nbQuartiers();
        getActionJeuSpeciale(magicien, EnumActionsJeu.EchangerCartesChoisiesAvecPaquet).agir();
        assertEquals(nbCartesPaquetAvant, paquet.nbQuartiers());
        assertEquals(nbCartesMain, magicien.getJoueur().main().nbQuartiers());
        assertNull(getActionJeu(magicien, EnumActionsJeu.EchangerCartesAvecJoueur));
        assertNull(getActionJeu(magicien, EnumActionsJeu.EchangerCartesChoisiesAvecPaquet));
    }

    @Test(expected = PasAssezDeCartesDansLePaquet.class)
    public void sIlNyAPasAssezDeCartesDansLePaquetOnRecupereUneExceptionEtLaMainEstInchangee() throws Exception {
        paquet.piocher(paquet.nbQuartiers() - 1);
        final Main mainAvant = magicien.getJoueur().main();
        getActionJeuSpeciale(magicien, EnumActionsJeu.EchangerCartesChoisiesAvecPaquet).agir();
        assertEquals(mainAvant, magicien.getJoueur().main());
        assertNotNull(getActionJeu(magicien, EnumActionsJeu.EchangerCartesAvecJoueur));
        assertNotNull(getActionJeu(magicien, EnumActionsJeu.EchangerCartesChoisiesAvecPaquet));
    }

    public void process(final InfoArbitre info) throws CitadellesModeleExceptions {
        if (OrdresArbitre.selectionnerCartesDansLaMainDuJoueur.equals(info.getOrdre())) {
            final ActionJeu echangerCartesAvecPaquet = info.getActionJeu();
            final Role r = info.getActionJeu().getRole();
            final Joueur j = r.getJoueur();
            final Main main = j.main();
            final List<QuartierEnum> listeChoisie = new ArrayList<QuartierEnum>();
            listeChoisie.add(q3);
            listeChoisie.add(q1);
            if (paquet.nbQuartiers() <= listeChoisie.size()) {
                throw new PasAssezDeCartesDansLePaquet();
            }
            main.retirerListe(listeChoisie);
            for (final QuartierEnum q : listeChoisie) {
                paquet.remettreCarte(q);
                main.ajouter(paquet.piocher(1).get(0));
            }
            r.removeActionJeu(echangerCartesAvecPaquet);
            r.removeActionJeu(getActionJeu(r, EnumActionsJeu.EchangerCartesAvecJoueur));
        }
    }
}
