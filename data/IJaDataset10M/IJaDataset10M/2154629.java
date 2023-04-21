package org.helianto.core.filter.form;

import org.helianto.core.Entity;
import org.helianto.core.TrunkEntity;

/**
 * Base class to trunk (aggregated to Entity) forms.
 * 
 * @author mauriciofernandesdecastro
 */
public abstract class AbstractTrunkForm implements TrunkEntity {

    private static final long serialVersionUID = 1L;

    private Entity entity;

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
