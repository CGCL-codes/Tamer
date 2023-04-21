package luzhin;

import java.io.*;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 * Board.
 * 
 * @author lacungus
 *
 */
public class Board implements Serializable {

    private static Logger logger = Logger.getLogger(Board.class);

    private static final long serialVersionUID = 8974238947L;

    /**
	 * Side of the desk.
	 */
    public static final int LEN = 8;

    /**
	 * Size of the desk.
	 */
    public static final int SIZE = LEN * LEN;

    private boolean isWhiteToMove;

    private boolean isCastledWhite = false;

    private boolean isCastledBlack = false;

    private boolean isRookA1Moved = false;

    private boolean isRookA8Moved = false;

    private boolean isRookH1Moved = false;

    private boolean isRookH8Moved = false;

    private boolean isWhiteKingMoved = false;

    private boolean isBlackKingMoved = false;

    private byte[] field;

    public static final int INITIAL_WHITE_KING_POSITION = 4;

    public static final int INITIAL_BLACK_KING_POSITION = 7 * 8 + 4;

    private Move lastMove = null;

    public boolean isCastledWhite() {
        return isCastledWhite;
    }

    public void setCalstledWhite(boolean b) {
        isCastledWhite = b;
    }

    public boolean isCastledBlack() {
        return isCastledBlack;
    }

    public void setCalstledBlack(boolean b) {
        isCastledBlack = b;
    }

    public boolean isRookA1Moved() {
        return isRookA1Moved;
    }

    public void setRookA1Moved(boolean b) {
        isRookA1Moved = b;
    }

    public boolean isRookA8Moved() {
        return isRookA8Moved;
    }

    public void setRookA8Moved(boolean b) {
        isRookA8Moved = b;
    }

    public boolean isRookH1Moved() {
        return isRookH1Moved;
    }

    public void setRookH1Moved(boolean b) {
        isRookH1Moved = b;
    }

    public boolean isRookH8Moved() {
        return isRookH8Moved;
    }

    public void setRookH8Moved(boolean b) {
        isRookH8Moved = b;
    }

    public void setWhiteKingMoved(boolean isWhiteKingMoved) {
        this.isWhiteKingMoved = isWhiteKingMoved;
    }

    public boolean isWhiteKingMoved() {
        return isWhiteKingMoved;
    }

    public void setBlackKingMoved(boolean isBlackKingMoved) {
        this.isBlackKingMoved = isBlackKingMoved;
    }

    public boolean isBlackKingMoved() {
        return isBlackKingMoved;
    }

    private void setPiece(int pos, int piece) {
        field[pos] = (byte) piece;
    }

    /**
	 * 
	 * @param pos number of the square.
	 * @return piece on the square.
	 */
    public int getPiece(int pos) {
        return field[pos];
    }

    public byte[] getField() {
        return field;
    }

    /**
	 * Copying constructor.
	 */
    public Board(Board b) {
        this.copyFrom(b);
    }

    public final void copyFrom(Board b) {
        this.field = Arrays.copyOf(b.field, b.field.length);
        this.isWhiteToMove = b.isWhiteToMove;
        this.isCastledWhite = b.isCastledWhite;
        this.isCastledBlack = b.isCastledBlack;
        this.isRookA1Moved = b.isRookA1Moved;
        this.isRookA8Moved = b.isRookA8Moved;
        this.isRookH1Moved = b.isRookH1Moved;
        this.isRookH8Moved = b.isRookH8Moved;
        this.isWhiteKingMoved = b.isWhiteKingMoved;
        this.isBlackKingMoved = b.isBlackKingMoved;
        this.lastMove = b.getLastMove();
    }

    @Override
    public boolean equals(Object bb) {
        Board b = (Board) bb;
        for (int i = 0; i < Board.SIZE; ++i) if (this.field[i] != b.field[i]) {
            return false;
        }
        boolean ok = true;
        ok = ok && this.isWhiteToMove == b.isWhiteToMove;
        ok = ok && this.isCastledWhite == b.isCastledWhite;
        ok = ok && this.isCastledBlack == b.isCastledBlack;
        ok = ok && this.isRookA1Moved == b.isRookA1Moved;
        ok = ok && this.isRookA8Moved == b.isRookA8Moved;
        ok = ok && this.isRookH1Moved == b.isRookH1Moved;
        ok = ok && this.isRookH8Moved == b.isRookH8Moved;
        ok = ok && this.isWhiteKingMoved == b.isWhiteKingMoved;
        ok = ok && this.isBlackKingMoved == b.isBlackKingMoved;
        return ok;
    }

    /**
	 * 
	 * @return the last move.
	 */
    public Move getLastMove() {
        return lastMove;
    }

    /**
	 * Set the last move.
	 * @param m
	 */
    public void setLastMove(Move m) {
        this.lastMove = m;
    }

    /**
	 * Creates a board with initial position. 
	 */
    public Board() {
        field = new byte[SIZE];
        setPiece(0, Piece.WR);
        setPiece(1, Piece.WN);
        setPiece(2, Piece.WB);
        setPiece(3, Piece.WQ);
        setPiece(4, Piece.WK);
        setPiece(5, Piece.WB);
        setPiece(6, Piece.WN);
        setPiece(7, Piece.WR);
        for (int i = 8; i < 16; ++i) {
            setPiece(i, Piece.WP);
        }
        for (int i = 16; i < 48; ++i) setPiece(i, Piece.NONE);
        for (int i = 48; i < 56; ++i) {
            setPiece(i, Piece.BP);
        }
        setPiece(56, Piece.BR);
        setPiece(57, Piece.BN);
        setPiece(58, Piece.BB);
        setPiece(59, Piece.BQ);
        setPiece(60, Piece.BK);
        setPiece(61, Piece.BB);
        setPiece(62, Piece.BN);
        setPiece(63, Piece.BR);
        isWhiteToMove = true;
    }

    /**
	 * 
	 * @return weather or not it's the white's move.
	 */
    public boolean isWhiteToMove() {
        return isWhiteToMove;
    }

    /**
	 * 
	 * @param b
	 */
    public void setWhiteToMove(boolean b) {
        this.isWhiteToMove = b;
    }

    /**
	 * Makes a move.
	 * @param move
	 */
    public Board applyMove(Move move) {
        Board newBoard = BoardPool.getBoard();
        newBoard.copyFrom(this);
        int from = move.getFrom();
        int to = move.getTo();
        if (newBoard.getPiece(from) == Piece.WK && from == Position.getPosition("e1") && to == Position.getPosition("g1")) {
            newBoard.setPiece(to, Piece.WK);
            newBoard.setPiece(from, Piece.NONE);
            newBoard.setPiece(Position.getPosition("h1"), Piece.NONE);
            newBoard.setPiece(Position.getPosition("f1"), Piece.WR);
            newBoard.isWhiteKingMoved = true;
            newBoard.isRookH1Moved = true;
            newBoard.isCastledWhite = true;
            newBoard.setLastMove(move);
            newBoard.isWhiteToMove = !newBoard.isWhiteToMove;
            return newBoard;
        }
        if (newBoard.getPiece(from) == Piece.WK && from == Position.getPosition("e1") && to == Position.getPosition("c1")) {
            newBoard.setPiece(to, Piece.WK);
            newBoard.setPiece(from, Piece.NONE);
            newBoard.setPiece(Position.getPosition("a1"), Piece.NONE);
            newBoard.setPiece(Position.getPosition("d1"), Piece.WR);
            newBoard.isWhiteKingMoved = true;
            newBoard.isRookA1Moved = true;
            newBoard.isCastledWhite = true;
            newBoard.setLastMove(move);
            newBoard.isWhiteToMove = !newBoard.isWhiteToMove;
            return newBoard;
        }
        if (newBoard.getPiece(from) == Piece.BK && from == Position.getPosition("e8") && to == Position.getPosition("g8")) {
            newBoard.setPiece(to, Piece.BK);
            newBoard.setPiece(from, Piece.NONE);
            newBoard.setPiece(Position.getPosition("h8"), Piece.NONE);
            newBoard.setPiece(Position.getPosition("f8"), Piece.BR);
            newBoard.isBlackKingMoved = true;
            newBoard.isRookH8Moved = true;
            newBoard.isCastledBlack = true;
            newBoard.setLastMove(move);
            newBoard.isWhiteToMove = !newBoard.isWhiteToMove;
            return newBoard;
        }
        if (newBoard.getPiece(from) == Piece.BK && from == Position.getPosition("e8") && to == Position.getPosition("c8")) {
            newBoard.setPiece(to, Piece.BK);
            newBoard.setPiece(from, Piece.NONE);
            newBoard.setPiece(Position.getPosition("a8"), Piece.NONE);
            newBoard.setPiece(Position.getPosition("d8"), Piece.BR);
            newBoard.isBlackKingMoved = true;
            newBoard.isRookA8Moved = true;
            newBoard.isCastledBlack = true;
            newBoard.setLastMove(move);
            newBoard.isWhiteToMove = !newBoard.isWhiteToMove;
            return newBoard;
        }
        newBoard.setPiece(move.getTo(), getPiece(move.getFrom()));
        newBoard.setPiece(move.getFrom(), Piece.NONE);
        if (move.getFrom() == Position.getPosition("e1")) newBoard.isWhiteKingMoved = true;
        if (move.getFrom() == Position.getPosition("e8")) newBoard.isBlackKingMoved = true;
        if (move.getFrom() == Position.getPosition("a1")) newBoard.isRookA1Moved = true;
        if (move.getFrom() == Position.getPosition("a8")) newBoard.isRookA8Moved = true;
        if (move.getFrom() == Position.getPosition("h1")) newBoard.isRookH1Moved = true;
        if (move.getFrom() == Position.getPosition("h8")) newBoard.isRookH8Moved = true;
        if (newBoard.getPiece(move.getTo()) == Piece.WP || newBoard.getPiece(move.getTo()) == Piece.BP) {
            int i = move.getTo() / 8;
            if (newBoard.isWhiteToMove && i == 7) {
                newBoard.setPiece(move.getTo(), Piece.WQ);
            }
            if (!newBoard.isWhiteToMove && i == 0) {
                newBoard.setPiece(move.getTo(), Piece.BQ);
            }
        }
        newBoard.setLastMove(move);
        newBoard.isWhiteToMove = !newBoard.isWhiteToMove;
        return newBoard;
    }

    /**
	 * 
	 */
    @Override
    public String toString() {
        String s = "";
        for (int i = LEN - 1; i >= 0; --i) {
            int start = i * LEN;
            for (int j = 0; j < LEN; ++j) s += Piece.toChar(getPiece(start + j));
            s += '\n';
        }
        return s;
    }

    /**
	 * Save the board.
	 * @param path path to the file to save.
	 * @throws IOException
	 */
    public void save(String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream outStream = new ObjectOutputStream(fos);
        outStream.writeObject(this);
        outStream.flush();
        System.out.println("Board saved.");
    }

    /**
	 * Load game.
	 * @param path
	 * @return
	 * @throws IOException
	 */
    public static Board load(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream inStream = new ObjectInputStream(fis);
        Board res = null;
        try {
            res = (Board) inStream.readObject();
        } catch (Exception e) {
            logger.error("Error loading board: ", e);
        }
        return res;
    }

    @Override
    public int hashCode() {
        long res = 0;
        long base = 1;
        for (int i = 0; i < Board.SIZE; ++i) {
            res += base * field[i] * field[i];
            res = (int) res;
            base *= 239017;
        }
        return (int) res;
    }
}
