package gameserver.world;

import gameserver.model.gameobjects.VisibleObject;

public class NpcKnownList extends KnownList {

    /**
	 * @param owner
	 */
    public NpcKnownList(VisibleObject owner) {
        super(owner);
    }

    /**
	 * Do KnownList update.
	 */
    public void doUpdate() {
        if (owner == null || !owner.isSpawned()) clear(); else if (owner.getActiveRegion().isMapRegionActive()) super.doUpdate(); else clear();
    }
}
