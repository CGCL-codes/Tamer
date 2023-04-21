package ch.nostromo.tiffanys.game.rules.pieces;

import ch.nostromo.lib.util.NosTools;
import ch.nostromo.tiffanys.game.board.Board;
import ch.nostromo.tiffanys.game.board.BoardConstants;

public abstract class ChessPiece {

    public static int checkDirection(Board board, int myPosition, int owner, int direction) {
        int result = 0;
        boolean exit = false;
        while (!exit) {
            int toField = myPosition + ((result + 1) * direction);
            boolean isEnemyPiece = board.isOpponentField(toField, owner);
            if (!board.isVoidField(toField) && (board.isEmptyFieldOnIndex(toField) || isEnemyPiece)) {
                result++;
                if (isEnemyPiece) exit = true;
            } else {
                exit = true;
            }
        }
        return result;
    }

    public static boolean isKingAttacked(Board board, int owner) {
        int kingPos = board.getKingField(owner);
        return isKingAttacked(board, kingPos, owner);
    }

    public static boolean isKingAttacked(Board board, int kingPos, int owner) {
        int enemyColor = invertOwner(owner);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + 19], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos + 19], enemyColor)) return true;
        if (NosTools.checkMask(board.getBoardArray()[kingPos + 21], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos + 21], enemyColor)) return true;
        if (NosTools.checkMask(board.getBoardArray()[kingPos + 12], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos + 12], enemyColor)) return true;
        if (NosTools.checkMask(board.getBoardArray()[kingPos - 8], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos - 8], enemyColor)) return true;
        if (NosTools.checkMask(board.getBoardArray()[kingPos - 19], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos - 19], enemyColor)) return true;
        if (NosTools.checkMask(board.getBoardArray()[kingPos - 21], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos - 21], enemyColor)) return true;
        if (NosTools.checkMask(board.getBoardArray()[kingPos - 12], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos - 12], enemyColor)) return true;
        if (NosTools.checkMask(board.getBoardArray()[kingPos + 8], BoardConstants.PIECE_KNIGHT) && NosTools.checkMask(board.getBoardArray()[kingPos + 8], enemyColor)) return true;
        int dcount = 0;
        int direction = 11;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_BISHOP) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        direction = -11;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_BISHOP) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        direction = 9;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_BISHOP) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        direction = -9;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_BISHOP) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        direction = 1;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_ROOK) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        direction = -1;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_ROOK) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        direction = 10;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_ROOK) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        direction = -10;
        dcount = checkDirection(board, kingPos, owner, direction);
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], enemyColor) && ((NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_ROOK) || (NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_QUEEN))))) return true;
        if (dcount == 1 && NosTools.checkMask(board.getBoardArray()[kingPos + (dcount * direction)], BoardConstants.PIECE_KING)) return true;
        int pawnDirection = 1;
        if (owner == BoardConstants.OWNER_BLACK) pawnDirection = -1;
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (11 * pawnDirection)], BoardConstants.PIECE_PAWN) && NosTools.checkMask(board.getBoardArray()[kingPos + (11 * pawnDirection)], enemyColor)) {
            return true;
        }
        if (NosTools.checkMask(board.getBoardArray()[kingPos + (9 * pawnDirection)], BoardConstants.PIECE_PAWN) && NosTools.checkMask(board.getBoardArray()[kingPos + (9 * pawnDirection)], enemyColor)) {
            return true;
        }
        return false;
    }

    private static int invertOwner(int owner) throws IllegalArgumentException {
        if (owner == BoardConstants.OWNER_WHITE) {
            return BoardConstants.OWNER_BLACK;
        } else if (owner == BoardConstants.OWNER_BLACK) {
            return BoardConstants.OWNER_WHITE;
        }
        throw new IllegalArgumentException("Unknown owner: " + owner);
    }

    protected static String fieldToCoord(int field) {
        String result = "";
        int tmp = (field / 10);
        tmp = tmp * 10;
        if (field - tmp == 1) {
            result += "a";
        } else if (field - tmp == 2) {
            result += "b";
        } else if (field - tmp == 3) {
            result += "c";
        } else if (field - tmp == 4) {
            result += "d";
        } else if (field - tmp == 5) {
            result += "e";
        } else if (field - tmp == 6) {
            result += "f";
        } else if (field - tmp == 7) {
            result += "g";
        } else if (field - tmp == 8) {
            result += "h";
        }
        if (field > 20 && field < 29) {
            result += "1";
        } else if (field > 30 && field < 39) {
            result += "2";
        } else if (field > 40 && field < 49) {
            result += "3";
        } else if (field > 50 && field < 59) {
            result += "4";
        } else if (field > 60 && field < 69) {
            result += "5";
        } else if (field > 70 && field < 79) {
            result += "6";
        } else if (field > 80 && field < 89) {
            result += "7";
        } else if (field > 90 && field < 99) {
            result += "8";
        }
        return result;
    }
}
