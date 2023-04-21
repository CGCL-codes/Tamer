package ao.chess.v2.state;

import ao.chess.v2.data.Location;
import ao.chess.v2.piece.Colour;
import ao.chess.v2.piece.Piece;
import java.util.Random;

/**
 * User: aostrovsky
 * Date: 7-Oct-2009
 * Time: 7:27:25 PM
 *
 */
public class Zobrist {

    private Zobrist() {
    }

    private static final long[][] PIECES = new long[Piece.VALUES.length][Location.COUNT];

    private static final long[] EN_PASSANTS = new long[Location.FILES];

    private static final long[][] CASTLES = new long[Colour.VALUES.length][CastleType.VALUES.length];

    private static final long WHITE_NEXT;

    private static final long[] REV_MOVES = new long[Byte.MAX_VALUE];

    static {
        Random rand = new Random(420);
        WHITE_NEXT = rand.nextLong();
        for (Piece piece : Piece.VALUES) {
            populateRandomly(PIECES[piece.ordinal()], rand);
        }
        populateRandomly(EN_PASSANTS, rand);
        for (Colour colour : Colour.VALUES) {
            populateRandomly(CASTLES[colour.ordinal()], rand);
        }
        populateRandomly(REV_MOVES, rand);
    }

    private static void populateRandomly(long[] values, Random random) {
        for (int i = 0; i < values.length; i++) {
            values[i] = random.nextLong();
        }
    }

    public static long togglePiece(long zobrist, Piece piece, int locationIndex) {
        return zobrist ^ PIECES[piece.ordinal()][locationIndex];
    }

    public static long toggleEnPassant(long zobrist, byte enPassantFile) {
        return zobrist ^ EN_PASSANTS[enPassantFile];
    }

    public static long toggleCastle(long zobrist, Colour forSide, CastleType castle) {
        return zobrist ^ CASTLES[forSide.ordinal()][castle.ordinal()];
    }

    public static long toggleWhiteToAct(long zobrist) {
        return zobrist ^ WHITE_NEXT;
    }

    public static long toggleReversibleMoves(long zobrist, byte reversibleMoves) {
        return zobrist ^ REV_MOVES[reversibleMoves];
    }
}
