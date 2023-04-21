package com.mw3d.core.event;

import com.mw3d.core.entity.Entity;

/**
 * @author Tareq doufish.
 */
public class EntityFocusEvent {

    /** The entity losing focus */
    public Entity loser;

    /** The entity gaining focus */
    public Entity gainer;

    public EntityFocusEvent(Entity loser, Entity gainer) {
        this.loser = loser;
        this.gainer = gainer;
    }
}
