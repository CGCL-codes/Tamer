package com.editev.chess;

import java.util.StringTokenizer;
import com.editev.chess.piece.Pawn;
import com.editev.util.Lists;

/** A GameHistory is a GameMoves with a history of previous moves. * *  @see See the source <a href="GameHistory.java">here</a>. */
public class GameHistory extends GameMoves {

    /** Number of moves in a long game.       *  Nearly all games should be shorter than this length for best performance.     */
    public static final int A_LONG_GAME = 127;

    /** A list of the moves already applied. */
    public final Moves moveHistory = new Moves(A_LONG_GAME);

    /** A list of the move indices already applied. */
    public final Lists.Shorts moveIndices = new Lists.Shorts(A_LONG_GAME);

    /** Lists of the pieces captured by White. */
    public final Lists.Bytes whiteCaptures = new Lists.Bytes(15);

    /** Lists of the pieces captured by Black. */
    public final Lists.Bytes blackCaptures = new Lists.Bytes(15);

    /** Get the list of pieces captured by this color. */
    public Lists.Bytes getCaptured(boolean isWhite) {
        return isWhite ? whiteCaptures : blackCaptures;
    }

    /** Add a piece to the list of captured pieces */
    public void capture(byte piece, boolean isWhite) {
        if (piece == NO_PIECE) return;
        if (piece == Pawn.EP_CAPTURE) {
            piece = isWhite ? Black.PAWN : White.PAWN;
        }
        getCaptured(isWhite).append(piece);
    }

    /** Apply a specific move to this Game and add it to the history!     *     *  @return the index of the captured piece, if any (special case for ep).     *  @see com.editev.chess.Game. */
    public byte applyMove(Move move) {
        moveHistory.append(move);
        byte captured = super.applyMove(move);
        capture(captured, isWhiteMove());
        return captured;
    }

    /** Apply a move by index to this board.      *     *  @return the index of the captured piece, if any (special case for ep).     *  @param move index of the move to apply.     */
    public byte applyMove(short move) {
        moveIndices.append(move);
        return applyMove(getMove(move));
    }

    /** Separates move in a Move description string. */
    public static final String SEPARATOR = "/";

    /** Apply a whole list of moves to this game.     *     *  @param moves slash-deliniated string listing the moves to apply to this game.     */
    public void applyMoves(String moveString) {
        StringTokenizer st = new StringTokenizer(moveString, SEPARATOR);
        short m;
        while (st.hasMoreTokens()) {
            try {
                m = Short.parseShort(st.nextToken());
            } catch (Exception e) {
                continue;
            }
            try {
                applyMove(m);
            } catch (Exception e) {
                return;
            }
        }
    }

    /** Pick a move at random from all the legal moves and apply it.      *     *  @return true if there were any legal moves from this position.     */
    public boolean moveRandomly() {
        if (getMoveCount() == 0) return false;
        short index = (short) (Math.random() * getMoveCount());
        applyMove(index);
        return true;
    }

    public String getMoveString() {
        if (moves == 0) return "";
        StringBuffer moveString = new StringBuffer();
        moveString.append(moveIndices.getAt(0));
        for (short i = 1; i < moves; i++) {
            moveString.append(SEPARATOR).append(moveIndices.getAt(i));
        }
        return moveString.toString();
    }

    /** Test this class by randomly playing an awful lot of games. */
    public static void main(String[] args) {
        GameHistory game = new GameHistory();
        int i, max = 0, total = 0;
        for (i = 0; i < 100000; i++) {
            try {
                if (i % 200 == 0) game = new GameHistory();
                if (i % 500 == 0) System.out.println(i);
                int c = game.getMoveCount();
                max = Math.max(c, max);
                total += c;
                if (false && 0 == i % 1000) {
                    System.out.println("move " + (i + 1) + ": " + max + "(" + (total / (i + 1)) + ")");
                }
                if (!game.moveRandomly()) game = new GameHistory();
            } catch (Exception e) {
                System.out.println("** exception " + e + " in game " + game.getMoveString());
                game = new GameHistory();
            }
        }
        System.out.println("move " + (i + 1) + ": " + max + "(" + (total / (i + 1)) + ")");
    }
}
