package prgechecsimpla;

import java.util.*;
import prgechecsmodel.IBoard;
import prgechecsmodel.IDefinition;
import prgechecsmodel.IMove;

final class HistoryData {

    Move m;

    int capture;

    int castle;

    int ep;

    int fifty;
}

public final class Board implements IBoard, IDefinition {

    public int[] boardArray;

    public int[] boardArrayUnique;

    public int toMove;

    public int enPassant;

    public int white_castle;

    public int black_castle;

    public int movesFifty;

    public int movesFull;

    public int[] history;

    public int[] captureHistory;

    public long[] zobristHistory;

    public long[] pawnZobristHistory;

    public int historyIndex;

    public long zobristKey;

    public long pawnZobristKey;

    public PieceList w_pawns;

    public PieceList b_pawns;

    public PieceList w_knights;

    public PieceList b_knights;

    public PieceList w_bishops;

    public PieceList b_bishops;

    public PieceList w_rooks;

    public PieceList b_rooks;

    public PieceList w_queens;

    public PieceList b_queens;

    public PieceList w_king;

    public PieceList b_king;

    int side = LIGHT;

    int xside = DARK;

    int castle = 15;

    int ep = -1;

    int fifty = 0;

    int hply = 0;

    HistoryData histDat[] = new HistoryData[HIST_STACK];

    public class PieceList {

        public int[] pieces;

        public int count;

        public PieceList() {
            this.pieces = new int[10];
            this.count = 0;
        }

        public void addPiece(int boardIndex) {
            boardArrayUnique[boardIndex] = count;
            pieces[count] = boardIndex;
            count++;
        }
    }

    public Board() {
        this.boardArray = new int[128];
        this.boardArrayUnique = new int[128];
        this.toMove = 1;
        this.enPassant = -1;
        this.white_castle = CASTLE_NONE;
        this.black_castle = CASTLE_NONE;
        this.w_pawns = new PieceList();
        this.b_pawns = new PieceList();
        this.w_knights = new PieceList();
        this.b_knights = new PieceList();
        this.w_bishops = new PieceList();
        this.b_bishops = new PieceList();
        this.w_rooks = new PieceList();
        this.b_rooks = new PieceList();
        this.w_queens = new PieceList();
        this.b_queens = new PieceList();
        this.w_king = new PieceList();
        this.b_king = new PieceList();
        this.history = new int[4096];
        this.captureHistory = new int[4096];
        this.zobristHistory = new long[4096];
        this.pawnZobristHistory = new long[4096];
        this.zobristKey = 0;
        this.pawnZobristKey = 0;
    }

    @Override
    public int getColor(int i, int j) {
        return color[(i << 3) + j];
    }

    @Override
    public int getPiece(int i, int j) {
        return piece[(i << 3) + j];
    }

    @Override
    public boolean isWhiteToMove() {
        return (side == LIGHT);
    }

    @Override
    public boolean inCheck(int s) {
        int i;
        for (i = 0; i < 64; ++i) {
            if (piece[i] == KING && color[i] == s) {
                return attack(i, s ^ 1);
            }
        }
        return true;
    }

    boolean attack(int sq, int s) {
        int i, j, n;
        for (i = 0; i < 64; ++i) {
            if (color[i] == s) {
                int p = piece[i];
                if (p == PAWN) {
                    if (s == LIGHT) {
                        if (COL(i) != 0 && i - 9 == sq) {
                            return true;
                        }
                        if (COL(i) != 7 && i - 7 == sq) {
                            return true;
                        }
                    } else {
                        if (COL(i) != 0 && i + 7 == sq) {
                            return true;
                        }
                        if (COL(i) != 7 && i + 9 == sq) {
                            return true;
                        }
                    }
                } else {
                    for (j = 0; j < offsets[p]; ++j) {
                        for (n = i; ; ) {
                            n = mailbox[mailbox64[n] + offset[p][j]];
                            if (n == -1) {
                                break;
                            }
                            if (n == sq) {
                                return true;
                            }
                            if (color[n] != EMPTY) {
                                break;
                            }
                            if (!slide[p]) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public TreeSet<IMove> gen() {
        TreeSet<IMove> ret = new TreeSet<IMove>();
        for (int i = 0; i < 64; ++i) {
            if (color[i] == side) {
                if (piece[i] == PAWN) {
                    if (side == LIGHT) {
                        if (COL(i) != 0 && color[i - 9] == DARK) {
                            genPush(ret, i, i - 9, 17);
                        }
                        if (COL(i) != 7 && color[i - 7] == DARK) {
                            genPush(ret, i, i - 7, 17);
                        }
                        if (color[i - 8] == EMPTY) {
                            genPush(ret, i, i - 8, 16);
                            if (i >= 48 && color[i - 16] == EMPTY) {
                                genPush(ret, i, i - 16, 24);
                            }
                        }
                    } else {
                        if (COL(i) != 0 && color[i + 7] == LIGHT) {
                            genPush(ret, i, i + 7, 17);
                        }
                        if (COL(i) != 7 && color[i + 9] == LIGHT) {
                            genPush(ret, i, i + 9, 17);
                        }
                        if (color[i + 8] == EMPTY) {
                            genPush(ret, i, i + 8, 16);
                            if (i <= 15 && color[i + 16] == EMPTY) {
                                genPush(ret, i, i + 16, 24);
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < offsets[piece[i]]; ++j) {
                        for (int n = i; ; ) {
                            n = mailbox[mailbox64[n] + offset[piece[i]][j]];
                            if (n == -1) {
                                break;
                            }
                            if (color[n] != EMPTY) {
                                if (color[n] == xside) {
                                    genPush(ret, i, n, 1);
                                }
                                break;
                            }
                            genPush(ret, i, n, 0);
                            if (!slide[piece[i]]) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (side == LIGHT) {
            if ((castle & 1) != 0) {
                genPush(ret, E1, G1, 2);
            }
            if ((castle & 2) != 0) {
                genPush(ret, E1, C1, 2);
            }
        } else {
            if ((castle & 4) != 0) {
                genPush(ret, E8, G8, 2);
            }
            if ((castle & 8) != 0) {
                genPush(ret, E8, C8, 2);
            }
        }
        if (ep != -1) {
            if (side == LIGHT) {
                if (COL(ep) != 0 && color[ep + 7] == LIGHT && piece[ep + 7] == PAWN) {
                    genPush(ret, ep + 7, ep, 21);
                }
                if (COL(ep) != 7 && color[ep + 9] == LIGHT && piece[ep + 9] == PAWN) {
                    genPush(ret, ep + 9, ep, 21);
                }
            } else {
                if (COL(ep) != 0 && color[ep - 9] == DARK && piece[ep - 9] == PAWN) {
                    genPush(ret, ep - 9, ep, 21);
                }
                if (COL(ep) != 7 && color[ep - 7] == DARK && piece[ep - 7] == PAWN) {
                    genPush(ret, ep - 7, ep, 21);
                }
            }
        }
        return ret;
    }

    @Override
    public TreeSet<IMove> genCaps() {
        TreeSet<IMove> ret = new TreeSet<IMove>();
        for (int i = 0; i < 64; ++i) {
            if (color[i] == side) {
                if (piece[i] == PAWN) {
                    if (side == LIGHT) {
                        if (COL(i) != 0 && color[i - 9] == DARK) {
                            genPush(ret, i, i - 9, 17);
                        }
                        if (COL(i) != 7 && color[i - 7] == DARK) {
                            genPush(ret, i, i - 7, 17);
                        }
                        if (i <= 15 && color[i - 8] == EMPTY) {
                            genPush(ret, i, i - 8, 16);
                        }
                    }
                    if (side == DARK) {
                        if (COL(i) != 0 && color[i + 7] == LIGHT) {
                            genPush(ret, i, i + 7, 17);
                        }
                        if (COL(i) != 7 && color[i + 9] == LIGHT) {
                            genPush(ret, i, i + 9, 17);
                        }
                        if (i >= 48 && color[i + 8] == EMPTY) {
                            genPush(ret, i, i + 8, 16);
                        }
                    }
                } else {
                    for (int j = 0; j < offsets[piece[i]]; ++j) {
                        for (int n = i; ; ) {
                            n = mailbox[mailbox64[n] + offset[piece[i]][j]];
                            if (n == -1) {
                                break;
                            }
                            if (color[n] != EMPTY) {
                                if (color[n] == xside) {
                                    genPush(ret, i, n, 1);
                                }
                                break;
                            }
                            if (!slide[piece[i]]) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (ep != -1) {
            if (side == LIGHT) {
                if (COL(ep) != 0 && color[ep + 7] == LIGHT && piece[ep + 7] == PAWN) {
                    genPush(ret, ep + 7, ep, 21);
                }
                if (COL(ep) != 7 && color[ep + 9] == LIGHT && piece[ep + 9] == PAWN) {
                    genPush(ret, ep + 9, ep, 21);
                }
            } else {
                if (COL(ep) != 0 && color[ep - 9] == DARK && piece[ep - 9] == PAWN) {
                    genPush(ret, ep - 9, ep, 21);
                }
                if (COL(ep) != 7 && color[ep - 7] == DARK && piece[ep - 7] == PAWN) {
                    genPush(ret, ep - 7, ep, 21);
                }
            }
        }
        return ret;
    }

    @Override
    public void genPush(TreeSet<IMove> ret, int from, int to, int bits) {
        if ((bits & 16) != 0) {
            if (side == LIGHT) {
                if (to <= H8) {
                    genPromote(ret, from, to, bits);
                    return;
                }
            } else {
                if (to >= A1) {
                    genPromote(ret, from, to, bits);
                    return;
                }
            }
        }
        IMove g = new Move(from, to, 0, bits);
        ret.add(g);
    }

    @Override
    public void genPromote(TreeSet<IMove> ret, int from, int to, int bits) {
        for (char i = KNIGHT; i <= QUEEN; ++i) {
            Move g = new Move(from, to, i, (bits | 32));
            g.setScore(1000000 + (i * 10));
            ret.add(g);
        }
    }

    boolean makeMove(Move m) {
        if ((m.bits & 2) != 0) {
            int from, to;
            if (inCheck(side)) {
                return false;
            }
            switch(m.to) {
                case 62:
                    if (color[F1] != EMPTY || color[G1] != EMPTY || attack(F1, xside) || attack(G1, xside)) {
                        return false;
                    }
                    from = H1;
                    to = F1;
                    break;
                case 58:
                    if (color[B1] != EMPTY || color[C1] != EMPTY || color[D1] != EMPTY || attack(C1, xside) || attack(D1, xside)) {
                        return false;
                    }
                    from = A1;
                    to = D1;
                    break;
                case 6:
                    if (color[F8] != EMPTY || color[G8] != EMPTY || attack(F8, xside) || attack(G8, xside)) {
                        return false;
                    }
                    from = H8;
                    to = F8;
                    break;
                case 2:
                    if (color[B8] != EMPTY || color[C8] != EMPTY || color[D8] != EMPTY || attack(C8, xside) || attack(D8, xside)) {
                        return false;
                    }
                    from = A8;
                    to = D8;
                    break;
                default:
                    from = -1;
                    to = -1;
                    break;
            }
            color[to] = color[from];
            piece[to] = piece[from];
            color[from] = EMPTY;
            piece[from] = EMPTY;
        }
        histDat[hply] = new HistoryData();
        histDat[hply].m = m;
        histDat[hply].capture = piece[m.to];
        histDat[hply].castle = castle;
        histDat[hply].ep = ep;
        histDat[hply].fifty = fifty;
        ++hply;
        castle &= castleMask[m.from] & castleMask[m.to];
        if ((m.bits & 8) != 0) {
            if (side == LIGHT) {
                ep = m.to + 8;
            } else {
                ep = m.to - 8;
            }
        } else {
            ep = -1;
        }
        if ((m.bits & 17) != 0) {
            fifty = 0;
        } else {
            ++fifty;
        }
        color[m.to] = side;
        if ((m.bits & 32) != 0) {
            piece[m.to] = m.promote;
        } else {
            piece[m.to] = piece[m.from];
        }
        color[m.from] = EMPTY;
        piece[m.from] = EMPTY;
        if ((m.bits & 4) != 0) {
            if (side == LIGHT) {
                color[m.to + 8] = EMPTY;
                piece[m.to + 8] = EMPTY;
            } else {
                color[m.to - 8] = EMPTY;
                piece[m.to - 8] = EMPTY;
            }
        }
        side ^= 1;
        xside ^= 1;
        if (inCheck(xside)) {
            takeBack();
            return false;
        }
        return true;
    }

    @Override
    public void takeBack() {
        side ^= 1;
        xside ^= 1;
        --hply;
        Move m = histDat[hply].m;
        castle = histDat[hply].castle;
        ep = histDat[hply].ep;
        fifty = histDat[hply].fifty;
        color[m.from] = side;
        if ((m.bits & 32) != 0) {
            piece[m.from] = PAWN;
        } else {
            piece[m.from] = piece[m.to];
        }
        if (histDat[hply].capture == EMPTY) {
            color[m.to] = EMPTY;
            piece[m.to] = EMPTY;
        } else {
            color[m.to] = xside;
            piece[m.to] = histDat[hply].capture;
        }
        if ((m.bits & 2) != 0) {
            int from, to;
            switch(m.to) {
                case 62:
                    from = F1;
                    to = H1;
                    break;
                case 58:
                    from = D1;
                    to = A1;
                    break;
                case 6:
                    from = F8;
                    to = H8;
                    break;
                case 2:
                    from = D8;
                    to = A8;
                    break;
                default:
                    from = -1;
                    to = -1;
                    break;
            }
            color[to] = side;
            piece[to] = ROOK;
            color[from] = EMPTY;
            piece[from] = EMPTY;
        }
        if ((m.bits & 4) != 0) {
            if (side == LIGHT) {
                color[m.to + 8] = xside;
                piece[m.to + 8] = PAWN;
            } else {
                color[m.to - 8] = xside;
                piece[m.to - 8] = PAWN;
            }
        }
    }

    @Override
    public String toString() {
        int i;
        StringBuffer sb = new StringBuffer("\n8 ");
        for (i = 0; i < 64; ++i) {
            switch(color[i]) {
                case EMPTY:
                    sb.append(" .");
                    break;
                case LIGHT:
                    sb.append(" ");
                    sb.append(pieceChar[piece[i]]);
                    break;
                case DARK:
                    sb.append(" ");
                    sb.append((char) (pieceChar[piece[i]] + ('a' - 'A')));
                    break;
                default:
                    throw new IllegalStateException("Square not EMPTY, LIGHT or DARK: " + i);
            }
            if ((i + 1) % 8 == 0 && i != 63) {
                sb.append("\n");
                sb.append(Integer.toString(7 - ROW(i)));
                sb.append(" ");
            }
        }
        sb.append("\n\n   a b c d e f g h\n\n");
        return sb.toString();
    }

    @Override
    public int reps() {
        int b[] = new int[64];
        int c = 0;
        int r = 0;
        if (fifty <= 3) {
            return 0;
        }
        for (int i = hply - 1; i >= hply - fifty - 1; --i) {
            if (++b[histDat[i].m.from] == 0) {
                --c;
            } else {
                ++c;
            }
            if (--b[histDat[i].m.to] == 0) {
                --c;
            } else {
                ++c;
            }
            if (c == 0) {
                ++r;
            }
        }
        return r;
    }

    static int COL(int x) {
        return (x & 7);
    }

    static int ROW(int x) {
        return (x >> 3);
    }

    @Override
    public int distance(int squareA, int squareB) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int gen_allLegalMoves(TreeSet<IMove> moves, int startIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int gen_allLegalMoves(int[] moves, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean validate(int move) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAttacked(int attacked, int side) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean makeMove(IMove m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void makeMove(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int rank(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int col(int x) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setupStart() {
    }

    @Override
    public void unmakeMove(int move) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getFen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Takes a FEN-string and sets the board accordingly
     *
     * @param String
     *            fen
     */
    @Override
    public final void inputFen(String fen) {
        String trimmedFen = fen.trim();
        boardArray = new int[128];
        for (int i = 0; i < 128; i++) {
            boardArrayUnique[i] = -1;
        }
        this.w_pawns = new PieceList();
        this.b_pawns = new PieceList();
        this.w_knights = new PieceList();
        this.b_knights = new PieceList();
        this.w_bishops = new PieceList();
        this.b_bishops = new PieceList();
        this.w_rooks = new PieceList();
        this.b_rooks = new PieceList();
        this.w_queens = new PieceList();
        this.b_queens = new PieceList();
        this.w_king = new PieceList();
        this.b_king = new PieceList();
        String currentChar;
        int i = 0;
        int boardIndex = 112;
        int currentStep = 0;
        white_castle = CASTLE_NONE;
        black_castle = CASTLE_NONE;
        boolean fenFinished = false;
        while (!fenFinished && i < trimmedFen.length()) {
            currentChar = trimmedFen.substring(i, i + 1);
            if (" ".equals(currentChar)) {
                i++;
                currentChar = trimmedFen.substring(i, i + 1);
                currentStep++;
            }
            switch(currentStep) {
                case 0:
                    {
                        switch(currentChar.charAt(0)) {
                            case '/':
                                boardIndex -= 24;
                                break;
                            case 'K':
                                boardArray[boardIndex] = W_KING;
                                w_king.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'Q':
                                boardArray[boardIndex] = W_QUEEN;
                                w_queens.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'R':
                                boardArray[boardIndex] = W_ROOK;
                                w_rooks.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'B':
                                boardArray[boardIndex] = W_BISHOP;
                                w_bishops.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'N':
                                boardArray[boardIndex] = W_KNIGHT;
                                w_knights.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'P':
                                boardArray[boardIndex] = W_PAWN;
                                w_pawns.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'k':
                                boardArray[boardIndex] = B_KING;
                                b_king.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'q':
                                boardArray[boardIndex] = B_QUEEN;
                                b_queens.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'r':
                                boardArray[boardIndex] = B_ROOK;
                                b_rooks.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'b':
                                boardArray[boardIndex] = B_BISHOP;
                                b_bishops.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'n':
                                boardArray[boardIndex] = B_KNIGHT;
                                b_knights.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            case 'p':
                                boardArray[boardIndex] = B_PAWN;
                                b_pawns.addPiece(boardIndex);
                                boardIndex++;
                                break;
                            default:
                                boardIndex += Integer.parseInt(currentChar);
                        }
                        break;
                    }
                case 1:
                    {
                        if ("w".equals(currentChar)) {
                            toMove = WHITE_TO_MOVE;
                        } else {
                            toMove = BLACK_TO_MOVE;
                        }
                        break;
                    }
                case 2:
                    {
                        switch(currentChar.charAt(0)) {
                            case 'K':
                                white_castle = CASTLE_SHORT;
                                break;
                            case 'Q':
                                {
                                    if (white_castle == CASTLE_SHORT) {
                                        white_castle = CASTLE_BOTH;
                                    } else {
                                        white_castle = CASTLE_LONG;
                                    }
                                    break;
                                }
                            case 'k':
                                black_castle = CASTLE_SHORT;
                                break;
                            case 'q':
                                {
                                    if (black_castle == CASTLE_SHORT) {
                                        black_castle = CASTLE_BOTH;
                                    } else {
                                        black_castle = CASTLE_LONG;
                                    }
                                    break;
                                }
                        }
                        break;
                    }
                case 3:
                    {
                        if ("-".equals(currentChar)) {
                            enPassant = -1;
                        } else {
                            switch(currentChar.charAt(0)) {
                                case 'a':
                                    enPassant = 0;
                                    break;
                                case 'b':
                                    enPassant = 1;
                                    break;
                                case 'c':
                                    enPassant = 2;
                                    break;
                                case 'd':
                                    enPassant = 3;
                                    break;
                                case 'e':
                                    enPassant = 4;
                                    break;
                                case 'f':
                                    enPassant = 5;
                                    break;
                                case 'g':
                                    enPassant = 6;
                                    break;
                                case 'h':
                                    enPassant = 7;
                                    break;
                            }
                            i++;
                            currentChar = trimmedFen.substring(i, i + 1);
                            if ("3".equals(currentChar)) {
                                enPassant += 32;
                            } else {
                                enPassant += 80;
                            }
                        }
                        break;
                    }
                case 4:
                    {
                        if (" ".equals(trimmedFen.substring(i + 1, i + 2))) {
                            movesFifty = Integer.parseInt(currentChar);
                        } else {
                            movesFifty = Integer.parseInt(trimmedFen.substring(i, i + 2));
                            i++;
                        }
                        i += 2;
                        movesFull = Integer.parseInt(trimmedFen.substring(i));
                        fenFinished = true;
                        break;
                    }
            }
            i++;
        }
    }
}
