package jbced.citadellesspring.modele.partie.arbitre.impl;

import static org.junit.Assert.*;
import jbced.citadellesspring.core.SpringBeans;
import jbced.citadellesspring.modele.joueurs.Cite;
import jbced.citadellesspring.modele.joueurs.Joueur;
import jbced.citadellesspring.modele.partie.arbitre.ScoreFactory;
import jbced.citadellesspring.modele.partie.arbitre.ScoreJoueur;
import jbced.citadellesspring.modele.quartiers.QuartierEnum;
import org.junit.Before;
import org.junit.Test;
import org.spring.contextholder.ContextHolder;
import org.springframework.context.ApplicationContext;

public class ScoreJoueurTest {

    private Joueur joueur;

    private Cite cite;

    private QuartierEnum q1, q2, q3, q4, q5, q6, q7, q8;

    private final ApplicationContext context = ContextHolder.getApplicationContext();

    private final ScoreFactory scoreFactory = (ScoreFactory) context.getBean(SpringBeans.scoreFactory.name());

    @Before
    public void setUp() throws Exception {
        this.joueur = (Joueur) context.getBean(SpringBeans.joueurHumain.name());
        cite = joueur.cite();
        q1 = QuartierEnum.taverne;
        q2 = QuartierEnum.caserne;
        q3 = QuartierEnum.eglise;
        q4 = QuartierEnum.carriere;
        q5 = QuartierEnum.chateau;
        q6 = QuartierEnum.beffroi;
        q7 = QuartierEnum.cathedrale;
        q8 = QuartierEnum.comptoir;
    }

    @Test
    public void parDefautUnJoueurAUnScoreNul() throws Exception {
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(0, score.total());
    }

    @Test
    public void leScoreCalculePourUneCiteSimpleEstLeBon() throws Exception {
        final int totalValeur = q1.getValeur() + q2.getValeur() + q3.getValeur() + q4.getValeur();
        cite.construire(q1);
        cite.construire(q2);
        cite.construire(q3);
        cite.construire(q4);
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(totalValeur, score.total());
    }

    @Test
    public void uneCiteCompleteRecoitUnBonusAuScore() throws Exception {
        q2 = QuartierEnum.grandeMuraille;
        q5 = QuartierEnum.cimetiere;
        cite.construire(q1);
        cite.construire(q2);
        cite.construire(q3);
        cite.construire(q4);
        cite.construire(q5);
        cite.construire(q6);
        cite.construire(q7);
        cite.construire(q8);
        final int totalValeur = q1.getValeur() + q2.getValeur() + q3.getValeur() + q4.getValeur() + q5.getValeur() + q6.getValeur() + q7.getValeur() + q8.getValeur() + 1;
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(totalValeur, score.total());
    }

    @Test
    public void leScoreDUneCiteAvec5CouleursDonneLeBonusDeCouleur() throws Exception {
        cite.construire(q1);
        cite.construire(q2);
        cite.construire(q3);
        cite.construire(q4);
        cite.construire(q5);
        final int totalValeur = q1.getValeur() + q2.getValeur() + q3.getValeur() + q4.getValeur() + q5.getValeur() + 3;
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(totalValeur, score.total());
    }

    @Test
    public void laCourDesMiraclesPermetDaccorderLeBonusDeCouleurs() throws Exception {
        cite.construire(q1);
        cite.construire(q2);
        cite.construire(q3);
        cite.construire(q4);
        final QuartierEnum courDesMiracles = QuartierEnum.courDesMiracles;
        cite.construire(courDesMiracles);
        final int totalValeur = q1.getValeur() + q2.getValeur() + q3.getValeur() + q4.getValeur() + courDesMiracles.getValeur() + 3;
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(totalValeur, score.total());
    }

    @Test
    public void laCourDesMiraclesNAccordePasLeBonusDeCouleurs() throws Exception {
        cite.construire(q1);
        cite.construire(q2);
        cite.construire(q4);
        cite.construire(q6);
        final QuartierEnum courDesMiracles = QuartierEnum.courDesMiracles;
        cite.construire(courDesMiracles);
        final int totalValeur = q1.getValeur() + q2.getValeur() + q4.getValeur() + q6.getValeur() + courDesMiracles.getValeur();
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(totalValeur, score.total());
    }

    @Test
    public void leTresorImperialAjouteLOrDuJoueurAuTotal() throws Exception {
        joueur.toucherOr(6);
        final QuartierEnum tresorImperial = QuartierEnum.tresorImperial;
        cite.construire(q1);
        cite.construire(q2);
        cite.construire(q3);
        cite.construire(q4);
        cite.construire(tresorImperial);
        final int totalValeur = q1.getValeur() + q2.getValeur() + q3.getValeur() + q4.getValeur() + tresorImperial.getValeur() + joueur.or();
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(totalValeur, score.total());
    }

    @Test
    public void laFontaineAuxSouahitDonneUnPointParQuartierVioletDansLaCite() throws Exception {
        q2 = QuartierEnum.fontaineAuxSouhaits;
        q5 = QuartierEnum.cimetiere;
        cite.construire(q1);
        cite.construire(q2);
        cite.construire(q3);
        cite.construire(q4);
        cite.construire(q5);
        cite.construire(q6);
        cite.construire(q7);
        final int totalValeur = q1.getValeur() + q2.getValeur() + q3.getValeur() + q4.getValeur() + q5.getValeur() + q6.getValeur() + q7.getValeur() + 4;
        final ScoreJoueur score = (ScoreJoueur) scoreFactory.create(joueur);
        assertEquals(totalValeur, score.total());
    }
}
