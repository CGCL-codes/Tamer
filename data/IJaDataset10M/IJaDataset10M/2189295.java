package com.chess.core;

public class Chess {

    ChessBoard chessBoard = new ChessBoard();

    Player whitePlayer, blackPlayer, turn;

    boolean isCheck;

    public Chess(Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        turn = whitePlayer;
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.ROOK), Position.A1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.KNIGHT), Position.B1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.BISHOP), Position.C1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.QUEEN), Position.D1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.KING), Position.E1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.BISHOP), Position.F1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.KNIGHT), Position.G1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.ROOK), Position.H1);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.A2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.B2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.C2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.D2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.E2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.F2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.G2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.WHITE, ChessPieceType.PAWN), Position.H2);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.A7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.B7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.C7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.D7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.E7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.F7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.G7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.PAWN), Position.H7);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.ROOK), Position.A8);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.KNIGHT), Position.B8);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.BISHOP), Position.C8);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.QUEEN), Position.D8);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.KING), Position.E8);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.BISHOP), Position.F8);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.KNIGHT), Position.G8);
        chessBoard.positionPiece(new ChessPiece(ChessPieceColor.BLACK, ChessPieceType.ROOK), Position.H8);
    }

    private boolean isRemovingCheck(Position initPos, Position targetPos) {
        return false;
    }

    private boolean isValidMove(Position initPos, Position targetPos) {
        if (initPos == targetPos || isCheck && !isRemovingCheck(initPos, targetPos)) return false;
        switch(chessBoard.getPiece(initPos).type) {
            case KING:
                return Math.abs(initPos.x - targetPos.x) > 1 || Math.abs(initPos.y - targetPos.y) > 1;
            case ROOK:
                return initPos.x == targetPos.x || initPos.y == targetPos.y;
            case BISHOP:
                return Math.abs(initPos.x - targetPos.x) == Math.abs(initPos.y - targetPos.y);
            case QUEEN:
                return initPos.x == targetPos.x || initPos.y == targetPos.y || Math.abs(initPos.x - targetPos.x) == Math.abs(initPos.y - targetPos.y);
            case KNIGHT:
                return (Math.abs(initPos.x - targetPos.x) == 1 && Math.abs(initPos.y - targetPos.y) == 2) || (Math.abs(initPos.x - targetPos.x) == 2 && Math.abs(initPos.y - targetPos.y) == 1);
            case PAWN:
                if (turn == whitePlayer) {
                    if (chessBoard.getPiece(targetPos) == null) return targetPos.x == initPos.x && (targetPos.y - initPos.y == 1 || targetPos.y - initPos.y == 2 && initPos.y == 1); else return targetPos.y - initPos.y == 1 && Math.abs(targetPos.x - initPos.x) == 1;
                } else {
                    if (chessBoard.getPiece(targetPos) == null) return initPos.x == targetPos.x && (initPos.y - targetPos.y == 1 || initPos.y - targetPos.y == 2 && initPos.y == 6); else return initPos.y - targetPos.y == 1 && Math.abs(initPos.x - targetPos.x) == 1;
                }
        }
        return false;
    }

    private boolean isCheck(Position pos) {
        return false;
    }

    public boolean move(Position initPos, Position targetPos) {
        if (isValidMove(initPos, targetPos)) {
            chessBoard.move(initPos, targetPos);
            isCheck = isCheck(targetPos);
            return true;
        }
        return false;
    }
}
