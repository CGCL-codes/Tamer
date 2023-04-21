package org.helianto.core;

import java.io.Serializable;

/**
 * To be implemented by any entity directly related to an <code>Entity</code>.
 * 
 * @author mauriciofernandesdecastro
 */
public interface TrunkEntity extends Serializable {

    /**
	 * The owning entity.
	 */
    public Entity getEntity();
}
