package org.helianto.core;

import java.io.Serializable;

/**
 * To be implemented by any entity directly related to an <code>Identity</code>.
 * 
 * @author mauriciofernandesdecastro
 */
public interface PersonalEntity extends Serializable {

    /**
	 * The owning identity.
	 */
    public Identity getIdentity();
}
