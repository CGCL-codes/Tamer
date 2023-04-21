package net.sourceforge.cruisecontrol.sampleproject.connectfour;

/**
 * Enumeration of possible connections of four chips. In Connect Four
 * one can win the game by connection four chips vertically, horizontally, or diagonally up/down.
 */
public class Direction {

    public static final Direction UPWARD_DIAGONAL = new Direction();

    public static final Direction HORIZONTAL = new Direction();

    public static final Direction DOWNWARD_DIAGONAL = new Direction();

    public static final Direction VERTICAL = new Direction();

    private Direction() {
    }
}
