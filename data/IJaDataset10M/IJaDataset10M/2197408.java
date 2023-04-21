package tgreiner.amy.chess.engine.recognizer;

import tgreiner.amy.chess.engine.ChessBoard;
import tgreiner.amy.chess.engine.ChessConstants;
import tgreiner.amy.chess.engine.EvalMasks;

/**
 * A recognizer for K+B+P versus K endgames.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public class KBPKRecognizer implements Recognizer {

    /** @see Recognizer#probe */
    public int probe(final ChessBoard board) {
        if (board.getMaterialSignature(false) == 0) {
            if (blackKingDefendsH8(board) || blackKingDefendsA8(board)) {
                return EXACT;
            }
        }
        if (board.getMaterialSignature(true) == 0) {
            if (whiteKingDefendsH1(board) || whiteKingDefendsA1(board)) {
                return EXACT;
            }
        }
        return USELESS;
    }

    /** @see Recognizer#getValue */
    public int getValue() {
        return 0;
    }

    /**
     * Check wether the black king defends h8.
     *
     * @param board the board
     * @return <code>true</code> if the black king defends h8.
     */
    boolean blackKingDefendsH8(final ChessBoard board) {
        if ((board.getMask(true, ChessConstants.BISHOP) & EvalMasks.BLACK_SQUARES) != 0L) {
            return false;
        }
        if ((board.getMask(true, ChessConstants.PAWN) & ~EvalMasks.FILE_MASK[7]) != 0L) {
            return false;
        }
        int sq = board.getKingPos(false);
        return (sq >> 3) >= 6 && (sq & 7) >= 6;
    }

    /**
     * Check wether the black king defends a8.
     *
     * @param board the board
     * @return <code>true</code> if the black king defends a8.
     */
    boolean blackKingDefendsA8(final ChessBoard board) {
        if ((board.getMask(true, ChessConstants.BISHOP) & EvalMasks.WHITE_SQUARES) != 0L) {
            return false;
        }
        if ((board.getMask(true, ChessConstants.PAWN) & ~EvalMasks.FILE_MASK[0]) != 0L) {
            return false;
        }
        int sq = board.getKingPos(false);
        return (sq >> 3) >= 6 && (sq & 7) <= 1;
    }

    /**
     * Check wether the white king defends h1.
     *
     * @param board the board
     * @return <code>true</code> if the white king defends h1.
     */
    boolean whiteKingDefendsH1(final ChessBoard board) {
        if ((board.getMask(false, ChessConstants.BISHOP) & EvalMasks.WHITE_SQUARES) != 0L) {
            return false;
        }
        if ((board.getMask(false, ChessConstants.PAWN) & ~EvalMasks.FILE_MASK[7]) != 0L) {
            return false;
        }
        int sq = board.getKingPos(true);
        return (sq >> 3) <= 1 && (sq & 7) >= 6;
    }

    /**
     * Check wether the white king defends a1.
     *
     * @param board the board
     * @return <code>true</code> if the white king defends a1.
     */
    boolean whiteKingDefendsA1(final ChessBoard board) {
        if ((board.getMask(false, ChessConstants.BISHOP) & EvalMasks.BLACK_SQUARES) != 0L) {
            return false;
        }
        if ((board.getMask(false, ChessConstants.PAWN) & ~EvalMasks.FILE_MASK[0]) != 0L) {
            return false;
        }
        int sq = board.getKingPos(true);
        return (sq >> 3) <= 1 && (sq & 7) <= 1;
    }
}
