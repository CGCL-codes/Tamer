package maggames.core.interfaces;

import maggames.core.Piece;

/**
 * This interface defines the interal representation of each basic game' board.  All games should used a game board
 * implementing this interface and customized to their particular needs.  The basic game board design assumes a tile
 * based board.  Rules regarding how pieces may move on a given board should be defined within the game engine and
 * various game interfaces.  Each tile may contain a single game piece of any of the types defined within the Piece
 * enum object.
 * 
 * @author BenjaminPLee
 * @version 1.0
 */
public interface GameBoard {

    /**
	 * Returns the Piece object found at the given coordinates.
	 * Coordinates are in the form 0...(dimmension-1).
	 * 
	 * @param x X coordinate within the game board (width)
	 * @param y Y coordinate within the game board (height)
	 * @return Piece object found at the given coordinates
	 */
    public Piece getTile(int x, int y);

    /**
	 * Places the given Piece at the given x,y coordinates on the game board.
	 * Coordinates are in the form 0...(dimmension-1).
	 * 
	 * @param x X coordinate within the game board (width)
	 * @param y Y coordinate within the game board (height)
	 * @param piece The Piece that should be placed at the above coordinates.
	 */
    public void setTile(int x, int y, Piece piece);

    /**
	 * Returns the game board's width.
	 * 
	 * @return board width
	 */
    public int getWidth();

    /**
	 * Return the game board's height
	 * 
	 * @return board height
	 */
    public int getHeight();

    /**
	 * Returns the game board as a 2 dimmensional array of Piece objects.  The array is built width X height.
	 * 
	 * @return game board as a 2D array of Piece objects (width x height)
	 */
    public Piece[][] getAsArray();
}
