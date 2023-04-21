package test.main;

import net.sf.nebulacards.main.*;
import junit.framework.*;

public class PlayerTest extends TestCase {

    public PlayerTest(String s) {
        super(s);
    }

    /**
	 * Test the bidding methods.
	 */
    public void testBid() {
        Player p = new Player();
        assertFalse(p.hasBid());
        p.setBid(100);
        assertTrue(p.hasBid());
        assertEquals(p.getBid(), 100);
        p.clearBid();
        assertFalse(p.hasBid());
    }

    /**
	 * Test the clone method.  Verify that the cloned object is indeed separate
	 * from the original.
	 */
    public void testClone() {
        Player p = new Player("original");
        p.setVacant(false);
        p.setBot(false);
        Player c = (Player) p.clone();
        assertNotSame(p, c);
        assertEquals(p, c);
        c.incScore(10);
        assertFalse(c.equals(p));
        assertFalse(c.getScore() == p.getScore());
    }
}
