package tests.board.pieces;

import junit.framework.TestCase;
import root.board.moviment.Moviment;
import root.board.pieces.Bishop;

/**
 * @author Calebe
 * 
 */
public class BishopTest extends TestCase {

    public void testBishopMoviments() {
        Moviment[] moviments = new Bishop().moviments(3, 3);
        assertEquals(13, moviments.length);
    }

    public void testBishopNorthEast() {
        Moviment[] moviments = new Bishop().moviments(7, 0);
        assertEquals(7, moviments.length);
    }

    public void testBishopNorthWest() {
        Moviment[] moviments = new Bishop().moviments(0, 0);
        assertEquals(7, moviments.length);
    }

    public void testBishopSouthEast() {
        Moviment[] moviments = new Bishop().moviments(7, 7);
        assertEquals(7, moviments.length);
    }

    public void testBishopSouthWest() {
        Moviment[] moviments = new Bishop().moviments(0, 7);
        assertEquals(7, moviments.length);
    }

    public void testBishopUnBlockedPosition() {
        Moviment[] moviments = new Bishop().moviments(6, 6);
        assertEquals(9, moviments.length);
    }
}
