package net.kan.lianliankan.domain.impl;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class CellTest {

    private Cell cell;

    private Location location;

    @Before
    public void setUp() {
        cell = new Cell(3, 4);
        location = new Location(3, 4);
    }

    @Test
    public void testGetColumn() {
        assertTrue(cell.getColumn() == 3);
    }

    @Test
    public void testGetLocation() {
        Location cellLocation = cell.getLocation();
        assertTrue(location.getX() == cellLocation.getX());
        assertTrue(location.getY() == cellLocation.getY());
    }

    @Test
    public void testGetRow() {
        assertTrue(cell.getRow() == 4);
    }

    @Test
    public void testSetTaken() {
        assertFalse(cell.isTaken());
        cell.setTaken(true);
        assertTrue(cell.isTaken());
    }
}
