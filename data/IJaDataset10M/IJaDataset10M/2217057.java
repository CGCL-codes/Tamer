package com.abiquo.framework.model.update;

import com.abiquo.framework.exception.UpdateModelException;

/**
 * All the classes who pretend to update the grid model should implement this
 * interface.
 * 
 * @author abiquo
 */
public interface IUpdateModel {

    /**
	 * Checks for model to be updated.
	 * 
	 * @return true if the model has been updated
	 * 
	 * @throws UpdateModelException
	 *             the updater can not provide any new information (any node)
	 */
    boolean isUpdate() throws UpdateModelException;

    /**
	 * Terminates the execution of the model updater. Consequences: The model
	 * won't be refreshed anymore
	 */
    void terminate();
}
