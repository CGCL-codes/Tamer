package hottargui.config;

import hottargui.framework.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class tests the Delta Board.
 */
public class TestDeltaBoard {

    private Board board;

    private DeltaBoardFactory boardFactory;

    public TestDeltaBoard() {
    }

    @Before
    public void setUp() {
        boardFactory = new DeltaBoardFactory();
        board = new DeltaBoard(boardFactory);
    }

    /**
     * Test that the Saltmine is in the center position (3,3)
     */
    @Test
    public void testSaltmineCenterPosition() {
        Tile tile = board.getTile(new Position(3, 3));
        TileType tileType = tile.getType();
        assertTrue(tileType.equals(TileType.Saltmine));
    }

    /**
     * Test the Settlement positions
     */
    @Test
    public void testSettlementPositions() {
        Tile tile = board.getTile(new Position(0, 0));
        TileType tileType = tile.getType();
        assertTrue(tileType.equals(TileType.Settlement));
        assertTrue(tile.getOwnerColor().equals(PlayerColor.Red));
        tile = board.getTile(new Position(0, 6));
        tileType = tile.getType();
        assertTrue(tileType.equals(TileType.Settlement));
        assertTrue(tile.getOwnerColor().equals(PlayerColor.Green));
        tile = board.getTile(new Position(6, 0));
        tileType = tile.getType();
        assertTrue(tileType.equals(TileType.Settlement));
        assertTrue(tile.getOwnerColor().equals(PlayerColor.Blue));
        tile = board.getTile(new Position(6, 6));
        tileType = tile.getType();
        assertTrue(tileType.equals(TileType.Settlement));
        assertTrue(tile.getOwnerColor().equals(PlayerColor.Yellow));
    }

    @Test
    public void testPositions() {
        Iterator iterator = board.getBoardIterator();
        int count = 0;
        while (iterator.hasNext()) {
            Tile tile = (Tile) iterator.next();
            System.out.println("Tile no. [" + count + "] has type: " + tile.getType() + " on position " + tile.getPosition());
            count++;
        }
        assertTrue(count == 49);
    }
}
