package com.bytopia.abalone.mechanics;

/**
 * Service class that represents a type of move made on the specific board.
 * 
 * @author Bytopia
 */
public class MoveType {

    /**
	 * Represents a move that cannot be made.
	 */
    public static final int NOMOVE = 0;

    /**
	 * Represents a leaping move.
	 */
    public static final int LEAP = 1;

    /**
	 * Represents a pushing move without interacting with enemy marbles.
	 */
    public static final int SILENTPUSH = 2;

    /**
	 * Represents a enemy pushing move.
	 */
    public static final int ENEMYPUSH = 3;

    /**
	 * Result type of the move.
	 */
    private int result;

    /**
	 * All cells that are moved with this move.
	 */
    private Group cells;

    /**
	 * Cells that are to be highlighted if this move is about to be performed.
	 */
    private Group highlightedCells;

    /**
	 * Constructs a new move type with result type only.
	 * 
	 * @param result
	 *            result type of the move
	 */
    public MoveType(int result) {
        this(result, null, null);
    }

    /**
	 * Constructs a new move type with all given parameters.
	 * 
	 * @param result
	 *            result type of the move
	 * @param group
	 *            group of all cells that are moved
	 * @param highGroup
	 *            group of cells that should be highlighted by Watcher if this
	 *            move is about to be performed.
	 */
    public MoveType(int result, Group group, Group highGroup) {
        this.result = result;
        cells = group;
        highlightedCells = highGroup;
    }

    /**
	 * Returns the result type of the move
	 * 
	 * @return result type
	 */
    public int getResult() {
        return result;
    }

    /**
	 * Returns the group of cells that are moved with this move.
	 * 
	 * @return group of cells that are to be moved
	 */
    public Group getMovedCells() {
        return cells;
    }

    /**
	 * Returns the group of cells that are to be highlighted during this move.
	 * Used by Watcher implementations.
	 * 
	 * @return group of cells that are to be highlighted
	 */
    public Group getHighlightedCells() {
        return highlightedCells;
    }
}
