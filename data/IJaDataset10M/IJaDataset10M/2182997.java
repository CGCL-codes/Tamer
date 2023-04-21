package free.chess;

/**
 * An implementation of WildVariant for the game of chess (classic).
 */
public final class Chess extends ChesslikeGenericVariant {

    /**
   * The lexigraphic representation of the initial position in chess.
   */
    public static final String INITIAL_POSITION_LEXIGRAPHIC = "rnbqkbnrpppppppp--------------------------------PPPPPPPPRNBQKBNR";

    /**
   * The FEN representation of the initial position in chess.
   */
    public static final String INITIAL_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
   * The sole instance of this class.
   */
    private static Chess instance = new Chess();

    /**
   * Returns an instance of Chess.
   */
    public static Chess getInstance() {
        return instance;
    }

    /**
   * Creates a new instance of Chess.
   */
    private Chess() {
        super(INITIAL_POSITION_FEN, "Chess");
    }
}
