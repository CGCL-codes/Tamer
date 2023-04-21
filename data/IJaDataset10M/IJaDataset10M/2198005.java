package xhack.object;

import xhack.game.GameStub;
import junit.framework.TestCase;

public class CharacterTest extends TestCase {

    Character c1;

    protected void setUp() {
        c1 = new Character(new GameStub());
        CharacterTest.setUpValues(c1);
    }

    public static void setUpValues(Character c) {
        CreatureTest.setUpValues(c);
    }

    public static void changeValues(Character c) {
        CreatureTest.changeValues(c);
        c.gold = c.gold + 1;
    }

    /**
	 * Test copy and equals methods
	 *
	 */
    public void testEquals() {
        Character c2 = new Character(c1);
        assertTrue(c1.equals(c2));
        CharacterTest.changeValues(c2);
        assertTrue(!c1.equals(c2));
    }
}
