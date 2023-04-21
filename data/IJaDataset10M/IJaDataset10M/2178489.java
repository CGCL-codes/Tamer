package de.hft_stuttgart.botwar.server.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pmv-mail@gmx.de
 */
public class CoinsystemTest {

    public CoinsystemTest() {
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
     * Test of getCoinsForGame method, of class Coinsystem.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCoinsForGameZeroRank() {
        Coinsystem.getCoinsForGame(2, 0);
    }

    /**
     * Test of getCoinsForGame method, of class Coinsystem.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCoinsForGameToHighRank() {
        Coinsystem.getCoinsForGame(2, 5);
    }

    /**
     * Test of getCoinsForGame method, of class Coinsystem.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCoinsForGameNotEnoughPlayers() {
        Coinsystem.getCoinsForGame(1, 3);
    }

    /**
     * Test of getCoinsForGame method, of class Coinsystem.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetCoinsForGameToManyPlayers() {
        Coinsystem.getCoinsForGame(5, 2);
    }

    /**
     * Test of getCoinsForGame method, of class Coinsystem.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetCoinsForGameDrawNotSupported() {
        Coinsystem.getCoinsForGame(3, 2, true);
    }

    /**
     * Test of getCoinsForGame method, of class Coinsystem.
     */
    @Test
    public void testGetCoinsForGame() {
        assertEquals(25, Coinsystem.getCoinsForGame(2, 2, true));
        assertEquals(50, Coinsystem.getCoinsForGame(2, 2));
        assertEquals(30, Coinsystem.getCoinsForGame(3, 3));
        assertEquals(75, Coinsystem.getCoinsForGame(3, 2));
        assertEquals(150, Coinsystem.getCoinsForGame(3, 1));
        assertEquals(20, Coinsystem.getCoinsForGame(4, 4));
        assertEquals(45, Coinsystem.getCoinsForGame(4, 3));
        assertEquals(90, Coinsystem.getCoinsForGame(4, 2));
        assertEquals(180, Coinsystem.getCoinsForGame(4, 1));
    }

    /**
     * Test of getLevelForCoins method, of class Coinsystem.
     */
    @Test
    public void testGetLevelForCoins() {
        assertEquals(1, Coinsystem.getLevelForCoins(30));
        assertEquals(2, Coinsystem.getLevelForCoins(500));
        assertEquals(3, Coinsystem.getLevelForCoins(1251));
        assertEquals(4, Coinsystem.getLevelForCoins(4999));
        assertEquals(Coinsystem.getTopLevel(), Coinsystem.getLevelForCoins(1000000));
    }

    /**
     * Test of getLevelForCoins method, of class Coinsystem.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetLevelForCoinsNegativCoins() {
        Coinsystem.getLevelForCoins(-1);
    }

    /**
     * Test of getCoinsForLevel method, of class Coinsystem.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCoinsForLevelZeroLevel() {
        Coinsystem.getCoinsForLevel(0);
    }

    /**
     * Test of getCoinsForLevel method, of class Coinsystem.
     */
    @Test
    public void testGetCoinsForLevel() {
        assertEquals(0, Coinsystem.getCoinsForLevel(1));
        assertEquals(500, Coinsystem.getCoinsForLevel(2));
        assertEquals(1250, Coinsystem.getCoinsForLevel(3));
        assertEquals(2500, Coinsystem.getCoinsForLevel(4));
        assertEquals(5000, Coinsystem.getCoinsForLevel(5));
        assertEquals(7500, Coinsystem.getCoinsForLevel(6));
        assertEquals(Long.MAX_VALUE, Coinsystem.getCoinsForLevel(7));
    }
}
