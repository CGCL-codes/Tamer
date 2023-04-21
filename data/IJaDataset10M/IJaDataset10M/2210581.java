package de.jardas.ictk.boardgame.chess.io;

import de.jardas.ictk.boardgame.chess.AmbiguousChessMoveException;
import de.jardas.ictk.boardgame.chess.ChessBoard;
import de.jardas.ictk.boardgame.chess.ChessMove;
import de.jardas.ictk.boardgame.chess.ChessPiece;
import de.jardas.ictk.boardgame.chess.ChessResult;
import de.jardas.ictk.boardgame.chess.Square;
import de.jardas.ictk.boardgame.chess.io.ChessAnnotation;
import de.jardas.ictk.boardgame.chess.io.ChessMoveNotation;
import de.jardas.ictk.boardgame.chess.io.NAG;
import java.util.regex.*;
import java.util.Locale;
import java.io.IOException;
import de.jardas.ictk.boardgame.Board;
import de.jardas.ictk.boardgame.IllegalMoveException;
import de.jardas.ictk.boardgame.Move;
import de.jardas.ictk.boardgame.Result;
import de.jardas.ictk.boardgame.chess.*;
import de.jardas.ictk.boardgame.io.MoveNotation;
import de.jardas.ictk.util.Log;

/** SAN - Standard Algebraic Notation
 *  
 *  Moves are represented as: Nb7++, gxh8=Q, O-O-O, e4, Rad1#
 */
public class SAN extends ChessMoveNotation {

    public static final long DEBUG = ChessMoveNotation.DEBUG;

    static {
        defaultMovePattern = getLocalePattern(PIECE_SETS[0], FILE_SETS[0], RANK_SETS[0]);
    }

    /** default regex pattern for matching moves */
    protected static final Pattern defaultMovePattern;

    /** to have access to NumericAnnotationGlyphs for parsing */
    protected static final NAG nag = new NAG();

    /** the actual move pattern that is Locale specific */
    protected Pattern movePattern;

    /** should a pawn move return a leading space or not [default=false] */
    protected boolean pawnSpace = false;

    public SAN() {
        this(false);
    }

    public SAN(boolean pawnSpace) {
        this.pawnSpace = pawnSpace;
    }

    /** note: different Locales other than the default (English) should
    *  only be used for local presentation.  The archive standard for SAN
    *  and PGN is English.
    */
    public SAN(Locale loc) {
        this(loc, false);
    }

    public SAN(Locale loc, boolean pawnSpace) {
        super(loc);
        this.pawnSpace = pawnSpace;
    }

    public void setPawnAsSpace(boolean t) {
        pawnSpace = t;
    }

    public boolean isPawnAsSpace() {
        return pawnSpace;
    }

    public boolean setLocale(Locale loc) {
        if (!super.setLocale(loc)) return false;
        String lang = null;
        if (loc == null || "eng".equals(lang = loc.getISO3Language())) movePattern = defaultMovePattern; else movePattern = getLocalePattern(pieceSet, fileSet, rankSet);
        return true;
    }

    /** converts a string into a ChessMove object.  The move will not 
    *  be played on the board and thus will not be verified as legal.
    *
    *  @return null if there was no move in the string
    *  @return null if the Board is not a ChessBoard
    *  @throws AmbiguousChessMoveException if the move is not unique
    *  @throws IllegalMoveException if the move is not legal on the board
    *  @throws IllegalArgumentException if the Board is null
    */
    public Move stringToMove(Board b, String s) throws AmbiguousChessMoveException, IllegalMoveException {
        if (b == null) {
            if (Log.debug) Log.debug(DEBUG, "cannot associate a move with a null ChessBoard");
            throw new IllegalArgumentException("Cannot associate a move with a null ChessBoard");
        }
        if (!(b instanceof ChessBoard)) {
            if (Log.debug) Log.debug(DEBUG, "non ChessBoard send to stringToMove");
            return null;
        }
        ChessBoard board = (ChessBoard) b;
        ChessMove move = null;
        Matcher result = null;
        String rest_of_string = null;
        ChessAnnotation anno = null;
        if (board == null) throw new IllegalArgumentException("can't make move on a null board");
        if (s == null) throw new IllegalArgumentException("can't make a move out of a null string");
        byte piece = ChessPiece.NULL_PIECE, promo = ChessPiece.NULL_PIECE, orig_f = 0, orig_r = 0;
        Square orig = null, dest = null;
        result = movePattern.matcher(s);
        if (result.find()) {
            if (Log.debug) {
                Log.debug(DEBUG, "regex result for: " + s, result);
            }
            if (result.group(1).equals("O-O-O") || result.group(1).equals("0-0-0")) {
                move = new ChessMove(board, ChessMove.CASTLE_QUEENSIDE);
            } else if (result.group(1).equals("O-O") || result.group(1).equals("0-0")) {
                move = new ChessMove(board, ChessMove.CASTLE_KINGSIDE);
            } else {
                if (result.group(2) != null) piece = pieceToNum(result.group(2).charAt(0));
                if (result.group(3) != null) orig_f = fileToNum(result.group(3).charAt(0));
                if (result.group(4) != null) orig_r = rankToNum(result.group(4).charAt(0));
                dest = board.getSquare(fileToNum(result.group(6).charAt(0)), rankToNum(result.group(7).charAt(0)));
                if (orig_f < 1 || orig_r < 1) {
                    if (piece == ChessPiece.NULL_PIECE) piece = Pawn.INDEX;
                    try {
                        orig = board.getOrigin(piece, orig_f, orig_r, dest);
                    } catch (IllegalMoveException e) {
                        e.setMoveString(s);
                        throw e;
                    }
                } else {
                    orig = board.getSquare(orig_f, orig_r);
                }
                if (result.group(8) != null) promo = pieceToNum(result.group(8).charAt(1));
                if (promo == ChessPiece.NULL_PIECE) move = new ChessMove(board, orig, dest); else move = new ChessMove(board, orig, dest, ChessPiece.toChessPiece(promo));
            }
            if (result.end() < s.length()) {
                short[] nags = null;
                nags = nag.stringToNumbers(s.substring(result.end()));
                if (nags != null) {
                    anno = new ChessAnnotation();
                    for (int i = 0; i < nags.length; i++) anno.addNAG(nags[i]);
                }
                move.setAnnotation(anno);
            }
        }
        return move;
    }

    /** converts a string into a result number
    *  the default return value if the string cannot be read is null
    */
    public Result stringToResult(String s) {
        ChessResult result = null;
        if (s.startsWith("1-0")) result = new ChessResult(ChessResult.WHITE_WIN); else if (s.startsWith("0-1")) result = new ChessResult(ChessResult.BLACK_WIN); else if (s.startsWith("1/2-1/2")) result = new ChessResult(ChessResult.DRAW); else if (s.startsWith("*")) result = new ChessResult(ChessResult.UNDECIDED);
        return result;
    }

    /** calls moveToString(false) and thus only returns the move.
    */
    public String moveToString(Move move) {
        return moveToString(move, false);
    }

    /** read the string an convert it into an unverified move
    *  @return null if Move is not a ChessMove
    */
    public String moveToString(Move move, boolean showSuffix) {
        if (!(move instanceof ChessMove)) return null;
        ChessMove m = (ChessMove) move;
        if (m == null) throw new NullPointerException("can't convert null move to string");
        if (Log.debug) Log.debug(DEBUG, "move: " + move + " showSuffix?: " + showSuffix);
        StringBuffer sb = new StringBuffer();
        char piece = pieceToChar(m.getChessPiece());
        int[] coords = m.getCoordinates();
        char take = (m.getCasualty() == null) ? '-' : 'x';
        char promo = (m.getPromotion() == null) ? ' ' : pieceToChar(m.getPromotion());
        if (m.isCastleKingside()) sb.append("O-O"); else if (m.isCastleQueenside()) sb.append("O-O-O"); else {
            if (piece == pieceSet[0]) {
                if (pawnSpace) sb.append(' ');
            } else sb.append(piece);
            if (!m.isRankUnique()) sb.append(fileToChar(coords[0]));
            if (!m.isFileUnique()) sb.append(rankToChar(coords[1]));
            if (m.getCasualty() != null) {
                if (piece == pieceSet[0] && m.isFileUnique() && m.isRankUnique()) sb.append(fileToChar(coords[0]));
                sb.append(take);
            }
            sb.append(fileToChar(coords[2])).append(rankToChar(coords[3]));
            if (promo != ' ') sb.append("=").append(promo);
            if (m.isCheckmate()) sb.append('#'); else if (m.isCheck()) {
                sb.append('+');
            }
        }
        if (showSuffix) {
            if (m.getAnnotation() != null && ((ChessAnnotation) m.getAnnotation()).getSuffix() != 0) sb.append(NAG.numberToString(((ChessAnnotation) m.getAnnotation()).getSuffix()));
        }
        return sb.toString();
    }

    public String moveToString(ChessMove m) {
        return moveToString(m, true);
    }

    /** converts a result number into a string.  By default null is returned
    *  indicating UNDECIDED
    */
    public String resultToString(Result result) {
        ChessResult res = (ChessResult) result;
        String str = null;
        switch(res.getIndex()) {
            case ChessResult.WHITE_WIN:
                str = "1-0";
                break;
            case ChessResult.DRAW:
                str = "1/2-1/2";
                break;
            case ChessResult.BLACK_WIN:
                str = "0-1";
                break;
            case ChessResult.UNDECIDED:
                str = "*";
                break;
        }
        return str;
    }

    /** uses the locale to generate a locale specific regex pattern
    */
    protected static Pattern getLocalePattern(char[] pieceSet, char[] fileSet, char[] rankSet) {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        sb.append("[^0O]*([0O]-[0O]-[0O]").append("|[0O]-[0O]").append("|^([");
        for (i = 0; i < pieceSet.length; i++) sb.append(pieceSet[i]);
        sb.append("])?([");
        for (i = 0; i < fileSet.length; i++) sb.append(fileSet[i]).append(Character.toUpperCase(fileSet[i]));
        sb.append("])?([");
        for (i = 0; i < rankSet.length; i++) sb.append(rankSet[i]);
        sb.append("])?([xX])?([");
        for (i = 0; i < fileSet.length; i++) sb.append(fileSet[i]).append(Character.toUpperCase(fileSet[i]));
        sb.append("])([");
        for (i = 0; i < rankSet.length; i++) sb.append(rankSet[i]);
        sb.append("])").append("(=[");
        for (i = 0; i < pieceSet.length - 1; i++) sb.append(pieceSet[i]);
        sb.append("])?)").append("(\\+)?(\\+)?(#)?");
        return Pattern.compile(sb.toString());
    }
}
