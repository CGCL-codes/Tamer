package com.bubblegumproject.keeper.model;

import com.bubblegumproject.ogo.action.ApplicationException;

/**
 * Exception indicating an entity won't be created because it already exists. This translates
 * {@link javax.persistence.EntityExistsException} into a model exception.
 *
 * @author Azubuko Obele (buko.obele@gmail.com)
 */
@ApplicationException
public class EntityAlreadyExistsException extends ModelException {

    public Object primaryKey;

    public EntityAlreadyExistsException(Object primaryKey) {
        super("entity primaryKey=" + primaryKey + " doesn't exist");
        this.primaryKey = primaryKey;
    }

    public Object getPrimaryKey() {
        return primaryKey;
    }
}
