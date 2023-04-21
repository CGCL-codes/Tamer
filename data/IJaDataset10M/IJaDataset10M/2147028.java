package org.DragonPokerServer.Rules;

import java.util.ArrayList;
import org.DragonPokerServer.Classes.Card;
import org.DragonPokerServer.Classes.HandPlayer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alien
 */
public class StraightFlushRuleTest {

    public StraightFlushRuleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of checkCards method, of class StraightFlushRule.
     */
    @Test
    public void testCheckCards() {
        System.out.println("checkCards");
        ArrayList<Card> tablecards = new ArrayList<Card>();
        tablecards.add(new Card(9, 3));
        tablecards.add(new Card(1, 2));
        tablecards.add(new Card(11, 3));
        tablecards.add(new Card(12, 3));
        tablecards.add(new Card(1, 1));
        ArrayList<Card> playercards = new ArrayList<Card>();
        playercards.add(new Card(13, 3));
        playercards.add(new Card(10, 3));
        StraightFlushRule instance = new StraightFlushRule();
        boolean expResult = true;
        boolean result = instance.checkCards(tablecards, playercards);
        assertEquals(expResult, result);
    }
}
