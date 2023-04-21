package org.freelords.util;

import org.freelords.entity.FreelordsEntity;
import org.freelords.game.Game;

/** Wait for and react to an arbitrary (!) object being added or removed from game.
 * 
 * The object here can be any instance of a class derived from
 * {@link org.freelords.entity.FreelordsEntity}. The {@link org.freelords.game.Game}
 * class keeps a list of listeners that are called whenever new entities are
 * linked to the game. Use this class to add "hooks" to react to such events.
 *
 * Note that this class is more general in that it is informed whenever an
 * entity is added or removed. This is in contrast to the more specialized
 * {@link StackListener} that is only called for changes in armies.
 */
public interface GameListener {

    /** Called whenever an entity is added to the game.
	  *
	  * @param collection the game instance that sends the information about added stuff.
	  * @param e	the entity that has been added to the game.
	  */
    public void entityAdded(Game collection, FreelordsEntity e);

    /** Called whenever an entity is removed from the game.
	  *
	  * @param collection the game instance that sends the information about removed stuff.
	  * @param e	the entity that has been removed from the game.
	  */
    public void entityRemoved(Game collection, FreelordsEntity e);
}
