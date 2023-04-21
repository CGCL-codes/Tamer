package objectmodel;

/**
 * The class containing the input methods required from a player in the game
 * @author Benda & Eizenman
 *
 */
public abstract class PlayerInput {

    protected Object sender;

    /**
	 * General c'tor (needed for the XML loading)
	 */
    public PlayerInput() {
        this.sender = null;
    }

    /**
	 * A c'tor that gets the player
	 * @param sender
	 */
    public PlayerInput(Object sender) {
        this.sender = sender;
    }

    /**
	 * Get the player's decision on whether he wants to buy a cell
	 * @param landCell - the cell on which the player landed and can now buy
	 * @return whether the player wants to buy the cell
	 */
    public abstract boolean buyCell(CellBase landCell, Player landedPlayer);

    /**
	 * Gets the player's decision on whether he wants to build a house in a city
	 * @param landCell - the city in which the player can now build a house
	 * @return The player's decision whether he wants to build a house
	 */
    public abstract boolean buildHouse(City landCell, Player landedPlayer);
}
