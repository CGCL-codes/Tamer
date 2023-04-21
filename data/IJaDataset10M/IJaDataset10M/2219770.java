package com.googlecode.progobots;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class BeepersActionsTest {

    private World world;

    private WorldSelection selection;

    private BeepersActions actions;

    @Before
    public void setUp() throws Exception {
        world = new World();
        selection = new WorldSelection();
        actions = new BeepersActions();
    }

    @Test
    public void testPickBeeper() {
        actions.pickBeeper(world, selection);
        assertEquals(Beepers.getBeepers(world, selection.getLocation()), 0);
        Beepers.setBeepers(world, selection.getLocation(), 2);
        actions.pickBeeper(world, selection);
        assertEquals(Beepers.getBeepers(world, selection.getLocation()), 1);
        selection.setLocation(new Location(2, 3));
        Beepers.setBeepers(world, selection.getLocation(), 5);
        actions.pickBeeper(world, selection);
        assertEquals(Beepers.getBeepers(world, selection.getLocation()), 4);
    }

    @Test
    public void testPutBeeper() {
        actions.putBeeper(world, selection);
        assertEquals(Beepers.getBeepers(world, selection.getLocation()), 1);
        actions.putBeeper(world, selection);
        assertEquals(Beepers.getBeepers(world, selection.getLocation()), 2);
        selection.setLocation(new Location(2, 3));
        actions.putBeeper(world, selection);
        assertEquals(Beepers.getBeepers(world, selection.getLocation()), 1);
        actions.putBeeper(world, selection);
        assertEquals(Beepers.getBeepers(world, selection.getLocation()), 2);
    }
}
