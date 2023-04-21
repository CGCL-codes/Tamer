package org.jskat.util.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.jskat.AbstractJSkatTest;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.data.SkatGameData;
import org.jskat.util.Card;
import org.jskat.util.GameType;
import org.jskat.util.Player;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for suit games
 */
public class SuitRuleTest extends AbstractJSkatTest {

    private GameAnnouncementFactory factory;

    private static BasicSkatRules clubsRules = SkatRuleFactory.getSkatRules(GameType.CLUBS);

    /**
	 * @see BeforeClass
	 */
    @Before
    public void initialize() {
        factory = GameAnnouncement.getFactory();
        factory.setGameType(GameType.CLUBS);
    }

    /**
	 * Checks @see SuitGrandRules#calcGameWon()
	 */
    @Test
    public void calcGameWon() {
        SkatGameData data = new SkatGameData();
        data.setAnnouncement(factory.getAnnouncement());
        data.setDeclarer(Player.FOREHAND);
        data.setDeclarerScore(61);
        assertTrue(clubsRules.calcGameWon(data));
    }

    /**
	 * Checks @see SuitGrandRules#calcGameResult()
	 */
    @Test
    public void calcGameResultGameWonWithoutJacks() {
        SkatGameData data = new SkatGameData();
        data.setDeclarerPickedUpSkat(true);
        data.setAnnouncement(factory.getAnnouncement());
        data.setDeclarer(Player.FOREHAND);
        data.setDeclarerScore(61);
        data.setDealtCard(Player.FOREHAND, Card.CA);
        data.calcResult();
        assertEquals(60, data.getResult().getGameValue());
        assertEquals(60, clubsRules.calcGameResult(data));
    }

    /**
	 * Checks @see SuitGrandRules#calcGameResult()
	 */
    @Test
    public void calcGameResultGameWonClubJack() {
        SkatGameData data = new SkatGameData();
        data.setDeclarerPickedUpSkat(true);
        data.setAnnouncement(factory.getAnnouncement());
        data.setDeclarer(Player.FOREHAND);
        data.setDeclarerScore(61);
        data.setDealtCard(Player.FOREHAND, Card.CJ);
        data.setDealtCard(Player.FOREHAND, Card.HJ);
        data.setDealtCard(Player.FOREHAND, Card.DJ);
        data.setDealtCard(Player.FOREHAND, Card.CA);
        data.calcResult();
        assertEquals(24, data.getResult().getGameValue());
        assertEquals(24, clubsRules.calcGameResult(data));
    }

    /**
	 * Checks @see GrandRules#calcGameResult()
	 */
    @Test
    public void calcGameResultGameWonClubSpadeHeartJack() {
        SkatGameData data = new SkatGameData();
        data.setDeclarerPickedUpSkat(true);
        data.setAnnouncement(factory.getAnnouncement());
        data.setDeclarer(Player.FOREHAND);
        data.setDeclarerScore(61);
        data.setDealtCard(Player.FOREHAND, Card.CJ);
        data.setDealtCard(Player.FOREHAND, Card.SJ);
        data.setDealtCard(Player.FOREHAND, Card.HJ);
        data.setDealtCard(Player.FOREHAND, Card.CA);
        data.calcResult();
        assertEquals(48, data.getResult().getGameValue());
        assertEquals(48, clubsRules.calcGameResult(data));
    }

    /**
	 * Checks @see SuitGrandRules#calcGameResult()
	 */
    @Test
    public void calcGameResultGameWonClubSpadeHeartDiamondJack() {
        SkatGameData data = new SkatGameData();
        data.setDeclarerPickedUpSkat(true);
        data.setAnnouncement(factory.getAnnouncement());
        data.setDeclarer(Player.FOREHAND);
        data.setDeclarerScore(61);
        data.setDealtCard(Player.FOREHAND, Card.CJ);
        data.setDealtCard(Player.FOREHAND, Card.SJ);
        data.setDealtCard(Player.FOREHAND, Card.HJ);
        data.setDealtCard(Player.FOREHAND, Card.DJ);
        data.setDealtCard(Player.FOREHAND, Card.CT);
        data.calcResult();
        assertEquals(60, data.getResult().getGameValue());
        assertEquals(60, clubsRules.calcGameResult(data));
    }

    /**
	 * Checks @see SuitGrandRules#calcGameResult()
	 */
    @Test
    public void calcGameResultGameWonMoreTops() {
        SkatGameData data = new SkatGameData();
        data.setDeclarerPickedUpSkat(true);
        data.setAnnouncement(factory.getAnnouncement());
        data.setDeclarer(Player.FOREHAND);
        data.setDeclarerScore(61);
        data.setDealtCard(Player.FOREHAND, Card.CJ);
        data.setDealtCard(Player.FOREHAND, Card.SJ);
        data.setDealtCard(Player.FOREHAND, Card.HJ);
        data.setDealtCard(Player.FOREHAND, Card.DJ);
        data.setDealtCard(Player.FOREHAND, Card.CA);
        data.setDealtCard(Player.FOREHAND, Card.CT);
        data.setDealtCard(Player.FOREHAND, Card.CQ);
        data.calcResult();
        assertEquals(84, data.getResult().getGameValue());
        assertEquals(84, clubsRules.calcGameResult(data));
    }
}
