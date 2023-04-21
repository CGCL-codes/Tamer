package ch.nostromo.tiffanys.game.move;

import ch.nostromo.tiffanys.game.board.Board;
import ch.nostromo.tiffanys.game.board.BoardTools;
import ch.nostromo.tiffanys.game.chess.Chess;
import ch.nostromo.tiffanys.game.game.GameTools;

public class MoveTools {

    public static Move[] alterMoves(MoveInput[] moves, Board board, int colorToMove) {
        Move[] result = new Move[moves.length];
        for (int i = 0; i < moves.length; i++) {
            result[i] = alterMove(moves[i], board, colorToMove);
        }
        return result;
    }

    public static Move alterMove(MoveInput move, Board board, int colorToMove) {
        String from = move.getFrom();
        String to = move.getTo();
        int promotionPiece = move.getPromotion();
        int startingPiece = BoardTools.getPiece(board, from);
        boolean isShortCastling = false;
        boolean isLongCastling = false;
        if (startingPiece == Board.PIECE_KING) {
            int iFrom = BoardTools.coordToField(from);
            int iTo = BoardTools.coordToField(to);
            isShortCastling = ((iFrom == 25 && iTo == 27) || (iFrom == 95 && iTo == 97));
            isLongCastling = ((iFrom == 25 && iTo == 23) || (iFrom == 95 && iTo == 93));
        }
        boolean isScored = move.isScored();
        int score = move.getScore();
        MoveInput[] origScoreTree = move.getScoreTree();
        Move[] scoreTree = null;
        if (origScoreTree != null) {
            scoreTree = new Move[origScoreTree.length];
            Board playBoard = board.clone();
            int playColorToMove = colorToMove;
            for (int i = origScoreTree.length - 1; i >= 0; i--) {
                if (origScoreTree[i] != null) {
                    Move toAdd = alterMove(origScoreTree[i], playBoard, playColorToMove);
                    playBoard.applyMove(toAdd);
                    scoreTree[i] = toAdd;
                    playColorToMove = GameTools.switchColor(playColorToMove);
                } else {
                    break;
                }
            }
        }
        boolean isStrikeMove = !BoardTools.isEmptyField(board, to);
        Board newBoard = board.clone();
        newBoard.applyMove(move);
        boolean checks = GameTools.isInCheck(newBoard, GameTools.switchColor(colorToMove));
        boolean mates = GameTools.isMate(newBoard, GameTools.switchColor(colorToMove));
        Move result = new Move(from, to, startingPiece, promotionPiece, isScored, score, scoreTree, isShortCastling, isLongCastling, isStrikeMove, checks, mates);
        result.setSanDescription(getSanDescription(result, board, colorToMove));
        return result;
    }

    public static Move alterCheckSanMove(String move, Move[] legalMoves) {
        if (move.startsWith("O-O-O")) {
            for (int i = 0; i < legalMoves.length; i++) {
                if (legalMoves[i].isCastlingLong()) {
                    return legalMoves[i];
                }
            }
            throw new IllegalArgumentException("Castling move not found");
        } else if (move.startsWith("O-O")) {
            for (int i = 0; i < legalMoves.length; i++) {
                if (legalMoves[i].isCastlingShort()) {
                    return legalMoves[i];
                }
            }
            throw new IllegalArgumentException("Castling move not found");
        } else {
            Move legalMoveFound = null;
            int rest = move.length();
            boolean isPromotion = false;
            int promotedPiece = 0;
            String to = "";
            String pieceDescription = "";
            String disambiguate = "";
            if ((move.charAt(rest - 1) == '+') || (move.charAt(rest - 1) == '#')) {
                move = move.substring(0, rest - 1);
                rest--;
            }
            if (move.charAt(rest - 2) == '=') {
                isPromotion = true;
                promotedPiece = BoardTools.getPieceCodeByDescription(move.substring(rest - 1, rest));
                move = move.substring(0, rest - 2);
                rest -= 2;
            }
            to = move.substring(rest - 2, rest);
            move = move.substring(0, rest - 2);
            rest -= 2;
            if (rest == 0) {
                pieceDescription = "p";
            } else {
                if (move.charAt(rest - 1) == 'x') {
                    move = move.substring(0, rest - 1);
                    rest--;
                }
                if (Character.isUpperCase(move.charAt(0))) {
                    pieceDescription = move.substring(0, 1);
                    if (rest > 1) {
                        disambiguate = move.substring(1, 2);
                    }
                } else {
                    pieceDescription = "p";
                    disambiguate = move.substring(0, 1);
                }
            }
            int pieceCode = BoardTools.getPieceCodeByDescription(pieceDescription);
            int filteredMovesCount = 0;
            if (disambiguate.equals("")) {
                for (int i = 0; i < legalMoves.length; i++) {
                    if (legalMoves[i].getStartingPiece() == pieceCode && legalMoves[i].getTo().equalsIgnoreCase(to)) {
                        if (isPromotion && legalMoves[i].getPromotion() == promotedPiece) {
                            legalMoveFound = legalMoves[i];
                            filteredMovesCount++;
                        } else if (!isPromotion) {
                            legalMoveFound = legalMoves[i];
                            filteredMovesCount++;
                        }
                    }
                }
            } else {
                if (disambiguate.length() == 2) {
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].getStartingPiece() == pieceCode && legalMoves[i].getTo().equalsIgnoreCase(to) && legalMoves[i].getFrom().equalsIgnoreCase(disambiguate)) {
                            if (isPromotion && legalMoves[i].getPromotion() == promotedPiece) {
                                legalMoveFound = legalMoves[i];
                                filteredMovesCount++;
                            } else if (!isPromotion) {
                                legalMoveFound = legalMoves[i];
                                filteredMovesCount++;
                            }
                        }
                    }
                } else {
                    char c = disambiguate.charAt(0);
                    if (Character.isDigit(c)) {
                        for (int i = 0; i < legalMoves.length; i++) {
                            if (legalMoves[i].getStartingPiece() == pieceCode && legalMoves[i].getTo().equalsIgnoreCase(to) && legalMoves[i].getFrom().endsWith(disambiguate)) {
                                if (isPromotion && legalMoves[i].getPromotion() == promotedPiece) {
                                    legalMoveFound = legalMoves[i];
                                    filteredMovesCount++;
                                } else if (!isPromotion) {
                                    legalMoveFound = legalMoves[i];
                                    filteredMovesCount++;
                                }
                            }
                        }
                    } else if (Character.isLetter(c)) {
                        for (int i = 0; i < legalMoves.length; i++) {
                            if (legalMoves[i].getStartingPiece() == pieceCode && legalMoves[i].getTo().equalsIgnoreCase(to) && legalMoves[i].getFrom().startsWith(disambiguate)) {
                                if (isPromotion && legalMoves[i].getPromotion() == promotedPiece) {
                                    legalMoveFound = legalMoves[i];
                                    filteredMovesCount++;
                                } else if (!isPromotion) {
                                    legalMoveFound = legalMoves[i];
                                    filteredMovesCount++;
                                }
                            }
                        }
                    }
                }
            }
            if (filteredMovesCount != 1) {
                throw new IllegalArgumentException("No unique move found. " + filteredMovesCount);
            }
            return legalMoveFound;
        }
    }

    public static String getSanDescription(Move move, Board board, int colorToMove) {
        MoveInput[] legalMoves = Chess.getLegalMoves(board, colorToMove);
        if (move.isCastlingLong()) {
            return "O-O-O";
        } else if (move.isCastlingShort()) {
            return "O-O";
        } else {
            String result = "";
            if (move.getStartingPiece() == Board.PIECE_PAWN) {
                String row = move.getFrom().substring(1, 2);
                boolean onSameRow = false;
                for (int i = 0; i < legalMoves.length; i++) {
                    if (!legalMoves[i].getFrom().equalsIgnoreCase(move.getFrom()) && BoardTools.getPiece(board, legalMoves[i].getFrom()) == Board.PIECE_PAWN && legalMoves[i].getTo().equalsIgnoreCase(move.getTo()) && legalMoves[i].getFrom().endsWith(row)) {
                        onSameRow = true;
                    }
                }
                String col = move.getFrom().substring(0, 1);
                boolean onSameCol = false;
                for (int i = 0; i < legalMoves.length; i++) {
                    if (!legalMoves[i].getFrom().equalsIgnoreCase(move.getFrom()) && BoardTools.getPiece(board, legalMoves[i].getFrom()) == Board.PIECE_PAWN && legalMoves[i].getTo().equalsIgnoreCase(move.getTo()) && legalMoves[i].getFrom().startsWith(col)) {
                        onSameCol = true;
                    }
                }
                boolean both = (onSameCol && onSameRow);
                boolean none = (!onSameCol && !onSameRow);
                if (none) {
                    if (move.isStrikeMove()) {
                        result += move.getFrom().substring(0, 1);
                    }
                } else if (both) {
                    result += move.getFrom();
                } else if (onSameCol) {
                    result += move.getFrom().substring(1, 2);
                } else if (onSameRow) {
                    result += move.getFrom().substring(0, 1);
                }
            } else {
                int myPiece = move.getStartingPiece();
                String row = move.getFrom().substring(1, 2);
                boolean onSameRow = false;
                for (int i = 0; i < legalMoves.length; i++) {
                    if (!legalMoves[i].getFrom().equalsIgnoreCase(move.getFrom()) && BoardTools.getPiece(board, legalMoves[i].getFrom()) == myPiece && legalMoves[i].getTo().equalsIgnoreCase(move.getTo()) && legalMoves[i].getFrom().endsWith(row)) {
                        onSameRow = true;
                    }
                }
                String col = move.getFrom().substring(0, 1);
                boolean onSameCol = false;
                for (int i = 0; i < legalMoves.length; i++) {
                    if (!legalMoves[i].getFrom().equalsIgnoreCase(move.getFrom()) && BoardTools.getPiece(board, legalMoves[i].getFrom()) == myPiece && legalMoves[i].getTo().equalsIgnoreCase(move.getTo()) && legalMoves[i].getFrom().startsWith(col)) {
                        onSameCol = true;
                    }
                }
                boolean same = false;
                for (int i = 0; i < legalMoves.length; i++) {
                    if (!legalMoves[i].getFrom().equalsIgnoreCase(move.getFrom()) && BoardTools.getPiece(board, legalMoves[i].getFrom()) == myPiece && legalMoves[i].getTo().equalsIgnoreCase(move.getTo())) {
                        same = true;
                    }
                }
                boolean both = (onSameCol && onSameRow);
                boolean none = (!onSameCol && !onSameRow && !same);
                if (none) {
                    result += BoardTools.getPieceDescriptionByPiceCode(move.getStartingPiece());
                } else if (both) {
                    result += BoardTools.getPieceDescriptionByPiceCode(move.getStartingPiece()) + move.getFrom();
                } else if (onSameCol) {
                    result += BoardTools.getPieceDescriptionByPiceCode(move.getStartingPiece()) + move.getFrom().substring(1, 2);
                } else if (onSameRow || same) {
                    result += BoardTools.getPieceDescriptionByPiceCode(move.getStartingPiece()) + move.getFrom().substring(0, 1);
                }
            }
            if (move.isStrikeMove()) {
                result += "x";
            }
            result += move.getTo();
            if (move.isPromotion()) {
                result += "=" + BoardTools.getPieceDescriptionByPiceCode(move.getPromotion());
            }
            if (move.mates()) {
                result += "#";
            } else if (move.checks()) {
                result += "+";
            }
            return result;
        }
    }

    public static String getMoveTreeDump(Move move) {
        String result = "";
        Move[] moves = move.getScoreTree();
        if (moves != null) {
            for (int i = moves.length - 1; i >= 0; i--) {
                if (moves[i] != null) {
                    result += MoveTools.getMoveDump(moves[i]) + " ";
                }
            }
        }
        return result;
    }

    public static String getMoveDump(Move move) {
        String result;
        result = move.getSanDescription();
        if (move.isScored()) {
            result += " Score: " + move.getScore();
            result += " Tree: " + getMoveTreeDump(move);
        }
        return result;
    }

    public static String getMovesDump(Move[] moves) {
        StringBuffer result = new StringBuffer("\n");
        for (int i = 0; i < moves.length; i++) {
            result.append(getMoveDump(moves[i]) + "\n");
        }
        return result.toString();
    }
}
