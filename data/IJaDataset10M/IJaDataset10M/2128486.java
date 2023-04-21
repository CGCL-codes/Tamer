package org.fbc.shogi.movesgen;

import java.util.Vector;
import junit.framework.TestCase;
import org.fbc.shogi.movesgen.model.Board;
import org.fbc.shogi.movesgen.model.Const;
import org.fbc.shogi.movesgen.model.Move;
import org.fbc.shogi.movesgen.model.PiecePositionVO;
import org.fbc.shogi.movesgen.pieces.Lance;
import org.fbc.shogi.movesgen.pieces.Pawn;
import org.fbc.shogi.movesgen.pieces.Piece;

/**
 * @author fat bold cyclop
 *
 */
public class LanceTest extends TestCase {

    protected Board emptyBoard;

    protected Board initialBoard;

    protected Board opposingPawns;

    PiecePositionVO blh1p;

    PiecePositionVO bl1p;

    PiecePositionVO wl1p;

    protected void setUp() throws Exception {
        super.setUp();
        emptyBoard = new Board();
        initialBoard = new Board(Board.begin_board.clone());
        opposingPawns = new Board();
        opposingPawns.putOnBoard(new Pawn(true), 6 * 9 + 4);
        opposingPawns.putOnBoard(new Pawn(false), 5 * 9 + 4);
        Lance bl1 = new Lance(true);
        Lance wl1 = new Lance(false);
        Lance blh1 = new Lance(true);
        blh1p = new PiecePositionVO(Const.DROP, blh1);
        bl1p = new PiecePositionVO(4 + 9 * (7 - 1), bl1);
        wl1p = new PiecePositionVO(4 + 9 * (3 - 1), wl1);
    }

    /**
	 * Test method for
	 * {@link org.fbc.shogi.movesgen.pieces.Lance#generateMoves(org.fbc.shogi.movesgen.model.Board)}.
	 * Basic test: possible lance moves from initial position.
	 */
    public void testGenerateMoves() {
        Lance l = (Lance) initialBoard.getPieceAt(8 * 9);
        l.generateMoves(initialBoard, 8 * 9);
        Vector<Move> moves = l.getPossibleMoves();
        assertEquals(1, moves.size());
        assertEquals("L9i-9h", moves.get(0).getASCIImove());
        l = (Lance) initialBoard.getPieceAt(0);
        l.generateMoves(initialBoard, 0);
        moves = l.getPossibleMoves();
        assertEquals(1, moves.size());
        assertEquals("L9a-9b", moves.get(0).getASCIImove());
    }

    /**
	 * Test method for
	 * {@link org.fbc.shogi.movesgen.pieces.Lance#generateMoves(org.fbc.shogi.movesgen.model.Board)}.
	 * Blak Lance on the 9th row, white pawn on the 1 row.
	 */
    public void testGenerateMoves2() {
        Board myBoard = new Board();
        Pawn p = new Pawn(false);
        Lance l = new Lance(true);
        myBoard.putOnBoard(p, 9);
        myBoard.putOnBoard(l, 9 * 8);
        l.generateMoves(myBoard, 9 * 8);
        Vector<Move> moves = l.getPossibleMoves();
        assertEquals(9, moves.size());
    }

    /**
	 * Test method for
	 * {@link org.fbc.shogi.movesgen.pieces.Lance#generateMoves(org.fbc.shogi.movesgen.model.Board)}.
	 * Basic test: possible lance moves from initial position.
	 */
    public void testGenerateMoves3() {
        Piece p = blh1p.getPiece();
        int position = blh1p.getPosition();
        p.generateMoves(emptyBoard, position);
        Vector<Move> moves = p.getPossibleMoves();
        assertEquals(81 - 9, moves.size());
    }

    /**
	 * Test method for
	 * {@link org.fbc.shogi.movesgen.pieces.Lance#getPromotedValue()}.
	 */
    public void testGetPromotedValue() {
        assertEquals(Const.BLACK_PLANCE, blh1p.getPiece().getPromotedValue());
        assertEquals(Const.BLACK_PLANCE, bl1p.getPiece().getPromotedValue());
        assertEquals(Const.WHITE_PLANCE, wl1p.getPiece().getPromotedValue());
    }
}
