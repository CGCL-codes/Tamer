package de.javagimmicks.games.jotris.model;

/**
 * Defines a group of JoTris {@link Block}s which belong together
 * @see Block
 */
public interface BlockGroup {

    /**
    * Retrieves the {@link Block} at the given position inside of this {@link BlockGroup}
    * @param row the row of the {@link Block} to get
    * @param col the column of the {@link Block} to get
    * @return the {@link Block} at the given position or null if there is no {@link Block}
    */
    public Block getBlockAt(int row, int col);

    /**
    * Determines, if this {@link BlockGroup} contains a {@link Block} at the given
    * @param row the row of the position to check
    * @param col the column of the position to check
    * @return if there is a {@link Block} at the given position
    */
    public boolean hasBlockAt(int row, int col);

    /**
    * Retrieves all {@link Block}s of this {@link BlockGroup} as two-dimensional array
    * @return all {@link Block}s of this {@link BlockGroup}
    */
    public Block[][] getBlocks();

    /**
    * Retrieves the {@link Format} of this {@link BlockGroup}.
    * @return the {@link Format} of this {@link BlockGroup}
    */
    public Format getFormat();
}
