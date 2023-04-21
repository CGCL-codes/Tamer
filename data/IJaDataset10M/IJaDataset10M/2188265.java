package org.modelibra.persistency;

import java.io.Serializable;
import org.modelibra.IEntities;

/**
 * IPersistentEntities interface to represent persistent entities.
 * 
 * @author Dzenan Ridjanovic
 * @version 2008-10-16
 */
public interface IPersistentEntities extends Serializable {

    /**
	 * Gets the persistent model.
	 * 
	 * @return persistent model
	 */
    public IPersistentModel getPersistentModel();

    /**
	 * Gets the entities.
	 * 
	 * @return entities
	 */
    public IEntities<?> getEntities();

    /**
	 * Loads the entities.
	 */
    public void load();

    /**
	 * Saves the entities.
	 */
    public void save();
}
