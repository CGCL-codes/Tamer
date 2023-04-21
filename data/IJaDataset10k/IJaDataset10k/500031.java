package games.midhedava.server.actions.equip;

import games.midhedava.server.entity.Entity;
import games.midhedava.server.entity.player.Player;

/**
 * source or destination object
 * 
 * @author hendrik
 */
public abstract class MoveableObject {

    /**
	 * the slot this item is in or should be placed into
	 */
    protected String slot;

    /** Player to notify about problems */
    protected final Player player;

    /**
	 * Creates a new MoveableObject
	 *
	 * @param player Player to notify about problems
	 */
    public MoveableObject(Player player) {
        this.player = player;
    }

    /**
	 * is this object valid?
	 *
	 * @return true, if the action may be preformed, false otherwise
	 */
    public abstract boolean isValid();

    /**
	 * is the owner of the slot in reach?
	 *
	 * @param entity   entity to compare to
	 * @param distance max distance
	 * @return true, if it is reachable, false otherwise
	 */
    public abstract boolean checkDistance(Entity entity, double distance);

    /**
	 * gets the name of the slot or null if there is none
	 *
	 * @return slot name
	 */
    String getSlot() {
        return slot;
    }
}
