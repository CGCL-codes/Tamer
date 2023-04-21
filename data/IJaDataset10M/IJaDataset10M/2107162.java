package StratelegoGame;

import java.awt.Container;

/**
  * An interface for a stratego game board. It defines
  * some basic functions a game board would require.
  * However, it is not complete and would need work to function 
  * with other boards.
  * @author Jace Ferguson
  * @filename GameBoard.java
  *
  */
public interface GameBoard {

    public Container getBoard();

    public void setBoardDimensions(Integer rows, Integer columns);

    public void recalculateBoard();

    public void clearBoard();
}
