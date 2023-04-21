package games.midhedava.server.entity.portal;

import games.midhedava.server.entity.Entity;
import games.midhedava.server.entity.RPEntity;

/**
 * A OneWayPortalDestination is an invisible point where players are
 * placed when they use a portal that leads there. One cannot interact
 * with OneWayPortalDestinations in any other way.
 */
public class OneWayPortalDestination extends Portal {

    public OneWayPortalDestination() {
        put("hidden", "");
    }

    /**
	 * Cannot be used, as one way portal destinations are only destinations of
	 * other portals.
	 */
    @Override
    public void setDestination(String zone, Object number) {
        throw new IllegalArgumentException("One way portal destinations are only destinations of other portals");
    }

    @Override
    public boolean loaded() {
        return true;
    }

    /**
	 * Determine if this is an obstacle for another entity.
	 *
	 * @param	entity		The entity to check against.
	 *
	 * @return	<code>false</code>.
	 */
    @Override
    public boolean isObstacle(Entity entity) {
        return false;
    }

    @Override
    public void onUsed(RPEntity user) {
    }
}
