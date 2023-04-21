package com.es.systems.movement;

import com.artemis.Entity;
import com.artemis.GroupManager;
import com.es.components.gamelogic.Position;
import com.es.components.generic.CachedComponentsSystem;
import com.es.systems.labels.GameTag;

public class MapBoundarySystem extends CachedComponentsSystem {

    private GroupManager groupManager;

    public MapBoundarySystem() {
        super(Position.class);
    }

    @Override
    public void initialize() {
        this.groupManager = world.getGroupManager();
    }

    @Override
    protected void process(Entity entity) {
        if (GameTag.ACTORS.equals(groupManager.getGroupOf(entity))) {
            Position pos = (Position) getSystemComponent(0, entity.getId());
            ;
            if (pos.x > 6000) {
                pos.x = pos.previousX = 6000;
            } else if (pos.x < -6000) {
                pos.x = pos.previousX = -6000;
            }
            if (pos.y > 6000) {
                pos.y = pos.previousY = 6000;
            } else if (pos.y < -6000) {
                pos.y = pos.previousY = -6000;
            }
        }
    }
}
