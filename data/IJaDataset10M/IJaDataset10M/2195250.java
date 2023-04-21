package org.ujorm.gxt.client;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Client Ujo
 * @author Pavel Ponec
 */
public interface Cujo extends ModelData {

    public CujoPropertyList readProperties();

    public <UJO extends Cujo, VALUE> VALUE get(CujoProperty<UJO, VALUE> property);

    public <UJO extends Cujo, VALUE> void set(CujoProperty<UJO, VALUE> property, VALUE value);

    /** Create a new empty instance from the same object. */
    public <T extends Cujo> T createInstance();
}
