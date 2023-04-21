package org.opencraft.server.model;

/**
 * Contains various methods handling block behavior.
 * @author Brett Russell
 * @author Graham Edgecombe
 */
public interface BlockBehaviour {

    /**
	 * Applies a behaviour to a block.
	 * @param level The level.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The block type.
	 */
    public void handlePassive(Level level, int x, int y, int z, int type);

    /**
	 * Applies a behaviour to a block.
	 * @param level The level.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The block type.
	 */
    public void handleDestroy(Level level, int x, int y, int z, int type);

    /**
	 * Applies a behaviour to a block.
	 * @param level The level.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param type The block type.
	 */
    public void handleScheduledBehaviour(Level level, int x, int y, int z, int type);
}
