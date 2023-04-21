package com.thoughtworks.twu.gameoflife;

import org.junit.Assert;
import org.junit.Test;
import com.thoughtworks.twu.gameoflife.grid.Direction;
import com.thoughtworks.twu.gameoflife.grid.Grid;
import com.thoughtworks.twu.gameoflife.grid.GridCoordinate;
import com.thoughtworks.twu.gameoflife.grid.WrappedGrid;

public class WrappedGridTest extends Assert {

    private Grid aNewGridIsCreated(int rows, int columns) {
        return new WrappedGrid(rows, columns);
    }

    @Test
    public void shouldNotReturnWhenGettingTheBordersCellsNeighbours() {
        Grid wrappedGrid = aNewGridIsCreated(2, 2);
        assertNotNull(wrappedGrid.returnNeighbour(0, 0, Direction.NORTHWEST));
        assertNotNull(wrappedGrid.returnNeighbour(0, 1, Direction.EAST));
    }

    @Test
    public void shouldReturnNeighboursInsideTheGrid() {
        Grid wrappedGrid = aNewGridIsCreated(5, 5);
        GridCoordinate nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.NORTHWEST);
        assertEquals(1, nwNeigbour.getRow());
        assertEquals(1, nwNeigbour.getColumn());
        nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.NORTH);
        assertEquals(1, nwNeigbour.getRow());
        assertEquals(2, nwNeigbour.getColumn());
        nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.NORTHEAST);
        assertEquals(1, nwNeigbour.getRow());
        assertEquals(3, nwNeigbour.getColumn());
        nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.WEST);
        assertEquals(2, nwNeigbour.getRow());
        assertEquals(1, nwNeigbour.getColumn());
        nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.EAST);
        assertEquals(2, nwNeigbour.getRow());
        assertEquals(3, nwNeigbour.getColumn());
        nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.SOUTHWEST);
        assertEquals(3, nwNeigbour.getRow());
        assertEquals(1, nwNeigbour.getColumn());
        nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.SOUTH);
        assertEquals(3, nwNeigbour.getRow());
        assertEquals(2, nwNeigbour.getColumn());
        nwNeigbour = wrappedGrid.returnNeighbour(2, 2, Direction.SOUTHEAST);
        assertEquals(3, nwNeigbour.getRow());
        assertEquals(3, nwNeigbour.getColumn());
    }

    @Test
    public void shouldInformIfAGridCoordinateIsValid() {
        Grid normalGrid = aNewGridIsCreated(2, 2);
        assertTrue(normalGrid.isCoordinateValid(0, 0));
        assertTrue(normalGrid.isCoordinateValid(0, 1));
        assertTrue(normalGrid.isCoordinateValid(1, 0));
        assertTrue(normalGrid.isCoordinateValid(1, 1));
        assertTrue(normalGrid.isCoordinateValid(-1, 0));
        assertTrue(normalGrid.isCoordinateValid(2, 0));
        assertTrue(normalGrid.isCoordinateValid(1, -1));
        assertTrue(normalGrid.isCoordinateValid(1, 2));
        assertFalse(normalGrid.isCoordinateValid(-2, 0));
        assertFalse(normalGrid.isCoordinateValid(3, 0));
        assertFalse(normalGrid.isCoordinateValid(1, -2));
        assertFalse(normalGrid.isCoordinateValid(1, 3));
    }
}
