package tallyho.model.tile;

import tallyho.model.Team;
import junit.framework.TestCase;

/**
 * Tests the Tree tile type
 */
public class TreeTest extends TestCase {

    private Bear bear;

    private Duck duck;

    private Fox fox;

    private Hunter hunter;

    private Lumberjack lumberjack;

    private Pheasant pheasant;

    private Tree tree;

    protected void setUp() {
        bear = new Bear();
        duck = new Duck();
        fox = new Fox();
        hunter = new Hunter();
        lumberjack = new Lumberjack();
        pheasant = new Pheasant();
        tree = new Tree();
    }

    /**
   * Tests that trees have the correct types of prey
   */
    public void testCanCapture() {
        assertFalse("Shouldn't be able to capture a null Tile", tree.canCapture(null));
        assertFalse("Shouldn't be able to capture a bear", tree.canCapture(bear));
        assertFalse("Shouldn't be able to capture a duck", tree.canCapture(duck));
        assertFalse("Shouldn't be able to capture a fox", tree.canCapture(fox));
        assertFalse("Shouldn't be able to capture a hunter", tree.canCapture(hunter));
        assertFalse("Shouldn't be able to capture a lumberjack", tree.canCapture(lumberjack));
        assertFalse("Shouldn't be able to capture a pheasant", tree.canCapture(pheasant));
        assertFalse("Shouldn't be able to capture a tree", tree.canCapture(new Tree()));
    }

    /**
   * Tests that trees have the correct name
   */
    public void testGetName() {
        assertEquals("Tree", tree.getName());
    }

    /**
   * Tests that trees have the correct types of prey
   */
    public void testGetPrey() {
        Class[] prey = tree.getPrey();
        assertNotNull("Prey array shouldn't be null", prey);
        assertEquals(0, prey.length);
    }

    /**
   * Tests that trees have the correct range
   */
    public void testGetRange() {
        assertEquals(0, tree.getRange());
    }

    /**
   * Tests that trees are on the correct team
   */
    public void testGetTeam() {
        assertEquals(Team.NEUTRAL, tree.getTeam());
    }
}
