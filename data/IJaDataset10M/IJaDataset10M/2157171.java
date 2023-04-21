package tallyho.model.tile;

/**
 * Model for a tree in Tally Ho
 */
public final class Tree extends NeutralTile {

    /**
   * Constructor
   */
    public Tree() {
        super();
    }

    /**
   * @see tallyho.model.tile.Tile#getRange()
   */
    public int getRange() {
        return 0;
    }

    /**
   * @see tallyho.model.tile.Tile#getValue()
   */
    public int getValue() {
        return 2;
    }
}
