package org.scribble.model.admin;

import org.scribble.model.ModelReference;

/**
 * This interface represents a model change listener.
 * 
 */
public interface ModelChangeListener {

    /**
	 * This method reports a new model has been added.
	 * 
	 * @param ref The reference associated with the model
	 */
    public void modelAdded(ModelReference ref);

    /**
	 * This method reports an update to a model.
	 * 
	 * @param ref The reference associated with the model
	 */
    public void modelUpdated(ModelReference ref);

    /**
	 * This method reports an existing model has been removed.
	 * 
	 * @param ref The reference associated with the model
	 */
    public void modelRemoved(ModelReference ref);
}
