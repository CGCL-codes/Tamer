package gameinfo;

import java.util.Vector;

/**
 * Provide the necessary info to configurate a board, with the element's position, 
 * board's dimension, intial time, etc. 
 * @author quique
 */
public class Level {

    /**
         * Number of the level.
         */
    private int numberOfLevel;

    /**
         * Horizontal size.
         */
    private int xDimension;

    /**
         * Vertical size.
         */
    private int yDimension;

    /**
         * Keeps the strings that identificate the elements when the board will be loaded.
         */
    private Vector initialBoard;

    /**
         * Text of the help that appears at the begining of the level.
         */
    private String initialHelp;

    /**
         * Time that the user has from the level starts.
         */
    private int initialTime;

    /**
         * 
         * @return horizontal dimension
         */
    public int getXDimension() {
        return xDimension;
    }

    /**
         * Sets the horizontal dimension.
         * @param xDimension
         */
    public void setXDimension(int xDimension) {
        this.xDimension = xDimension;
    }

    /**
         * 
         * @return vertical dimension
         */
    public int getYDimension() {
        return yDimension;
    }

    /**
         * Sets the vertical dimension.
         * @param yDimension
         */
    public void setYDimension(int yDimension) {
        this.yDimension = yDimension;
    }

    /**
         * 
         * @return the vector with the info of the elements.
         */
    public Vector getInitialBoard() {
        return initialBoard;
    }

    /**
         * Establishes the initialBoard. 
         * @param initialBoard
         */
    public void setInitialBoard(Vector initialBoard) {
        this.initialBoard = initialBoard;
    }

    /**
         * Position go from 1 to x or y dimension.
         * @param positionX x position in the board
         * @param positionY y position in the board
         * @return strings that identificates the element
         */
    public String getElement(int positionX, int positionY) {
        return (String) initialBoard.get((positionY + (xDimension * (positionX - 1))) - 1);
    }

    /**
         * 
         * @return number of the level
         */
    public int getNumberOfLevel() {
        return numberOfLevel;
    }

    /**
         * Establishes the number of the level.
         * @param numberOfLevel
         */
    public void setNumberOfLevel(int numberOfLevel) {
        this.numberOfLevel = numberOfLevel;
    }

    /**
         * 
         * @return string with the intial help
         */
    public String getInitialHelp() {
        return initialHelp;
    }

    /**
         * Sets the initial help.
         * @param initialHelp
         */
    public void setInitialHelp(String initialHelp) {
        this.initialHelp = initialHelp;
    }

    /**
         * Time in seconds.
         * @return the initial time of the level
         */
    public int getInitialTime() {
        return this.initialTime;
    }

    /**
         * Sets the initial time.
         * @param initialTime
         */
    public void setInitialTime(int initialTime) {
        this.initialTime = initialTime;
    }
}
