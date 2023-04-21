package pieces.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import pieces.Bishop;
import pieces.Piece.ChessColor;

public class TestBishop {

    Bishop test = new Bishop(10, 2, 4, ChessColor.BLACK);

    private int[][] directions = { { 1, 1 }, { -1, 1 } };

    @Test
    public void testGetMoves() {
        assertNull(test.getMoves());
    }

    @Test
    public void testGetMovableDirections() {
        assertArrayEquals(directions, test.getMovableDirections());
    }
}
