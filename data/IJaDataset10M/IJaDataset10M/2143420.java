package net.sf.excompcel.gui.model;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author detlevs
 *
 */
public class GridPositionTest {

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#GridPosition()}.
	 */
    @Test
    public void testGridPosition() {
        GridPosition pos = new GridPosition();
        assertNotNull(pos);
        assertEquals(pos.getGridCol(), "");
        assertEquals(pos.getGridRow(), new Integer(0));
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#GridPosition(java.lang.String, java.lang.Integer)}.
	 */
    @Test
    public void testGridPositionStringInteger() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        assertEquals(pos.getGridCol(), "A");
        assertEquals(pos.getGridRow(), new Integer(5));
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#isConfigured()}.
	 */
    @Test
    public void testIsConfigured() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        assertTrue(pos.isConfigured());
        pos = new GridPosition();
        assertNotNull(pos);
        assertFalse(pos.isConfigured());
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#setGridPosition(java.lang.String)}.
	 */
    @Test
    public void testSetGridPosition() {
        GridPosition pos = new GridPosition();
        assertNotNull(pos);
        pos = new GridPosition("A", 5);
        String position = "";
        pos.setGridPosition(position);
        assertEquals("A:5", pos.getGridPosition());
        pos = new GridPosition("A", 5);
        position = null;
        pos.setGridPosition(position);
        assertEquals("A:5", pos.getGridPosition());
        pos = new GridPosition("A", 5);
        position = "H";
        pos.setGridPosition(position);
        assertEquals(position + ":5", pos.getGridPosition());
        pos = new GridPosition("A", 5);
        position = "4";
        pos.setGridPosition(position);
        assertEquals("A:5", pos.getGridPosition());
        pos = new GridPosition("A", 5);
        position = "L:33";
        pos.setGridPosition(position);
        assertEquals(position, pos.getGridPosition());
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#getGridPosition()}.
	 */
    @Test
    public void testGetGridPosition() {
        GridPosition pos = new GridPosition();
        assertNotNull(pos);
        assertEquals(":0", pos.getGridPosition());
        pos = new GridPosition("A", 5);
        assertNotNull(pos);
        assertEquals("A:5", pos.getGridPosition());
        pos = new GridPosition("T", 12);
        assertNotNull(pos);
        assertEquals("T:12", pos.getGridPosition());
        pos = new GridPosition("AN", 9);
        assertNotNull(pos);
        assertEquals("AN:9", pos.getGridPosition());
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#setGridCol(java.lang.String)}.
	 */
    @Test
    public void testSetGridCol() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        pos.setGridCol("B");
        assertEquals(pos.getGridCol(), "B");
        assertEquals(pos.getGridRow(), new Integer(5));
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#getGridCol()}.
	 */
    @Test
    public void testGetGridCol() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        pos.setGridCol("B");
        assertEquals(pos.getGridCol(), "B");
        pos.setGridCol("AA");
        assertEquals(pos.getGridCol(), "AA");
        try {
            pos.setGridCol("5");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#setGridRow(java.lang.Integer)}.
	 */
    @Test
    public void testSetGridRow() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        pos.setGridRow(12);
        assertEquals(pos.getGridRow(), new Integer(12));
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#getGridRow()}.
	 */
    @Test
    public void testGetGridRow() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        pos.setGridRow(45);
        assertEquals(pos.getGridRow(), new Integer(45));
        pos.setGridRow(0);
        assertEquals(pos.getGridRow(), new Integer(0));
        pos.setGridRow(-1);
        assertEquals(pos.getGridRow(), new Integer(0));
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#equals(java.lang.Object)}.
	 */
    @Test
    public void testEqualsObject() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        GridPosition posEqual = new GridPosition("A", 5);
        assertNotNull(posEqual);
        assertTrue(pos.equals(posEqual));
        GridPosition posDiff = new GridPosition("A", 12);
        assertNotNull(posDiff);
        assertFalse(pos.equals(posDiff));
        posDiff = new GridPosition("D", 5);
        assertNotNull(posDiff);
        assertFalse(pos.equals(posDiff));
        posDiff = new GridPosition("F", 12);
        assertNotNull(posDiff);
        assertFalse(pos.equals(posDiff));
    }

    /**
	 * Test method for {@link net.sf.excompcel.gui.model.GridPosition#toString()}.
	 */
    @Test
    public void testToString() {
        GridPosition pos = new GridPosition("A", 5);
        assertNotNull(pos);
        assertEquals("Col=A Row=5", pos.toString());
    }
}
