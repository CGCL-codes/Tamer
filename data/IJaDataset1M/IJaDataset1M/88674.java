package com.es.systems.logic;

import com.artemis.Entity;
import com.es.components.gamelogic.Lifespan;
import com.es.components.generic.CachedComponentsSystem;
import com.es.state.GameWorld;

public class LifespanSystem extends CachedComponentsSystem {

    public LifespanSystem() {
        super(Lifespan.class);
    }

    @Override
    protected void process(Entity entity) {
        Lifespan lifespan = (Lifespan) getSystemComponent(0, entity.getId());
        lifespan.lifespan -= world.getDelta();
        if (!lifespan.isAlive()) {
            ((GameWorld) world).expireEntity(entity);
        }
    }
}
