package org.igsl.app.fifteens;

/**
 *  Position in Fifteens Puzzle. Tiles with numbers are gathered into an <code>int</code> array and
 *  each tile is specified by a row and a column indices. Empty tile is defined as having a zero value.
 *  To avoid finding an empty tile its location is duplicated a pair of indices. The code does not perform
 *  any validations to check that tiles are unique and their values are limited from 0 to 15. Also for methods
 *  moveTileXXX a return to a parent position is treated as impossible. 
 */
public class Position {

    private int[][] tiles;

    private int i0, j0;

    private Position parent;

    /**
	 * Creates a position from a given array.
	 * 
	 * @param tiles array of tiles
	 */
    public Position(int[][] tiles) {
        this.parent = null;
        this.tiles = tiles;
        boolean found = false;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (tiles[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    found = true;
                    break;
                }
                if (found) break;
            }
        }
    }

    /**
	 * Create a position from a given one by moving an empty tile to a new location
	 * 
	 * @param parent base position
	 * @param i0 row for an empty tile
	 * @param j0 column for an empty tile
	 */
    private Position(Position parent, int i0, int j0) {
        this.parent = parent;
        this.tiles = new int[4][4];
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                tiles[i][j] = parent.tiles[i][j];
            }
        }
        this.i0 = i0;
        this.j0 = j0;
        tiles[parent.i0][parent.j0] = tiles[this.i0][this.j0];
        tiles[this.i0][this.j0] = 0;
    }

    /**
	 * Moves an empty tile up
	 * @return null - if the move is invalid, a new position - otherwise
	 */
    public Position moveTileUp() {
        if ((i0 > 0) && ((parent == null) || (parent.i0 != i0 - 1))) {
            return new Position(this, i0 - 1, j0);
        } else {
            return null;
        }
    }

    /**
	 * Moves an empty tile down
	 * @return null - if the move is invalid, a new position - otherwise
	 */
    public Position moveTileDown() {
        if ((i0 < 3) && ((parent == null) || (parent.i0 != i0 + 1))) {
            return new Position(this, i0 + 1, j0);
        } else {
            return null;
        }
    }

    /**
	 * Moves an empty tile left
	 * @return null - if the move is invalid, a new position - otherwise
	 */
    public Position moveTileLeft() {
        if ((j0 > 0) && ((parent == null) || (parent.j0 != j0 - 1))) {
            return new Position(this, i0, j0 - 1);
        } else {
            return null;
        }
    }

    /**
	 * Moves an empty tile right
	 * @return null - if the move is invalid, a new position - otherwise
	 */
    public Position moveTileRight() {
        if ((j0 < 3) && ((parent == null) || (parent.j0 != j0 + 1))) {
            return new Position(this, i0, j0 + 1);
        } else {
            return null;
        }
    }

    /**
	 * Checks for equality
	 * @param p position to check
	 * @return true - if tiles in a position are located in a same order, false - otherwise
	 */
    public boolean equals(Position p) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (tiles[i][j] != p.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
	 * Calculates Manhattan distance for a given position
	 * @param p a position
	 * @return <code>int</code> Manhattan value
	 */
    public int manhattanDistance(Position p) {
        int result = 0;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                boolean found = false;
                for (int i1 = 0; i1 < 4; ++i1) {
                    for (int j1 = 0; j1 < 4; ++j1) {
                        if (p.tiles[i][j] == tiles[i1][j1]) {
                            result += Math.abs(i1 - i) + Math.abs(j1 - j);
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            }
        }
        return result;
    }

    /**
	 * Returns a row and a column indices of an empty tile 
	 */
    public String toString() {
        return "(" + i0 + "," + j0 + ")";
    }
}
