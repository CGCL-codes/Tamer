package StratelegoGame;

/**
  * Defines an interface the piece hoard should have.
  * Like the board interface, it needs a little more work
  * @author Jace Ferguson
  * @filename GameBoardPieceHoard.java
  *
  */
public interface GameBoardPieceHoard {

    public void setDimensions(Integer rows, Integer columns);

    public void clearHoard();

    public void recalculateHoard();

    public void addPiece(GameBoardPiece piece);
}
