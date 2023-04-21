package net.todd.games.boardgame;

import java.util.List;

public interface IUserPiecesModel {

    List<PieceInfo> getAllPieces();

    void setSelectedPiece(IPieceGroup selectedPiece);
}
