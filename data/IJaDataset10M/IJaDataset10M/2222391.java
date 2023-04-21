package ch.jester.system.ranking.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import ch.jester.common.test.internal.ActivatorProviderForTestCase;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.model.Category;
import ch.jester.model.FinalRanking;
import ch.jester.model.Pairing;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Ranking;
import ch.jester.model.RankingEntry;
import ch.jester.model.RankingSystem;
import ch.jester.model.RankingSystemPoint;
import ch.jester.model.Round;
import ch.jester.model.Tournament;
import ch.jester.model.factories.ModelFactory;
import ch.jester.orm.ORMPlugin;
import ch.jester.system.api.ranking.IRankingSystem;
import ch.jester.system.api.ranking.IRankingSystemEntry;
import ch.jester.system.api.ranking.IRankingSystemManager;
import ch.jester.system.exceptions.NotAllResultsException;
import ch.jester.system.pairing.test.RoundRobinTest;

/**
 * Tests der Sonneborn-Berger Feinwertung
 *
 */
public class SonnebornBergerTest extends ActivatorProviderForTestCase {

    public static final String PLUGIN_ID = "ch.jester.rankingsystem.sonnebornberger";

    public static final String RANKINGSYSTEM_CLASS = "ch.jester.rankingsystem.sonnebornberger.SonnebornBergerRankingSystem";

    public static final String RANKINGSYSTEM_TYPE = "Sonneborn-Berger";

    private ServiceUtility mServiceUtil = new ServiceUtility();

    private IRankingSystem sonnebornbergerSystem;

    private RankingSystem rankingSystem;

    private Tournament t;

    private Category cat;

    private EntityManager entityManager;

    @Before
    public void setUp() {
        IRankingSystemManager rankingSystemManager = mServiceUtil.getService(IRankingSystemManager.class);
        List<IRankingSystemEntry> registredEntries = rankingSystemManager.getRegistredEntries();
        for (IRankingSystemEntry rankingSystemEntry : registredEntries) {
            if (rankingSystemEntry.getImplementationClass().equals(RANKINGSYSTEM_CLASS)) {
                sonnebornbergerSystem = rankingSystemEntry.getService();
                break;
            }
        }
        rankingSystem = new RankingSystem();
        rankingSystem.setPluginId(PLUGIN_ID);
        rankingSystem.setImplementationClass(RANKINGSYSTEM_CLASS);
        rankingSystem.setShortType(RANKINGSYSTEM_TYPE);
        rankingSystem.setRankingSystemNumber(1);
        t = new Tournament();
        t.setName("SonnebornBergerTestTournament");
        t.addRankingSystem(rankingSystem);
        t.setPairingSystemPlugin(RoundRobinTest.PAIRING_PLUGIN);
        t.setPairingSystem(RoundRobinTest.ALGORITHM_CLASS);
        t.setEloCalculator("ch.jester.system.fidecalculator.FideCalculator");
        cat = new Category();
        cat.setDescription("HT1");
        cat.setPlayerCards(initPlayerCards());
        initPairings(cat);
        initResults();
        t.addCategory(cat);
        entityManager = ORMPlugin.getJPAEntityManager();
        if (entityManager.getTransaction().isActive()) {
            entityManager.joinTransaction();
        } else {
            entityManager.getTransaction().begin();
        }
        entityManager.clear();
        entityManager.persist(t);
        entityManager.flush();
    }

    /**
	 * Rangliste mit Sonneborn-Berger Feinwertung erstellen<br/>
	 * Resultate gemäss: <a:href ="http://de.wikipedia.org/wiki/Feinwertung"/>
	 * test-ID: U-SR-3
	 */
    @Test
    public void testExecuteRanking() {
        try {
            Ranking ranking = sonnebornbergerSystem.calculateRanking(cat, null);
            assertTrue(ranking instanceof FinalRanking);
            List<RankingEntry> rankingEntries = ranking.getRankingEntries();
            assertTrue(rankingEntries.size() > 0);
            for (int i = 0; i < rankingEntries.size(); i++) {
                assertEquals(rankingEntries.get(i).getPlayerCard(), cat.getPlayerCards().get(i));
            }
        } catch (NotAllResultsException e) {
            e.printStackTrace();
        }
        testSBPoints();
    }

    /**
	 * Testen der berechneten Sonneborn-Berger Punkte
	 */
    public void testSBPoints() {
        Double sbPunkteC = cat.getPlayerCards().get(2).getRankingSystemPoint(RANKINGSYSTEM_TYPE).getPoints();
        Double sbPunkteD = cat.getPlayerCards().get(3).getRankingSystemPoint(RANKINGSYSTEM_TYPE).getPoints();
        assertEquals(new Double(9.0), sbPunkteC);
        assertEquals(new Double(7.75), sbPunkteD);
    }

    private List<PlayerCard> initPlayerCards() {
        List<Player> players = new ArrayList<Player>();
        Player p1 = new Player();
        p1.setFirstName("a");
        p1.setLastName("A");
        Player p2 = new Player();
        p2.setFirstName("b");
        p2.setLastName("B");
        Player p3 = new Player();
        p3.setFirstName("c");
        p3.setLastName("C");
        Player p4 = new Player();
        p4.setFirstName("d");
        p4.setLastName("D");
        Player p5 = new Player();
        p5.setFirstName("e");
        p5.setLastName("E");
        Player p6 = new Player();
        p6.setFirstName("f");
        p6.setLastName("F");
        Player p7 = new Player();
        p7.setFirstName("g");
        p7.setLastName("G");
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);
        players.add(p6);
        players.add(p7);
        List<PlayerCard> playerCards = new ArrayList<PlayerCard>();
        for (Player player : players) {
            PlayerCard playerCard = ModelFactory.getInstance().createPlayerCard(cat, player);
            playerCard.addRankingSystemPoint(new RankingSystemPoint(RANKINGSYSTEM_TYPE));
            playerCards.add(playerCard);
        }
        return playerCards;
    }

    private void initPairings(Category cat) {
        List<PlayerCard> playerCards = cat.getPlayerCards();
        PlayerCard p1 = playerCards.get(0);
        PlayerCard p2 = playerCards.get(1);
        PlayerCard p3 = playerCards.get(2);
        PlayerCard p4 = playerCards.get(3);
        PlayerCard p5 = playerCards.get(4);
        PlayerCard p6 = playerCards.get(5);
        PlayerCard p7 = playerCards.get(6);
        Round r1 = new Round();
        r1.setNumber(1);
        Pairing pair1 = new Pairing();
        pair1.setWhite(p1);
        pair1.setBlack(p2);
        Pairing pair2 = new Pairing();
        pair2.setWhite(p1);
        pair2.setBlack(p3);
        Pairing pair3 = new Pairing();
        pair3.setWhite(p1);
        pair3.setBlack(p4);
        Pairing pair4 = new Pairing();
        pair4.setWhite(p1);
        pair4.setBlack(p5);
        Pairing pair5 = new Pairing();
        pair5.setWhite(p1);
        pair5.setBlack(p6);
        Pairing pair6 = new Pairing();
        pair6.setWhite(p1);
        pair6.setBlack(p7);
        Pairing pair7 = new Pairing();
        pair7.setWhite(p2);
        pair7.setBlack(p3);
        Pairing pair8 = new Pairing();
        pair8.setWhite(p2);
        pair8.setBlack(p4);
        Pairing pair9 = new Pairing();
        pair9.setWhite(p2);
        pair9.setBlack(p5);
        Pairing pair10 = new Pairing();
        pair10.setWhite(p2);
        pair10.setBlack(p6);
        Pairing pair11 = new Pairing();
        pair11.setWhite(p2);
        pair11.setBlack(p7);
        Pairing pair12 = new Pairing();
        pair12.setWhite(p3);
        pair12.setBlack(p4);
        Pairing pair13 = new Pairing();
        pair13.setWhite(p3);
        pair13.setBlack(p5);
        Pairing pair14 = new Pairing();
        pair14.setWhite(p3);
        pair14.setBlack(p6);
        Pairing pair15 = new Pairing();
        pair15.setWhite(p3);
        pair15.setBlack(p7);
        Pairing pair16 = new Pairing();
        pair16.setWhite(p4);
        pair16.setBlack(p5);
        Pairing pair17 = new Pairing();
        pair17.setWhite(p4);
        pair17.setBlack(p6);
        Pairing pair18 = new Pairing();
        pair18.setWhite(p4);
        pair18.setBlack(p7);
        Pairing pair19 = new Pairing();
        pair19.setWhite(p5);
        pair19.setBlack(p6);
        Pairing pair20 = new Pairing();
        pair20.setWhite(p5);
        pair20.setBlack(p7);
        Pairing pair21 = new Pairing();
        pair21.setWhite(p6);
        pair21.setBlack(p7);
        r1.addPairing(pair1);
        r1.addPairing(pair2);
        r1.addPairing(pair3);
        r1.addPairing(pair4);
        r1.addPairing(pair5);
        r1.addPairing(pair6);
        r1.addPairing(pair7);
        r1.addPairing(pair8);
        r1.addPairing(pair9);
        r1.addPairing(pair10);
        r1.addPairing(pair11);
        r1.addPairing(pair12);
        r1.addPairing(pair13);
        r1.addPairing(pair14);
        r1.addPairing(pair15);
        r1.addPairing(pair16);
        r1.addPairing(pair17);
        r1.addPairing(pair18);
        r1.addPairing(pair19);
        r1.addPairing(pair20);
        r1.addPairing(pair21);
        cat.addRound(r1);
    }

    private void initResults() {
        List<Round> rounds = cat.getRounds();
        Round round = rounds.get(0);
        List<Pairing> pairings = round.getPairings();
        pairings.get(0).setResult("X");
        pairings.get(1).setResult("X");
        pairings.get(2).setResult("1");
        pairings.get(3).setResult("1");
        pairings.get(4).setResult("1");
        pairings.get(5).setResult("1");
        pairings.get(6).setResult("X");
        pairings.get(7).setResult("X");
        pairings.get(8).setResult("1");
        pairings.get(9).setResult("1");
        pairings.get(10).setResult("1");
        pairings.get(11).setResult("X");
        pairings.get(12).setResult("X");
        pairings.get(13).setResult("1");
        pairings.get(14).setResult("1");
        pairings.get(15).setResult("1");
        pairings.get(16).setResult("1");
        pairings.get(17).setResult("1");
        pairings.get(18).setResult("1");
        pairings.get(19).setResult("1");
        pairings.get(20).setResult("1");
    }
}
