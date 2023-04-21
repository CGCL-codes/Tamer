package echecs.launch;

import java.awt.Color;
import java.awt.Image;

public interface ChessInterface {

    final int PAWN = 1;

    final int KNIGHT = 2;

    final int BISHOP = 3;

    final int ROOK = 4;

    final int QUEEN = 5;

    final int KING = 6;

    final int WHITE = 1;

    static final String[] NomPiecesBlanches = { "P", "C", "F", "T", "Q", "R" };

    static final String[] NomPiecesNoires = { "p", "c", "f", "t", "d", "r" };

    static final int[] deplacementKnight = { 12, -12, +21, -21, +19, -19, +8, -8 };

    static final int[] deplacementBishop = { -9, -11, +9, +11 };

    static final int[] deplacementRook = { -10, 10, -1, 1 };

    static final int[] deplacementKing = { -9, -11, +9, +11, -10, 10, -1, 1 };

    int[] board = new int[120];

    int[] graphboard = new int[120];

    Color dunkel = new Color(0x999999);

    Color hell = new Color(0xFFFFCC);

    Color red = new Color(0xCC0000);

    Color green = new Color(0x009900);

    Color blue = new Color(0x000099);

    Image[] pieces = new Image[18];

    int[] movelist = new int[250];

    float minimax[] = new float[10];

    public static float alphabeta[] = new float[10];

    void enter(String fenPosition);

    void execute(int start, int end);

    float evaluation();

    boolean ischeck();

    void genmove();

    boolean ishHumanMoveValid(int move);

    void multisimulize(int start, int inc);

    void simulize(int start, int end);

    void alphabeta(int start, int end);
}
