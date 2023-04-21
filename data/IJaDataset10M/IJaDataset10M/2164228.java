package ecbm.world;

import ecbm.entity.base.Entity;

/**
 *
 * @author ito
 */
public class WorldEntityEvent {

    protected World world;

    protected Entity entity;

    public WorldEntityEvent(World sender, Entity entity) {
        this.world = sender;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public World getWorld() {
        return world;
    }
}
