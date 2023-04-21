package tallyho.model.tile;

/**
 * Describes the behaviour of a tile that can have a direction
 */
public interface Directional {

    /**
   * The direction represented by large values of X
   */
    int BIG_X = 1;

    /**
   * The direction represented by large values of Y
   */
    int BIG_Y = 2;

    /**
   * The direction represented by small values of X
   */
    int SMALL_X = 3;

    /**
   * The direction represented by small values of Y
   */
    int SMALL_Y = 4;

    /**
   * The possible directions in which a Directional object can face
   */
    int[] DIRECTIONS = { BIG_X, BIG_Y, SMALL_X, SMALL_Y };

    /**
   * Returns the direction in which this tile is facing
   * 
   * @return one of the constants in this class
   */
    int getDirection();
}
