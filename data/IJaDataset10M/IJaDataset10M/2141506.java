package ictk.boardgame.chess.io;

import java.util.Locale;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;
import ictk.util.Log;
import ictk.boardgame.Board;
import ictk.boardgame.chess.*;

/** FEN is an abstract class that parses strings to
 *  produce Board objects.
 */
public class FEN implements ChessBoardNotation {

    public static long DEBUG = Log.BoardNotation;

    /** for translation from PNBRQK to Pieces */
    protected static SAN san = new SAN();

    Locale locale;

    public FEN() {
    }

    /** NOT IMPLEMENTED YET
    */
    public void setLocale(Locale loc) {
    }

    /** NOT IMPLEMENTED YET
    */
    public Locale getLocale() {
        return locale;
    }

    /** converts strings into ChessBoard objects.
    *
    * @throws IOException for the normal reasons
    */
    public Board stringToBoard(String str) throws IOException {
        ChessBoard board = null;
        char[][] matrix = new char[ChessBoard.MAX_FILE][ChessBoard.MAX_RANK];
        char[] strArray = null;
        int rank = ChessBoard.MAX_RANK - 1;
        int file = 0;
        boolean isBlackMove = false;
        boolean canWhiteCastleKingside = false, canWhiteCastleQueenside = false, canBlackCastleKingside = false, canBlackCastleQueenside = false;
        char enpassantFile = '-';
        int plyCount = 0, moveNumber = 1;
        str.trim();
        strArray = str.toCharArray();
        int i;
        for (i = 0; i < strArray.length; i++) {
            if (strArray[i] == '/') {
                rank--;
                file = 0;
            } else if (Character.isDigit(strArray[i])) {
                file += Character.digit(strArray[i], 10);
            } else if (isPieceChar(strArray[i])) {
                matrix[file++][rank] = strArray[i];
            } else if (strArray[i] == ' ') {
                break;
            } else {
                throw new IOException("Unsupported character found in FEN at:" + i);
            }
        }
        i++;
        if (strArray[i] == 'w') isBlackMove = false; else if (strArray[i] == 'b') isBlackMove = true; else throw new IOException("Unsupported character found in FEN at:" + i + "(" + strArray[i] + ") expecting who to move");
        i++;
        i++;
        for (; i < strArray.length && strArray[i] != ' '; i++) {
            switch(strArray[i]) {
                case 'K':
                    canWhiteCastleKingside = true;
                    break;
                case 'Q':
                    canWhiteCastleQueenside = true;
                    break;
                case 'k':
                    canBlackCastleKingside = true;
                    break;
                case 'q':
                    canBlackCastleQueenside = true;
                    break;
            }
        }
        i++;
        if (strArray[i] != '-') {
            enpassantFile = strArray[i];
            i++;
        }
        i++;
        i++;
        int j;
        String strPly = str.substring(i, j = str.indexOf(" ", i));
        try {
            plyCount = Integer.parseInt(strPly);
        } catch (NumberFormatException e) {
            throw new IOException("Unsupported character found in FEN at:" + i + " (" + strPly + ") expecting ply count");
        }
        i = j;
        i++;
        String strMoves = str.substring(i, str.length());
        try {
            moveNumber = Integer.parseInt(strMoves);
        } catch (NumberFormatException e) {
            throw new IOException("Unsupported character found in FEN at:" + i + " (" + strMoves + ") expecting move number");
        }
        board = new ChessBoard(matrix, isBlackMove, canWhiteCastleKingside, canWhiteCastleQueenside, canBlackCastleKingside, canBlackCastleQueenside, enpassantFile, plyCount, moveNumber);
        return board;
    }

    /** converts board objects into string format
    */
    public String boardToString(Board b) {
        ChessBoard board = (ChessBoard) b;
        char[][] ray = board.toCharArray();
        StringBuffer buff = new StringBuffer();
        int count = 0;
        for (int r = ChessBoard.MAX_RANK - 1; r >= 0; r--) {
            if (r != ChessBoard.MAX_RANK - 1) buff.append("/");
            count = 0;
            for (int f = 0; f < ChessBoard.MAX_FILE; f++) {
                if (Character.isLetter(ray[f][r])) {
                    if (count > 0) {
                        buff.append(count);
                        count = 0;
                    }
                    buff.append(ray[f][r]);
                } else count++;
            }
            if (count > 0) buff.append(count);
        }
        buff.append(" ");
        buff.append(((board.isBlackMove()) ? 'b' : 'w'));
        buff.append(" ");
        boolean castle = false;
        if (board.isWhiteCastleableKingside()) {
            castle = true;
            buff.append("K");
        }
        if (board.isWhiteCastleableQueenside()) {
            castle = true;
            buff.append("Q");
        }
        if (board.isBlackCastleableKingside()) {
            castle = true;
            buff.append("k");
        }
        if (board.isBlackCastleableQueenside()) {
            castle = true;
            buff.append("q");
        }
        if (!castle) buff.append("-");
        buff.append(" ");
        if (board.getEnPassantFile() != ChessBoard.NO_ENPASSANT) {
            buff.append(san.fileToChar(board.getEnPassantFile()));
            if (board.isBlackMove()) buff.append("3"); else buff.append("6");
        } else buff.append("-");
        buff.append(" ");
        buff.append(board.get50MoveRulePlyCount());
        buff.append(" ");
        buff.append(board.getCurrentMoveNumber());
        return buff.toString();
    }

    /** FEN is not Locale specifc yet.
     * @return true if the character entered is a legal piece in FEN
    */
    protected boolean isPieceChar(char c) {
        switch(c) {
            case 'r':
            case 'n':
            case 'b':
            case 'q':
            case 'k':
            case 'p':
            case 'R':
            case 'N':
            case 'B':
            case 'Q':
            case 'K':
            case 'P':
                return true;
            default:
                return false;
        }
    }
}
