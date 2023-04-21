package de.icanmakeit.jpokerstats.jpa;

import static org.junit.Assert.*;
import org.junit.Test;
import de.icanmakeit.jpokerstats.util.DateTimeUtil;

/**
 * Test class for the {@link Game} class.
 * @author Hendrik Busch
 * @version $Revision$
 *
 */
public class GameTest {

    /**
	 * Tests and makes sure that the {@link Game#equals(Object)} and
	 * {@link Game#hashCode()} methods work as expected.
	 */
    @Test
    public void testHashCodeAndEquals() {
        final Game one = new Game();
        final Game two = new Game();
        assertNotSame(one, two);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
        one.setBuyinChips(500L);
        one.setBuyinCosts(2000000L);
        one.setRebuyChips(500L);
        one.setRebuyCosts(1000000L);
        one.setDate(DateTimeUtil.makeDate(2008, 2, 9));
        one.setStartTime(DateTimeUtil.makeDateWithTime(2008, 2, 9, 22, 49));
        one.setEndTime(DateTimeUtil.makeDateWithTime(2008, 2, 9, 23, 55));
        one.setGameType(GameType.CASH);
        two.setBuyinChips(500L);
        two.setBuyinCosts(2000000L);
        two.setRebuyChips(500L);
        two.setRebuyCosts(1000000L);
        two.setDate(DateTimeUtil.makeDate(2008, 2, 9));
        two.setStartTime(DateTimeUtil.makeDateWithTime(2008, 2, 9, 22, 49));
        two.setEndTime(DateTimeUtil.makeDateWithTime(2008, 2, 9, 23, 55));
        two.setGameType(GameType.CASH);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
        one.setId(47);
        two.setId(44);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
        two.setBuyinChips(501L);
        assertFalse(one.equals(two));
        assertFalse(one.hashCode() == two.hashCode());
        two.setBuyinChips(500L);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
        two.setEndTime(null);
        assertFalse(one.equals(two));
        assertFalse(one.hashCode() == two.hashCode());
        one.setEndTime(null);
        assertEquals(one, two);
        assertEquals(one.hashCode(), two.hashCode());
        one.setStartTime(null);
        assertFalse(one.equals(two));
        assertFalse(one.hashCode() == two.hashCode());
    }
}
