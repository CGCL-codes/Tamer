package tgreiner.amy.chess.engine;

import tgreiner.amy.bitboard.BoardConstants;
import tgreiner.amy.chess.engine.ChessBoard;
import tgreiner.amy.chess.engine.IllegalEpdException;
import tgreiner.amy.chess.engine.IllegalSANException;
import tgreiner.amy.chess.engine.Move;
import junit.framework.TestCase;

/**
 * JUnit test case for Move.
 *
 * @author Thorsten Greiner.
 */
public class MoveTest extends TestCase {

    public MoveTest(String name) {
        super(name);
    }

    public void testCheckMate() throws IllegalEpdException, IllegalSANException {
        ChessBoard cb = new ChessBoard("3K4/8/3k4/8/8/8/8/q7 b - -");
        int move = Move.parseSAN(cb, "Qa8#");
        assertEquals("Qa8#", Move.toSAN(cb, move));
    }

    public void testCheckMate2() throws IllegalEpdException, IllegalSANException {
        ChessBoard cb = new ChessBoard("r1bn1r2/pp4p1/6k1/q1b1Pp2/2Bp1P2/N6R/1PP1N2P/1K41R w - -");
        int move = Move.parseSAN(cb, "Rg1#");
        assertEquals("Rg1#", Move.toSAN(cb, move));
    }

    public void testParseSan() throws IllegalEpdException, IllegalSANException {
        ChessBoard cb = new ChessBoard("4K3/3N4/2b5/8/8/3N4/8/7k w - -");
        int move = Move.parseSAN(cb, "Ne5");
        assertEquals(BoardConstants.D3, Move.getFrom(move));
    }

    public void testIsPromotion() {
        assertTrue(Move.isPromotion(Move.PROMO_QUEEN));
        assertFalse(Move.isPromotion(0));
    }

    public void testIsCastle() {
        assertTrue(Move.isCastle(Move.CASTLE_KSIDE));
        assertTrue(Move.isCastle(Move.CASTLE_QSIDE));
        assertFalse(Move.isCastle(0));
    }

    public void testIsKingSideCastle() {
        assertTrue(Move.isKingSideCastle(Move.CASTLE_KSIDE));
        assertFalse(Move.isKingSideCastle(Move.CASTLE_QSIDE));
    }
}
