package it.chesslab.junit;

import it.chesslab.chessboard.Chessboard;
import it.chesslab.chessboard.CompletePosition;
import it.chesslab.chessboard.Move;
import it.chesslab.chessboard.Piece;
import it.chesslab.chessboard.Resolver;
import junit.framework.TestCase;

/** 
 * @author Romano Ghetti
 */
public class ResolverTestCase extends TestCase {

    /**  */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(ResolverTestCase.class);
    }

    /**  */
    private static final String FEN_1 = "r1b2rk1/1p2bppp/p6B/3np3/2qNP3/6Q1/PPP3PP/3R1RK1 b - - 0 17";

    private static final String FEN_2 = "1rb2rk1/4bp1p/p4np1/4p3/3NP1Q1/2P1q3/1p3RPP/2R3K1 w - - 0 26";

    private static final String FEN_3 = "1rb2rk1/4bp1p/p4np1/4p3/3NP1Q1/2P1q3/1p3RPP/2R3K1 b - - 0 26";

    /**  */
    private Resolver resolver;

    /**  */
    public ResolverTestCase(String arg0) {
        super(arg0);
    }

    /**  */
    protected void setUp() throws Exception {
        super.setUp();
        this.resolver = new Resolver();
    }

    /**  */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for 'it.chesslab.chessboard.Resolver.getMove(CompletePosition, int, int)'
     */
    public final void testGetMove() {
        Move move;
        move = this.resolver.getMove(new CompletePosition(FEN_1), Chessboard.SQUARE_C4, Chessboard.SQUARE_F1);
        assertEquals(move.toString(), "qc4xf1+{Qxf1+}");
        move = this.resolver.getMove(new CompletePosition(FEN_1), Chessboard.SQUARE_G7, Chessboard.SQUARE_H6);
        assertNull(move);
        move = this.resolver.getMove(new CompletePosition(FEN_1), Chessboard.SQUARE_G7, Chessboard.SQUARE_G6);
        assertEquals(move.toString(), "pg7g6{g6}");
        move = this.resolver.getMove(new CompletePosition(FEN_1), Chessboard.SQUARE_C4, Chessboard.SQUARE_C3);
        assertEquals(move.toString(), "qc4c3{Qc3}");
        move = this.resolver.getMove(new CompletePosition(FEN_1), Chessboard.SQUARE_C4, Chessboard.SQUARE_D2);
        assertNull(move);
        move = this.resolver.getMove(new CompletePosition(FEN_1), Chessboard.SQUARE_C4, Chessboard.SQUARE_C1);
        assertNull(move);
        move = this.resolver.getMove(new CompletePosition(FEN_2), Chessboard.SQUARE_C1, Chessboard.SQUARE_E1);
        assertEquals(move.toString(), "Rc1e1{Re1}");
        move = this.resolver.getMove(new CompletePosition(FEN_2), Chessboard.SQUARE_F2, Chessboard.SQUARE_E2);
        assertNull(move);
        move = this.resolver.getMove(new CompletePosition(FEN_2), Chessboard.SQUARE_G4, Chessboard.SQUARE_C8);
        assertEquals(move.toString(), "Qg4xc8{Qxc8}");
        move = this.resolver.getMove(new CompletePosition(FEN_2), Chessboard.SQUARE_G4, Chessboard.SQUARE_G7);
        assertNull(move);
        move = this.resolver.getMove(new CompletePosition(FEN_3), Chessboard.SQUARE_G4, Chessboard.SQUARE_C8);
        assertNull(move);
        move = this.resolver.getMove(new CompletePosition(FEN_3), Chessboard.SQUARE_E3, Chessboard.SQUARE_F2);
        assertEquals(move.toString(), "qe3xf2+{Qxf2+}");
        move = this.resolver.getMove(new CompletePosition(FEN_3), Chessboard.SQUARE_B2, Chessboard.SQUARE_B1);
        assertEquals(move.toString(), "pb2b1=?");
        move = this.resolver.getMove(new CompletePosition(FEN_3), Chessboard.SQUARE_B2, Chessboard.SQUARE_C1);
        assertEquals(move.toString(), "pb2xc1=?");
    }

    public final void testCompletePromotionMove() {
        Move move;
        CompletePosition completePosition;
        completePosition = new CompletePosition(FEN_3);
        move = this.resolver.getMove(completePosition, Chessboard.SQUARE_B2, Chessboard.SQUARE_B1);
        assertEquals(move.toString(), "pb2b1=?");
        assertTrue(move.isPromotionUndefined());
        this.resolver.completePromotionMove(completePosition, move, Piece.QUEEN);
        assertFalse(move.isPromotionUndefined());
        assertEquals(move.toString(), "pb2b1=Q{b1=Q}");
        completePosition = new CompletePosition(FEN_3);
        move = this.resolver.getMove(completePosition, Chessboard.SQUARE_B2, Chessboard.SQUARE_C1);
        assertEquals(move.toString(), "pb2xc1=?");
        assertTrue(move.isPromotionUndefined());
        this.resolver.completePromotionMove(completePosition, move, Piece.QUEEN);
        assertFalse(move.isPromotionUndefined());
        assertEquals(move.toString(), "pb2xc1+=Q{bxc1=Q+}");
    }

    public final void testGetMovesCompletePosition() {
    }

    public final void testGetMovesCompletePositionInt() {
    }

    public final void testExecuteMoveCompletePositionMove() {
    }

    public final void testExecuteMoveCompletePositionString() {
    }
}
