package net.todd.games.boardgame;

import javax.vecmath.Vector3f;

public interface IMoveValidator {

    void confirmMove(PieceInfo pieceInfo, Vector3f targetLocation) throws ValidMoveException;
}
